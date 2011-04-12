/*
Copyright (C) 2008-2011 GSyC/LibreSoft, Universidad Rey Juan Carlos.

Author: Jose Antonio Santos Cadenas <jcaden@libresoft.es>
Author: Santiago Carot-Nemesio <scarot@libresoft.es>

This program is a (FLOS) free libre and open source implementation
of a multiplatform manager device written in java according to the
ISO/IEEE 11073-20601. Manager application is designed to work in
DalvikVM over android platform.

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.

*/
package ieee_11073.part_20601.fsm.manager;

import ieee_11073.part_20601.asn1.AarqApdu;
import ieee_11073.part_20601.asn1.ApduType;
import ieee_11073.part_20601.asn1.ConfigObject;
import ieee_11073.part_20601.asn1.DataProto;
import ieee_11073.part_20601.asn1.DataProtoList;
import ieee_11073.part_20601.asn1.DataReqModeCapab;
import ieee_11073.part_20601.asn1.PhdAssociationInformation;
import ieee_11073.part_20601.fsm.StateHandler;
import ieee_11073.part_20601.fsm.Unassociated;
import ieee_11073.part_20601.phd.dim.InvalidAttributeException;
import ieee_11073.part_20601.phd.dim.manager.MDSManager;

import java.util.Collection;
import java.util.Iterator;

import es.libresoft.openhealth.Agent;
import es.libresoft.openhealth.Device;
import es.libresoft.openhealth.DeviceConfig;
import es.libresoft.openhealth.DeviceConfigCreator;
import es.libresoft.openhealth.ManagerConfig;
import es.libresoft.openhealth.error.ErrorCodes;
import es.libresoft.openhealth.events.Event;
import es.libresoft.openhealth.events.EventType;
import es.libresoft.openhealth.events.InternalEventReporter;
import es.libresoft.openhealth.events.application.ExternalEvent;
import es.libresoft.openhealth.logging.Logging;
import es.libresoft.openhealth.messages.MessageFactory;
import es.libresoft.openhealth.storage.ConfigStorage;
import es.libresoft.openhealth.storage.ConfigStorageFactory;
import es.libresoft.openhealth.storage.StorageException;
import es.libresoft.openhealth.utils.ASN1_Tools;
import es.libresoft.openhealth.utils.ASN1_Values;

public final class MUnassociated extends Unassociated {

	public MUnassociated(StateHandler handler) {
		super(handler);
	}

	@Override
	public synchronized void process(ApduType apdu) {
		if (apdu.isAarqSelected()){
			associating(apdu.getAarq());
		} else if (apdu.isAareSelected() || apdu.isRlrqSelected() || apdu.isPrstSelected()){
			state_handler.send(MessageFactory.AbrtApdu_UNDEFINED());
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public synchronized boolean processEvent(Event event) {
		if (event.getTypeOfEvent() == EventType.REC_CORRUPTED_APDU) {
			state_handler.send(MessageFactory.AbrtApdu_UNDEFINED());
			return true;
		} else if (event.getTypeOfEvent() == EventType.IND_TRANS_DESC) {
			Logging.error("2.2) IND Transport disconnect. Should indicate to application layer...");
			state_handler.changeState(new MDisconnected(state_handler));
			try {
				ExternalEvent<Boolean, Object> eevent = (ExternalEvent<Boolean, Object> ) event;
				eevent.processed(true, ErrorCodes.NO_ERROR);
			} catch (ClassCastException e) {

			}
			return true;
		} else
			return false;

		//else: Ignore
		/*
		}else if (event.getTypeOfEvent() == EventType.REQ_ASSOC_REL){
			Logging.error("2.6) REQ AssocRel. Should not happen. Ignore");
		}else if (event.getTypeOfEvent() == EventType.REQ_ASSOC_ABORT) {
			Logging.error("2.7) REQ AssocAbort. Should not happen. Ignore");
		}else{
			Logging.error("Warning: unexpected event (" + event.getTypeOfEvent() + ") arrived in disconnected state.");
		}
		*/
	}



	//----------------------------------PRIVATE--------------------------------------------------------

	//Associating state is omitted, its functionality is implemented in next private method
	private void associating(AarqApdu aarq) {
		if (areArraysEquals(aarq.getAssoc_version().getValue().getValue(),ManagerConfig.assoc_version)) {
			DataProtoList dpl = aarq.getData_proto_list();
			Iterator<DataProto> i = dpl.getValue().iterator();
			boolean selected = false;
			DataProto dp=null;
			while (!selected && i.hasNext()) {
				dp = i.next();
				/* Current version only provides support for standard data exchange protocol */
				if (dp.getData_proto_id().getValue().intValue() == ASN1_Values.DATA_PROTO_ID_20601){
					try {
						/* Get PhdAssociationInformation data structure*/
						PhdAssociationInformation phd = ASN1_Tools.decodeData(dp.getData_proto_info(),
																			PhdAssociationInformation.class,
																			Device.MDER_DEFAULT);
						if (isValidPhdAssociationInformation(phd)){
							processDeviceConnection(phd);
						}else{
							state_handler.send(MessageFactory.AareRejectApdu_NO_COMMON_PARAMETER());
						}
						selected = true;
					} catch (Exception e) {
						/* Could not get the PhdAssociationInformation: Search another DataProto in the list*/
						e.printStackTrace();
					}
				}
				/*
				 * else if (dp.getData_proto_id().getValue().intValue() == ASN1_Values.DATA_PROTO_ID_EXTERNAL)
					Logging.error("Warning: DATA_PROTO_ID_EXTERNAL configuration is not supported.");
				 */
			}

			if (!selected) {
				/* Reject because there is not common data protocol found in DataProtoList sent from the agent */
				state_handler.send(MessageFactory.AareRejectApdu_NO_COMMON_PROTOCOL());
				InternalEventReporter.error((Agent) state_handler.getMDS().getDevice(), ErrorCodes.ASSOCIATION_ERROR);
			}

		}else{
			state_handler.send(MessageFactory.AareRejectApdu_UNSUPPORTED_ASSOC_VERSION());
			InternalEventReporter.error((Agent) state_handler.getMDS().getDevice(), ErrorCodes.ASSOCIATION_ERROR);
		}
	}


	private boolean areArraysEquals (byte[] a1, byte[] a2) {
		if (a1.length != a2.length)
			return false;
		for (int i =0; i< a1.length; i++ ) {
			if (a1[i]!=a2[i])
				return false;
		}
		return true;
	}

	private boolean isValidPhdAssociationInformation (PhdAssociationInformation phd) {

		/* Check protocol version (only version 1) */
		//if (!areArraysEquals(phd.getProtocol_version().getValue().getValue(),ManagerConfig.protocol_version)){
		if ((phd.getProtocol_version().getValue().getValue()[0] &
				(ManagerConfig.PROTOCOL_VERSION1[0] | ManagerConfig.PROTOCOL_VERSION2[0])) == 0) {
			/*TODO: If there is not a common protocol version, the manager shall reject the association
			request and set the protocolVersion to all zeros.
			*/
			return false;
		}

		/* Check if MDER encoding rules bit is set */
		int bytes = 0x000000FF & phd.getEncoding_rules().getValue().getValue()[0];
		bytes = (bytes << 8) | phd.getEncoding_rules().getValue().getValue()[1];
		if ((bytes & ASN1_Values.ENC_MDER) != ASN1_Values.ENC_MDER){
			/* Not support MDER */
			return false;
		}

		/* check nomenclature version */
		bytes = phd.getEncoding_rules().getValue().getValue()[0];
		bytes = (bytes << 24);
		if ((bytes & ASN1_Values.NOMENCLATURE_VERSION1) != ASN1_Values.NOMENCLATURE_VERSION1){
			return false;
		}

		/* Check functional units */
		if ((phd.getFunctional_units().getValue().getValue()[0] | 0x00 )!=0){
			//Not supported this functionality
			return false;
		}

		/* Check system type*/
		long flags = 0;
		for (int i=0; i<phd.getSystem_type().getValue().getValue().length; i++){
			flags = (flags << 8) | (0x00000000000000FFL & phd.getSystem_type().getValue().getValue()[i]);
		}
		if ((flags & ASN1_Values.SYS_TYPE_MANAGER) == ASN1_Values.SYS_TYPE_MANAGER) {
			return false;
		}else if ((flags & ASN1_Values.SYS_TYPE_AGENT) != ASN1_Values.SYS_TYPE_AGENT){
			return false;
		}

		DataReqModeCapab drmc = phd.getData_req_mode_capab();
		if (!(drmc.getData_req_init_agent_count()==0 || drmc.getData_req_init_agent_count()==1)){
			 /* maximum number of parallel agent initiated data requests/ flows.
			  * Shall currently be set only to 0 or 1
			  */
			return false;
		}

		/* Check AttributeList */
		/*TODO: Get the information of services used to access object attributes
		Iterator<AVA_Type> i = phd.getOption_list().getValue().iterator();
		AVA_Type ava;
		while (i.hasNext()) {
			ava = i.next();
		}
		*/
		return true;
	}

	private DeviceConfig getDeviceConfiguration (PhdAssociationInformation phd, int data_proto_id) {
		DeviceConfigCreator dev_conf = new DeviceConfigCreator();
		dev_conf.setDataProtoId(data_proto_id);
		dev_conf.setPhdId(phd.getDev_config_id().getValue().intValue());

		if ((phd.getProtocol_version().getValue().getValue()[0] & ManagerConfig.PROTOCOL_VERSION2[0]) == ManagerConfig.PROTOCOL_VERSION2[0])
			dev_conf.setProtocolVersion(2);
		else
			dev_conf.setProtocolVersion(1);

		/* Check encoding rules */
		int bytes = 0x000000FF & phd.getEncoding_rules().getValue().getValue()[0];
		bytes = (bytes << 8) | phd.getEncoding_rules().getValue().getValue()[1];
		String srules = Device.MDER_DEFAULT;
		//if ((bytes & ASN1_Values.ENC_PER) == ASN1_Values.ENC_PER)
		//	srules = "PER";
		//else  if ((bytes & ASN1_Values.ENC_XER) == ASN1_Values.ENC_XER)
		//	srules = "XER"; //Not supported by binary notes
		dev_conf.setEncondigRules(srules);
		dev_conf.setNomenclatureVersion(1);

		DataReqModeCapab drmc = phd.getData_req_mode_capab();
		/*Data req mode flags*/
		dev_conf.setDataReqModeFlags(drmc.getData_req_mode_flags().getValue().getValue());

		dev_conf.setDataReqInitAgentCount(drmc.getData_req_init_agent_count());
		// Maximum number of parallel manager initiated data requests:
		dev_conf.setDataReqInitManagerCount(drmc.getData_req_init_manager_count());

		return dev_conf.getDeviceConfig();
	}

	private void processDeviceConnection(PhdAssociationInformation phd){
		int id = phd.getDev_config_id().getValue().intValue();
		DeviceConfig dev_conf = getDeviceConfiguration(phd, ASN1_Values.DATA_PROTO_ID_20601);
		ConfigStorage cs = null;

		try {
			cs = ConfigStorageFactory.getDefaultConfigStorage();
			processStoredConfiguration(phd, cs.recover(phd.getSystem_id(), dev_conf));
			return;
		} catch (Exception e) {
			Logging.debug("Not stored configuration for device, requesting configuration");
			if (cs != null)
				cs.delete(phd.getSystem_id(), dev_conf);

			if ((ASN1_Values.CONF_ID_STANDARD_CONFIG_START <= id) && (id <= ASN1_Values.CONF_ID_STANDARD_CONFIG_END)){
				// Standard Configuration
				processStandardConfiguration(phd);
				//processExtendedConfiguration(phd);
			}else if ((ASN1_Values.CONF_ID_EXTENDED_CONFIG_START <= id) && (id <= ASN1_Values.CONF_ID_EXTENDED_CONFIG_END)){
				//Extended configuration
				processUnknownConfiguration(phd);
			}
		}
	}

	private void processUnknownConfiguration(PhdAssociationInformation phd) {
		DeviceConfig dev_conf = getDeviceConfiguration(phd, ASN1_Values.DATA_PROTO_ID_20601);
		MDSManager mds = new MDSManager(phd.getSystem_id(), phd.getDev_config_id());
		mds.setStateHandler(state_handler);
		//Set device config
		mds.setDeviceConfig(dev_conf);
		//Set MDS Object
		state_handler.setMDS(mds);
		//Send AareApdu Accepted unknown config and transit to configuring state
		state_handler.send(MessageFactory.AareApdu_20601_ACCEPTED_UNKNOWN_CONFIG(dev_conf));
		state_handler.changeState(new WaitingForConfig(state_handler));

		//mds.GET(null);
	}

	private void processStoredConfiguration(PhdAssociationInformation phd, Collection<ConfigObject> data) throws InvalidAttributeException {
		DeviceConfig dev_conf = getDeviceConfiguration(phd, ASN1_Values.DATA_PROTO_ID_20601);
		MDSManager mds = new MDSManager(phd.getSystem_id(), phd.getDev_config_id());
		mds.setStateHandler(state_handler);
		//Set device config
		mds.setDeviceConfig(dev_conf);
		//Set MDS Object
		state_handler.setMDS(mds);
		// TODO: Refactor this part in a MDS factory

		try {
			mds.configureMDS(data);
		} catch (InvalidAttributeException e) {
			e.printStackTrace();
			Logging.error("Stored configuration can't be loaded, deleting the stored configuration");

			try {
				ConfigStorage cs = ConfigStorageFactory.getDefaultConfigStorage();
				cs.delete(phd.getSystem_id(), dev_conf);
			} catch (StorageException e1) {
				Logging.error("Error while the bad configuration was being deleted");
				e1.printStackTrace();
			}

			throw e;
		}
		state_handler.send(MessageFactory.AareApdu_20601_ACCEPTED(dev_conf));
		state_handler.changeState(new MOperating(state_handler));

		//mds.GET(null);
	}

	private void processStandardConfiguration(PhdAssociationInformation phd) {
		// TODO:
		Logging.debug("Standard configuration not implemented, using unknown");
		processUnknownConfiguration(phd);
	}
}

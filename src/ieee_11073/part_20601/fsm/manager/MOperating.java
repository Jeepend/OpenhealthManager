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

import java.util.List;

import ieee_11073.part_10101.Nomenclature;
import ieee_11073.part_20601.asn1.ApduType;
import ieee_11073.part_20601.asn1.DataApdu;
import ieee_11073.part_20601.asn1.EventReportArgumentSimple;
import ieee_11073.part_20601.asn1.EventReportResultSimple;
import ieee_11073.part_20601.asn1.InvokeIDType;
import ieee_11073.part_20601.asn1.PrstApdu;
import ieee_11073.part_20601.asn1.RelativeTime;
import ieee_11073.part_20601.asn1.ScanReportInfoFixed;
import ieee_11073.part_20601.asn1.ScanReportInfoVar;
import ieee_11073.part_20601.asn1.SegmentDataEvent;
import ieee_11073.part_20601.asn1.SegmentDataResult;
import ieee_11073.part_20601.asn1.DataApdu.MessageChoiceType;
import ieee_11073.part_20601.fsm.Operating;
import ieee_11073.part_20601.fsm.StateHandler;
import ieee_11073.part_20601.phd.dim.DIM;
import ieee_11073.part_20601.phd.dim.EpiCfgScanner;
import ieee_11073.part_20601.phd.dim.PM_Segment;
import ieee_11073.part_20601.phd.dim.PM_Store;
import ieee_11073.part_20601.phd.dim.DimTimeOut;
import ieee_11073.part_20601.phd.dim.PeriCfgScanner;
import ieee_11073.part_20601.phd.dim.SET_Service;
import es.libresoft.openhealth.events.Event;
import es.libresoft.openhealth.events.EventType;
import es.libresoft.openhealth.events.application.ExternalEvent;
import es.libresoft.openhealth.events.application.GetPmStoreEventData;
import es.libresoft.openhealth.events.application.SetEventData;
import es.libresoft.openhealth.messages.MessageFactory;
import es.libresoft.openhealth.utils.ASN1_Tools;
import es.libresoft.openhealth.utils.ASN1_Values;

public final class MOperating extends Operating {

	// Next interface is used to process the received PrstApdu depending of
	// the device configuration is 20601 or external

	private interface ProcessHandler {
		public void processPrstApdu(PrstApdu prst);
	};

	private ProcessHandler process;

	public MOperating(StateHandler handler) {
		super(handler);
		int data_proto_id = state_handler.getMDS().getDeviceConf().getDataProtoId();
		if (data_proto_id== ASN1_Values.DATA_PROTO_ID_20601){
			process = new ProcessHandler(){
				@Override
				public void processPrstApdu(PrstApdu prst) {
					process_20601_PrstApdu(prst);
				}
			};
		}else{
			//TODO: Add here support for data-proto-id-external...
			System.err.println("Data_Proto_id: " + data_proto_id +" is not supported");
		}
	}

	@Override
	public synchronized void process(ApduType apdu) {
		if (apdu.isPrstSelected()){
			process.processPrstApdu(apdu.getPrst());
		}else if (apdu.isRlrqSelected()) {
			state_handler.send(MessageFactory.RlreApdu_NORMAL());
			state_handler.changeState(new MUnassociated(state_handler));
		}else if(apdu.isAarqSelected() || apdu.isAareSelected() || apdu.isRlreSelected()){
			state_handler.send(MessageFactory.AbrtApdu_UNDEFINED());
			state_handler.changeState(new MUnassociated(state_handler));
		}else if(apdu.isAbrtSelected()){
			state_handler.changeState(new MUnassociated(state_handler));
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public synchronized boolean processEvent(Event event) {
		switch (event.getTypeOfEvent()) {
		case EventType.REQ_GET_PM_STORE:
			ExternalEvent<List<PM_Segment>, GetPmStoreEventData> pmEvent = (ExternalEvent<List<PM_Segment>, GetPmStoreEventData>) event;
			PM_Store pm_store = this.state_handler.getMDS().getPM_Store(pmEvent.getPrivData().getHandle());
			pm_store.GET(pmEvent);
			return true;

		case EventType.REQ_SET:
			ExternalEvent<Boolean, SetEventData> setEvent = (ExternalEvent<Boolean, SetEventData>) event;
			DIM obj = state_handler.getMDS().getObject(setEvent.getPrivData().getObjectHandle());
			try {
				SET_Service serv = (SET_Service) obj;
				serv.SET(setEvent, setEvent.getPrivData().getAttribute());
			} catch (ClassCastException e) {
				System.err.println("Set cannot be done in object: " + setEvent.getPrivData().getObjectHandle().getValue().getValue() +
							" it does not implement a SET service");
			}
			return true;

		case EventType.REQ_MDS:
			state_handler.getMDS().GET(event);
			return true;

		case EventType.IND_TRANS_DESC:
			System.err.println("2.2) IND Transport disconnect. Should indicate to application layer...");
			state_handler.changeState(new MDisconnected(state_handler));
			return true;

		case EventType.IND_TIMEOUT:
			state_handler.send(MessageFactory.AbrtApdu(event.getReason()));
			state_handler.changeState(new MUnassociated(state_handler));
			return true;

		case EventType.REQ_ASSOC_REL:
			state_handler.send(MessageFactory.RlrqApdu_NORMAL());
			state_handler.changeState(new MDisassociating(state_handler));
			return true;

		case EventType.REQ_ASSOC_ABORT:
			state_handler.send(MessageFactory.AbrtApdu_UNDEFINED());
			state_handler.changeState(new MUnassociated(state_handler));
			return true;

		default:
			return false;
		}

	}

	//----------------------------------PRIVATE--------------------------------------------------------

	private void process_20601_PrstApdu(PrstApdu prst){
		try {
			/*
			 * The DataApdu and the related structures in A.10 shall use encoding rules
			 * as negotiated during the association procedure as described in 8.7.3.1.
			 */
			processDataApdu(ASN1_Tools.decodeData(prst.getValue(),
					DataApdu.class,
					this.state_handler.getMDS().getDeviceConf().getEncondigRules()));
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("Error getting DataApdu encoded with " +
					this.state_handler.getMDS().getDeviceConf().getEncondigRules() +
					". The connection will be released.");
			state_handler.send(MessageFactory.RlrqApdu_NORMAL());
			state_handler.changeState(new MDisassociating(state_handler));
		}
	}

	private void processDataApdu(DataApdu data) {
		MessageChoiceType msg = data.getMessage();
		//Process the message received
		if (msg.isRoiv_cmip_event_reportSelected()) {
			//TODO:
			System.out.println(">> Roiv_cmip_event_report");
		}else if (msg.isRoiv_cmip_confirmed_event_reportSelected()) {
			System.out.println(">> Roiv_cmip_confirmed_event_report");
			roiv_cmip_confirmed_event_report(data);
		}else if (msg.isRoiv_cmip_getSelected()) {
			//TODO:
			System.out.println(">> Roiv_cmip_get");
		}else if (msg.isRoiv_cmip_setSelected()) {
			//TODO:
			System.out.println(">> Roiv_cmip_set");
		}else if (msg.isRoiv_cmip_confirmed_setSelected()) {
			//TODO:
			System.out.println(">> Roiv_cmip_confirmed_set");
		}else if (msg.isRoiv_cmip_actionSelected()){
			//TODO:
			System.out.println(">> Roiv_cmip_action");
		}else if (msg.isRoiv_cmip_confirmed_actionSelected()) {
			//TODO:
			System.out.println(">> Roiv_cmip_confirmed_action");
		}else if (msg.isRors_cmip_confirmed_event_reportSelected()){
			//TODO:
			System.out.println(">> Rors_cmip_confirmed_event_report");
		}else if (msg.isRors_cmip_getSelected()){
			//TODO:
			System.out.println(">> Rors_cmip_get");
		}else if (msg.isRors_cmip_confirmed_setSelected()){
			//TODO:
			System.out.println(">> Rors_cmip_confirmed_set");
		}else if (msg.isRors_cmip_confirmed_actionSelected()){
			//TODO:
			System.out.println(">> Rors_cmip_confirmed_action");
		}else if (msg.isRoerSelected()){
			//TODO:
			System.out.println(">> Roer");
		}else if (msg.isRorjSelected()){
			//TODO:
			System.out.println(">> Rorj");
		}

		DimTimeOut to = state_handler.retireTimeout(data.getInvoke_id().getValue());
		if (to != null)
			to.procResponse(data);

		//TODO search in the timeouts if is there one for this event
	}

	private void processSegmentDataEvent(InvokeIDType id, EventReportArgumentSimple event) {
		PM_Store pmstore = this.state_handler.getMDS().getPM_Store(event.getObj_handle());
		if (pmstore == null)
			return;

		RelativeTime rt = event.getEvent_time();
		System.out.println("Relative Time: " + rt.getValue().getValue().intValue());

		try {
			SegmentDataEvent sde = ASN1_Tools.decodeData(event.getEvent_info(),
									SegmentDataEvent.class,
									this.state_handler.getMDS().getDeviceConf().getEncondigRules());
			SegmentDataResult sdr = pmstore.Segment_Data_Event(sde);

			EventReportResultSimple errs = MessageFactory.genEventReportResultSimple(event, sdr,
											state_handler.getMDS().getDeviceConf().getEncondigRules());
			DataApdu data= new DataApdu();
			MessageChoiceType mct = new MessageChoiceType();
			mct.selectRors_cmip_confirmed_event_report(errs);
			data.setInvoke_id(id);
			data.setMessage(mct);

			state_handler.send(MessageFactory.composeApdu(data, state_handler.getMDS().getDeviceConf()));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void processBufScannerEvent(EventReportArgumentSimple event) {
		try {
			PeriCfgScanner scanner = (PeriCfgScanner) state_handler.getMDS().getScanner(event.getObj_handle());
			switch (event.getEvent_type().getValue().getValue()) {
			case Nomenclature.MDC_NOTI_BUF_SCAN_REPORT_VAR:
				scanner.Buf_Scan_Report_Var(event);
				break;
			case Nomenclature.MDC_NOTI_BUF_SCAN_REPORT_FIXED:
				scanner.Buf_Scan_Report_Fixed(event);
				break;
			case Nomenclature.MDC_NOTI_BUF_SCAN_REPORT_GROUPED:
				scanner.Buf_Scan_Report_Grouped(event);
				break;
			case Nomenclature.MDC_NOTI_BUF_SCAN_REPORT_MP_VAR:
				scanner.Buf_Scan_Report_MP_Var(event);
				break;
			case Nomenclature.MDC_NOTI_BUF_SCAN_REPORT_MP_FIXED:
				scanner.Buf_Scan_Report_MP_Fixed(event);
				break;
			case Nomenclature.MDC_NOTI_BUF_SCAN_REPORT_MP_GROUPED:
				scanner.Buf_Scan_Report_MP_Grouped(event);
				break;
			}
		} catch(ClassCastException e) {
			System.err.println("Only Periodic Scanners can receive Buffered scanner events");
		}
	}

	private void processUnbufScannerEvent(EventReportArgumentSimple event) {
		try {
			EpiCfgScanner scanner = (EpiCfgScanner) state_handler.getMDS().getScanner(event.getObj_handle());
			switch (event.getEvent_type().getValue().getValue()) {
			case Nomenclature.MDC_NOTI_UNBUF_SCAN_REPORT_VAR:
				scanner.Unbuf_Scan_Report_Var(event);
				break;
			case Nomenclature.MDC_NOTI_UNBUF_SCAN_REPORT_FIXED:
				scanner.Unbuf_Scan_Report_Fixed(event);
				break;
			case Nomenclature.MDC_NOTI_UNBUF_SCAN_REPORT_GROUPED:
				scanner.Unbuf_Scan_Report_Grouped(event);
				break;
			case Nomenclature.MDC_NOTI_UNBUF_SCAN_REPORT_MP_VAR:
				scanner.Unbuf_Scan_Report_MP_Var(event);
				break;
			case Nomenclature.MDC_NOTI_UNBUF_SCAN_REPORT_MP_FIXED:
				scanner.Unbuf_Scan_Report_MP_Fixed(event);
				break;
			case Nomenclature.MDC_NOTI_UNBUF_SCAN_REPORT_MP_GROUPED:
				scanner.Unbuf_Scan_Report_MP_Grouped(event);
				break;
			}
		} catch(ClassCastException e) {
			System.err.println("Only Episodic Scanner can receive UnBuffered scanner events");
		}
	}

	private void roiv_cmip_confirmed_event_report(DataApdu data) { //EventReportArgumentSimple event, MessageChoiceType msg){
		EventReportArgumentSimple event = data.getMessage().getRoiv_cmip_confirmed_event_report();
		//(A.10.3 EVENT REPORT service)
		if (event.getObj_handle().getValue().getValue().intValue() == 0){
			//obj-handle is 0 to represent the MDS
			process_MDS_Object_Event(event);
			this.state_handler.send(MessageFactory.PrstTypeResponse(data, state_handler.getMDS().getDeviceConf()));
		} else {
			switch (event.getEvent_type().getValue().getValue().intValue()) {
			case Nomenclature.MDC_NOTI_SEGMENT_DATA:
				processSegmentDataEvent(data.getInvoke_id(), event);
				break;
			case Nomenclature.MDC_NOTI_UNBUF_SCAN_REPORT_VAR:
			case Nomenclature.MDC_NOTI_UNBUF_SCAN_REPORT_FIXED:
			case Nomenclature.MDC_NOTI_UNBUF_SCAN_REPORT_GROUPED:
			case Nomenclature.MDC_NOTI_UNBUF_SCAN_REPORT_MP_VAR:
			case Nomenclature.MDC_NOTI_UNBUF_SCAN_REPORT_MP_FIXED:
			case Nomenclature.MDC_NOTI_UNBUF_SCAN_REPORT_MP_GROUPED:
				processUnbufScannerEvent(event);
				this.state_handler.send(MessageFactory.PrstTypeResponse(data, state_handler.getMDS().getDeviceConf()));
				break;
			case Nomenclature.MDC_NOTI_BUF_SCAN_REPORT_VAR:
			case Nomenclature.MDC_NOTI_BUF_SCAN_REPORT_FIXED:
			case Nomenclature.MDC_NOTI_BUF_SCAN_REPORT_GROUPED:
			case Nomenclature.MDC_NOTI_BUF_SCAN_REPORT_MP_VAR:
			case Nomenclature.MDC_NOTI_BUF_SCAN_REPORT_MP_FIXED:
			case Nomenclature.MDC_NOTI_BUF_SCAN_REPORT_MP_GROUPED:
				processBufScannerEvent(event);
				this.state_handler.send(MessageFactory.PrstTypeResponse(data, state_handler.getMDS().getDeviceConf()));
				break;
			default:
				//TODO: handle representing a scanner or PM-store object.
				System.err.println("Warning: Received Handle=" + event.getObj_handle().getValue().getValue() + " in Operating state. Ignore.");
			}
		}
	}

	private void process_MDS_Object_Event(EventReportArgumentSimple event){
		switch (event.getEvent_type().getValue().getValue().intValue()){
			case Nomenclature.MDC_NOTI_CONFIG:
				//TODO:
				System.out.println("MDC_NOTI_CONFIG");
				break;
			case Nomenclature.MDC_NOTI_SCAN_REPORT_VAR:
				mdc_noti_scan_report_var(event.getEvent_info());
				break;
			case Nomenclature.MDC_NOTI_SCAN_REPORT_FIXED:
				mdc_noti_scan_report_fixed(event.getEvent_info());
				break;
			case Nomenclature.MDC_NOTI_SCAN_REPORT_MP_VAR:
				//TODO:
				System.out.println("MDC_NOTI_SCAN_REPORT_MP_VAR");
				break;
			case Nomenclature.MDC_NOTI_SCAN_REPORT_MP_FIXED:
				//TODO:
				System.out.println("MDC_NOTI_SCAN_REPORT_MP_FIXED");
				break;
		}
	}

	private void mdc_noti_scan_report_fixed (byte[] einfo){
		try {
			ScanReportInfoFixed srif = ASN1_Tools.decodeData(einfo,
					ScanReportInfoFixed.class,
					this.state_handler.getMDS().getDeviceConf().getEncondigRules());
			this.state_handler.getMDS().MDS_Dynamic_Data_Update_Fixed(srif);
		} catch (Exception e) {
			System.out.println("Error decoding mdc_noti_scan_report_fixed");
			e.printStackTrace();
		}

	}

	private void mdc_noti_scan_report_var(byte[] einfo) {
		try {
			ScanReportInfoVar sriv = ASN1_Tools.decodeData(einfo,
					ScanReportInfoVar.class,
					this.state_handler.getMDS().getDeviceConf().getEncondigRules());
			this.state_handler.getMDS().MDS_Dynamic_Data_Update_Var(sriv);
		} catch (Exception e) {
			System.out.println("Error decoding mdc_noti_scan_report_var");
			e.printStackTrace();
		}

	}
}

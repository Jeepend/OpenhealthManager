/*
Copyright (C) 2008-2009  Santiago Carot Nemesio
email: scarot@libresoft.es
Copyright (C) 2008-2009  José Antonio Santos Cadenas
email: jcaden@libresoft.es

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


package ieee_11073.part_20601.phd.dim.manager;

import ieee_11073.part_10101.Nomenclature;
import ieee_11073.part_20601.asn1.AVA_Type;
import ieee_11073.part_20601.asn1.AbsoluteTime;
import ieee_11073.part_20601.asn1.AttrValMap;
import ieee_11073.part_20601.asn1.AttrValMapEntry;
import ieee_11073.part_20601.asn1.AttributeList;
import ieee_11073.part_20601.asn1.BasicNuObsValue;
import ieee_11073.part_20601.asn1.ConfigId;
import ieee_11073.part_20601.asn1.ConfigObject;
import ieee_11073.part_20601.asn1.ConfigReport;
import ieee_11073.part_20601.asn1.ConfigReportRsp;
import ieee_11073.part_20601.asn1.ConfigResult;
import ieee_11073.part_20601.asn1.HANDLE;
import ieee_11073.part_20601.asn1.INT_U16;
import ieee_11073.part_20601.asn1.INT_U32;
import ieee_11073.part_20601.asn1.MetricSpecSmall;
import ieee_11073.part_20601.asn1.OID_Type;
import ieee_11073.part_20601.asn1.ObservationScan;
import ieee_11073.part_20601.asn1.ObservationScanFixed;
import ieee_11073.part_20601.asn1.ScanReportInfoFixed;
import ieee_11073.part_20601.asn1.ScanReportInfoVar;
import ieee_11073.part_20601.asn1.TYPE;
import ieee_11073.part_20601.phd.dim.Attribute;
import ieee_11073.part_20601.phd.dim.InvalidAttributeException;
import ieee_11073.part_20601.phd.dim.MDS;
import ieee_11073.part_20601.phd.dim.Metric;
import ieee_11073.part_20601.phd.dim.Numeric;

import java.io.ByteArrayInputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Hashtable;
import java.util.Iterator;

import org.bn.CoderFactory;
import org.bn.IDecoder;

import es.libresoft.mdnf.FloatType;
import es.libresoft.mdnf.SFloatType;
import es.libresoft.openhealth.events.InternalEventReporter;
import es.libresoft.openhealth.events.MeasureReporter;
import es.libresoft.openhealth.events.MeasureReporterFactory;
import es.libresoft.openhealth.utils.ASN1_Tools;
import es.libresoft.openhealth.utils.ASN1_Values;
import es.libresoft.openhealth.utils.DIM_Tools;

public abstract class MDSManager extends MDS {
	  
	/**
	 * Used only in extended configuration when the agent configuration is unknown
	 */
	public MDSManager (byte[] system_id, ConfigId devConfig_id){
		super(system_id,devConfig_id);
	}
	
	public MDSManager(Hashtable<Integer, Attribute> attributeList)
		throws InvalidAttributeException {
		super(attributeList);
	}

	@Override
	public ConfigReportRsp MDS_Configuration_Event(ConfigReport config) {
		int configId = config.getConfig_report_id().getValue();
		Iterator<ConfigObject> i = config.getConfig_obj_list().getValue().iterator();
		ConfigObject confObj;
		try{
			while (i.hasNext()){
				confObj = i.next();
				//Get Attributes
				Hashtable<Integer,Attribute> attribs = getAttributes(confObj.getAttributes());
				//checkGotAttributes(attribs);
				
				//Generate attribute Handle:
				HANDLE handle = new HANDLE();
				handle.setValue(new INT_U16(new Integer
						(confObj.getObj_handle().getValue().getValue())));
				Attribute attr = new Attribute(Nomenclature.MDC_ATTR_ID_HANDLE,
						handle);
				//Set Attribute Handle to the list
				attribs.put(Nomenclature.MDC_ATTR_ID_HANDLE, attr);
				
				//checkGotAttributes(attribs);
				int classId = confObj.getObj_class().getValue().getValue();
				switch (classId) {
				case Nomenclature.MDC_MOC_VMS_MDS_SIMP : // MDS Class
					throw new Exception("Unsoportedd MDS Class");
				case Nomenclature.MDC_MOC_VMO_METRIC : // Metric Class
					throw new Exception("Unsoportedd Metric Class");
				case Nomenclature.MDC_MOC_VMO_METRIC_NU : // Numeric Class
					addNumeric(new Numeric(attribs));
					break;
				case Nomenclature.MDC_MOC_VMO_METRIC_SA_RT: // RT-SA Class
					throw new Exception("Unsoportedd RT-SA Class");
				case Nomenclature.MDC_MOC_VMO_METRIC_ENUM: // Enumeration Class
					throw new Exception("Unsoportedd Enumeration Class");
				case Nomenclature.MDC_MOC_VMO_PMSTORE: // PM-Store Class
					throw new Exception("Unsoportedd PM-Store Class");
				case Nomenclature.MDC_MOC_PM_SEGMENT: // PM-Segment Class
					throw new Exception("Unsoportedd PM-Segment Class");
				case Nomenclature.MDC_MOC_SCAN: // Scan Class
					throw new Exception("Unsoportedd Scan Class");
				case Nomenclature.MDC_MOC_SCAN_CFG: // CfgScanner Class
					throw new Exception("Unsoportedd CfgScanner Class");
				case Nomenclature.MDC_MOC_SCAN_CFG_EPI: // EpiCfgScanner Class
					throw new Exception("Unsoportedd EpiCfgScanner Class");
				case Nomenclature.MDC_MOC_SCAN_CFG_PERI: // PeriCfgScanner Class
					throw new Exception("Unsoportedd PeriCfgScanner Class");
				}
			}
			return generateConfigReportRsp(configId,
					ASN1_Values.CONF_RESULT_ACCEPTED_CONFIG);
		}catch (Exception e) {
			e.printStackTrace();
			clearObjectsFromMds();
			if ((ASN1_Values.CONF_ID_STANDARD_CONFIG_START <= configId) && (configId <= ASN1_Values.CONF_ID_STANDARD_CONFIG_END))
				//Error in standard configuration
				return generateConfigReportRsp(configId,
						ASN1_Values.CONF_RESULT_STANDARD_CONFIG_UNKNOWN);
			else return generateConfigReportRsp(configId,
					ASN1_Values.CONF_RESULT_UNSUPPORTED_CONFIG);
		}
	}
	
	@Override
	public void MDS_Dynamic_Data_Update_Fixed(ScanReportInfoFixed info) {
		try{
			String system_id = DIM_Tools.byteArrayToString(
					(byte[])getAttribute(Nomenclature.MDC_ATTR_SYS_ID).getAttributeType());
			
			Iterator<ObservationScanFixed> i= info.getObs_scan_fixed().iterator();
			ObservationScanFixed obs;
			while (i.hasNext()) {
				obs=i.next();
				
				//Get Numeric from Handle_id
				Numeric numeric = getNumeric(obs.getObj_handle());
				AttrValMap avm = (AttrValMap)numeric.getAttribute(Nomenclature.MDC_ATTR_ATTRIBUTE_VAL_MAP).getAttributeType();
				Iterator<AttrValMapEntry> it = avm.getValue().iterator();
				DataExtractor de = new DataExtractor(obs.getObs_val_data());
				MeasureReporter mr = MeasureReporterFactory.getDefaultMeasureReporter();
				addAttributesToReport(mr,numeric);
				while (it.hasNext()){
					AttrValMapEntry attr = it.next();
					int attrId = attr.getAttribute_id().getValue().getValue();
					int length = attr.getAttribute_len();
					try {
						mr.addMeasure(attrId, decodeRawData(attrId,de.getData(length)));
					}catch(Exception e){
						System.err.println("Error: Can not get attribute " + attrId);
						e.printStackTrace();
					}
				}
				InternalEventReporter.receivedMeasure(system_id, mr);
			}
		}catch (Exception e){
			e.printStackTrace();
		}
	}
	
	@Override
	public void MDS_Dynamic_Data_Update_Var(ScanReportInfoVar info) {
		try{
			String system_id = DIM_Tools.byteArrayToString(
					(byte[])getAttribute(Nomenclature.MDC_ATTR_SYS_ID).getAttributeType());
			
			Iterator<ObservationScan> i= info.getObs_scan_var().iterator();
			ObservationScan obs;
			MeasureReporter mr = MeasureReporterFactory.getDefaultMeasureReporter();
			
			while (i.hasNext()) {
				obs=i.next();
				//Get Numeric from Handle_id
				Numeric numeric = getNumeric(obs.getObj_handle());
				addAttributesToReport(mr,numeric);
				if (numeric == null)
					throw new Exception("Numeric class not found for handle: " + obs.getObj_handle().getValue().getValue());
				
				Iterator<AVA_Type> it = obs.getAttributes().getValue().iterator();	
				while (it.hasNext()){
					AVA_Type att = it.next();
					Integer att_id = att.getAttribute_id().getValue().getValue();
					byte[] att_value = att.getAttribute_value();
					mr.addMeasure(att_id, decodeRawData(att_id,att_value));
				}
				InternalEventReporter.receivedMeasure(system_id, mr);
			}
		}catch (Exception e){
			e.printStackTrace();
		}
		
	}
	
	/* Next method add additional info to report to application layer. Please, feel 
	 * free to make changes. */
	private void addAttributesToReport (MeasureReporter mr, Metric measure) {
		Attribute at;
		
		at = measure.getAttribute(Nomenclature.MDC_ATTR_ID_TYPE);
		TYPE type = (TYPE)at.getAttributeType();
		mr.set_attribute(Nomenclature.MDC_ATTR_ID_TYPE, type.getCode().getValue().getValue());
		
		at = measure.getAttribute(Nomenclature.MDC_ATTR_UNIT_CODE);
		OID_Type unit_cod = (OID_Type)at.getAttributeType();
		mr.set_attribute(Nomenclature.MDC_ATTR_UNIT_CODE, unit_cod.getValue().getValue());
	}
	/**
	 * Get data defined in the Attribute-Value-Map of the object
	 * @param <T>
	 * @param data
	 * @return
	 */
	public <T> T decodeRawData(int attrId, byte[] data) throws Exception {
		ByteArrayInputStream input = new ByteArrayInputStream(data);
		//Decode AttrValMap using accorded enc_rules
		IDecoder decoder = CoderFactory.getInstance().newDecoder(this.getDeviceConf().getEncondigRules());
		switch (attrId){
		case Nomenclature.MDC_ATTR_NU_VAL_OBS_BASIC:
			INT_U16 iu = decoder.decode(input, INT_U16.class);
			SFloatType ft = new SFloatType(iu.getValue());
			System.out.println("Measure: " + ft.doubleValueRepresentation());
			return (T)ft;
		case Nomenclature.MDC_ATTR_NU_VAL_OBS_SIMP:
			INT_U32 iu2 = decoder.decode(input, INT_U32.class);
			FloatType ft2 = new FloatType(iu2.getValue());
			System.out.println("Measure: " + ft2.doubleValueRepresentation());
			return (T)ft2;
		case Nomenclature.MDC_ATTR_TIME_STAMP_ABS:
			/*
			 * The absolute time data type specifies the time of day with a resolution of 1/100 
			 * of a second. The hour field shall be reported in 24-hr time notion (i.e., from 0 to 23).
			 * The values in the structure shall be encoded using binary coded decimal (i.e., 4-bit 
			 * nibbles). For example, the year 1996 shall be represented by the hexadecimal value 0x19 
			 * in the century field and the hexadecimal value 0x96 in the year field. This format is 
			 * easily converted to character- or integer-based representations. See AbsoluteTime 
			 * structure for details.
			 */
			final String rawDate = ASN1_Tools.getHexString(data);
			final String source = rawDate.substring(0, 4) + "/" + /*century + year(first 2Bytes)*/
					rawDate.substring(4, 6) + "/" +   /* month next 2B*/
					rawDate.substring(6, 8) + " " +   /* day next 2B */
					rawDate.substring(8, 10) + ":" +  /* hour next 2B */
					rawDate.substring(10, 12) + ":" + /* minute next 2B */
					rawDate.substring(12, 14) + ":" + /* second next 2B */
					rawDate.substring(14); /* frac-sec last 2B */
			SimpleDateFormat sdf =  new SimpleDateFormat("yy/MM/dd HH:mm:ss:SS");
			Date d = sdf.parse(source);
			System.out.println("date: " + d);
			return (T)d;
		}
		throw new Exception ("Attribute " + attrId + " unknown.");
	}
	
	//----------------------------------------PRIVATE-----------------------------------------------------------
	private Hashtable<Integer,Attribute> getAttributes (AttributeList attrList) throws Exception {
		Hashtable<Integer,Attribute> attribs = new Hashtable<Integer,Attribute>();
		Iterator<AVA_Type> it = attrList.getValue().iterator();
		AVA_Type ava;
		while (it.hasNext()){
			ava = it.next();					
			Class attrClass = DIM_Tools.getAttributeClass(ava.getAttribute_id().getValue().getValue());
			Attribute attr = new Attribute(ava.getAttribute_id().getValue().getValue(),
					ASN1_Tools.decodeData(ava.getAttribute_value(), attrClass, getDeviceConf().getEncondigRules()));
			attribs.put(new Integer(ava.getAttribute_id().getValue().getValue()), attr);
		}
		return attribs;
	}
	
	private void checkGotAttributes(Hashtable<Integer,Attribute> attribs){
		Iterator<Integer> i = attribs.keySet().iterator();
		while (i.hasNext()){
			int id = i.next();
			attribs.get(id);
			System.out.println("-----------------------");
			System.out.println("Probando atributo: " + DIM_Tools.getAttributeName(id));
			Attribute attr = attribs.get(id);
			switch (id){
			case Nomenclature.MDC_ATTR_ID_TYPE :
				TYPE t = (TYPE) attribs.get(new Integer(id)).getAttributeType();
				System.out.println("partition: " + t.getPartition().getValue());
				System.out.println("code: " + t.getCode().getValue().getValue());
				System.out.println("ok.");
				break;
			case Nomenclature.MDC_ATTR_TIME_STAMP_ABS :
				AbsoluteTime time = (AbsoluteTime) attribs.get(new Integer(id)).getAttributeType();
				System.out.println("century: " + time.getCentury().getValue());
				System.out.println("year: " + time.getYear().getValue());
				System.out.println("month: " + time.getMonth().getValue());
				System.out.println("day: "+ time.getDay().getValue());
				System.out.println("hour: " + time.getHour().getValue());
				System.out.println("minute: " + time.getMinute().getValue());
				System.out.println("second: " + time.getSecond().getValue());
				System.out.println("sec-fraction: " + time.getSec_fractions().getValue());
				System.out.println("ok.");
				break;
			case Nomenclature.MDC_ATTR_UNIT_CODE: 
				OID_Type oid = (OID_Type)attribs.get(new Integer(id)).getAttributeType();
				System.out.println("oid: " + oid.getValue().getValue());
				System.out.println("ok.");
				break;
			case Nomenclature.MDC_ATTR_METRIC_SPEC_SMALL:
				MetricSpecSmall mss = (MetricSpecSmall)attribs.get(new Integer(id)).getAttributeType();
				//System.out.println("partition: " + getHexString(mss.getValue().getValue()));
				System.out.println("ok.");
				break;
			case Nomenclature.MDC_ATTR_NU_VAL_OBS_BASIC :
				BasicNuObsValue val = (BasicNuObsValue)attribs.get(new Integer(id)).getAttributeType();
				try {
						SFloatType sf = new SFloatType(val.getValue().getValue());
						System.out.println("BasicNuObsValue: " + sf.doubleValueRepresentation());
					} catch (Exception e) {
						e.printStackTrace();
					}
				System.out.println("ok.");
				break;
			case Nomenclature.MDC_ATTR_ATTRIBUTE_VAL_MAP: 
				AttrValMap avm = (AttrValMap)attribs.get(new Integer(id)).getAttributeType();
				Iterator<AttrValMapEntry> iter = avm.getValue().iterator();
				while (iter.hasNext()){
					AttrValMapEntry entry = iter.next();
					System.out.println("--");
					System.out.println("attrib-id: " + entry.getAttribute_id().getValue().getValue());
					System.out.println("attrib-len: " + entry.getAttribute_len());
				}
				System.out.println("ok.");
				break;
			default: break;
			}
		}
	}
	
	/**
	 * Generate a response for configuration 
	 * @param result Reponse configuration
	 * @return
	 */
	private ConfigReportRsp generateConfigReportRsp (int report_id, int result) {
		ConfigReportRsp configRsp = new ConfigReportRsp();
		ConfigId confId = new ConfigId (new Integer(report_id));
		ConfigResult confResult = new ConfigResult(new Integer(result));
		configRsp.setConfig_report_id(confId);
		configRsp.setConfig_result(confResult);
		return configRsp;
	}
	
	private final class DataExtractor {
		private int index;
		private byte[] raw;
		
		public DataExtractor (byte[] raw_data){
			raw = raw_data;
			index = 0;
		}
		
		public byte[] getData (int len){
			if ((index + len)>raw.length)
				return null;
			byte[] data = new byte[len];
			for (int i = 0; i<len; i++)
				data[i]=raw[index++];
			return data;
		}
	}
}

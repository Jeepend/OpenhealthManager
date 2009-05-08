/*
Copyright (C) 2008-2009  Santiago Carot Nemesio
email: scarot@libresoft.es

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
package ieee_11073.part_104zz.manager;

import ieee_11073.part_10101.Nomenclature;
import ieee_11073.part_20601.asn1.AbsoluteTime;
import ieee_11073.part_20601.asn1.ConfigId;
import ieee_11073.part_20601.asn1.INT_U16;
import ieee_11073.part_20601.asn1.ScanReportInfoMPFixed;
import ieee_11073.part_20601.asn1.ScanReportInfoMPVar;
import ieee_11073.part_20601.asn1.ScanReportInfoVar;
import ieee_11073.part_20601.phd.dim.manager.MDSManager;

import java.io.ByteArrayInputStream;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.bn.CoderFactory;
import org.bn.IDecoder;

import es.libresoft.mdnf.SFloatType;
import es.libresoft.openhealth.utils.ASN1_Tools;

public class DS_Extended extends MDSManager {
	  
	/**
	 * Used only in extended configuration when the agent configuration is unknown
	 */
	public DS_Extended(byte[] system_id, ConfigId devConfig_id){
		super(system_id,devConfig_id);
	} 
	
	@Override
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

	@Override
	public void MDS_DATA_REQUEST() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void Set_Time() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void MDS_Dynamic_Data_Update_MP_Fixed(ScanReportInfoMPFixed info) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void MDS_Dynamic_Data_Update_MP_Var(ScanReportInfoMPVar info) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void MDS_Dynamic_Data_Update_Var(ScanReportInfoVar info) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void GET() {
		// TODO Auto-generated method stub
		
	}

}

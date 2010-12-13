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
package ieee_11073.part_20601.phd.dim.manager;

import java.util.Hashtable;

import es.libresoft.openhealth.messages.MessageFactory;
import es.libresoft.openhealth.utils.ASN1_Tools;

import ieee_11073.part_20601.asn1.ApduType;
import ieee_11073.part_20601.asn1.DataApdu;
import ieee_11073.part_20601.asn1.InvokeIDType;
import ieee_11073.part_20601.phd.dim.Attribute;
import ieee_11073.part_20601.phd.dim.DimTimeOut;
import ieee_11073.part_20601.phd.dim.InvalidAttributeException;
import ieee_11073.part_20601.phd.dim.PM_Store;
import ieee_11073.part_20601.phd.dim.TimeOut;

public class MPM_Store extends PM_Store {

	public MPM_Store(Hashtable<Integer, Attribute> attributeList)
			throws InvalidAttributeException {
		super(attributeList);
	}

	@Override
	public void Clear_Segments() {
		// TODO Auto-generated method stub

	}

	@Override
	public void Get_Segment_Info() {
		// TODO Auto-generated method stub

	}

	@Override
	public void Trig_Segment_Data_Xfer() {
		// TODO Auto-generated method stub

	}

	@Override
	public void Segment_Data_Event() {
		// TODO Auto-generated method stub

	}

	@Override
	public void GET() {
		try {
			ApduType apdu = MessageFactory.PrstRoivCmpGet(this);
			DataApdu data = ASN1_Tools.decodeData(apdu.getPrst().getValue(), DataApdu.class, getMDS().getDeviceConf().getEncondigRules());
			InvokeIDType invokeId = data.getInvoke_id();
			getMDS().getStateHandler().send(apdu);

			DimTimeOut to = new DimTimeOut(TimeOut.PM_STORE_TO_GET, invokeId.getValue(), getMDS().getStateHandler()) {

				@Override
				public void procResponse(DataApdu data) {
					System.out.println("___PROCESAR RESPUESTA TO GET_PMSOTRE");
				}

			};
			to.start();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}

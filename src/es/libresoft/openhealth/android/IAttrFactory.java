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

package es.libresoft.openhealth.android;

import android.os.Parcelable;
import es.libresoft.openhealth.android.aidl.types.IAttribute;
import es.libresoft.openhealth.android.aidl.types.IHANDLE;
import es.libresoft.openhealth.android.aidl.types.INomPartition;
import es.libresoft.openhealth.android.aidl.types.IOID_Type;
import es.libresoft.openhealth.android.aidl.types.ISystemModel;
import es.libresoft.openhealth.android.aidl.types.ITYPE;
import ieee_11073.part_20601.asn1.HANDLE;
import ieee_11073.part_20601.asn1.SystemModel;
import ieee_11073.part_20601.asn1.TYPE;


public class IAttrFactory {

	private static IHANDLE HANDLE2parcelable(HANDLE handle) {
		IHANDLE ihandle = new IHANDLE(handle.getValue().getValue());
		return ihandle;
	}

	private static ITYPE TYPE2parcelable(TYPE type) {
		INomPartition partition = new INomPartition(type.getPartition().getValue());
		IOID_Type code = new IOID_Type(type.getCode().getValue().getValue());
		ITYPE itype = new ITYPE(partition, code);
		return itype;
	}

	private static Parcelable SystemModel2parcelable(SystemModel model) {
		return new ISystemModel(new String(model.getManufacturer()), new String(model.getModel_number()));
	}

	public static final boolean getParcelableAttribute (Object asnAttr, IAttribute attr) {

		Parcelable parcel = null;

		if (attr == null)
			return false;

		if (asnAttr instanceof HANDLE)
			parcel = HANDLE2parcelable((HANDLE) asnAttr);
		else if (asnAttr instanceof TYPE)
			parcel = TYPE2parcelable((TYPE) asnAttr);
		else if (asnAttr instanceof SystemModel)
			parcel = SystemModel2parcelable((SystemModel) asnAttr);

		if (parcel != null) {
			attr.setAttr(parcel);
			return true;
		}

		System.err.println("Unknown method provided. Can't create parcelable attribute.");
		return false;
	}

}

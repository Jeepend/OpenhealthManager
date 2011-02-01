package es.libresoft.openhealth.android.types;

import ieee_11073.part_20601.asn1.HANDLE;


public class IAttrFactory {

	public static final void parcelHANDLE (HANDLE handle, IAttribute attr) {
		IHANDLE ihandle = new IHANDLE();
		ihandle.setHandle(handle.getValue().getValue());
		attr.setAttr(ihandle);
	}

	public static final void getParcelableAttribute (Object asnAttr, IAttribute attr) {

		if (asnAttr instanceof HANDLE) {
			parcelHANDLE((HANDLE) asnAttr, attr);
			return;
		}

		System.err.println("Unknown method provided. Can't create parcelable attribute.");
	}
}

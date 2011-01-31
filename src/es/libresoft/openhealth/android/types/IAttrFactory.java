package es.libresoft.openhealth.android.types;

import ieee_11073.part_20601.asn1.HANDLE;


public class IAttrFactory {

	public static final IAttribute parcelHANDLE (HANDLE handle) {
		IAttribute iattr = new IAttribute();
		IHANDLE ihandle = new IHANDLE();
		ihandle.setHandle(handle.getValue().getValue());
		iattr.setAttr(ihandle);
		return iattr;
	}

	public static final void getParcelableAttribute (Object asnAttr, IAttribute attr) {
		attr = null;

		if (asnAttr instanceof HANDLE)
			attr = parcelHANDLE((HANDLE) asnAttr);

		if (attr == null)
			System.err.println("Unknown method provided. Can't create parcelable attribute.");
	}
}

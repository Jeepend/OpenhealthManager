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

import ieee_11073.part_10101.Nomenclature;
import android.content.Context;

public class AttributeUtils {

	private static Context context = null;

	public static void setContext(Context context) {
		AttributeUtils.context = context;
	}

	public static String type2string(int attrType) {
		if (context == null)
			return "Error: Context not set, set context before use this method";

		switch (attrType) {
		case Nomenclature.MDC_ATTR_ID_TYPE:
			return context.getString(R.string.MDC_ATTR_ID_TYPE);
		case Nomenclature.MDC_ATTR_UNIT_CODE:
			return context.getString(R.string.MDC_ATTR_UNIT_CODE);
		}

		return context.getString(R.string.UNKNOWN_TYPE) + " " + attrType;
	}

	public static String value2string(int attrValue) {
		if (context == null)
			return "Error: Context not set, set context before use this method";

		switch (attrValue) {
		case Nomenclature.MDC_DIM_DEGC:
			return context.getString(R.string.MDC_DIM_DEGC);
		case Nomenclature.MDC_TEMP_BODY:
			return context.getString(R.string.MDC_TEMP_BODY);
		}

		return context.getString(R.string.UNKNOWN_VALUE) + " " + attrValue;
	}
}

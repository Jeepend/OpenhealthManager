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
		case Nomenclature.MDC_ATTR_ID_HANDLE:
			return context.getString(R.string.MDC_ATTR_ID_HANDLE);
		}

		return context.getString(R.string.UNKNOWN_TYPE) + " " + attrType;
	}

	public static String value2string(int attrType, int attrValue) {
		if (context == null)
			return "Error: Context not set, set context before use this method";

		if (attrType == Nomenclature.MDC_ATTR_UNIT_CODE) {
			switch (attrValue) {
			case Nomenclature.MDC_DIM_PERCENT:
				return context.getString(R.string.MDC_DIM_PERCENT);
			case Nomenclature.MDC_DIM_MILLI_L:
				return context.getString(R.string.MDC_DIM_MILLI_L);
			case Nomenclature.MDC_DIM_X_G:
				return context.getString(R.string.MDC_DIM_X_G);
			case Nomenclature.MDC_DIM_KILO_G:
				return context.getString(R.string.MDC_DIM_KILO_G);
			case Nomenclature.MDC_DIM_MILLI_G:
				return context.getString(R.string.MDC_DIM_MILLI_G);
			case Nomenclature.MDC_DIM_MILLI_G_PER_DL:
				return context.getString(R.string.MDC_DIM_MILLI_G_PER_DL);
			case Nomenclature.MDC_DIM_MIN:
				return context.getString(R.string.MDC_DIM_MIN);
			case Nomenclature.MDC_DIM_HR:
				return context.getString(R.string.MDC_DIM_HR);
			case Nomenclature.MDC_DIM_DAY:
				return context.getString(R.string.MDC_DIM_DAY);
			case Nomenclature.MDC_DIM_BEAT_PER_MIN:
				return context.getString(R.string.MDC_DIM_BEAT_PER_MIN);
			case Nomenclature.MDC_DIM_KILO_PASCAL:
				return context.getString(R.string.MDC_DIM_KILO_PASCAL);
			case Nomenclature.MDC_DIM_MMHG:
				return context.getString(R.string.MDC_DIM_MMHG);
			case Nomenclature.MDC_DIM_MILLI_MOLE_PER_L:
				return context.getString(R.string.MDC_DIM_MILLI_MOLE_PER_L);
			case Nomenclature.MDC_DIM_DEGC:
				return context.getString(R.string.MDC_DIM_DEGC);
			}
		} else if (attrType == Nomenclature.MDC_ATTR_ID_TYPE) {
			switch (attrValue) {
			case Nomenclature.MDC_PULS_OXIM_PULS_RATE:
				return context.getString(R.string.MDC_PULS_OXIM_PULS_RATE);
			case Nomenclature.MDC_PULS_RATE_NON_INV:
				return context.getString(R.string.MDC_PULS_RATE_NON_INV);
			case Nomenclature.MDC_PRESS_BD_NONINV:
				return context.getString(R.string.MDC_PRESS_BD_NONINV);
			case Nomenclature.MDC_PRESS_BD_NONINV_SYS:
				return context.getString(R.string.MDC_PRESS_BD_NONINV_SYS);
			case Nomenclature.MDC_PRESS_BD_NONINV_DIA:
				return context.getString(R.string.MDC_PRESS_BD_NONINV_DIA);
			case Nomenclature.MDC_PRESS_BD_NONINV_MEAN:
				return context.getString(R.string.MDC_PRESS_BD_NONINV_MEAN);
			case Nomenclature.MDC_SAT_O2_QUAL:
				return context.getString(R.string.MDC_SAT_O2_QUAL);
			case Nomenclature.MDC_TEMP_BODY:
				return context.getString(R.string.MDC_TEMP_BODY);
			case Nomenclature.MDC_PULS_OXIM_PERF_REL:
				return context.getString(R.string.MDC_PULS_OXIM_PERF_REL);
			case Nomenclature.MDC_PULS_OXIM_PLETH:
				return context.getString(R.string.MDC_PULS_OXIM_PLETH);
			case Nomenclature.MDC_PULS_OXIM_SAT_O2:
				return context.getString(R.string.MDC_PULS_OXIM_SAT_O2);
			case Nomenclature.MDC_PULS_OXIM_PULS_CHAR:
				return context.getString(R.string.MDC_PULS_OXIM_PULS_CHAR);
			case Nomenclature.MDC_PULS_OXIM_DEV_STATUS:
				return context.getString(R.string.MDC_PULS_OXIM_DEV_STATUS);
			case Nomenclature.MDC_CONC_GLU_GEN:
				return context.getString(R.string.MDC_CONC_GLU_GEN);
			case Nomenclature.MDC_CONC_GLU_CAPILLARY_WHOLEBLOOD:
				return context.getString(R.string.MDC_CONC_GLU_CAPILLARY_WHOLEBLOOD);
			case Nomenclature.MDC_CONC_GLU_CAPILLARY_PLASMA:
				return context.getString(R.string.MDC_CONC_GLU_CAPILLARY_PLASMA);
			case Nomenclature.MDC_CONC_GLU_VENOUS_WHOLEBLOOD:
				return context.getString(R.string.MDC_CONC_GLU_VENOUS_WHOLEBLOOD);
			case Nomenclature.MDC_CONC_GLU_VENOUS_PLASMA:
				return context.getString(R.string.MDC_CONC_GLU_VENOUS_PLASMA);
			case Nomenclature.MDC_CONC_GLU_ARTERIAL_WHOLEBLOOD:
				return context.getString(R.string.MDC_CONC_GLU_ARTERIAL_WHOLEBLOOD);
			case Nomenclature.MDC_CONC_GLU_ARTERIAL_PLASMA:
				return context.getString(R.string.MDC_CONC_GLU_ARTERIAL_PLASMA);
			case Nomenclature.MDC_CONC_GLU_CONTROL:
				return context.getString(R.string.MDC_CONC_GLU_CONTROL);
			case Nomenclature.MDC_CONC_GLU_ISF:
				return context.getString(R.string.MDC_CONC_GLU_ISF);
			case Nomenclature.MDC_CONC_HBA1C:
				return context.getString(R.string.MDC_CONC_HBA1C);
			case Nomenclature.MDC_TRIG:
				return context.getString(R.string.MDC_TRIG);
			case Nomenclature.MDC_TRIG_BEAT:
				return context.getString(R.string.MDC_TRIG_BEAT);
			case Nomenclature.MDC_TRIG_BEAT_MAX_INRUSH:
				return context.getString(R.string.MDC_TRIG_BEAT_MAX_INRUSH);
			case Nomenclature.MDC_MASS_BODY_ACTUAL:
				return context.getString(R.string.MDC_MASS_BODY_ACTUAL);
			case Nomenclature.MDC_BODY_FAT:
				return context.getString(R.string.MDC_BODY_FAT);
			case Nomenclature.MDC_METRIC_NOS:
				return context.getString(R.string.MDC_METRIC_NOS);
			}
		} else if (attrType == Nomenclature.MDC_ATTR_ID_HANDLE) {
			return "" + attrValue;
		}

		return context.getString(R.string.UNKNOWN_VALUE) + " " + attrValue + " for type: " + type2string(attrType);
	}
}

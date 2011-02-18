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

package es.libresoft.openhealth.events;

import ieee_11073.part_10101.Nomenclature;
import ieee_11073.part_20601.phd.dim.Attribute;
import ieee_11073.part_20601.phd.dim.DIM;

/**
 * This class provides static methods for MeasureReporter management
 *
 * @author Jose Antonio Santos Cadenas
 *
 */
public class MeasureReporterUtils {

	/**
	 * Adds additional information to report
	 *
	 * @param mr The meassure reporter
	 * @param meassure The meassure that will be added to the reporter
	 */
	public static void addAttributesToReport (MeasureReporter mr, DIM measure) {
		addAttribute(mr, measure, Nomenclature.MDC_ATTR_ID_TYPE);
		addAttribute(mr, measure, Nomenclature.MDC_ATTR_UNIT_CODE);
		addAttribute(mr, measure, Nomenclature.MDC_ATTR_ID_HANDLE);
	}

	private static void addAttribute(MeasureReporter mr, DIM obj, int attID) {
		Attribute at = obj.getAttribute(attID);
		if (at != null)
			mr.set_attribute(at);
	}
}

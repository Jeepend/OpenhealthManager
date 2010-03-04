/*
Copyright (C) 2008-2010  Santiago Carot Nemesio
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

package es.libresoft.openhealth.events;

import java.util.List;
	/**
	 * This class represent a metric measure sended from remote agent. It
	 * is used to represent a metric object from DIM in application context.
	 * @author sancane
	 *
	 */
public class MetricReport {
	int mdc_attr_id_type;
	int mdc_attr_unit_code;
	
	public MetricReport (int type, int unit_code) {
		mdc_attr_id_type = type;
		mdc_attr_unit_code = unit_code;
	}
}

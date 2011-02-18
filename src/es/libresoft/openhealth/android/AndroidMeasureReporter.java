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

import ieee_11073.part_20601.phd.dim.Attribute;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import es.libresoft.mdnf.FloatType;
import es.libresoft.mdnf.SFloatType;
import es.libresoft.openhealth.android.aidl.types.IAttribute;
import es.libresoft.openhealth.android.aidl.types.measures.IDateMeasure;
import es.libresoft.openhealth.android.aidl.types.measures.IMeasure;
import es.libresoft.openhealth.android.aidl.types.measures.IMeasureArray;
import es.libresoft.openhealth.android.aidl.types.measures.IValueMeasure;
import es.libresoft.openhealth.android.aidl.types.measures.IAgentMetric;
import es.libresoft.openhealth.events.MeasureReporter;

public class AndroidMeasureReporter implements MeasureReporter{

	IAgentMetric metric = new IAgentMetric();

	private IMeasure getMeasure(int mType, Object data) {
		if (data instanceof SFloatType){
			SFloatType sf = (SFloatType)data;
			return new IValueMeasure(mType,sf.getExponent(),sf.getMagnitude(), sf.doubleValueRepresentation(), sf.toString());
		}else if (data instanceof FloatType){
			FloatType sf = (FloatType)data;
			return new IValueMeasure(mType,sf.getExponent(),sf.getMagnitude(), sf.doubleValueRepresentation(), sf.toString());
		}else if (data instanceof Date){
			Date timestamp = (Date)data;
			return new IDateMeasure(mType,timestamp.getTime());
		}else if (data instanceof List<?>) {
			ArrayList<IMeasure> values = new ArrayList<IMeasure>();
			List<?> list = (List<?>) data;
			Iterator<?> it = list.iterator();
			while (it.hasNext()) {
				Object obj = it.next();
				IMeasure m = getMeasure(mType, obj);
				if (m != null)
					values.add(m);
			}
			return new IMeasureArray(mType, values);
		}

		System.err.println("The unknown data type " + mType + " won't be reported to the manager.");
		return null;
	}

	@Override
	public void addMeasure(int mType, Object data) {
		IMeasure m = getMeasure(mType, data);
		if (m != null)
			metric.addMeasure(m);
	}

	@Override
	public List getMeasures(){
		return metric.getMeasures();
	}

	public List getAttributes(){
		return metric.getAttributes();
	}

	@Override
	public void clearMeasures() {
		metric.clearMeasures();
	}

	@Override
	public void set_attribute(Attribute att) {
		IAttribute iAtt = new IAttribute();
		if (IAttrFactory.getParcelableAttribute(att, iAtt)) {
			metric.addAttribute(iAtt);
		}
	}

	public IAgentMetric getMetric() {
		return metric;
	}

}

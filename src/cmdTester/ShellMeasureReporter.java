package cmdTester;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


import es.libresoft.openhealth.events.MeasureReporter;

public class ShellMeasureReporter implements MeasureReporter {

	private final ArrayList<Object> measures = new ArrayList<Object>();

	@Override
	public void addMeasure(int mType, Object data) {
		measures.add(data);
	}

	@Override
	public void clearMeasures() {
		measures.clear();
	}

	@Override
	public List getMeasures() {
		return measures;
	}

	@Override
	public void set_attribute(int type, int value) {
		// TODO Auto-generated method stub

	}

	@Override
	public <T> List<T> getAttributes() {
		// TODO Auto-generated method stub
		return null;
	}

}

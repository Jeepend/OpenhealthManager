package cmdTester;

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

}

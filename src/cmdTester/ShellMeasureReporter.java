package cmdTester;

import java.util.List;

import es.libresoft.openhealth.events.MeasureReporter;

public class ShellMeasureReporter implements MeasureReporter {

	//private final ArrayList<ShellMeasure> measures = new ArrayList<ShellMeasure>();
	//private final ArrayList<Object> attributes = new ArrayList<Object>();
	AgentMetric metric = new AgentMetric();

	@Override
	public void addMeasure(int mType, Object data) {
		metric.addMeasure(new ShellMeasure(mType, data));
	}

	@Override
	public void clearMeasures() {
		metric.clearMeasures();
	}

	@Override
	public List<Object> getMeasures() {
		return metric.getMeasures();
	}

	@Override
	public void set_attribute(int type, int value) {
		metric.addAttribute(new ShellAttribute(type, value));
	}

	@Override
	public List<Object> getAttributes() {
		return metric.getAttributes();
	}

}

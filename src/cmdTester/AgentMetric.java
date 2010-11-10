package cmdTester;

import java.util.ArrayList;
import java.util.List;

public class AgentMetric {

	public ArrayList<Object> attributes = new ArrayList<Object>();
	public ArrayList<Object> measures = new ArrayList<Object>();

	public AgentMetric(){}

	public void addMeasure(Object obj) {
		measures.add(obj);
	}

	public void addAttribute(Object obj) {
		attributes.add(obj);
	}

	public List<Object> getAttributes(){
		return attributes;
	}

	public List<Object> getMeasures(){
		return measures;
	}

	public void clearMeasures() {
		measures.clear();
	}
}

package cmdTester;

public class ShellMeasure {

	private int type;
	private Object data;

	public ShellMeasure(int type, Object data) {
		this.type = type;
		this.data = data;
	}

	public int getType() {
		return type;
	}

	public Object getData() {
		return data;
	}

	public String toString(){
		return "Type: " + type + " value: " + data;
	}
}

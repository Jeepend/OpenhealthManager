package cmdTester;

public class ShellAttribute {

	private int type; /* Id type */
	private int code; /* value */

	public ShellAttribute (int type, int code){
		this.type = type;
		this.code = code;
	}

	public int getAttrId () {
		return this.type;
	}

	public int getCode () {
		return this.code;
	}

	public String toString() {
		return "Type: " + type + " code: " + code;
	}
}


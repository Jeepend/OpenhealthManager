package es.libresoft.openhealth.events;

public class ExternalEvent extends Event {
	private Object data;
	
	public ExternalEvent(int eventType, Object data) {
		super (eventType);
		this.data = data;
	}
	
	public Object getData() {
		return data;
	}
}

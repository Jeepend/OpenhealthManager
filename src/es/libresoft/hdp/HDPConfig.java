package es.libresoft.hdp;

public class HDPConfig {

	private String serveName;
	private String servDecName;
	private String provName;
	private FeatureGroup[] features;

	public HDPConfig (String srvName, String srvDecName, String provName, FeatureGroup[] features) {
		this.serveName = srvName;
		this.servDecName = srvDecName;
		this.provName = provName;
		this.features = features;
	}
}

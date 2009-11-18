package es.libresoft.hdp;

public class FeatureGroup {

	private int mdep_id;
	private Feature[] features;
	private int role;

	public FeatureGroup(Feature[] features, int role){
		this.features = features;
		this.role = role;

		//TODO: Assign the mdep_id when registring in hdp
	}
}

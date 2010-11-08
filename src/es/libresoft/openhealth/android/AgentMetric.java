package es.libresoft.openhealth.android;

import java.util.ArrayList;
import java.util.List;

import android.os.Parcel;
import android.os.Parcelable;

public class AgentMetric implements Parcelable {

	public ArrayList<Parcelable> attributes = new ArrayList<Parcelable>();
	public ArrayList<Parcelable> measures = new ArrayList<Parcelable>();

	public static final Parcelable.Creator<AgentMetric> CREATOR =
			new Parcelable.Creator<AgentMetric>() {
	    public AgentMetric createFromParcel(Parcel in) {
	        return new AgentMetric(in);
	    }

	    public AgentMetric[] newArray(int size) {
	        return new AgentMetric[size];
	    }
	};

	private AgentMetric (Parcel in){
		java.lang.ClassLoader cl = (java.lang.ClassLoader)this.getClass().getClassLoader();
		in.readList(attributes, cl);
		in.readList(measures, cl);
	}
	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	public AgentMetric(){}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeList(attributes);
		dest.writeList(measures);
	}

	public void addMeasure(Parcelable obj) {
		measures.add(obj);
	}

	public void addAttribute(Parcelable obj) {
		attributes.add(obj);
	}

	public List getAttributes(){
		return attributes;
	}

	public List getMeasures(){
		return measures;
	}

	public void clearMeasures() {
		measures.clear();
	}
}

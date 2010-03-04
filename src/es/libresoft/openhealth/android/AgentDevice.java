package es.libresoft.openhealth.android;

import android.os.Parcel;
import android.os.Parcelable;

public class AgentDevice implements Parcelable {
	private int phdid;
	private String systId;
	private String manufacturer;
	private String modelNumber;
	
	public static final Parcelable.Creator<AgentDevice> CREATOR = 
			new Parcelable.Creator<AgentDevice>() {
	    public AgentDevice createFromParcel(Parcel in) {
	        return new AgentDevice(in);
	    }
	
	    public AgentDevice[] newArray(int size) {
	        return new AgentDevice[size];
	    }
	};
	
	private AgentDevice (Parcel in) {
		phdid = in.readInt();
		systId = in.readString();
		manufacturer = in.readString();
		modelNumber = in.readString();
	}
	
	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(phdid);
		dest.writeString(systId);
		dest.writeString(manufacturer);
		dest.writeString(modelNumber);
	}
	
	public AgentDevice (int id, String systId, String manufacturer, String modelNumber) {
		this.phdid = id;
		this.systId = systId;
		this.manufacturer = manufacturer;
		this.modelNumber = modelNumber;
	}
	
	public int getPhdId () {return this.phdid;}
	public String getSystemId () {return this.systId;}
	public String getManufacturer () {return this.manufacturer;}
	public String getModelNumber () {return this.modelNumber;}
}

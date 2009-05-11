package es.libresoft.openhealth.android;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.os.Parcel;
import android.os.Parcelable;

public class AndroidDateMeasure implements Parcelable{
	
	private int measure_type;	
	//internal value representation of the date
	private Date timestamp;
	
	public static final Parcelable.Creator<AndroidDateMeasure> CREATOR = 
			new Parcelable.Creator<AndroidDateMeasure>() {
	    public AndroidDateMeasure createFromParcel(Parcel in) {
	        return new AndroidDateMeasure(in);
	    }
	
	    public AndroidDateMeasure[] newArray(int size) {
	        return new AndroidDateMeasure[size];
	    }
	};
	
	private AndroidDateMeasure (Parcel in){
    	measure_type = in.readInt();
    	timestamp.setTime(in.readLong());
    }
	
	public AndroidDateMeasure (int mType, Date timestamp){
		measure_type = mType;
		this.timestamp = timestamp;
	}
	
	public int getMeasureType(){return measure_type;}
	
	public Date getTimeStamp(){return timestamp;}
	
	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(measure_type);
		dest.writeLong(timestamp.getTime());
	}

	
	public String toString(){
		SimpleDateFormat sdf =  new SimpleDateFormat("yy/MM/dd HH:mm:ss:SS");
		return sdf.format(timestamp);
	}
}

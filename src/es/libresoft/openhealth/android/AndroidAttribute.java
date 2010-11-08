package es.libresoft.openhealth.android;

import java.io.Serializable;

import android.os.Parcel;
import android.os.Parcelable;

public class AndroidAttribute implements Parcelable {

	private int type; /* Id type */
	private int code; /* value */

	public static final Parcelable.Creator<AndroidAttribute> CREATOR =
			new Parcelable.Creator<AndroidAttribute>() {
	    public AndroidAttribute createFromParcel(Parcel in) {
	        return new AndroidAttribute(in);
	    }

	    public AndroidAttribute[] newArray(int size) {
	        return new AndroidAttribute[size];
	    }
	};

	private AndroidAttribute (Parcel in){
		type = in.readInt();
		code = in.readInt();
	}

	public AndroidAttribute (int type, int code){
		this.type = type;
		this.code = code;
	}


	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(type);
		dest.writeInt(code);
	}

	public int getAttrId () {
		return this.type;
	}

	public int getCode () {
		return this.code;
	}
}

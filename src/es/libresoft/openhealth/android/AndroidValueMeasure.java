/*
Copyright (C) 2008-2009  Santiago Carot Nemesio
email: scarot@libresoft.es

This program is a (FLOS) free libre and open source implementation
of a multiplatform manager device written in java according to the
ISO/IEEE 11073-20601. Manager application is designed to work in 
DalvikVM over android platform.

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.

*/

package es.libresoft.openhealth.android;

import es.libresoft.mdnf.FloatType;
import android.os.Parcel;
import android.os.Parcelable;

public class AndroidValueMeasure implements Parcelable {

	//Indicates the measure type 
	private int measure_type;
	
	//internal value representation by exponent and mantissa (float is not needed to be pass from ipc call)
	private int value_exp;
	private int value_mag;
	
	public static final Parcelable.Creator<AndroidValueMeasure> CREATOR = 
			new Parcelable.Creator<AndroidValueMeasure>() {
	    public AndroidValueMeasure createFromParcel(Parcel in) {
	        return new AndroidValueMeasure(in);
	    }
	
	    public AndroidValueMeasure[] newArray(int size) {
	        return new AndroidValueMeasure[size];
	    }
	};
	
	private AndroidValueMeasure (Parcel in){
    	measure_type = in.readInt();
    	value_exp = in.readInt();
    	value_mag = in.readInt();
    }
	
	public AndroidValueMeasure (int mType, int exp, int mag){
		measure_type = mType;
		value_exp = exp;
		value_mag = mag;
	}
	
	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(measure_type);
		dest.writeInt(value_exp);
		dest.writeInt(value_mag);
	}

	public FloatType getFloatType () throws Exception{
		return new FloatType(value_exp,value_mag);
	}
}

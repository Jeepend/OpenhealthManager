/*
Copyright (C) 2008-2009  Miguel Angel Tinte
email: matinte@libresoft.es

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

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class DeviceManage extends Activity {
	
	TextView system_id;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		Bundle extras  = getIntent().getExtras();
		String deviceName = "";
		if (extras != null){
			deviceName = extras.getString("agent");
		}
		//setContentView(R.layout.device_manage);
		//system_id = (TextView) findViewById(R.id.widgetagentid);
		system_id.setText(deviceName);
	}

	
//	@Override
//	protected void onStart() {
//		
//	}
}

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
		setContentView(R.layout.device_manage);
		system_id = (TextView) findViewById(R.id.widgetagentid);
		system_id.setText(deviceName);
	}

	
//	@Override
//	protected void onStart() {
//		
//	}
}

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
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class DeviceManage extends Activity {
	
	/** The primary interface we will be calling on the service. */
    IAgentRegister mService;
    
    TextView system_id;
    TextView stateView;
    TextView measureView;
    TextView dateView;
    
	String deviceName = "";
	String stateAgent = "";
	String measure = "";
	String date = "";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		Bundle extras  = getIntent().getExtras();
		if (extras != null){
			deviceName = extras.getString("agent");
		}
		setContentView(R.layout.device_manage);
		system_id = (TextView) findViewById(R.id.widgetagentid);
		system_id.setText(deviceName);
		
		// ***************************************************
	    // binding to the service
	    // ***************************************************
        bindService(new Intent(IAgentRegister.class.getName()),
        			mConnection, Context.BIND_AUTO_CREATE);
	}

	
    private Handler handler = new Handler();
    
    private Runnable doUpdateGUI = new Runnable(){
    	public void run(){
    		updateGUI();
    	}    	
    };
    
    private void updateGUI(){
    	System.out.println("en updateGUI");
    	
    	setContentView(R.layout.device_manage);
		system_id = (TextView) findViewById(R.id.widgetagentid);
		system_id.setText(deviceName);
    	
    	// updating agent state, measure and date 
    	setContentView(R.layout.device_manage);
		stateView = (TextView) findViewById(R.id.widgetstate);
		stateView.setText(deviceName);
		
		setContentView(R.layout.device_manage);
		measureView = (TextView) findViewById(R.id.widgetmeasure);
		measureView.setText(deviceName);
		
		setContentView(R.layout.device_manage);
		dateView = (TextView) findViewById(R.id.widgetdate);
		dateView.setText(deviceName);
    }
	
	
	
	/**
    * This implementation is used to receive callbacks from the remote
    * service.
    */
    private IAgentCallbackService mCallback = new IAgentCallbackService.Stub() {

		@Override
		public void agentMeasureReceived(String system_id,
				List<Measure> measures) throws RemoteException {
			System.out.println("mesaures: " + measures.size());
			Iterator<Measure> i = measures.iterator();
			Measure measure;
			while (i.hasNext()){
				measure = i.next();
				if (measure instanceof AndroidValueMeasure){
					try {
						System.out.println("valor: " + ((AndroidValueMeasure)measure).getFloatType());
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}else System.out.println("DATO NO IMPLEMENTADO!!!");
			}
			handler.post(doUpdateGUI);
			
		}

		@Override
		public void agentStateChanged(String state) throws RemoteException {
			System.out.println("Agente en estado: " + state);
			stateAgent = state;
			handler.post(doUpdateGUI);
		}

   };
   
   private ServiceConnection mConnection = new ServiceConnection() {

	@Override
	public void onServiceConnected(ComponentName name, IBinder service) {
		// TODO Auto-generated method stub
		mService = IAgentRegister.Stub.asInterface(service);
		try {
			mService.registerAgentCallback(deviceName, mCallback);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void onServiceDisconnected(ComponentName name) {
		// TODO Auto-generated method stub
		// unexpectedly disconnected -- that is, its process crashed.
		System.out.println("estoy en ServiceDisconnected");	
		mService = null;
	}
   };
    
}

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

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import es.libresoft.mdnf.FloatType;
import es.libresoft.openhealth.events.EventType;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class DeviceManage extends Activity {
	
	/** The primary interface we will be calling on the service. */
    IAgentRegister mService;
    IAgentActionService actionService;
    
    // var for interacting with the GUI  
    TextView system_id;
    TextView stateView;
    TextView measureView;
    TextView dateView;
    
    Button buttonGet;
    Button buttonSet;
    Button buttonDisconnect;
    
	String deviceName = "init";
	String stateAgent = "Operating";
	String measureAgent = "init";
	String date = "init";
	
	
	/////// LYFE CYCLE OF AN ANDROID ACTIVITY /////////
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
	    // binding to the Manager service and to the Agent Action service
	    // ***************************************************
        bindService(new Intent(IAgentRegister.class.getName()),
        			mConnection, Context.BIND_AUTO_CREATE);
        
        bindService(new Intent(IAgentActionService.class.getName()),
        			agentConnection, Context.BIND_AUTO_CREATE);
	}

	protected void onDestroy(){
		System.err.println("estoy en onDestroy deviceManage");
		try {
			mService.unregisterCallback(deviceName, mCallback);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// Detach our existing connection.
        unbindService(mConnection);
        unbindService(agentConnection);
        
        super.onDestroy();
	}
    
	protected void onResume(Bundle savedInstanceState){
		super.onResume();
		System.err.println("estoy en onResume deviceManage");
	}
	
	protected void onPause(Bundle savedInstanceState){
		super.onPause();
		System.err.println("estoy en onPause deviceManage");
        // If we have received the service, and hence registered with
        // it, then now is the time to unregister.
        if (mService != null) {
            try {
                mService.unregisterCallback(deviceName, mCallback);
            } catch (RemoteException e) {
                // There is nothing special we need to do if the service
                // has crashed.
            }
        }
	}
	
	protected void onStop(Bundle savedInstanceState){
		super.onStop();
		System.err.println("estoy en onStop deviceManage");
	}
	
	protected void onRestart(Bundle savedInstanceState){
		super.onRestart();
		System.err.println("estoy en onRestart deviceManage");
	}
	///////////////////////////////////////////////////////////////////
	
	
	// handler for updating the GUI 
    private Handler handler = new Handler();
    
    private Runnable doUpdateGUI = new Runnable(){
    	public void run(){
    		updateGUI();
    	}    	
    };
    
    private Runnable doUpdateState = new Runnable(){
    	
    	public void run(){
    		updateState();
    	}    	
    };
    
    private void updateGUI(){
    	System.out.println("en updateGUI");
    	
    	// set device name
    	setContentView(R.layout.device_manage);
		system_id = (TextView) findViewById(R.id.widgetagentid);
		system_id.setText(deviceName);
    	
    	// updating agent state, measure and date 
		stateView = (TextView) findViewById(R.id.widgetstate);
		stateView.setText(stateAgent);
		measureView = (TextView) findViewById(R.id.widgetmeasure);
		measureView.setText(measureAgent);
		dateView = (TextView) findViewById(R.id.widgetdate);
		dateView.setText(date);
		
		// add events to the button
		// Capture our button from layout
	    buttonGet = (Button)findViewById(R.id.widgetget);
	    // Register the onClick listener with the implementation above
	    buttonGet.setOnClickListener(getListener);
	    // Capture our button from layout
	    buttonSet = (Button)findViewById(R.id.widgetset);
	    // Register the onClick listener with the implementation above
	    buttonSet.setOnClickListener(setListener);
	    // Capture our button from layout
	    buttonDisconnect = (Button)findViewById(R.id.widgetdisconnect);
	    // Register the onClick listener with the implementation above
	    buttonDisconnect.setOnClickListener(disconnectListener);
    }
    
    private void updateState(){
    	System.out.println("en updateState");
    	System.out.println(stateAgent);	
    	if ("Disconected".equals(stateAgent)){
    		System.out.println("entro en disconnected !!!");
    		// updating agent state, measure and date 
    		stateView = (TextView) findViewById(R.id.widgetstate);
    		stateView.setText(stateAgent);
			// show a dialog
			showMessage("Agent disconnected");
			// disable the buttons 
			buttonDisconnect.setEnabled(false);
			buttonGet.setEnabled(false);
			buttonSet.setEnabled(false);
    	} else {
    		// updating agent state, measure and date 
    		stateView = (TextView) findViewById(R.id.widgetstate);
    		stateView.setText(stateAgent);
    	}
    }
	
    private void showMessage(CharSequence msg) {
    	Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    } 
    
    // listeners for the buttons events
    private OnClickListener getListener = new OnClickListener() {
        public void onClick(View v) {
          // do something when the button is clicked
        	try {
        		actionService.getService(deviceName);
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
    };

    private OnClickListener setListener = new OnClickListener() {
        public void onClick(View v) {
          // do something when the button is clicked
        	try {
        		actionService.setService(deviceName);
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
    };
    
    private OnClickListener disconnectListener = new OnClickListener() {
        public void onClick(View v) {
          // do something when the button is clicked
        	try {
				actionService.sendEvent(deviceName, EventType.REQ_ASSOC_REL);
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
    };
	
    
	// implementation of the agent callbacks IDL
    private IAgentCallbackService mCallback = new IAgentCallbackService.Stub() {

		@Override
		public void agentMeasureReceived(List measures) throws RemoteException {
			//System.out.println("******************** mesaures: " + measures.size());
			Iterator i = measures.iterator();
			Object measure;
			while (i.hasNext()){
				measure = i.next();
				if (measure instanceof AndroidValueMeasure){
					try {
						//System.out.println("valor: " + ((AndroidValueMeasure)measure).getFloatType());
						DecimalFormat formater = new DecimalFormat("00.00");
						FloatType aux_measure = ((AndroidValueMeasure)measure).getFloatType();
						Double doubleMeasure = aux_measure.doubleValueRepresentation();
						measureAgent = formater.format(doubleMeasure);
						handler.post(doUpdateGUI);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}else if (measure instanceof AndroidDateMeasure) {
					System.out.println("timestamp: " + (AndroidDateMeasure)measure);
					date = ((AndroidDateMeasure)measure).toString();
					handler.post(doUpdateGUI);
				}
			}
		}

		@Override
		public void agentStateChanged(String state) throws RemoteException {
			System.out.println("Agente en estado: " + state);
			stateAgent = state;
			handler.post(doUpdateState);
		}

   };
   
   
   //  implement the ServiceConnection interface 
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
   
   private ServiceConnection agentConnection = new ServiceConnection() {

	@Override
	public void onServiceConnected(ComponentName name, IBinder service) {
		// TODO Auto-generated method stub
		actionService = IAgentActionService.Stub.asInterface(service);
	}

	@Override
	public void onServiceDisconnected(ComponentName name) {
		// TODO Auto-generated method stub
		System.out.println("estoy en ServiceDisconnected");	
		actionService = null;
	}
	   
   };
    
}

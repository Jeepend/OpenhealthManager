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

import java.util.ArrayList;


import android.app.Activity;
import android.app.ListActivity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;
import android.widget.ArrayAdapter;
import android.widget.ListView;


public class DevicesActivity extends ListActivity {

	private ArrayList<String> devices; 
	/** The primary interface we will be calling on the service. */
    IManagerRegister mService;    
    
    private Handler handler = new Handler();
        
    private Runnable doUpdateGUI = new Runnable(){
    	public void run(){
    		updateGUI();
    	}    	
    };
    
    private void updateGUI(){
    	System.out.println("en updateGUI");
    	setListAdapter(new ArrayAdapter<String>(this,
	          	   	   android.R.layout.simple_list_item_1, devices));
    	getListView().setTextFilterEnabled(true);
    }
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		devices = new ArrayList<String>();
		//devices.add("prueba");  
		setListAdapter(new ArrayAdapter<String>(this,
		          	   android.R.layout.simple_list_item_1, devices));
		getListView().setTextFilterEnabled(true);

//		setContentView(R.layout.device_windows);
//		deviceList = (ListView)findViewById(android.R.id.list);
//		
//		devices = new ArrayList<String>();
//		devices.add("prueba");
//		aa = new ArrayAdapter<String>(
//				this,
//				R.layout.device_raw,
//				devices);
//
////		aa.notifyDataSetChanged();
//		setListAdapter(aa);
//		getListView().setTextFilterEnabled(true);
////		deviceList.setAdapter(aa);
////		aa.notifyDataSetChanged();
		
        bindService(new Intent(IManagerRegister.class.getName()),
        			mConnection, Context.BIND_AUTO_CREATE);
	}
	
	protected void onDestroy(){
		System.out.println("estoy en onDestroy");
		// Detach our existing connection.
		// If we have received the service, and hence registered with
        // it, then now is the time to unregister.
//        if (mService != null) {
//            try {
//                mService.unregisterCallback(mCallback);
//            } catch (RemoteException e) {
//                // There is nothing special we need to do if the service
//                // has crashed.
//            }
//        }
		try {
			mService.unregisterCallback(mCallback);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        unbindService(mConnection);
        super.onDestroy();
	}
    
	protected void onResume(Bundle savedInstanceState){
		  System.out.println("estoy en onResume");
          // If we have received the service, and hence registered with
          // it, then now is the time to unregister.
          if (mService != null) {
              try {
                  mService.unregisterCallback(mCallback);
              } catch (RemoteException e) {
                  // There is nothing special we need to do if the service
                  // has crashed.
              }
          }
          // Detach our existing connection.
          //unbindService(mConnection);
	}
	
    /**
     * This implementation is used to receive callbacks from the remote
     * service.
     */
    private IManagerCallbackService mCallback = new IManagerCallbackService.Stub() {
        /**
         * This is called by the remote service regularly to tell us about
         * new values.  Note that IPC calls are dispatched through a thread
         * pool running in each process, so the code executing here will
         * NOT be running in our main thread like most other things -- so,
         * to update the UI, we need to use a Handler to hop over there.
         */

		@Override
		public void agentConnection(String system_id) throws RemoteException {
			// TODO Auto-generated method stub
			System.out.println("Estoy en agentConnection!!!!");
			devices.add(system_id);
			handler.post(doUpdateGUI);
		}

		@Override
		public void agentDisconnection(String system_id) throws RemoteException {
			// TODO Auto-generated method stub
			System.out.println("Estoy en agentDisconnection!!!!");
			devices.remove(system_id);
			handler.post(doUpdateGUI);
		}
    };
    
    private ServiceConnection mConnection = new ServiceConnection() {

		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			// TODO Auto-generated method stub
			mService = IManagerRegister.Stub.asInterface(service);
			try {
				mService.registerCallback(mCallback);
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		@Override
		public void onServiceDisconnected(ComponentName name) {
			// TODO Auto-generated method stub
          // This is called when the connection with the service has been
          // unexpectedly disconnected -- that is, its process crashed.
		  System.out.println("estoy en ServiceDisconnected");	
          mService = null;
		}
    };
    
}

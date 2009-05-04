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
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class DevicesActivity extends Activity {

	private ListView deviceList;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.device_windows);
		deviceList = (ListView)findViewById(R.id.widget30);
		
		final ArrayList<String> devices = new ArrayList<String>();
		final ArrayAdapter<String> aa = new ArrayAdapter<String>(
				this,
				R.layout.device_raw,
				devices);
		deviceList.setAdapter(aa);
		
		devices.add(0,"Thermomether1");
		devices.add(0,"Thermomether2");
		devices.add(0,"Thermomether3");
		devices.add(0,"Thermomether4");
		aa.notifyDataSetChanged();
		
		bindService();
	}

	/** The primary interface we will be calling on the service. */
    IManagerRegister mService = new IManagerRegister(){

		@Override
		public void registerCallback(IManagerCallbackService mc)
				throws RemoteException {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void unregisterCallback(IManagerCallbackService mc)
				throws RemoteException {
			// TODO Auto-generated method stub
			
		}

		@Override
		public IBinder asBinder() {
			// TODO Auto-generated method stub
			return null;
		} 
    	
    };
    
    private void bindService(){
    	bindService(new Intent(IManagerRegister.class.getName()), mConnection, Context.BIND_AUTO_CREATE);
    }
    
    private ServiceConnection mConnection = new ServiceConnection() {

		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			// TODO Auto-generated method stub
			mService = IManagerRegister.Stub.asInterface(service);
			IManagerCallbackService mc = null;
			try {
				mService.registerCallback(mc);
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
          mService = null;
		}
    };
    


}

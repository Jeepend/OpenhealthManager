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


import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;

public class EHealth extends Activity {
    /** Called when the activity is created. */
	TextView output;
	Button button;
	ScrollView scroll;
	boolean connected = false;
	//TcpChannel tcpChannel;
	ComponentName service;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        button = (Button) this.findViewById(R.id.widget30);
        output = (TextView) this.findViewById(R.id.widget29);
        scroll =  (ScrollView) this.findViewById(R.id.widget28);
        
        button.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (!connected){
					//print("Starting server thread...");
					//tcpChannel = new TcpChannel();
					//tcpChannel.start();
					startService();
			        button.setText("Disconnect");
				}else{
					//print("Stopping ManagerService...");
					//System.out.println("Disconnecting...");
					//tcpChannel.finish();
					stopService();
					button.setText("Connect");			        
				}
				connected = !connected;
			}        	
        });
        
        println("DeviceManager is running.");
    }
    
    private void startService (){
    	service = startService(new Intent(this,DrDroid.class));
    }
    
    private void stopService (){
    	try {
			Class serviceClass = Class.forName(service.getClassName());
			stopService(new Intent(this, serviceClass));
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    }
    
    public void print (String msg) {
		this.output.setText(output.getText() + msg);
		scroll.fullScroll(ScrollView.FOCUS_DOWN);
	}
	
	public void println (String msg) {
		Log.i("THREAD","TOPRINT" + msg);
		this.output.setText(output.getText() + msg + "\n");
		scroll.fullScroll(ScrollView.FOCUS_DOWN);
	}
}
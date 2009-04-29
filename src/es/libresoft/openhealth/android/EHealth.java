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
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class EHealth extends Activity {
	
	Button button1;
	Button button2;
	boolean connected = false;
	
	/** Called when the activity is created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	
        setContentView(R.layout.main_windows);
        
        button1 = (Button) this.findViewById(R.id.widget2);
        button2 = (Button) this.findViewById(R.id.widget4);
        button1.setOnClickListener(new View.OnClickListener (){
			@Override
			public void onClick(View v) {
				if (!connected){
					startService ();
					button1.setText("Stop");
					connected=true;
					Intent intent = new Intent (EHealth.this,DevicesActivity.class);
					startActivity(intent);
				}else{
					stopService ();
					button1.setText("Start");
					connected=false;
				}
			}     	
        });
        
        button2.setOnClickListener(new View.OnClickListener (){
			@Override
			public void onClick(View v) {
				finaliceEHealth();
			}     	
        });
    }
    
    private void finaliceEHealth (){
    	this.finish();
    }
    
    private void startService (){
    	// Make sure the service is started.  It will continue running
        // until someone calls stopService().
        // We use an action code here, instead of explictly supplying
        // the component name, so that other packages can replace
        // the service.
        startService(new Intent(DrDroid.droidEvent));
    }
    
    private void stopService (){
    	// Cancel a previous call to startService().  Note that the
        // service will not actually stop at this point if there are
        // still bound clients.
        stopService(new Intent(DrDroid.droidEvent));
    	
    }
}
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


import java.util.List;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
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
        
        // checking whether the activity is already started
        ActivityManager a = (ActivityManager)getSystemService(Activity.ACTIVITY_SERVICE);
        List<RunningServiceInfo> servicesInfo= a.getRunningServices(15);
        //System.err.println("Numero servicios activos: " + servicesInfo.size());
        
        for (RunningServiceInfo ele : servicesInfo) {
        	//System.err.println("Proceso..." + ele.process.toString());
	        if (ele.process.equals("es.libresoft.openhealth.android:remote")) {
	        	if(ele.started){
	        		System.err.println("Ele..." + ele);
	        		// service already started
					Intent intent = new Intent (EHealth.this,DevicesActivity.class);
					startActivity(intent);
	        		connected=true;
	        		button1 = (Button) this.findViewById(R.id.widget2);
	        		button1.setText("Stop");
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
	                break;
	        	} else {
	        		System.err.println("service not started!!");
	        		//connected=true;
	        		button1 = (Button) this.findViewById(R.id.widget2);
	        		button1.setText("Stop");
	                button2 = (Button) this.findViewById(R.id.widget4);
	                button1.setOnClickListener(new View.OnClickListener (){
	        			@Override
	        			public void onClick(View v) {
	        				if (!connected){
	        					startService ();
	        					button1.setText("Stop");
	        					connected=true;
	        					Intent intent = new Intent (EHealth.this,DevicesActivity.class);
	        					//intent.addFlags(Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
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
	        		break;
	        	}
	        }else{
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
        					//intent.addFlags(Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
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
        } 
    }    
          
//        button1 = (Button) this.findViewById(R.id.widget2);
//        button2 = (Button) this.findViewById(R.id.widget4);
//        button1.setOnClickListener(new View.OnClickListener (){
//			@Override
//			public void onClick(View v) {
//				if (!connected){
//					startService ();
//					button1.setText("Stop");
//					connected=true;
//					Intent intent = new Intent (EHealth.this,DevicesActivity.class);
//					//intent.addFlags(Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
//					startActivity(intent);
//				}else{
//					stopService ();
//					button1.setText("Start");
//					connected=false;
//				}
//			}     	
//        });
//        
//        button2.setOnClickListener(new View.OnClickListener (){
//			@Override
//			public void onClick(View v) {
//				finaliceEHealth();
//			}     	
//        });

    
    private void finaliceEHealth (){
    	this.finish();
    }
    
    private void startService (){
    	// Make sure the service is started.  It will continue running
        // until someone calls stopService().
        // We use an action code here, instead of explictly supplying
        // the component name, so that other packages can replace
        // the service.
    	Intent intentDroid = new Intent(DrDroid.droidEvent);
    	//intentDroid.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP); 
        startService(intentDroid);
    }
    
    private void stopService (){
    	// Cancel a previous call to startService().  Note that the
        // service will not actually stop at this point if there are
        // still bound clients.
        stopService(new Intent(DrDroid.droidEvent));
    	
    }
}
/*
Copyright (C) 2008-2009  Miguel Angel Tinte
email: jfcogato@gsyc.es

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

package es.libresoft.openhealth.android.webaccess;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.DecimalFormat;
import java.util.Iterator;
import java.util.List;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.webkit.WebView;
import es.libresoft.mdnf.FloatType;
import es.libresoft.openhealth.android.AndroidDateMeasure;
import es.libresoft.openhealth.android.AndroidValueMeasure;
import es.libresoft.openhealth.android.IAgentActionService;
import es.libresoft.openhealth.android.IAgentCallbackService;
import es.libresoft.openhealth.android.IAgentRegister;
import es.libresoft.openhealth.android.R;

public class AgentActivity extends Activity	    	      
{
	
	/** The primary interface we will be calling on the service. */
    IAgentRegister mService;
    IAgentActionService actionService;

	static String deviceName = "init";
	static String stateAgent = "Operating";
	static String measureAgent = "init";
	static String date = "init";
	
	static boolean running = false;
	static Thread server;
    	
	/////// LYFE CYCLE OF AN ANDROID ACTIVITY /////////
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		Bundle extras  = getIntent().getExtras();
		if (extras != null){
			deviceName = extras.getString("agent");
		}
        setContentView(R.layout.web_view);
		WebView new_wv = (WebView) findViewById (R.id.webview);
    	new_wv.getSettings().setJavaScriptEnabled(true);
    	
        new_wv.loadUrl("file:///android_asset/index.html");
        
        bindService(new Intent(IAgentRegister.class.getName()), mConnection, Context.BIND_AUTO_CREATE);
		bindService(new Intent(IAgentActionService.class.getName()), agentConnection, Context.BIND_AUTO_CREATE);

		running = true;
		server = new Thread (){
			
			ServerSocket servsock;
			Socket sock;
			
			public void run() {
				showTemp();
			}
			private void showTemp(){

				try{
					servsock = new ServerSocket(8000);
				}catch(Exception e){}
				
				while(running){
					try{

						Log.w("Waiting...","");
						sock = servsock.accept();
						Log.w("Accepted connection : " , sock.toString());
				   
						BufferedReader br=new BufferedReader(new
								InputStreamReader(sock.getInputStream()));
						String getCallback = br.readLine().split(" ")[1].split("callback=")[1];
				   
						String js = getCallback;
						js = js + "([{\"temperature\":\"" + measureAgent +"\"";
						js = js + ",\"date\":\"" + date + "\"";
						js = js + ",\"stateAgent\":\"" + stateAgent + "\"";
						js = js + ",\"deviceName\":\"" + deviceName + "\"";
						js = js + "}]);";

						String prueba = "HTTP/1.0  200 OK\r\n";
						prueba = prueba + "Content-type: text/plain\r\n";
						prueba = prueba + "Content-length: " + js.length() + "\r\n";
						prueba = prueba + "\r\n";
						prueba = prueba + js;
					    		  
						Log.w("sending JS",".......");
				    		  			    		  
						PrintStream out = new PrintStream( sock.getOutputStream() );
						out.println(prueba);
						sock.close();
					}
				
					catch(Exception e){}
				}//end while
				
				System.out.println("closing the thread");
				try {
					servsock.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}//end showtemp

		}; //End of Thread
		
        server.start();
       
	}
///////////////////
	private IAgentCallbackService mCallback = new IAgentCallbackService.Stub() {

		@Override
		public void agentMeasureReceived(List measures) throws RemoteException {
			
			Iterator i = measures.iterator();
			Object measure;
			while (i.hasNext()){
				measure = i.next();
				if (measure instanceof AndroidValueMeasure){
					try {

						DecimalFormat formater = new DecimalFormat("00.00");
						FloatType aux_measure = ((AndroidValueMeasure)measure).getFloatType();
						Double doubleMeasure = aux_measure.doubleValueRepresentation();
						measureAgent = formater.format(doubleMeasure);
						
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}else if (measure instanceof AndroidDateMeasure) {
					AndroidDateMeasure m = (AndroidDateMeasure)measure;
					date = m.toString();
				}
			}
		}

		@Override
		public void agentStateChanged(String state) throws RemoteException {
			System.out.println("Agente en estado---: " + state);
			stateAgent = state;
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
		System.out.println("Un agente esta conectado");
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

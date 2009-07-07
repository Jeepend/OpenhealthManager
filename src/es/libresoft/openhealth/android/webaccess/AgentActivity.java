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

import java.io.IOException;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.text.DecimalFormat;
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
import android.util.Log;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;
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
    
    // var for interacting with the GUI  
    TextView system_id;
    TextView stateView;
    TextView measureView;
    TextView dateView;
    
    Boolean new_data = false;
    
    Button buttonGet;
    Button buttonSet;
    Button buttonDisconnect;
    
    String full_data = "";
	String deviceName = "init";
	String stateAgent = "Operating";
	String measureAgent = "init";
	String date = "init";
	
	public WebView wv;

	private void setWv (WebView wv){
    	this.wv = wv;
    }
	
	private Handler handler = new Handler();
		
	private Runnable doGetData = new Runnable(){
	   	public void run(){
	   		getData();
	   	}    	
	};
    public void getData(){

   		Log.i("------------", measureAgent);
   		//full_data = "[measureAgent]:" + measureAgent + ",[deviceName]:" + deviceName + ",[stateAgent]:" + stateAgent + ",[date]:" + date ;
		//wv.loadUrl("javascript:successCallback('" + full_data +"')");

   	}
    public String getMeasureAgent(){
    	return measureAgent;
    }
    	
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
        //setWv(new_wv);
		
    	// ***************************************************
	    // binding to the Manager service and to the Agent Action service
	    // ***************************************************
		//bindService(new Intent(IAgentRegister.class.getName()),
    	//		mConnection, Context.BIND_AUTO_CREATE);
    
		//bindService(new Intent(IAgentActionService.class.getName()),
    	//		agentConnection, Context.BIND_AUTO_CREATE);

		Thread server = new Thread (){
			
			String my_temp = "init";
			ServerSocket servsock;
			Socket sock;
			
			public void run() {
				
					   bindService(new Intent(IAgentRegister.class.getName()),
				    			mConnection, Context.BIND_AUTO_CREATE);
				    
						bindService(new Intent(IAgentActionService.class.getName()),
				    			agentConnection, Context.BIND_AUTO_CREATE);
						
						
						
			}
			private void showTemp(){
				try{
				   
				   //this.sleep(5000L);
					
				   servsock = new ServerSocket(8000);
				   
				   Log.w("Waiting...","");
				   sock = servsock.accept();
				   Log.w("Accepted connection : " , sock.toString());

				   String js = "var temperature = " + my_temp + "\n";
				   js = js + "function getData(){\n";
				   js = js + "    return(temperature);\n";
				   js = js + "}";
					    	  
				   String prueba = "HTTP/1.0  200 OK\r\n";
				   prueba = prueba + "Content-type: text/plain\r\n";
				   prueba = prueba + "Content-length: " + js.length() + "\r\n";
				   prueba = prueba + "\r\n";
				   prueba = prueba + js;
					    		  
				   Log.w("sending JS",".......");
				    		  			    		  
				   PrintStream out = new PrintStream( sock.getOutputStream() );
				   out.println(prueba);
				   sock.close();
				   servsock.close();
				}
				
				catch(Exception e){}
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
								//System.out.println("valor: " + ((AndroidValueMeasure)measure).getFloatType());
								DecimalFormat formater = new DecimalFormat("00.00");
								FloatType aux_measure = ((AndroidValueMeasure)measure).getFloatType();
								Double doubleMeasure = aux_measure.doubleValueRepresentation();
								my_temp = formater.format(doubleMeasure);
													
								try{
									showTemp();
								}
								catch (Exception e){
									//there are another showTemp
								}
								//handler.post(doGetData);
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}else if (measure instanceof AndroidDateMeasure) {
							AndroidDateMeasure m = (AndroidDateMeasure)measure;
							date = m.toString();
							//handler.post(doUpdateGUI);
						}
					}
					new_data = true;
				}

				@Override
				public void agentStateChanged(String state) throws RemoteException {
					System.out.println("Agente en estado: ");
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
			
		};
		//MyServer server = new MyServer();
        server.start();
        
        //MyClient client = new MyClient();
        //client.start();
       
	}
	 
	
   /*
   public class MyClient extends Thread {
		@Override
		public void run() {
			for(;;){
				try {
					this.sleep(5000L);
					Log.w("Conectando","CLIENTE");
					Socket c = new Socket("localhost",8000);
					String s="hola socket!";
					byte[] b = new byte[200];
					c.getInputStream().read(b);
					String salida="";
					for (int i=0;i<199;i++)
						salida+=(char)b[i];
					Log.w("Recivido:", salida);
					
					//c.getOutputStream().write(s.getBytes());
					c.close();
				} catch (UnknownHostException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
		}
	}*/
    
}

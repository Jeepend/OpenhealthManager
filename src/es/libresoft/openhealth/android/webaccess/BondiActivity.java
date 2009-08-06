package es.libresoft.openhealth.android.webaccess;

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
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.TextView;
import es.libresoft.openhealth.android.DrDroid;
import es.libresoft.openhealth.android.IAgentActionService;
import es.libresoft.openhealth.android.IAgentRegister;
import es.libresoft.openhealth.android.IManagerCallbackService;
import es.libresoft.openhealth.android.IManagerRegister;
import es.libresoft.openhealth.android.R;


public class BondiActivity extends Activity {
	
	String deviceName = "init";
	String stateAgent = "Operating";
	String measureAgent = "init";
	String date = "init";
	
	//Agent agent;
	
	public String new_device = "init";
	public WebView wv;
	/** The primary interface we will be calling on the service. */
    
	IManagerRegister mService;
	IAgentRegister mService2;
    IAgentActionService actionService;
	
    private Handler handler = new Handler();

    private Runnable doUpdateGUI = new Runnable(){
    	public void run(){
    		updateGUI();
    	}    	
    };
    
    private void updateGUI(){

    	System.out.println("en updateGUI");
    	System.out.println(new_device);

    	Intent intent = new Intent(BondiActivity.this, AgentActivity.class);
    	intent.putExtra("agent", new_device);
		startActivity(intent);

    }

	@Override
	public void onCreate(Bundle icicle){
		super.onCreate(icicle);
        setContentView(R.layout.web_view);
        WebView my_wv = (WebView) findViewById (R.id.webview);

        startService(new Intent(DrDroid.droidEvent));
  
        bindService(new Intent(IManagerRegister.class.getName()),mConnection, Context.BIND_AUTO_CREATE);

        TextView tv = new TextView(this);
        tv.setText("Waiting agent connection...");
        setContentView(tv);

	}

    private IManagerCallbackService mCallback = new IManagerCallbackService.Stub() {
        

		@Override
		public void agentConnection(String system_id) throws RemoteException {
			// TODO Auto-generated method stub
			System.out.println("Estoy en agentConnection!!!!");
			Log.i("XXXXXXXXXXX", system_id);
			new_device = system_id;
			handler.post(doUpdateGUI);
		}

		@Override
		public void agentDisconnection(String system_id) throws RemoteException {
			// TODO Auto-generated method stub
			System.out.println("Estoy en agentDisconnection!!!!");
			//devices.remove(system_id);
			//handler.post(doEraseAgent);
			System.out.println("Saliendo de agentDisconnection");
		}
    };
    
    private ServiceConnection mConnection = new ServiceConnection() {

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

		public void onServiceDisconnected(ComponentName name) {
			// TODO Auto-generated method stub
          // This is called when the connection with the service has been
          // unexpectedly disconnected -- that is, its process crashed.
		  System.out.println("estoy en ServiceDisconnected");	
          mService = null;
		}
    };

}
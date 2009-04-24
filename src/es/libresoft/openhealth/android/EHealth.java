package es.libresoft.openhealth.android;


import android.app.Activity;
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
	AndroidServerThread tcpChannel;
	
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
					print("Starting server thread...");
					tcpChannel = new AndroidServerThread();
					tcpChannel.start();
			        button.setText("Disconnect");
				}else{
					print("Stopping ManagerService...");
					System.out.println("Disconnecting...");
					tcpChannel.finish();
					button.setText("Connect");			        
				}
				connected = !connected;
			}        	
        });
        
        println("DeviceManager is running.");
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
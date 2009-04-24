package es.libresoft.openhealth.android;

import ieee_11073.part_20601.phd.channel.Channel;
import ieee_11073.part_20601.phd.channel.VirtualChannel;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import es.libresoft.openhealth.Agent;

import android.util.Log;

public class AndroidServerThread extends Thread {
	private boolean finish = false;
	private ServerSocket ss;
	
	public void run() {
		String status="";
		try {
			ss = new ServerSocket (9999);
			System.out.println("Server attached on " + ss.getInetAddress() + ":" + ss.getLocalPort());
			while(!this.finish){				
				System.out.println("Waiting for clients...");
				Socket s = ss.accept();
				Channel chnl = new Channel (s.getInputStream(),s.getOutputStream());
				VirtualChannel vch = new VirtualChannel(chnl);
				Agent a = new Agent();
				a.setVirtualChannel(vch);				
			}
			if (!ss.isClosed()) //Finishing in good way
				ss.close();
		} catch (IOException e) {
			if (ss!=null && ss.isClosed()){
				status = "Ok";
			}else{
				status = "Error";
				Log.e("THREAD","Error: Can't create a server socket in 9999." + e.getMessage());
			}
		} catch (Exception e) {
			status = "Unexpected error";
			Log.e("THREAD","Unexpected error: " + e.getMessage());
		} finally {
			Log.i("THREAD","Android manager service exiting..." + status);
		}
	}

	public void finish() {
		// TODO Auto-generated method stub
		this.finish = true;
		Log.i("THREAD","Closing manager service...");
		try {
			ss.close();
		} catch (IOException e) {
			Log.e("THREAD","Error: " + e.getMessage());
		}
	}
}

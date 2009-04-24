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

import ieee_11073.part_20601.phd.channel.TCPChannel;
import ieee_11073.part_20601.phd.channel.VirtualChannel;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import es.libresoft.openhealth.Agent;

import android.util.Log;

public class TcpChannel extends Thread {
	private boolean finish = false;
	private ServerSocket ss;
	private AgentHandler handler;
	
	TcpChannel (AgentHandler handler) {
		this.handler = handler;
	}
	
	public void run() {
		String status="";
		try {
			ss = new ServerSocket (9999);
			System.out.println("Server attached on " + ss.getInetAddress() + ":" + ss.getLocalPort());
			while(!this.finish){				
				System.out.println("Waiting for clients...");
				Socket s = ss.accept();
				TCPChannel chnl = new TCPChannel (s);
				VirtualChannel vch = new VirtualChannel(chnl);
				Agent a = new Agent();
				a.setVirtualChannel(vch);
				handler.addAgent(a);
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
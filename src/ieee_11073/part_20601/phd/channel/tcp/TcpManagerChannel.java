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
package ieee_11073.part_20601.phd.channel.tcp;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import es.libresoft.openhealth.Agent;

//import android.util.Log;

public class TcpManagerChannel extends Thread {
	private boolean finish = false;
	private ServerSocket ss;
	private TCPManagedAgents agents = new TCPManagedAgents();

	public void run() {
		String status="";
		try {
			ss = new ServerSocket (9999);
			System.out.println("Server attached on " + ss.getInetAddress() + ":" + ss.getLocalPort());
			System.out.println("Waiting for clients...");
			while(!this.finish){
				Socket s = ss.accept();
				TCPChannel chnl = new TCPChannel (s);
				Agent a = new Agent();
				a.addChannel(chnl);
				agents.addAgent(a);
			}
			if (!ss.isClosed()) //Finishing in good way
				ss.close();
		} catch (IOException e) {
			if (ss!=null && ss.isClosed()){
				status = "Ok";
			}else{
				status = "Error";
				System.out.println("Error: Can't create a server socket in 9999." + e.getMessage());
			}
		} catch (Exception e) {
			status = "Unexpected error";
			System.out.println("Unexpected error: " + e.getMessage());
		} finally {
			System.out.println("manager service exiting..." + status);
		}
		agents.freeAllResources();
	}

	public void finish() {
		// TODO Auto-generated method stub
		this.finish = true;
		System.out.println("Closing manager service...");
		try {
			ss.close();
		} catch (IOException e) {
			System.out.println("Error: " + e.getMessage());
		}
	}
}
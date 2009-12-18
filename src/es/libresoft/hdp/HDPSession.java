/*
Copyright (C) 2008-2009  Jose Antonio Santos Cadenas
email: jcaden@libresoft.es

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

package es.libresoft.hdp;

public class HDPSession {
	
	/* peer field stores the underlying C++ pointer class */
	private long peer;
	
	private HDPCallbacks cb;
	
	private static native void initIDs ();
	private native void init_hdp(HDPConfig config);
	private native synchronized void HDPfree (long peer);

	public HDPSession(HDPConfig config, HDPCallbacks callbacks) throws Exception{
		if ((config == null) || (callbacks == null))
			throw new Exception();
		//Call to native method to start HDP session
		init_hdp(config);
		cb = callbacks;
	}

	public void free () {
		HDPfree(peer);
	}
	
	protected void finalize() {
		free();
	}
	
	public void close(){
		System.out.println("Adios");
	}
	
	static {
		System.loadLibrary("es_libresoft_hdp_HDPSession");
		initIDs();
	}
	
	/* Invoked from native code */
	private void device_connected(String btaddr) {
		System.out.println("JAVA_CONNECTED::::::::::::::::::Got: " + btaddr);
		//cb.new_device_connected(new HDPDevice(btaddr));
	}
	
	/* Invoked from native code */
	private void device_disconected(String btaddr) {
		System.out.println("JAVA_DISCONNECTED::::::::::::::::::Got: " + btaddr);
		//cb.device_disconected(new HDPDevice(btaddr));
	}
	
	/* Invoked from native code */
	private void dc_connected(String btaddr, int mdlid) {
		
		cb.dc_connected(null);
	}
	public int read(String btaddr, int mdlid) {
		// TODO Auto-generated method stub
		return 0;
	}
}

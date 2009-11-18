package es.libresoft.hdp;

import java.io.InputStream;
import java.io.OutputStream;

public class HDPDataChannel {

	private HDPDevice dev;

	public InputStream getInputStream(){
		return null;
	}

	public OutputStream getOutputStream(){
		return null;
	}

	public void close(){

	}

	public void reconnect(){

	}

	public HDPDevice getDevice(){
		return this.dev;
	}
}

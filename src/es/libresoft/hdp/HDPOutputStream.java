package es.libresoft.hdp;

import java.io.IOException;
import java.io.OutputStream;

public class HDPOutputStream extends OutputStream {

	private HDPDataChannel dc;
	
	HDPOutputStream(HDPDataChannel dc){
		this.dc = dc;
	}
	
	@Override
	public void write(int oneByte) throws IOException {
		// TODO Auto-generated method stub
		dc.write(oneByte);
	}

}

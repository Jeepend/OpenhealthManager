package es.libresoft.hdp;

import java.io.IOException;
import java.io.OutputStream;

public class HDPOutputStream extends OutputStream {

		private HDPSession session;
		/*Data channel descriptor*/
		private long dcd;

		HDPOutputStream(HDPSession session, long dc){
			this.session = session;
			this.dcd = dc;
		}
	
		@Override
		public void write(int oneByte) throws IOException {
			byte b[] = new byte[1];
	        b[0] = (byte)oneByte;
			session.write(dcd, b, 0, 1);
		}
		
		@Override
		public void write(byte[] b, int offset, int count) throws IOException {
			session.write(dcd, b, offset, count);
		}
}

package es.libresoft.hdp;

import java.io.IOException;
import java.io.InputStream;

public class HDPInputStream extends InputStream{

		private HDPSession session;
		/*Data channel descriptor*/
		private long dcd;
		
		protected byte buff[];
		protected int coun;
		protected int mark;
		protected int pos;
		
		private static final int L2CAP_DEFAULT_MTU = 672;
		
		HDPInputStream(HDPSession session, long dc){
			this.session = session;
			this.dcd = dc;
			buff = new byte[L2CAP_DEFAULT_MTU];
			coun = 0;
			mark = 0;
			pos = 0;
		}

		@Override
		public int read() throws IOException {
			byte b[] = new byte[1];
	        int ret = session.read(dcd, b, 0, 1);
	        if (ret == 1) {
	            return (int)b[0] & 0xff;
	        } else {
	            return -1;
	        }
		}

		@Override
		public int read(byte[] b, int offset, int length) throws IOException {
			System.out.println("READ with array");
			return session.read(dcd, b, offset, length);
		}		
		
}

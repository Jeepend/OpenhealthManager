package es.libresoft.hdp;

import java.io.IOException;
import java.io.InputStream;

public class HDPInputStream extends InputStream{

		private HDPSession session;
		private String btaddr;
		private int mdlid;
		
		HDPInputStream(HDPSession session, String btaddr, int mdlid){
			this.session = session;
			this.btaddr = btaddr;
			this.mdlid = mdlid;
		}

		@Override
		public int read() throws IOException {
			return session.read(btaddr, mdlid);
		}
}

package es.libresoft.hdp;

import java.io.IOException;
import java.io.InputStream;

public class HDPInputStream extends InputStream{

		private HDPDataChannel dc;
		
		HDPInputStream(HDPDataChannel dc){
			this.dc = dc;
		}

		@Override
		public int read() throws IOException {
			return dc.read();
		}
}

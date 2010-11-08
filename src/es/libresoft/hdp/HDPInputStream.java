package es.libresoft.hdp;

import java.io.IOException;
import java.io.InputStream;

public class HDPInputStream extends InputStream{

		private HDPSession session;
		/*Data channel descriptor*/
		private long dcd;

		protected byte buff[];
		protected int coun;
		protected int pos;

		private static final int L2CAP_DEFAULT_MTU = 672;

		public HDPInputStream(HDPSession session, long dc){
			this.session = session;
			this.dcd = dc;
			buff = new byte[L2CAP_DEFAULT_MTU];
			coun = 0;
			pos = 0;
		}

		@Override
		public int read() throws IOException {
			if (pos >= coun) {
				int ret;
				/* get next packet */
				ret = session.read(dcd, buff, 0, L2CAP_DEFAULT_MTU);
				if (ret <= 0)
					return -1;
				coun = ret;
				pos = 0;
				//System.out.println("readed: " + coun + ", pos: " + pos);
			}

			//System.out.println("coun: " + coun + ", pos: " + pos);

			return (int)buff[pos++] & 0xff;
		}

		@Override
		public int read(byte[] b, int offset, int length) throws IOException {
			int r;
			if (pos >= coun) {
				/* get next packet */
				r = session.read(dcd, buff, 0, L2CAP_DEFAULT_MTU);
				if (r <= 0)
					return r;
				coun = r;
				pos = 0;
			}

			r = coun - pos;
			if (r >= length) {
				System.arraycopy(buff, pos, b, offset, length);
				pos = pos + length;
				return length;
			}

			System.arraycopy(buff, pos, b, offset, r);
			pos = coun;
			return r;
		}

		@Override
		public int available() throws IOException {
			return coun - pos;
		}

		@Override
		public boolean markSupported() {
			return false;
		}
}

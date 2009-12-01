package ieee_11073.part_20601.phd.channel.bluetooth;

import ieee_11073.part_20601.phd.channel.InitializedException;
import ieee_11073.part_20601.phd.channel.VirtualChannel;
import es.libresoft.hdp.HDPCallbacks;
import es.libresoft.hdp.HDPConfig;
import es.libresoft.hdp.HDPDataChannel;
import es.libresoft.hdp.HDPDevice;
import es.libresoft.hdp.HDPSession;
import es.libresoft.openhealth.Agent;

public class HDPManagerChannel {
	
	private static final String srvName = "Libresoft Manager";
	private static final String srvDescName = "Libresoft 11073-20601 Manager";
	private static final String provName = "Libresoft";
	
	private HDPSession hdps;
	
	private HDPConfig config;
	private HDPCallbacks callbacks = new HDPCallbacks () {

		@Override
		public void dc_connected(HDPDataChannel dc) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void dc_deleted(HDPDataChannel dc) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void device_disconected(HDPDevice dev) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void new_device_connected(HDPDevice dev) {
			// TODO Auto-generated method stub
			
		}
		
	};
	
	public void HDPManagerChannel () {
		config = new HDPConfig (srvName, srvDescName, provName, null );
		hdps = new HDPSession(config, callbacks);
	}
	
	public void finish() {
		System.out.println("Closing HDP manager service...");
		hdps.close();
	}
}

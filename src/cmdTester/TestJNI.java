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

package cmdTester;

import es.libresoft.hdp.Feature;
import es.libresoft.hdp.FeatureGroup;
import es.libresoft.hdp.HDPCallbacks;
import es.libresoft.hdp.HDPConfig;
import es.libresoft.hdp.HDPDataChannel;
import es.libresoft.hdp.HDPDevice;
import es.libresoft.hdp.HDPSession;

public class TestJNI {

	public static HDPCallbacks callbacks = new HDPCallbacks(){
		@Override
		public void dc_connected(HDPDataChannel dc) {
			System.out.println("callback: dc_connected");
		}

		@Override
		public void dc_deleted(HDPDataChannel dc) {
			System.out.println("callback: dc_deleted");
		}

		@Override
		public void device_disconected(HDPDevice dev) {
			System.out.println("callback: device_disconected");
		}

		@Override
		public void new_device_connected(HDPDevice dev) {
			System.out.println("callback: new_device_connected");
		}
		
	};
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Feature[] fs = new Feature [] {
				new Feature (0, "feature1"),
				new Feature (1, "feature2"),
		};
		FeatureGroup[] fg = new FeatureGroup[] {
				new FeatureGroup(fs,0),
				new FeatureGroup(fs,1),
		};
		HDPConfig conf = new HDPConfig("string1", "string2", "string3", fg);
		try {
			HDPSession hdp = new HDPSession(conf, callbacks);
			hdp.close();
			System.out.println("Push any key to exit");
			System.in.read();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}

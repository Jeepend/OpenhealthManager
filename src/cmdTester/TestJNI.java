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
				new Feature (4100, "Pulse-oximeter"),
/*
				new Feature (1, "feature2"),
				new Feature (2, "feature1"),
				new Feature (3, "feature2"),
				new Feature (4, "feature1"),
				new Feature (5, "feature2"),
				new Feature (6, "feature1"),
				new Feature (7, "feature2"),
				new Feature (8, "feature1"),
				new Feature (9, "feature2"),
				new Feature (10, "feature1"),
				new Feature (11, "feature2"),
				new Feature (12, "feature1"),
				new Feature (13, "feature2"),
				new Feature (14, "feature1"),
				new Feature (15, "feature2"),
				new Feature (16, "feature1"),
				new Feature (17, "feature2"),
				new Feature (18, "feature1"),
				new Feature (19, "feature2"),
				new Feature (20, "feature1"),
				new Feature (21, "feature2"),
				new Feature (22, "feature1"),
				new Feature (23, "feature2"),
				new Feature (24, "feature1"),
				new Feature (25, "feature2"),
				new Feature (26, "feature1"),
				new Feature (27, "feature2"),
				new Feature (28, "feature1"),
				new Feature (29, "feature2"),
*/
		};
		FeatureGroup[] fg = new FeatureGroup[] {
				new FeatureGroup(fs,FeatureGroup.SINK_ROLE),
/*
				new FeatureGroup(fs,1),
				new FeatureGroup(fs,2),
				new FeatureGroup(fs,3),
				new FeatureGroup(fs,4),
				new FeatureGroup(fs,5),
				new FeatureGroup(fs,6),
				new FeatureGroup(fs,7),
				new FeatureGroup(fs,8),
				new FeatureGroup(fs,9),
				new FeatureGroup(fs,10),
				new FeatureGroup(fs,11),
				new FeatureGroup(fs,12),
				new FeatureGroup(fs,13),
				new FeatureGroup(fs,14),
				new FeatureGroup(fs,15),
				new FeatureGroup(fs,16),
				new FeatureGroup(fs,17),
				new FeatureGroup(fs,18),
				new FeatureGroup(fs,19),
				new FeatureGroup(fs,20),
				new FeatureGroup(fs,21),
				new FeatureGroup(fs,22),
				new FeatureGroup(fs,23),
				new FeatureGroup(fs,24),
				new FeatureGroup(fs,25),
*/
		};
		HDPConfig conf = new HDPConfig("string1", "string2", "string3", fg);
		try {
			HDPSession hdp = new HDPSession(conf, callbacks);
			hdp.close();
			System.out.println("Push any key to exit");
			System.in.read();
			hdp.free();
			System.out.println("Exiting...");
			System.in.read();
			System.out.println("Exited.");
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}

package cmdTester;

import es.libresoft.hdp.Feature;
import es.libresoft.hdp.FeatureGroup;
import es.libresoft.hdp.HDPConfig;
import es.libresoft.hdp.HDPSession;

public class TestJNI {

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
			HDPSession hdp = new HDPSession(conf,null);
			hdp.close();
			System.out.println("Push any key to exit");
			System.in.read();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}

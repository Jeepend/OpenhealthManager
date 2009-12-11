package cmdTester;

import es.libresoft.hdp.HDPSession;

public class TestJNI {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			HDPSession hdp = new HDPSession(null,null);
			hdp.close();
			System.out.println("Push any key to exit");
			System.in.read();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}

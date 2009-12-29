package cmdTester;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import es.libresoft.hdp.HDPInputStream;


public class InputStreamTester {

	/**
	 * @param args
	 */
	public static String file = "test.txt";
	
	public static void main(String[] args) {
		try {			
			HDPInputStream in1 = new HDPInputStream (
					new FileInputStream (
							new File (file)));
			
			int r;
			for (int i = 0; i < 34; i++) {
				r = in1.read();
				if (r > 0)
					System.out.print((char)r );
				else
					System.out.println("-");
			}
			
			HDPInputStream in2 = new HDPInputStream (
					new FileInputStream (
							new File (file)));
			
			byte buff[] = new byte[33];
			int offset = 0, len = buff.length;
			while (true) {
				r = in2.read(buff, offset, len);
				if (r < 0) {
					System.out.println("Fin con offset="+offset);
					break;
				}
				offset = offset + r;
				len = buff.length - offset;
				System.out.println("offset=" + offset + ", len=" + len);
			}
			/*
			while ((r = in2.read(buff, offset, buff.length)) > 0) {
				System.out.println("Readed: " + r);
			}
			*/
			for (int i = 0; i < 33; i++)
				System.out.print((char)buff[i]);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}

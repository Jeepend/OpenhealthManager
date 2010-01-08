package cmdTester;

import ieee_11073.part_20601.phd.channel.bluetooth.HDPManagerChannel;
import ieee_11073.part_20601.phd.channel.tcp.TcpManagerChannel;

import java.util.Iterator;
import java.util.List;

import es.libresoft.openhealth.Agent;
import es.libresoft.openhealth.events.InternalEventManager;
import es.libresoft.openhealth.events.InternalEventReporter;
import es.libresoft.openhealth.events.MeasureReporterFactory;

public class ManagerShell {

	private static InternalEventManager ieManager = new InternalEventManager(){

		@Override
		public void agentChangeStatus(String systemId, String state) {
			System.out.println("ID: " + systemId + " state: " + state);
		}

		@Override
		public void agentConnected(Agent agent) {
			System.out.println("Agent " + agent.getSystem_id() + " connected");
		}

		@Override
		public void agentDisconnected(String systemId) {
			System.out.println("Agent " + systemId + " disconnected");
		}

		@Override
		public void receivedMeasure(String systemId, List measures) {
			Iterator i = measures.iterator();
			if (measures.isEmpty()) return;

			System.out.println("Measures received from: " + systemId);
			while (i.hasNext()) {
				System.out.println("" + i.next());
			}
		}

	};

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.println("Starting CmdManager.");
		try {
			/* uncomment next line to get HDP support for agents */
			//HDPManagerChannel chanHDP = new HDPManagerChannel();
			/* uncomment next line to get TCP support for agents */
			TcpManagerChannel channelTCP = new TcpManagerChannel();
			//Set the event manager handler to get internal events from the manager thread
			InternalEventReporter.setDefaultEventManager(ieManager);
			//Set target platform to android to report measures using IPC mechanism
			MeasureReporterFactory.setDefaultMeasureReporter(MeasureReporterFactory.SHELL);
			
			/* Start TCP server */
			channelTCP.start();
			
			System.out.println("Push any key to exit");
			System.in.read();
			
			//chanHDP.finish();
			channelTCP.finish();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}

package cmdTester;

import ieee_11073.part_20601.phd.channel.bluetooth.HDPManagerChannel;
import ieee_11073.part_20601.phd.channel.tcp.TcpManagerChannel;

import java.util.Iterator;
import java.util.List;

import es.libresoft.openhealth.Agent;
import es.libresoft.openhealth.events.InternalEventManager;
import es.libresoft.openhealth.events.InternalEventReporter;
import es.libresoft.openhealth.events.MeasureReporter;
import es.libresoft.openhealth.events.MeasureReporterFactory;

public class ManagerShell {

	private static InternalEventManager ieManager = new InternalEventManager(){

		@Override
		public void agentChangeState(Agent agent, String state) {
			System.out.println("ID: " + agent.getId() + " state: " + state);
		}

		@Override
		public void receivedMeasure(Agent agent, MeasureReporter mr) {
			List<Object> measures = mr.getMeasures();
			Iterator<Object> ims = measures.iterator();

			List<Object> attributes = mr.getAttributes();
			Iterator<Object> iat = attributes.iterator();

			if (!measures.isEmpty()) {
				System.out.println("Measures received from: " + agent.getId());
				while (ims.hasNext()) {
					System.out.println("" + ims.next());
				}
			}

			if (!attributes.isEmpty()) {
				System.out.println("Attributes received from: " + agent.getId());
				while (iat.hasNext()) {
					System.out.println("" + iat.next());
				}
			}
		}

		@Override
		public void agentPlugged(Agent agent) {
			System.out.println("TODO: agentPlugged");
		}

		@Override
		public void agentUnplugged(Agent agent) {
			System.out.println("TODO: agentUnplugged");
		}

		@Override
		public void error(Agent agent, int errorCode) {
			System.out.println("TODO: error: " + errorCode);
		}

	};

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.println("Starting CmdManager.");
		try {
			/* uncomment next line to get HDP support for agents */
			// HDPManagerChannel chanHDP = new HDPManagerChannel();
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

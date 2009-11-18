package cmdTester;

import java.util.Iterator;
import java.util.List;

import es.libresoft.openhealth.Agent;
import es.libresoft.openhealth.android.TcpChannel;
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
				System.out.println("" + i.next().toString());
			}
		}

	};

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.println("Arrancando manager.");
		TcpChannel channelTCP = new TcpChannel();
		//Set the event manager handler to get internal events from the manager thread
		InternalEventReporter.setDefaultEventManager(ieManager);
		//Set target platform to android to report measures using IPC mechanism
		MeasureReporterFactory.setDefaultMeasureReporter(MeasureReporterFactory.SHELL);

		//Start TCP server
		channelTCP.start();
	}

}

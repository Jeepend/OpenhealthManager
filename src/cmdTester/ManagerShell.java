/*
Copyright (C) 2008-2011 GSyC/LibreSoft, Universidad Rey Juan Carlos.

Author: Jose Antonio Santos Cadenas <jcaden@libresoft.es>
Author: Santiago Carot-Nemesio <scarot@libresoft.es>

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
		public void agentChangeState(Agent agent, int state) {
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
			e.printStackTrace();
		}
	}

}

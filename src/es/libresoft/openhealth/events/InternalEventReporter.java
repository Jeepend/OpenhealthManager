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

package es.libresoft.openhealth.events;

import java.util.List;

import es.libresoft.openhealth.Agent;

public class InternalEventReporter {

	private static InternalEventManager iEvent;

	public static synchronized void setDefaultEventManager (InternalEventManager handler){
		iEvent = handler;
	}

	public static void agentConnected(Agent agent) {
		if (iEvent!=null)
			iEvent.agentConnected(agent);
	}

	public static void agentDisconnected(String system_id) {
		if (iEvent!=null)
			iEvent.agentDisconnected(system_id);
	}

	public static void agentChangeStatus(String system_id, String state) {
		if (iEvent!=null)
			iEvent.agentChangeStatus(system_id, state);
	}

	public static void receivedMeasure(String system_id, MeasureReporter mr) {
		if (iEvent!=null)
			iEvent.receivedMeasure(system_id, mr);
	}
}

/*
Copyright (C) 2008-2009  José Antonio Santos Cadenas
email: jcaden __at__ libresoft _dot__ es

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

package ieee_11073.part_20601.phd.channel.tcp;

import ieee_11073.part_20601.phd.channel.bluetooth.HDPConnection;

import java.util.ArrayList;
import java.util.Iterator;

import es.libresoft.hdp.HDPDevice;
import es.libresoft.openhealth.Agent;

public class TCPManagedAgents {

	private ArrayList<Agent> agents;

	public TCPManagedAgents() {
		agents = new ArrayList<Agent>();
	}

	public synchronized boolean addAgent (Agent agent) {
		if (agent == null)
			return true;
		return agents.add(agent);
	}

	public synchronized boolean delAgent (Agent agent) {
		if (agent == null)
			return false;
		if (agents.remove(agent)){
			agent.freeResources();
			return true;
		}
		return false;
	}

	public synchronized void freeAllResources () {
		Iterator<Agent> i = agents.iterator();
		while (i.hasNext()) {
			i.next().freeResources();
		}
		agents.clear();
	}
}

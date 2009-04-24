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

package es.libresoft.openhealth.android;

import java.util.ArrayList;
import java.util.Iterator;

import es.libresoft.openhealth.Agent;
import es.libresoft.openhealth.events.Event;
import es.libresoft.openhealth.events.EventType;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class DrDroid extends Service {

	private ArrayList<Agent> agents;
	private TcpChannel channelTCP;
	private AgentHandler agentHandler = new AgentHandler(){
		@Override
		public synchronized void addAgent(Agent newAgent) {
			System.out.println("Nuevo agente!!");
			agents.add(newAgent);
		}
	};
	
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		System.out.println("Service created");
		channelTCP = new TcpChannel(agentHandler);
		agents = new ArrayList<Agent>();
		super.onCreate();
	}

	@Override
	public void onStart(Intent intent, int startId) {
		// TODO Auto-generated method stub
		System.out.println("Service started");
		channelTCP.start();
		super.onStart(intent, startId);
	}

	@Override
	public void onDestroy() {
		System.out.println("Service stopped");
		channelTCP.finish();
		Iterator<Agent> iterator = agents.iterator();
		Agent agent;
		//Send abort signal to all agents
		while (iterator.hasNext()){
			agent = iterator.next();
			agent.sendEvent(new Event(EventType.REQ_ASSOC_ABORT));
		}
		
		//Free resources taken by agents 
		iterator = agents.iterator();
		while (iterator.hasNext()){
			agent = iterator.next();
			agent.freeResources();
		}
		agents.clear();
		super.onDestroy();
	}

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	//---------------------------------Private Methods----------------------------------------

}

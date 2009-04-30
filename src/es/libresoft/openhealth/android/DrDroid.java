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
import java.util.HashMap;
import java.util.Iterator;

import es.libresoft.openhealth.Agent;
import es.libresoft.openhealth.events.Event;
import es.libresoft.openhealth.events.EventType;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteCallbackList;
import android.os.RemoteException;


public class DrDroid extends Service {

	/**
     * This is a list of callbacks that have been registered with the manager
     * service.  Note that this is package scoped (instead of private) so
     * that it can be accessed more efficiently from inner classes.
     */
    final RemoteCallbackList<IManagerCallbackService> mCallbacks
            = new RemoteCallbackList<IManagerCallbackService>();
    
    /**
     * This is a list of callbacks that have been registered with the agent
     * service.  Note that this is package scoped (instead of private) so
     * that it can be accessed more efficiently from inner classes.
     */
    final HashMap<String,RemoteCallbackList<IAgentCallbackService>> aCallback
    		= new HashMap<String,RemoteCallbackList<IAgentCallbackService>>();
    
	public static final String droidEvent = "es.libresoft.openhealth.android.DRDROID_SERVICE";
	
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
		System.out.println("Service created");
		channelTCP = new TcpChannel(agentHandler);
		agents = new ArrayList<Agent>();
		super.onCreate();
	}

	@Override
	public void onStart(Intent intent, int startId) {
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
		
		// Unregister all callbacks.
        mCallbacks.kill();
        
		super.onDestroy();
	}

	@Override
	public IBinder onBind(Intent arg0) {
		// Select the interface to return.  
        if (IManagerRegister.class.getName().equals(arg0.getAction())) {
        	//Register for manager events
            return mRegisterServiceStub;
        } else if (IAgentRegister.class.getName().equals(arg0.getAction())){
        	//Register for agent events
        	return aRegisterServiceStub;
        } else if (IAgentActionService.class.getName().equals(arg0.getAction())){
        	//Use agent events
        	return aActionServiceStub;
        } else return null;
	}
	
	//---------------------------------Private Methods----------------------------------------
	
	/************************************************************
	 * Call back service implementation in the same class
	 ************************************************************/
	
	/**
     * The IManagerRegister is defined through IDL
     */
    private final IManagerRegister.Stub mRegisterServiceStub = new IManagerRegister.Stub() {

		@Override
		public void registerCallback(IManagerCallbackService mc)
				throws RemoteException {
			System.out.println("Register for manager events");
			if (mc != null) mCallbacks.register(mc);		
		}

		@Override
		public void unregisterCallback(IManagerCallbackService mc)
				throws RemoteException {
			System.out.println("Unregister for manager events");
			if (mc != null) mCallbacks.unregister(mc);			
		}
    };
    
    /**
     * The IAgentRegister is defined through IDL
     */
    private final IAgentRegister.Stub aRegisterServiceStub = new IAgentRegister.Stub() {

		@Override
		public void registerAgentCallback(String system_id,
				IAgentCallbackService mc) throws RemoteException {
			System.out.println("Register for agent events");
			if (mc == null || !aCallback.containsKey(system_id))
				return;
			aCallback.get(system_id).register(mc);
		}

		@Override
		public void unregisterCallback(String system_id,
				IAgentCallbackService mc) throws RemoteException {
			System.out.println("Unregister for agent events");
			if (mc == null || !aCallback.containsKey(system_id))
				return;
			aCallback.get(system_id).unregister(mc);
		}
    };
    
    
    /************************************************************
	 * Action service
	 ************************************************************/
    /**
     * The IAgentRegister is defined through IDL
     */
    private final IAgentActionService.Stub aActionServiceStub = new IAgentActionService.Stub() {

		@Override
		public void getService(String system_id) throws RemoteException {
			// TODO Auto-generated method stub
			System.out.println("get service invoke on " + system_id);
		}

		@Override
		public void sendEvent(String system_id, int eventType)
				throws RemoteException {
			// TODO Auto-generated method stub
			System.out.println("send event " + eventType +" to " + system_id);
		}

		@Override
		public void setService(String system_id) throws RemoteException {
			// TODO Auto-generated method stub
			System.out.println("set service invoke on " + system_id);
		}
    };
}

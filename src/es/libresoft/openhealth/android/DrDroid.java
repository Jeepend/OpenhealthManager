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

import ieee_11073.part_10101.Nomenclature;
import ieee_11073.part_20601.asn1.SystemModel;
import ieee_11073.part_20601.phd.channel.bluetooth.HDPManagerChannel;
import ieee_11073.part_20601.phd.channel.tcp.TcpManagerChannel;
import ieee_11073.part_20601.phd.dim.Attribute;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.Parcelable;
import android.os.RemoteCallbackList;
import android.os.RemoteException;
import es.libresoft.openhealth.Agent;
import es.libresoft.openhealth.events.Event;
import es.libresoft.openhealth.events.EventType;
import es.libresoft.openhealth.events.InternalEventManager;
import es.libresoft.openhealth.events.InternalEventReporter;
import es.libresoft.openhealth.events.MeasureReporter;
import es.libresoft.openhealth.events.MeasureReporterFactory;
import es.libresoft.openhealth.utils.DIM_Tools;


public class DrDroid extends Service {

	/**
     * This is a list of callbacks that have been registered with the manager
     * service.  Note that this is package scoped (instead of private) so
     * that it can be accessed more efficiently from inner classes.
     */
    private final RemoteCallbackList<IManagerCallbackService> mCallbacks
            = new RemoteCallbackList<IManagerCallbackService>();
    
    /**
     * This is a list of callbacks that have been registered with the agent
     * service.  Note that this is package scoped (instead of private) so
     * that it can be accessed more efficiently from inner classes.
     */
    private final HashMap<String,RemoteCallbackList<IAgentCallbackService>> aCallback
    		= new HashMap<String,RemoteCallbackList<IAgentCallbackService>>();
    
    private final HashMap<String, Agent> agentsId = new HashMap<String, Agent>();
    
	public static final String droidEvent = "es.libresoft.openhealth.android.DRDROID_SERVICE";
	
	private TcpManagerChannel channelTCP;
	private HDPManagerChannel chanHDP;
	
	@Override
	public void onCreate() {
		System.out.println("Service created");
		channelTCP = new TcpManagerChannel();
		//Set the event manager handler to get internal events from the manager thread
		InternalEventReporter.setDefaultEventManager(ieManager);
		//Set target platform to android to report measures using IPC mechanism
		MeasureReporterFactory.setDefaultMeasureReporter(MeasureReporterFactory.ANDROID);
		super.onCreate();
	}

	@Override
	public void onStart(Intent intent, int startId) {
		try {
			channelTCP.start();
			//chanHDP = new HDPManagerChannel();
			System.out.println("Service started");
			super.onStart(intent, startId);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void onPause(){
		System.out.println("Service paused");
	}

	public void onResume(){
		System.out.println("Service resumed");
	}
	
	public void onStop(){
		System.out.println("Service Stopped");
	}
	
	@Override
	public void onDestroy() {
		System.out.println("Service stopped");
		channelTCP.finish();
		//chanHDP.finish();
		Iterator<Agent> iterator = agentsId.values().iterator();
		Agent agent;
		//Send abort signal to all agents
		while (iterator.hasNext()){
			agent = iterator.next();
			agent.sendEvent(new Event(EventType.REQ_ASSOC_ABORT));
		}
		
		//Free resources taken by agents 
		iterator = agentsId.values().iterator();
		while (iterator.hasNext()){
			agent = iterator.next();
			agent.freeResources();
		}
		
		// Unregister all callbacks.
        mCallbacks.kill();
        Iterator<String> i = aCallback.keySet().iterator();
        while (i.hasNext()){
        	aCallback.get(i.next()).kill();
        }
        agentsId.clear();
        aCallback.clear();
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
			if (mc != null) mCallbacks.register(mc);		
		}

		@Override
		public void unregisterCallback(IManagerCallbackService mc)
				throws RemoteException {
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
			System.err.println("Registering agent callback");
			if (mc == null || !aCallback.containsKey(system_id)){
				return;	
			}
			aCallback.get(system_id).register(mc);
			System.err.println("Registered callback for agent " + system_id);
		}

		@Override
		public void unregisterCallback(String system_id,
				IAgentCallbackService mc) throws RemoteException {
			if (mc == null || !aCallback.containsKey(system_id))
				return;
			aCallback.get(system_id).unregister(mc);
		}
    };
    
    
    /************************************************************
	 * Action service implemented in the same class
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
			System.out.println("disconnect service invoke on " + system_id);
			Agent agt = agentsId.get(system_id);
			if (agt==null)
				return;
			agt.sendEvent(new Event(eventType));
		}

		@Override
		public void setService(String system_id) throws RemoteException {
			// TODO Auto-generated method stub
			System.out.println("set service invoke on " + system_id);
		}
    };
    
    private AgentDevice getAgentDevice (Agent a) {
    	String manufacturer = "Unknown";
    	String model = "Unknown";
		Attribute attr = a.mdsHandler.getMDS().getAttribute(Nomenclature.MDC_ATTR_ID_MODEL);
		if (attr != null) {
			SystemModel sm = (SystemModel)attr.getAttributeType();
			manufacturer = DIM_Tools.byteArrayToString(sm.getManufacturer());
			model = DIM_Tools.byteArrayToString(sm.getModel_number());
		}
		AgentDevice ad = new AgentDevice (a.mdsHandler.getMDS().getDeviceConf().getPhdId(),
				a.getSystem_id(),manufacturer, model);
		return ad;
    }
    /************************************************************
	 * Internal events triggered from manager thread
	 ************************************************************/
    private final InternalEventManager ieManager = new InternalEventManager(){
    	//+++++++++++++++++++++++++++++++
    	//+ Manager's events:
    	//+++++++++++++++++++++++++++++++
		@Override
		public void agentConnected(Agent agent) {
			
			// Create new input for callbacks events for the new agent
			agentsId.put(agent.getSystem_id(), agent);
			aCallback.put(agent.getSystem_id(), new RemoteCallbackList<IAgentCallbackService>());
			
			// Send a manager Broadcast Event to all clients.
            final int N = mCallbacks.beginBroadcast();
            for (int i=0; i<N; i++) {
                try {
                    mCallbacks.getBroadcastItem(i).agentConnection(getAgentDevice(agent));
                } catch (RemoteException e) {
                    // The RemoteCallbackList will take care of removing
                    // the dead object for us.
                }
            }
            mCallbacks.finishBroadcast();
		}

		@Override
		public void agentDisconnected(String system_id) {
			
			// Send a manager Broadcast Event to all clients.
            final int N = mCallbacks.beginBroadcast();
            for (int i=0; i<N; i++) {
                try {
                    mCallbacks.getBroadcastItem(i).agentDisconnection(system_id);
                } catch (RemoteException e) {
                    // The RemoteCallbackList will take care of removing
                    // the dead object for us.
                }
            }
            mCallbacks.finishBroadcast();
			
			// Remove all inputs for this agent
			agentsId.remove(system_id);
			aCallback.get(system_id).kill();
			aCallback.remove(system_id);
		}
		
		//+++++++++++++++++++++++++++++++
		//+ Agent's Events
		//+++++++++++++++++++++++++++++++
		@Override
		public void agentChangeStatus(String system_id, String state) {
			if (system_id==null || !aCallback.containsKey(system_id))
				//Unknown agent changes from disconnect state to unassociated (system_id is not received yet) 
				return;
			
			// Send a agent Broadcast Event to all clients.
			final RemoteCallbackList<IAgentCallbackService> agentCallbacks = aCallback.get(system_id);
            final int N = agentCallbacks.beginBroadcast();
            for (int i=0; i<N; i++) {
                try {
                	agentCallbacks.getBroadcastItem(i).agentStateChanged(state);
                } catch (RemoteException e) {
                    // The RemoteCallbackList will take care of removing
                    // the dead object for us.
                }
            }
            agentCallbacks.finishBroadcast();
			//System.out.println("agente " + system_id + " changed to: " + state);
		}

		@Override
		public void receivedMeasure(String system_id, MeasureReporter mr) {
			AndroidMeasureReporter amr = (AndroidMeasureReporter)mr;
			
			if (system_id==null || !aCallback.containsKey(system_id))
				return;
			// Send a agent Broadcast Event to all clients.
			final RemoteCallbackList<IAgentCallbackService> agentCallbacks = aCallback.get(system_id);
            final int N = agentCallbacks.beginBroadcast();
            for (int i=0; i<N; i++) {
                try {
                	agentCallbacks.getBroadcastItem(i).metricReceived(amr.getMetric());
                } catch (RemoteException e) {
                    // The RemoteCallbackList will take care of removing
                    // the dead object for us.
                	e.printStackTrace();
                }
            }
            agentCallbacks.finishBroadcast();
		}
    	
    };
}

/*
Copyright (C) 2011 GSyC/LibreSoft, Universidad Rey Juan Carlos.

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

package es.libresoft.openhealth.android;

import ieee_11073.part_20601.phd.channel.tcp.TcpManagerChannel;
import ieee_11073.part_20601.phd.dim.Attribute;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import es.libresoft.openhealth.Agent;
import es.libresoft.openhealth.android.aidl.IAgent;
import es.libresoft.openhealth.android.aidl.types.IError;
import es.libresoft.openhealth.android.aidl.IAgentService;
import es.libresoft.openhealth.android.aidl.IManagerClientCallback;
import es.libresoft.openhealth.android.aidl.IManagerService;
import es.libresoft.openhealth.android.aidl.types.IAttribute;
import es.libresoft.openhealth.error.ErrorException;
import es.libresoft.openhealth.error.ErrorFactory;
import es.libresoft.openhealth.events.EventType;
import es.libresoft.openhealth.events.InternalEventManager;
import es.libresoft.openhealth.events.InternalEventReporter;
import es.libresoft.openhealth.events.MeasureReporter;
import es.libresoft.openhealth.events.MeasureReporterFactory;
import es.libresoft.openhealth.storage.ConfigStorageFactory;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;

public class HealthService extends Service {

	/** Registered clients */
	Vector<IManagerClientCallback> clients = new Vector<IManagerClientCallback>();

	private TcpManagerChannel channelTCP;
	private boolean started = false;
	private Vector<Agent> agents = new Vector<Agent>();

	/************************************************************
	 * Internal events triggered from manager thread
	 ************************************************************/
	private final InternalEventManager ieManager = new InternalEventManager() {

		@Override
		public void agentChangeState(Agent agent, String state) {
			System.out.println("TODO: agentChangeStatus to " + state);
		}

		@Override
		public void receivedMeasure(Agent agent, MeasureReporter mr) {
			System.out.println("TODO: receivedMeasure....");
		}

		@Override
		public void agentPlugged(Agent agent) {
			agents.add(agent);
			for (IManagerClientCallback c: clients) {
				try {
					c.agentPlugged(new IAgent(agent.getId()));
				} catch (RemoteException e) {
					clients.remove(c);
				}
			}
		}

		@Override
		public void agentUnplugged(Agent agent) {
			agents.removeElement(agent);
			for (IManagerClientCallback c: clients) {
				try {
					c.agentUnplugged(new IAgent(agent.getId()));
				} catch (RemoteException e) {
					clients.remove(c);
				}
			}
		}
	};

	@Override
	public void onCreate() {
		//Set the event manager handler to get internal events from the manager thread
		InternalEventReporter.setDefaultEventManager(ieManager);
		//Set target platform to android to report measures using IPC mechanism
		MeasureReporterFactory.setDefaultMeasureReporter(MeasureReporterFactory.ANDROID);
		ConfigStorageFactory.setDefaultConfigStorage(new AndroidConfigStorage(this.getApplicationContext()));
		ErrorFactory.setDefaultErrorGenerator(new AndroidError(this.getApplicationContext()));
		System.out.println("Service created");
		channelTCP = new TcpManagerChannel();
		super.onCreate();
	}

	void initTransportLayers() {
		channelTCP.start();
	}

	void shutdownTransportLayers() {
		channelTCP.finish();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		if (!started) {
			initTransportLayers();
			started = true;
		}

		// We want this service to continue running until it is explicitly
		// stopped, so return sticky.
		return START_STICKY;
	}

	@Override
	public void onDestroy() {
		shutdownTransportLayers();
		started = false;
	}

	/**
	 * The IManagerService is defined through IDL
	 */
	private final IManagerService.Stub managerServiceStub = new IManagerService.Stub() {

		@Override
		public void agents(List<IAgent> agentList) throws RemoteException {
			if (agentList == null)
				agentList= new ArrayList<IAgent>();

			for(Agent agent: agents) {
				agentList.add(new IAgent(agent.getId()));
			}
		}

		@Override
		public void registerApplication(IManagerClientCallback mc)
				throws RemoteException {
			if (mc != null)
				clients.add(mc);
		}

		@Override
		public void unregisterApplication(IManagerClientCallback mc)
				throws RemoteException {
			clients.removeElement(mc);
			if (clients.size() == 0) {
				HealthService.this.stopSelf();
			}
		}

	};

	private Agent getAgent(IAgent agent) {
		for(Agent a : agents) {
			if (a.getId() == agent.getId())
				return a;
		}
		return null;
	}

	/**
	 * The IAgentService is defined through IDL
	 */
	private final IAgentService.Stub agentServiceStub = new IAgentService.Stub() {

		@Override
		public void connect(IAgent agent) throws RemoteException {
			// TODO Auto-generated method stub
			System.out.println("TODO: Connect with the agent");
		}

		@Override
		public void getAttribute(IAgent agent, int attrId, IAttribute attr)
				throws RemoteException {

			Agent a = getAgent(agent);

			if (a == null) {
				System.err.println("Invalid agent error");
				attr  = null;
				return;
			}

			Attribute at = a.mdsHandler.getMDS().getAttribute(attrId);
			IAttrFactory.getParcelableAttribute(at.getAttributeType(), attr);
		}

		@Override
		public boolean updateMDS(IAgent agent, IError err) throws RemoteException {
			Agent a = getAgent(agent);

			if (a == null) {
				System.err.println("Invalid agent error");
				return false;
			}

			AndroidExternalEvent<Boolean, Object> ev = new AndroidExternalEvent<Boolean, Object>(EventType.REQ_MDS, null);

			a.sendEvent(ev);

			try {
				ev.proccessing();
			} catch (InterruptedException e) {
				e.printStackTrace();
				return false;
			}

			if (ev.wasError()) {
				try {
					System.err.println("Error happened getting MDS: " + ErrorFactory.getDefaultErrorGenerator().error2string(ev.getError()));
				} catch (ErrorException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return false;
			}

			return ev.getRspData();
		}
	};

	@Override
	public IBinder onBind(Intent intent) {
		if (IManagerService.class.getName().equals(intent.getAction()))
			return managerServiceStub;
		if (IAgentService.class.getName().equals(intent.getAction()))
			return agentServiceStub;
		return null;
	}

}

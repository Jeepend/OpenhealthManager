package es.libresoft.openhealth.android;

import java.util.List;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;

public class HealthService extends Service {

	/**
	 * The IManagerService is defined through IDL
	 */
	private final IManagerService.Stub managerServiceStub = new IManagerService.Stub() {

		@Override
		public void agents(List<IAgent> agentList) throws RemoteException {
			System.out.println("TODO implement 'agents' method");
		}

		@Override
		public void registerApplication(IManagerClientCallback mc)
				throws RemoteException {
			System.out.println("TODO implement 'registerApplication' method");
		}

		@Override
		public void unregisterApplication(IManagerClientCallback mc)
				throws RemoteException {
			System.out.println("TODO implement 'unregisterApplication' method");
		}

	};

	@Override
	public IBinder onBind(Intent intent) {
		if (IManagerService.class.getName().equals(intent.getAction()))
			return managerServiceStub;
		return null;
	}

}

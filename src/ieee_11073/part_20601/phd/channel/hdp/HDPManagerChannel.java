/*
Copyright (C) 2012 GSyC/LibreSoft, Universidad Rey Juan Carlos.

Author: Jorge Fernandez-Gonzalez <jfernandez@libresoft.es>
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

package ieee_11073.part_20601.phd.channel.hdp;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Set;
import java.util.UUID;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothHealth;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.os.ParcelUuid;
import android.widget.Toast;
import es.libresoft.openhealth.Agent;
import es.libresoft.openhealth.logging.Logging;

public class HDPManagerChannel {
	private static String TAG = "HDPManagerChannel";

	private static UUID HDP_SOURCE = UUID.fromString("00001401-0000-1000-8000-00805f9b34fb");

	private Context context;
	private BluetoothAdapter mBluetoothAdapter;
	private BluetoothHealth mBluetoothHealth;

	public HDPManagerChannel(Context context) {
		this.context = context;
	}

	public void start() {
		// Check for Bluetooth availability on the Android platform.
		mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

		// Ensures user has turned on Bluetooth on the Android device.
		if (mBluetoothAdapter == null || !mBluetoothAdapter.isEnabled()) {
			// Bluetooth adapter isn't available.  The client of the service is supposed to
			// verify that it is available and activate before invoking this service.
			Toast.makeText(context, "bluetooth_not_available", Toast.LENGTH_LONG).show();
			return;
		}

		if (!mBluetoothAdapter.getProfileProxy(this.context, mBluetoothServiceListener,
				BluetoothProfile.HEALTH)) {
			Toast.makeText(this.context, "bluetooth_health_profile_not_available",
					Toast.LENGTH_LONG).show();
			return;
		}

		Set<BluetoothDevice> btDevices = mBluetoothAdapter.getBondedDevices();
		for (BluetoothDevice device: btDevices){
			checkHDPProfile(device);
		}

		Logging.info("TODO: ieee_11073.part_20601.phd.channel.hdp.HDPManagerChannel register applications");
	}

	public void finish() {
		Logging.info("TODO: ieee_11073.part_20601.phd.channel.hdp.HDPManagerChannel finish() method");
	}

	// Callbacks to handle connection set up and disconnection clean up.
	private final BluetoothProfile.ServiceListener mBluetoothServiceListener =
		new BluetoothProfile.ServiceListener() {
		public void onServiceConnected(int profile, BluetoothProfile proxy) {
			if (profile == BluetoothProfile.HEALTH) {
				mBluetoothHealth = (BluetoothHealth) proxy;
				Logging.debug(TAG + " - onServiceConnected to profile: " + profile);
			}
		}

		public void onServiceDisconnected(int profile) {
			if (profile == BluetoothProfile.HEALTH) {
				mBluetoothHealth = null;
			}
		}
	};

	private void checkHDPProfile(BluetoothDevice device){
		try {
			/*
			 * Check for HDP Source profile (0x1401)
			 */
			Method m = device.getClass().getMethod("getUuids");
			ParcelUuid[] uuids = (ParcelUuid[])m.invoke(device);

			for (int i = 0; i < uuids.length; i++){
				if (uuids[i].getUuid().equals(HDP_SOURCE)){
					Logging.debug(TAG + " - The device " + device.getAddress() + " contains HDP Profile");

					/*
					 * The description of the agent will be on the format MAC/feature
					 * being feature the number of the corresponding profile (IEEE 11073)
					 * e.g. 4100 for the pulsioximeter
					 * */
					String description = device.getAddress() + "/ " + device.getBluetoothClass().getDeviceClass();
					Logging.debug(TAG + " - Creating agent with description: " + description);

					Agent a = new Agent(description);
					HDPManagedAgents.getInstance().addAgent(a);
				}
			}
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
	}

}

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

package ieee_11073.part_20601.phd.channel.bluetooth;

import java.util.ArrayList;
import java.util.Iterator;

import es.libresoft.hdp.HDPDevice;

public class HDPManagedDevices {

	private ArrayList<HDPConnection> devices;
	
	public HDPManagedDevices () {
		devices = new ArrayList<HDPConnection>();
	}
	
	public synchronized boolean addHDPDevice (HDPConnection newcon) {
		if (newcon == null)
			return true;
		return devices.add(newcon);
	}
	
	public synchronized boolean delHDPDevice (HDPConnection con) {
		if (con == null)
			return false;
		return devices.remove(con);
	}
	
	public synchronized HDPConnection getHDPConnection (HDPDevice dev) {		
		Iterator<HDPConnection> i = devices.iterator();
		HDPConnection obj;
		while (i.hasNext()) {
			obj = i.next();
			if (obj.getHDPDevice().equals(dev))
				return obj;
		}
		return null;
	}
}

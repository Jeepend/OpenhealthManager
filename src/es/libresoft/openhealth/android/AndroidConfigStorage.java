/*
Copyright (C) 2011 GSyC/LibreSoft, Universidad Rey Juan Carlos.

Author: Jose Antonio Santos Cadenas <jcaden@libresoft.es>

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

import ieee_11073.part_20601.asn1.GetResultSimple;

import java.util.Collection;

import es.libresoft.openhealth.DeviceConfig;
import es.libresoft.openhealth.storage.ConfigStorage;
import es.libresoft.openhealth.storage.StorageNotFoundException;

public class AndroidConfigStorage implements ConfigStorage {

	@Override
	public Collection<GetResultSimple> recover(byte[] sysId, DeviceConfig config) throws StorageNotFoundException {
		System.out.println("TODO: Implement storage recovery for Android platform");
		throw new StorageNotFoundException("This method is not yet implemented");
		// TODO Auto-generated method stub
	}

	@Override
	public void store(byte[] sysId, DeviceConfig config, GetResultSimple data) {
		System.out.println("TODO: Implement storage for Android platform");
		// TODO Auto-generated method stub
	}

}

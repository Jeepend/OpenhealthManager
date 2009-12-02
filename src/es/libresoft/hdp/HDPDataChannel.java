/*
Copyright (C) 2008-2009  Jose Antonio Santos Cadenas
email: jcaden@libresoft.es

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

package es.libresoft.hdp;

import ieee_11073.part_20601.phd.channel.Channel;

import java.io.InputStream;
import java.io.OutputStream;

public class HDPDataChannel extends Channel {

	public HDPDataChannel(InputStream input, OutputStream output)
			throws Exception {
		super(input, output);
		// TODO Auto-generated constructor stub
	}

	private HDPDevice dev;

	public InputStream getInputStream(){
		return null;
	}

	public OutputStream getOutputStream(){
		return null;
	}

	public void close(){

	}

	public void reconnect(){

	}

	public HDPDevice getDevice(){
		return this.dev;
	}

	@Override
	public void releaseChannel() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getChannelId() {
		// TODO Auto-generated method stub
		return 0;
	}
}

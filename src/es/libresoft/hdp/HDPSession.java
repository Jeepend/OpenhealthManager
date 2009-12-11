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

public class HDPSession {

	private HDPConfig config;
	private HDPCallbacks callbacks;

	public HDPSession(HDPConfig config, HDPCallbacks callbacks) throws Exception{
		this.config = config;
		this.callbacks = callbacks;
		//Call to native method to start HDP session
		if (!init_hdp())
			throw new Exception();
	}

	private native boolean init_hdp();

	/*
	 * Esperar HDPDevice
	 * Esperar HDPdc en HDPDevice
	 * Crear HDPdc en HDPDevice
	 * Borrar HDPdc
	 * Cerrar HDPdc
	 *
	 */

	public void close(){

	}
}

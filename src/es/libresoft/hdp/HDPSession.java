package es.libresoft.hdp;

public abstract class HDPSession {

	private HDPConfig config;
	private HDPCallbacks callbacks;

	public HDPSession(HDPConfig config, HDPCallbacks callbacks){
		this.config = config;
		this.callbacks = callbacks;
	}

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

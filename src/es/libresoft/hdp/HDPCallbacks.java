package es.libresoft.hdp;

public interface HDPCallbacks {

	public void new_device_connected(HDPDevice dev);
	public void device_disconected(HDPDevice dev);
	public void dc_connected(HDPDataChannel dc);
	public void dc_deleted(HDPDataChannel dc);
}

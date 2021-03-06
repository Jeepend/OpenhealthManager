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
package es.libresoft.openhealth;

import java.util.Iterator;
import java.util.Set;

import es.libresoft.openhealth.events.Event;
import es.libresoft.openhealth.events.InternalEventReporter;
import es.libresoft.openhealth.utils.DIM_Tools;
import ieee_11073.part_10101.Nomenclature;
import ieee_11073.part_20601.asn1.HANDLE;
import ieee_11073.part_20601.asn1.INT_U16;
import ieee_11073.part_20601.fsm.manager.ManagerStateController;
import ieee_11073.part_20601.phd.channel.InitializedException;
import ieee_11073.part_20601.phd.dim.IMDS_Handler;
import ieee_11073.part_20601.phd.dim.MDS;
import ieee_11073.part_20601.phd.dim.PM_Store;

public final class Agent extends Device{

	private ManagerStateController stc;
	private MDS mdsObj;
	private String system_id;

	public final IMDS_Handler mdsHandler = new IMDS_Handler(){
		@Override
		public synchronized MDS getMDS() {
			return mdsObj;
		}

		@Override
		public synchronized String setMDS(MDS mds) {
			if ((mdsObj == null) && (mds!=null)) {
				mdsObj = mds;
				system_id = DIM_Tools.byteArrayToString(
						(byte[])mds.getAttribute(Nomenclature.MDC_ATTR_SYS_ID).getAttributeType());
				//Send event using internal event report service
				InternalEventReporter.agentConnected(Agent.this);
				return system_id;
			}
			return null;
		}
	};


	public Agent() {
		super();
		stc = new ManagerStateController (mdsHandler);
		stc.configureController(this.inputQueue, this.outputQueue, this.eventQueue);
		try {
			stc.initFSMController();
		} catch (InitializedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public String getSystem_id(){return system_id;}

	@Override
	public void freeResources() {
		super.freeResources();
		stc.freeResources();
	}

	public void sendEvent(Event event){
		stc.processEvent(event);
	}

	@Override
	public boolean equals(Object o) {
		if (system_id==null){
			return false;
		} else if (o instanceof Agent){
			return system_id.equals(((Agent)o).getSystem_id());
		} else return false;
	}

	@Override
	public int hashCode() {
		return system_id.hashCode();
	}

	public Iterator<Integer> getPM_StoresHandlers() {
		return mdsObj.getPM_StoresHandlers().iterator();
	}

	public Iterator<Integer> getScannerHandlers() {
		return mdsObj.getScannerHandlers().iterator();
	}
}

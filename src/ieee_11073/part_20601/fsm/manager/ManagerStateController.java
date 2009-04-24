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
package ieee_11073.part_20601.fsm.manager;

import java.util.Timer;
import java.util.concurrent.Semaphore;

import es.libresoft.openhealth.events.Event;
import es.libresoft.openhealth.utils.IFIFO;
import es.libresoft.openhealth.utils.IUnlock;
import ieee_11073.part_20601.asn1.ApduType;
import ieee_11073.part_20601.fsm.State;
import ieee_11073.part_20601.fsm.StateController;
import ieee_11073.part_20601.fsm.StateHandler;
import ieee_11073.part_20601.phd.channel.InitializedException;
import ieee_11073.part_20601.phd.dim.MDS;


public class ManagerStateController implements StateController {

	private State state;
	private Semaphore semInputs = new Semaphore(0);
	private Semaphore semEvents = new Semaphore(0);
	
	private IFIFO<ApduType> inputQueue;
	private IFIFO<ApduType> outputQueue;
	private IFIFO<Event> eventQueue;
	
	private DispatcherApduThread dispatcher;
	private DispatcherEventThread dispatcherEvents;
	
	private MDS mds;
	
	private boolean initialized = false;
	private Timer timer;
	
	private StateHandler state_handler = new StateHandler(){

		@Override
		public void changeState(State newState) {
			java.text.SimpleDateFormat sdf =  new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SS");
			System.out.println(sdf.format(java.util.Calendar.getInstance().getTime()) + "\tState: " + newState.getStateName());
			state = newState;
		}

		@Override
		public MDS getMDS() {
			return mds;
		}

		@Override
		public void send(ApduType apdu) {
			outputQueue.add(apdu);
		}

		@Override
		public void setMDS(MDS newMds) {
			mds = newMds;			
		}

		@Override
		public void sendEvent(Event event) {
			eventQueue.add(event);
		}

		@Override
		public Timer getTimer() {
			return timer;
		}
		
	};
	
	private IUnlock dispatcherController = new IUnlock(){
		public void unlock() {
			semInputs.release();
		}
	};
	
	private IUnlock eventController = new IUnlock(){
		public void unlock() {
			semEvents.release();
		}
	};
	
	public ManagerStateController () {
		timer = new Timer();
		this.state = new MDisconnected(state_handler);
	}
	
	public void configureController(IFIFO<ApduType> inputQueue, IFIFO<ApduType> outputQueue, IFIFO<Event> eventQueue){
		//dev_handler = handler;
		this.eventQueue = eventQueue;
		this.eventQueue.setHandler(eventController);
		this.inputQueue = inputQueue;
		this.inputQueue.setHandler(dispatcherController);
		this.outputQueue = outputQueue;
	}
	
	public void initFSMController() throws InitializedException{
		if (!initialized){
			dispatcher = new DispatcherApduThread();
			dispatcher.start();
			dispatcherEvents = new DispatcherEventThread();
			dispatcherEvents.start();
			initialized = true;
		}else
			throw new InitializedException("Manager state controller is already initialized.");
	}
	
	public void freeResources (){
		dispatcher.interrupt();
		dispatcherEvents.interrupt();
	}
	public void processApdu (ApduType apdu){
		inputQueue.add(apdu);
	}
	
	public void processEvent(Event event) {
		eventQueue.add(event);
	}
	
	
	
	
	/**
	 * Receiver thread for the input channel
	 * @author sancane
	 */	
	public class DispatcherApduThread extends Thread {
		public void run() {
			boolean repeat = true;
			while(repeat) {
				try {
					semInputs.acquire();
					//Send input ADPU to finite state machine
					state.process(inputQueue.remove());
				} catch (InterruptedException e1) {
					System.out.println("Interrupted dispatcher Apdu thread");
					repeat = false;
				}catch (Exception e) {
					System.out.println("Exception dispatcher Apdu thread");
					repeat = false;
					e.printStackTrace();
				}
			}
			System.out.println("Exiting dispatcher");
		}
	}

	/**
	 * Receiver thread for events input channel
	 * @author sancane
	 */	
	public class DispatcherEventThread extends Thread {
		public void run() {
			boolean repeat = true;
			while(repeat) {
				try {
					semEvents.acquire();
					//Send input Event to finite state machine
					state.processEvent(eventQueue.remove());
				} catch (InterruptedException e1) {
					System.out.println("Interrupted dispatcher Events thread");
					repeat = false;
				}catch (Exception e) {
					System.out.println("Exception dispatcher Events thread");
					repeat = false;
					e.printStackTrace();
				}
			}
			System.out.println("Exiting dispatcher event");
		}
	}
}

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
package ieee_11073.part_20601.phd.channel;

import es.libresoft.openhealth.events.Event;
import es.libresoft.openhealth.events.EventType;
import es.libresoft.openhealth.utils.*;
import ieee_11073.part_20601.asn1.ApduType;

import java.util.concurrent.Semaphore;

public class VirtualChannel {
	
	private Channel[] channels;
	private IFIFO<ApduType> outputQueue;
	private IFIFO<Event> eventQueue;
	private boolean initialized = false;
	
	private SenderThread sender;	
	private Semaphore sem = new Semaphore(0);
	
	private IUnlock senderController = new IUnlock(){
		public void unlock() {
			sem.release();
		}
	};
	
	private ChannelEventHandler eventHandler = new ChannelEventHandler(){
		@Override
		public synchronized void processEvent(Event e) {
			if (e.getTypeOfEvent()==EventType.IND_TRANS_DESC) {				
				//interrupt all threads blocked in input channels
				for (int i=0; i < channels.length; i++){
					channels[i].setReceiverStatus(false);
				}
				//interrupt sender thread
				sender.interrupt();
				//Send disconnected event to fsm
				eventQueue.add(e);
			}
		}
	};
		
	public VirtualChannel (Channel channel) {
		channels = new Channel[1];
		channels[0] = channel;
	}
	
	public VirtualChannel (Channel[] channels) {
		this.channels = channels;
	}
	
	public void configureChannels(IFIFO<ApduType> inputQueue, IFIFO<ApduType> outputQueue, IFIFO<Event> eventQueue) throws InitializedException {
		if (initialized)
			throw new InitializedException("VirtualChannel is already initialized.");
		
		this.eventQueue = eventQueue;
		this.outputQueue = outputQueue;
		this.outputQueue.setHandler(senderController);
		
		for (int i=0; i < channels.length; i++){
			channels[i].configureChannel(i,inputQueue,eventHandler);
		}
		sender = new SenderThread();
		sender.start();
		
		//VirualChannel is ready to send and receive APDUs 
		eventQueue.add(new Event(EventType.IND_TRANS_CONN));
		initialized=true;
	}
	
	public class SenderThread extends Thread { 
		
		public void run() {
			boolean repeat = true;
			while(repeat) {
				try {
					sem.acquire();
					channels[0].sendAPDU(outputQueue.remove());
				} catch (InterruptedException e) {
					System.out.println("Interrupted sender");
					repeat = false;
				}catch (Exception e) {
					System.out.println("Exception sender thread");
					repeat = false;
					e.printStackTrace();
				}
			}
			System.out.println("Exiting sender");
		}
	}
}

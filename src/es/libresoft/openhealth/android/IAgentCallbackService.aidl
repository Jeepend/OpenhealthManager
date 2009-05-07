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

package es.libresoft.openhealth.android;

import es.libresoft.openhealth.android.Measure;

/**
 * Agent notification interface, all clients registered with the remote service, will be
 * notified when next events occurs in the agent.
 * Note that this is a one-way interface so the server does not block waiting for the client.
 */
oneway interface IAgentCallbackService {
    /**
     * Called when agent change of state.
     */
    void agentStateChanged (String state);
    
    /**
     * Called when manager receives a measure from agent.
     */
    void agentMeasureReceived (String system_id, out List<Measure> measures);
}
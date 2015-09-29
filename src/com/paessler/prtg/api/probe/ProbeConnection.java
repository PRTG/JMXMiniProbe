/*
 * Copyright (c) 2014, Paessler AG <support@paessler.com>
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 * 3. Neither the name of the copyright holder nor the names of its contributors
 *    may be used to endorse or promote products derived from this software without
 *    specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
 * ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.paessler.prtg.api.probe;

import java.util.List;

import com.google.gson.Gson;
import com.paessler.prtg.api.PRTGServer;
import com.paessler.prtg.jmx.Logger;
import com.paessler.prtg.jmx.definitions.SensorDefinition;
import com.paessler.prtg.jmx.network.NetworkWrapper;
import com.paessler.prtg.jmx.responses.DataResponse;

public class ProbeConnection {

	protected PRTGServer serverConnection;
	protected String	probeName;
	protected String	probeVersion;
	
	public String getProbeVersion() {
		return probeVersion;
	}

	public void setProbeVersion(String probeVersion) {
		this.probeVersion = probeVersion;
	}

	public PRTGServer getServerConnection() {
		return serverConnection;
	}

	public void setServerConnection(PRTGServer serverConnection) {
		this.serverConnection = serverConnection;
	}

	public String getProbeName() {
		return probeName;
	}

	public void setProbeName(String probeName) {
		this.probeName = probeName;
	}

	// ------------------------------------------------------------------------------------------
	public ProbeConnection(PRTGServer server){
		setServerConnection(server);
		setProbeName("PRTG JMX Probe");
		setProbeVersion("14.3.1");
	}
	
	// ------------------------------------------------------------------------------------------
	public boolean sendAnnouncement(List<SensorDefinition> definitions, int interval){
		boolean retVal = false;
			Announcement announcement = new Announcement(getProbeName(), getProbeVersion(), interval, definitions);
			String url = announcement.buildUrl(getServerConnection());
			try {
			    String postBody = announcement.toString();
			    Logger.log("Sending: " + postBody);
			    NetworkWrapper.post(url, postBody);
			    retVal =  true;
			} catch (Exception e) {
			    Logger.log(e.getLocalizedMessage());
			}
		return retVal;
	}
	
	// ------------------------------------------------------------------------------------------
    protected boolean sendResponse(String dataJson){
    	boolean retVal = false;
        String dataUrl =  getServerConnection().getURLPrefix("/probe/data");
        if (dataJson.length() > 0) {
            if(getServerConnection().getDebugLevel() > 0){
                Logger.log("Debug posting sensor data: " + dataUrl+"  "+dataJson);
            }
            try {
                NetworkWrapper.post(dataUrl, dataJson);
                retVal = true;
            } catch (Exception e) {
                Logger.log("Error sending data: " + e.getLocalizedMessage());
	             if(getServerConnection().getDebugLevel() > 1){
	            	 e.printStackTrace();
	             }
            }
        }
        return retVal;
    }
	
}

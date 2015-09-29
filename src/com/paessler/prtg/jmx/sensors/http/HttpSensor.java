/*
 * Copyright (c) 2015, Paessler AG <support@paessler.com>
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
package com.paessler.prtg.jmx.sensors.http;

import java.net.SocketTimeoutException;

import javax.net.ssl.SSLException;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.client.CloseableHttpClient;

import com.google.gson.JsonObject;
import com.paessler.prtg.jmx.Logger;
import com.paessler.prtg.jmx.channels.Channel;
import com.paessler.prtg.jmx.channels.LongChannel;
import com.paessler.prtg.jmx.definitions.SensorDefinition;
import com.paessler.prtg.jmx.responses.DataResponse;
import com.paessler.prtg.jmx.sensors.RemoteSensor;
import com.paessler.prtg.jmx.sensors.Sensor;
import com.paessler.prtg.util.TimingUtility;

public class HttpSensor extends RemoteSensor<HTTPEntry> {
	protected int			delay = 0;
	protected int			portTimeout = 50;
	protected String		authenticationMethod = "BASIC";
	protected String		httpMethod = "GET";

	public String getAuthenticationMethod() {return authenticationMethod;}
	public void setAuthenticationMethod(String val) {this.authenticationMethod = val;}
	public String getHttpMethod() {	return httpMethod;}
	public void setHttpMethod(String httpMethod) {this.httpMethod = httpMethod;	}

	// -------------------------------
	public int getDelay() 			{return delay;}
	public void setDelay(int delay) {this.delay = delay;}
	// --------------------------------------
	public HttpSensor(){
		super();
		setDefinition(new HttpSensorDefinition());
		setKind(HttpSensorDefinition.KIND);
		setSensorName("HttpSensor");
}
	// --------------------------------------
	public HttpSensor(HttpSensor tocpy){
		super(tocpy);
		delay = tocpy.delay;
		portTimeout = tocpy.portTimeout;
		authenticationMethod = tocpy.authenticationMethod;
		httpMethod = tocpy.httpMethod;
		
		
	}
	//----------------------------------------------------------------------
    @Override
    public Sensor copy(){
		return new HttpSensor(this);
    }
	
	
	//----------------------------------------------------------------------
	//----------------------------------------------------------------------
	@Override
	protected void init(){
		super.init();
		setInitialized(true);
	}
	
	
	//----------------------------------------------------------------------
	protected Channel issueHTTPRequest(HTTPEntry entry) throws Exception{
		LongChannel retVal = new LongChannel(entry.getDescription(), Channel.Unit.TIME_RESPONSE, 0, Channel.Mode.INTEGER);
		TimingUtility sensorcreationtimer = new TimingUtility();
		HttpUriRequest   req = null;
		CloseableHttpClient cli = null;
		CloseableHttpResponse ret = null;
		try{
	        HttpClientContext localContext = HttpClientContext.create();
	        localContext.setAuthCache(entry.getAuthCache());
	 
			req = entry.getHttpRequest();
			cli = entry.getCloseableHttpClient();
//			ret = cli.execute(req, localContext);
			ret = cli.execute(req);
			retVal.setValue(sensorcreationtimer.getElapsed());
		} catch(SocketTimeoutException e){
			retVal.setValue(sensorcreationtimer.getElapsed());
			retVal.setError("Socket");
			retVal.setWarning(1);
			retVal.setMessage(e.getMessage());
		} catch(SSLException e){
			retVal.setValue(sensorcreationtimer.getElapsed());
			retVal.setError("SSLException");
			retVal.setWarning(1);
			retVal.setMessage(e.getMessage());
		} catch(Exception e){
			retVal.setValue(sensorcreationtimer.getElapsed());
			retVal.setError("Exception");
			retVal.setWarning(1);
			retVal.setMessage(e.getMessage());
		} finally
		{
			if(ret != null){
				ret.close();
			}
			cli.close();
		}
		return retVal;
	}
	//----------------------------------------------------------------------
	@Override
	public DataResponse go() {
        DataResponse response = new DataResponse(sensorid, HttpSensorDefinition.KIND);
        if(!isInitialized()){
        	init();
        }
        if(isInitialized()){
        	Channel tmpchannel = null;
        	HTTPEntry err = null;
        	if(getVectorOfValues().size() > 0){
	        	try{
//	        		boolean isfirst = true;
	        		for(HTTPEntry curr: getVectorOfValues()){
	        			tmpchannel = issueHTTPRequest(curr);
	        			if(tmpchannel != null){
	        				response.addChannel(tmpchannel);
	        			}
	        		}
	        	}
	        	catch(Exception e){
	    			response = getErrorResponse("HTTP ["+err+"] Exception", -1, e.getLocalizedMessage());
	        	}
        	} else{
    			response = getErrorResponse("Configuration Error", -1, "No URL defined");
        	}
        } else {
			response = getErrorResponse("Init Error", -1, "HTTP Sensor not initialized");
        }
		// TODO Auto-generated method stub
		return response;
	}
	
	//----------------------------------------------------------------------
	@Override
	public SensorDefinition getDefinition() {
		// TODO Auto-generated method stub
		return new HttpSensorDefinition();
	}

	// ---------------------------
	protected void addVectorEntry(String value, boolean inoutonly){
		// Expecting int port numbers
		HTTPEntry tmp = HTTPEntry.httpEntryFactory(value, getHttpMethod(), getAuthenticationMethod());
		if(tmp != null){
			tmp.setUsername(getUsername());
			tmp.setPassword(getPassword());
			addVectorEntry(tmp);
		}
	}
	
	//----------------------------------------------------------------------
	@Override
	public void loadFromJson(JsonObject json)  throws Exception{
		// Local
//		TRAFFIC_SENSOR_VALS
//    	JsonElement tmpJSON = null;
    	try{

            // -------------------------
    		int tmp = getJsonElementInt(json, HttpSensorDefinition.FIELD_HTTP_METHOD, -1);
    		if(tmp != -1){
    			setHttpMethod(HttpSensorDefinition.getMethod(tmp));
    		}
    		// ----------------------------------------
    		tmp = getJsonElementInt(json, HttpSensorDefinition.FIELD_HTTP_AUTHMETH, -1);
    		if(tmp != -1){
    			setAuthenticationMethod(HttpSensorDefinition.getAuthentication(tmp));
    		}
    		// ----------------------------------------

            // Set Vector name to be scanned by parrent
            setVectorPropertyName(HttpSensorDefinition.FIELD_HTTP_VECTOR); // URL
            
            // Fields TimeoutField, PortField handled by RemoteSensor
            // Fields Username, password handled by Sensor
    		// Delegate to parent
    		super.loadFromJson(json);
                        
            
        } catch (Exception e) {
            Logger.log("Error parsing sensor["+getName()+"] JSON##"+json+"##: " + e.getLocalizedMessage());
            throw e;
        }
		
        
        init();
	}

}

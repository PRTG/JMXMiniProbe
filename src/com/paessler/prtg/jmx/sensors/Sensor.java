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

package com.paessler.prtg.jmx.sensors;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.paessler.prtg.jmx.Logger;
import com.paessler.prtg.jmx.channels.Channel;
import com.paessler.prtg.jmx.definitions.SensorConstants;
import com.paessler.prtg.jmx.definitions.SensorDefinition;
import com.paessler.prtg.jmx.responses.DataError;
import com.paessler.prtg.jmx.responses.DataResponse;
import com.paessler.prtg.jmx.sensors.profile.Profile;
import com.paessler.prtg.jmx.tasks.TaskFetcherTask;

public abstract class Sensor implements Runnable{
    public int sensorid = -1;
    protected String host;
//	protected int 	 port;
    protected String username;
    protected String password;
    protected String kind = "Sensor";
    protected SensorDefinition sensordefinition;
	protected String		sensorName = null;
	// --------------------------------------------------------
    public Sensor() {
    	super();
    }
	// --------------------------------------------------------
    public Sensor(Sensor tocopy) {
    	super();
		sensorName  = tocopy.sensorName;
    	username = tocopy.username;
    	password = tocopy.password;
    	host = tocopy.host;
//    	port = tocopy.port;
        kind = tocopy.kind;
        sensordefinition = tocopy.sensordefinition;
    }
    public abstract Sensor copy();
    // --------------------------------------------------------------------------------------------
	public String getSensorName() {return sensorName;}
	public void setSensorName(String name) {sensorName = name;}
	// --------------------------------------------------------
	public String getKind() {return kind;}
	public void   setKind(String kind) {this.kind = kind;}
	// --------------------------------------------------------
	public String 	getName() 			{return getKind();}
	// --------------------------------------------------------
//	public int getPort() {return port;}
//	public void setPort(int port) {	this.port = port;	}
	// ----------------------
	public String getHost() {	return host;}
	public void setHost(String host) {	this.host = host;}

	// ----------------------------------------------------------------------
	public String toString() {return getSensorName()+";"+getKind()+"["+getSensorid()+"]";}
	// ----------------------------------------------------------------------
    public SensorDefinition getDefinition() 	{return sensordefinition;};
    public void  setDefinition(SensorDefinition def) {sensordefinition = def;};

    protected TaskFetcherTask controllerTask;
	// ----------------------------
	public TaskFetcherTask getControllerTask() {
		return controllerTask;
	}
	// ----------------------------
	public void setControllerTask(TaskFetcherTask controllerTask) {
		this.controllerTask = controllerTask;
	}
	// ----------------------------
	protected void returnResult(DataResponse response) {
		TaskFetcherTask task = getControllerTask();
		if(task != null && response != null ){
			task.addDataResponse(this, response);
		}
	}
	
	// --------------------------------------------------------
	public DataResponse getDataResponse(String errortype, int errorcode, String message){
		return new DataResponse(getSensorid(), getKind());
	}
	// --------------------------------------------------------
	public DataError getErrorResponse(String errortype, int errorcode, String message){
		DataError retVal = new DataError(getSensorid(), getName());
		retVal.setCode(errorcode);
		retVal.setError(errortype);
		retVal.setMessage(message);
		return retVal;
	}
	
	// --------------------------------------------------------
	protected String jsonString = null;
	// ----------------------------
	public String getJsonString() {	return jsonString;	}
	// ----------------------------
	protected void setJsonString(String jsonString) {
		this.jsonString = jsonString;
	}
	// ----------------------------
	protected void setJsonString(JsonObject json) {
		this.jsonString = json.toString();
	}
	// ----------------------------
	public boolean isSame(String json){
		boolean retVal = false;
		if(jsonString != null && json.equals(jsonString)){
			retVal = true;
		}
		return retVal;
	}
	// ----------------------------
	public boolean isSame(JsonObject json){
		boolean retVal = false;
		if(json != null ){
			retVal = isSame(json.toString());
		}
		return retVal;
	}
    public int getSensorid() {
		return sensorid;
	}
	public void setSensorid(int sensorId) {
		this.sensorid = sensorId;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
	// ------------------------------------------------------------------
	public static JsonElement getJsonElement(JsonObject json, String element){
    	JsonElement retVal = null;
		retVal = json.get(element);
        if (retVal != null && retVal.isJsonNull()) {
        	retVal = null;
        }
		return retVal;
	}
	// ------------------------------------------------------------------
	public static String getJsonElementString(JsonObject json, String element){
		String retVal = null;
		JsonElement val = getJsonElement(json, element);
        if (val != null) {
        	retVal = val.getAsString();
        }
		return retVal;
	}
	// ------------------------------------------------------------------
	public static int getJsonElementInt(JsonObject json, String element, int defval){
		int retVal = defval;
		String val = getJsonElementString(json, element);
        if (val != null && !val.isEmpty()) {
        	retVal = Integer.parseInt(val);
        }
		return retVal;
	}
	// ------------------------------------------------------------------
	public static long getJsonElementLong(JsonObject json, String element, long defval){
		long retVal = defval;
		String val = getJsonElementString(json, element);
        if (val != null && !val.isEmpty()) {
        	retVal = Long.parseLong(val);
        }
		return retVal;
	}
	// ----------------------------------------------------------------------
/*    
        rmiGroupDefinition.fields.add( new SimpleEditFieldDefinition(SensorConstants.USERNAME, "Username/Login"));
        rmiGroupDefinition.fields.add( new SimpleEditFieldDefinition(SensorConstants.PASSWORD, "Password"));

    protected SensorDefinition definition;
    protected SensorDefinition setDefinition(SensorDefinition def){ definition = def;}
    public SensorDefinition getDefinition() { return definition;};
*/    
    
	public void run(){
		DataResponse retVal = null;
	try {
		//		System.out.println(Thread.currentThread().getName()+" Start. "+getName());
			retVal = go();
			if(retVal == null){
				retVal = new DataError(getSensorid(), getName());
				retVal.addMessage("Sensor failed, NULL return");
			} else if(retVal.getChannelCount() < 1){
				retVal = new DataError(getSensorid(), getName());
				retVal.addMessage("Sensor returned no data points, Empty return");
			} else {
				String msg = null;
				for(Channel curr : retVal.getChannels()){
					msg = curr.getMessage();
					if(msg != null){
						retVal.addMessage(msg+"["+curr.getName()+"]");
					}
				}
			}
		//		System.out.println(Thread.currentThread().getName()+" Stop. "+getName());
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			DataError error = new DataError(getSensorid(), getName());
			retVal = error;
            error.setError("Exception");
            error.addMessage("Sensor "+getName()+" Cought exception "+e.getMessage());
			Logger.log(error.getMessage());
			e.printStackTrace();
		}
		returnResult(retVal);

	}
	
    public abstract DataResponse go();
	// ----------------------------------------------------------------------
//    public abstract void loadFromJson(JsonObject json);
    public void loadFromJson(JsonObject json) throws Exception {
    	JsonElement tmpJSON = null;
    	try{
        	String tmpval = getJsonElementString(json, SensorConstants.USERNAME);
            if (tmpval != null && !tmpval.isEmpty()) {
	            setUsername(tmpval);
	        }
	
        	tmpval = getJsonElementString(json, SensorConstants.PASSWORD);
            if (tmpval != null && !tmpval.isEmpty()) {
	            setPassword(tmpval);
	        }
            setSensorid(getJsonElementInt(json, SensorConstants.SENSORID, getSensorid()));
        	tmpval = getJsonElementString(json, SensorConstants.HOST);
            if (tmpval != null && !tmpval.isEmpty()) {
	            setHost(tmpval);
	        }
//        	tmpval = getJsonElementString(json, SensorConstants.PORT);
//            if (tmpval != null && !tmpval.isEmpty()) {
//                setPort(getJsonElementInt(json, SensorConstants.PORT, getPort()));
//	        }
            // Set JSON for change compare
	    	setJsonString(json);
        } catch (Exception e) {
            Logger.log("Error parsing sensor["+toString()+"] JSON##"+json+"##: " + e.getLocalizedMessage());
            throw e;
        }
    }
	// ----------------------------------------------------------------------
    public void loadFrom(Profile profile) {
    	
    	setSensorName(profile.getName());
    	setKind(profile.getKind());
    	String tmptag = profile.getTag();
//    	String tmp2 = profile.getTags();
//    	if()
    	
    }
    
}

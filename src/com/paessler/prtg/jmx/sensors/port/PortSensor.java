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
package com.paessler.prtg.jmx.sensors.port;

import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;
import com.google.gson.JsonObject;
import com.paessler.prtg.jmx.Logger;
import com.paessler.prtg.jmx.channels.Channel;
import com.paessler.prtg.jmx.definitions.IPPortField;
import com.paessler.prtg.jmx.responses.DataResponse;
import com.paessler.prtg.jmx.sensors.RemoteSensor;
import com.paessler.prtg.jmx.sensors.Sensor;
import com.paessler.prtg.jmx.sensors.profile.Attribute;
import com.paessler.prtg.jmx.sensors.profile.Entry;
import com.paessler.prtg.jmx.sensors.profile.IntegerAttribute;
import com.paessler.prtg.jmx.sensors.profile.Profile;
import com.paessler.prtg.util.NumberUtility;
import com.paessler.prtg.util.TimingUtility;

public class PortSensor extends RemoteSensor<IntegerAttribute> {
	protected int			delay = 0;
	protected int			portTimeout = 50;

	
	// -------------------------------
	public int getDelay() 			{return delay;}
	public void setDelay(int delay) {this.delay = delay;}
	// --------------------------------------
	public PortSensor(){
		super();
		setDefinition(new PortSensorDefinition());
		setKind(PortSensorDefinition.KIND);
		setSensorName("PortSensor");
}
	// --------------------------------------
	public PortSensor(PortSensor tocpy){
		super(tocpy);
		delay = tocpy.delay;
		portTimeout = tocpy.portTimeout;
	}
	//----------------------------------------------------------------------
    @Override
    public Sensor copy(){
		return new PortSensor(this);
    }
	
	//----------------------------------------------------------------------
	//----------------------------------------------------------------------
	@Override
	protected void init(){
		super.init();
		setInitialized(true);
	}
	
	//----------------------------------------------------------------------
	protected Channel connectToPort(IntegerAttribute attr) throws Exception{
		Socket s = new Socket();
		int port = attr.getObject();
		Channel retVal = null;
		TimingUtility sensorcreationtimer = new TimingUtility();
		try{
			s.connect(new InetSocketAddress(getHost(), port), (int)getTimeout());
			retVal = attr.getChannel(sensorcreationtimer.getElapsed());
		} catch(SocketTimeoutException e){
			retVal= attr.getChannel(sensorcreationtimer.getElapsed());
			retVal.setError("Socket");
			retVal.setWarning(1);
			retVal.setMessage(e.getMessage());
		} catch(Exception e){
			retVal= attr.getChannel(sensorcreationtimer.getElapsed());
			retVal.setError("Exception");
			retVal.setWarning(1);
			retVal.setMessage(e.getMessage());
		} finally
		{s.close();}
		return retVal;
	}
	//----------------------------------------------------------------------
	@Override
	public DataResponse go() {
        DataResponse response = new DataResponse(sensorid, PortSensorDefinition.KIND);
        if(!isInitialized()){
        	init();
        }
        if(isInitialized()){
        	Channel tmpchannel = null;
        	Integer errport = null;
        	if(getVectorOfValues().size() > 0){
	        	try{
	        		boolean isfirst = true;
	        		for(IntegerAttribute curr:getVectorOfValues()){
	        			if(curr.isEnabled()){
		        			if(!isfirst && getDelay() > 0){
		        				Thread.sleep(getDelay());
		        			}
		        			errport = curr.getObject(); 
		        			tmpchannel = connectToPort(curr);
		        			if(tmpchannel != null){
		        				response.addChannel(tmpchannel);
		        			}
	        			}
	        		}
	        	}
	        	catch(Exception e){
	    			response = getErrorResponse("Open Port["+errport+"] Exception", -1, e.getLocalizedMessage());
	        	}
        	} else{
    			response = getErrorResponse("Configuration Error", -1, "Port No ports defined");
        	}
        } else {
			response = getErrorResponse("Init Error", -1, "Port Range Sensor not initialized");
        }
		// TODO Auto-generated method stub
		return response;
	}
	
	// ---------------------------
	protected void addVectorEntry(String value, boolean inoutonly){
		// Expecting int port numbers
		int tmp = NumberUtility.convertToInt(value, -1);
		if(IPPortField.MIN_PORT_VALUE  <= tmp && tmp < IPPortField.MAX_PORT_VALUE ){
			
			String channelname = "Port: "+tmp;
//			LongChannel retVal = new LongChannel(channelname, Channel.Unit.TIME_RESPONSE, 0, Channel.Mode.INTEGER);

			IntegerAttribute attr = new IntegerAttribute();
			attr.setUnit(Channel.UNIT_STR_TRESPONSE);
			attr.setDescription(channelname);
			attr.setObject(tmp);
			addVectorEntry(attr);
		}
	}
	
	//----------------------------------------------------------------------
	@Override
	public void loadFromJson(JsonObject json)  throws Exception{
		// Local
//		TRAFFIC_SENSOR_VALS
//    	JsonElement tmpJSON = null;
    	try{

    		this.setDelay(getJsonElementInt(json, PortSensorDefinition.FIELD_DELAY, this.getDelay()));

            // Set Vector name to be scanned by parrent
            setVectorPropertyName(PortSensorDefinition.FIELD_PORT_VECTOR);
            
    		// Delegate to parent
    		super.loadFromJson(json);
                        
            
        } catch (Exception e) {
            Logger.log("Error parsing sensor["+getName()+"] JSON##"+json+"##: " + e.getLocalizedMessage());
            throw e;
        }
		
        
        init();
	}
	// ----------------------------------------------------------------------
    @Override
    public void loadFrom(Profile profile) {
    	String tmptag = profile.getTag();
    	PortSensorDefinition def =  new PortSensorDefinition(profile.getKind(), profile.getName(), profile.getDescription(), 
    			tmptag, profile.getHelp(), false);
    	setDefinition(def);
    	super.loadFrom(profile);

    	IntegerAttribute attr;
    	for(Entry curr : profile.getEntries()){
        	for(Attribute<?> curra : curr.getAttributes()){
            	attr = new IntegerAttribute(curra);
        		if(attr != null){
        			addVectorEntry(attr);
        		}
        		
        	}
    	}
    }

}

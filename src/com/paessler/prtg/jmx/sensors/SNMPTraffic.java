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

/**
 * @Author JR Andreassen
 */
package com.paessler.prtg.jmx.sensors;

import java.util.Map;

import org.snmp4j.smi.OID;
import org.snmp4j.smi.Variable;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.paessler.prtg.jmx.Logger;
import com.paessler.prtg.jmx.channels.Channel;
import com.paessler.prtg.jmx.channels.LongChannel;
import com.paessler.prtg.jmx.channels.Channel.Unit;
import com.paessler.prtg.jmx.sensors.snmp.SNMPSensorDefinition;
import com.paessler.prtg.jmx.sensors.snmp.SNMPTrafficDef;
import com.paessler.prtg.jmx.definitions.SensorDefinition;
import com.paessler.prtg.jmx.responses.DataResponse;
import com.paessler.prtg.jmx.sensors.snmp.SNMPGetHolder;
import com.paessler.prtg.util.NumberUtility;
import com.paessler.prtg.util.snmp.SNMPUtil.SNMPCounterType;

public class SNMPTraffic extends SNMPSensor {
	
	protected boolean			sumChannels = false;
	protected SNMPCounterType	trafficCounterType = SNMPCounterType.Counter64bit;   
	
	// --------------------------------------
	public SNMPCounterType getTrafficCounter()	{return trafficCounterType;}
	public void setTrafficCounter(SNMPCounterType val){
		trafficCounterType = val;
	}
	public void setTrafficCounter(int val){
		if(val == 1){
			trafficCounterType = SNMPCounterType.Counter32bit;
		} else {
			trafficCounterType = SNMPCounterType.Counter64bit;
		}
	}
	
	// --------------------------------------
	public boolean getSumChannels()	{return sumChannels;}
	public void setSumChannels(boolean val){
		sumChannels = val;
	}
	
	// --------------------------------------
	public SNMPTraffic(){
		super();
		setDefinition(new SNMPTrafficDef());
		setKind(SNMPTrafficDef.KIND);
		setSensorName("SNMPTraffic");
	}
	// --------------------------------------
	public SNMPTraffic(SNMPTraffic tocpy){
		super(tocpy);
		sumChannels = tocpy.sumChannels;
	}
	
	//----------------------------------------------------------------------
    @Override
    public Sensor copy(){
		return new SNMPTraffic(this);
    }
	
	//----------------------------------------------------------------------
	//----------------------------------------------------------------------
	@Override
	protected void init(){
		super.init();
	}
	
	//----------------------------------------------------------------------
	@Override
	public SensorDefinition getDefinition() {
		// TODO Auto-generated method stub
		return new SNMPTrafficDef();
	}

	// --------------------------------------------------------------
	protected void fillChannels(DataResponse response, Map<OID, Variable> vars){
		super.fillChannels(response, vars);
		if(getSumChannels()){
			long value = 0;
	    	for(Channel curr : response.getChannels()){
	    		if(curr instanceof LongChannel){
	    			value += ((LongChannel)curr).toLong();
	    		}
	    	}
    		response.addChannel(new LongChannel("Total", Unit.BANDWIDTH, value, Channel.Mode.COUNTER));
		}		
	}
	
	
	// ---------------------------
	protected void addVectorEntry(String value, boolean inoutonly){
		int currint = Integer.parseInt(value);
		SNMPGetHolder tmp = new SNMPGetHolder(currint, true, getTrafficCounter());
		if(tmp != null)
			addVectorIndex(tmp);
	}
	
	//----------------------------------------------------------------------
	@Override
	public void loadFromJson(JsonObject json)  throws Exception{
		// Local
//		TRAFFIC_SENSOR_VALS
    	try{
	        String tmpval = getJsonElementString(json, SNMPTrafficDef.SUM_CHANNELS);
            if (tmpval != null && !tmpval.isEmpty()) {
            	int tmpVal = NumberUtility.getInt(tmpval, 0);
	        	this.setSumChannels(tmpVal == 1);
	        }
            
	        tmpval = getJsonElementString(json, SNMPTrafficDef.TRAFFIC_COUNTER);
            if (tmpval != null && !tmpval.isEmpty()) {
            	int tmpVal = NumberUtility.getInt(tmpval, 0);
	        	this.setTrafficCounter(tmpVal);
	        }
            
        } catch (Exception e) {
            Logger.log("Error parsing sensor["+getName()+"] JSON##"+json+"##: " + e.getLocalizedMessage());
            throw e;
        }
		
		// Delegate to parent
		super.loadFromJson(json);
        
        init();
	}

}

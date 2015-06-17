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
package com.paessler.prtg.jmx.sensors.dns;

import java.net.Socket;

import com.google.gson.JsonObject;
import com.paessler.prtg.jmx.Logger;
import com.paessler.prtg.jmx.channels.Channel;
import com.paessler.prtg.jmx.channels.LongChannel;
import com.paessler.prtg.jmx.definitions.SensorDefinition;
import com.paessler.prtg.jmx.responses.DataResponse;
import com.paessler.prtg.jmx.sensors.RemoteSensor;
import com.paessler.prtg.jmx.sensors.Sensor;
import com.paessler.prtg.util.TimingUtility;

import org.xbill.DNS.Lookup;
import org.xbill.DNS.Record;
import org.xbill.DNS.TextParseException;
import org.xbill.DNS.Type;

public class DNSSensor extends RemoteSensor<DNSLookupEntry> {
	protected int			lookupType = Type.A;

	
	// -------------------------------
	public int	getLookupType() 				{return lookupType;}
	public void setLookupType(int lookuptype)	{this.lookupType = lookuptype;}
	// --------------------------------------
	public DNSSensor(){
		super();
		setDefinition(new DNSSensorDefinition());
		setKind(DNSSensorDefinition.KIND);
		setSensorName("DNSSensor");
	}
	// --------------------------------------
	public DNSSensor(DNSSensor tocpy){
		super(tocpy);
		lookupType = tocpy.lookupType;
	}
	
	//----------------------------------------------------------------------
    @Override
    public Sensor copy(){
		return new DNSSensor(this);
    }
	
	//----------------------------------------------------------------------
	//----------------------------------------------------------------------
	@Override
	protected void init(){
		super.init();
		setInitialized(true);
	}
	
	//----------------------------------------------------------------------
	protected Channel resolveDNSRecord(DNSLookupEntry dnsentry) throws Exception{
		Channel retVal = null;
		TimingUtility sensorcreationtimer = new TimingUtility();
		Lookup lkup = null;
//		Record [] records = null;
		{
			lkup = new Lookup(dnsentry.getName(), dnsentry.getType());
//			records = lkup.run();
			lkup.run();
			retVal = new LongChannel(dnsentry.toString(), Channel.Unit.TIME_RESPONSE, sensorcreationtimer.getElapsed(), Channel.Mode.INTEGER);
			switch(lkup.getResult()) {
				case  Lookup.SUCCESSFUL:
					break;
				case  Lookup.HOST_NOT_FOUND:
					retVal.setMessage("Host Record not found");
					retVal.setError("HOST_NOT_FOUND");
					retVal.setWarning(1);
					break;
				case  Lookup.UNRECOVERABLE:
					retVal.setMessage("Unrecoverable Error");
					retVal.setError("Response");
					retVal.setWarning(1);
					break;
				case  Lookup.TRY_AGAIN:
					retVal.setError("Response");
					retVal.setWarning(1);
					break;
				case  Lookup.TYPE_NOT_FOUND:
					retVal.setMessage("DNS Record Type Not found");
					retVal.setError("Response");
					retVal.setWarning(1);
					break;
					
			}
		}
		return  retVal;
	}
	//----------------------------------------------------------------------
	@Override
	public DataResponse go() {
        DataResponse response = new DataResponse(sensorid, DNSSensorDefinition.KIND);
        if(!isInitialized()){
        	init();
        }
        if(isInitialized()){
        	Channel tmpchannel = null;
        	DNSLookupEntry err = null;
        	if(getVectorOfValues().size() > 0){
	        	try{
//	        		boolean isfirst = true;
	        		for(DNSLookupEntry curr:getVectorOfValues()){
	        			err = curr; 
	        			tmpchannel = resolveDNSRecord(curr);
	        			if(tmpchannel != null){
	        				response.addChannel(tmpchannel);
	        			}
	        		}
	        	}
	        	catch(Exception e){
	    			response = getErrorResponse("Resolve DNS["+err+"] Exception", -1, e.getLocalizedMessage());
	        	}
        	} else{
    			response = getErrorResponse("Configuration Error", -1, "No Domain names defined");
        	}
        } else {
			response = getErrorResponse("Init Error", -1, "DNS Sensor not initialized");
        }
		// TODO Auto-generated method stub
		return response;
	}
	
	//----------------------------------------------------------------------
	@Override
	public SensorDefinition getDefinition() {
		// TODO Auto-generated method stub
		return new DNSSensorDefinition();
	}

	// ---------------------------
	protected void addVectorEntry(String value, boolean inoutonly){
		// Expecting int port numbers
		DNSLookupEntry tmp = null;
		
		try {
			tmp = new DNSLookupEntry(value, getLookupType());
		} catch (TextParseException e) {
            Logger.log("Error parsing sensor["+getName()+"] JSON##"+value+"##: " + e.getLocalizedMessage());
//            throw e;
//			e.printStackTrace();
		}
		if(tmp != null ){
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

    		String tmp = getJsonElementString(json, DNSSensorDefinition.FIELD_TYPE);
    		if(tmp != null){
        		this.setLookupType(Type.value(tmp));
    		}

            // Set Vector name to be scanned by parrent
            setVectorPropertyName(DNSSensorDefinition.FIELD_DNS_VECTOR);
            
    		// Delegate to parent
    		super.loadFromJson(json);
                        
            
        } catch (Exception e) {
            Logger.log("Error parsing sensor["+getName()+"] JSON##"+json+"##: " + e.getLocalizedMessage());
            throw e;
        }
		
        
        init();
	}

}

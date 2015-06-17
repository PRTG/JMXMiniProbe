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

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.snmp4j.PDU;
import org.snmp4j.Target;
import org.snmp4j.event.ResponseEvent;
import org.snmp4j.mp.SnmpConstants;
import org.snmp4j.smi.Null;
import org.snmp4j.smi.OID;
import org.snmp4j.smi.Variable;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.paessler.prtg.jmx.Logger;
import com.paessler.prtg.jmx.channels.Channel;
import com.paessler.prtg.jmx.channels.Channel.Unit;
import com.paessler.prtg.jmx.channels.LongChannel;
import com.paessler.prtg.jmx.sensors.snmp.SNMPSensorSubInterfaceDefinition;
import com.paessler.prtg.jmx.definitions.SensorConstants;
import com.paessler.prtg.jmx.definitions.SensorDefinition;
import com.paessler.prtg.jmx.responses.DataResponse;
import com.paessler.prtg.util.snmp.SNMPUtil;
import com.paessler.prtg.util.snmp.OIDHolder;

public class SNMPSensorSubInterface extends RemoteSensor {
	protected String community;
	protected boolean useDelta;
	protected int	 snmpVersion;
	
	public void setSnmpVersion(int ver) {snmpVersion = ver;}
	public int getSnmpVersion() {return snmpVersion;}
	// --------------------------------------
	public SNMPSensorSubInterface(){
		super();
		this.remoteProtocol = "udp";
		remotePort = 161;
		setSnmpVersion(SnmpConstants.version2c);
		useDelta = false;
	}
	// --------------------------------------
	public SNMPSensorSubInterface(SNMPSensorSubInterface tocpy){
		super(tocpy);
		this.remoteProtocol = tocpy.remoteProtocol;
		remotePort = tocpy.remotePort;
		setSnmpVersion(tocpy.getSnmpVersion());
		useDelta = tocpy.useDelta;
	}
	//----------------------------------------------------------------------
    @Override
    public Sensor copy(){
		return new SNMPSensorSubInterface(this);
    }
	
	protected OIDHolder sysUpTime;
	// ------------------------
	public class snmpGetHolder{
		public	int 		ifIndex;
		public	OIDHolder	ifHolder;
		public	String		ifName;
		public 	String		ifDescription;

		public List<OIDHolder> channels;
		// ------------------------------------
		public snmpGetHolder(int idx, boolean inoutonly){
			ifIndex = idx;
			ifHolder = SNMPUtil.getIFOIDHolder(ifIndex);
			channels = SNMPUtil.getIFOIDHolderInst(new Vector<OIDHolder>(), ifIndex, inoutonly);
		}
	} // class snmpGetHolder
	protected Vector<snmpGetHolder> vectorIndecies;
	// ----------------------------------------------------
	public Vector<snmpGetHolder> getVectorIndecies() {return vectorIndecies;	}
	// ---------------------------
	public void setVectorIndecies(Vector<snmpGetHolder> vectorIndecies) {
		this.vectorIndecies = vectorIndecies;
	}
	// ---------------------------
	protected void getVectorIndecies(String commaseparatedlist, boolean inoutonly){
		vectorIndecies = new Vector<snmpGetHolder>(); 
		String[] intarr = commaseparatedlist.split(",");
		for(String curr: intarr){
			int currint = Integer.parseInt(curr);
			vectorIndecies.add(new snmpGetHolder(currint, inoutonly));
		}
	}
	
	// ----------------------------------------------------
	// ----------------------------------------------------
	// ----------------------------------------------------
	public String getCommunity() {	return community;	}
	// ---------------------------
	public void setCommunity(String community) {
		this.community = community;
	}
	// ----------------------------------------------------
	public boolean getUseDelta() {	return useDelta;	}
	// ---------------------------
	public void setUseDelta(boolean useDelta) {
		this.useDelta = useDelta;
	}
	
	// ----------------------------------------------------
	public Target getSnmpTarget() {	return snmpTarget;	}

	public void setSnmpTarget(Target snmpTarget) {
		this.snmpTarget = snmpTarget;
	}

	
	//----------------------------------------------------------------------
	private boolean initialized = false;
	public boolean isInitialized() {return initialized;};
	
	//----------------------------------------------------------------------
	private Target snmpTarget = null;
	public String getKind() {return SNMPSensorSubInterfaceDefinition.KIND;}

	//----------------------------------------------------------------------
	public String getStringVar(Variable var){
		String retVal = null;
		if(!(var instanceof Null)){
			retVal = var.toString();
		}
		return retVal;
	}
	//----------------------------------------------------------------------
	@Override
	protected void init(){
		SNMPUtil.startSNMPListen();
		// -----------------------------------------------------
		snmpTarget = SNMPUtil.getTarget(getRemoteProtocol(), getRemoteHost(), ""+getRemotePort(), community, getSnmpVersion());
		ResponseEvent resp = null;
		PDU pdu = SNMPUtil.getMetaInfoOIDs(-1);
		OID hostOID = new OID(SNMPUtil.SYSNAME_OID);
		// Add Meta
		OIDHolder tmp;
		// request Sub-if name/desc
    	for(snmpGetHolder curr :vectorIndecies){
    		tmp = curr.ifHolder;
    		pdu.add(tmp.getVariableBinding());
    		tmp = tmp.getDescriptionHolder();
    		if(tmp != null){
        		pdu.add(tmp.getVariableBinding());
    		}
    	}
		try {
			resp = SNMPUtil.get(getSnmpTarget(), pdu);
			if(resp != null){
				String tmpstr;
				PDU resppdu = resp.getResponse();
				if(resppdu != null){
					Map<OID, Variable> map = SNMPUtil.convertToMap(resppdu);
					Variable tmpvar = map.get(hostOID);
					if(tmpvar != null){
						setSensorName(tmpvar.toString());
						initialized = true;
					}
					// get Sub-if name/desc
			    	for(snmpGetHolder curr :vectorIndecies){
			    		tmp = curr.ifHolder;
			    		tmpvar = map.get(tmp.oid);
						tmpstr = getStringVar(tmpvar);
			    		if(tmpstr != null && !tmpstr.isEmpty()){
			    			curr.ifName = tmpstr;
			    			for(OIDHolder currc :curr.channels){
			    				currc.name = curr.ifName + " " +currc.description; 
			    			}
			    		}
			    		tmp = tmp.getDescriptionHolder();
			    		if(tmp != null){
				    		tmpvar = map.get(tmp.oid);
							tmpstr = getStringVar(tmpvar);
				    		if(tmpstr != null && !tmpstr.isEmpty()){
				    			curr.ifDescription = curr.ifHolder.name+ tmpstr;
				    		}
			    			
			    		}
			    	} // for
				}
			}
			sysUpTime = SNMPUtil.getOIDHolder(SNMPUtil.UPTIME_OID);	
		} catch (IOException e) {
				// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	// -----------------------------------------------------------
	protected PDU getPDU(){
		PDU retVal = new PDU();
		retVal.add(sysUpTime.getVariableBinding());
    	for(snmpGetHolder curr :vectorIndecies){
    		for(OIDHolder currh : curr.channels){
    			retVal.add(currh.getVariableBinding());
    		}
    	}
    	return retVal;
	}
	
	
	// -----------------------------------------------
	protected Channel getChannelInfo(OIDHolder oidh, Map<OID, Variable> vars){
		Channel retVal = null;
		Variable var = vars.get(oidh.oid);
		String name = oidh.getName();
		long value = -1;
		if(var instanceof Null){
			String tmp = var.toString();
			retVal = new LongChannel(name, Unit.BANDWIDTH, -1);
			String logString = "OID["+oidh.oidString+"] "+name+" returned: "+tmp;
//			System.out.println(logString);
            Logger.log(logString );
//			retVal.
		} else {
			value = var.toLong();
		}
//			public enum SNMPDataResponseType {STRING,INT,LONG,FLOAT,TICKS,OCTIN,OCTOUT,COUNT};
			switch(oidh.dataType){
			case INT:
			case LONG:
			case OCTIN:
			case OCTOUT:
				retVal = new LongChannel(name, Unit.BANDWIDTH, value, Channel.Mode.COUNTER);
				break;
			case TICKS:
				retVal = new LongChannel(name, Unit.TIME_SECONDS, (value < 0 ? value : (long)(value/100)));
				break;
			case COUNT:
				retVal = new LongChannel(name, Unit.COUNT, value);
				break;
//			case STRING:
//				retVal = new LongChannel(name, "BytesBandwidth", var.toLong());
//				break;
//		lchannel  = new LongChannel("Packet Loss", "Percent", stats.loss);
			
		}
		return retVal;
	}
	// --------------------------------------------------------------
	protected void addChannel(DataResponse response, OIDHolder oidh, Map<OID, Variable> vars){
		Channel channel;
		channel =getChannelInfo(oidh, vars);
		if(channel != null){
			response.addChannel(channel);
		}
		
	}
	// --------------------------------------------------------------
	protected void fillChannels(DataResponse response, Map<OID, Variable> vars){
		addChannel(response, sysUpTime, vars);
    	for(snmpGetHolder curr :vectorIndecies){
    		for(OIDHolder currh : curr.channels){
    			addChannel(response, currh, vars);
    		}
    	}
		
	}
	
	//{'sensorid': 13297, 'message': 'OK', 'channel': [
	//	{'unit': 'BytesBandwidth', 'name': 'Traffic Total', 'value': '3062456331', 'mode': 'counter'}, 
	//	{'unit': 'BytesBandwidth', 'name': 'Traffic In',    'value':  '578460173', 'mode': 'counter'}, 
	//	{'unit': 'BytesBandwidth', 'name': 'Traffic Out',   'value': '2483996158', 'mode': 'counter'}
	// ]}
	//----------------------------------------------------------------------
	@Override
	public DataResponse go() {
        DataResponse response = new DataResponse(sensorid, SNMPSensorSubInterfaceDefinition.KIND);
        if(!isInitialized()){
        	init();
        }
        if(isInitialized()){
    		PDU pdu = getPDU();
    		
    		ResponseEvent resp = SNMPUtil.getNoThrow(getSnmpTarget(), pdu);
    		if(resp != null){
				PDU resppdu = resp.getResponse();
				if(resppdu != null){
					fillChannels(response,  SNMPUtil.convertToMap(resppdu));
				}    			
	        } else {
				response = getErrorResponse("Data Error", -1, "SNMP Sensor returned no data");
	        }

        } else {
			response = getErrorResponse("Init Error", -1, "SNMP Sensor not initialized");
        }
		// TODO Auto-generated method stub
		return response;
	}

	//----------------------------------------------------------------------
	@Override
	public SensorDefinition getDefinition() {
		// TODO Auto-generated method stub
		return new SNMPSensorSubInterfaceDefinition();
	}

	//----------------------------------------------------------------------
	@Override
	public void loadFromJson(JsonObject json)  throws Exception{
		// Delegate to parent
		super.loadFromJson(json);
		// Local
    	JsonElement tmpJSON = null;
    	try{
        	String tmpval = getJsonElementString(json, SNMPSensorSubInterfaceDefinition.COMMUNITY);
            if (tmpval != null && !tmpval.isEmpty()) {
	        	this.setCommunity(tmpval);
	        }
	
	        tmpval = getJsonElementString(json, SNMPSensorSubInterfaceDefinition.VECTOR_INDECIES);
            if (tmpval != null && !tmpval.isEmpty()) {
	        	getVectorIndecies(tmpval, false);
	        } else {
	        	getVectorIndecies("1", false);
	        }
	        tmpval = getJsonElementString(json, SensorConstants.VERSION);
            if (tmpval != null && !tmpval.isEmpty()) {
	        	this.setSnmpVersion(SNMPUtil.getVersion(tmpval));
	        }
            
/*	        tmpval = getJsonElementString(json, SNMPSensorSubInterfaceDefinition.VALUES_DELTA);
            if (tmpval != null && !tmpval.isEmpty()) {
            	int tmpVal = NumberUtility.getInt(tmpval, 0);
	        	this.setUseDelta(tmpVal == 1);
	        }
*/            
        } catch (Exception e) {
            Logger.log("Error parsing sensor["+getName()+"] JSON##"+json+"##: " + e.getLocalizedMessage());
            throw e;
        }
		
        
        init();
	}

}

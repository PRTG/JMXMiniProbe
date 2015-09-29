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

import com.google.gson.JsonObject;
import com.paessler.prtg.jmx.Logger;
import com.paessler.prtg.jmx.channels.Channel;
import com.paessler.prtg.jmx.sensors.port.PortSensorDefinition;
import com.paessler.prtg.jmx.sensors.profile.Attribute;
import com.paessler.prtg.jmx.sensors.profile.Entry;
import com.paessler.prtg.jmx.sensors.profile.IntegerAttribute;
import com.paessler.prtg.jmx.sensors.profile.Profile;
import com.paessler.prtg.jmx.sensors.snmp.SNMPSensorDefinition;
import com.paessler.prtg.jmx.definitions.SensorConstants;
import com.paessler.prtg.jmx.responses.DataResponse;
import com.paessler.prtg.jmx.sensors.snmp.SNMPCustomDef;
import com.paessler.prtg.jmx.sensors.snmp.SNMPEntry;
import com.paessler.prtg.jmx.sensors.snmp.SNMPGetHolder;
import com.paessler.prtg.util.NumberUtility;
import com.paessler.prtg.util.snmp.SNMPUtil;
import com.paessler.prtg.util.snmp.OIDHolder;

public class SNMPSensor extends RemoteSensor<SNMPEntry> {
	protected String	community;
//	protected boolean	useDelta;
	protected int		snmpVersion;
	protected int		mpyFactor = 1;
	protected int		divFactor = 1;
	
	// --------------------------------------------------------
	@Override
	public String toString() {return super.toString()+"|"+community;}
	// --------------------------------------
	public SNMPSensor(){
		super();
		this.remoteProtocol = "udp";
		remotePort = 161;
		setSnmpVersion(SnmpConstants.version2c);
//		useDelta = false;
	}
	// ----------------------------
	public SNMPSensor(SNMPSensor toclone){
		super(toclone);
		community = toclone.community;
//		useDelta = toclone.useDelta;
		snmpVersion = toclone.snmpVersion;
		mpyFactor = toclone.mpyFactor;
		divFactor = toclone.divFactor;
	}
	// ----------------------------
	public Sensor copy(){
		return new SNMPSensor(this);
	}
	
	//----------------------------------------------------------------------
	public int getMpyFactor()				{return mpyFactor;}
	public void setMpyFactor(int mpyFactor) {this.mpyFactor = mpyFactor;}
	// ----------------------------
	public int getDivFactor() 				{return divFactor;}
	public void setDivFactor(int divFactor) {this.divFactor = divFactor;}

	public void setSnmpVersion(int ver) {snmpVersion = ver;}
	public int getSnmpVersion() {return snmpVersion;}
	
	//----------------------------------------------------------------------
	private Target snmpTarget = null;
	public Target makeSnmpTarget() {
		return SNMPUtil.getTarget(getRemoteProtocol(), getHost(), ""+getRemotePort(), getCommunity(), getSnmpVersion());
	}
	
	public Target getSnmpTarget() {	
		return snmpTarget;	
		}

	public void setSnmpTarget(Target snmpTarget) {
		this.snmpTarget = snmpTarget;
	}
	// ----------------------------------------------------
	
	protected OIDHolder sysUpTime;
	// ----------------------------------------------------
	public String getCommunity() {	return community;	}
	// ---------------------------
	public void setCommunity(String community) {
		this.community = community;
	}
/*	
	// ----------------------------------------------------
	public boolean getUseDelta() {	return useDelta;	}
	// ---------------------------
	public void setUseDelta(boolean useDelta) {
		this.useDelta = useDelta;
	}
*/	
	// ----------------------------------------------------
	
	protected List<SNMPGetHolder> vectorIndecies;
	// ----------------------------------------------------
	public List<SNMPGetHolder> getVectorIndecies() 
	{
		if(vectorIndecies == null){
			vectorIndecies = new Vector<SNMPGetHolder>();
		}
		return vectorIndecies;	
	}
	// ---------------------------
	public void setVectorIndecies(List<SNMPGetHolder> vectorIndecies) {
		this.vectorIndecies = vectorIndecies;
	}
	// ------------------------------------------------------------------------------	
	public void addVectorIndex(SNMPGetHolder entry) {
		getVectorIndecies().add(entry);
	}
	// ------------------------------------------------------------------------------	
	// ------------------------------------------------------------------------------	
	//----------------------------------------------------------------------
	public String getStringVar(Variable var){
		String retVal = null;
		if(!(var instanceof Null)){
			retVal = var.toString();
		}
		return retVal;
	}
	
//	Vector<SNMPEntry> getSNMPVector()
	
	//----------------------------------------------------------------------
	protected ResponseEvent fetchSNMPData(PDU pdu){
		ResponseEvent retVal = null;
		try {
			retVal = SNMPUtil.get(getSnmpTarget(), pdu);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return retVal;
	}
	//----------------------------------------------------------------------
/*	
	protected void fetchSNMPData(PDU pdu, Vector<snmpGetHolder> vect){
		ResponseEvent retVal = null;
		addVariableBindings(pdu, vectorIndecies);
		try {
			retVal = SNMPUtil.get(getSnmpTarget(), pdu);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return retVal;
	}
*/
	protected boolean buildIfDescriptions(){
		boolean retVal = false;
		PDU pdu = SNMPUtil.getMetaInfoOIDs(-1);
		OID hostOID = new OID(SNMPUtil.SYSNAME_OID);
		OIDHolder tmp;
		ResponseEvent resp = null;
		// request Sub-if name/desc
		if(getVectorIndecies().size() > 0){
			SNMPGetHolder.addVariableBindings(pdu, getVectorIndecies());
			resp = fetchSNMPData(pdu);
			if(resp != null){
				String tmpstr;
				PDU resppdu = resp.getResponse();
				if(resppdu != null){
					Map<OID, Variable> map = SNMPUtil.convertToMap(resppdu);
					Variable tmpvar = map.get(hostOID);
					if(tmpvar != null){
						setSensorName(tmpvar.toString());
						setInitialized(true);
					}
					// get Sub-if name/desc
			    	for(SNMPGetHolder curr :getVectorIndecies() ){
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
		    	for(SNMPGetHolder curr :getVectorIndecies()){
	    			for(OIDHolder currc :curr.channels){
	    				SNMPEntry snmpe = new SNMPEntry(currc.dataType, currc.name, currc.oid, currc.oidString, currc.description);
	    				if(snmpe != null){
	    					if(divFactor != 1)
	    						snmpe.setDiv(divFactor);
	    					if(mpyFactor != 1)
	    						snmpe.setMpy(mpyFactor);
	    					addVectorEntry(snmpe);
	    				}
	    			}
		    		
		    	}
			}
		} else {// if(getVectorIndecies().size() > 0)
			setInitialized(true);
		}
		return retVal;
		
	}
	//----------------------------------------------------------------------
	@Override
	protected void init(){
		SNMPUtil.startSNMPListen();
		setSnmpTarget(makeSnmpTarget());
		buildIfDescriptions();
	}
	// --------------------------------------------------------------
	protected void addChannel(DataResponse response, SNMPEntry snmpe, Map<OID, Variable> vars){
		Channel channel;
		channel = snmpe.getChannel(vars.get(snmpe.getOid()));
		if(channel != null){
			response.addChannel(channel);
		}
		
	}
	// --------------------------------------------------------------
	protected void fillChannels(DataResponse response, Map<OID, Variable> vars){
    	for(SNMPEntry curr : getVectorOfValues()){
    		addChannel(response, curr, vars);
    	}
	}
	
	//{'sensorid': 13297, 'message': 'OK', 'channel': [
	//	{'unit': 'BytesBandwidth', 'name': 'Traffic Total', 'value': '3062456331', 'mode': 'counter'}, 
	//	{'unit': 'BytesBandwidth', 'name': 'Traffic In',    'value':  '578460173', 'mode': 'counter'}, 
	//	{'unit': 'BytesBandwidth', 'name': 'Traffic Out',   'value': '2483996158', 'mode': 'counter'}
	// ]}
	// -----------------------------------------------------------
	protected PDU getPDU(){
    	return SNMPEntry.getPDU(getVectorOfValues());
	}
	//----------------------------------------------------------------------
	@Override
	public DataResponse go() {
        DataResponse response = new DataResponse(sensorid, SNMPSensorDefinition.KIND);
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
	public void loadVectorFromJson(String jsonstring) {
	    if (jsonstring != null && !jsonstring.isEmpty()) {
			String[] intarr = jsonstring.split(",");
			for(String curr: intarr){
				addVectorEntry(curr, false);
			}
	    }
	}
	//----------------------------------------------------------------------
	@Override
	public void loadFromJson(JsonObject json)  throws Exception{
		// Local
    	try{
        	String tmpval = getJsonElementString(json, SNMPSensorDefinition.FIELD_COMMUNITY);
            if (tmpval != null && !tmpval.isEmpty()) {
	        	this.setCommunity(tmpval);
	        }
            
	        tmpval = getJsonElementString(json, SNMPSensorDefinition.FIELD_VERSION);
            if (tmpval != null && !tmpval.isEmpty()) {
	        	this.setSnmpVersion(SNMPUtil.getVersion(tmpval));
	        }
    		tmpval = getJsonElementString(json, SNMPSensorDefinition.FIELD_MULTIPLICATION);
            if (tmpval != null && !tmpval.isEmpty()) {
            	int tmpVal = NumberUtility.getInt(tmpval, 0);
	        	this.setMpyFactor(tmpVal);
	        }
    		tmpval = getJsonElementString(json, SNMPSensorDefinition.FIELD_DIVISION);
            if (tmpval != null && !tmpval.isEmpty()) {
            	int tmpVal = NumberUtility.getInt(tmpval, 0);
	        	this.setDivFactor(tmpVal);
	        }
            
/*	        tmpval = getJsonElementString(json, SNMPSensorDefinition.VALUES_DELTA);
            if (tmpval != null && !tmpval.isEmpty()) {
            	int tmpVal = NumberUtility.getInt(tmpval, 0);
	        	this.setUseDelta(tmpVal == 1);
	        }
*/            
            // Set Vector name to be scanned by parrent
            setVectorPropertyName(SNMPSensorDefinition.FIELD_SNMP_VECTOR);
    		// Delegate to parent
    		super.loadFromJson(json);
        } catch (Exception e) {
            Logger.log("Error parsing sensor["+getName()+"] JSON##"+json+"##: " + e.getLocalizedMessage());
            throw e;
        }
		
	}
	// ----------------------------------------------------------------------
    @Override
    public void loadFrom(Profile profile) {
    	String tmptag = profile.getTag();
    	SNMPSensorDefinition def =  new SNMPSensorDefinition(profile.getKind(), profile.getName(), profile.getDescription(), 
    			tmptag, profile.getHelp());
		setDefinition(def);
    	super.loadFrom(profile);

    	SNMPEntry attr;
    	for(Entry curr : profile.getEntries()){
        	for(Attribute<?> curra : curr.getAttributes()){
            	attr = new SNMPEntry(curra);
        		if(attr != null){
        			addVectorEntry(attr);
        		}
        		
        	}
    	}
    } 
}

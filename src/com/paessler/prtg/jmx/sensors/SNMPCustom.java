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

//import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.paessler.prtg.jmx.Logger;
import com.paessler.prtg.jmx.channels.Channel;
import com.paessler.prtg.jmx.sensors.port.PortSensorDefinition;
import com.paessler.prtg.jmx.sensors.profile.Attribute;
import com.paessler.prtg.jmx.sensors.profile.Entry;
import com.paessler.prtg.jmx.sensors.profile.IntegerAttribute;
import com.paessler.prtg.jmx.sensors.profile.Profile;
import com.paessler.prtg.jmx.sensors.snmp.SNMPCustomDef;
import com.paessler.prtg.jmx.sensors.snmp.SNMPEntry;
import com.paessler.prtg.util.snmp.OIDHolder.SNMPDataType;

public class SNMPCustom extends SNMPSensor {
	protected Channel.Unit	unit = Channel.Unit.COUNT;
	protected String		customUnit = null;
	protected Channel.Mode	valueType;

    // --------------------------------------------------------------------------------------------
	public SNMPCustom(){
		super();
		setDefinition(new SNMPCustomDef());
		setKind(SNMPCustomDef.KIND);
		setSensorName("SNMPCustom");
	}
	public SNMPCustom(SNMPCustom toclone){
		super(toclone);
		customUnit = toclone.customUnit;
		valueType = toclone.valueType;
	}
	//----------------------------------------------------------------------
    @Override
    public Sensor copy(){
		return new SNMPCustom(this);
	}

	// -------------------------------
	public Channel.Unit getUnit() 			{return unit;	}
	public void setUnit(Channel.Unit unit)	{this.unit = unit;}
	public void setUnit(String unit)		{setUnit(Channel.toUnit(unit));	}

	// -------------------------------
	public String getCustomUnit() 				{return customUnit;	}
	public void setCustomUnit(String customUnit){this.customUnit = customUnit;}

	// -------------------------------
	public Channel.Mode getValueType() 				{return valueType;}
	public void setValueType(Channel.Mode valueType){this.valueType = valueType;}
	
	// -------------------------------
	public String getSensorName() 				{return sensorName;}
	public void setSensorName(String customUnit){this.sensorName = customUnit;}

	// --------------------------------------
	
	//----------------------------------------------------------------------
	//----------------------------------------------------------------------
	@Override
	protected void init(){
		super.init();
	}

	// ---------------------------
	protected void addVectorEntry(String value, boolean inoutonly){
		// Expecting OID
		String description = "OID:"+value;
		if(getSensorName() == null){
			setSensorName(description);
		}
		SNMPEntry snmpe = new SNMPEntry(SNMPDataType.OTHER, getSensorName(), value, description);
		if(snmpe != null){
			snmpe.setDiv(divFactor);
			snmpe.setMpy(mpyFactor);
			snmpe.setModeEnum(getValueType());
			snmpe.setUnitEnum(getUnit());
			snmpe.setCustomUnit(getCustomUnit());
			// Kick the type adjustment
			snmpe.setDataType(SNMPDataType.OTHER);
			addVectorEntry(snmpe);
		}
	}
	
	//----------------------------------------------------------------------
	@Override
	public void loadFromJson(JsonObject json)  throws Exception{
		// Local
//		TRAFFIC_SENSOR_VALS
//    	JsonElement tmpJSON = null;
    	try{
    		
	        String tmpstr = getJsonElementString(json, SNMPCustomDef.FIELD_UNIT);
            if (tmpstr != null && !tmpstr.isEmpty()) {
	        	this.setUnit(tmpstr);
	        }
	        tmpstr = getJsonElementString(json, SNMPCustomDef.FIELD_CUSTOMUNIT);
            if (tmpstr != null && !tmpstr.isEmpty()) {
	        	this.setCustomUnit(tmpstr);
	        }
            
    		int tmpint = getJsonElementInt(json, SNMPCustomDef.FIELD_VALUE_TYPE, -1);
            if (tmpint > -1) {
            	Channel.Mode lmode = Channel.Mode.COUNTER; 
            	if(tmpint == 1){
            		lmode = Channel.Mode.INTEGER;
            	}
	        	this.setValueType(lmode);
	        }
    		tmpstr = getJsonElementString(json, SNMPCustomDef.FIELD_DESCR);
            if (tmpstr != null && !tmpstr.isEmpty()) {
	        	this.setSensorName(tmpstr);
	        }
    		// Delegate to parent
    		super.loadFromJson(json);
                        
            
        } catch (Exception e) {
            Logger.log("Error parsing sensor["+getName()+"] JSON##"+json+"##: " + e.getLocalizedMessage());
            throw e;
        }
		
        
        init();
	}

}

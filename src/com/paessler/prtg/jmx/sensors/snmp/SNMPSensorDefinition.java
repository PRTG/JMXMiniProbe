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
package com.paessler.prtg.jmx.sensors.snmp;

import java.util.List;

import com.paessler.prtg.jmx.definitions.FieldDefinition;
import com.paessler.prtg.jmx.definitions.GroupDefinition;
import com.paessler.prtg.jmx.definitions.IntegerField;
import com.paessler.prtg.jmx.definitions.RadioFieldDefinition;
import com.paessler.prtg.jmx.definitions.RemoteSensorDefinition;
import com.paessler.prtg.jmx.definitions.SensorConstants;
import com.paessler.prtg.jmx.definitions.SimpleEditFieldDefinition;

/**
 * @Author JR Andreassen
 */

public abstract class SNMPSensorDefinition extends RemoteSensorDefinition {
	public static String KIND = "snmpvector";

	public static final String FIELD_VERSION		= "version";
	public static final String FIELD_SNMP_VECTOR	= "snmpvector";
	public static final String FIELD_VALUES_DELTA	= "usedelta"; 
	public static final String FIELD_COMMUNITY 		= "community";
	public static final String FIELD_OIDS 			= "oids";
	public static final String FIELD_MULTIPLICATION = "multiplication";
	public static final String FIELD_DIVISION 		= "division";

	
	protected FieldDefinition getSNMPVersion(String defaultvalue ){
        RadioFieldDefinition retVal = new RadioFieldDefinition(SNMPSensorDefinition.FIELD_VERSION, "SNMP Version", "Choose your SNMP Version", defaultvalue);
        retVal.setRequired(FieldDefinition.FIELDVALUE_REQUIRED_TRUE);
        retVal.addOption("1", "V1");
        retVal.addOption("2", "V2c");
        retVal.addOption("3", "V3");
		return retVal;
	}
	protected FieldDefinition getSNMPCommunityField(){
        FieldDefinition retVal = new SimpleEditFieldDefinition(SNMPSensorDefinition.FIELD_COMMUNITY, "SNMP Community String", "Default is \"public\"");
        retVal.setDefaultValue("public");
        retVal.setRequired(FieldDefinition.FIELDVALUE_REQUIRED_TRUE);
        return retVal;
	}
    // -----------------------------------------------------------------------
	@Override
	public List<GroupDefinition> getGroups(List<GroupDefinition> groups){
		List<GroupDefinition> retVal = super.getGroups(groups);
		
        GroupDefinition group = new GroupDefinition(SensorConstants.CONNECTION, "SNMP Connection Settings");
        // -------------------------
		group.fields.add(getPortField(161, "Please provide Port of the SNMP Service (Default: 161)"));
		group.fields.add(getSNMPVersion("2"));
        group.fields.add(getSNMPCommunityField());
        // -------------------------
        group.fields.add(getUserNameField("SNMP v3 Username", false));
        group.fields.add(getPasswordField("SNMP v3 Password for given User", false));
        // -------------------------
        // -------------------------
        // -------------------------
        IntegerField tmpIfield = new IntegerField(FIELD_MULTIPLICATION, "Multiplication");
        tmpIfield.setDefaultValue(new Integer(1));
        tmpIfield.setMaximum(9999999);
        tmpIfield.setMinimum(0);
        tmpIfield.setRequired(FieldDefinition.FIELDVALUE_REQUIRED_TRUE);
        tmpIfield.setHelp("Provide a value the raw SNMP value is to be multiplied by.");
        group.fields.add(tmpIfield);
        // -------------------------
        tmpIfield = new IntegerField(FIELD_DIVISION, "Division");
        tmpIfield.setDefaultValue(new Integer(1));
        tmpIfield.setMaximum(9999999);
        tmpIfield.setMinimum(0);
        tmpIfield.setRequired(FieldDefinition.FIELDVALUE_REQUIRED_TRUE);
        tmpIfield.setHelp("Provide a value the raw SNMP value is divided by.");
        group.fields.add(tmpIfield);
        // -------------------------
        retVal.add(group);
		return retVal;
	}
	
    public SNMPSensorDefinition(String kind, String name, String description, String tag, String help) {
        super(kind, name, description, tag, help);
    }

}

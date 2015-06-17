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

import java.util.ArrayList;

import com.paessler.prtg.jmx.definitions.FieldDefinition;
import com.paessler.prtg.jmx.definitions.GroupDefinition;
import com.paessler.prtg.jmx.definitions.IPPortField;
import com.paessler.prtg.jmx.definitions.IntegerField;
import com.paessler.prtg.jmx.definitions.RadioFieldDefinition;
import com.paessler.prtg.jmx.definitions.SensorConstants;
import com.paessler.prtg.jmx.definitions.SensorDefinition;
import com.paessler.prtg.jmx.definitions.SimpleEditFieldDefinition;

/**
 * @Author JR Andreassen
 */

public class SNMPSensorSubInterfaceDefinition extends SensorDefinition {
	public static String KIND = "snmpvector";

	public static final String SNMP_SETTINGS	= "SNMPSettings";
	public static final String VECTOR_INDECIES	= "vectorindecies";
	public static final String VALUES_DELTA		= "usedelta"; 
	public static final String COMMUNITY 		= "community";
	public static final String OIDS 			= "oids";

	
    public SNMPSensorSubInterfaceDefinition() {
    	super(KIND, "SNMP Interface Vector ", "Monitors SNMP interface vector elements", KIND, 
        		"");

        groups = new ArrayList<GroupDefinition>();
        GroupDefinition connectionSettings = new GroupDefinition(SensorConstants.CONNECTION, "Connection Settings");
        connectionSettings.fields.add( new SimpleEditFieldDefinition(SensorConstants.REMOTE_HOST, "Host name"));
        // -------------------------
        IntegerField tmpIfield = new IPPortField(SensorConstants.REMOTE_PORT, "SNMP Port", "Please provide Port");
        tmpIfield.setRequired(FieldDefinition.FIELDVALUE_REQUIRED_TRUE);
        tmpIfield.setDefaultValue(161);
        connectionSettings.fields.add( tmpIfield);
        // -------------------------
//      connectionSettings.fields.add( new SimpleEditFieldDefinition(SensorConstants.VERSION, "SNMP version[1,2,3]"));
        RadioFieldDefinition snmpVersion = new RadioFieldDefinition(SNMPSensorSubInterfaceDefinition.SNMP_SETTINGS, "SNMP Version", "Choose your SNMP Version", "2");
        snmpVersion.setRequired(FieldDefinition.FIELDVALUE_REQUIRED_TRUE);
        snmpVersion.addOption("1", "V1");
        snmpVersion.addOption("2", "V2c");
        snmpVersion.addOption("3", "V3");
        connectionSettings.fields.add(snmpVersion);
        // -------------------------
        FieldDefinition tmpfield = new SimpleEditFieldDefinition(SNMPSensorSubInterfaceDefinition.COMMUNITY, "SNMP Community String");
        tmpfield.setRequired(FieldDefinition.FIELDVALUE_REQUIRED_TRUE);
        connectionSettings.fields.add(tmpfield);
        // -------------------------
        connectionSettings.fields.add( new SimpleEditFieldDefinition(SensorConstants.USERNAME, "Username/Login"));
        connectionSettings.fields.add( new SimpleEditFieldDefinition(SensorConstants.PASSWORD, "Password"));
        // -------------------------
        tmpIfield = new IntegerField(SensorConstants.TIMEOUT, "SNMP timeout");
        tmpIfield.setMinimum(1);
        tmpIfield.setMaximum(900);
        tmpIfield.setDefaultValue(900);
        connectionSettings.fields.add(tmpIfield);
        groups.add(connectionSettings);
        // -------------------------

/*       RadioFieldDefinition delta = new RadioFieldDefinition(SNMPSensorSubInterfaceDefinition.VALUES_DELTA, "Use Value Delta or Raw", "Choose number type");
        delta.setRequired(FieldDefinition.FIELDVALUE_REQUIRED_TRUE);
        delta.addOption("0", "Raw");
        delta.addOption("1", "Delta");
        delta.setDefaultValue("0");
        connectionSettings.fields.add(delta);
*/        
        // -------------------------
        tmpfield = new SimpleEditFieldDefinition(SNMPSensorSubInterfaceDefinition.VECTOR_INDECIES, "SNMP Vector indecies, delimited with ,");
        tmpfield.setRequired(FieldDefinition.FIELDVALUE_REQUIRED_TRUE);
        connectionSettings.fields.add(tmpfield);
        // -------------------------
    }

    /*
    public SNMPSensorSubInterfaceDefinition() {
        kind = KIND;
        name = "SNMP Vector ";
        description = "Monitors SNMP vector elements";
//        help = "";
        tag = "snmpvector";

        groups = new ArrayList<GroupDefinition>();
        GroupDefinition connectionSettings = new GroupDefinition(SensorConstants.CONNECTION, "Connection Settings");
        connectionSettings.fields.add( new SimpleEditFieldDefinition(SensorConstants.REMOTE_HOST, "Host name"));
        connectionSettings.fields.add( new SimpleEditFieldDefinition(SensorConstants.USERNAME, "Username/Login"));
        connectionSettings.fields.add( new SimpleEditFieldDefinition(SensorConstants.PASSWORD, "Password"));
        // -------------------------
//      connectionSettings.fields.add( new SimpleEditFieldDefinition(SensorConstants.VERSION, "SNMP version[1,2,3]"));
        RadioFieldDefinition snmpVersion = new RadioFieldDefinition(SNMPSensorSubInterfaceDefinition.SNMP_SETTINGS, "SNMP Version", "Choose your SNMP Version");
        snmpVersion.setRequired(FieldDefinition.FIELDVALUE_REQUIRED_TRUE);
        snmpVersion.addOption("1", "V1");
        snmpVersion.addOption("2", "V2c");
        snmpVersion.addOption("3", "V3");
        snmpVersion.setDefaultValue("2");
        connectionSettings.fields.add(snmpVersion);
        // -------------------------
        FieldDefinition tmpfield = new SimpleEditFieldDefinition(SensorConstants.REMOTE_PORT, "SNMP Port", "Please provide Port");
        tmpfield.setRequired(FieldDefinition.FIELDVALUE_REQUIRED_TRUE);
        tmpfield.setDefaultValue("161");
        connectionSettings.fields.add( tmpfield);
        // -------------------------
        tmpfield = new SimpleEditFieldDefinition(SNMPSensorSubInterfaceDefinition.COMMUNITY, "SNMP Community String");
        tmpfield.setRequired(FieldDefinition.FIELDVALUE_REQUIRED_TRUE);
        connectionSettings.fields.add(tmpfield);
        // -------------------------
        connectionSettings.fields.add(new SimpleEditFieldDefinition(SNMPSensorSubInterfaceDefinition.VECTOR_INDECIES, "SNMP Vector indecies, delimited with ,"));
        connectionSettings.fields.add(new SimpleEditFieldDefinition(SensorConstants.TIMEOUT, "SNMP timeout"));
        groups.add(connectionSettings);
    }
     */
}

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
package com.paessler.prtg.jmx.definitions;

import java.util.ArrayList;

/**
 * @Author JR Andreassen
 */

public class SNMPSensorDefinition extends SensorDefinition {
	public static String KIND = "snmpvector";

	public static final String VECTOR_INDECIES	= "vectorindecies";
	public static final String COMMUNITY 		= "community";
	public static final String OIDS 			= "oids";
	
	
    public SNMPSensorDefinition() {
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
        RadioFieldDefinition snmpVersion = new RadioFieldDefinition("SNMP Settings", "SNMP Version", "Choose your SNMP Version");
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
        tmpfield = new SimpleEditFieldDefinition(SNMPSensorDefinition.COMMUNITY, "SNMP Community String");
        tmpfield.setRequired(FieldDefinition.FIELDVALUE_REQUIRED_TRUE);
        connectionSettings.fields.add(tmpfield);
        // -------------------------
        connectionSettings.fields.add(new SimpleEditFieldDefinition(SNMPSensorDefinition.VECTOR_INDECIES, "SNMP Vector indecies, delimited with ,"));
        connectionSettings.fields.add(new SimpleEditFieldDefinition(SensorConstants.TIMEOUT, "SNMP timeout"));
        groups.add(connectionSettings);
    }

}

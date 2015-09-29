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
import com.paessler.prtg.jmx.definitions.RadioFieldDefinition;
import com.paessler.prtg.jmx.definitions.SensorConstants;
import com.paessler.prtg.jmx.definitions.SimpleEditFieldDefinition;

/**
 * @Author JR Andreassen
 */

public class SNMPTrafficDef extends SNMPSensorDefinition {
	public static String KIND = "snmptraffic";
	public static String TAG = "mjsnmptrafficsensor";
	public static String TRAFFIC_SENSOR_VALS = "TrafficSensorParameters";
	public static final String SUM_CHANNELS		= "sumchannels";
	public static final String TRAFFIC_COUNTER		= "snmp_counter";

    // -----------------------------------------------------------------------
	@Override
	public List<GroupDefinition> getGroups(List<GroupDefinition> groups){
		List<GroupDefinition> retVal = super.getGroups(groups);

        GroupDefinition group = new GroupDefinition(TRAFFIC_SENSOR_VALS, "Interface and Channel Settings");
        // -------------------------
        RadioFieldDefinition snmpCounter = new RadioFieldDefinition(SNMPTrafficDef.TRAFFIC_COUNTER, "SNMP Counter Type", "Choose the Counter Type to be used", "2");
        snmpCounter.setRequired(FieldDefinition.FIELDVALUE_REQUIRED_TRUE);
        snmpCounter.addOption("1", "32 bit");
        snmpCounter.addOption("2", "64 bit");
//        snmpVersion.setDefaultValue("2");
        group.fields.add(snmpCounter);
        // -------------------------
        SimpleEditFieldDefinition tmpfield = new SimpleEditFieldDefinition(SNMPSensorDefinition.FIELD_SNMP_VECTOR, "SNMP Interface index");
        tmpfield.setRequired(FieldDefinition.FIELDVALUE_REQUIRED_TRUE);
        group.fields.add(tmpfield);
        // -------------------------
        RadioFieldDefinition delta = new RadioFieldDefinition(SNMPTrafficDef.SUM_CHANNELS, "Make total sum channel", "Choose Yes/No", "0");
        delta.setRequired(FieldDefinition.FIELDVALUE_REQUIRED_TRUE);
        delta.addOption("0", "No");
        delta.addOption("1", "Yes");
        group.fields.add(delta);
        // -------------------------
        retVal.add(group);
		return retVal;
	}	

    public SNMPTrafficDef() {
    	super(KIND, "SNMP Traffic Sensor", "Monitors interface  elements", TAG, 
    		"Produces a channel for InOctets, OutOctets and Optionally Total");
    }

}

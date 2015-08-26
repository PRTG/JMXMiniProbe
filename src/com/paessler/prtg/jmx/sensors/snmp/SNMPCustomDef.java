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
import com.paessler.prtg.jmx.definitions.SimpleEditFieldDefinition;

/**
 * @Author JR Andreassen
 */

public class SNMPCustomDef extends SNMPSensorDefinition {
	public static final String KIND = "mjsnmpcustom";
	public static final String TAG = "mjsnmpcustomsensor";
	public static final String CUSTOM_SENSOR_VALS = "CustomSensorParameters";
	public static final String FIELD_DESCR = "channeldescr";
	public static final String FIELD_UNIT = "unit";
	public static final String FIELD_VALUE_TYPE = "value_type";

    // -----------------------------------------------------------------------
	@Override
	public List<GroupDefinition> getGroups(List<GroupDefinition> groups){
		List<GroupDefinition> retVal = super.getGroups(groups);
        GroupDefinition group = new GroupDefinition(CUSTOM_SENSOR_VALS, "Interface and Channel Settings");
        // -------------------------
        FieldDefinition tmpfield = new SimpleEditFieldDefinition(SNMPSensorDefinition.FIELD_SNMP_VECTOR, "Custom OID value");
        tmpfield.setRequired(FieldDefinition.FIELDVALUE_REQUIRED_TRUE);
        tmpfield.setHelp("Enter a specific OID");
        group.fields.add(tmpfield);
        // -------------------------
        tmpfield = new SimpleEditFieldDefinition(SNMPCustomDef.FIELD_DESCR, "OID Description String", "Custom OID Description");
        tmpfield.setHelp("Display string for the Channel");
//        tmpfield.setRequired(FieldDefinition.FIELDVALUE_REQUIRED_TRUE);
        group.fields.add(tmpfield);
        // -------------------------
        tmpfield = new SimpleEditFieldDefinition(FIELD_UNIT, "Unit String");
        tmpfield.setRequired(FieldDefinition.FIELDVALUE_REQUIRED_TRUE);
        tmpfield.setHelp("Enter a 'unit' string, e.g. 'ms', 'Kbyte' (for display purposes only).");
        tmpfield.setDefaultValue("#");
        group.fields.add(tmpfield);
        // -------------------------
        RadioFieldDefinition radio = new RadioFieldDefinition(FIELD_VALUE_TYPE, "Value Type", "Choose Guage/Delta", "1");
        radio.setRequired(FieldDefinition.FIELDVALUE_REQUIRED_TRUE);
        radio.setHelp("Select 'Gauge' if you want to see absolute values (e.g. for temperature value) "
                                    +"or 'Delta' for counter differences divided by time period "
                                    +"(e.g. for bandwidth values)");
        radio.addOption("1", "Guage");
        radio.addOption("2", "Delta");
        group.fields.add(radio);
        // -------------------------
        retVal.add(group);
		return retVal;
	}	
    public SNMPCustomDef() {
    	super(KIND, "SNMP Custom Sensor", "Monitors a numerical value returned by a specific OID using SNMP", TAG, 
    		"Monitors a numerical value returned by a specific OID using SNMP");

    }

}

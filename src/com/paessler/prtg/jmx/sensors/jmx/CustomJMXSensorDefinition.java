/*
 * Copyright (c) 2014, Paessler AG <support@paessler.com>
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

package com.paessler.prtg.jmx.sensors.jmx;

import java.util.ArrayList;

import com.paessler.prtg.jmx.definitions.FieldDefinition;
import com.paessler.prtg.jmx.definitions.GroupDefinition;
import com.paessler.prtg.jmx.definitions.PasswordFieldDefinition;
import com.paessler.prtg.jmx.definitions.SensorConstants;
import com.paessler.prtg.jmx.definitions.SensorDefinition;
import com.paessler.prtg.jmx.definitions.SimpleEditFieldDefinition;
import com.paessler.prtg.jmx.definitions.UnitFieldDefinition;
import com.paessler.prtg.util.SystemUtility;

public class CustomJMXSensorDefinition extends SensorDefinition {
	
    public static String KIND = "jmxcustomattributes";
    public static String TAG = "mjjmxcustomattributessensor";
    public static String DEF_RMI = "service:jmx:rmi:///jndi/rmi://{TARGET_MACHINE}:{RMI_REGISTRY_PORT def:1099}/jmxrmi";
    // ----------------------------------------------------------
    public CustomJMXSensorDefinition() {
        super(KIND, "JMX Custom MBean Attributes Sensor", "Monitors custom attribute values", TAG,
        	"Monitor custom MBean and attributes");
    	FieldDefinition tmpfld;
        groups = new ArrayList<GroupDefinition>();
        GroupDefinition rmiGroupDefinition = new GroupDefinition("Connection", "Connection Settings");
        tmpfld = new SimpleEditFieldDefinition(SensorConstants.RMISTRING, "RMI Connection String", DEF_RMI);
        tmpfld.setDefaultValue("service:jmx:rmi:///jndi/rmi://localhost:"+System.getProperty(SystemUtility.SYS_PROPERTY_JMX_REMOTE_PORT)+"/jmxrmi");
        addField(tmpfld);
        rmiGroupDefinition.fields.add( tmpfld);
        // -----------------------------
        addField(tmpfld);
        rmiGroupDefinition.fields.add(new SimpleEditFieldDefinition(SensorConstants.USERNAME, "Username"));
        addField(tmpfld);
        rmiGroupDefinition.fields.add(new PasswordFieldDefinition(SensorConstants.PASSWORD, "Password", ""));
//        tmpfld = new SimpleEditFieldDefinition(SensorConstants.MBEAN_STRING, "MBean", "Name of MBean Locally available:["+tmpstr+"]");
        tmpfld = new SimpleEditFieldDefinition(SensorConstants.MBEAN_STRING, "MBean", "Name of MBean ");
        tmpfld.setDefaultValue("");
        addField(tmpfld);
        rmiGroupDefinition.fields.add(tmpfld);
        groups.add(rmiGroupDefinition);
        // -----------------------------
        for (int i = 1; i < 11; i++) {
            GroupDefinition attributeDefinition = new GroupDefinition("Attribute #" + i, "Attribute");
            SimpleEditFieldDefinition objectName = new SimpleEditFieldDefinition("object_name_" + i, "Object name");
// Skipped this one, since we don't want them to change the actual attribute name             
//            addField(objectName);
            attributeDefinition.fields.add(objectName);
            UnitFieldDefinition units = new UnitFieldDefinition("unit_name_" + i, "Unit types", "Choose the type of value the attribute returns", null);
            attributeDefinition.fields.add(units);
            addField(units);
            groups.add(attributeDefinition);
        }
    }
    
// 
}

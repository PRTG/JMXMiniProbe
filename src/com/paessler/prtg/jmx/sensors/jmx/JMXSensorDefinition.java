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

public class JMXSensorDefinition extends SensorDefinition {

// ----------------------------------------------------------------------------------------------------
	
    public static String KIND = "mjjmxsensor";
    public static String TAG = "mjjmxsensor";
    
    // ----------------------------------------------------------
    public JMXSensorDefinition(String kind, String name, String description, String tag, String help) {
        super(kind, name, description, tag, help);
    	FieldDefinition tmpfld;
        groups = new ArrayList<GroupDefinition>();
        GroupDefinition rmiGroupDefinition = new GroupDefinition("Connection", "Connection Settings");
        tmpfld = new SimpleEditFieldDefinition(SensorConstants.RMISTRING, "RMI Connection String", JMXUtils.RMI_STRING_DEFAULT);
        tmpfld.setDefaultValue(JMXUtils.makeRMIString(null,null));
        addField(tmpfld);
        rmiGroupDefinition.fields.add( tmpfld);
        // -----------------------------
        tmpfld = new SimpleEditFieldDefinition(SensorConstants.USERNAME, "Username");
        addField(tmpfld);
        rmiGroupDefinition.fields.add(tmpfld);
        tmpfld = new PasswordFieldDefinition(SensorConstants.PASSWORD, "Password", "");
        addField(tmpfld);
        rmiGroupDefinition.fields.add(tmpfld);
//        tmpfld = new SimpleEditFieldDefinition("mbean", "MBean", "Name of MBean Locally available:["+tmpstr+"]");
        groups.add(rmiGroupDefinition);
    }
    
// 
}

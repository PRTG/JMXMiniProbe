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

package com.paessler.prtg.jmx.definitions;

import java.util.ArrayList;

import com.paessler.prtg.jmx.sensors.jmx.JMXUtils;

public class VMHealthDefinition extends SensorDefinition {
    public static String KIND = "jmxvmhealth";
    public static String TAG = "mjjmxvmhealthsensor";

    public VMHealthDefinition() {
        super(KIND, "JMX Monitors VM Health", "JMX VM Health", TAG,
        		"JVM Health monitoring via JMX");
        groups = new ArrayList<GroupDefinition>();
        GroupDefinition rmiGroupDefinition = new GroupDefinition("Connection", "Connection Settings");
        
        FieldDefinition tmpfld = new SimpleEditFieldDefinition("rmi_string", "RMI Connection String");
        tmpfld.setDefaultValue(JMXUtils.RMI_STRING_LOCAL);
        addField(tmpfld);
        rmiGroupDefinition.fields.add(tmpfld);
     
        tmpfld = new SimpleEditFieldDefinition("rmi_username", "Username");
        rmiGroupDefinition.fields.add(tmpfld);
        addField(tmpfld);
        tmpfld = new PasswordFieldDefinition("rmi_password", "Password", "");
        rmiGroupDefinition.fields.add(tmpfld);
        addField(tmpfld);
        groups.add(rmiGroupDefinition);
    }

}

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

public class CustomJMXSensorDefinition extends SensorDefinition {
    public static String KIND = "jmxcustomattributes";
    public String description = "Monitors custom attribute values";
    public String help = "";
    public String tag = "jmx";
    public CustomJMXSensorDefinition() {
        kind = KIND;
        name = "Custom MBean Attributes Sensor";

        groups = new ArrayList<GroupDefinition>();
        GroupDefinition rmiGroupDefinition = new GroupDefinition("Connection", "Connection Settings");
        rmiGroupDefinition.fields.add( new SimpleEditFieldDefinition("rmi_string", "RMI Connection String"));
        rmiGroupDefinition.fields.add(new SimpleEditFieldDefinition("rmi_username", "Username"));
        rmiGroupDefinition.fields.add(new PasswordFieldDefinition("rmi_password", "Password", ""));
        rmiGroupDefinition.fields.add(new SimpleEditFieldDefinition("mbean", "MBean"));
        groups.add(rmiGroupDefinition);

        for (int i = 1; i < 11; i++) {
            GroupDefinition attributeDefinition = new GroupDefinition("Attribute #" + i, "Attribute");
            SimpleEditFieldDefinition objectName = new SimpleEditFieldDefinition("object_name_" + i, "Object name");
            attributeDefinition.fields.add(objectName);
            UnitFieldDefinition units = new UnitFieldDefinition("unit_name_" + i, "Unit types", "Choose the type of value the attribute returns");
            attributeDefinition.fields.add(units);
            groups.add(attributeDefinition);
        }
    }
}

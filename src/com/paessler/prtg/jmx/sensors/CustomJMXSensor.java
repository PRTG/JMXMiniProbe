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

package com.paessler.prtg.jmx.sensors;

import com.google.gson.JsonObject;
import com.paessler.prtg.jmx.channels.FloatChannel;
import com.paessler.prtg.jmx.channels.LongChannel;
import com.paessler.prtg.jmx.definitions.CustomJMXSensorDefinition;
import com.paessler.prtg.jmx.definitions.SensorDefinition;
import com.paessler.prtg.jmx.responses.DataError;
import com.paessler.prtg.jmx.responses.DataResponse;

import javax.management.MBeanServerConnection;
import javax.management.ObjectName;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CustomJMXSensor extends Sensor {
    private class AttributePair {
        public String mName, mUnit;
        public AttributePair(String name, String unit) {
            mName = name;
            mUnit = unit;
        }
    }
    private String mBean;
    private List<AttributePair> attributeList;

    @Override
    public DataResponse go() {
        if (attributeList == null)
            return null;
        DataResponse response = null;
        JMXConnector jmxc = null;
        try {
            response = new DataResponse(sensorId, mBean);
            String[] creds = {rmiUsername, rmiPassword};
            Map env = new HashMap<String, String[]>();
            env.put(JMXConnector.CREDENTIALS, creds);
            JMXServiceURL serviceURL = new JMXServiceURL(rmiString);
            jmxc = JMXConnectorFactory.connect(serviceURL, env);
            MBeanServerConnection mbsc = jmxc.getMBeanServerConnection();
            ObjectName bean = new ObjectName(mBean);

            for (AttributePair attributePair : attributeList) {
                String attributeName = attributePair.mName;
                String unitType = attributePair.mUnit;
                Object obj = mbsc.getAttribute(bean, attributeName);
                if (obj instanceof Number) {
                    Number number = (Number) obj;
                    if (obj instanceof Integer || obj instanceof Long) {
                        long val = number.longValue();
                        LongChannel channel = new LongChannel(attributeName, unitType, val);
                        response.channel.add(channel);
                    } else if (obj instanceof Float || obj instanceof Double) {
                        float val = number.floatValue();
                        FloatChannel channel = new FloatChannel(attributeName, unitType, val);
                        response.channel.add(channel);
                    }
                } else {
                    System.out.println("Couldn't cast!");
                    DataError error = new DataError(sensorId, mBean);
                    error.setError("Cast error");
                    error.setMessage("Invalid attribute value type (Service URL: " + rmiString + ")");
                    response = error;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            DataError error = new DataError(sensorId, mBean);
            error.setError("Exception");
            error.setMessage(e.getMessage() + " (Service URL: " + rmiString + ")");
            response = error;
        } finally {
            if (jmxc != null) {
                try {
                    jmxc.close();
                } catch (Exception e) {
                    // Ignore
                }
            }
        }
        return response;
    }

    @Override
    public SensorDefinition getDefinition() {
        return new CustomJMXSensorDefinition();
    }

    @Override
    public void loadFromJson(JsonObject json) {
        attributeList = new ArrayList<AttributePair>(10);
        if (json.has("rmi_string")) {
            this.rmiString = json.get("rmi_string").getAsString();
        }

        if (json.has("rmi_username")) {
            this.rmiUsername = json.get("rmi_username").getAsString();
        }

        if (json.has("rmi_password")) {
            this.rmiPassword = json.get("rmi_password").getAsString();
        }
        if (json.has("sensorid")){
            this.sensorId = json.get("sensorid").getAsInt();
        }

        if (json.has("mbean")) {
            this.mBean = json.get("mbean").getAsString();
        }

        for (int i = 1; i < 11; i++) {
            String attributeName = "object_name_" + i;
            String unitName = "unit_name_" + i;
            if (json.has(attributeName)) {
                String name = json.get(attributeName).getAsString();
                if (json.has(unitName)) {
                    String unit = json.get(unitName).getAsString();
                    if (name != null && unit != null && name.length() > 0 && unit.length() > 0) {
                        AttributePair attributePair = new AttributePair(name, unit);
                        attributeList.add(attributePair);
                    }
                }
            }
        }
    }
}

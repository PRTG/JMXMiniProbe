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
import com.paessler.prtg.jmx.channels.LongChannel;
import com.paessler.prtg.jmx.definitions.SensorDefinition;
import com.paessler.prtg.jmx.definitions.VMHealthDefinition;
import com.paessler.prtg.jmx.responses.DataError;
import com.paessler.prtg.jmx.responses.DataResponse;

import javax.management.MBeanServerConnection;
import javax.management.ObjectName;
import javax.management.openmbean.CompositeData;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;
import java.util.HashMap;
import java.util.Map;

public class VMHealthSensor extends Sensor {
    @Override
    public DataResponse go() {
        DataResponse response = null;
        JMXConnector jmxc = null;
        try {
            String[] creds = {rmiUsername, rmiPassword};
            Map env = new HashMap<String, String[]>();
            env.put(JMXConnector.CREDENTIALS, creds);
            JMXServiceURL serviceURL = new JMXServiceURL(rmiString);

            jmxc = JMXConnectorFactory.connect(serviceURL, env);
            MBeanServerConnection mbsc = jmxc.getMBeanServerConnection();
            ObjectName memoryBeanName = new ObjectName("java.lang:type=Memory");
            CompositeData heapMemoryUsage = (CompositeData) mbsc.getAttribute(memoryBeanName, "HeapMemoryUsage");
            response = new DataResponse(sensorId, "VMHealth");
            if (heapMemoryUsage != null) {
                long hmu = (Long) heapMemoryUsage.get("committed");
                long initHmu = (Long) heapMemoryUsage.get("init");
                long maxHmu = (Long) heapMemoryUsage.get("max");
                long usedHmu = (Long) heapMemoryUsage.get("used");

                LongChannel hmuChannel = new LongChannel("Committed heap memory", "BytesMemory", hmu);
                LongChannel initChannel = new LongChannel("Initialized heap memory", "BytesMemory", initHmu);
                LongChannel maxChannel = new LongChannel("Max heap memory", "BytesMemory", maxHmu);
                LongChannel usedChannel = new LongChannel("Used heap memory", "BytesMemory", usedHmu);

                response.channel.add(hmuChannel);
                response.channel.add(initChannel);
                response.channel.add(maxChannel);
                response.channel.add(usedChannel);
            }

            ObjectName threadingBeanName = new ObjectName("java.lang:type=Threading");
            int liveThreadCount = (Integer) mbsc.getAttribute(threadingBeanName, "ThreadCount");
            int peakThreadCount = (Integer) mbsc.getAttribute(threadingBeanName, "PeakThreadCount");
            int daemonThreadCount = (Integer) mbsc.getAttribute(threadingBeanName, "DaemonThreadCount");
            long totalThreadCount = (Long) mbsc.getAttribute(threadingBeanName, "TotalStartedThreadCount");
            LongChannel liveThreadChannel = new LongChannel("Live threads", "Count", liveThreadCount);
            LongChannel peakThreadChannel = new LongChannel("Peak threads", "Count", peakThreadCount);
            LongChannel daemonThreadChannel = new LongChannel("Daemon threads", "Count", daemonThreadCount);
            LongChannel totalThreadChannel = new LongChannel("Total threads started", "Count", totalThreadCount);
            response.channel.add(liveThreadChannel);
            response.channel.add(peakThreadChannel);
            response.channel.add(daemonThreadChannel);
            response.channel.add(totalThreadChannel);

            ObjectName classBeanName = new ObjectName("java.lang:type=ClassLoading");
            long totalLoadedClasses = (Long) mbsc.getAttribute(classBeanName, "TotalLoadedClassCount");
            int currentLoadedClasses = (Integer) mbsc.getAttribute(classBeanName, "LoadedClassCount");
            long unloadedClasses = (Long) mbsc.getAttribute(classBeanName, "UnloadedClassCount");
            LongChannel totalLoadedClassChannel = new LongChannel("Total classes loaded", "Count", totalLoadedClasses);
            LongChannel currentLoadedClassChannel = new LongChannel("Current classes loaded", "Count", currentLoadedClasses);
            LongChannel unloadedClassChannel = new LongChannel("Total classes unloaded", "Count", unloadedClasses);
            response.channel.add(totalLoadedClassChannel);
            response.channel.add(currentLoadedClassChannel);
            response.channel.add(unloadedClassChannel);

        } catch (Exception e) {
            e.printStackTrace();
            DataError error = new DataError(sensorId, "VMHealth");
            error.setCode(-1);
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
        return new VMHealthDefinition();
    }

    @Override
    public void loadFromJson(JsonObject json) {
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
    }
}

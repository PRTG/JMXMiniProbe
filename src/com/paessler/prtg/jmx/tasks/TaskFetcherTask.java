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

package com.paessler.prtg.jmx.tasks;

import com.google.gson.*;
import com.paessler.prtg.jmx.Logger;
import com.paessler.prtg.jmx.ProbeContext;
import com.paessler.prtg.jmx.definitions.CustomJMXSensorDefinition;
import com.paessler.prtg.jmx.definitions.VMHealthDefinition;
import com.paessler.prtg.jmx.network.NetworkWrapper;
import com.paessler.prtg.jmx.network.Tasks;
import com.paessler.prtg.jmx.responses.DataResponse;
import com.paessler.prtg.jmx.sensors.CustomJMXSensor;
import com.paessler.prtg.jmx.sensors.Sensor;
import com.paessler.prtg.jmx.sensors.VMHealthSensor;

import javax.servlet.ServletContext;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.TimerTask;

public class TaskFetcherTask extends TimerTask {
    final private ServletContext mServletContext;
    final private ProbeContext mProbeContext;

    public TaskFetcherTask(ServletContext context, ProbeContext probeContext) {
        mServletContext = context;
        mProbeContext = probeContext;
    }

    @Override
    public void run() {
        String url = Tasks.buildUrl(mProbeContext);
        String json = null;
        try {
            json = NetworkWrapper.fetch(url);
        } catch (IOException e) {
            Logger.log(mServletContext, e.getLocalizedMessage());
        }

        if (json == null)
            return;

        JsonParser parser = new JsonParser();
        JsonElement top = parser.parse(json);
        JsonArray tasksArray = top.getAsJsonArray();
        int len = tasksArray.size();
        List<DataResponse> responseList = new ArrayList<DataResponse>();
        for (int i = 0; i < len; i++) {
            JsonObject task = tasksArray.get(i).getAsJsonObject();
            String kind = task.get("kind").getAsString();
            int id = task.get("sensorid").getAsInt();
            try {
                Sensor sensor;
                if (kind.equals(VMHealthDefinition.KIND)) {
                    sensor = new VMHealthSensor();
                } else if (kind.equals(CustomJMXSensorDefinition.KIND)) {
                    sensor = new CustomJMXSensor();
                } else {
                    continue;
                }
                sensor.loadFromJson(task);
                sensor.sensorid = id;
                DataResponse response = sensor.go();
                if (response == null)
                    return;
                responseList.add(response);
            } catch (Exception e) {
                Logger.log(mServletContext, "Error executing sensor: " + e.getLocalizedMessage());
            }
        }

        String dataUrl =  String.format("https://%s/probe/data?gid=%s&key=%s&protocol=%d", mProbeContext.host,
                mProbeContext.gid,
                mProbeContext.key,
                mProbeContext.protocol);
        if (responseList.size() > 0) {
            Gson gson = new Gson();
            String dataJson = gson.toJson(responseList);
            try {
                NetworkWrapper.post(dataUrl, dataJson);
            } catch (Exception e) {
                Logger.log(mServletContext, "Error sending data: " + e.getLocalizedMessage());
            }
        }


    }
}

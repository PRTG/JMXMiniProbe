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

package com.paessler.prtg.jmx.network;

import com.google.gson.Gson;
import com.paessler.prtg.jmx.ProbeContext;
import com.paessler.prtg.jmx.definitions.SensorDefinition;

import java.util.List;

public class Announcement {
    transient public String name;
    transient public String version;
    transient public int baseInterval;
    transient public int protocol = 1;
    public List<SensorDefinition> sensors;

    public Announcement(String name, String version, int baseInterval, List<SensorDefinition> sensors) {
        this.name = name;
        this.version = version;
        this.baseInterval = baseInterval;
        this.sensors = sensors;
    }

/*    public String buildUrl(ProbeContext probeContext) {
        return String.format("http://%s/probe/announce?gid=%s&key=%s&protocol=%d&version=%s&name=%s&baseinterval=%d",
                probeContext.host,
                probeContext.gid,
                probeContext.key,
                probeContext.protocol,
                this.version,
                probeContext.encode(this.name),
                this.baseInterval);
    }
*/    
    public String buildUrl(ProbeContext probeContext) {
      return String.format("%s&version=%s&name=%s&baseinterval=%d",
    		  probeContext.getURLPrefix("/probe/announce"),
              this.version,
              probeContext.encode(this.name),
              this.baseInterval);
  }
  
    public String toString() {
        Gson gson = new Gson();
        return gson.toJson(sensors);
    }
}

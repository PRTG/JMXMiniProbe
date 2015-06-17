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

package com.paessler.prtg.jmx.responses;

import com.paessler.prtg.jmx.channels.Channel;

import java.util.ArrayList;
import java.util.List;

public class DataResponse {
    public int sensorid = -1;
    public Long  time = null;
    public String message = null;
    public transient String sensorName = "";
    protected List<Channel> channel;
    
    // --------------------------------------------
    public int getChannelCount(){
    	int retVal = -1;
    	if(channel != null){
    		retVal =  channel.size();
    	}
    	return retVal;
    }
    public List<Channel> getChannels(){
    	return channel;
    }
    // --------------------------------------------
    public void addChannel(Channel ch){
    	channel.add(ch);
    }
    // --------------------------------------------
    public void addChannels(List<Channel> chs){
    	channel.addAll(chs);
    }
    // --------------------------------------------
    public static boolean timestampEnabled = false;
    public static void setTimestampEnabled(boolean val){
    	timestampEnabled = val;
    }
    public static boolean isTimestampEnabled(){
    	return timestampEnabled;
    }
    // --------------------------------------------
    public String getMessage() 				{return this.message;  }
    public void setMessage(String message)	{this.message = message; }
    public void addMessage(String message) {
    	if(this.message == null){
    		this.message = message;
    	} else {
    		this.message += "; " + message; 
    	}
    }
    // --------------------------------------------
    public void setTime(){
    	time = System.currentTimeMillis() / 1000L;
//    	long unixTimestamp = Instant.now().getEpochSecond();
    }
    
    // --------------------------------------------
    public DataResponse(int sensorId, String sensorName) {
    	if(timestampEnabled)
    		setTime();
        this.sensorid = sensorId;
        this.sensorName = sensorName;
        this.channel = new ArrayList<Channel>();
    }
}

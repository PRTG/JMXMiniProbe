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
package com.paessler.prtg.jmx.sensors.http;

import java.util.ArrayList;
import java.util.List;

import com.paessler.prtg.jmx.definitions.FieldDefinition;
import com.paessler.prtg.jmx.definitions.GroupDefinition;
import com.paessler.prtg.jmx.definitions.RadioFieldDefinition;
import com.paessler.prtg.jmx.definitions.RemoteSensorDefinition;
import com.paessler.prtg.jmx.definitions.SimpleEditFieldDefinition;

/**
 * @Author JR Andreassen
 */

public class HttpSensorDefinition extends RemoteSensorDefinition {
	public static final String KIND = "mjhttpsensor";
	public static final String TAG  = "mjhttpsensor";
	public static final String HTTP_SENSOR_VALS 	= "httpsensorvals";
	public static final String FIELD_HTTP_VECTOR	= "url";
	public static final String FIELD_HTTP_METHOD 	= "http_method";
	public static final String FIELD_HTTP_POSTDATA 	= "post_data";
	
	public static final String FIELD_HTTP_AUTHMETH 	= "auth_method";
	
	public static String getMethod(int val){
		String retVal = null;
		if(val == 1)  		retVal =  "GET";
		else if(val == 2)  	retVal =  "POST";
		else if(val == 3)  	retVal =  "HEAD";
		return retVal;
	}
    // -----------------------------------------------------------------------
	public static String getAuthentication(int val){
		String retVal = null;
		if(val == 1)  		retVal =  "NONE";
		else if(val == 2)  	retVal =  "BASIC";
		return retVal;
	}
    // -----------------------------------------------------------------------
	@Override
	public List<GroupDefinition> getGroups(List<GroupDefinition> groups){
//		List<GroupDefinition> retVal = super.getGroups(groups);
		List<GroupDefinition> retVal = new ArrayList<GroupDefinition>();
		

		GroupDefinition group = new GroupDefinition(HTTP_SENSOR_VALS, "HTTP Specific settings");
        group.fields.add(getTimeoutField(5, 1, 900, "sec"));
        group.fields.add(getPortField(53, "HTTP Port"));
        // -------------------------
        FieldDefinition tmpfield = new SimpleEditFieldDefinition(FIELD_HTTP_VECTOR, "URL", 
        		 "Enter a valid URL to monitor. The server part (e.g. www.paessler.com) "
        		+"may be different from the 'DNS Name' property in the settings of the "
        		+"associated server.");
        tmpfield.setRequired(FieldDefinition.FIELDVALUE_REQUIRED_TRUE);
        group.fields.add( tmpfield);
        // -------------------------
        RadioFieldDefinition radiofld = new RadioFieldDefinition(FIELD_HTTP_METHOD, "Query Type", "Choose the type of the HTTP request", 1);
        radiofld.setRequired(FieldDefinition.FIELDVALUE_REQUIRED_TRUE);
        radiofld.addOption(1, "GET");
        radiofld.addOption(2, "POST");
        radiofld.addOption(3, "HEAD");
        group.fields.add(radiofld);
        // -------------------------
        retVal.add(group);
        // -------------------------
        // -------------------------
        group = new GroupDefinition("Authentication", "HTTP Authentication");
        // -------------------------
        radiofld = new RadioFieldDefinition(FIELD_HTTP_AUTHMETH, "Authentication Method", "Choose the type of authentication used", 1);
        radiofld.setRequired(FieldDefinition.FIELDVALUE_REQUIRED_TRUE);
        radiofld.addOption(1, "No authentication");
        radiofld.addOption(2, "Basic");
        group.fields.add(radiofld);
        // -------------------------
        group.fields.add(getUserNameField("Provide username here if target requires authentication", false));
        group.fields.add(getPasswordField("Provide password here if target requires authentication", false));
        // -------------------------
        retVal.add(group);
        // -------------------------
		return retVal;
	}	
    // -----------------------------------------------------------------------
    public HttpSensorDefinition() {
       	super(KIND, "HTTP Sensor", "Monitors a web server using HTTP", TAG, 
        		"Monitors a web server using HTTP");
        // -------------------------

    }

}

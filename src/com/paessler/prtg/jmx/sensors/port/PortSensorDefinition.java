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
package com.paessler.prtg.jmx.sensors.port;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.Expose;
import com.paessler.prtg.jmx.definitions.FieldDefinition;
import com.paessler.prtg.jmx.definitions.GroupDefinition;
import com.paessler.prtg.jmx.definitions.IntegerField;
import com.paessler.prtg.jmx.definitions.RemoteSensorDefinition;
import com.paessler.prtg.jmx.definitions.SimpleEditFieldDefinition;

/**
 * @Author JR Andreassen
 */

public class PortSensorDefinition extends RemoteSensorDefinition {
	public static final String KIND = "portrangesensor";
	public static final String TAG  = "mjportrangesensor";
	public static final String PORT_SENSOR_VALS 	= "portrangesensor";
	public static final String FIELD_PORT_VECTOR	= "portlist";
	public static final String FIELD_DELAY 			= "delay";

	@Expose(serialize = false)
	private boolean showportList = true;
	// -----------------------------------------------------------------------
    public boolean isShowportList() {return showportList;}
	public void setShowportList(boolean showportlist) {	this.showportList = showportlist;}
	// -----------------------------------------------------------------------
	@Override
	public List<GroupDefinition> getGroups(List<GroupDefinition> groups){
		List<GroupDefinition> retVal = super.getGroups(groups);

		GroupDefinition group = new GroupDefinition(PORT_SENSOR_VALS, "Port Settings");
        
        // -------------------------
		IntegerField tmpIfield = new IntegerField(FIELD_DELAY, "Port-by-Port-Delay (ms)");
        tmpIfield.setHelp("Define how long the sensor will wait to go to the next port while running through all given ports.");
        tmpIfield.setMinimum(0);
        tmpIfield.setMaximum(1000);
        tmpIfield.setDefaultValue(0);
        group.fields.add(tmpIfield);
        // -------------------------
        if(isShowportList()){
	        FieldDefinition tmpfield = new SimpleEditFieldDefinition(FIELD_PORT_VECTOR, "Port List", "Please provide Port(s). Comma separated List");
	        tmpfield.setRequired(FieldDefinition.FIELDVALUE_REQUIRED_TRUE);
	        group.fields.add( tmpfield);
        }
        // -------------------------
        retVal.add(group);
		return retVal;
	}	
    // -----------------------------------------------------------------------
    public PortSensorDefinition(String kind, String name, String description, String tag, String help, boolean showportlist) {
        super(kind, name, description, tag, help);
        setShowportList(showportlist);
        setGroups(getGroups( new ArrayList<GroupDefinition>()));
    }
    // -----------------------------------------------------------------------
    public PortSensorDefinition(String kind, String name, String description, String tag, String help) {
        this(kind, name, description, tag, help, true);
    }
    // -----------------------------------------------------------------------
    public PortSensorDefinition() {
       	this(KIND, "Port Range Sensor", "Monitors network services by connecting ports specified TCP/IP ports", TAG, 
        		"Tries to connect to the specified TCP/IP port and waits for the request to be accepted");
        // -------------------------
    }

}

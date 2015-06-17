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
package com.paessler.prtg.jmx.definitions;

import java.util.List;

/**
 * @Author JR Andreassen
 */

public abstract class RemoteSensorDefinition extends SensorDefinition {

	protected FieldDefinition getHostField(){
        FieldDefinition retVal = new SimpleEditFieldDefinition(SensorConstants.REMOTE_HOST, "Host name"); 
        retVal.setHelp("DNS(server.domain.com) Name or numeric IP(A.B.C.D) of Host");
        return retVal;
	}
	
	protected FieldDefinition getPortField(int defaultvalue, String comment){
        // -------------------------
        IntegerField retVal = new IPPortField(SensorConstants.REMOTE_PORT, 
        		"Remote Host Port", (comment != null ? comment :"Please provide Port"));
        retVal.setDefaultValue(defaultvalue);
        return retVal;
	}
	
	// ------------------------------------------------------------------------------
	protected FieldDefinition getTimeoutField(int defaultvalue, int min, int max, String units){
        // -------------------------
        IntegerField retVal = new IntegerField(SensorConstants.TIMEOUT, "timeout");
        retVal.setHelp("Enter a timeout in"+units+". If the reply takes longer than this value, the request is aborted and triggers an error message. The maximum value is "+max+".");
        retVal.setMinimum(min);
        retVal.setMaximum(max);
        retVal.setDefaultValue(defaultvalue);
        return retVal;
		
	}
	// ------------------------------------------------------------------------------
	public List<GroupDefinition> getGroups(List<GroupDefinition> groups){
		List<GroupDefinition> retVal = super.getGroups(groups);
		
        GroupDefinition group = new GroupDefinition(SensorConstants.CONNECTION, "Connection Settings");
        addGroup(group);
        // -------------------------
        FieldDefinition tmpfld = getHostField();         
        addField(tmpfld);
        group.fields.add(tmpfld);
        tmpfld = getTimeoutField(900, 1, 900000, "ms");
        addField(tmpfld);
        group.fields.add(tmpfld);
        
        retVal.add(group);
		return retVal;
	}	
    public RemoteSensorDefinition(String kind, String name, String description, String tag, String help) {
        super(kind, name, description, tag, help);

        // -------------------------
    }

}

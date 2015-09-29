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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class SensorDefinition {
    public String kind = "Unknown";
    public String name = "Unknown";
    public String description;
	public String help;
    public String tag;
    public List<GroupDefinition> groups = null;
    
    // -----------------------------------------------------------------------
    protected Map<String,FieldDefinition> fields = new HashMap<String,FieldDefinition>();
    public FieldDefinition addField(FieldDefinition val){
    	FieldDefinition retVal = null;
    	if(val != null){
    		retVal = fields.put(val.getName(), val);
    	}
    	return retVal;
    }
    // --------------------------------
    public FieldDefinition getField(String name){
    	FieldDefinition retVal = null;
    	if(name != null){
    		retVal = fields.get(name);
    	}
    	return retVal;
    }
    // -----------------------------------------------------------------------
    public void setFieldDefaultValue(String name, Object value){
    	FieldDefinition field = getField(name);
    	if(field != null && value != null){
    		field.setDefaultValue(value.toString());
    	}
    }
    // -----------------------------------------------------------------------
    public void setFieldCaption(String name, Object value){
    	FieldDefinition field = getField(name);
    	if(field != null && value != null){
    		field.setCaption(value.toString());
    	}
    }
    // -----------------------------------------------------------------------
    public void setFieldHelp(String name, Object value){
    	FieldDefinition field = getField(name);
    	if(field != null && value != null){
    		field.setHelp(value.toString());
    	}
    }
    // -----------------------------------------------------------------------
	protected FieldDefinition getUserNameField(String help, boolean required){
        FieldDefinition retVal = new SimpleEditFieldDefinition(SensorConstants.USERNAME, "Username/Login");
        // -------------------------
        retVal.setHelp(help);
        if(required){
            retVal.setRequired(FieldDefinition.FIELDVALUE_REQUIRED_TRUE);
        }
        return retVal;
	}
    // -----------------------------------------------------------------------
	protected FieldDefinition getPasswordField(String help, boolean required){
        FieldDefinition retVal = new SimpleEditFieldDefinition(SensorConstants.PASSWORD, "Password");
        if(required){
            retVal.setRequired(FieldDefinition.FIELDVALUE_REQUIRED_TRUE);
        }
        retVal.setHelp(help);
        return retVal;
	}

    
    // -----------------------------------------------------------------------
	public List<GroupDefinition> getGroups(List<GroupDefinition> groups){
		return groups;
	}
    // -----------------------------------------------------------------------
    public SensorDefinition(String kind, String name, String description, String tag, String help){
        this.kind = kind;
        this.name = name;
        this.description = description;
        this.help = help;
        this.tag = tag;
        
        setGroups(getGroups( new ArrayList<GroupDefinition>()));
    }
/*    public SensorDefinition(String kind, String name, String description, String tag){
        this(kind, name, description, tag, null);
    }
*/    
    // -----------------------------------------------------------------------
    public String getKind() {
		return kind;
	}
    // -------------------------------------------
	public void setKind(String kind) {
		this.kind = kind;
	}
    // -----------------------------------------------------------------------
	public String getName() {
		return name;
	}
    // -------------------------------------------
	public void setName(String name) {
		this.name = name;
	}
    // -----------------------------------------------------------------------
	public String getDescription() {
		return description;
	}
    // -------------------------------------------
	public void setDescription(String description) {
		this.description = description;
	}
    // -----------------------------------------------------------------------
	public String getHelp() {
		return help;
	}
    // -------------------------------------------
	public void setHelp(String help) {
		this.help = help;
	}
    // -----------------------------------------------------------------------
	public String getTag() {
		return tag;
	}
    // -----------------------------------------------------------------------
	public void setTag(String tag) {
		this.tag = tag;
	}
    // -----------------------------------------------------------------------
	public boolean addGroup(GroupDefinition toadd) {
		boolean retVal = false;
		if(groups != getGroups()){
	        groups.add(toadd);
		}
		return retVal;
	}
    // -----------------------------------------------------------------------
	public List<GroupDefinition> getGroups() {
		if(groups == null){
	        groups = new ArrayList<GroupDefinition>();
		}
		return groups;
	}
    // -------------------------------------------
	public void setGroups(List<GroupDefinition> groups) {
		this.groups = groups;
	}
}

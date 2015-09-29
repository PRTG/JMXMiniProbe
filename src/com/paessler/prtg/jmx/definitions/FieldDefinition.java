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

import com.google.gson.annotations.SerializedName;

public abstract class FieldDefinition {
    public static final String FIELDVALUE_REQUIRED_TRUE  = "1";
    public static final String FIELDVALUE_REQUIRED_FALSE  = "0";
	public String type;
    public String name;
    public String caption;
    public @SerializedName("default") Object defaultValue;
    public String required;
    public String help;

    // ------------------------------------------------------------------
    public String getType() {return type;}
	public void setType(String type) {this.type = type;	}
	// --------------------------------------------------------------
	public String getName() 		{return name;}
	public void setName(String name){this.name = name;	}
	// --------------------------------------------------------------
	public String getCaption() {return caption;	}
	public void setCaption(String caption) {this.caption = caption;}

	// --------------------------------------------------------------
	public Object getDefaultValue() {return defaultValue;	}
	public void setDefaultValue(Object defaultValue) {
		this.defaultValue = defaultValue;
	}
	// --------------------------------------------------------------
	public String getRequired() {	return required;	}
	public void setRequired(String required) {this.required = required;	}
	// --------------------------------------------------------------
	public String getHelp() {	return help;	}
	public void setHelp(String help) {	this.help = help;}
	// --------------------------------------------------------------
    public FieldDefinition(String type) {
    	this.type = type;
    }
	// --------------------------------------------------------------
/*  // Not yet supported in MiniProbe
//    public String inherritable;
//	public @SerializedName("hiddenonreadonly") boolean hiddenOnReadonly;

    public @SerializedName("masterfield") String masterField;
	public @SerializedName("ismaster") boolean masterValue;
    public @SerializedName("mastervalues") Vector<String> masterValues;
    public @SerializedName("offvalues") Vector<String> offValues;
    // ------------------------------------------------------------------
    // ------------------------------------------------------------------
    public String getMasterField() {
		return masterField;
	}
	// --------------------------------------------------------------
	public void setMasterField(String masterField) {
		this.masterField = masterField;
	}
	// --------------------------------------------------------------
	public boolean isMasterValue() {
		return masterValue;
	}
	// --------------------------------------------------------------
	public boolean getMasterValue() {
		return masterValue;
	}
	// --------------------------------------------------------------
	public void setMasterValue(boolean masterValue) {
		this.masterValue = masterValue;
	}
	// --------------------------------------------------------------
	public Vector<String> getMasterValues() {
		return masterValues;
	}
	// --------------------------------------------------------------
	public void addMasterValue(String value) {
		this.masterValues.add(value);
	}
	// --------------------------------------------------------------
	public void setMasterValues(Vector<String> masterValues) {
		this.masterValues = masterValues;
	}
	// --------------------------------------------------------------
	public Vector<String> getOffValues() {
		return offValues;
	}
	// --------------------------------------------------------------
	public void setOffValues(Vector<String> offValues) {
		this.offValues = offValues;
	}
*/
	// --------------------------------------------------------------
    public FieldDefinition(String type, String name, String caption, String help, Object defaultValue) {
    	this(type);
    	this.name = name;
    	this.caption = caption;
    	this.help = help;
    	this.required = null;
    	this.defaultValue = defaultValue;
    }
    public FieldDefinition(String type, String name, String caption, String help) {
    	this(type, name, caption, help, null);
    	
    }
}

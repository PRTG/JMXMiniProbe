/*
 * Copyright (c) 2014-2015, Paessler AG <support@paessler.com>
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
package com.paessler.prtg.jmx.sensors.profile;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.apache.commons.digester3.Digester;

public class Entry  implements Comparable<Entry> {
	public String			object;
	public String			description;
	public String			displayValue;
	public String			shortDescription;
    public String 			comment;
    
	// ---------------------------------------------
	// ---------------------------------------------
	public Map<String,Object>	propMap = new HashMap<String,Object>();
	// ---------------------------------------------
	public Map<String,Object> getProperties() {return propMap;}
	public Object getProperty(String key) {return propMap.get(key);}
	public Object setProperty(String key, Object value) {return propMap.put(key, value);}
	public Object addProperty(PropertyEntry prope) 
	{return propMap.put(prope.getName(), prope.getValue());}
    // ------------------------------------------------------------
	public List<Attribute<String>>	attributes = new Vector<Attribute<String>>();

	// --------------------------------
	public String getObject() {return object;}
	public void setObject(String object) {this.object = object;	}
	// --------------------------------
	public String getDisplayValue() {return displayValue;}
	public void setDisplayValue(String displayValue) {	this.displayValue = displayValue;}
	// --------------------------------
	public String getDescription() {	return description;	}
	public void setDescription(String description) {this.description = description;	}
	// --------------------------------
	public String getShortDescription() {	return shortDescription;	}
	public void setShortDescription(String shortdescription) {
		this.shortDescription = shortdescription;
	}
	// ----------------------------------
	public String getComment() {return comment;}
	public void setComment(String comment) {
		this.comment = comment;
	}
	// --------------------------------

	public List<Attribute<String>> getAttributes() {return attributes;}
	public void setAttributes(List<Attribute<String>> attributes) {
		this.attributes = attributes;
	}
	public void addAttribute(Attribute<String> attribute) {
		this.attributes.add(attribute);
	}
	// --------------------------------

	public static Digester describe4Digester(Digester digester, String base) throws Exception{
		String mybase = base;
        digester.addBeanPropertySetter( mybase+"/object", "object" );
        digester.addBeanPropertySetter( mybase+"/description", "description" );
        digester.addBeanPropertySetter( mybase+"/shortdescription", "shortDescription" );
        digester.addBeanPropertySetter( mybase+"/displayvalue", "displayValue" );
        digester.addBeanPropertySetter( mybase+"/comment", "comment" );
		String tmp = mybase + "/attributes/attribute";
		digester.addObjectCreate( tmp, StringAttribute.class );
        Attribute.describe4Digester(digester, tmp);
        digester.addSetNext( tmp, "addAttribute" );
        // Properties
		tmp = mybase + "/properties/property";
        digester.addObjectCreate( tmp , PropertyEntry.class );
        PropertyEntry.describe4Digester(digester, tmp);
        digester.addSetNext( tmp, "addProperty" );

        return digester;
	}
	// -------------------------------------------
	public StringBuilder toXML(StringBuilder target, String prefix, String suffix){
		
		target.append(prefix);
		target.append("<entry>");
		target.append(suffix);
		String tmp = prefix + "\t";

		// -------------------------------------
		ProfileFactory.addXMLElement(target, "object", getObject(), tmp, suffix);
		ProfileFactory.addXMLElement(target, "displayvalue", getDisplayValue(), tmp, suffix);
		ProfileFactory.addXMLElement(target, "description", getDescription(), tmp, suffix);
		ProfileFactory.addXMLElement(target, "shortdescription", getShortDescription(), tmp, suffix);
		ProfileFactory.addXMLElement(target, "comment", getComment(), tmp, suffix);
		// -------------------------------------
		ProfileFactory.toXML(target, getProperties(), tmp, suffix);
		// -------------------------------------
		target.append(tmp);
		target.append("<attributes>");
		target.append(suffix);
		String tmp2 = tmp + "\t";
		for(Attribute<?> curr: getAttributes()){
			target = curr.toXML(target, tmp2, suffix);
		}
		target.append(tmp);
		target.append("</attributes>");
		target.append(suffix);
		// -------------------------------------
		target.append(prefix);
		target.append("</entry>");
		target.append(suffix);
		return target;
	}
	// -------------------------------------------
	public String toXML(String prefix, String suffix){
		StringBuilder retVal = toXML(new StringBuilder(), prefix, suffix);
		
		return retVal.toString();
	}
    // -------------------------------------------
    @Override
    public int compareTo(Entry other){
    	return object.compareTo(other.getObject());
    }
	
	// -----------------------------------------------------------------------------------
	  public void sort(){
	        Collections.sort(getAttributes());
	  }
}

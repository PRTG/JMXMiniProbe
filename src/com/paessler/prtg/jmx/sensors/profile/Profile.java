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

public class Profile {
	public String		baseclass;
	public String		kind;
	public String		tag;
	public String		name;
	public String		description;
	public String		tags;
	public String		help;
	public List<Entry> entries = new Vector<Entry>();
	// ---------------------------------------------
	public Map<String,Object>	propMap = new HashMap<String,Object>();
	// ---------------------------------------------
	public Map<String,Object> getProperties() {return propMap;}
	public Object getProperty(String key) {return propMap.get(key);}
	public Object setProperty(String key, Object value) {return propMap.put(key, value);}
	public Object addProperty(PropertyEntry prope) 
	{return propMap.put(prope.getName(), prope.getValue());}
	// ---------------------------------------------
	public String getBaseclass() {	return baseclass;}
	public void setBaseclass(String baseclass) {this.baseclass = baseclass;	}
	// ---------------------------------------------
	public String getKind() {	return kind;}
	public void setKind(String kind) {this.kind = kind;}
	// ---------------------------------------------
	public String getTag() {	return tag;}
	public void setTag(String tag) {this.tag = tag;}
	// ---------------------------------------------
	public String getName() {return name;}
	public void setName(String name) {this.name = name;	}
	// ---------------------------------------------
	public String getDescription() {return description;	}
	public void setDescription(String description) {this.description = description;	}
	// ---------------------------------------------
	public String getHelp() {return help;	}
	public void setHelp(String help) {this.help = help;	}
	// ---------------------------------------------
	public String getTags() {	return tags;}
	public void setTags(String tags) {this.tags = tags;}
	// ---------------------------------------------
	public List<Entry> getEntries() {	return entries;	}
	public void setEntries(List<Entry> entries) {this.entries = entries;}
	public void addEntry(Entry entry) {this.entries.add(entry);}


	public static Digester describe4Digester(Digester digester, String base) throws Exception{
			String mybase = base;
	        digester.addBeanPropertySetter( mybase+"/baseclass", "baseclass" );
	        digester.addBeanPropertySetter( mybase+"/kind", "kind" );
	        digester.addBeanPropertySetter( mybase+"/tag", "tag" );
	        digester.addBeanPropertySetter( mybase+"/name", "name" );
	        digester.addBeanPropertySetter( mybase+"/description", "description" );
	        digester.addBeanPropertySetter( mybase+"/help", "help" );
			String tmp = mybase + "/entries/entry";
	        digester.addObjectCreate( tmp , Entry.class );
	        Entry.describe4Digester(digester, tmp);
	        digester.addSetNext( tmp, "addEntry" );
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
		target.append("<profile>");
		target.append(suffix);
		String tmp = prefix + "\t";
		// -------------------------------------
		ProfileFactory.addXMLElement(target, "name", getName(), tmp, suffix);
		ProfileFactory.addXMLElement(target, "tag", getTag(), tmp, suffix);
		ProfileFactory.addXMLElement(target, "kind", getKind(), tmp, suffix);
		ProfileFactory.addXMLElement(target, "description", getDescription(), tmp, suffix);
		ProfileFactory.addXMLElement(target, "baseclass", getBaseclass(), tmp, suffix);
		ProfileFactory.addXMLElement(target, "help", getHelp(), tmp, suffix);
		// -------------------------------------
		ProfileFactory.toXML(target, getProperties(), tmp, suffix);
		// -------------------------------------
		target.append(tmp);
		target.append("<entries>");
		target.append(suffix);
		for(Entry curre: getEntries()){
			target = curre.toXML(target, tmp + "\t", suffix);
		}
		target.append(tmp);
		target.append("</entries>");
		target.append(suffix);
		
		target.append(prefix);
		target.append("</profile>");
		target.append(suffix);
		return target;
	}
	// -------------------------------------------
	public String toXML(String prefix, String suffix){
		StringBuilder retVal = toXML(new StringBuilder(), prefix, suffix);
		return retVal.toString();
	}
	// -----------------------------------------------------------------------------------
	 public void sort(){
		 Collections.sort(getEntries());
		  
	  }
	

}

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
package com.paessler.prtg.jmx.sensors.jmx;

import java.util.ArrayList;
import java.util.List;

import javax.management.ObjectName;

import com.paessler.prtg.jmx.sensors.profile.Attribute;
import com.paessler.prtg.jmx.sensors.profile.Entry;

public class JMXBean {
    public ObjectName objectName;
    public String name;
    public String description;
    public String shortDescription;
    public List<JMXAttribute> attributeList;
    // ------------------------------------------------------
    public JMXBean(Entry entry) {
    	this(entry.getObject());
    	name = entry.getObject();
        description = entry.getDescription();
        shortDescription = entry.getShortDescription();
    	JMXAttribute attr;
    	for(Attribute<?> curr : entry.getAttributes()){
    		attr = new JMXAttribute(curr);
    		if(attr != null){
    			// Add my short description to the Display value
    			if(shortDescription != null){
    				attr.setDescription(shortDescription+": "+attr.getDescription());
    			}
    			addAttributePair(attr);
    		}
    		
    	}
    }
    // ------------------------------------------------------
    public JMXBean(ObjectName objname) {
    	this.objectName = objname;
    	this.attributeList = new ArrayList<JMXAttribute>(10);
    }
    // ------------------------------------------------------
    public JMXBean(String name) {
    	this(JMXUtils.getObjectName(name));
    }
    // ------------------------------------------------------
    public void addAttributePair(JMXAttribute pair){
    	if(pair != null ){
    		attributeList.add(pair);
    	}
    }
    // ------------------------------------------------------
    public void addAttributePair(String name, String unit){
    	if(name != null && unit != null){
    		addAttributePair(new JMXAttribute(name, unit));
    	}
    }

}

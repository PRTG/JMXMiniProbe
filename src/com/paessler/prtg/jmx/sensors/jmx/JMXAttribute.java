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

import java.util.List;
import java.util.Vector;
import java.util.regex.Pattern;

import javax.management.openmbean.CompositeDataSupport;

import com.paessler.prtg.jmx.channels.Channel;
import com.paessler.prtg.jmx.channels.Channel.Unit;
import com.paessler.prtg.jmx.channels.FloatChannel;
import com.paessler.prtg.jmx.channels.LongChannel;
import com.paessler.prtg.jmx.sensors.profile.Attribute;

public class JMXAttribute {
	public static String SEPARATOR_STRING = ".";
	public static double DEFAULT_NOOPVAL = 1.0d;
	public List<String> object;
    public String description = null;
    public Unit unit;
    public double mpy = DEFAULT_NOOPVAL, div = DEFAULT_NOOPVAL;
    public boolean enabled = true;

    public List<String> parseObjectName(String name){
    	List<String> retVal = new Vector<String>();
    	if(name != null){
    		String[] strings = name.split(Pattern.quote(SEPARATOR_STRING));
    		for(String curr: strings){
        		retVal.add(curr);
    		}
    	}
    	return retVal;
    }
    // ---------------------------------------------
    public String getObjectName(){
    	return getObjectName(SEPARATOR_STRING);
    }
    // ---------------------------------------------
    public String getObjectName(String sep){
    	StringBuilder retVal = new StringBuilder();
    	boolean isFirst = true;
		for(String curr: object){
			if(!isFirst){
				retVal.append(sep);
				isFirst = false;
			}
    		retVal.append(curr);
		}
    	return retVal.toString();
    }
    // ---------------------------------------------
    public String toString(){
    	StringBuilder retVal = new StringBuilder();
    	retVal.append("JMXAttribute[");
    	retVal.append(getObjectName());
		retVal.append("|Unit=");
		retVal.append(getUnit().toString());
		retVal.append("]");
    	return retVal.toString();
    }
    // ---------------------------------------------
    public JMXAttribute(Attribute<?> attr) {
        setObject(attr.getObject().toString());
        setUnit(attr.getUnitEnum());
        setDescription(attr.getDescription());
    }
    
    public JMXAttribute(String name, Unit unit) {
        setObject(name);
        setUnit(unit);
    }
    public JMXAttribute(String name, String unit) {
    	this(name, Channel.toUnit(unit));
    }
	// --------------------------------
    public int getObjectCount() {return object.size();}
    public boolean isObjectIndexValid(int index) {return object != null && (index < getObjectCount());}
	// --------------------------------
	public String getObject(int index) 
	{	String retVal = null;
		if(isObjectIndexValid(index)){
			retVal = object.get(index);
		}
		return retVal;
	}
	public void setObject(String name, int index) 
	{
		if(index > getObjectCount()){
			// Adjust to make sure there is space
			for(int idx=getObjectCount();idx < index;++idx){
				this.object.add(null);				
			}
		}
		if(index == getObjectCount()){
			this.object.add(name);
		} else if(index < getObjectCount()){
			this.object.set(index, name);
		}
	}
	// --------------------------------
	public String getObject() {	return getObject(0);}
	public void setObject(String name) {this.object = parseObjectName(name);	}
	// --------------------------------
	public Unit getUnit() {	return unit;	}
	public void setUnit(Unit unit) {this.unit = unit;	}
	// --------------------------------
	public String getDescription(String sep) {
		return (description == null ? getObjectName(sep) :description);
	}
	// ------------
	public String getDescription() {
		return getDescription(SEPARATOR_STRING);
	}
	// ------------
	public void setDescription(String description) {
		this.description = description;
	}
	// --------------------------------
	public double getMpy() {return mpy;	}
	public void setMpy(double mpy) {this.mpy = mpy;	}
	// --------------------------------
	public double getDiv() {return div;	}
	public void setDiv(double div) {this.div = div;	}
	// --------------------------------
    public boolean isEnabled() {return enabled;}
    public void setEnabled(boolean val) {enabled = val;}
    
	// --------------------------------------------------
	public long adjustValue(long val){
		long retVal = val;
		if(mpy != DEFAULT_NOOPVAL){
			retVal *= mpy;
		}
		if(div != DEFAULT_NOOPVAL && div != 0.0d){
			retVal /= div;
		}
		return retVal;
	}
	// --------------------------------------------------
	public float adjustValue(double val){
		double retVal = val;
		if(mpy != DEFAULT_NOOPVAL){
			retVal *= mpy;
		}
		if(div != DEFAULT_NOOPVAL && div != 0.0d){
			retVal /= div;
		}
		return (float)retVal;
	}
	// --------------------------------------------------
	public Channel getChannel(Object obj, int index){
		Channel retVal = null;
		if(isObjectIndexValid(index)){
	        if (obj instanceof Number) {
	            Number number = (Number) obj;
	            String descr = getDescription(" - ");
	            if (obj instanceof Integer || obj instanceof Long) {
	                long val = adjustValue(number.longValue());
	                retVal = new LongChannel(descr, getUnit(), val);
	            } else if (obj instanceof Float || obj instanceof Double) {
	                float val = adjustValue(number.floatValue());
	                retVal = new FloatChannel(descr, getUnit(), val);
	            }
	        } else if(obj instanceof CompositeDataSupport) {
	        	CompositeDataSupport cd = (CompositeDataSupport)obj;
	        	String key = getObject(++index);
	        	if(cd.containsKey(key)){
	        		obj = cd.get(key);
		        	retVal = getChannel(obj, index);
	        	} else {
	        		retVal = new LongChannel(getDescription(), getUnit(), 0);
	        		retVal.setWarning(1);
	                retVal.setMessage(toString()+": element["+key+"] not found ");
	        	}
	        }
		}
		return retVal;
	}
	// --------------------------------------------------
	public Channel getChannel(Object obj){
		return getChannel(obj, 0);
	}
	
}

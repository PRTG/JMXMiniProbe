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
package com.paessler.prtg.jmx.sensors.profile;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.digester3.Digester;
import org.snmp4j.smi.Null;

import com.paessler.prtg.jmx.Logger;
import com.paessler.prtg.jmx.channels.Channel;
import com.paessler.prtg.jmx.channels.Channel.Mode;
import com.paessler.prtg.jmx.channels.FloatChannel;
import com.paessler.prtg.jmx.channels.LongChannel;
import com.paessler.prtg.jmx.channels.Channel.Unit;

public abstract class Attribute<T extends Comparable<T>> implements Comparable<Attribute<T>> {
	public static double DEFAULT_NOOPVAL = 1.0d;
	public T	object;
    public String description = null, shortDescription = null;
    public String displayValue;
    public String unit;
    public String mode;
    public String customUnit;
	public Mode	eMode = Channel.Mode.INTEGER;
	public Unit eUnit = Unit.COUNT;
    public String comment;
    public double mpy = DEFAULT_NOOPVAL, div = DEFAULT_NOOPVAL;
    public boolean enabled = true;
	// ---------------------------------------------
	// ---------------------------------------------
	public Map<String,Object>	propMap = new HashMap<String,Object>();
	// ---------------------------------------------
	public Map<String,Object> getProperties() {return propMap;}
	public Object getProperty(String key) {return propMap.get(key);}
	public Object setProperty(String key, Object value) {return propMap.put(key, value);}
	public Object addProperty(PropertyEntry prope) 
	{return propMap.put(prope.getName(), prope.getValue());}
	// --------------------------------
    /** Define an upper error limit for the channel. If enabled, the sensor will be set to a "Down" status if this value is overrun and the LimitMode is activated. Note: Please provide the limit value in the unit of the base data type, just as used in the <Value> element of this section. While a sensor shows a "Down" status triggered by a limit, it will still receive data in its channels. The values defined with this element will be considered only on the first sensor scan, when the channel is newly created; they are ignored on all further sensor scans (and may be omitted). You can change this initial setting later in the Channel settings of the sensor.	Integer */
    public Integer	limitMaxError = null;	
    /** Define an upper warning limit for the channel. If enabled, the sensor will be set to a "Warning" status if this value is overrun and the LimitMode is activated. Note: Please provide the limit value in the unit of the base data type, just as used in the <Value> element of this section. The values defined with this element will be considered only on the first sensor scan, when the channel is newly created; they are ignored on all further sensor scans (and may be omitted). You can change this initial setting later in the Channel settings of the sensor.	Integer */
    public Integer	limitMaxWarning = null;
    /** Define a lower warning limit for the channel. If enabled, the sensor will be set to a "Warning" status if this value is undercut and the LimitMode is activated. Note: Please provide the limit value in the unit of the base data type, just as used in the <Value> element of this section. The values defined with this element will be considered only on the first sensor scan, when the channel is newly created; they are ignored on all further sensor scans (and may be omitted). You can change this initial setting later in the Channel settings of the sensor.	Integer */
    public Integer	limitMinWarning = null;
    /** Define a lower error limit for the channel. If enabled, the sensor will be set to a "Down" status if this value is undercut and the LimitMode is activated. Note: Please provide the limit value in the unit of the base data type, just as used in the <Value> element of this section. While a sensor shows a "Down" status triggered by a limit, it will still receive data in its channels. The values defined with this element will be considered only on the first sensor scan, when the channel is newly created; they are ignored on all further sensor scans (and may be omitted). You can change this initial setting later in the Channel settings of the sensor.	Integer */
    public Integer	limitMinError = null;
    /** Define an additional message. It will be added to the sensor's message when entering a "Down" status that is triggered by a limit. Note: The values defined with this element will be considered only on the first sensor scan, when the channel is newly created; they are ignored on all further sensor scans (and may be omitted). You can change this initial setting later in the Channel settings of the sensor.*/
    public String	limitErrorMsg = null;
    /** Define an additional message. It will be added to the sensor's message when entering a "Warning" status that is triggered by a limit. Note: The values defined with this element will be considered only on the first sensor scan, when the channel is newly created; they are ignored on all further sensor scans (and may be omitted). You can change this initial setting later in the Channel settings of the sensor. */
    public String	limitWarningMsg = null;
    /** Define if the limit settings defined above will be active. Default is 0 (no; limits inactive). If 0 is used the limits will be written to the sensor channel settings as predefined values, but limits will be disabled. Note: This setting will be considered only on the first sensor scan, when the channel is newly created; it is ignored on all further sensor scans (and may be omitted). You can change this initial setting later in the Channel settings of the sensor.	0 (= no)  1 (= yes) */
    public int limitMode = 0;
    
	// --------------------------------
    public Attribute()
    {
    	setUnitEnum(Unit.COUNT);
    	unit = getUnitEnum().toString();

    }

    public Attribute(Attribute<?> attr){
    	
    	setObject(toObjectType(attr.object));
    	setDescription(attr.description);
    	setShortDescription(attr.shortDescription);
    	setDisplayValue(attr.displayValue);
    	setMode(attr.mode);
    	setUnit(attr.unit);
    	setComment(attr.comment);
    	setMpy(attr.mpy);
    	setDiv(attr.div);
//    	attr.enabled = true;
    	setLimitMaxError(attr.getLimitMaxError());
    	setLimitMaxWarning(attr.getLimitMaxWarning());
    	setLimitMinWarning(attr.getLimitMinWarning());
    	setLimitMinError(attr.getLimitMinError());
    	setLimitErrorMsg(attr.getLimitErrorMsg());
    	setLimitWarningMsg(attr.getLimitWarningMsg());
    	setLimitMode(attr.getLimitMode());
    	
    }
    
	// --------------------------------
	public T getObject() 			{return object;}
	public void setObject(T object) {this.object = object;}
	public abstract T toObjectType(Object objectstr);
	// --------------------------------
	public String getDisplayValue() {return displayValue;}
	public void setDisplayValue(String displayValue) {	this.displayValue = displayValue;}
	// --------------------------------
	public String getUnit() {return unit;}
	public void setUnit(String unit) 	{this.unit = unit;	setUnitEnum(Channel.toUnit(unit));}
	public Unit getUnitEnum()	{return eUnit;	}
	public void setUnitEnum(Unit eunit)	{eUnit = eunit;	}
//	public void setUnit(String mUnit)	{this.unit = Channel.toUnit(mUnit);	}
	// --------------------------------
	public String getMode() {return mode;}
	public void setMode(String mode) 	{this.mode = mode;	setModeEnum(Channel.toMode(mode));}
	public Mode getModeEnum()	{return eMode;	}
	public void setModeEnum(Mode emode)	{eMode = emode;	}
//	public void setUnit(String mUnit)	{this.unit = Channel.toUnit(mUnit);	}
	// --------------------------------
    public String getCustomUnit() {	return customUnit;}
	public void setCustomUnit(String customunit) {this.customUnit = customunit;	}
	// --------------------------------
    public boolean isEnabled() {return enabled;}
    public void setEnabled(boolean val) {enabled = val;}
	// ----------------------------------
	public String getDescription() {
		return (description == null ? shortDescription :description);
	}
	public void setDescription(String description) {
		this.description = description;
	}
	// ----------------------------------
	public String getShortDescription() {return shortDescription;}
	public void setShortDescription(String shortDescription) {
		this.shortDescription = shortDescription;
	}
	// ----------------------------------
	public String getComment() {return comment;}
	public void setComment(String comment) {
		this.comment = comment;
	}
	// ----------------------------------
	public double getMpy() {return mpy;	}
	public void setMpy(double mpy) {this.mpy = mpy;	}
	// ----------------------------------
	public double getDiv() {return div;	}
	public void setDiv(double div) {this.div = div;	}
	// ----------------------------------
	public Integer getLimitMaxError() {return limitMaxError;}
	public void setLimitMaxError(Integer limitMaxError) {
		this.limitMaxError = limitMaxError;
	}
	// ----------------------------------
	public Integer getLimitMaxWarning() {return limitMaxWarning;}
	public void setLimitMaxWarning(Integer limitMaxWarning) {
		this.limitMaxWarning = limitMaxWarning;
	}
	// ----------------------------------
	public Integer getLimitMinWarning() {return limitMinWarning;}
	public void setLimitMinWarning(Integer limitMinWarning) {
		this.limitMinWarning = limitMinWarning;
	}
	// ----------------------------------
	public Integer getLimitMinError() {	return limitMinError;	}
	public void setLimitMinError(Integer limitMinError) {
		this.limitMinError = limitMinError;
	}
	// ----------------------------------
	public String getLimitErrorMsg() {	return limitErrorMsg;}
	public void setLimitErrorMsg(String limitErrorMsg) {
		this.limitErrorMsg = limitErrorMsg;
	}
	// ----------------------------------
	public String getLimitWarningMsg() {return limitWarningMsg;	}
	public void setLimitWarningMsg(String limitWarningMsg) {
		this.limitWarningMsg = limitWarningMsg;
	}
	// ----------------------------------
	public int getLimitMode() {return limitMode;	}
	public void setLimitMode(int limitMode) {
		this.limitMode = limitMode;
	}
	
	
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
	public Channel checkInvalid(Object obj){
		Channel retVal = null;
		if(obj == null){
			String tmp = obj.toString();
			retVal = new LongChannel(getDescription(), getUnit(), -1);
			String logString = "["+getObject()+"] "+getDescription()+" returned: "+obj;
			retVal.setMessage(logString);
			retVal.setWarning(1);
            Logger.log(logString );
		}
		return retVal;
	}
	// --------------------------------------------------
	private boolean haveSetChannels = false;
	public Channel getChannel(Object obj){
		Channel retVal = checkInvalid(obj);
		if(retVal != null)
		{	return retVal;}
		// ------------------------------
        if (obj instanceof Number) {
            Number number = (Number) obj;
            if (obj instanceof Integer || obj instanceof Long) {
                long val = adjustValue(number.longValue());
                retVal = new LongChannel(getDescription(), getUnit(), val);
            } else if (obj instanceof Float || obj instanceof Double) {
                float val = adjustValue(number.floatValue());
                retVal = new FloatChannel(getDescription(), getUnit(), val);
            }
        }
        if(getLimitMode() > 0 && !haveSetChannels){
        	retVal.setLimitMode(getLimitMode());
        	retVal.setLimitMaxError(getLimitMaxError());
        	retVal.setLimitMaxWarning(getLimitMaxWarning());
        	retVal.setLimitMinWarning(getLimitMinWarning());
        	retVal.setLimitMinError(getLimitMinError());
        	retVal.setLimitErrorMsg(getLimitErrorMsg());
        	retVal.setLimitWarningMsg(getLimitWarningMsg());
        	haveSetChannels = true;
        }
		if(retVal != null && retVal.getUnit() == Unit.CUSTOM){
			retVal.setCustomunit(getCustomUnit());
		}
        
		return retVal;
	}
	
	// ----------------------------------
	public static Digester describe4Digester(Digester digester, String base) throws Exception{
        digester.addBeanPropertySetter( base+"/object", "object" );
        digester.addBeanPropertySetter( base+"/displayvalue", "displayValue" );
        digester.addBeanPropertySetter( base+"/description", "description" );
        digester.addBeanPropertySetter( base+"/comment", "comment" );
        digester.addBeanPropertySetter( base+"/shortdescription", "shortDescription" );
        digester.addBeanPropertySetter( base+"/enabled", "enabled" );
        digester.addBeanPropertySetter( base+"/unit", "unit" );
        digester.addBeanPropertySetter( base+"/mode", "mode" );
        digester.addBeanPropertySetter( base+"/customunit", "customunit" );
        
//        digester.addCallMethod(base+"/unit", "setUnit", 1,  new String[]{"java.lang.String"});
        digester.addBeanPropertySetter( base+"/mpy", "mpy" );
        digester.addBeanPropertySetter( base+"/div", "div" );
        
        digester.addBeanPropertySetter( base+"/limitmaxerror", "limitMaxError" );	
        digester.addBeanPropertySetter( base+"/limitmaxwarning", "limitMaxWarning" );
        digester.addBeanPropertySetter( base+"/limitminwarning", "limitMinWarning" );
        digester.addBeanPropertySetter( base+"/limitminerror", "limitMinError" );
        digester.addBeanPropertySetter( base+"/limiterrormsg", "limitErrorMsg" );
        digester.addBeanPropertySetter( base+"/limitwarningmsg", "limitWarningMsg" );
        digester.addBeanPropertySetter( base+"/limitmode", "limitMode" );
//        digester.addBeanPropertySetter( base+"/limitmode", "limitMode" );
        // Properties
		String tmp = base + "/properties/property";
        digester.addObjectCreate( tmp , PropertyEntry.class );
        PropertyEntry.describe4Digester(digester, tmp);
        digester.addSetNext( tmp, "addProperty" );
        
        return digester; 
	}
	// -------------------------------------------
	public StringBuilder toXML(StringBuilder target, String prefix, String suffix){
		
		target.append(prefix);
		target.append("<attribute>");
		target.append(suffix);
		
		String tmp = prefix + "\t";
		ProfileFactory.addXMLElement(target, "object", getObject(), tmp, suffix);
		ProfileFactory.addXMLElement(target, "displayvalue", getDisplayValue(), tmp, suffix);
		ProfileFactory.addXMLElement(target, "description", getDescription(), tmp, suffix);
		ProfileFactory.addXMLElement(target, "shortdescription", getShortDescription(), tmp, suffix);
		ProfileFactory.addXMLElement(target, "comment", getComment(), tmp, suffix);
		ProfileFactory.addXMLElement(target, "unit", getUnit(), tmp, suffix);
		ProfileFactory.addXMLElement(target, "mode", getMode(), tmp, suffix);
		ProfileFactory.addXMLElement(target, "customunit", getCustomUnit(), tmp, suffix);
		ProfileFactory.addXMLElement(target, "enabled", isEnabled(), tmp, suffix);
		if(getMpy() != DEFAULT_NOOPVAL){
			ProfileFactory.addXMLElement(target, "mpy", getMpy(), tmp, suffix);
		}
		if(getDiv() != DEFAULT_NOOPVAL){
			ProfileFactory.addXMLElement(target, "div", getMpy(), tmp, suffix);
		}
		if(getLimitMaxError() != null)
			ProfileFactory.addXMLElement(target, "limitMaxError", getLimitMaxError(), tmp, suffix);	
		if(getLimitMaxWarning() != null)
			ProfileFactory.addXMLElement(target, "limitmaxwarning", getLimitMaxWarning(), tmp, suffix);
		if(getLimitMinWarning() != null)
			ProfileFactory.addXMLElement(target, "limitMinWarning", getLimitMinWarning(), tmp, suffix);
		if(getLimitMinError() != null)
			ProfileFactory.addXMLElement(target, "limitMinError", getLimitMinError(), tmp, suffix);
		if(getLimitErrorMsg() != null)
			ProfileFactory.addXMLElement(target, "limitErrorMsg", getLimitErrorMsg(), tmp, suffix);
		if(getLimitWarningMsg() != null)
			ProfileFactory.addXMLElement(target, "limitWarningMsg", getLimitWarningMsg(), tmp, suffix);
		if(getLimitMode() > 0)
			ProfileFactory.addXMLElement(target, "limitMode", getLimitMode(), tmp, suffix);

		// -------------------------------------
		ProfileFactory.toXML(target, getProperties(), tmp, suffix);
		// -------------------------------------
		
		target.append(prefix);
		target.append("</attribute>");
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
	public abstract int compareTo(Attribute<T> other);
}


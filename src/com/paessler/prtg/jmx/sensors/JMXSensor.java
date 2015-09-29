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

package com.paessler.prtg.jmx.sensors;

import com.google.gson.JsonObject;
import com.paessler.prtg.jmx.channels.Channel;
import com.paessler.prtg.jmx.definitions.SensorConstants;
import com.paessler.prtg.jmx.responses.DataError;
import com.paessler.prtg.jmx.responses.DataResponse;
import com.paessler.prtg.jmx.sensors.jmx.JMXAttribute;
import com.paessler.prtg.jmx.sensors.jmx.JMXBean;
import com.paessler.prtg.jmx.sensors.jmx.JMXSensorDefinition;
import com.paessler.prtg.jmx.sensors.jmx.JMXUtils;
import com.paessler.prtg.jmx.sensors.profile.Entry;
import com.paessler.prtg.jmx.sensors.profile.Profile;

import javax.management.MBeanServerConnection;
import javax.management.ObjectName;
import javax.management.remote.JMXConnector;

import java.util.ArrayList;
import java.util.List;

public class JMXSensor extends Sensor {
    // --------------------------------------------------------------------------------------------
	public JMXSensor(){
		super();
	}
	public JMXSensor(JMXSensor toclone){
		super(toclone);
		rmiString = toclone.rmiString;
		beanList.addAll(toclone.beanList);
	}
	public Sensor copy(){
		return new JMXSensor(this);
	}
	//----------------------------------------------------------------------
    protected String rmiString;
    protected List<JMXBean> beanList = new ArrayList<JMXBean>();
    
    public List<JMXBean> getBeanList() {return beanList;}
	public void addBeanList(JMXBean attrs) {this.beanList.add(attrs);}
//	public void setmBeanList(List<MBeanAttributes> mBeanList) {this.mBeanList = mBeanList;}
	// --------------------------------------------------------------------------------------------
    public String getRmiString() {	return rmiString;}
	public void setRmiString(String rmiString) {this.rmiString = rmiString;	}

	
    // --------------------------------------------------------------------------------------------
    public DataResponse addResponses(MBeanServerConnection mbsc, DataResponse target, ObjectName bean, List<JMXAttribute> attributeList) throws Exception{
        DataResponse retVal = target;
        Channel channel;
        for (JMXAttribute curr : attributeList) {
			if(curr.isEnabled()){
            String attributeName = curr.object;
            Object obj = null;  
            try {
				obj = mbsc.getAttribute(bean, attributeName);
			} catch (Exception e) {
                DataError error = new DataError(sensorid, bean.toString());
                error.setError("Exception");
                error.setMessage("Invalid attribute["+attributeName+"] for MBean["+bean.toString()+"] value type (Service URL: " + rmiString + ")");
                retVal = error;
			}
            if (obj != null) {
            	channel = curr.getChannel(obj);
            	if(channel != null){
            		retVal.addChannel(channel);
            	}
            }
		  } // if(curr.isEnabled())
        } // for
    	return retVal;
    }
    // --------------------------------------------------------------------------------------------
    public DataResponse addResponses(MBeanServerConnection mbsc, DataResponse target, JMXBean beans) throws Exception{
    	return addResponses(mbsc, target, beans.objectName, beans.attributeList);
    }

    // --------------------------------------------------------------------------------------------
    public DataResponse getResponses(MBeanServerConnection mbsc) throws Exception {
        DataResponse retVal = new DataResponse(sensorid, getSensorName());
        
        for(JMXBean curr: beanList){
        	addResponses(mbsc, retVal, curr);
        }
        return retVal;
    }

    // --------------------------------------------------------------------------------------------
    @Override
    public DataResponse go() {
        DataResponse response = null;
    	JMXConnector jmxc = null; 
        try {
//            MBeanServerConnection mbsc = getMBeanServer();
            MBeanServerConnection mbsc = JMXUtils.getJMXConnection(rmiString, username, password);
            if(mbsc == null){
                DataError error = new DataError(sensorid, getSensorName());
                error.setCode(-1);
                error.setError("Connection Error");
                error.setMessage("Failed to connect JMX Server (Service URL: " + rmiString + ")");
             	return error;
            }
            
            response = getResponses(mbsc);
            
        } catch (Exception e) {
            e.printStackTrace();
            DataError error = new DataError(sensorid, getSensorName());
            error.setCode(-1);
            error.setError("Exception");
            error.setMessage(e.getMessage() + " (Service URL: " + rmiString + ")");
            response = error;
        } finally {
            if (jmxc != null) {
                try {
                    jmxc.close();
                } catch (Exception e) {
                    // Ignore
                }
            }
        }

        return response;
    }
    // --------------------------------------------------------------------------------------------
    @Override
    public void loadFromJson(JsonObject json)  throws Exception{
    	rmiString = JMXUtils.RMI_STRING_LOCAL;
    	super.loadFromJson(json);
	    if (json.has(SensorConstants.RMISTRING)) {
	        this.rmiString = json.get(SensorConstants.RMISTRING).getAsString();
	    }
    }
    
    
	// ----------------------------------------------------------------------
    @Override
    public void loadFrom(Profile profile) {
    	super.loadFrom(profile);
    	setSensorName(profile.getName());
    	setKind(profile.getKind());
    	String tmptag = profile.getTag();
//    	String tmp2 = profile.getTags();
//    	if()
    	JMXSensorDefinition def =new JMXSensorDefinition(profile.getKind(), profile.getName(), profile.getDescription(), 
    			tmptag, profile.getHelp());
    	setDefinition(def);
    	Object prop = profile.getProperty(SensorConstants.RMISTRING);
    	if(prop != null){
    		getDefinition().setFieldDefaultValue(SensorConstants.RMISTRING, prop.toString());
    	}
    	JMXBean bean;
    	for(Entry curr : profile.getEntries()){
    		bean = new JMXBean(curr);
    		if(bean != null){
    			addBeanList(bean);
    		}
    	}
    }
}

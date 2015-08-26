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

package com.paessler.prtg.jmx;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;
import java.util.UUID;
import java.util.Vector;

import com.paessler.prtg.api.PRTGServer;
import com.paessler.prtg.jmx.definitions.SensorDefinition;
import com.paessler.prtg.jmx.responses.DataResponse;
import com.paessler.prtg.jmx.sensors.CustomJMXSensor;
import com.paessler.prtg.jmx.sensors.PingSensor;
import com.paessler.prtg.jmx.sensors.SNMPCustom;
import com.paessler.prtg.jmx.sensors.SNMPTraffic;
import com.paessler.prtg.jmx.sensors.Sensor;
import com.paessler.prtg.jmx.sensors.VMHealthSensor;
import com.paessler.prtg.jmx.sensors.dns.DNSSensor;
import com.paessler.prtg.jmx.sensors.http.HttpSensor;
import com.paessler.prtg.jmx.sensors.port.PortSensor;
import com.paessler.prtg.jmx.sensors.profile.Profile;
import com.paessler.prtg.jmx.sensors.profile.ProfileFactory;
import com.paessler.prtg.jmx.sensors.profile.Profiles;
import com.paessler.prtg.util.ClassUtility;

//import sun.org.mozilla.javascript.internal.ast.TryStatement;

/**
 * Keeps the GID, key, and protocol around
 *
 */
public class ProbeContext {
	public static final String HOST_STRING = "host";
	public static final String HOST_GUID = "guid";
	public static final String KEY_STRING = "key";
	public static final String SENSORPROFILE_STRING = "sensorprofile";
	public static final String DUMPJMXRMI_STRING = "dumpjmx.rmistring";
	public static final String DUMPJMXFNAME_STRING = "dumpjmx.filename";
	public static final String DUMPJMX_STRING = "dumpjmx";
	public static final String DEBUG_STRING = "debug";
	public static final String BASEINTERVAL_STRING = "baseinterval";
	public static final String PORT_STRING = "port";
	public static final String WEBPROTO_STRING = "webprotocol";
	public static final String WORKERTHREADS_STRING = "workerthreads";
	public static final String TS_ENABLED_STRING = "timestampenabled";
	
    // -------------------------------------------------------------------------------
	public String probeName = "PRTG JMX Probe";
	public String versionString = "15.2.0";
//	public String versionString = "14.3.1";
	public PRTGServer serverConnection;
    public int baseInterval = 60;
    public int debugLevel=0;
    public int dumpJMXMBeans = 0;
    public int timestampEnabled = 0;
	public String dumpJMXMBeansFileN = "JMXMBeanDump.xml";
    public String dumpRMIString		 = "localhost";
	public int workerThreads = 3;
    public String sensorprofile = "profiles/SensorProfiles.xml";
    public String homePath = ".";
    protected List<String> responseTargets = new ArrayList<String>();
    
    public static final String DEF_CONFIG_FILENAME = "prtgjmx.properties"; 
    // --------------------------------------
    public static String getConfigFile(String path){
        String retVal = System.getProperty("com.paessler.jmxprobe.config");
        if (retVal == null) {
        	if(path != null && path.endsWith("bin")){
        		path = (new File(path)).getParentFile().getAbsolutePath();
        	}
        	retVal = path+"/"+DEF_CONFIG_FILENAME;
            Logger.log("Missing configuration file path. Reverting to default("+retVal+")");
        }
        return retVal;
    }
    
    public static void initLog4J(){
    	org.apache.log4j.BasicConfigurator.configure();
    }
    // --------------------------------------
    public String getHomePath() {return homePath;} 
    public void setHomePath(String homePath) {this.homePath = homePath;} 
    // --------------------------------------
    public String getSensorProfile() {return sensorprofile;} 
    public void setSensorProfile(String sensorprofile) {this.sensorprofile = sensorprofile;} 
    // --------------------------------------
    public int getTimestampEnabled() {return timestampEnabled;}
	public void setTimestampEnabled(int timestampEnabled) 
	{	this.timestampEnabled = timestampEnabled;
		DataResponse.setTimestampEnabled(isTimestampEnabled());
	}
	public boolean isTimestampEnabled() {return (this.timestampEnabled > 0);}
    // --------------------------------------

	protected Map<String,Sensor>	sensorsNameMap = new HashMap<String,Sensor>();
    protected Map<String,Sensor> getSensorMap() { return sensorsNameMap;}
    protected Sensor getSensor(String name) {
    	return sensorsNameMap.get(name);
    }
    protected void addSensor(Sensor sensor) {
    	Sensor ret;
    	if(sensor != null){
    		ret = sensorsNameMap.put(sensor.getKind().toLowerCase(), sensor);
//    		ret = sensorsNameMap.put(sensor.getClass().getName().toLowerCase(), sensor);
    		if(ret != null){
            	System.out.println("Sensor Name clash: " +ret.getKind());
            	System.out.println("Added : " +sensor);
            	System.out.println("Lost  : " +ret);
    		}
    	}
    }
    // --------------------------------------
    public Properties settings;
    
    public Properties loadProperties(String configPath){
    	Properties retVal = settings = new Properties();
    	String tmp;
        try {
        	settings.load(new FileInputStream(configPath));
        } catch (IOException e) {
        	setErrorMessage("Invalid configuration file: " + configPath);
        }
        // ------------------------------------
        // BEGIN Required parameters
        if(!isErrorStatus()) {
        	tmp = settings.getProperty(HOST_STRING);
	        if (tmp == null) {
	        	setErrorMessage("Missing/Invalid "+HOST_STRING);
	        }
	        else{
	        	serverConnection.setHost(tmp);
	        }
        }
        if(!isErrorStatus()) {
        	tmp = settings.getProperty(ProbeContext.KEY_STRING);
	        if (tmp == null) {
	        	setErrorMessage("Missing/Invalid key");
	        }
	        else{
	        	serverConnection.setKey(tmp);
	        }
        }
        if(!isErrorStatus()) {
	        tmp = settings.getProperty(ProbeContext.HOST_GUID);
	        if (tmp == null) {
	        	tmp = UUID.randomUUID().toString();
	        	settings.put(ProbeContext.HOST_GUID, tmp);
	            try {
	            	settings.store(new FileOutputStream(configPath), "");
	            } catch (IOException e) {
		        	setErrorMessage("Could not write to " + configPath);
	            }
	        }
	        serverConnection.setGid(tmp);
        }
        // END Required parameters
        // ------------------------------------
        // BEGIN Optional
        if(!isErrorStatus()) {

        	tmp = settings.getProperty(ProbeContext.SENSORPROFILE_STRING);
	        if (tmp != null) {
		        setSensorProfile(tmp);
		        tmp = null;
	        }
	        
        	tmp = settings.getProperty(BASEINTERVAL_STRING);
	        if (tmp != null) {
		        setBaseInterval(tmp);
		        tmp = null;
	        }

        	tmp = settings.getProperty(PORT_STRING);
	        if (tmp != null) {
	        	serverConnection.setPort(tmp);
		        tmp = null;
	        }
	        // ------
        	tmp = settings.getProperty(WEBPROTO_STRING);
	        if (tmp != null) {
	        	serverConnection.setWebprotocol(tmp);
		        tmp = null;
	        }
	        // ------
        	tmp = settings.getProperty(WORKERTHREADS_STRING);
	        if (tmp != null) {
	        	setWorkerThreads(tmp);
		        tmp = null;
	        }
	        // ------
        	tmp = settings.getProperty(DEBUG_STRING);
	        if (tmp != null) {
	        	setDebugLevel(tmp);
	        	serverConnection.setDebugLevel(tmp);
		        tmp = null;
	        }
	        
        	tmp = settings.getProperty(DUMPJMX_STRING);
	        if (tmp != null) {
	        	setDumpJMXMBeans(Integer.parseInt(tmp));
	        }
        	tmp = settings.getProperty(DUMPJMXFNAME_STRING);
	        if (tmp != null) {
	        	setDumpJMXMBeansFileN(tmp);
	        }
        	tmp = settings.getProperty(DUMPJMXRMI_STRING);
	        if (tmp != null) {
	        	setDumpRMIString(tmp);
	        }
	        
        	tmp = settings.getProperty(TS_ENABLED_STRING);
	        if (tmp != null) {
	        	setTimestampEnabled(Integer.parseInt(tmp));
	        }
        }
        // END Optional parameters
        // ------------------------------------
        loadSensors();
    	return retVal;
    }
    public int getDumpJMXMBeans() {
		return dumpJMXMBeans;
	}

	// -------------------------------------------------------------------------------
	public void setDumpJMXMBeans(int dumpJMXMBeans) {this.dumpJMXMBeans = dumpJMXMBeans;}
	public String getDumpJMXMBeansFileN() {	return dumpJMXMBeansFileN;}
	public void setDumpJMXMBeansFileN(String dumpJMXMBeansFileN) {
		this.dumpJMXMBeansFileN = dumpJMXMBeansFileN;
	}
	// -------------------------------------------------------------------------------
    public String getDumpRMIString() {return dumpRMIString;}
	public void setDumpRMIString(String dumpRMIString) {this.dumpRMIString = dumpRMIString;	}

	// -------------------------------------------------------------------------------
	public String getProbeName() {return probeName;}
	public void setProbeName(String probeName) {this.probeName = probeName;	}
	// -------------------------------------------------------------------------------
	public String getVersionString() {return versionString;}
	public void setVersionString(String versionString) {this.versionString = versionString;	}
	// -------------------------------------------------------------------------------
    public int  getWorkerThreads()	{return workerThreads;}
    // ----
    public void  setWorkerThreads(int val)	{workerThreads = val;}
    public void  setWorkerThreads(String val)	{
    	if(val != null){
    		setWorkerThreads(Integer.parseInt(val));
    	}
    }
    // -------------------------------------------------------------------------------
    public int getBaseInterval(){	return baseInterval;}
    public void setBaseInterval(int level){baseInterval = level;}
    public void setBaseInterval(String level){
    	int ilevel = 0;
    	try{
    		ilevel = Integer.parseInt(level);
    		setBaseInterval(ilevel);
    	}
    	catch(Exception e)
    	{} // Ignore and default
   	}
    // -------------------------------------------------------------------------------
    public int getDebugLevel(){	return debugLevel;}
    public void setDebugLevel(int level){debugLevel = level;}
    public void setDebugLevel(String level){
    	int ilevel = 0;
    	try{
    		ilevel = Integer.parseInt(level);
        	setDebugLevel(ilevel);
    	}
    	catch(Exception e)
    	{} // Ignore and default
   	}
    // -------------------------------------------------------------------------------
    // -------------------------------------------------------------------------------
    String errorMessage = null;
    public boolean isErrorStatus(){
    	return errorMessage != null;
    }
    public String getErrorMessage(){
    	return errorMessage;
    }
    protected void setErrorMessage(String msg){
    	errorMessage = msg;
    }
    
    // -------------------------------------------------------------------------------
    public ProbeContext() {
    	serverConnection = new PRTGServer();
    }

	// -------------------------------------------------------------------------------
    public static ProbeContext getProbeContext(String configPath){
    	ProbeContext retVal = new ProbeContext();
    	if(retVal != null && configPath != null){
	    	retVal.setHomePath((new File(configPath)).getParent());
	    	retVal.loadProperties(configPath);
    	}
        return retVal;
    }
    // -------------------------------------------------------------------------------
    public String getURLHost(){
    	String retVal = serverConnection.getURLHost();
    	return retVal;
    }
    // -------------------------------------------------------------------------------
    public String getURLPrefix(String path){
//    	URL url = new URL(getWebprotocol(), getURLHost(), path);
    	String retVal = serverConnection.getURLPrefix(path);
    	return retVal;
    }
    // -------------------------------------------------------------------------------
    public String encode(String string) {
        try {
            String ret = URLEncoder.encode(string, "utf-8");
            return ret;
        } catch (UnsupportedEncodingException e) {
            return string;
        }
    }
    
    public Sensor createSensor(Profile sensorprofile){
    	Sensor retVal = null;
    	String base = sensorprofile.getBaseclass();
    	try {
			retVal = sensorFactory(base);
			retVal.loadFrom(sensorprofile);
		} catch (Exception e) {
        	setErrorMessage("createSensor Profile["+sensorprofile.getName()+"]: invalid base class["+base+"]");
        	e.printStackTrace();
		}
    	return retVal;
    }
    // --------------------------------------
    public  void loadSensors() {
    	// Loading Base Sensors
        addSensor(new CustomJMXSensor());
        addSensor(new VMHealthSensor());
        addSensor(new PingSensor());
//	    addSensor(new SNMPSensor());
        addSensor(new SNMPTraffic());
        addSensor(new SNMPCustom());
        addSensor(new PortSensor());
        addSensor(new DNSSensor());
        addSensor(new HttpSensor());
    	// Loading Sensors Profiles
        String profilefname = getSensorProfile();
        Profiles sesorprofiles = null;
        if(profilefname != null){
        	sesorprofiles = ProfileFactory.loadProfiles(this, getHomePath()+"/"+profilefname);
        }
        if(sesorprofiles != null){
        	Sensor tmpsens;
        	for(Profile curr:sesorprofiles.getProfiles()){
        		tmpsens = createSensor(curr);
        		if(tmpsens != null){
        			addSensor(tmpsens);
        		}
        	}
        }
    }
    // --------------------------------------
    public  List<SensorDefinition> getSensorDefinitionsListHack() {
        Map<String,Sensor> map = getSensorMap();
//        List<SensorDefinition> retVal = new Vector<SensorDefinition>(map.size());
        List<SensorDefinition> retVal = new Vector<SensorDefinition>();
        for(Sensor curr: map.values()){
        	retVal.add(curr.getDefinition());
        }
	    return retVal;
    }

    // --------------------------------------
    public Sensor sensorFactory(String name) throws Exception{
    	Sensor retVal = null;
    	if(name != null && !name.isEmpty()) {
    		Sensor sensor = getSensor(name);
    		if(sensor != null) {
	//	    	Class<?> clazz = sensor.getClass();
//		    	retVal = (Sensor) ClassUtility.newInstance(clazz, new Object[0]);
				retVal = (Sensor) sensor.copy();   	
    		}
			if(retVal == null){
				Class<?> clazz = ClassUtility.classForName(name);
				if(clazz != null ){
					try {
						Object o = ClassUtility.newInstance(clazz, new Object[0]);
						if(o instanceof Sensor){
							retVal = (Sensor)o; 
						}
					} catch (Exception e) {
						// Do nothing for failure
					}
				}
			}
    	}
    	return retVal;
    }
    // ---------------------------------------------------------------------
/*    
    protected boolean loadSensors() {
    	boolean retVal = false;
    	Class<?> clazz = null;
    	Sensor sensor = null;
       	String tmp = settings.getProperty("sensors");
       	if(tmp != null){
       		String[] sensorNames = tmp.split(",");
       		for(String curr: sensorNames) {
       			tmp = settings.getProperty(curr+".class");
       			if(tmp != null) {
       				clazz = ClassUtility.classForName(tmp);
                	sensorsNameMap.put(curr, clazz);
       			}
       		}
       	}
       	return retVal;
    }
*/    
}

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

package com.paessler.prtg.jmx.tasks;

import com.google.gson.*;
import com.paessler.prtg.jmx.Logger;
import com.paessler.prtg.jmx.ProbeContext;
import com.paessler.prtg.jmx.definitions.SensorDefinition;
import com.paessler.prtg.jmx.mbean.PRTGInterface;
import com.paessler.prtg.jmx.network.Announcement;
import com.paessler.prtg.jmx.network.NetworkWrapper;
import com.paessler.prtg.jmx.network.Tasks;
import com.paessler.prtg.jmx.responses.DataResponse;
import com.paessler.prtg.jmx.sensors.Sensor;
import com.paessler.prtg.util.TimingUtility;

import javax.servlet.ServletContext;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TimerTask;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class TaskFetcherTask extends TimerTask {
	public static final String EMPTY_TASKLIST = "[]";
	
    final private ServletContext mServletContext;
    final private ProbeContext mProbeContext;

    private ExecutorService executor = null;
    private boolean 		myExecutor = false;

    private PRTGInterface prtgMBean = null;
    // -----------------------------------------------------
    protected List<DataResponse> resultList = null;
	// ----------------------------
    public List<DataResponse> getResultList() {	return resultList;	}
	// ----------------------------
	protected void setResultList(List<DataResponse> resultList) {
		this.resultList = resultList;
	}
	
    // ----------------------------------------------------------------------------------------
    // ----------------------------------------------------------------------------------------
    // ----------------------------------------------------------------------------------------
	private boolean reRunAnnouncement = true;
	public void setRerunAnnouncement(boolean val) {reRunAnnouncement = val;}
	public boolean getReRunAnnouncement() {return reRunAnnouncement;}
    public boolean sendAnnouncement(ProbeContext context){
    	boolean retVal = false;
        try {
			List<SensorDefinition> definitions = context.getSensorDefinitionsListHack();

			Announcement announcement = new Announcement(context.getProbeName(), context.getVersionString(), context.getBaseInterval(), definitions);
			String url = announcement.buildUrl(context);
			try {
			    String postBody = announcement.toString();
			    if(context.getDebugLevel() > 0){
			    	Logger.log("Sending: "+url+" " + postBody);
			    }
			    NetworkWrapper.post(url, postBody);
			    retVal =  true;
			    reRunAnnouncement = false;
			} catch (Exception e) {
			    Logger.log(e.getLocalizedMessage());
			}

		} catch (Throwable e) {
	        Logger.log("Cought Throwable in TaskFetcherTask.run(). "+e.getMessage());
			e.printStackTrace();
		}
        return retVal;
    }
    // ----------------------------------------------------------------------------------------
    // ----------------------------------------------------------------------------------------
    // ----------------------------------------------------------------------------------------
	public synchronized void addDataResponse(Sensor sensor, DataResponse response){
		List<DataResponse>  list = getResultList();
		if(list != null){
			list.add(response);
		}
	}
    // -----------------------------------------------------
    public TaskFetcherTask(ServletContext context, ProbeContext probeContext, ExecutorService execsrvc) {
        prtgMBean = PRTGInterface.getAndRegisterBean(PRTGInterface.getDefaultBeanName());
        mServletContext = context;
        mProbeContext = probeContext;
        if(execsrvc != null){
        	executor = execsrvc;
        } else {
//          executor = Executors.newFixedThreadPool(mProbeContext.workerThreads);
        	executor = Executors.newFixedThreadPool(2);
        	myExecutor = true;
        }
        setResultList(new Vector<DataResponse>());
    }
    public void cleanup(){
    	if(prtgMBean != null){
    		PRTGInterface.unregisterBean(prtgMBean);
    	}
    	
    }
    // -----------------------------------------------------
    public void finalizer(){
    	cleanup();
    	if(myExecutor && executor != null){
    		executor.shutdown();
    	}
    }

// -----------------------------------------------------
    private String currJson = null;
    private Map<Integer, Sensor> sensorMap = null;
    private Map<Integer, Sensor> getSensorMap(){ return sensorMap;}
    protected boolean addSensor(Sensor sensor){
    	boolean retVal = false;
    	if(sensorMap != null){
    	Integer key = sensor.getSensorid();
    	Sensor ret = sensorMap.put(key, sensor);
    	retVal = true;
    	}
    	return retVal;
    }
    // -----------------------------------------------------
    protected Sensor sensorFactory(JsonObject json) {
    	Sensor retVal = null; 
    	String kind = json.get("kind").getAsString();
        int id = json.get("sensorid").getAsInt();
        try {
        	retVal = mProbeContext.sensorFactory(kind);
            if(retVal != null){
            	retVal.loadFromJson(json);
            }
        } catch (Exception e) {
            Logger.log("Error creating sensor from JSON: " +json + " " + e.getLocalizedMessage());
            if(mProbeContext.getDebugLevel() > 0){
           	 e.printStackTrace();
            }
        }
    	return retVal;
    }
    // -----------------------------------------------------
    private Map<Integer, Sensor> getSensors(String json){
    	if(currJson != null && sensorMap != null && json.equals(currJson)){
    		return sensorMap;
    	}
        JsonParser parser = new JsonParser();
        JsonElement top = parser.parse(json);
        JsonArray tasksArray = top.getAsJsonArray();
        Sensor sensor = null;
        int len = tasksArray.size();
    	Map<Integer, Sensor> oldSensorMap = sensorMap; 
    	sensorMap = new ConcurrentHashMap<Integer, Sensor>(len);
//    	sensorMap = new ConcurrentSkipListMap<Integer, Sensor>();
        JsonObject json4Sensor = null;
//        String kind = null;
        int id = -1;
        for (int i = 0; i < len; i++) {
            json4Sensor = tasksArray.get(i).getAsJsonObject();
            id = json4Sensor.get("sensorid").getAsInt();
            sensor = null;
            if(oldSensorMap != null){
            	sensor = oldSensorMap.get(id);
            }
        	if(sensor == null || !sensor.isSame(json4Sensor)){
                sensor = sensorFactory(json4Sensor);
        	}
        	if(sensor != null ){
        		addSensor(sensor);
        	}
        }
        currJson = json;
        return sensorMap;
    }
    // --------------------------------------------------
    private Map<Integer, Sensor> getSensors(){
    	Map<Integer, Sensor> retVal = null;
        String url = Tasks.buildUrl(mProbeContext);
        String json = null;
        boolean nocontact = false;
        try {
            json = NetworkWrapper.fetch(url);
        } catch (IOException e) {
        	nocontact = true;
       		setRerunAnnouncement(true);
            Logger.log(e.getLocalizedMessage());
            if(mProbeContext.getDebugLevel() > 0){
           	 e.printStackTrace();
            }
        }

        // If we lost contact with the server, probe anyways and cache results.
        if(nocontact){
	        retVal = getSensorMap();
        } else{
	        if (json != null && !json.equals(EMPTY_TASKLIST))
	        {
		        if(mProbeContext.getDebugLevel() > 0){
		            Logger.log("Debug task: " + url+"  "+json);
		        }
		        retVal = getSensors(json);
	        }
        }                	prtgMBean.addQueryCount(1);

        return retVal;
    }
    
    // -----------------------------------------------------
    protected void sendResponse(List<DataResponse> responseList){
        String dataUrl =  mProbeContext.getURLPrefix("/probe/data");
        if (responseList.size() > 0) {
            Gson gson = new Gson();
            String dataJson = gson.toJson(responseList);
            if(mProbeContext.getDebugLevel() > 0){
                Logger.log("Debug posting sensor data: " + dataUrl+"  "+dataJson);
            }
            try {
                NetworkWrapper.post(dataUrl, dataJson);
                responseList.clear();
            } catch (Exception e) {
                Logger.log("Error sending data: " + e.getLocalizedMessage());
	             if(mProbeContext.getDebugLevel() > 1){
	            	 e.printStackTrace();
	             }
            }
        }
        // Until we have timestamp, no caching
        if(!mProbeContext.isTimestampEnabled())
            responseList.clear();
        	
    }
    // -----------------------------------------------------
//    @Override
    public void runMT() {
    	TimingUtility sensorexectimer = new TimingUtility();
    	TimingUtility sensorcreationtimer = new TimingUtility();
    	TimingUtility sensoruploadtimer = new TimingUtility();
    	boolean updatestats = false;
    	sensorcreationtimer.start();
    	Map<Integer, Sensor> sensors = getSensors();
    	sensorcreationtimer.stop();
    	if(sensors != null && sensors.size() > 0){
    		prtgMBean.setSensorCount(getSensorMap().size());
    		// Run the probe
            List<Future<?>> futures = new ArrayList<Future<?>> (sensors.size());
            sensorexectimer.start();
            for (Sensor sensor: sensors.values()) {
            	sensor.setControllerTask(this);
            	futures.add( executor.submit(sensor));
            }
            for (Future<?> f : futures) {
    	        try {
//    	        		if(!f.isDone())
     	        		if(f != null)
    	        			f.get(); //blocks until the runnable completes
     	        		
    	         } catch (InterruptedException ie) {
    	             Logger.log("InterruptedException: on["+f+"] " + ie.getLocalizedMessage());
    	         } catch (ExecutionException ee) {
    	             Logger.log("ExecutionException: on["+f+"] " + ee.getLocalizedMessage());
    	             if(mProbeContext.getDebugLevel() > 1){
    	            	 ee.printStackTrace();
    	             }
    	         }
            }
            updatestats = true;
            sensorexectimer.stop();
            Logger.log("Sensor Creation:" + sensorcreationtimer +", Data Collection : " + sensorexectimer);
        	prtgMBean.addQueryCount(1);
        	prtgMBean.addSenorCreationTime(sensorcreationtimer.getElapsed());
        	prtgMBean.addExecutionTime(sensorexectimer.getElapsed());
    	} else{
            Logger.log("Sensor Creation Yeilded nothing to do[empty list of sensors]");
            sensorexectimer.stop();
    	}
	    try{
	    	sensoruploadtimer.start();
	    	sendResponse(getResultList());
	    	sensoruploadtimer.stop();
	       } catch (Exception e) {
	            Logger.log("Error sending data: " + e.getLocalizedMessage());
	             if(mProbeContext.getDebugLevel() > 1){
	            	 e.printStackTrace();
	             }
	        }

        if(updatestats){
            Logger.log("Sensor Creation:" + sensorcreationtimer +", Data Collection : " + sensorexectimer);
        	prtgMBean.addQueryCount(1);
        	prtgMBean.addSenorCreationTime(sensorcreationtimer.getElapsed());
        	prtgMBean.addExecutionTime(sensorexectimer.getElapsed());
        	prtgMBean.addUploadTime(sensoruploadtimer.getElapsed());
        }

    }
 // -----------------------------------------------------
//    @Override
    public void runST() {
    	Map<Integer, Sensor> sensors = getSensors();
        setResultList(new ArrayList<DataResponse>());
        for (Sensor sensor: sensors.values()) {
                DataResponse response = sensor.go();
                if (response != null)
                	addDataResponse(sensor, response);
        }
    	try{
            sendResponse(getResultList());
        } catch (Exception e) {
            Logger.log("Error sending data: " + e.getLocalizedMessage());
            if(mProbeContext.getDebugLevel() > 1){
           	 e.printStackTrace();
            }
        }


    }
    // -----------------------------------------------------
    @Override
    public void run() {
    	try{
    		if(getReRunAnnouncement()){
    			if(!sendAnnouncement(mProbeContext)){
    				// Announcement failed, retry next iteration
    				return;
    			}
    		}
    		runMT();
    	}
    	catch(Error err){
            Logger.log("Error run: " + err.getLocalizedMessage());
            if(mProbeContext.getDebugLevel() > 0){
            	err.printStackTrace();
            }
    	}
    	catch(Throwable t){
            Logger.log("Throwable in run: " + t.getLocalizedMessage());
            if(mProbeContext.getDebugLevel() > 0){
            	t.printStackTrace();
            }
    	}
    }
}

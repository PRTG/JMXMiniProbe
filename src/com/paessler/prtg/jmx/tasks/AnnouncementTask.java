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

import com.paessler.prtg.jmx.Logger;
import com.paessler.prtg.jmx.ProbeContext;
import com.paessler.prtg.jmx.sensors.jmx.JMXUtils;

import javax.servlet.ServletContext;

import java.util.*;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class AnnouncementTask extends TimerTask {
    private final static int ANNOUNCEMENT_BACKOFF_IN_SECONDS = 10;

    private int mAnnouncementTries = 1;
    private final ScheduledExecutorService mScheduledExecutorService;
    private final ServletContext mServletContext;
    private final ProbeContext mProbeContext;

    public AnnouncementTask(ProbeContext probeContext, ScheduledExecutorService scheduledExecutorService) {
        this(probeContext, null, scheduledExecutorService);
    }

    public AnnouncementTask(ProbeContext probeContext, ServletContext context, ScheduledExecutorService scheduledExecutorService) {
        mScheduledExecutorService = scheduledExecutorService;
        mServletContext = context;
        mProbeContext = probeContext;
    }
/*
    public boolean sendAnnouncement(){
    	boolean retVal = false;
        try {
			List<SensorDefinition> definitions = mProbeContext.getSensorDefinitionsListHack();

			Announcement announcement = new Announcement("PRTG JMX Probe", "14.3.1", mProbeContext.getBaseInterval(), definitions);
			String url = announcement.buildUrl(mProbeContext);
			try {
			    String postBody = announcement.toString();
			    Logger.log("Sending: " + postBody);
			    NetworkWrapper.post(url, postBody);
			    retVal =  true;
			} catch (Exception e) {
			    Logger.log(e.getLocalizedMessage());
			    if (mAnnouncementTries <= 10) {
			        int seconds =  ANNOUNCEMENT_BACKOFF_IN_SECONDS * mAnnouncementTries;
			        Logger.log(String.format("Trying again in %d seconds", seconds));
			        mScheduledExecutorService.schedule(this, seconds, TimeUnit.SECONDS);
			        mAnnouncementTries += 1;
			    } else {
			        Logger.log("Could not send announcement. I'm giving up now.");
			    }
			}

		} catch (Throwable e) {
	        Logger.log("Cought Throwable in AnnouncementTask.run(). "+e.getMessage());
			e.printStackTrace();
		}
        return retVal;
    }
*/    
    
    @Override
    public void run() {
        Logger.log("Announcement task running");
        TaskFetcherTask taskfetcher = new TaskFetcherTask(mServletContext, mProbeContext, mScheduledExecutorService);
    	if(mProbeContext.getDumpJMXMBeans() > 0){
    		try {
				JMXUtils.dumpJMXToFile(mProbeContext.getDumpJMXMBeansFileN(), mProbeContext.getDumpRMIString(), null, null, false);
			} catch (Throwable e) {
		        Logger.log("Dump of JMX MBeans["+mProbeContext.getDumpJMXMBeansFileN()+"] failed. "+e.getMessage());
				e.printStackTrace();
			}
    		mProbeContext.setDumpJMXMBeans(0);
    	}
        try {
        	
        	if(taskfetcher.sendAnnouncement(mProbeContext)){
//        		mScheduledExecutorService.scheduleWithFixedDelay(new TaskFetcherTask(mServletContext, mProbeContext, mScheduledExecutorService), (int)(mProbeContext.getBaseInterval() / 2), mProbeContext.getBaseInterval(), TimeUnit.SECONDS);
        		mScheduledExecutorService.scheduleWithFixedDelay(taskfetcher, 15, mProbeContext.getBaseInterval(), TimeUnit.SECONDS);
        		taskfetcher = null;
        	} else {
    		    if (mAnnouncementTries <= 10) {
    		        int seconds =  ANNOUNCEMENT_BACKOFF_IN_SECONDS * mAnnouncementTries;
    		        Logger.log(String.format("Trying again in %d seconds", seconds));
    		        mScheduledExecutorService.schedule(this, seconds, TimeUnit.SECONDS);
    		        mAnnouncementTries += 1;
    		        taskfetcher = null;
    		    } else {
    		        Logger.log("Could not send announcement. I'm giving up now.");
    		    }
        	}
        	
		} catch (Throwable e) {
	        Logger.log("Cought Throwable in AnnouncementTask.run(). "+e.getMessage());
			e.printStackTrace();
		}
        if(taskfetcher != null){
        	taskfetcher.cleanup();
        }
    }
}

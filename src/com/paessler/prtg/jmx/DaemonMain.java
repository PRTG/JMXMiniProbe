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

import com.paessler.prtg.jmx.tasks.AnnouncementTask;
import com.paessler.prtg.jmx.tasks.TaskFetcherTask;
import com.paessler.prtg.util.SystemUtility;

import org.apache.commons.cli.*;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class DaemonMain {

    private static void showHelp(Options options) {
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("PRTG JMX Probe", options);
        System.exit(1);
    }

    public static void main(String[] args) {
        Options options = new Options();
        options.addOption("c", true, "Config file");
        options.addOption("h", true, "Show help and exit");
        String configFile = null;
        CommandLineParser parser = new PosixParser();
        CommandLine cmdLine;
        String configPath = null;
        try {
            cmdLine = parser.parse(options, args);
            if (cmdLine.hasOption("h"))
                showHelp(options);
            if (cmdLine.hasOption("c")) {
            	configPath = cmdLine.getOptionValue("c");
            }
            if (configFile == null) {
                configFile = ProbeContext.getConfigFile(System.getProperty("user.dir"));
            }
        } catch (Exception e) {
            showHelp(options);
        }
        try{
	        ProbeContext probeContext = ProbeContext.getProbeContext(configFile);
	        
	        if(probeContext.getDebugLevel() > 0){
	        	Logger.log("The current OS String is:\'"+
	        				System.getProperty(SystemUtility.SYS_PROPERTY_OS_NAME)+"\" version:"+
	        				System.getProperty(SystemUtility.SYS_PROPERTY_OS_VER));
	        }
	        
			if(!probeContext.isErrorStatus()){
				ScheduledExecutorService scheduledExecutorService = null; 
				if(probeContext.getWorkerThreads() > 1){
					scheduledExecutorService = Executors.newScheduledThreadPool(probeContext.getWorkerThreads());
				} else {
					scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
				}
		//        ScheduledExecutorService 
//		        scheduledExecutorService.schedule(new AnnouncementTask(probeContext, scheduledExecutorService), 1, TimeUnit.SECONDS);
		        scheduledExecutorService.scheduleWithFixedDelay(new TaskFetcherTask(null, probeContext, scheduledExecutorService), 10, probeContext.getBaseInterval(), TimeUnit.SECONDS);
			} else {
				System.out.println(probeContext.getErrorMessage()+" Failiure to launch->"+probeContext.getErrorMessage());
				Logger.log(probeContext.getErrorMessage());
			}
        }
        catch(Throwable e){
            Logger.log(e.getMessage());
            e.printStackTrace();
        }
    }

    private static void errorOut(String message) {
        System.out.println(message);
        System.exit(1);
    }
}


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

import javax.servlet.*;

import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ServletMain extends GenericServlet implements ServletContextListener {
    /**
	 * 
	 */
	private static final long serialVersionUID = -7416678098747609639L;
	private ScheduledExecutorService mScheduledExecutorService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
    }

    public void contextInitialized(ServletContextEvent servletContextEvent) {
        ServletContext context = servletContextEvent.getServletContext();
    	Logger.initLogger(context);
        String defCondpath = context.getRealPath("/"+ProbeContext.DEF_CONFIG_FILENAME);
        String configFile = System.getProperty("com.paessler.jmxprobe.config");
        if (configFile == null) {
        	configFile = defCondpath;
            Logger.log("Missing configuration file path. Reverting to default("+configFile+")");
        }
        Logger.log("Reading configuration from " + configFile);
		ProbeContext probeContext = ProbeContext.getProbeContext(configFile);
		
		if(!probeContext.isErrorStatus()){
//        mScheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
			if(probeContext.getWorkerThreads() > 1){
				mScheduledExecutorService = Executors.newScheduledThreadPool(probeContext.getWorkerThreads());
			} else {
				mScheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
			}
//			mScheduledExecutorService.schedule(new AnnouncementTask(probeContext, context, mScheduledExecutorService), 1, TimeUnit.SECONDS);
			mScheduledExecutorService.scheduleWithFixedDelay(new TaskFetcherTask(context, probeContext, mScheduledExecutorService), 10, probeContext.getBaseInterval(), TimeUnit.SECONDS);
		} else {
			System.out.println(probeContext.getErrorMessage()+" Failiure to launch-> [Default Config path="+defCondpath+"]");
            Logger.log(probeContext.getErrorMessage()+" [Default Config path="+defCondpath+"]");
		}
    }

    public void contextDestroyed(ServletContextEvent servletContextEvent) {
    	if(mScheduledExecutorService != null){
    		mScheduledExecutorService.shutdownNow();
    	}
    }

    @Override
    public void service(ServletRequest servletRequest, ServletResponse servletResponse) throws ServletException, IOException {
    }

}

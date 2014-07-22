
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

import javax.servlet.*;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ServletMain extends GenericServlet implements ServletContextListener {
    private ScheduledExecutorService mScheduledExecutorService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
    }

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        ServletContext context = servletContextEvent.getServletContext();
        String key = null;
        String prtgServer = null;
        String configFile = System.getProperty("com.paessler.jmxprobe.config");
        if (configFile == null) {
            Logger.log(context, "Missing configuration file path");
            return;
        }
        Logger.log(context, "Reading configuration from " + configFile);

        Properties settings = new Properties();
        try {
            settings.load(new FileInputStream(configFile));
            key = settings.getProperty("key");
            prtgServer = settings.getProperty("host");
        } catch (IOException e) {
            // Ignore it
        }

        String guid = settings.getProperty("guid");
        if (guid == null) {
            guid = UUID.randomUUID().toString();
            settings.put("guid", guid);
            try {
                settings.store(new FileOutputStream(configFile), "");
            } catch (IOException e) {
                Logger.log(context, "Could not write to the config file");
                return;
            }
        }

        if (guid == null) {
            Logger.log(context, "I need a GUID to continue");
            return;
        }

        if (key == null) {
            Logger.log(context, "I need a key to continue");
            return;
        }

        if (prtgServer == null) {
            Logger.log(context, "I need a server to connect to");
        }

        ProbeContext probeContext = new ProbeContext(prtgServer, guid, key);
        mScheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        mScheduledExecutorService.schedule(new AnnouncementTask(probeContext, context, mScheduledExecutorService), 1, TimeUnit.SECONDS);
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        mScheduledExecutorService.shutdownNow();
    }

    @Override
    public void service(ServletRequest servletRequest, ServletResponse servletResponse) throws ServletException, IOException {
    }

}

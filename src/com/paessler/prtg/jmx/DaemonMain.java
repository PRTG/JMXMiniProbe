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
import org.apache.commons.cli.*;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.UUID;
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
        if (args.length == 0) {
            showHelp(options);
        }

        CommandLineParser parser = new PosixParser();
        CommandLine cmdLine;
        String host = null, key = null, configPath = null, guid = null;
        try {
            cmdLine = parser.parse(options, args);
            if (!cmdLine.hasOption("c"))
                showHelp(options);
            configPath = cmdLine.getOptionValue("c");
        } catch (Exception e) {
            showHelp(options);
        }

        if (configPath == null || configPath.length() == 0)
            showHelp(options);


        Properties settings = new Properties();
        try {
            settings.load(new FileInputStream(configPath));
        } catch (IOException e) {
            errorOut("Invalid configuration file: " + configPath);
        }
        host = settings.getProperty("host");
        key = settings.getProperty("key");
        if (host == null) {
            errorOut("Invalid host");
        }

        if (key == null) {
            errorOut("Invalid key");
        }
        guid = settings.getProperty("guid");

        if (guid == null) {
            guid = UUID.randomUUID().toString();
            settings.put("guid", guid);
            try {
                settings.store(new FileOutputStream(configPath), "");
            } catch (IOException e) {
                errorOut("Could not write to " + configPath);
            }
        }

        ProbeContext probeContext = new ProbeContext(host, guid, key);
        ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        scheduledExecutorService.schedule(new AnnouncementTask(probeContext, scheduledExecutorService), 1, TimeUnit.SECONDS);
    }

    private static void errorOut(String message) {
        System.out.println(message);
        System.exit(1);
    }
}

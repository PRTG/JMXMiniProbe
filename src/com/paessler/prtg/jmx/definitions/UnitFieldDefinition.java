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

package com.paessler.prtg.jmx.definitions;

import com.paessler.prtg.jmx.channels.Channel;


public class UnitFieldDefinition extends RadioFieldDefinition {

/*    public final static String BANDWIDTH_TYPE = "1";
    public final static String MEMORY_TYPE = "2";
    public final static String DISK_TYPE = "3";
    public final static String FILE_TYPE = "4";
    public final static String PERCENT_TYPE = "5";
    public final static String COUNT_TYPE = "6";
    public final static String CPU_TYPE = "7";
    public final static String RESPONSETIME_TYPE = "8";
    public final static String TIMESECONDS_TYPE = "9";
    public final static String TIMEHOURS_TYPE = "10";
    public final static String CUSTOM_TYPE = "11";
*/    
	// --------------------------------------------------------------
    public UnitFieldDefinition(String name, String caption, String help, String defaultvalue) {
        super(name, caption, help, defaultvalue);
/*        addOption(BANDWIDTH_TYPE, "Bandwidth (bytes)");
        addOption(MEMORY_TYPE, "Memory (bytes)");
        addOption(DISK_TYPE, "Disk (bytes)");
        addOption(FILE_TYPE, "File (bytes)");
        addOption(PERCENT_TYPE, "Percent");
        addOption(COUNT_TYPE, "Count");
        addOption(CPU_TYPE, "CPU (%)");
        addOption(RESPONSETIME_TYPE, "Response Time");
        addOption(TIMESECONDS_TYPE, "Time Seconds");
        addOption(TIMEHOURS_TYPE, "Time Hours");
*/
        addOption(Channel.UNIT_STR_BANDWIDTH, "Bandwidth (bytes)");
        addOption(Channel.UNIT_STR_MEMORY, "Memory (bytes)");
        addOption(Channel.UNIT_STR_DISK, "Disk (bytes)");
        addOption(Channel.UNIT_STR_FILE, "File (bytes)");
        addOption(Channel.UNIT_STR_PERCENT, "Percent");
        addOption(Channel.UNIT_STR_COUNT, "Count");
        addOption(Channel.UNIT_STR_CPU, "CPU (%)");
        addOption(Channel.UNIT_STR_TRESPONSE, "Response Time");
        addOption(Channel.UNIT_STR_TSEC, "Time Seconds");
        addOption(Channel.UNIT_STR_THOURS, "Time Hours");
        addOption(Channel.UNIT_STR_TEMP, "Temperature");
//        addOption(Channel.UNIT_STR_CUSTOM, "Custom");

    }
}

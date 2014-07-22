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

import java.util.HashMap;
import java.util.Map;

public class UnitFieldDefinition extends FieldDefinition {
    public String type = "radio";
    public String name;
    public String caption;
    public String help;
    public Map<String, String> options;

    public final static String BANDWIDTH_TYPE = "1";
    public final static String MEMORY_TYPE = "2";
    public final static String DISK_TYPE = "3";
    public final static String FILE_TYPE = "4";
    public final static String PERCENT_TYPE = "5";
    public final static String COUNT_TYPE = "6";
    public final static String CPU_TYPE = "7";
    public UnitFieldDefinition(String name, String caption, String help) {
        this.name = name;
        this.caption = caption;
        this.help = help;
        options = new HashMap<String, String>();
        options.put(BANDWIDTH_TYPE, "Bandwidth (bytes)");
        options.put(MEMORY_TYPE, "Memory (bytes)");
        options.put(DISK_TYPE, "Disk (bytes)");
        options.put(FILE_TYPE, "File (bytes)");
        options.put(PERCENT_TYPE, "Percent");
        options.put(COUNT_TYPE, "Count");
        options.put(CPU_TYPE, "CPU (%)");
    }
}

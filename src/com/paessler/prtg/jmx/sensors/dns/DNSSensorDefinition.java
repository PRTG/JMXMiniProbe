/*
 * Copyright (c) 2015, Paessler AG <support@paessler.com>
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
package com.paessler.prtg.jmx.sensors.dns;

import java.util.ArrayList;
import java.util.List;

import com.paessler.prtg.jmx.definitions.FieldDefinition;
import com.paessler.prtg.jmx.definitions.GroupDefinition;
import com.paessler.prtg.jmx.definitions.RadioFieldDefinition;
import com.paessler.prtg.jmx.definitions.RemoteSensorDefinition;
import com.paessler.prtg.jmx.definitions.SimpleEditFieldDefinition;

/**
 * @Author JR Andreassen
 */

public class DNSSensorDefinition extends RemoteSensorDefinition {
	public static final String KIND = "mjdns";
	public static final String TAG  = "mjdnssensor";
	public static final String DNS_SENSOR_VALS 		= "dnssensor";
	public static final String FIELD_DNS_VECTOR		= "domain";
	public static final String FIELD_DELAY 			= "delay";
	public static final String FIELD_TYPE 			= "type";

	
    // -----------------------------------------------------------------------
	@Override
	public List<GroupDefinition> getGroups(List<GroupDefinition> groups){
//		List<GroupDefinition> retVal = super.getGroups(groups);
		List<GroupDefinition> retVal = new ArrayList<GroupDefinition>();
		

		GroupDefinition group = new GroupDefinition(DNS_SENSOR_VALS, "DNS Specific settings");
        group.fields.add(getTimeoutField(5, 1, 900, "sec"));
        group.fields.add(getPortField(53, "DNS Port"));
        // -------------------------
        FieldDefinition tmpfield = new SimpleEditFieldDefinition(FIELD_DNS_VECTOR, "Domain(s)", "Enter a DNS name(s) or IP address(se) to resolve. [Comma separated list]");
        tmpfield.setRequired(FieldDefinition.FIELDVALUE_REQUIRED_TRUE);
        group.fields.add( tmpfield);
        // -------------------------
        RadioFieldDefinition snmpVersion = new RadioFieldDefinition(FIELD_TYPE, "Query Type", "Specify the type of query that the sensor will send to the DNS server.", "A");
        snmpVersion.setRequired(FieldDefinition.FIELDVALUE_REQUIRED_TRUE);
        snmpVersion.addOption("A", 		"Host address IPv4 (A)");
        snmpVersion.addOption("AAAA",	"Host address IPv6 (AAAA)");
        snmpVersion.addOption("CNAME",	"Canonical name for an alias (CNAME)");
        snmpVersion.addOption("MX",		"Mail exchange (MX)");
        snmpVersion.addOption("NS",		"Authoritative name server (NS)");
        snmpVersion.addOption("PTR",	"Domain name pointer (PTR)");
        snmpVersion.addOption("SOA",	"Start of a zone of authority marker (SOA)");
        snmpVersion.addOption("SRV",	"Service Record");

        group.fields.add(snmpVersion);
        // -------------------------
        retVal.add(group);
		return retVal;
	}	
    // -----------------------------------------------------------------------
    public DNSSensorDefinition() {
       	super(KIND, "DNS", "Monitors a DNS server (Domain Name Service), resolves a domain name, and compares it to an IP address", TAG, 
        		"The DNS sensor monitors a Domain Name Service (DNS) server. It resolves a domain name and compares it to a given IP address.");
        // -------------------------

    }

}

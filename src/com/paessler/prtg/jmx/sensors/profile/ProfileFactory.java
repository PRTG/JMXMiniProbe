/*
 * Copyright (c) 2014-2015, Paessler AG <support@paessler.com>
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
package com.paessler.prtg.jmx.sensors.profile;

import java.io.File;
import java.io.FilenameFilter;
import java.util.Map;

import org.apache.commons.digester3.Digester;
import org.apache.commons.lang3.StringEscapeUtils;

import com.paessler.prtg.jmx.ProbeContext;
import com.paessler.prtg.jmx.channels.Channel;
import com.paessler.prtg.jmx.sensors.jmx.JMXUtils;

public class ProfileFactory {

	// -------------------------------------------
	public static StringBuilder toXML(StringBuilder target, Map<String,Object> map, String prefix, String suffix){
		if(map != null && map.size() > 0){
			// -------------------------------------
			target.append(prefix);
			target.append("<properties>");
			target.append(suffix);
			String tmp = prefix + "\t";
			String tmp2 = tmp + "\t";
			for(Map.Entry<String,Object> currme: map.entrySet()){
				target.append(tmp);
				target.append("<property>");
				target.append(suffix);
				ProfileFactory.addXMLElement(target, "name", currme.getKey(), tmp2, suffix);
				ProfileFactory.addXMLElement(target, "value", currme.getValue(), tmp2, suffix);
				target.append(tmp);
				target.append("</property>");
				target.append(suffix);
			}
			target.append(prefix);
			target.append("</properties>");
			target.append(suffix);
		}
		return target;
	}	
	// -------------------------------------------
	public static void addXMLElement(StringBuilder target, String name, Object val, String prefix, String suffix){
		target.append(prefix);
		if(val != null){
			target.append("<"+name+">");
//			target.append(suffix);
			target.append(StringEscapeUtils.escapeXml11(val.toString()));
//			target.append(prefix);
			target.append("</"+name+">");
		} else {
			target.append("<"+name+"/>");
		}
		target.append(suffix);
	}
	// -------------------------------------------
	  public static String toXML(Profiles prof){
			StringBuilder retVal = new StringBuilder();
			retVal.append("<!-- ###########################################################\n");
			retVal.append("Unit Values[");
			retVal.append(StringEscapeUtils.escapeXml11(Channel.getUnitStrings()));
			retVal.append("]\n");
			retVal.append("***************************************************************\n");
			retVal.append("Please note, the apache.commons.digester3 is case sensitive.\n");
			retVal.append("All XML TAGS should be in lower case.\n");
			retVal.append("Profile Tags * MUST * be unique.\n");
			retVal.append("***************************************************************\n");
			retVal.append("RMI Connection Strings:\n");
			retVal.append("Generic:\t"+JMXUtils.RMI_STRING_DEFAULT);
			retVal.append("Weblogic:\tservice:jmx:iiop://{ServerIP}:{Port}/jndi/weblogic.management.mbeanservers.domainruntime");
			retVal.append("Websphere:\tservice:jmx:iiop://{ServerIP}:{Port}/jndi/JMXConnector");
			retVal.append("***************************************************************\n");
			retVal.append("The following are all the JMX MBeans found\n");
			retVal.append("########################################################### -->\n");
			prof.toXML(retVal, "", "\n");
			return retVal.toString();
      }

	
		// -----------------------------------------------------------------------------------
	public static Digester getDigester(){
		Digester retVal = null;
	    try {
	    	retVal = new Digester();
	    	retVal.setValidating( false );
	    	retVal.setValidating( false );
	        Profiles.describe4Digester(retVal, "");
	      } catch( Exception exc ) {
	        exc.printStackTrace();
	      }		
	    return retVal;
	}

	// -----------------------------------------------------------------------------------
	public static Profiles loadProfile(ProbeContext context, File inputFile){
		Profiles retVal = null;
		if(inputFile.exists()){
			System.out.println("Loading profile from["+inputFile.getAbsolutePath()+"]");
		    try {
		        Digester digester = getDigester();
		        retVal = (Profiles)digester.parse( inputFile );
		      } catch( Exception exc ) {
		        exc.printStackTrace();
		      }
		} else {
//			Logger.getRootLogger().debug("File does not exist["+inputFile.getAbsolutePath()+"]");
			System.out.println("File does not exist["+inputFile.getAbsolutePath()+"]");
		}
	    return retVal;
	}
	// -----------------------------------------------------------------------------------
	public static Profiles loadProfiles(ProbeContext context, File inputFile){
		Profiles retVal = null;
		if(inputFile.exists()){
			if(!inputFile.isDirectory()){
//				try {
					inputFile = new File(inputFile.getParent());
/*				} catch (IOException e) {
					inputFile = null;
					System.out.println("Failed Loading profiles from["+inputFile.getAbsolutePath()+"]");
				}
*/				
			}
			File[] files = inputFile.listFiles(new FilenameFilter(){
				public boolean accept(File dir, String name) {
					String lowercaseName = name.toLowerCase();
					if (lowercaseName.endsWith(".xml")) {
						return true;
					} else {
						return false;
					}
				}
			});
			Profiles tmpp;
			System.out.println("Loading profiles from["+inputFile.getAbsolutePath()+"]");
			for(File curr : files){
				tmpp = loadProfile(context, curr);
				if(tmpp != null){
					if(retVal != null){
						retVal.addProfiles(tmpp);
					} else {
						retVal = tmpp; 
					}
				}
			}
		} else {
//			Logger.getRootLogger().debug("File does not exist["+inputFile.getAbsolutePath()+"]");
			System.out.println("File does not exist["+inputFile.getAbsolutePath()+"]");
		}
	    return retVal;
	}
	// -----------------------------------------------------------------------------------
	public static  Profiles loadProfiles(ProbeContext context, String filename){
        return loadProfiles(context, new File( filename));
	}
	
	// -----------------------------------------------------------------------------------
	  public static void main( String[] args ) {
		  org.apache.log4j.BasicConfigurator.configure();
//		  Profiles prof = loadProfile(null, new File("W:/Paessler/java/JMXMiniProbe/data/profiles/jmx/Tomcat.xml"));
		  Profiles prof = loadProfile(null, new File("C:/AppServers/Tomcat8/webapps/JMXProbe/profiles/SensorProfiles.xml"));
		  System.out.println(prof);
	  }
}

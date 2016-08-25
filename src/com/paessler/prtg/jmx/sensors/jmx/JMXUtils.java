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
 */package com.paessler.prtg.jmx.sensors.jmx;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.management.ManagementFactory;
import java.util.HashMap;
import java.util.Map;

import javax.management.InstanceNotFoundException;
import javax.management.IntrospectionException;
import javax.management.MBeanAttributeInfo;
import javax.management.MBeanInfo;
import javax.management.MBeanServerConnection;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectInstance;
import javax.management.ObjectName;
import javax.management.ReflectionException;
import javax.management.openmbean.CompositeData;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;
import javax.naming.Context;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.PosixParser;

import com.paessler.prtg.jmx.Logger;
import com.paessler.prtg.jmx.mbean.PRTGInterface;
import com.paessler.prtg.jmx.sensors.JMXSensor;
import com.paessler.prtg.jmx.sensors.profile.Entry;
import com.paessler.prtg.jmx.sensors.profile.Profile;
import com.paessler.prtg.jmx.sensors.profile.ProfileFactory;
import com.paessler.prtg.jmx.sensors.profile.Profiles;
import com.paessler.prtg.jmx.sensors.profile.StringAttribute;
import com.paessler.prtg.util.SystemUtility;

public class JMXUtils {
	// ----------------------------------------------------------------------
	// http://docs.oracle.com/javase/7/docs/api/javax/management/remote/rmi/package-summary.html
	public static String LOCAL_STRING	= "local";
	public static String LOCALHOST_STRING	= "localhost";
	public static String RMI_PORT_DEFAULT = "1099";
	public static String RMI_STRING_LOCAL = "service:jmx:rmi:///jndi/rmi://localhost:"+RMI_PORT_DEFAULT+"/jmxrmi";
    public static String RMI_STRING_DEFAULT = "service:jmx:{rmi|iiop}:///jndi/rmi://{TARGET_MACHINE}:{RMI_REGISTRY_PORT def:"+RMI_PORT_DEFAULT+"}/jmxrmi";
	// ----------------------------------------------------------------------
	public static String makeRMIString(String host, String port){
		StringBuilder retVal = new StringBuilder();
		retVal.append("service:jmx:rmi:///jndi/rmi://");
		if(host == null){
			port = "localhost"; 
		}
		retVal.append(host);
		retVal.append(":");
		if(port == null){
			if("localhost".equalsIgnoreCase(host)){
				port = System.getProperty(SystemUtility.SYS_PROPERTY_JMX_REMOTE_PORT);
			}
			if(port == null){
				port = RMI_PORT_DEFAULT;
			}
		}
		retVal.append(port);
		retVal.append("/jmxrmi");
		return retVal.toString();
	}
	// ----------------------------------------------------------------------
	public static ObjectName getObjectName(String name){
		ObjectName retVal = null;
        try {
        	retVal = new ObjectName(name);
		} catch (MalformedObjectNameException e) {
		}
		return retVal;
	}

	// -----------------------------------------------------------------------------------
	/**
	 *  Get Entry for MBeanInfo
	 * @param mbi			mBeanInfo to extract from
	 * @param objectName	Name of object[mBean]
	 * @param classname		Class name of mBean
	 * @return
	 */
	  public static Entry getBeanEntry(MBeanServerConnection server, MBeanInfo mbi, ObjectName objectName, String classname){
		  	Entry retVal = new Entry();
		  	Object obj;
			retVal.setObject(objectName.toString());
			retVal.setDescription(mbi.getDescription());
			retVal.setComment("Class Name:"+classname);
	//					entry.setDisplayValue();
	        for(MBeanAttributeInfo mbai: mbi.getAttributes()){
	        	StringAttribute attr = new StringAttribute(); 
	        	attr.setObject(mbai.getName());
	        	attr.setDescription(mbai.getDescription());
	        	Object type = mbai.getType();
	        	String commentstring = "";
	        	if(type instanceof CompositeData){
	        		commentstring = "Composit[";
	        		CompositeData data = (CompositeData)type;
	        		commentstring = data.getCompositeType().toString();
	        		for(Object curr: data.values()){
	        			commentstring += ";"+curr+"["+curr.getClass().getSimpleName()+"]";
	        		}
        			commentstring += "]";
	        	} else {
	        		if(type != null){
	        			commentstring = type.toString();
	        		}
	        	}
	        	commentstring = "type="+commentstring+",unit=COUNT";
				try {
					obj = server.getAttribute(objectName, mbai.getName());
					if(obj != null){
						commentstring += "; Value="+obj.toString();
					}
					
				} catch (Throwable e) {
					commentstring += "; Value="+e.getMessage();
				}
	        	attr.setComment(commentstring);
	        	retVal.addAttribute(attr);
	        }
		  retVal.sort();
		  return retVal;
    }
		// -----------------------------------------------------------------------------------
		/**
		 * Extract all Beans for server
		 * @param server	MBean Server to extract from
		 * @param kind		Kind of Bean for output
		 * @param tag		Tag for output
		 * @param includeselfie	Include definition of Self
		 * @return			Profiles
		 * @throws IOException 
		 */
		  public static Profiles getBeanProfiles(MBeanServerConnection server, String kind, String tag, boolean includeselfie) throws IOException{
			  Profiles retVal = new Profiles();
			  if(includeselfie){
			        PRTGInterface.getAndRegisterBean(PRTGInterface.getDefaultBeanName());
			  }
			  for(ObjectInstance instance: server.queryMBeans(null, null)){
				  Profile prof = new Profile(); 
				  prof.setBaseclass(JMXSensor.class.getName());
				  prof.setName(instance.getObjectName().toString());
				  prof.setKind(kind);
				  prof.setTag(kind+","+tag);
				  try {
						MBeanInfo mbi = server.getMBeanInfo(instance.getObjectName());
						Entry entry = getBeanEntry(server, mbi, instance.getObjectName(), instance.getClassName());
		            	prof.addEntry(entry);
					} catch (IntrospectionException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (InstanceNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (ReflectionException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					  if(prof != null){
						  prof.sort();
					  }
					  retVal.addProfile(prof);
				  }
			  if(retVal != null){
				  retVal.sort();
			  }
	        return retVal;
	    }
	
		  
		  public static class MBeanConnectionHolder {
	            MBeanServerConnection mbc = null;
	        	JMXConnector jmxc = null;
	        	public MBeanConnectionHolder(MBeanServerConnection mbconn, JMXConnector jmxconn){
		            this.mbc = mbconn;
		            this.jmxc = jmxconn;
	        		
	        	}
	        	public void finalize() throws Throwable{
	        		close();
	        		super.finalize();
	        	}
				public MBeanServerConnection getMbc()  throws IOException{
					if(mbc == null){
						mbc = jmxc.getMBeanServerConnection();
					}
					return mbc;
				}
				public void setMbc(MBeanServerConnection mbc) {
					this.mbc = mbc;
				}
				public JMXConnector getJmxc() {
					return jmxc;
				}
				public void setJmxc(JMXConnector jmxc) {
					mbc = null;
					this.jmxc = jmxc;
				}

				public void close(){
					mbc = null;
					if(jmxc != null){
						try {
							jmxc.close();
						}
						catch(Throwable e) {}
						jmxc = null;
					}
				}
		  }
			// -----------------------------------------------------------------------------------
		  /**
		   *  Create a connection to a JMX Server
		   * @param rmistring	RMI String to server
		   * @param username	Username to login with 
		   * @param password	Passeword for user
		   * @return			Connection or null on failure
		   * @throws IOException
		   */
		  public static MBeanConnectionHolder getJMXConnection(String rmistring, String username, String password) throws IOException{
			  MBeanConnectionHolder retVal = new MBeanConnectionHolder(ManagementFactory.getPlatformMBeanServer(), null);
	        	JMXConnector jmxc = null; 
	        	if(!(rmistring == null || 
	        			LOCALHOST_STRING.equalsIgnoreCase(rmistring) ||
	        			LOCAL_STRING.equalsIgnoreCase(rmistring) || 
	        			RMI_STRING_LOCAL.equalsIgnoreCase(rmistring))){
	                String[] creds = {username, password};
	                Map<String, Object> env = new HashMap<String, Object>();
	                env.put(JMXConnector.CREDENTIALS, creds);
	                env.put(Context.SECURITY_PRINCIPAL, username);
	                env.put(Context.SECURITY_CREDENTIALS, password);
	                JMXServiceURL serviceURL = new JMXServiceURL(rmistring);

	                retVal.setJmxc(JMXConnectorFactory.connect(serviceURL, env));
	                if(retVal.getJmxc() != null){
	                	retVal.getMbc();
	                } else {
	                	retVal = null;
	                }
	        	}
	        	return retVal;
				  
			  }
			// -----------------------------------------------------------------------------------
		    private static void showHelp(Options options) {
		        HelpFormatter formatter = new HelpFormatter();
		        formatter.printHelp("JMX scanner", options);
		        System.exit(1);
		    }
		    public static boolean dumpJMXToFile(String outputfile, String rmiString, String username, String password, boolean includeselfie){
		    	boolean retVal = false;
//		        Profiles prof = loadProfile(null, "W:/Paessler/java/JMXMiniProbe/data/profiles/jmx/Tomcat.xml");
		    	MBeanConnectionHolder mbsch = null;
		        MBeanServerConnection mbsc = null;
		        try {
		        	mbsch = getJMXConnection(rmiString, username, password);
		        	if(mbsch != null) {
		        		mbsc = mbsch.getMbc();
		        	}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					Logger.log("Cought Exception while Connecting to ["+rmiString+"] "+e.getMessage());
				}
		        String xml = null;
		        if(mbsc != null){
		        	Profiles prof = null;
		        	try {
						prof = getBeanProfiles(mbsc, JMXSensorDefinition.KIND, JMXSensorDefinition.TAG, includeselfie);
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						Logger.log("Cought Exception while dumpJMXToFile ", e1);
						
					}
		        	if(prof != null){
		        		xml = ProfileFactory.toXML(prof);
				        if(xml != null){
				        	if(outputfile != null){
				        		File ofn = new File(outputfile);
								Logger.log("Outputting XML to  ["+ofn.getAbsolutePath()+"] ");
				        		OutputStream os = null;
				        		try {
									os = new FileOutputStream(ofn);
									os.write(xml.getBytes());
									retVal = true;
								} catch (FileNotFoundException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								} catch (IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								} finally {
									if(os != null) {
										try {
											os.close();
										} catch (IOException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}
									}
								} // finally
				        	} else {// if os
				        		Logger.log(xml);
				        	}
				        }
		        	}
		        	// Close it
	        		mbsch.close();

		        } else {
					Logger.log("No Connection to JMX Server");
		        }
		        return retVal;
		    }
			// -----------------------------------------------------------------------------------
		    // -r "service:jmx:rmi:///jndi/rmi://192.168.0.172:1090/jmxrmi" -o junk.xml
		    public static void main(String[] args) {
//		    	org.apache.log4j.BasicConfigurator.configure();
		        Options options = new Options();
		        options.addOption("r", true, "RMI String["+RMI_STRING_DEFAULT+"]");
		        options.addOption("o", true, "Output file");
		        options.addOption("u", true, "username");
		        options.addOption("p", true, "password");
		        options.addOption("s", false, "include selfie[Definition of PRTGMBean]");
		        if (args.length == 0) {
		            showHelp(options);
		        }
		        CommandLineParser parser = new PosixParser();
		        CommandLine cmdLine;
		        String rmiString = null, outputfile = null, username = null, password = null;
		        
//		        rmiString = "service:jmx:iiop:///jndi/rmi://192.168.0.189:2809/jndi/JMXConnector";
		        rmiString = "service:jmx:iiop://192.168.0.189:2809/jndi/JMXConnector";
		        boolean includeselfie = false;
		        try {
		            cmdLine = parser.parse(options, args);
		            if (cmdLine.hasOption("r"))
		            	rmiString = cmdLine.getOptionValue("r");
		            if(rmiString == null)
		            	showHelp(options);
		            outputfile = cmdLine.getOptionValue("o");
		            username = cmdLine.getOptionValue("u");
		            password = cmdLine.getOptionValue("p");
		            includeselfie = cmdLine.hasOption("s");
		        } catch (Exception e) {
		            showHelp(options);
		        }
		        
//		        Profiles prof = loadProfile(null, "W:/Paessler/java/JMXMiniProbe/data/profiles/jmx/Tomcat.xml");
		        if(!dumpJMXToFile(outputfile, rmiString, username, password, includeselfie)){
					Logger.log("Failed to dump JMX MBeans");
		        }
//		        System.out.println(prof);
		  }
		  
}

/*
 * JVMRuntimeClient.java
 *
 * Created on March 9, 2007, 2:44 PM
 *
 * Copyright (c) Sun Microsystems, 2007 - All rights reserved.
 */

package com.jvmruntime;

import java.io.File;
import java.lang.management.ManagementFactory;
import java.net.InetAddress;
import java.util.logging.Logger;

import javax.management.remote.JMXServiceURL;

import com.sun.tools.attach.VirtualMachine;
import com.sun.tools.attach.VirtualMachineDescriptor;

import java.lang.management.RuntimeMXBean;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.management.MBeanServerConnection;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;

/**
 * Class JVMRuntimeClient - Shows how to programmatically connect to
 * a running VM and interact with its RuntimeMXBean.
 *
 * @author Sun Microsystems, 2007 - All rights reserved.
 */
public class JVMRuntimeClient {
    
    /**
     * A logger for this class.
     **/
    private static final Logger LOG =
            Logger.getLogger(JVMRuntimeClient.class.getName());
    
    /** Creates a new instance of Main */
    public JVMRuntimeClient() {
    }

    /**
     * A helper class to analyze the command line and create a JMXServiceURL.
     * Allows to pass a JMXServiceURL, a host and port, or a VM PID.
     **/
    public static class ConnectionArgs {
        private static final String CONNECTOR_ADDRESS =
          "com.sun.management.jmxremote.localConnectorAddress";
        
        
        public final JMXServiceURL jmxURL;
        final public String SYNTAX = "JVMRuntimeClient -url <jmx-url> " +
                "| -port <port-number> [-host <host-or-ip] " +
                "| -pid <pid> | -help";
        
        public ConnectionArgs(String[] args) {
            jmxURL = parseArgs(args);
        }
        
        public final JMXServiceURL getJMXServiceURL() {
            return jmxURL;
        }
        
        private JMXServiceURL parseArgs(String[] args) {
            
            String host = null;
            int port = 0;
            String pid = null;
            JMXServiceURL serviceURL = null;
            
            for (int i=0;i<args.length;i++) {
                
                if (args[i].startsWith("-url")) {
                // The '-url' option will let you specify a JMXServiceURL
                // on the command line. This is an URL that begins with
                // service:jmx:<protocol>
                //
                    if (host != null)
                        throwSyntaxError(
                                "-url and -host are mutually exclusive");
                    if (pid != null)
                        throwSyntaxError(
                                "-pid and -url are mutually exclusive");
                    if (port > 0)
                        throwSyntaxError(
                                "-port and -url are mutually exclusive");
                    if (++i >= args.length)
                        throwSyntaxError(
                                "missing JMXServiceURL after -url");
                    
                    try {
                        serviceURL = new JMXServiceURL(args[i]);
                    } catch (Exception x) {
                        throwSyntaxError("bad JMXServiceURL after -url: " + x);
                    }
                    continue;
                    
                } else if (args[i].startsWith("-host")) {
                // The '-host' and '-port' options will let you specify a host
                // and port, and from that will construct the JMXServiceURL of
                // the default RMI connector, that is:
                // service:jmx:rmi:///jndi/rmi://<host>:<port>/jmxrmi"
                //
                    if (serviceURL != null)
                        throwSyntaxError("-url and -host are mutually exclusive");
                    if (pid != null)
                        throwSyntaxError("-pid and -host are mutually exclusive");
                    if (++i >= args.length)
                        throwSyntaxError("missing host after -host");
                    try {
                        InetAddress.getByName(args[i]);
                        host = args[i];
                    } catch (Exception x) {
                        throwSyntaxError("bad host after -url: " + x);
                    }
                    
                } else if (args[i].startsWith("-port")) {
                // The '-host' and '-port' options will let you specify a host
                // and port, and from that will construct the JMXServiceURL of
                // the default RMI connector, that is:
                // service:jmx:rmi:///jndi/rmi://<host>:<port>/jmxrmi"
                //
                    if (serviceURL != null)
                        throwSyntaxError("-url and -port are mutually exclusive");
                    if (pid != null)
                        throwSyntaxError("-pid and -port are mutually exclusive");
                    if (++i >= args.length)
                        throwSyntaxError("missing port number after -port");
                    try {
                        port = Integer.parseInt(args[i]);
                        if (port <= 0)
                            throwSyntaxError("bad port number after -port: " +
                                    "must be positive");
                    } catch (Exception x) {
                        throwSyntaxError("bad port number after -port: " + x);
                    }
                } else if (args[i].startsWith("-pid")) {
                // The '-pid' and option will let you specify the PID of the
                // target VM you want to connect to. It will then use the 
                // attach API to dynamically launch the JMX agent in the target
                // VM (if needed) and to find out the JMXServiceURL of the
                // the default JMX Connector in that VM.
                //
                    if (serviceURL != null)
                        throwSyntaxError("-url and -pid are mutually exclusive");
                    if (port > 0)
                        throwSyntaxError("-port and -pid are mutually exclusive");
                    if (++i >= args.length)
                        throwSyntaxError("missing pid after -pid");
                    try {
                        pid = args[i];
                    } catch (Exception x) {
                        throwSyntaxError("bad pid after -pid: " + x);
                    }
                } else if (args[i].startsWith("-help")) {
                    final List<String> vmlist = new ArrayList<String>();
                    for (VirtualMachineDescriptor vd : VirtualMachine.list()) {
                        vmlist.add(vd.id());
                    }
                    System.err.println(SYNTAX);
                    System.err.println("Running JVMs are: "+vmlist);
                    throw new IllegalArgumentException(SYNTAX);
                } else {
                    throwSyntaxError("Unknown argument: "+args[i]);
                } 
            }
            
            // A JMXServiceURL was given on the command line, just use this.
            //
            if (serviceURL != null)
                return serviceURL;
            
            // A -host -port info was given on the command line. 
            // Construct the default RMI JMXServiceURL from this.
            //
            if (port > 0) {
                if (host == null)
                    host = "localhost";
                
                try {
                    return new JMXServiceURL("service:jmx:rmi:///jndi/rmi://"+
                            host+":"+port+"/jmxrmi");
                } catch (Exception x) {
                    throwSyntaxError("Bad host or port number: "+x);
                }
            }
            
            // A PID was given on the command line. 
            // Use the attach API to find the target's connector address, and
            // start it if needed.
            //
            if (pid != null) {
            	JMXServiceURL retVal = null;
                try {
	                	retVal = getURLForPid(pid);
	                	if(retVal == null){
	                		retVal = extractJMXServiceURL(pid);
	                	}
	                } catch (Exception x) {}
            		if(retVal == null){
		                try {
		                		retVal = extractJMXServiceURL(pid);
		                } catch (Exception x) { }
            		}
                if(retVal == null) {
                    throwSyntaxError("cannot attach to target vm "+pid);
                }
                return retVal;
            }
            
            final List<String> vmlist = new ArrayList<String>();
            for (VirtualMachineDescriptor vd : VirtualMachine.list()) {
                vmlist.add(vd.id());
            }
            throwSyntaxError("missing argument: "+ "-port | -url | -pid | -list"
                    +"\\n\\tRunning VMs are: "+vmlist);
            
            // Unreachable.
            return null;
        }
        
        
        private void throwSyntaxError(String msg) {
            System.err.println(msg);
            System.err.println(SYNTAX);
            throw new IllegalArgumentException(msg);
        }

        
        private JMXServiceURL extractJMXServiceURL(String pid) throws Exception
        {
          String serviceURL = null;
          int ipid;
          try
          {
        	ipid = Integer.parseInt(pid);
            serviceURL = sun.management.ConnectorAddressLink.importFrom(ipid);
          }
          catch(Exception e)
          {
            System.out.println("Cannot find process pid");
          }
            
          if(serviceURL == null)
            return null;
          else
            return new JMXServiceURL(serviceURL);
        }        
        private JMXServiceURL getURLForPid(String pid) throws Exception {
            
            // attach to the target application
            final VirtualMachine vm = VirtualMachine.attach(pid);
            
            // get the connector address
            String connectorAddress =
                    vm.getAgentProperties().getProperty(CONNECTOR_ADDRESS);
            
            // no connector address, so we start the JMX agent
            if (connectorAddress == null) {
                String agent = vm.getSystemProperties().getProperty("java.home") +
                        File.separator + "lib" + File.separator + "management-agent.jar";
                vm.loadAgent(agent);
                
                // agent is started, get the connector address
                connectorAddress =
                        vm.getAgentProperties().getProperty(CONNECTOR_ADDRESS);
                assert connectorAddress != null;
            }
            return new JMXServiceURL(connectorAddress);
        }
        
    }
    
	////////////////////////////////////////////////////////////////////
	/**
	 * get the PID of the running JVM. NOTE: this may not work on all platforms/jvms
	 */
	public static String getProcessIDString()
	{
		RuntimeMXBean mxbean = java.lang.management.ManagementFactory.getRuntimeMXBean();
		String jvmNameStr = mxbean.getName();
		System.err.println("jvmNameStr->" + jvmNameStr);
		//4072@THPDEVL11
		int atIndex = jvmNameStr.indexOf('@');
		if( atIndex < 0 )
			return null;
		
		String pid = jvmNameStr.substring(0, atIndex);
		//System.err.println("pid->" + pid);
		return pid;
	}//method
    
	/**
	 * Get current Java Spec as an integer.
	 */
	public static void dumpSystemProperties()
	{
			Properties prop = System.getProperties();
			prop.list(System.out);
	}	// dumpSystemProperties
    
    /**
     * @param args the command line arguments: 
     *             must be -url <jmx-url>,
     *             or -port <port-number> [-host <host-or-ip],
     *             or -pid <pid>,
     *             or -help
     */
    public static void main(String[] args) throws Exception {
 //   	dumpSystemProperties();
//    	String pid = getProcessIDString();
//    	String[] args2 = {"-pid", pid};
    	String[] args2 = {"-port", "9010"};
    	
        // Parse arguments.
        final ConnectionArgs cArgs = new ConnectionArgs(args2);
        
        // Get target's URL
        final JMXServiceURL target = cArgs.getJMXServiceURL();
        
        System.out.println("jmxrmi URL: "+target);
        // Connect to target (assuming no security)
        final JMXConnector connector = JMXConnectorFactory.connect(target);
        
        // Get an MBeanServerConnection on the remote VM.
        final MBeanServerConnection remote = 
                connector.getMBeanServerConnection();
        
        final RuntimeMXBean remoteRuntime = 
                ManagementFactory.newPlatformMXBeanProxy(
                    remote,
                    ManagementFactory.RUNTIME_MXBEAN_NAME,
                    RuntimeMXBean.class);
        
        System.out.println("Target VM is: "+remoteRuntime.getName());
        System.out.println("Started since: "+remoteRuntime.getUptime());
        System.out.println("With Classpath: "+remoteRuntime.getClassPath());
        System.out.println("And args: "+remoteRuntime.getInputArguments());
        connector.close();
        Thread.sleep(1000000);
    }
    
}
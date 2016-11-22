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

package com.paessler.prtg.jmx.sensors;

import com.google.gson.JsonObject;
import com.paessler.prtg.jmx.Logger;
import com.paessler.prtg.jmx.channels.Channel;
import com.paessler.prtg.jmx.definitions.SensorConstants;
import com.paessler.prtg.jmx.definitions.VMHealthDefinition;
import com.paessler.prtg.jmx.sensors.jmx.JMXAttribute;
import com.paessler.prtg.jmx.sensors.jmx.JMXBean;
import com.paessler.prtg.util.SystemUtility;

import java.lang.management.ManagementFactory;

public class VMHealthSensor extends JMXSensor {
	
	public VMHealthSensor(){
		//----------------------------------------------------------------------
		setKind(VMHealthDefinition.KIND);
	    setDefinition(new VMHealthDefinition());
	    setSensorName("VMHealth");
		
	}
    // --------------------------------------------------------------------------------------------
	public VMHealthSensor(VMHealthSensor tocpy){
		super(tocpy);
	}
	
    // --------------------------------------------------------------------------------------------
	public String getSensorMessage() 
	{
		return "JVM v"+SystemUtility.getJavaVersionString()+ " " 
				+ SystemUtility.getSysPropertyString(SystemUtility.SYS_PROPERTY_JAVA_VENDOR_VENDOR);
	}
	//----------------------------------------------------------------------
    @Override
    public Sensor copy(){
		return new VMHealthSensor(this);
	}
    // --------------------------------------------------------------------------------------------
/*    @Override
    public DataResponse getResponses(MBeanServerConnection mbsc) throws Exception{
        DataResponse retVal = new DataResponse(sensorid, getSensorName());
        ObjectName memoryBeanName = new ObjectName(ManagementFactory.OPERATING_SYSTEM_MXBEAN_NAME);
        Double tmpD;
        if (memoryBeanName != null) {
//        	tmpD = ((Double) mbsc.getAttribute(memoryBeanName, "SystemLoadAverage"))*100.0;
//        	response.addChannel(new FloatChannel("OS: SystemLoadAverage", Channel.Unit.CPU, tmpD.floatValue()));
        	tmpD = ((Double) mbsc.getAttribute(memoryBeanName, "SystemCpuLoad"))*100.0 ;
        	retVal.addChannel(new FloatChannel("OS: SystemCpuLoad", Channel.Unit.CPU, tmpD.floatValue()));
        	tmpD = ((Double) mbsc.getAttribute(memoryBeanName, "ProcessCpuLoad"))*100.0;
        	retVal.addChannel(new FloatChannel("OS: ProcessCpuLoad", Channel.Unit.CPU, tmpD.floatValue()));
        	retVal.addChannel(new LongChannel("OS: FreePhysicalMemorySize", Channel.Unit.COUNT, (Long) mbsc.getAttribute(memoryBeanName, "FreePhysicalMemorySize")));
        	retVal.addChannel(new LongChannel("OS: AvailableProcessors", Channel.Unit.COUNT, (Integer) mbsc.getAttribute(memoryBeanName, "AvailableProcessors")));
        }
        memoryBeanName = new ObjectName(ManagementFactory.MEMORY_MXBEAN_NAME);
        CompositeData heapMemoryUsage = (CompositeData) mbsc.getAttribute(memoryBeanName, "HeapMemoryUsage");
        if (heapMemoryUsage != null) {
            long hmu = (Long) heapMemoryUsage.get("committed");
            long initHmu = (Long) heapMemoryUsage.get("init");
            long maxHmu = (Long) heapMemoryUsage.get("max");
            long usedHmu = (Long) heapMemoryUsage.get("used");

            LongChannel hmuChannel = new LongChannel("JVM: Committed heap memory", Channel.Unit.MEMORY, hmu);
            LongChannel initChannel = new LongChannel("JVM: Initialized heap memory", Channel.Unit.MEMORY, initHmu);
            LongChannel maxChannel = new LongChannel("JVM: Max heap memory", Channel.Unit.MEMORY, maxHmu);
            LongChannel usedChannel = new LongChannel("JVM: Used heap memory", Channel.Unit.MEMORY, usedHmu);

            retVal.addChannel(hmuChannel);
            retVal.addChannel(initChannel);
            retVal.addChannel(maxChannel);
            retVal.addChannel(usedChannel);
        }

       
        memoryBeanName = new ObjectName(ManagementFactory.THREAD_MXBEAN_NAME);
        {
//        	final long[] deadlocks = mbsc.findMonitorDeadlockedThreads();
            int liveThreadCount = (Integer) mbsc.getAttribute(memoryBeanName, "ThreadCount");
            int peakThreadCount = (Integer) mbsc.getAttribute(memoryBeanName, "PeakThreadCount");
            int daemonThreadCount = (Integer) mbsc.getAttribute(memoryBeanName, "DaemonThreadCount");
            long totalThreadCount = (Long) mbsc.getAttribute(memoryBeanName, "TotalStartedThreadCount");
            LongChannel liveThreadChannel = new LongChannel("JVM: Live threads", Channel.Unit.COUNT, liveThreadCount);
            LongChannel peakThreadChannel = new LongChannel("JVM: Peak threads", Channel.Unit.COUNT, peakThreadCount);
            LongChannel daemonThreadChannel = new LongChannel("JVM: Daemon threads", Channel.Unit.COUNT, daemonThreadCount);
            LongChannel totalThreadChannel = new LongChannel("JVM: Total threads started", Channel.Unit.COUNT, totalThreadCount);
            retVal.addChannel(liveThreadChannel);
            retVal.addChannel(peakThreadChannel);
            retVal.addChannel(daemonThreadChannel);
            retVal.addChannel(totalThreadChannel);
        }
        memoryBeanName = new ObjectName(ManagementFactory.CLASS_LOADING_MXBEAN_NAME);
        {
            long totalLoadedClasses = (Long) mbsc.getAttribute(memoryBeanName, "TotalLoadedClassCount");
            int currentLoadedClasses = (Integer) mbsc.getAttribute(memoryBeanName, "LoadedClassCount");
            long unloadedClasses = (Long) mbsc.getAttribute(memoryBeanName, "UnloadedClassCount");
            LongChannel totalLoadedClassChannel = new LongChannel("JVM: Total classes loaded", Channel.Unit.COUNT, totalLoadedClasses);
            LongChannel currentLoadedClassChannel = new LongChannel("JVM: Current classes loaded", Channel.Unit.COUNT, currentLoadedClasses);
            LongChannel unloadedClassChannel = new LongChannel("JVM: Total classes unloaded", Channel.Unit.COUNT, unloadedClasses);
            retVal.addChannel(totalLoadedClassChannel);
            retVal.addChannel(currentLoadedClassChannel);
            retVal.addChannel(unloadedClassChannel);
        }
    	return retVal;
    }
*/    
    // --------------------------------------------------------------------------------------------
    @SuppressWarnings("unused")
	protected void addDefs(){
    	
    	
        // --------------------------------------
    	JMXAttribute tmppair;
    	JMXBean attrlist = new JMXBean(ManagementFactory.OPERATING_SYSTEM_MXBEAN_NAME);
    	addBeanList(attrlist);
/*
	Object Name:java.lang:type=OperatingSystem, Information on the management interface of the MBean
		Attribute Name: CommittedVirtualMemorySize, Type:long, Description: CommittedVirtualMemorySize
		Attribute Name: FreePhysicalMemorySize, Type:long, Description: FreePhysicalMemorySize
		Attribute Name: FreeSwapSpaceSize, Type:long, Description: FreeSwapSpaceSize
		Attribute Name: ProcessCpuLoad, Type:double, Description: ProcessCpuLoad
		Attribute Name: ProcessCpuTime, Type:long, Description: ProcessCpuTime
		Attribute Name: SystemCpuLoad, Type:double, Description: SystemCpuLoad
		Attribute Name: TotalPhysicalMemorySize, Type:long, Description: TotalPhysicalMemorySize
		Attribute Name: TotalSwapSpaceSize, Type:long, Description: TotalSwapSpaceSize
		Attribute Name: Name, Type:java.lang.String, Description: Name
		Attribute Name: AvailableProcessors, Type:int, Description: AvailableProcessors
		Attribute Name: Arch, Type:java.lang.String, Description: Arch
		Attribute Name: SystemLoadAverage, Type:double, Description: SystemLoadAverage
		Attribute Name: Version, Type:java.lang.String, Description: Version
		Attribute Name: ObjectName, Type:javax.management.ObjectName, Description: ObjectName
 */
        {
//        	tmpD = ((Double) mbsc.getAttribute(memoryBeanName, "SystemLoadAverage"))*100.0;
//        	response.addChannel(new FloatChannel("OS: SystemLoadAverage", Channel.Unit.CPU, tmpD.floatValue()));
        	tmppair = new JMXAttribute("SystemCpuLoad",Channel.Unit.CPU);
        	tmppair.setMpy(100.0d);
        	tmppair.setDescription("OS: SystemCpuLoad");
        	attrlist.addAttributePair(tmppair);
        	// --------------
        	tmppair = new JMXAttribute("ProcessCpuLoad",Channel.Unit.CPU);
        	tmppair.setMpy(100.0d);
        	tmppair.setDescription("OS: ProcessCpuLoad");
        	attrlist.addAttributePair(tmppair);
        	// --------------
        	tmppair = new JMXAttribute("FreePhysicalMemorySize",Channel.Unit.MEMORY);
        	tmppair.setDescription("OS: FreePhysicalMemorySize");
        	attrlist.addAttributePair(tmppair);
        	// --------------
        	tmppair = new JMXAttribute("AvailableProcessors",Channel.Unit.COUNT);
        	tmppair.setDescription("OS: AvailableProcessors");
        	attrlist.addAttributePair(tmppair);
        }
        // --------------------------------------
/*
MBean Found, Class Name:sun.management.MemoryImpl
	Object Name:java.lang:type=Memory, Information on the management interface of the MBean
		Attribute Name: Verbose, Type:boolean, Description: Verbose
		Attribute Name: HeapMemoryUsage, Type:javax.management.openmbean.CompositeData, Description: HeapMemoryUsage
		Attribute Name: NonHeapMemoryUsage, Type:javax.management.openmbean.CompositeData, Description: NonHeapMemoryUsage
		Attribute Name: ObjectPendingFinalizationCount, Type:int, Description: ObjectPendingFinalizationCount
		Attribute Name: ObjectName, Type:javax.management.ObjectName, Description: ObjectName
 */
        attrlist = new JMXBean(ManagementFactory.MEMORY_MXBEAN_NAME);
        if(attrlist != null){
        	addBeanList(attrlist);
        	
        	tmppair = new JMXAttribute("HeapMemoryUsage.used",Channel.Unit.MEMORY);
        	tmppair.setDescription("JVM: Heap memory[used]");
        	attrlist.addAttributePair(tmppair);
        	// --------------
        	tmppair = new JMXAttribute("HeapMemoryUsage.max",Channel.Unit.MEMORY);
        	tmppair.setDescription("JVM: Heap memory[max]");
        	attrlist.addAttributePair(tmppair);
        	// --------------
        	tmppair = new JMXAttribute("HeapMemoryUsage.init",Channel.Unit.MEMORY);
        	tmppair.setDescription("JVM: Heap memory[init]");
        	attrlist.addAttributePair(tmppair);
        	// --------------
        	tmppair = new JMXAttribute("HeapMemoryUsage.committed",Channel.Unit.MEMORY);
        	tmppair.setDescription("JVM: Heap memory[committed]");
        	attrlist.addAttributePair(tmppair);
        	
        	// --------------
        	tmppair = new JMXAttribute("NonHeapMemoryUsage.used",Channel.Unit.MEMORY);
        	tmppair.setDescription("JVM: Non-heap memory[used]");
        	attrlist.addAttributePair(tmppair);
        	// --------------
        	tmppair = new JMXAttribute("NonHeapMemoryUsage.max",Channel.Unit.MEMORY);
        	tmppair.setDescription("JVM: Non-heap memory[max]");
        	attrlist.addAttributePair(tmppair);
        	// --------------
/*        	tmppair = new AttributePair("max",Channel.Unit.MEMORY);
        	tmppair.setDescription("JVM: Max heap memory");
        	attrlist.addAttributePair(tmppair);
        	// --------------
        	tmppair = new AttributePair("used",Channel.Unit.MEMORY);
        	tmppair.setDescription("JVM: Used heap memory");
        	attrlist.addAttributePair(tmppair);
*/        	
        } else{
        	Logger.log("**** Error: Failed to get JMXBean: "+ManagementFactory.MEMORY_MXBEAN_NAME+"*****\n\n\n");
        }
        // --------------------------------------
        attrlist = new JMXBean(ManagementFactory.THREAD_MXBEAN_NAME);
    	addBeanList(attrlist);
        {
/*
	Object Name:java.lang:type=Threading, Information on the management interface of the MBean
		Attribute Name: ThreadAllocatedMemoryEnabled, Type:boolean, Description: ThreadAllocatedMemoryEnabled
		Attribute Name: ThreadAllocatedMemorySupported, Type:boolean, Description: ThreadAllocatedMemorySupported
		Attribute Name: ThreadContentionMonitoringEnabled, Type:boolean, Description: ThreadContentionMonitoringEnabled
		Attribute Name: DaemonThreadCount, Type:int, Description: DaemonThreadCount
		Attribute Name: PeakThreadCount, Type:int, Description: PeakThreadCount
		Attribute Name: CurrentThreadCpuTimeSupported, Type:boolean, Description: CurrentThreadCpuTimeSupported
		Attribute Name: ObjectMonitorUsageSupported, Type:boolean, Description: ObjectMonitorUsageSupported
		Attribute Name: SynchronizerUsageSupported, Type:boolean, Description: SynchronizerUsageSupported
		Attribute Name: ThreadContentionMonitoringSupported, Type:boolean, Description: ThreadContentionMonitoringSupported
		Attribute Name: ThreadCpuTimeEnabled, Type:boolean, Description: ThreadCpuTimeEnabled
		Attribute Name: AllThreadIds, Type:[J, Description: AllThreadIds
		Attribute Name: CurrentThreadCpuTime, Type:long, Description: CurrentThreadCpuTime
		Attribute Name: CurrentThreadUserTime, Type:long, Description: CurrentThreadUserTime
		Attribute Name: ThreadCount, Type:int, Description: ThreadCount
		Attribute Name: TotalStartedThreadCount, Type:long, Description: TotalStartedThreadCount
		Attribute Name: ThreadCpuTimeSupported, Type:boolean, Description: ThreadCpuTimeSupported
		Attribute Name: ObjectName, Type:javax.management.ObjectName, Description: ObjectName
 */
//        	final long[] deadlocks = mbsc.findMonitorDeadlockedThreads();
        	tmppair = new JMXAttribute("ThreadCount",Channel.Unit.COUNT);
        	tmppair.setDescription("JVM: Live threads");
        	attrlist.addAttributePair(tmppair);
        	// --------------
        	tmppair = new JMXAttribute("PeakThreadCount",Channel.Unit.COUNT);
        	tmppair.setDescription("JVM: Peak threads");
        	attrlist.addAttributePair(tmppair);
        	// --------------
        	tmppair = new JMXAttribute("DaemonThreadCount",Channel.Unit.COUNT);
        	tmppair.setDescription("JVM: Daemon threads");
        	attrlist.addAttributePair(tmppair);
        	// --------------
        	tmppair = new JMXAttribute("TotalStartedThreadCount",Channel.Unit.COUNT);
        	tmppair.setDescription("JVM: Total threads started");
        	attrlist.addAttributePair(tmppair);
        }
        // --------------------------------------
        attrlist = new JMXBean(ManagementFactory.CLASS_LOADING_MXBEAN_NAME);
    	addBeanList(attrlist);
        {
/*
	Object Name:java.lang:type=ClassLoading, Information on the management interface of the MBean
		Attribute Name: LoadedClassCount, Type:int, Description: LoadedClassCount
		Attribute Name: TotalLoadedClassCount, Type:long, Description: TotalLoadedClassCount
		Attribute Name: UnloadedClassCount, Type:long, Description: UnloadedClassCount
		Attribute Name: Verbose, Type:boolean, Description: Verbose
		Attribute Name: ObjectName, Type:javax.management.ObjectName, Description: ObjectName
 */
        	tmppair = new JMXAttribute("TotalLoadedClassCount",Channel.Unit.COUNT);
        	tmppair.setDescription("JVM: Total classes loaded");
        	attrlist.addAttributePair(tmppair);
        	// --------------
        	tmppair = new JMXAttribute("LoadedClassCount",Channel.Unit.COUNT);
        	tmppair.setDescription("JVM: Current classes loaded");
        	attrlist.addAttributePair(tmppair);
        	// --------------
        	tmppair = new JMXAttribute("UnloadedClassCount",Channel.Unit.COUNT);
        	tmppair.setDescription("JVM: Total classes unloaded");
        	attrlist.addAttributePair(tmppair);
        }
    }
    
    // --------------------------------------------------------------------------------------------
    @Override
    public void loadFromJson(JsonObject json)  throws Exception{
    	super.loadFromJson(json);
		if (json.has(SensorConstants.RMIUSERNAME)) {
			setUsername(json.get(SensorConstants.RMIUSERNAME).getAsString());
		}
		if (json.has(SensorConstants.RMIPASSWORD)) {
			setPassword(json.get(SensorConstants.RMIPASSWORD).getAsString());
		}
    	addDefs();
//        setmBean(ManagementFactory.OPERATING_SYSTEM_MXBEAN_NAME);
    }
    // --------------------------------------------------------------------------------------------
}

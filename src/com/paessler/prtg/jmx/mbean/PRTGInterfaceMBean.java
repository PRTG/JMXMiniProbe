package com.paessler.prtg.jmx.mbean;

import javax.management.MXBean;

@MXBean
public interface PRTGInterfaceMBean {
	 // read-only attribute 'ID'
	public long getID();
	  
	  // read-write attribute 'QueryCount'
	public long getQueryCount();
//	public void addQueryCount(long n);
	  
	  // read-write attribute 'AvgExecutionTime'
	public long getAvgExecutionTime();
	public void addExecutionTime(long val);
	  
	  // read-write attribute 'AvgUploadTime'
	public long getAvgUploadTime();
	public void addUploadTime(long val);
	  
	public long getAvgSenorCreationTime();
	public void addSenorCreationTime(long val);

	  // read-write attribute 'SensorCount'
	public int getSensorCount();
	
	  // management operations
	public String printInfo();

}

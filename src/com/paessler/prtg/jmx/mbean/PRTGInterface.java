package com.paessler.prtg.jmx.mbean;

import java.lang.management.ManagementFactory;
import java.lang.reflect.Constructor;

import javax.management.InstanceAlreadyExistsException;
import javax.management.MBeanAttributeInfo;
import javax.management.MBeanConstructorInfo;
import javax.management.MBeanInfo;
import javax.management.MBeanNotificationInfo;
import javax.management.MBeanOperationInfo;
import javax.management.MBeanParameterInfo;
import javax.management.MBeanRegistrationException;
import javax.management.NotCompliantMBeanException;
import javax.management.ReflectionException;
import javax.management.InstanceNotFoundException;
import javax.management.MBeanException;
import javax.management.MBeanServer;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import javax.management.StandardMBean;


//import javax.management.MBeanInfo;
//import javax.management.DynamicMBean;

// http://www.javaworld.com/article/2072243/playing-with-jmx-2-0-annotations.html
// http://stackoverflow.com/questions/20050127/jmx-mbeaninfo-and-descriptors
//@JmxResource(description = "")
//@JmxResource(description = "Paessler PRTG Performance stats", domainName = "j256")
//@JMXBean(description = "My first JMX bean test")
public class PRTGInterface
	extends StandardMBean
	implements PRTGInterfaceMBean{
	
	  private long	id     = System.currentTimeMillis();
	  private String beanName = null;
	  private long	queryCount    	= 0;
	  private long	avgExecutionTime  = 0;
	  private long	avgUploadTime  = 0;
	  private long	avgSenorCreationTime  = 0;
	  private int	sensorCount = 0;

	  public PRTGInterface (String name) throws NotCompliantMBeanException{
		  super(PRTGInterfaceMBean.class);
		  beanName = name;
		  buildMBeanInfo();
	  }
	  
	  public static String getObjectName(String name){
		  return "com.paessler.prtg.jmx.mbean.PRTGBean:name="+name;
	  }
	  
	  public static ObjectName getJMXObjectName(String beanname){
		  ObjectName retVal = null;
		  try {
			  retVal = new ObjectName(beanname);
			} catch (MalformedObjectNameException e) {
			}
		  return retVal;
	  }
	  
	  public String getBeanName()	{return beanName;}
	  public String getObjectName(){
		  return getObjectName(getBeanName());
	  }
	@Override
	public long getID() {
		// TODO Auto-generated method stub
		return 0;
	}

	public void setQueryCount(long val) {queryCount = val;}
	@Override
	public long getQueryCount() {
		// TODO Auto-generated method stub
		return queryCount;
	}
	  // --------------------------------------------------------------
	public long addTime(long val, long add) {
		long retVal = val;
		if(getQueryCount() < 2){
			retVal = add;
			setQueryCount(1);
		} else if(getQueryCount() == 2){
			retVal = (retVal + add)/2;
		} else {
			// http://stackoverflow.com/questions/12636613/how-to-calculate-moving-average-without-keeping-the-count-and-data-total
			long n = getQueryCount();
			long newval = add/n;
			long rem = (long)(retVal * ((double)(n-1)/n));
			retVal = rem + newval;
		}
		return retVal;
	}

	  // --------------------------------------------------------------
//	@Override
	public void addQueryCount(long n) {	queryCount += n;}

	  // --------------------------------------------------------------
	@Override
	public long getAvgExecutionTime() {	return avgExecutionTime;}

	@Override
	public void addExecutionTime(long val) {
		avgExecutionTime = addTime(avgExecutionTime, val);
	}
	// --------------------------------------------------------------
	// read-write attribute 'AvgUploadTime'
	public long getAvgUploadTime(){ return avgUploadTime; }
	@Override
	public void addUploadTime(long val) {
		avgUploadTime = addTime(avgUploadTime, val);
	}

	// --------------------------------------------------------------
	// read-write attribute 'AvgUploadTime'
	public long getAvgSenorCreationTime(){ return avgSenorCreationTime; }
	@Override
	public void addSenorCreationTime(long val) {
		avgSenorCreationTime = addTime(avgSenorCreationTime, val);
	}
	
	  // read-write attribute 'SensorCount'
	public int getSensorCount() {return sensorCount;};
	public void setSensorCount(int val) {sensorCount = val;};
	
	@Override
	public String printInfo() {
		// TODO Auto-generated method stub
		return "Query Count: " + getQueryCount()
				+"\nAvgExecutionTime: "+getAvgExecutionTime()+"ms";
	}

	public static String getDefaultBeanName(){ return "JMXMiniProbe";}
	
	public static PRTGInterface getBean(String beanname){
		   PRTGInterface retVal = null;
		   if(retVal == null){
			   try {
				retVal = new PRTGInterface(beanname);
			} catch (NotCompliantMBeanException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		   } // if
		   return retVal;
	}
	public static PRTGInterface getAndRegisterBean(String beanname){
		   // Find an agent from this JVM. Null argument will return
		   // a list of all MBeanServer instances.
		   MBeanServer server =ManagementFactory.getPlatformMBeanServer();
		   
		   ObjectName name = getJMXObjectName(getObjectName(beanname));
		   PRTGInterface retVal = getBean(beanname);
		   try {    
			   if(server.isRegistered(name)){
				   server.unregisterMBean(name);
			   }
		     // register the MBean
		     server.registerMBean(retVal, name);

		     // Invoke the printInfo operation on an
		     // uninitialized MBean instance.
		     Object result = server.invoke(
		              name,     // MBean name
		              "printInfo",  // operation name
		              null,     // no parameters
		              null      // void signature
		             );
		   }
		   catch (InstanceNotFoundException e) {
		     e.printStackTrace();
		   }
		   catch (MBeanException e) {
		     e.getTargetException().printStackTrace();
		   }
		   catch (ReflectionException e) {
		     e.printStackTrace();
		   }
		   catch (InstanceAlreadyExistsException e) {
		     e.printStackTrace();
		   }
		   catch (NotCompliantMBeanException e) {
		     e.printStackTrace();
		   }
		   return retVal;
	}
	
	
	
	public static void unregisterBean(PRTGInterface bean){
	        final MBeanServer server = ManagementFactory.getPlatformMBeanServer();
	        try {
	            server.unregisterMBean(new ObjectName(bean.getObjectName()));
	        } catch (MalformedObjectNameException mone) {
	            mone.printStackTrace();
	        } catch (MBeanRegistrationException mbre) {
	            mbre.printStackTrace();
	        } catch (InstanceNotFoundException infe) {
	            infe.printStackTrace();
	        } 
	    } 
    /**
     * This method provides the exposed attributes and operations of the
     * Dynamic MBean. It provides this information using an MBeanInfo object.
     */
    public MBeanInfo getMBeanInfo() {

        // Return the information we want to expose for management:
        // the dMBeanInfo private field has been built at instanciation time
        //
        return dMBeanInfo;
    }
    /*
     * -----------------------------------------------------
     * PRIVATE METHODS
     * -----------------------------------------------------
     */
  
    /**
     * Build the private dMBeanInfo field,
     * which represents the management interface exposed by the MBean,
     * that is, the set of attributes, constructors, operations and
     * notifications which are available for management. 
     *
     * A reference to the dMBeanInfo object is returned by the getMBeanInfo()
     * method of the DynamicMBean interface. Note that, once constructed, an
     * MBeanInfo object is immutable.
     */
    private void buildMBeanInfo() {

        dAttributes[0] =
            new MBeanAttributeInfo("QueryCount",
                                   "java.lang.long",
                                   "QueryCount, Number of queries run.",
                                   true,
                                   true,
                                   false);
        dAttributes[1] =
            new MBeanAttributeInfo("AvgExecutionTime",
				                    "java.lang.long",
				                    "AvgExecutionTime, Average query Execution Time[ms].",
                                   true,
                                   false,
                                   false);
        dAttributes[2] =
                new MBeanAttributeInfo("AvgUploadTime",
    				                    "java.lang.long",
    				                    "avgUploadTime, Average time to upload data Time[ms].",
                                       true,
                                       false,
                                       false);
        dAttributes[3] =
                new MBeanAttributeInfo("AvgSenorCreationTime",
    				                    "java.lang.long",
    				                    "AvgSenorCreationTime, Average sensor creation Execution.",
                                       true,
                                       false,
                                       false);

        dAttributes[4] =
        new MBeanAttributeInfo("sensorCount",
			                    "java.lang.int",
			                    "SensorCount, Number of sensors.",
                               true,
                               false,
                               false);

        
        Constructor[] constructors = this.getClass().getConstructors();
        dConstructors[0] =
            new MBeanConstructorInfo("Constructs a " +
                                     "PRTGInterface object",
                                     constructors[0]);

        MBeanParameterInfo[] params = new MBeanParameterInfo[1];
        params[0] = new MBeanParameterInfo("val", "java.lang.long", "Last execution time[ms] to add");
        dOperations[0] =
            new MBeanOperationInfo("addExecutionTime",
                                   "addExecutionTime, Add execution run stats",
                                   params , 
                                   "void", 
                                   MBeanOperationInfo.ACTION);

//        dNotifications[0] =
//            new MBeanNotificationInfo(
//            new String[] { AttributeChangeNotification.ATTRIBUTE_CHANGE },
//            AttributeChangeNotification.class.getName(),
//            "This notification is emitted when the reset() method is called.");

        dMBeanInfo = new MBeanInfo(dClassName,
                                   dDescription,
                                   dAttributes,
                                   dConstructors,
                                   dOperations,
                                   dNotifications);
    }

    /*
     * -----------------------------------------------------
     * PRIVATE VARIABLES
     * -----------------------------------------------------
     */

    private String dClassName = this.getClass().getName();
    private String dDescription = "JMX: PRTG JMXMiniProbe statistics";

    private MBeanAttributeInfo[] dAttributes =
        new MBeanAttributeInfo[5];
    private MBeanConstructorInfo[] dConstructors =
        new MBeanConstructorInfo[1];
    private MBeanNotificationInfo[] dNotifications =
        new MBeanNotificationInfo[1];
    private MBeanOperationInfo[] dOperations =
        new MBeanOperationInfo[1];
    private MBeanInfo dMBeanInfo = null;
	
}

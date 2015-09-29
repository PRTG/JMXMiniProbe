package com.paessler.prtg.util;

import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.io.StringWriter;
import java.lang.reflect.Field;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.Properties;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.StringTokenizer;

import com.paessler.prtg.jmx.Logger;
import com.paessler.prtg.util.concurrency.ThreadUtility;
import com.paessler.prtg.util.io.FileUtility;
import com.paessler.prtg.util.net.Net_Const;
/******************************************************************************
 *  A library of static System utility functions
 *  @author JR Andreassen
 *  @version 0.1
 *****************************************************************************/
public abstract class SystemUtility
{

      /** Constant (Thread) Sleep Interval : 1 second. */
	public static final int	SLEEP_INTERVAL_ONE_SECOND = 1000;
      /** Constant (Thread) Sleep Interval : .1 second. */
	public static final int	SLEEP_INTERVAL_DECI_SECOND = 100;
      /** Constant (Thread) Sleep Interval : .01 second. */
	public static final int	SLEEP_INTERVAL_CENTI_SECOND = 10;

      /** Constant File suffix : Out  */
	public static final String	FILE_SUFFIX_OUTPUT	= ".out";
      /** Constant File suffix : Error  */
	public static final String	FILE_SUFFIX_ERROR 	= ".err";
	
	//////////////////////////////////////////////////////////
	
	/** 
	 *	<pre>
	 *	This 32 bit int value can be used with a bit mask 
	 *	to specify application versions. Each used bit should be documented here
	 *	as to the meaning, and also have a mask defined. 
	 *	bit 0  - undefined
	 *	bit 1  - undefined
	 *	bit 2 - see VERSION_MASK_POSTDATAVALUE
	 *	bit 3  - undefined
	 *	bit 4  - undefined
	 *	bit 5  - undefined
	 *	bit 6  - undefined
	 *	bit 7  - undefined
	 *	bits 8-31  - undefined
	 *	</pre>
	 */
	public static final int FRAMEWORK_VERSION = 4;
	
	/** bit mask value specifying post data value version of code */ 
	public static final int VERSION_MASK_POSTDATAVALUE = 4;
	
	/** contains the path name to an "SDI_Apps" directory under the users home directory */
	public static final String SDI_USER_DIRECTORY = getUserHomeDir() + File.separatorChar + "SDI_Apps";
	
	/** Constant for System Property : System Property file name */
	public static final String	PROP_FILE_NAME_SYS_PROPERTY				= "System.properties";
	
	/** Constant for System Property : File separator ("/" on Unix) */
	public static final String	SYS_PROPERTY_FILE_SEPARATOR	= "file.separator";
	
	/** Constant for System Property : Line Separator */
	public static final String	SYS_PROPERTY_LINE_SEPARATOR				= "line.separator";

	/** Constant for System Property : Path Separator */
	public static final String	SYS_PROPERTY_PATH_SEPARATOR				= "file.separator";


	/** Constant for System Property : Temp directory */
	public static final String	SYS_PROPERTY_TEMP_DIR							= "java.io.tmpdir";

	/** Constant for System Property : Operating System Architecture {x86|ppc|mips|sparc|...} */
	public static final String	SYS_PROPERTY_OS_ARCHITECTURE			= "os.arch";

		/** Constant for System Property : Operating System Name {%AIX%|WINDOWS|LINUX|...} */
	public static final String	SYS_PROPERTY_OS_NAME							= "os.name";
	
	/** Constant for System Property : Operating System Version */
	public static final String	SYS_PROPERTY_OS_VER								= "os.version";

      /** Constant File suffix : Unix: Environment Substitution Charcter */
	public static final String	ENV_VAR_SUBST_CHAR_xNIX 	= "$";
      /** Constant File suffix : Windows: Environment Substitution Charcter */
	public static final String	ENV_VAR_SUBST_CHAR_WIN 	= "%";

      /** Constant File suffix : Windows: Environment Substitution Charcter */
	public static String	ENV_VAR_SUBST_CHAR 	= ENV_VAR_SUBST_CHAR_WIN;

	/**
	 * OS Specific Env Subst string
	 * @param varname
	 * @return
	 */
	public static String makeEnvSubstString(String varname)
	{
		// Windows "%varname%"
		return ENV_VAR_SUBST_CHAR_WIN+varname+ENV_VAR_SUBST_CHAR_WIN;
		// Unix "$(varname)"
	}


	/** Constant for System Property : OS Arc {8(?)|16(?)|32|64|...} */
	public static final String	SYS_PROPERTY_OS_ARC_DATA_MODEL		= "sun.arch.data.model";
	

	/** Constant for System Property : Endian {little|big} */
	public static final String	SYS_PROPERTY_ENCODING_CPU_ENDIAN	= "sun.cpu.endian";

	/** Constant for System Property : UniCode Endian {UnicodeBig|UnicodeLittle} */
	public static final String	SYS_PROPERTY_ENCODING_UNICO_ENDIAN= "sun.io.unicode.encoding";



	// ---------------------------------------------------------------------
	// Proxy related Properties
	/** Constant for System Property : Proxy SOCKS Host  @see http://java.sun.com/j2se/1.4.1/docs/guide/net/properties.html */
	public static final String	SYS_PROPERTY_PROXY_SOCKS_HOST			= "socksProxyHost";
	/** Constant for System Property : Proxy SOCKS Port  @see http://java.sun.com/j2se/1.4.1/docs/guide/net/properties.html */
	public static final String	SYS_PROPERTY_PROXY_SOCKS_PORT			= "socksProxyPort";
	/** Constant for System Property : Proxy SOCKS Password  */
	public static final String	SYS_PROPERTY_PROXY_SOCKS_PWD			= "java.net.socks.password";
	/** Constant for System Property : Proxy SOCKS User Name */
	public static final String	SYS_PROPERTY_PROXY_SOCKS_UID			= "java.net.socks.username";

	/** Constant for System Property : Proxy FTP Host (default: <none>)  @see http://java.sun.com/j2se/1.4.1/docs/guide/net/properties.html */
	public static final String	SYS_PROPERTY_PROXY_FTP_HOST			= "ftp.proxyHost";
	/** Constant for System Property : Proxy FTP Port (default: 80 if ftp.proxyHost specified)  @see http://java.sun.com/j2se/1.4.1/docs/guide/net/properties.html */
	public static final String	SYS_PROPERTY_PROXY_FTP_PORT			= "ftp.proxyPort";
	/** Constant for System Property : Proxy FTP Port (default: <none>)  @see http://java.sun.com/j2se/1.4.1/docs/guide/net/properties.html */
	public static final String	SYS_PROPERTY_PROXY_FTP_NON_HOSTS= "ftp.nonProxyHosts";

	/** Constant for System Property : Proxy FTP Host (default: <none>)  @see http://java.sun.com/j2se/1.4.1/docs/guide/net/properties.html */
	public static final String	SYS_PROPERTY_PROXY_HTTP_HOST		= "http.proxyHost";
	/** Constant for System Property : Proxy FTP Port (default: 80 if http.proxyHost specified)  @see http://java.sun.com/j2se/1.4.1/docs/guide/net/properties.html */
	public static final String	SYS_PROPERTY_PROXY_HTTP_PORT		= "http.proxyPort";
	/** Constant for System Property : Proxy FTP Port (default: <none>)  @see http://java.sun.com/j2se/1.4.1/docs/guide/net/properties.html */
	public static final String	SYS_PROPERTY_PROXY_HTTP_NON_HOSTS	= "http.nonProxyHosts";

	/** Constant for System Property : Proxy HTTP Validate Server (default: false)  @see http://java.sun.com/j2se/1.4.1/docs/guide/net/properties.html */
	public static final String	SYS_PROPERTY_PROXY_HTTP_AUTH_VALID_SRV	= "http.auth.digest.validateServer";
	/** Constant for System Property : Proxy HTTP Validate Proxy (default: false)  @see http://java.sun.com/j2se/1.4.1/docs/guide/net/properties.html */
	public static final String	SYS_PROPERTY_PROXY_HTTP_AUTH_VALID_PROXY	= "http.auth.digest.validateProxy";
	/** Constant for System Property : Proxy HTTP how many times a cnonce value is reused (default: 5)  @see http://java.sun.com/j2se/1.4.1/docs/guide/net/properties.html */
	public static final String	SYS_PROPERTY_PROXY_HTTP_AUTH_VALID_CNCRPT	= "http.auth.digest.cnonceRepeat";
 

/**
	* The host may be any of: 
	* a complete host name (e.g. "www.disney.com") 
	* a domain name; domain names must begin with a dot (e.g. ".disney.com") 
	* an IP-address (e.g. "12.34.56.78") 
	* an IP-subnet, specified as an IP-address and a netmask separated by a "/" (e.g. "34.56.78/255.255.255.192"); a 0 bit in the netmask means that that bit won't be used in the comparison (i.e. the addresses are AND'ed with the netmask before comparison). 
	* 
  * nonProxyHosts format: <item>{<sep><item>{<sep>...}}
  * where <sep> = '|'
  * <item> = <host>, <domain> or <IP address>
  */  
/*
// Windows
System Props..
java.runtime.name=Java(TM) 2 Runtime Environment, Standard Edition
sun.boot.library.path=C:\java\jdk14\jre\bin
java.vm.version=1.4.1_01-b01
java.vm.vendor=Sun Microsystems Inc.
java.vendor.url=http://java.sun.com/
path.separator=;
java.vm.name=Java HotSpot(TM) Client VM
file.encoding.pkg=sun.io
user.country=US
sun.os.patch.level=Service Pack 1
java.vm.specification.name=Java Virtual Machine Specification
user.dir=c:\j_projects\test
java.runtime.version=1.4.1_01-b01
java.awt.graphicsenv=sun.awt.Win32GraphicsEnvironment
java.endorsed.dirs=C:\java\jdk14\jre\lib\endorsed
os.arch=x86
os.version=5.1
java.io.tmpdir=c:\TEMP\
line.separator=

java.vm.specification.vendor=Sun Microsystems Inc.
user.variant=
os.name=Windows XP
sun.java2d.fontpath=
java.library.path=C:\java\jdk14\bin;.;C:\WINDOWS\System32;C:\WINDOWS;C:\WINDOWS\system32;C:\WINDOWS;C:\WINDOWS\System32\Wbem;C:\MSSQL7\BINN;C:\SNA\system;C:\Program Files\Common Files\Adaptec Shared\System;C:\MSSQL65\BINN;C:\java\jdk14\bin;C:\ORANT\BIN;C:\IBM\DB2_81\SQLLIB\BIN;C:\IBM\DB2_81\SQLLIB\FUNCTION;C:\Program Files\Hummingbird\Connectivity\7.10\Accessories\;C:\c_projects\ACE_wrappers\bin;C:\MSDEV\Common\Tools\WinNT;C:\MSDEV\Common\MSDev98\Bin;C:\MSDEV\Common\Tools;C:\MSDEV\VC98\bin
java.specification.name=Java Platform API Specification
java.class.version=48.0
java.util.prefs.PreferencesFactory=java.util.prefs.WindowsPreferencesFactory
user.home=C:\Documents and Settings\JA01058
user.timezone=
java.awt.printerjob=sun.awt.windows.WPrinterJob
file.encoding=Cp1252
java.specification.version=1.4
user.name=ja01058
java.class.path=C:\J_PROJECTS\test\data;C:\J_PROJECTS\TEST\classes;.;C:\IBM\DB2_81\SQLLIB\java\db2java.zip;C:\IBM\DB2_81\SQLLIB\java\db2jcc.jar;C:\IBM\DB2_81\SQLLIB\bin;C:\IBM\DB2_81\SQLLIB\java\common.jar;c:\java\kawa\kawaclasses.zip;c:\java\jdk14\lib\tools.jar;c:\java\jdk14\jre\lib\rt.jar
java.vm.specification.version=1.0
sun.arch.data.model=32
java.home=C:\java\jdk14\jre
java.specification.vendor=Sun Microsystems Inc.
user.language=en
awt.toolkit=sun.awt.windows.WToolkit
java.vm.info=mixed mode
java.version=1.4.1_01
java.ext.dirs=C:\java\jdk14\jre\lib\ext
sun.boot.class.path=C:\java\jdk14\jre\lib\rt.jar;C:\java\jdk14\jre\lib\i18n.jar;C:\java\jdk14\jre\lib\sunrsasign.jar;C:\java\jdk14\jre\lib\jsse.jar;C:\java\jdk14\jre\lib\jce.jar;C:\java\jdk14\jre\lib\charsets.jar;C:\java\jdk14\jre\classes
java.vendor=Sun Microsystems Inc.
file.separator=\
java.vendor.url.bug=http://java.sun.com/cgi-bin/bugreport.cgi
sun.cpu.endian=little
sun.io.unicode.encoding=UnicodeLittle
sun.cpu.isalist=pentium i486 i386
//-----------------------------------------------------------
// AIX
java.assistive=ON
java.runtime.name=Java(TM) 2 Runtime Environment, Standard Edition
sun.boot.library.path=/usr/java/jre/bin
java.vm.version=1.4.0
java.vm.vendor=IBM Corporation
java.vendor.url=http://www.ibm.com/
path.separator=:
java.vm.name=Classic VM
file.encoding.pkg=sun.io
user.country=US
sun.os.patch.level=unknown
java.vm.specification.name=Java Virtual Machine Specification
user.dir=/export/home/ja01058
java.runtime.version=1.4.0
java.fullversion=J2RE 1.4.0 IBM AIX 5L for PowerPC (64 bit JVM) build caix641401-20021126 (JIT enabled: jitc)
java.awt.graphicsenv=sun.awt.X11GraphicsEnvironment
java.endorsed.dirs=/usr/java/jre/lib/endorsed
os.arch=ppc64
java.io.tmpdir=/tmp/
line.separator=

java.vm.specification.vendor=Sun Microsystems Inc.
user.variant=
java.awt.fonts=
os.name=AIX
sun.java2d.fontpath=
java.library.path=/usr/java14_64/jre/bin:/usr/java14_64/jre/bin/classic:/usr/java14_64/jre/bin:/usr/lib
java.specification.name=Java Platform API Specification
java.class.version=48.0
ibm.system.encoding=ISO8859-1
java.util.prefs.PreferencesFactory=java.util.prefs.FileSystemPreferencesFactory
invokedviajava=
os.version=5.2
user.home=/export/home/ja01058
user.timezone=
java.awt.printerjob=sun.print.PSPrinterJob
java.specification.version=1.4
file.encoding=ISO8859-1
user.name=ja01058
java.class.path=$DB2DIR/tools/dxxadmin.jar:$DB2DIR/tools/db2cmn.jar:$DB2DIR/java/db2java.zip:$DB2DIR/java/db2jcc.jar:$DB2DIR/java/sqlj.zip:$DB2DIR/sqllib/function:/usr/java_jars:.
java.vm.specification.version=1.0
sun.arch.data.model=64
java.home=/usr/java/jre
java.specification.vendor=Sun Microsystems Inc.
user.language=en
java.vm.info=J2RE 1.4.0 IBM AIX 5L for PowerPC (64 bit JVM) build caix641401-20021126 (JIT enabled: jitc)
java.version=1.4.0
java.ext.dirs=/usr/java/jre/lib/ext
sun.boot.class.path=/usr/java/jre/lib/core.jar:/usr/java/jre/lib/graphics.jar:/usr/java/jre/lib/security.jar:/usr/java/jre/lib/server.jar:/usr/java/jre/lib/xml.jar:/usr/java/jre/lib/charsets.jar:/usr/java/jre/classes
java.vendor=IBM Corporation
file.separator=/
java.vendor.url.bug=
java.compiler=jitc
sun.io.unicode.encoding=UnicodeBig
*/
//	http://stackoverflow.com/questions/1272648/reading-my-own-jars-manifest	
	/**
	 * Get current Java Spec as an integer.
	 */
	public static void dumpSystemProperties()
	{
			Properties prop = System.getProperties();
			prop.list(System.out);
	}	// dumpSystemProperties

	/** Constant for System Property : Java version number */
	public static final String	SYS_PROPERTY_JAVA_VER	= "java.version";
 	/** Constant for System Property : Java vendor specific string */
	public static final String	SYS_PROPERTY_JAVA_VENDOR_VENDOR	= "java.vendor";
	/** Constant for System Property : Java vendor URL */
	public static final String	SYS_PROPERTY_JAVA_VENDOR_URL	= "java.vendor.url";
	/** Constant for System Property : Java installation directory*/
	public static final String	SYS_PROPERTY_JAVA_HOME	= "java.home";
	/** Constant for System Property : Java class version number */
	public static final String	SYS_PROPERTY_CLASS_VER	= "java.class.version";
	/** Constant for System Property : Java classpath */
	public static final String	SYS_PROPERTY_CLASS_PATH	= "java.class.path";
	/** Constant for System Property : Java library path */
	public static final String	SYS_PROPERTY_LIB_PATH	= "java.library.path";
	/** Constant for System Property : Java library path */
	public static final String	SYS_PROPERTY_PATH_SEP	= "path.separator";
	
	//////////////////////////////////////////////////////////
	/** Constant for System Property : System JMXPort */
	public static final String	SYS_PROPERTY_JMX_REMOTE_PORT	= "com.sun.management.jmxremote.port";

	//////////////////////////////////////////////////////////
	/** Constant for System Property : Prefix for user properties */
	public static final String	SYS_PROPERTY_USER_PREFIX		= "user";
	/** Constant for System Property : Username */
	public static final String	SYS_PROPERTY_USER_COUNTRY		= "user.country";
	/** Constant for System Property : User Language */
	public static final String	SYS_PROPERTY_USER_LANGUAGE	= "user.language";
	/** Constant for System Property : User Time Zone */
	public static final String	SYS_PROPERTY_USER_TIMEZONE	= "user.timezone";
	
	/** Constant for System Property : Username */
	public static final String	SYS_PROPERTY_USER_NAME			= "user.name";
	/** Constant for System Property : User Current directory */
	public static final String	SYS_PROPERTY_USER_DIR				= "user.dir";
	/** Constant for System Property : User Home Directory */
	//public static final String	SYS_PROPERTY_USER_HOME			= "user.home";
	/** Constant for System Property : Username */
	public static final String	SYS_PROPERTY_USER_HOSTNAME	= "user.hostName";
	/** Constant for System Property : Local Printer */
	public static final String	SYS_PROPERTY_USER_LOCALPRT	= "user.LocalPrinters";
	/** Constant for System Property : Action Menu */
	public static final String	SYS_PROPERTY_USER_ACTOION_MENU= "user.actionMenu";
	/** Constant for System Property : Open Menu */
	public static final String	SYS_PROPERTY_USER_OPEN_MENU	= "user.openMenu";
	/** Constant for System Property : Record Fetch Size */
	public static final String	SYS_PROPERTY_USER_REC_FETCH_SZ= "user.recordsFetchSize";
	
	/** Constant for System Property : Display Delay, before showing Panel */
	public static final String	SYS_PROPERTY_USER_DISPLAY_DLY	= "user.displayDelay";

	/** Constant for System Property : Application Size X */
	public static final String	SYS_PROPERTY_USER_APP_SIZE_Y	= "user.appSizeY";
	/** Constant for System Property : Application Size Y */
	public static final String	SYS_PROPERTY_USER_APP_SIZE_X	= "user.appSizeX";

	/** Constant for System Property : Window Size X */
	public static final String	SYS_PROPERTY_USER_WIN_SIZE_Y	= "user.appWindowSizeY";
	/** Constant for System Property : Window Size Y */
	public static final String	SYS_PROPERTY_USER_WIN_SIZE_X	= "user.appWindowSizeX";

	
	static
	{
     /** Constant File suffix : Windows: Environment Substitution Charcter */
		ENV_VAR_SUBST_CHAR 	= ENV_VAR_SUBST_CHAR_WIN;

	}

	//////////////////////////////////////////////////////////
	/** Flag for Daemon / background processes */
	private static boolean daemonProcess = false;
	
	////////////////////////////////////////////////////////////
	/**
	 * Reset Lib Path
	 * @param add2path
	 * @see http://blog.cedarsoft.com/2010/11/setting-java-library-path-programmatically/
	 */
	public static void addToLibPath(String add2path)
	{	
		if(add2path == null || add2path.isEmpty())
		{	return;}
		String pathsep = System.getProperty(SYS_PROPERTY_PATH_SEP);
		String savepath = System.getProperty(SYS_PROPERTY_LIB_PATH);
		StringBuilder newpath = new StringBuilder(savepath);
		// Check for Separator ?
		newpath.append(pathsep);
		
//		if(newpath.indexOf(""))
			newpath.append(add2path);
			
		System.out.println("Resetting LIB Path to ["+newpath+"]");
		System.setProperty( SYS_PROPERTY_LIB_PATH, newpath.toString()); 
		Field fieldSysPath;
			try
			{
				fieldSysPath = ClassLoader.class.getDeclaredField( "sys_paths" );
				fieldSysPath.setAccessible( true );
				fieldSysPath.set( null, null );
			}
			catch(NoSuchFieldException e)
			{
				// TODO Auto-generated catch block
				System.out.println("COUGHT [NoSuchFieldException]: "+e.getMessage());
			}
			catch(SecurityException e)
			{
				// TODO Auto-generated catch block
				System.out.println("COUGHT [SecurityException]: "+e.getMessage());
			}
			catch(IllegalArgumentException e)
			{
				// TODO Auto-generated catch block
				System.out.println("COUGHT [IllegalArgumentException]: "+e.getMessage());
			}
			catch(IllegalAccessException e)
			{
				// TODO Auto-generated catch block
				System.out.println("COUGHT [IllegalAccessException]: "+e.getMessage());
			}
	}
	
	
	/**
	* Adds the specified path to the java library path
	* http://fahdshariff.blogspot.be/2011/08/changing-java-library-path-at-runtime.html
	* @param pathToAdd the path to add
	* @throws Exception
	*/
	public static void addLibraryPathToUsrPaths(String pathToAdd) throws Exception
	{
		final Field usrPathsField = ClassLoader.class.getDeclaredField("usr_paths");
		usrPathsField.setAccessible(true);

		//get array of paths
		final String[] paths = (String[]) usrPathsField.get(null);

		//check if the path to add is already present
		for( String path : paths )
		{
			//System.err.println("path->" + path);
			if( path.equals(pathToAdd) )
				return;
		}

		//add the new path
		final String[] newPaths = Arrays.copyOf(paths, paths.length + 1);
		newPaths[newPaths.length - 1] = pathToAdd;
		usrPathsField.set(null, newPaths);
	}
	
	/**
	 * adds the path to jars/winjni/amd64 or jars/winjni/x86 depending on the jvm version
	 * this is added to the classloaders usr_paths which should allow native dlls to be loaded from there
	 */
	public static void addSdiWinJniPath()
	{
		//System.err.println(System.getProperty("os.arch")); -> amd64
		//System.err.println(System.getProperty("sun.arch.data.model")); -> 64
		//NOTE: os.arch is supposed to contain the JVM version, not the actual OS version
		String winJniPath = "jars/winjni/" + System.getProperty("os.arch");
		try
		{
			SystemUtility.addLibraryPathToUsrPaths(winJniPath);
		}
		catch(Exception e)
		{
			System.err.println("e->" + e + ", winJniPath->" + winJniPath);
			e.printStackTrace();
		}
	}
	
	////////////////////////////////////////////////////////////
	/**
	 * Construct property Key from parts
	 * @param 	key 		- the property key of the property to get
	 * @param prefix1 	- the property prefix of the property to get
	 * @param prefix2 	- the property prefix of the property to get
	 * @param separator	- property separator
	 * @return String	- the property value
	 */
	public static String makePropertyKey(String prefix, String key, String separator )
	{	String retVal = key;
		StringBuffer retBuff = new StringBuffer();
		boolean needSep = false;
		if(	prefix != null && prefix.length() > 0)
	 	{ retBuff.append(prefix);	
			needSep = true;	
		}	
		
		if (key != null	&& key.length() > 0)
		{	
			if (needSep)
			{ retBuff.append(separator);}
			retBuff.append(key);
		}
		if (retBuff.length() > 0)
		{	retVal = retBuff.toString();
		}
//System.out.println("DBSession.makePropertyKey("+prefix+", "+key+") => "+retVal);
		return retVal;
	} // makePropertyKey

	
	//////////////////////////////////////////////////////////
	/**
	 * Determine wether program is a daemon/background process
	 * @param on	Flag
	 */
	public static final void setDaemonProcess(boolean on)
	{	daemonProcess = on;}
	//////////////////////////////////////////////////////////
	/**
	 * Determine wether program is a daemon/background process
	 * @return boolean	true / false
	 */
	public static final boolean isDaemonProcess()
	{	return daemonProcess;}
	
	
	//////////////////////////////////////////////////////////
	
	public static final String getArchitectureBits()
	{
		return System.getProperty("sun.arch.data.model");
	}
	//////////////////////////////////////////////////////////
//	java.specification.version=1.4
	public static String SYSTEM_JAVA_SPEC_PROPERTY = "java.specification.version";
	public static int JAVASPEC_VERSION_1_1	= 1180;
	public static int JAVASPEC_VERSION_1_2	= 1200;
	public static int JAVASPEC_VERSION_1_4	= 1400;
	public static int JAVASPEC_VERSION_1_5	= 1500;
	public static int JAVASPEC_VERSION_1_6	= 1600;
	public static int JAVASPEC_VERSION_1_7	= 1700;
	private static int JAVASPEC_MULTIPLIER	= 1000;
	private static int javaSpecVersion = -1;
	private static int javaSubVersion = -1;

	public static String getJavaVersionString()
	{
		Properties prop = System.getProperties();
		String str = prop.getProperty("java.version");
		return str;
	}

// SystemUtility.getJavaSpecVersion() >= SystemUtility.JAVASPEC_VERSION_1_4	
	/**
	 * Get current Java Spec as an integer.
	 */
	public static int getJavaSpecVersion()
	{
		if (javaSpecVersion == -1)
		{	Properties prop = System.getProperties();
			String str = prop.getProperty(SYSTEM_JAVA_SPEC_PROPERTY);
			if (str != null)
			{ Float tmpFlt = new Float(1.0);
				try
				{	tmpFlt = Float.valueOf(str);
				}
				catch(Exception e)		{	}
				javaSpecVersion = (int) (tmpFlt.floatValue() * JAVASPEC_MULTIPLIER);
			}
		}
		return javaSpecVersion;
	}	// getJavaSpecVersion

	//////////////////////////////////////////////////////////
	/**
	 * Check if the System JDK Level si at least minlevel
	 * @param minlevel	Minimum JDK Level
	 * @return	True / false
	 */
	public static boolean isJavaSpecVersion(int minlevel)
	{
		boolean retVal = false;
		if( getJavaSpecVersion() >= minlevel)
		{retVal = true;}
		return retVal;
	}

	public static int getJavaSubVersion()
	{
		if (javaSubVersion == -1)
		{
			String str = getJavaVersionString();
			int underscoreIndex = str.indexOf('_');
			String subVerStr = str.substring(underscoreIndex+1);
			//System.err.println("subVerStr->" + subVerStr + "<");

			javaSubVersion = NumberUtility.convertToInt(subVerStr, -99);
			if( javaSubVersion == -99 )
			{
				System.err.println("cannot determine java subversion for->" + str);
				javaSubVersion = 0;
			}
		}
		return javaSubVersion;
	}	// getJavaSpecVersion

	//////////////////////////////////////////////////////////
	/**
	 * Sound system bell
	 */
	public static void beep()
	{	java.awt.Toolkit.getDefaultToolkit().beep();
	}

	
	// **********************************************************************
	// **   Error redirect  
	// **********************************************************************
	// ----------------------------

	// ----------------------------
	/**
	 * Redirect Standard output
	 * @param prtstream		Print stream to redirect to 
	 */
	public static boolean redirectError(PrintStream prtstream)
	{
		boolean retVal = true;
		// System.setOut(PrintStream out) 
		// PrintStream(OutputStream out, boolean autoFlush) 
    // FileOutputStream(String name, boolean append)
		try
		{
			System.setErr(prtstream);
		}
		catch(Exception e)
		{	
			Logger.log("Unable to redirect error to PrintStream "+e);
			retVal = false;
		}
		return retVal;
	} // redirectError
	
	// **********************************************************************
	// **   Output redirect  
	// **********************************************************************
	// ----------------------------


	// ----------------------------
	/**
	 * Redirect Standard output
	 * @param prtstream		Print stream to redirect to 
	 */
	public static boolean redirectOutput(PrintStream prtstream)
	{
		boolean retVal = true;
		// System.setOut(PrintStream out) 
		// PrintStream(OutputStream out, boolean autoFlush) 
    // FileOutputStream(String name, boolean append)
		try
		{	
			
			System.setOut(prtstream);
		}
		catch(Exception e)
		{	
			Logger.log("Unable to redirect error to PrintStream "+e);
			retVal = false;
		}
		return retVal;
	} // redirectOutput
	
// ----------------------------
 /**
  * Load system properties from a a bundle
	* @param res	Resource Bundle to load from
	*/
	public static boolean loadSyspropeties(ResourceBundle res)
	{
		boolean retVal = true;
			if (res != null)
			{  
				Enumeration<?> e = res.getKeys();
				String 	theKey 	= null;
				String	value		= null;
				while(e.hasMoreElements()) 
				{ 	theKey 	= (String) e.nextElement();
						value		= res.getString(theKey);
//Log.printDebug(theKey+"= "+value);		
				  	System.setProperty(theKey, value);
				}
				retVal = true;
			} // if
		return retVal;
	} // loadSyspropeties

// ----------------------------
	private static String theHostName = null;
// ----------------------------
 /**
  * Get the host name
	* @param String 	Name of the machine were running on
	*/
	public static String getHostName()
	{
		if(theHostName == null)
		{
			theHostName = Net_Const.NET_LOCAL_HOST;
			try
			{
				String tmp = java.net.InetAddress.getLocalHost().toString();
				StringTokenizer theTokenizer = new StringTokenizer(tmp, "/");
				theHostName =  theTokenizer.nextToken();
			}
			catch(UnknownHostException e)
			{
				Logger.log("Unable to set user.hostName");
			}
			theHostName = StringUtility.toUpperCase(theHostName);
		}
		return theHostName;
	}
	
//----------------------------
/**
 * Get the host name
	* @param String 	Name of the Logged In User were running on
	*/
	public static String getUserName()
	{
		return System.getProperty(SystemUtility.SYS_PROPERTY_USER_NAME);
	}
//----------------------------
/**
 * Get the host name
	* @param String 	Name of the Logged In User were running on
	*/
	public static String getDomainUserName()
	{
		return System.getenv("USERDOMAIN")+"\\"+System.getProperty(SystemUtility.SYS_PROPERTY_USER_NAME);
	}
// ----------------------------
 /**
  * Sleep for a while
	* @param millis 	How lng to sleep
	* @reurn boolean If interrupted true, otherwise false. 
	*/
	public static boolean sleep(long millis)
	{
		return ThreadUtility.sleep(millis);
	}

	// ---------------------------------------------------------
	/**
	 * This method will run garbage collection and finalization and then
	 * output the memory useage statistics
	 */
	public static String getMemoryUsage()
	{	return getMemoryUsage(false);	}
	// ----------------------------------
	/**
	 * Get System Memory usage
	 * @param extended	Print extended info
	 * @return Multiline String
	 */
	public static String getMemoryUsage(boolean extended)
	{
		StringBuffer retVal = new StringBuffer(512);
		retVal.append(StringUtility.NewLine+"-------------------------------------- " + new java.util.Date() + StringUtility.NewLine);

		Runtime rt = Runtime.getRuntime();
		rt.gc();
		rt.runFinalization();
		rt.gc();

		long tm = rt.totalMemory();
		long fm = rt.freeMemory();
		long am = tm - fm;

		if(extended)
		{
			retVal.append("availableProc->" + rt.availableProcessors()+StringUtility.NewLine);
			retVal.append("   maxMemory->" + rt.maxMemory()+StringUtility.NewLine);
		}
		retVal.append("  totalMemory->" + MathUtility.makeReadableNumber(Long.toString(tm))+StringUtility.NewLine);
		retVal.append("   freeMemory->" + MathUtility.makeReadableNumber(Long.toString(fm))+StringUtility.NewLine);
		retVal.append(" allocatedMem->" + MathUtility.makeReadableNumber(Long.toString(am))+StringUtility.NewLine);

		return retVal.toString();
	}

	////////////////////////////////////////////////////////////////////
	/**
	 * get the PID of the running JVM. NOTE: this may not work on all platforms/jvms
	 */
	public static String getProcessIDString()
	{
		String jvmNameStr = java.lang.management.ManagementFactory.getRuntimeMXBean().getName();
		//System.err.println("jvmNameStr->" + jvmNameStr);
		//4072@THPDEVL11
		int atIndex = jvmNameStr.indexOf('@');
		if( atIndex < 0 )
			return null;
		
		String pid = jvmNameStr.substring(0, atIndex);
		//System.err.println("pid->" + pid);
		return pid;
	}//method

	////////////////////////////////////////////////////////////////////
	/**
	 * finds any items in the source string within percent signs and replaces that part with the system environment var of the same name
	 */
	public static String replaceEnvironmentVariablesInString(String srcString)
	{
		String varString = StringUtility.findVariablePattern(srcString, '%', '%', 0);
		int minSearchIdx = 0;
		while( varString != null )
		{
			String patternString = '%' + varString + '%';
			String sysVarValue = System.getenv(varString);
			if( sysVarValue == null )
			{
				//if we can't find an env var for the pattern, we have to update the minSearchIdx so we dont keep looking for that pattern
				minSearchIdx = srcString.indexOf(patternString, minSearchIdx);
				minSearchIdx += patternString.length();
				//System.err.println("minSearchIdx->" + minSearchIdx);
				Logger.log("failed to replace system variable for->" + patternString);
			}
			else
				srcString = StringUtility.searchReplace(srcString, patternString, sysVarValue);

			varString = StringUtility.findVariablePattern(srcString, '%', '%', minSearchIdx);
		}//while
		return srcString;

		/*
		String s;
		s = replaceEnvironmentVariablesInString("aa%COMPUTERNAME%bb%USERDOMAIN%");
		System.err.println("s->" + s);
		s = replaceEnvironmentVariablesInString("%RA%.%USERDOMAIN%");
		System.err.println("s->" + s);
		s = replaceEnvironmentVariablesInString("%RA%%USERDOMAIN%%HA%");
		System.err.println("s->" + s);
		*/
	}//method
	////////////////////////////////////////////////////////////////////
	/**
	 * finds any items in the source string within percent signs and replaces that part with the system environment var of the same name
	 */
	public static String replaceEnvVarAndVerifyPath(String srcString)
	{
		String retVal = replaceEnvironmentVariablesInString(srcString);
		if(retVal != null)
		{
			if(!FileUtility.fileExists(retVal))
			{	retVal = null;}
		}
		return retVal;
	}



	public static final short	DEF_PROGRAM_PATH = 1;
	public static final short	DEF_PROGRAM_PATH32 = 2;
	public static final short	DEF_COMONFILES_PATH = 3;
	public static final short	DEF_COMONFILES_PATH32 = 4;

	public static final String ENV_VAR_STR_WIN_PGMFILES = "ProgramFiles";
	public static final String ENV_VAR_STR_WIN_PGMFILESx86 = "ProgramFiles(x86)";
	public static final String ENV_VAR_STR_WIN_COMONFILES = "CommonProgramFiles";
	public static final String ENV_VAR_STR_WIN_COMONFILESx86 = "CommonProgramFiles(x86)";
	/**
	 * Get system default element
	 *  SystemUtility.getSystemDefault(SystemUtility.DEF_PROGRAM_PATH)
	 * @param type type of default you want
	 * @return
	 */
	public static String getSystemDefault(int type)
	{
		String varname ="";
		String retVal =null;
		switch(type)
		{
			case DEF_PROGRAM_PATH:
				varname = ENV_VAR_STR_WIN_PGMFILES;
				break;
			case DEF_PROGRAM_PATH32:
				varname = ENV_VAR_STR_WIN_PGMFILESx86;
				break;
			case DEF_COMONFILES_PATH:
				varname = ENV_VAR_STR_WIN_COMONFILES;
				break;
			case DEF_COMONFILES_PATH32:
				varname = ENV_VAR_STR_WIN_COMONFILESx86;
				break;
		}
				retVal = System.getenv(varname);
		return retVal;
	}
// ------------------------------------------------------------------
	/**
	 * Replace all standard Environment variable substitutions.
	 * Used to deal with issues with the 64bit vs 32 bit program filed directories in windows
	 * For example, if a string containing %ProgramFiles% is passed in, it will return it replaced with %ProgramFiles(x86)%
	 * It also works with CommonProgramFiles.
	 * The substitution checking is case insensitive.
	 * @param a string potentially containing an environment variable
	 * @return
	 */
	public static String makeStdEnvVarSubstitutions(String src)
	{
		String retVal = src;
		if(retVal != null)
		{
			retVal = StringUtility.searchReplace(retVal, "%"+ENV_VAR_STR_WIN_PGMFILES+"%", "%"+ENV_VAR_STR_WIN_PGMFILESx86+"%", false);
			retVal = StringUtility.searchReplace(retVal, "%"+ENV_VAR_STR_WIN_COMONFILES+"%", "%"+ENV_VAR_STR_WIN_COMONFILESx86+"%", false);
		}
		return retVal;
	}


	/**
	 * NOTE: this code was grabbed off the net, seems to work ok.. Only tested for Winxp32 so far
	 * ex call) String value = readRegistry("HKCU\\Software\\Microsoft\\Windows\\CurrentVersion\\Explorer\\Shell Folders", "Personal");
	 * @param location path in the registry
	 * @param key registry key
	 * @return registry value or null if not found
	 */
	public static final String readWindowsRegistry(String location, String key)
	{
		String retVal = null;
		//set the key to empty quotes if you want the default
		String qkey = key;
		if( qkey == null )
			qkey = "\"\"";

		try
		{
			String cmd = "reg query "	+ '"' + location + "\" /v " + qkey;
			// Run reg query, then read output with StreamReader (internal class)
			Process process = Runtime.getRuntime().exec(cmd );

			StreamReader reader = new StreamReader(process.getInputStream());
			reader.start();
			process.waitFor();
			reader.join();
			String output = reader.getResult();

			if( output == null )
				return retVal;

			int keyloc = output.indexOf("HKEY_");
			if(keyloc != StringUtility.STRING_INVALID_POSSITION)
			{
				output = output.substring(keyloc);
			}
			// Output has the following format:
			// \n<Version information>\n\n<key>\t<registry type>\t<value>
//			if (!output.contains("\t"))
//			{		return null;	}

			// Output has the following format:
			// XP=> \n<Version information>\n\nHKEY_<key>\t<registry type>\t<value>
			// W7=> \n\nHKEY_<key>    <registry type>    <value>

			output = output.substring(output.indexOf(StringUtility.NewLine)+1);
			// Now we should be at the <key> in both versions

			String tmp = System.getProperty(SYS_PROPERTY_OS_VER);
			float osver = 0.0f;
			try
			{		osver = NumberUtility.convertToFloat(tmp);}
			catch(Exception e){}

			String sep = "\t";
			if(osver > 5.1) // Check Windows Version for separator
			{	sep = "    ";}
//			int loc = output.indexOf("REG_");
//			if (loc == StringUtility.STRING_INVALID_POSSITION)
//			{		return null;	}

			// Parse out the value
			StringTokenizer toknzr = new StringTokenizer(output, sep);
				String p_key = toknzr.nextToken();
				String p_type = toknzr.nextToken();
				if(p_type != null)
				{		retVal = output.substring(output.indexOf(p_type)+p_type.length()+sep.length());}
				
				// find trailing Newline
				keyloc = retVal.indexOf(StringUtility.NewLine);
				if(keyloc != StringUtility.STRING_INVALID_POSSITION)
				{
					retVal = retVal.substring(0, keyloc-1);
				}

		}
		catch (Exception e)
		{
			System.err.println("caught exception reading win registry->" + e);
			e.printStackTrace();			
		}
		
			
		return retVal;

	}

	/**
	 * horked from the net, only used by method above..
	 */
	private static class StreamReader extends Thread
	{
		private InputStream is;
		private StringWriter sw = new StringWriter();
		public StreamReader(InputStream is)
		{
			this.is = is;
		}

		@Override
		public void run()
		{
			try
			{
				int c;
				while ((c = is.read()) != -1)
				{
					sw.write(c);
				}
			} catch (IOException e)
			{
			}
		}

		public String getResult()
		{
			return sw.toString();
		}
	}//iner private class

	/**
	 * get the default windows command string (from registry) for the extension passed in. NOTE: this has only been tested for WinXP32 so far.
	 * it will take it with a dot or without
	 * ex) getDefaultCommandStringForFileExtension("zip"); or  getDefaultCommandStringForFileExtension(".zip");
	 * Some sample returns:
	 * "C:\Program Files\Adobe\Reader 9.0\Reader\AcroRd32.exe" "%1"
	 * %SystemRoot%\system32\NOTEPAD.EXE %1
	 * C:\PROGRA~1\WINZIP\winzip32.exe "%1"
	 */
	public static String getDefCmdForFileExtensionWindows(String fileExt)
	{
		if( fileExt == null )
			return null;
		if( !fileExt.startsWith(".") )
			fileExt = "." + fileExt;

		//first have to get the name of the key that is associated with the extension
		String regTag = readWindowsRegistry("HKCR\\" + fileExt , null);
		if( regTag == null )
			return null;

		String regKey = "HKCR\\" + regTag + "\\Shell\\Open\\Command";
		//System.err.println("regKey->" + regKey);
		String val = readWindowsRegistry(regKey, null);
		return val;
	}

	////////////////////////////////////////////////////////////////////
	/**
	 * get text data from the system clipboard
	 */
	public static String getClipboardText()
	{
		Transferable t = Toolkit.getDefaultToolkit().getSystemClipboard().getContents(null);
		try
		{
			if (t != null && t.isDataFlavorSupported(DataFlavor.stringFlavor))
			{
				String text = (String) t.getTransferData(DataFlavor.stringFlavor);
				return text;
			}
		}
		catch (UnsupportedFlavorException e)
		{
			Logger.log("e->" + e);
			e.printStackTrace();
		}
		catch (IOException e)
		{
			Logger.log("e->" + e);
			e.printStackTrace();
		}
		return null;
	}

	////////////////////////////////////////////////////////////////////
	/**
	 * sets the system clipboard text
	 */
	public static void setClipboardText(String str)
	{
		StringSelection ss = new StringSelection(str);
		Toolkit.getDefaultToolkit().getSystemClipboard().setContents(ss, null);
	}

	////////////////////////////////////////////////////////////////////
	/**
	 * returns the temp directory (system dependant)
	 */
	public static String getTempDirectory()
	{
		//note: I saw a blog that said windows returns the temp dir with a file separator at the end, other plaforms do not
		//so i'm feeding it to a file and getting the abs path which should make it consistent
		String dirName = System.getProperty(SYS_PROPERTY_TEMP_DIR);
		File f = new File(dirName);
		return f.getAbsolutePath();
	}//method


	////////////////////////////////////////////////////////////////////
	/**
	 * starts a thread that simply checks for the existance of a file in the working dir every minute called "printDebugKillJVM.txt"
	 * if the file is found, stack dumps from all running threads are output to standard err as well as a file in the output directory
	 * and the jvm is then killed. This is intended for dire circumstances where the app locks up, to assist in seeing which threads were doing what..
	 */
	public static void startMonitorDumpAndKillDirective()
	{
		Runnable r = new Runnable()
		{
			@Override
			public void run()
			{
				while (true)
				{
					File f = new File("printDebugKillJVM.txt");
					if( !f.exists() )
					{
						try
						{
							Thread.sleep(60000);
						} catch (InterruptedException ex)
						{
							Logger.log(ex.toString());
						}
						continue;
					}

					StringBuilder sb = new StringBuilder();
					sb.append("PRINTING ALL STACKS AND DIE INVOKED DUE TO EXISTENCE OF file printDebugKillJVM.txt !!!!!!!!!!!!!!!!!!!!!!!!\n");
					f.deleteOnExit();
					f.delete();

					ThreadUtility.getAllStackDumps(sb);

					System.err.println(sb.toString());
					System.err.flush();
					
					File outputDir = new File("output");
					if( !outputDir.exists() || !outputDir.isDirectory() )
						outputDir = null;
//					File outFile = FileUtility.createUniqueFile(outputDir, "KillJVMDebug", ".txt");
					File outFile = new File(outputDir, "KillJVMDebug.txt");
					try
					{
						FileWriter fw = new FileWriter(outFile);
						fw.write(sb.toString());
						fw.flush();
						fw.close();
					}
					catch (Exception ex)
					{
						System.err.println("caught exception trying to write file..->" + ex);
					}
					System.exit(0);
				}
			}//run
		};//runnable
		Thread t = new Thread(r, "printDebugKillJVM thread");
		t.start();
	}//method

	////////////////////////////////////////////////////////////////////
	/**
	 *
	 */
	public void xxx()
	{
		
		/*File file = chooser.getSelectedFile();
		try
		{
			if( file.exists() )
			{
				option = JOptionPane.showConfirmDialog(cont, "The file already exists. Would you like to replace it?");
				if( option != JOptionPane.YES_OPTION )
					return;
			}
			else
				file.createNewFile();
			FileWriter fw = new FileWriter(file);
			fw.write(text.toCharArray());
			fw.flush();
			fw.close();
		}
		catch(java.io.IOException ioe)
		{
			JOptionPane.showMessageDialog(cont, "There was an error saving the file. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
			System.err.println("ioe->" + ioe);
			return;
		}
		*/
	}//method

	// If a string is on the system clipboard, this method returns it; // otherwise it returns null. public static String getClipboard() { Transferable t = Toolkit.getDefaultToolkit().getSystemClipboard().getContents(null); try { if (t != null && t.isDataFlavorSupported(DataFlavor.stringFlavor)) { String text = (String)t.getTransferData(DataFlavor.stringFlavor); return text; } } catch (UnsupportedFlavorException e) { } catch (IOException e) { } return null; } // This method writes a string to the system clipboard. // otherwise it returns null. public static void setClipboard(String str) { StringSelection ss = new StringSelection(str); Toolkit.getDefaultToolkit().getSystemClipboard().setContents(ss, null); }
	
	public static void main(String[] args)
	{ 
		 
		//SimpleDateFormat sdf = new SimpleDateFormat();
		//Random random = new Random();
		//String fileName  = String.format("%s.%s", sdf.format( new java.util.Date() ), random.nextInt(9));
		//System.err.println("fileName->" + fileName);
		System.err.println("ooo->" + new Random().nextInt(9999));



		//System.err.println("getJavaSubVersion()->" + getJavaSubVersion());

		String value = readWindowsRegistry("HKCU\\Software\\Microsoft\\Windows\\CurrentVersion\\Explorer\\Shell Folders", "Personal");
		System.err.println(value);
		value = readWindowsRegistry("HKCR\\.pdf", "\"\"");
		System.err.println(value);

		value = getDefCmdForFileExtensionWindows("pdf");
		System.err.println(value);

		System.err.println("getTempDirectory()->" + getTempDirectory());
		
		System.err.println("ooo->" + makeStdEnvVarSubstitutions("%programfiles%"));
		//System.err.println("ooo->" + makeStdEnvVarSubstitutions("programfiles"));


		value = getDefCmdForFileExtensionWindows("txt");
		System.out.println(value);

		value = getDefCmdForFileExtensionWindows("zip");
		System.out.println(value);

		value = replaceEnvironmentVariablesInString("%ProgramFiles%\\%USERDOMAIN%\\In_Car_Rpts_CR10\\CR10Dlg.exe");
		System.out.println(value);
		
		Runtime runtime = Runtime.getRuntime();
		try
		{
		runtime.exec("notepad.exe \"" + "c:\\temp\\svn_unversioned.txt" + "\"");
		}
		catch(java.io.IOException ioe)
		{
		System.err.println("IOException ioe->" + ioe);
		}
	}
	
	/**
	 * attempts to find the user's picture dir
	 * returns null if it cannot locate a directory
	 */
	public static File getUserPictureDirectory()
	{
		String baseName = getUserHomeDir();
		baseName += File.separator;
		String dirName = baseName + "Pictures";
		File f = new File(dirName);
		if( f.exists() )
			return f;
		
		dirName = baseName + "My Documents\\My Pictures";
		f = new File(dirName);
		if( f.exists() )
			return f;
		
		return null;
		
		//C:\Users\cm06213\Pictures
		// \\thpdevl11\c$\Documents and Settings\cm06213\My Documents\My Pictures
	}
	
	/**
	 * returns the user home dir. due to JRE bugs sometimes the user.home value is not correct, so this will attempt to get the 
	 * system env var, or default to the user.home value
	 */
	public static String getUserHomeDir()
	{
		try
		{
			//NOTE: saw some cases on win7 jre 1.7.7 where the user.home reported c:/users/user as the home dir
			//not sure if it was a machine load problem or jre problem or what..
			//also saw some bug reports and threads of similar errors
			//getting the userprofile env var comes back correct though
			//jumping through the hoops here in case there is no userprofile or we get a security exception or something on it
			String winUserProf = System.getenv("USERPROFILE");
			if( winUserProf != null )
				return winUserProf;
		}
		catch(Exception e)
		{
			System.err.println("exception trying to get user home dir->" + e);
			e.printStackTrace();
		}
		return System.getProperty("user.home");
	}
	

} // class

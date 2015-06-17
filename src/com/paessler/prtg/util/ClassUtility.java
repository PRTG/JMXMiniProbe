package com.paessler.prtg.util;

import java.io.PrintStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import com.paessler.prtg.jmx.Logger;

/******************************************************************************
 *  A library of static Class utility functions
 *  @author JR Andreassen
 *  @version 0.1
 *****************************************************************************/
public abstract class ClassUtility
{
	public static final int		STRING_INVALID_POSSITION = -1;
	public static final char	CLASS_PATH_SEPARATOR 	= '/';
	public static final char	CLASS_PACKAGE_SEPARATOR = '.';
	
		 //-----------------------------------------------------------
		 /**
		  *	Load class by name 
		  * @param classname Name of class to load
		  * @return
		  */
		  public static Class<?>	classForName(String classname)
		  {
				Class<?> retVal = null;
				String ssClassName = classname;
				if (ssClassName != null)
				{	ssClassName = ssClassName.replace(CLASS_PATH_SEPARATOR, CLASS_PACKAGE_SEPARATOR);}

			  try
			  {
			  	retVal = Class.forName(ssClassName);
			  }
			  catch (Exception e)
			  {
		  		Logger.log("(\'"+classname+"\'/\'"+ssClassName+"\') Could not find class file:  Msg   => " + e.getMessage());
		  		retVal = null;
			  }
				return retVal;
		  }
			 //-----------------------------------------------------------
			 /**
			  * Get sub Array
			  * @param ch_arr	Array to slice
			  * @param start		Starting Index
			  * @param len			Length
			  * @return String	String representation of the character
			  */
			  public static Object	newInstance(Class<?> objclass, Object[] args)
			  {
			  	Object retVal = null;
			  	if(objclass != null)
			  	{
			  		
				  	try
						{
							//Class[] parameterTypes = new Class[2];
				            //parameterTypes[0] = Class.forName("com.dbwiz.ui.AppFrame");
							//parameterTypes[1] = Class.forName("java.lang.Integer");
							//Constructor c = panelClass.getConstructor(parameterTypes);
							// The only public constructor takes an af and int -GSI
							Constructor c[] = objclass.getConstructors();
							//protected AppPanel(String primaryTbleName, AppFrame af, int mode)
		
							//for classed with multiple constructors, try them all
//							for( int i=0; i < c.length; i++)
							for(Constructor curr : c)
							{
								//if the constructor has a different number of arguments.. skip it
								//we're only looking for the ones with (String tableName, AppFrame af, int mode)
								Class<?>[] paramTypeArr = curr.getParameterTypes();
								if( paramTypeArr.length != args.length )
									continue;
								try
								{
									retVal = curr.newInstance(args);
									break;
								}
								catch(java.lang.IllegalArgumentException ex)
								{
//									Logger.printDebug("ClassUtility.newInstance("+objclass.getName()+", arg) IllegalArgumentException -> Class not accessible e->" + ex);
									Logger.log("ClassUtility.newInstance("+objclass.getName()+", arg) IllegalArgumentException -> Class not accessible e->" + ex);
									ex.printStackTrace();
								}
							}//for
						}
						catch(InvocationTargetException e)
						{
//							Logger.printDebug("ClassUtility.classForName() -> Constructor invocation failed  e->" + e);
							e.getTargetException().printStackTrace();			
						}
						catch(IllegalAccessException e)
						{
//						 	Logger.printDebug("ClassUtility.classForName() -> Class not accessible e->" + e);
							e.printStackTrace();
						}
						catch(InstantiationException e)
						{
//						 	Logger.printDebug("ClassUtility.classForName():InstantiationException trying to clone an abstract class? e->" + e);
							e.printStackTrace();
						}
			  	} // if(objclass != null)
			  	return retVal;
			  }
		 //-----------------------------------------------------------
		 /**
		  * Get sub Array
		  * @param ch_arr	Array to slice
		  * @param start		Starting Index
		  * @param len			Length
		  * @return String	String representation of the character
		  */
		  public static Object	newInstanceForName(String classname, Object[] args)
		  {
		  	Object retVal = null;
		  	Class<?> objclass = classForName(classname);
		  	if(objclass != null)
		  	{
		  		
			  	try
					{
						//Class[] parameterTypes = new Class[2];
			            //parameterTypes[0] = Class.forName("com.dbwiz.ui.AppFrame");
						//parameterTypes[1] = Class.forName("java.lang.Integer");
						//Constructor c = panelClass.getConstructor(parameterTypes);
						// The only public constructor takes an af and int -GSI
						Constructor c[] = objclass.getConstructors();
						//protected AppPanel(String primaryTbleName, AppFrame af, int mode)
	
						//for classed with multiple constructors, try them all
//						for( int i=0; i < c.length; i++)
						for(Constructor curr : c)
						{
							//if the constructor has a different number of arguments.. skip it
							//we're only looking for the ones with (String tableName, AppFrame af, int mode)
							Class<?>[] paramTypeArr = curr.getParameterTypes();
							if( paramTypeArr.length != args.length )
								continue;
							try
							{
								retVal = curr.newInstance(args);
								break;
							}
							catch(java.lang.IllegalArgumentException ex)
							{
								Logger.log("ClassUtility.newInstanceForName("+objclass.getName()+", arg) IllegalArgumentException -> Class not accessible e->" + ex);
								ex.printStackTrace();
							}
						}//for
					}
					catch(InvocationTargetException e)
					{
						Logger.log("ClassUtility.classForName() -> Constructor invocation failed  e->" + e);
						e.getTargetException().printStackTrace();			
					}
					catch(IllegalAccessException e)
					{
						Logger.log("ClassUtility.classForName() -> Class not accessible e->" + e);
						e.printStackTrace();
					}
					catch(InstantiationException e)
					{
						Logger.log("ClassUtility.classForName():InstantiationException trying to clone an abstract class? e->" + e);
						e.printStackTrace();
					}
		  	} // if(objclass != null)
		  	return retVal;
		  }
			 //-----------------------------------------------------------
			 /**
			  * Create a new Instance for Name
			  * @param classname	name of class
			  * @return
			  */
			  public static Object	newInstanceForName(String classname)
			  {	
					String ssClassName = classname;
					if (ssClassName != null)
					{	ssClassName = ssClassName.replace(CLASS_PATH_SEPARATOR, CLASS_PACKAGE_SEPARATOR);}
					
					Object retVal = null;
				  try 
				  {
				  	Class<?> clazz = Class.forName(ssClassName);
				  	retVal = clazz.newInstance();
				  } 
				  catch (Exception e) 
				  {	
					Logger.log("(\'"+classname+"\'/\'"+ssClassName+"\') Could not find class file:  Msg   => " + e.getMessage());
			  		retVal = null;
				  } 
					return retVal;
			  }

		  //-----------------------------------------------------------
		   /**
		    * Dump Class info
		    * @param dest			Output destination
		    * @param classobj	Class object to dump
		    */
		   public static void	dumpHierarchy(PrintStream dest, Class<?> classobj)
		   {	
			 		if (classobj != null)
					{
						String name = classobj.getName();
			 			dest.println("Name: "+name);
			 			dest.println("  Interfaces");
			 			arrayDump(dest, classobj.getInterfaces());
			 			if (!name.equals("java.lang.Object"))
						{
							dumpHierarchy(dest, classobj.getSuperclass());
						}
					}
		   }

			 //-----------------------------------------------------------
			 /**
			  * Get sub Array
			  * @param ch_arr	Array to slice
			  * @param start		Starting Index
			  * @param len			Length
			  * @return String	String representation of the character
			  */
			  public static void	arrayDump(PrintStream dest, Object[] arr)
			  {	if(arr != null)
			 		{	
				 		synchronized(dest)
				 		{
							for(int i= 0; i < arr.length; i++)
			 				{	
			 					dest.println("arr["+i+"]="+arr[i]);
			 				} // for
				 		} // synchronized(dest)
			 		} // if
			  } // dump()

		  //-----------------------------------------------------------
		  /**
		   * Dump Class info
		   * @param dest			Output destination
		   * @param classobj	Class object to dump
		   */
		   public static void	dumpClassInfo(PrintStream dest, Class<?> classobj)
		   {	
		    try 
		    {
					
//					dest.println("Name " + classobj.getName());
					dest.println("Class Hierarchy ");
					dumpHierarchy(dest, classobj);
					dest.println("Methods");
					arrayDump(dest, classobj.getMethods());
					dest.println("DeclaredFields");
					arrayDump(dest, classobj.getDeclaredFields());
		    } 
		    catch (Exception e) 
		    {	
		    	Logger.log(""+e.getMessage());
		    } 
		   }

		  //-----------------------------------------------------------
		  //###########################################################
		  //-----------------------------------------------------------
		  /**
		   * Get Class Name (Without path)
		   * @param classobj		Object to get name for
		   */
		   public static String	getClassName(String classname)
		   {
				 String retVal = null;
				 if(classname != null)
				 {
					 retVal = classname;
					 int idx = retVal.lastIndexOf('.');
					 if(idx != STRING_INVALID_POSSITION)
					 {	retVal = retVal.substring(idx+1);}
				 }
				 return retVal;
			 }
		  //-----------------------------------------------------------
		  /**
		   * Get Class Path (Without name)
		   * @param classname		Object to get path for
		   */
		   public static String	getClassPath(String classname)
		   {
				 String retVal = null;
				 if(classname != null)
				 {
					 retVal = classname;
					 int idx = retVal.lastIndexOf('.');
					 if(idx != STRING_INVALID_POSSITION)
					 {	retVal = retVal.substring(0, idx);}
				 }
				 return retVal;
			 }

		  //-----------------------------------------------------------
		  /**
		   * Get Class Name (Without path)
		   * @param classobj			Object to get name for
		   */
		   public static String	getClassName(Class<?> classobj)
		   { return getClassName(classobj.getName());		}
		  //-----------------------------------------------------------
		  /**
		   * Get Class Path (Without name)
		   * @param classobj			Object to get name for
		   */
		   public static String	getClassPath(Class<?> classobj)
		   {	 return getClassPath(classobj.getName());		}


		  //-----------------------------------------------------------
		  //-----------------------------------------------------------
/*			public static void main(String[] args)
			{
				System.out.println("Calling getClassPath()"+getClassPath(ClassUtility.class));
				System.out.println("Calling getClassName()"+getClassName(ClassUtility.class));
			}
 */
        // ------------------------------------------------------------------------
        // ------------------------------------------------------------------------
} // class


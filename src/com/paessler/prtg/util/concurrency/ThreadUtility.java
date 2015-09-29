package com.paessler.prtg.util.concurrency;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Map;

/**
 * CLASS: ThreadUtility - Thread utility functions
 *
 */
public abstract class ThreadUtility
{

	////////////////////////////////////////////////////////////////////
	/**
	 * Print all Current Threads
	 * @param ps	PrintStream to print data to
	 */
	public static void printThreads(PrintStream ps)
	{
		ArrayList<Thread> vect = getAllThreads();
		int i = 0;
		for( Thread t : vect)
		{
			ps.println("Thread[" + i + "] " + t.toString());
			i++;
		}
	}	// printThreads
	
	////////////////////////////////////////////////////////////////////
	/**
	 * returns a summary string with the thread name/id and stack dump of each active thread
	 */
	public static StringBuilder getAllStackDumps(StringBuilder sb)
	{
		Map<Thread,StackTraceElement[]> allThreads = Thread.getAllStackTraces();
		for( Thread t : allThreads.keySet() )
		{
			sb.append("@@@@@@@@@@@@@@@@@@@@@@\n");
			sb.append("Thead t->" + t.getId() + " " + t.getName() + "\n");
			StackTraceElement[] steArray = allThreads.get(t);
			for( StackTraceElement ste : steArray )
			{
				sb.append("\t");
				sb.append(ste.toString());
				sb.append("\n");
			}
		}
		return sb;
	}//method
	////////////////////////////////////////////////////////////////////
	/**
	 * returns a summary string with the thread name/id and stack dump of each active thread
	 */
	public static String getAllStackDumps()
	{
		StringBuilder sb = new StringBuilder();
		getAllStackDumps(sb);
		return sb.toString();
	}//method

	////////////////////////////////////////////////////////////////////
	/**
	 * Return all Current Threads as a vector
	 * @return Vector Vector of Threads
	 */
	public static ArrayList<Thread> getAllThreads()
	{
		ThreadGroup tg = getRootThreadGroup();
		int totthr = tg.activeCount();
		Thread[] threadList = new Thread[totthr * 2];
		totthr = tg.enumerate(threadList, true);
		ArrayList<Thread> retVal = new ArrayList<Thread>();
		//Log.printDebug("Root="+tg.toString()+",  Thead Count="+totthr);
		for (int i = 0; i < totthr; i++)
		{
			retVal.add(threadList[i]);
		}
		return retVal;
	}	// getAllThreads

	////////////////////////////////////////////////////////////////////
	/**
	 * Set the name of the threads for readability
	 */
	public static void setThreadName(String name)
	{
		Thread thisthread = Thread.currentThread();
		setThreadName(thisthread, name);
	}

	////////////////////////////////////////////////////////////////////
	/**
	 * Set the name of the threads for readability
	 */
	public static void setThreadName(Thread thisthread, String name)
	{
		String thname = thisthread.getName();
		//		System.out.println("setMsgHandlerThreadName() Changing Task name from =>" + thname);
		if (thname.startsWith("Thread"))
		{
			thname = thname.substring(6);
		}
		thname = name + thname;
		thisthread.setName(thname);
		//		System.out.println("setMsgHandlerThreadName() Changed Task name to =>" + thisthread.getName());
	}

	////////////////////////////////////////////////////////////////////
	/**
	 * Sleep for a while
	 * @param millis 	How lng to sleep
	 * @reurn boolean If interrupted true, otherwise false.
	 */
	public static boolean sleep(long millis)
	{
		boolean retVal = false;
		if (millis > 0)
		{
			try
			{
				Thread.sleep(millis);
			} catch (InterruptedException e)
			{
				retVal = true;
				System.err.println("InterruptedException ->" + e);
				e.printStackTrace();
			}
		}
		return retVal;
	}

	////////////////////////////////////////////////////////////////////
	/**
	 * Print all Current Threads
	 * @param ps	PrintStream to print data to
	 */
	public static ThreadGroup getRootThreadGroup()
	{
		ThreadGroup retVal = null;
		ThreadGroup tmp = Thread.currentThread().getThreadGroup();
		while (tmp != null)
		{
			retVal = tmp;
			tmp = retVal.getParent();
		}
		return retVal;
	}

	////////////////////////////////////////////////////////////////////
	/**
	 *
	 */
	public static String getThreadIDString(Thread t, boolean printtname)
	{
		if (t == null)
			return "NULL THREAD";

		String retVal = t.getId() +(printtname ? ":" + t.getName() : "");
		return retVal;
	}//method
	////////////////////////////////////////////////////////////////////
	/**
	 *
	 */
	public static String getThreadIDString(Thread t)
	{
		return getThreadIDString(t, false);
	}//method

	public static String getThreadIDString(boolean printtname)
	{
		return getThreadIDString(Thread.currentThread(), printtname);
	}//method
	public static String getThreadIDString()
	{
		return getThreadIDString(Thread.currentThread(), false);
	}//method

	////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////
	/*public static void main(String[] arg)
	{

	}*/
}//class


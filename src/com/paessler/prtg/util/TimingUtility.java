package com.paessler.prtg.util;

/******************************************************************************
 *  A library of static timing utility functions.
 * The time is tracked in MilliSeconds (ms) so, to change the display use 
 * the division factor, to use:
 * <blockquote><pre>
 *  TimingUtility timero = new TimingUtility(DIV_FACT_SECONDS);
 *  ... [perform task to be timed]
 *  System.out.println("The task took "+timero);
 * </pre></blockquote>
 *<p>
 *  @author JR Andreassen
 *  @version 0.1
 *****************************************************************************/
public class TimingUtility
{
	/** Divisor for Seconds */
	public static int DIV_FACT_SECONDS	= 1000;
	//-----------------------------------------------------------
	/** Starting time */
	protected long	starttime;
	/** Ending time */
	protected long	endtime;
	
	/** Time units */
	protected String	timeunits;
	/** Division factor for display */
	protected int divisionFactor;
	//-----------------------------------------------------------
	/**
	 * Constructor
	 * @param divfact Division factor,
	 * @param units Unit suffix,
	 */
	public TimingUtility(int divfact, String units )
	{	this(divfact);
		timeunits = units;
	}
	//-----------------------------------------------------------
	/**
	 * Constructor
	 * @param divfact Division factor,
	 */
	public TimingUtility(int divfact)
	{	endtime = -1;
		divisionFactor = (divfact != 0 ? divfact : 1);
		starttime = System.currentTimeMillis();
		timeunits = null;
		if(divisionFactor == DIV_FACT_SECONDS)
		{	timeunits = "Sec(s)";}
	}
	
	//-----------------------------------------------------------
        /**
         * Default Constructor
         * Division factor 1 (Display numbers in Milliseconds).
         */
	public TimingUtility()
	{	this(1);
	}
	
	//-----------------------------------------------------------
        /**
         * Get time elapsed.
         * @return long time
         */
	public long getElapsed()
	{ return 	(getEndTime() - starttime) / divisionFactor;
	}
	//-----------------------------------------------------------
        /**
         * Get time elapsed as a float.
         * Convinient if division factor is used to show fractions.
         * @return float time
         */
	public float getElapsedFloat()
	{ return 	(getEndTime() - starttime) / (float)divisionFactor;
	}
	
	//-----------------------------------------------------------
        /** Accessor: Get statring time.       */
	public long getStartTime()		{return starttime;}
        /** Accessor: Get ending time.       */
	public long getEndTime()			{return (endtime == -1 ? System.currentTimeMillis() : endtime);}
	//-----------------------------------------------------------
        /** Start timing, Set starting time.       */
	public void start()			{starttime = System.currentTimeMillis();}
        /** Stop timing, Set ending time.       */
	public void stop()			{endtime = System.currentTimeMillis();}
	
	//-----------------------------------------------------------
        /** Default string conversion, print elapsed time as float.      */
	public String toString()
	{	
		StringBuffer retVal = new StringBuffer(String.valueOf(getElapsedFloat()));
		if(timeunits != null)
		{ retVal.append(" ").append(timeunits);}
		return retVal.toString();	
	}
	
	public String getPrintableStats()
	{
		long elapsedMillis = getEndTime() - starttime;
		
		long days = elapsedMillis / DateUtility.MILLISECONDS_PER_DAY;
		elapsedMillis -= (days*DateUtility.MILLISECONDS_PER_DAY);
		
		long hours = elapsedMillis / DateUtility.MILLISECONDS_PER_HOUR;
		elapsedMillis -= (hours*DateUtility.MILLISECONDS_PER_HOUR);
		
		long minutes = elapsedMillis / DateUtility.MILLISECONDS_PER_MINUTE;
		elapsedMillis -= (minutes*DateUtility.MILLISECONDS_PER_MINUTE);
		
		long seconds = elapsedMillis / DateUtility.MILLISECONDS_PER_SECOND;
		elapsedMillis -= (seconds*DateUtility.MILLISECONDS_PER_SECOND);
		
		//Total process time = " + minuteDiff + " minutes " + secondDiff + " seconds "  + millisDiff + " milliseconds");" +
		String retString = (days > 0 ? days + " days, " : "") + (hours > 0 ? hours + " hours, ":"") + (minutes > 0 ? minutes + " minutes, ":"") + (seconds > 0 ? seconds + " seconds, " : "") + elapsedMillis + " milliseconds.";
		return retString;
	}
	
} // class TimingUtility


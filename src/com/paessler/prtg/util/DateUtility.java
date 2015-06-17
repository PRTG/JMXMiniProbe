package com.paessler.prtg.util;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.StringTokenizer;
import java.util.TimeZone;

import com.paessler.prtg.jmx.Logger;

/******************************************************************************
 *  A library of static Date utility functions
 * @author  JR Andreassen
 *  @version 0.1
 *****************************************************************************/
public abstract class DateUtility
{
// ------------------------------------------------------------		
	/**  Constant: Default Date/Time formating String with No Time Zone */
	public static final String DEFAULT_DATE_FORMAT_STR_NTZ	= "yyyy-MM-dd";
	public static final String DEFAULT_TIME_FORMAT_STR_NTZ	= "HH:mm:ss";
	
	public static final String DEFAULT_DATETIME_FORMAT_STR_NTZ	= "yyyy-MM-dd HH:mm:ss";
	
	/** Follows the W3C DTF format, minus the tz designation at the end, which our webservers barf on */
	public static final String DATETIME_FORMAT_STR_HTTP_NTZ	= "yyyy-MM-dd'T'HH:mm:ss";  //String formatString = "yyyy-MM-dd'T'HH:mm:ssXXX"; the XXX would give the proper time zone designation
	

	/**  Constant: Short Date formating String with No Time Zone */
	public static final String DATE_FORMAT_YYYMMDD	= "yyyyMMdd";
	public static final String DATE_FORMAT_MDY_STR	= "MM/dd/yyyy";

	/**  Constant: XML Date/Time formating String with No Time Zone */
	public static final String XML_DATE_FORMAT_NTZ	= "yyyy-MM-ddTHH:mm:ss";
	/**  Constant: Default Date/Time formating String with Time Zone */
	public static final String DEFAULT_DATE_FORMAT_WMS	= "yyyy-MM-dd HH:mm:ss.S";
	/**  Constant: Default Date/Time formating String with Time Zone */
	public static final String DEFAULT_DATE_FORMAT_WTZ	= "yyyy-MM-dd HH:mm:ss z";
	/**  Constant: Default Date/Time formating String with Millis and Time Zone */
	public static final String DEFAULT_DATE_FORMAT_WMSTZ	= "yyyy-MM-dd HH:mm:ss.S Z";

	public static final String UTIL_DATE_TO_STRING_SIMPLEDATEFORMAT = "EEE MMM dd HH:mm:ss zzz yyyy";
	
	// -----------------------------------------------------------------------------------
	/**
	 *  Get Date formatter
	 * @return default DateFormat(DateUtility.DEFAULT_DATE_FORMAT_NTZ)
	 */
	public static final DateFormat getDefaultDateFormatter()
	{ return new SimpleDateFormat(DEFAULT_DATETIME_FORMAT_STR_NTZ);	}

	// -----------------------------------------------------------------------------------
	/** an append value for time */
	public static final String TIMESTAMP_TIME_APPENDSTR = " 00:00:00.000000000";

	/**  Constant: UTC time(String representation of long) of Jan 1, 0001 */
	public static final String STR_UTC_DAY_0001_01_01_00_00	= "-62135748000000";

	/**  Constant: UTC time(long) of Jan 1, 0001 */
	public static final long UTC_DAY_0001_01_01_00_00	= Long.parseLong(STR_UTC_DAY_0001_01_01_00_00);

	/**  Constant: UTC time(long) of Jan 1, 0001 */
	public static final Timestamp TS_DAY_0001_01_01_00_00	= new Timestamp(Long.parseLong(STR_UTC_DAY_0001_01_01_00_00));

	/**  Constant: TimeZone String GMT */
	public static final String TZ_GMT_STR	= "GMT-0:00";
	/**  Constant: TimeZone String Central */
	public static final String TZ_CST_STR	= "GMT-6:00";
	/**  Constant: TimeZone String Mounain */
	public static final String TZ_MST_STR	= "GMT-7:00";

	/**  Constant: TimeZone GMT */
	public static final TimeZone TZ_GMT	= java.util.TimeZone.getTimeZone(TZ_GMT_STR);
	/**  Constant: TimeZone Local for host */
	public static final TimeZone TZ_LOCAL	= java.util.TimeZone.getDefault();
	/**  Constant: TimeZone CST */
	public static final TimeZone TZ_CST	= java.util.TimeZone.getTimeZone(TZ_CST_STR);
	/**  Constant: TimeZone MST */
	public static final TimeZone TZ_MST	= java.util.TimeZone.getTimeZone(TZ_MST_STR);

	public static int DATEPART_YEAR		= Calendar.YEAR;
	public static int DATEPART_MONTH	= Calendar.MONTH;
	public static int DATEPART_DAY		= Calendar.DAY_OF_YEAR;
	public static int DATEPART_HOUR		= Calendar.HOUR;
	public static int DATEPART_MINUTE = Calendar.MINUTE;
	public static int DATEPART_SECOND = Calendar.SECOND;
	public static int DATEPART_MILLI	= Calendar.MILLISECOND;

// **************************************************************
	public static final int MILLISECONDS_PER_DAY	=	86400000;
	public static final int SECONDS_PER_DAY	=	86400;
	public static final int MILLISECONDS_PER_HOUR	=	3600000;
	public static final int MILLISECONDS_PER_MINUTE	=	60000;
	public static final int MILLISECONDS_PER_SECOND	=	1000;

	/**
	 * Constant: Julian Day of UTC 0, Jan 1, 1970.
	 * For Julian Day calc with Time Structs..
	 */
	public static final int UTC_JULIAN_DAY 	=   2440588;
	
	/**
	 * offset from the juilan day to Jan 1, 2000
	 */
	public static final int JULIAN_OFFSET_Y2K = 2451545;
	
	public static java.util.Date DATE_MILLIS_01011900;
	public static java.util.Date DATE_MILLIS_06062079;
	//public static java.util.Date DATE_MILLIS_01011753 = null;
	//public static java.util.Date DATE_MILLIS_12319999 = null;
	
	static 
	{
		try
		{
			DATE_MILLIS_01011900 = stringToDate("1900-01-01 00:00:00.0");
			DATE_MILLIS_06062079 = stringToDate("2079-06-06 23:59:00.0");
			//DATE_MILLIS_01011753 = stringToDate("1753-01-01 00:00:00.0");
			//DATE_MILLIS_12319999 = stringToDate("9999-12-31 00:00:00.0");
		}
		catch(java.text.ParseException pe)
		{
			System.err.println("Unexpected ParseException!!! pe ->" + pe);
		}
			
	}
	
	
	// -------------------------------------------------------
	/**
	 * Get current Julian day in the current timezone
	 * @return long Julian Day number
	 */
	public static int getCurrentJulian()
	{
		return getJulian(getCurrentCalendar());
	}

	// -------------------------------------------------------
	/**
	 * Get current Julian day in the current timezone
	 * @return long Julian Day number
	 */
	public static int getCurrentJulianOffsetY2K()
	{
		return getJulianOffsetY2K(getCurrentCalendar());
	}

	// -------------------------------------------------------
	/**
	 * Get current Julian day for the calendar given
	 * @param	cal	Calendar to use
	 * @return long Julian Day number
	 */
	public static int getJulian(Calendar cal)
	{
		long millis = getMillisOffsetToTZ(cal);
		int daysSinceEpoch = (int) ((double)millis / (double)MILLISECONDS_PER_DAY);
		return UTC_JULIAN_DAY + daysSinceEpoch;
	}

	// -------------------------------------------------------
	/**
	 * Return time represented by the given calendar as UTC offset (ie GMT)
	 * this is the same as converting the calndar to GMT timezone and getting
	 * the milliseconds from it
	 */
	public static long getMillisOffsetToTZ(Calendar cal)
	{
		long tmp = cal.get(Calendar.ZONE_OFFSET) + cal.get(Calendar.DST_OFFSET);
		return (cal.getTimeInMillis() + tmp);
	}

	// -------------------------------------------------------
	/**
	 * Get current Julian day for the calendar given, offset by the value given.
	 * the offset will be subtracted from the julian, so should generally be a postive number
	 * @param	cal	Calendar to use
	 * @return
	 */
	public static int getJulianOffset(Calendar cal, int offset)
	{
		int julian = getJulian(cal);
		return julian - offset;
	}

	// -------------------------------------------------------
	/**
	 * Get current Julian day for the calendar given, offset by the value given.
	 * the offset will be subtracted from the julian, so should generally be a postive number
	 * @param	cal	Calendar to use
	 * @return
	 */
	public static int getJulianOffsetY2K(Calendar cal)
	{
		return getJulianOffset(cal, JULIAN_OFFSET_Y2K);
	}
	
	/**
	 * convert a utc time passed in to the local time  NOTE: manually manipulating the milliseconds to effect different timezones has problems
	 * try converting 2010-11-07 03:00 UTC to local via this method will be off an hour 
	 */
	/*public static long convertUTCtoLocal(long utcTime)
	{
		long tmp = defCalendar.get(Calendar.ZONE_OFFSET) + defCalendar.get(Calendar.DST_OFFSET);
		return utcTime + tmp;
	}*/

	/**
	 * convert the local time passed in to a utc time  NOTE: manually manipulating the milliseconds to effect different timezones has problems
	 * try converting 2010-11-07 00:00 to UTC via this method will be off an hour
	 */
	/*public static long convertLocalToUTC(long localTime)
	{
		long tmp = defCalendar.get(Calendar.ZONE_OFFSET) + defCalendar.get(Calendar.DST_OFFSET);
		return localTime - tmp;
	}*/
	
	// -------------------------------------------------------
	/**
	 * Return current local time as Timestamp
	 */
	public static java.sql.Timestamp getCurrentTimeStamp()
	{
		return new java.sql.Timestamp(getCurrentMillis());
	}	
	// -------------------------------------------------------
	/**
	 * Return current local time as Time
	 */
	public static java.sql.Time getCurrentTime(long millis)
	{
		return new java.sql.Time(millis);
	}	
	// -------------------------------------------------------
	/**
	 * Return current local time as Time
	 */
	public static java.sql.Time getCurrentTime()
	{
		return new java.sql.Time(getCurrentMillis());
	}
	// -------------------------------------------------------
	/**
	 * Return current local time as Date
	 */
	public static java.util.Date getCurrentDate()
	{
		return new java.util.Date();
	}	
	
	/**
	 * Returns the number of milliseconds since January 1, 1970, 00:00:00 GMT
	 * of the current time 
	 */ 
	public static long getCurrentMillis()
	{
		return getCurrentDate().getTime();
	}	

	// -------------------------------------------------------
	/**
	 * Return current local time as Calendar
	 */
	public static Calendar getCurrentCalendar()
	{
		return GregorianCalendar.getInstance();
	}	
	
	// -------------------------------------------------------
	/**
	 * returns the current date-time as a string in the format yyyy-mm-dd hh:mm:ss:m.
	 * this one was created for compatibility with class TimeDateField as a setable value
 	 */
	public static String getDateTimeYearFirst()
	{
		return getCurrentTimeStamp().toString();
	}//method
	
	// -------------------------------------------------------
	/**
	 * returns current local timestamp as a string
	 */
	public static String getCurrentTSString()
	{
		return getCurrentTimeStamp().toString();
	}
	
	// -------------------------------------------------------
	/**
	 * returns date with the current UTC date
	 */
	public static java.util.Date getCurrentUTCDate()
	{
		return convertLocalDateToUTC(new java.util.Date());
	}

	// -------------------------------------------------------
	/**
	 * returns date with the current UTC date
	 */
	public static java.util.Date convertLocalDateToUTC(java.util.Date localDate)
	{
		SimpleDateFormat dateFormatGmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		dateFormatGmt.setTimeZone(TimeZone.getTimeZone("GMT"));

		String formattedDateString = dateFormatGmt.format(localDate);

		SimpleDateFormat dateFormatLocal = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		//dateFormatGmt.setTimeZone(TimeZone.getTimeZone(timezoneStr));
		java.util.Date utcDate = null;
		try
		{
			utcDate = dateFormatLocal.parse(formattedDateString);
		} catch (ParseException ex)
		{
			Logger.log("ex->" + ex);
			Thread.dumpStack();
		}
		return utcDate;
	}

	// -------------------------------------------------------
	/**
	 * returns timestamp with the current UTC date
	 */
	public static java.sql.Timestamp getCurrentUTCTimestamp()
	{
		java.util.Date date = getCurrentUTCDate();
		return new java.sql.Timestamp(date.getTime());
	}
	
	// -------------------------------------------------------
	
	/**
	 * adds a date part to the date, equivalent to Calendar.add()
	 * @see java.text.Calendar
	 */
	public static java.util.Date	addDatePart(java.util.Date d, int field, int amount )
	{
		Calendar defCalendar = new GregorianCalendar();
		defCalendar.setTime(d);
		defCalendar.add(field, amount);
		d = defCalendar.getTime();
		return d;
	}

 	
	// -------------------------------------------------------
	/**
	 * Parses a date string acording to the format format.
	 * @see java.text.SimpleDateFormat
	 */
	 public static java.util.Date	parseDate(String dateStr, String format)
	 {	java.util.Date retVal = null;
			if (dateStr != null)
			{
				try
				{
					SimpleDateFormat sdf = new SimpleDateFormat(format);
					retVal = sdf.parse(dateStr);
				//System.out.println("DateUtility.parseDate(" + dateStr + ", " +format + ") Date="+retVal);
				}
				catch(ParseException  pe)
				{
					Logger.log("DateUtility.parseDate(" + dateStr + ") ParseException =>"+pe);
				}
				catch(IllegalArgumentException iae)
				{
					Logger.log("DateUtility.parseDate(" + dateStr + ") IllegalArgumentException =>"+iae);
				}
				catch(Exception e)
				{
					Logger.log("DateUtility.parseDate(" + dateStr + ") Other Exception =>"+e);
				}
			} // if (dateStr != null)
			return retVal;
	 }

	 // -------------------------------------------------------
	 /**
	  * Parses a date string acording to the format format.
	  * @see java.text.SimpleDateFormat
	  */
	  public static String	formatDate(java.util.Date date, DateFormat dateformat)
	  {	String retVal = null;
	 		try
	 		{	
	 			retVal = dateformat.format(date);
	 		}
//	 		catch(IllegalArgumentException iae)
	 		catch(Exception iae)	 		
			{
	 			Logger.log( "DateUtility.formatDate("+date+", " + dateformat.toString() + ") Other Exception =>"+iae);
			}
	 		return retVal;
	  }
	 // -------------------------------------------------------
	 /**
	  * Parses a date string acording to the format format.
	  * @see java.text.SimpleDateFormat
	  */
	  public static String	formatDate(java.util.Date date, String format)
	  {	return formatDate(date, new SimpleDateFormat(format));	  }
	 
	// -------------------------------------------------------
	/**
	 * Converts a string in the format fromFormat to the format toFormat.
	 * Supported formats must include mm, dd, and yyyy parts
	 * Time information(from the 10th char on) will be returned as it was in the dateString.
	 * For example, this method will allow you to convert the date 12/31/2000 to 2000-12-31
	 * with the call: convertFormat("12/31/2000", "mm/dd/yyyy", "yyyy-mm-dd");
	 */
	public static String convertFomat(String dateString, String fromFormat, String toFormat)
	{
		if( dateString == null || fromFormat == null || toFormat == null)
			return null;
		int yyStart = StringUtility.STRING_INVALID_POSSITION;
		int monthStart = fromFormat.indexOf("mm");
		int dayStart = fromFormat.indexOf("dd");
		int yyyyStart = fromFormat.indexOf("yyyy");
		
		if(yyyyStart == StringUtility.STRING_INVALID_POSSITION)
		{	yyStart = fromFormat.indexOf("yy");}
		
		String dayPart = null, monthPart = null, yearPart = null, timePart=null;
		if (monthStart != StringUtility.STRING_INVALID_POSSITION)
		{	monthPart = dateString.substring(monthStart, monthStart+2);}
		if (yyyyStart != StringUtility.STRING_INVALID_POSSITION)
		{	yearPart	= dateString.substring(yyyyStart, yyyyStart+4);}
		if (yyStart	 != StringUtility.STRING_INVALID_POSSITION)
		{	yearPart	= dateString.substring(yyStart	, yyStart	+2);}
		if (dayStart != StringUtility.STRING_INVALID_POSSITION)
		{	dayPart = dateString.substring(dayStart, dayStart+2);	}
	
		if (dateString.length() >10)
		{	timePart	= dateString.substring(10);}
		
		String returnString = null;
		if (monthPart != null)
		{	returnString =  StringUtility.searchReplace(toFormat, "mm", monthPart);	}
		if (dayPart != null)
		{	returnString =  StringUtility.searchReplace(returnString, "dd", dayPart);}
		if (yearPart != null)
		{	if(yyyyStart != StringUtility.STRING_INVALID_POSSITION)
			{	returnString =  StringUtility.searchReplace(returnString, "yyyy", yearPart);}
			if(yyStart != StringUtility.STRING_INVALID_POSSITION)	
			{	returnString =  StringUtility.searchReplace(returnString, "yy", yearPart);}
		}

		if (timePart != null)
		{	returnString += timePart;}
		
		return returnString;
	}
	
	// -------------------------------------------------------
	/**
	 * returns the requested Date part
	 * @param part	Date part to return
	 * @return int 
	 * @see Java.util.Calendar
 	 */
	public static int getDatePart(int part)
	{
		GregorianCalendar gc = new GregorianCalendar();
		return gc.get(part);
	}

	// -------------------------------------------------------
	/**
	 * returns the requested Date part
	 * @param part	Date part to return
	 * @return int
	 * @see Java.util.Calendar
 	 */
	public static int getDatePart(int part, java.util.Date date)
	{
		GregorianCalendar gc = new GregorianCalendar();
		gc.setTime(date);
		return gc.get(part);
	}

	// -------------------------------------------------------
	/**
	 * returns the requested Date part as a string. Optionally pre-pad the part with '0'
	 * @param cal		Calendar to use
	 * @param part	Date part to return
	 * @param minLength	Minimum length of the returned string
	 * @return int 
	 * @see Java.util.Calendar
 	 */
	public static String getDatePartString(Calendar cal, int part, int minLength)
	{
		String retVal = Integer.toString(cal.get(part));
		if(retVal != null)
		{
			retVal = retVal.trim();
			if(retVal.length() < minLength)
			{	retVal = StringUtility.padString(retVal, '0', minLength, true);}
		}
		return retVal;
	}
	
	////////////////////////////////////////////////////////////////////
	/**
	 * @see getTimeString
	 */
	public static String getCurrentTimeString(boolean twentyFourHour)
	{
		return getTimeString(new GregorianCalendar(), twentyFourHour);
	}
	
	////////////////////////////////////////////////////////////////////
	/**
	 * extacts the time part of the calendar given, returns is as a sting. 
	 * ex) if the time is 1:53pm, you would get eother 01:53pm or 13:53 
	 * depending on the option for twentyFourHour
	 */
	public static String getTimeString(Calendar cal, boolean twentyFourHour)
	{
		if( cal == null )
			return null;
		StringBuffer sb = new StringBuffer();
		
		int hour = cal.get(Calendar.HOUR);
		boolean pm = (cal.get(Calendar.AM_PM) == Calendar.PM);
		if( twentyFourHour == true && pm == true )
			hour += 12;
		if( hour < 10 )
			sb.append('0');

		sb.append(hour);
		sb.append(':');

		int min = cal.get(Calendar.MINUTE);
		if( min < 10 )
			sb.append('0');
		sb.append(min);
		
		if( !twentyFourHour )
		{
			if( pm )
				sb.append("pm");
			else
				sb.append("am");
		}
		
		return sb.toString();
	}
	
	// -------------------------------------------------------
	/**
	 * returns the current date-time as a string in the format: mm/dd/yyyy hh:mm:ss:m
 	 */
	public static String getDateTimeString()
	{
		return getDateTimeString( new GregorianCalendar());
	}
	
	/**
	 * returns the date as a string in the format: mm/dd/yyyy hh:mm:ss:m
 	 */
	public static String getDateTimeString(Calendar gc)
	{
		if( gc == null )
			return null;
		StringBuffer sb = new StringBuffer();
		
		int month = gc.get(Calendar.MONTH) + 1;//cal months are idexed at 0
		sb.append(month);
		sb.append('/');
		sb.append(gc.get(Calendar.DATE));
		sb.append('/');
		sb.append(gc.get(Calendar.YEAR));
		sb.append(' ');

		String timePart = getTimeString(gc, true);
		sb.append(timePart);
		
		return sb.toString();

	}//method
	
	// ---------------------------------------------------
	/**
	 * Get the number of days for a month
	 */
	private static int getLastDayofMonth(int month)
	{	int retVal = -1;
		//for some reason, the Calendar.MONTH numberings start at 0 in
		// JRE 1.3, this method accounts for that
		switch (month)
		{
			case Calendar.JANUARY:
			case Calendar.MARCH:
			case Calendar.MAY:
			case Calendar.JULY:
			case Calendar.AUGUST:
			case Calendar.OCTOBER:
			case Calendar.DECEMBER:
				retVal = 31;
				break;
			case Calendar.APRIL:
			case Calendar.JUNE:
			case Calendar.SEPTEMBER:
			case Calendar.NOVEMBER:
				retVal = 30;
				break;
			case Calendar.FEBRUARY:
				retVal = 28;
				break;
		}//switch
		
		return retVal;
	
	}//method
	
	
	// -------------------------------------------------------
	/**
	 * attempt to convert the object passed in to a Date
	 * will use stringToDate if it can't be done directly
	 * returns null if there is an error in the conversion
	 * @see stringToDate
	 */
	public static java.util.Date convertToUtilDate(Object toConvert)
	{
		if ( toConvert == null )
			return null;
		
		if( toConvert instanceof java.util.Date )
			return (java.util.Date) toConvert;
		
		if( toConvert instanceof Number )
		{
			long val = ((Number)toConvert).longValue();
			return new java.util.Date(val);
		}
		try
		{
			java.util.Date retVal = stringToDate(toConvert.toString());
			return retVal;
		}
		catch(java.text.ParseException pe)
		{
			System.err.println("Error converting date ->" + toConvert + " , object class->" + toConvert.getClass().getName());
			Logger.log(pe.getLocalizedMessage());
		}
		return null;
	}//method
	
	/*
	public static java.util.Date convertToUtilDate_nothrow(Object toConvert)
	{
		try
		{
			convertToUtilDate(toConvert);
		}
		catch(ParseException pe)
		{
			Logger.logException(pe);
			return null;
		}
	}
	 */

	public static java.util.Date stringToDate(String date)
		throws java.text.ParseException
	{
		return stringToDate(date, null);
	}
	
	// -------------------------------------------------------
	/**
	 * Takes a data string in an expected format and returns a Date. 
	 * The expected formats are:
	 * MMddyyyy' , 'MM/dd/yyyy , 'yyyy-MM-dd'
	 * Time data of the format HH:mm{:ss{SS}}{AM/PM} can also be included
	 * as per SimpleDateFormat#parseObject with leniency false
	 * @see SimpleDateFormat
	 * @throws java.text.ParseException
	 */
	public static java.util.Date stringToDate(String date, TimeZone tz)
		throws java.text.ParseException
	{
		if( date == null )
			return null;
		date = date.trim();
		date = date.toUpperCase();

		//check for "dow mon dd hh:mm:ss zzz yyyy" = format used on java.util.Date.toString()
		if( date.length() == 28 )
		{
			if( date.startsWith("SUN") || date.startsWith("MON") || date.startsWith("TUE") || date.startsWith("WED") || date.startsWith("THU") || date.startsWith("FRI") || date.startsWith("SAT") )
			{
				SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd hh:mm:ss zzz yyyy");
				//sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
				if( tz != null )
					sdf.setTimeZone(tz);
				sdf.setLenient(true); //lenient will do things like convert Feb 31 to Mar 3
				return (java.util.Date) sdf.parseObject(date);
			}
		}

		//in some cases were have timezone info tacked on.. ex XML Timestamp values like:2011-09-20T18:10:53-05:00
		//cut off the -05:00 in this cases..
		if(date.length() > 19 )
		{
			char char19 = date.charAt(19);
			//could be milliseconds values.. as well..
			if( char19 != ':' && char19 != '.' )
				date = date.substring(0, 19);
		}

		java.util.Date retVal = null;
		StringBuilder format = new StringBuilder();

		int slashIndex = date.lastIndexOf('/');
		int dashIndex = date.lastIndexOf('-');
		int timeIndex = date.indexOf(':');
		int amPMIndex = date.lastIndexOf("AM");
		if( amPMIndex < 0 )
			amPMIndex = date.lastIndexOf("PM");
		int tzGMTIndex = date.lastIndexOf("GMT");


		//for XML dates, well get 2008-01-01T00:00:00, removing the T allows it to parse normally
		if( date.indexOf("T") == 10 )
		{
			byte[]	bytes = date.getBytes();
			bytes[10] = ' ';
			date = new String(bytes);
		}
		if( dashIndex > -1 )
		{
			format.append("yyyy-MM-dd");
		}//if
		else if (slashIndex > -1)
		{
			if( date.length() == 8 )
				format.append("MM/dd/yy");
			else
				format.append("MM/dd/yyyy");
		}//if
		else if( date.length() >= 8 )
		{
			//2 general cases here, we probably have either MMddyyyy like: 02152006 or yyyyMMdd like 20050101
			//we can probably determine it by checking where the 1st 2 digits where either the year or month
			//would be.. if they are > 12, we know it's the year
			try
			{
				int tmpInt = Integer.parseInt(date.substring(0,2));
				int tmpInt2 = Integer.parseInt(date.substring(4,6));
				//Logger.log("tmpInt->" + tmpInt + ", tmpInt2->" + tmpInt2 );
				if( tmpInt > 12 )
					format.append("yyyyMMdd");
				else if ( tmpInt2 > 12 )
					format.append("MMddyyyy");
				else //in this case, we have a year less than 1200, this case is less likely
				{ //we can check the second 2 digits of the years then, so, look at the month fields
					int tmpInt3 = Integer.parseInt(date.substring(2,4));
					int tmpInt4 = Integer.parseInt(date.substring(6,8));
					if( tmpInt3 > 12 )
						format.append("yyyyMMdd");
					else if( tmpInt4 > 12 )
						format.append("MMddyyyy");
					else //cant figure it out, just use MMddyyyy
					{
						Logger.log("Cannot determine date format for date->" + date);
						Logger.log("tmpInt->" + tmpInt + ", tmpInt2->" + tmpInt2 + ", tmpInt3->" + tmpInt3 + ", tmpInt4->" + tmpInt4);
						Thread.dumpStack();
						format.append("MMddyyyy");
					}
				}//else
			}//try
			catch(NumberFormatException nfe)
			{
				Logger.log("caught NumberFormatException->" + nfe);
				Thread.dumpStack();
				format.append("MMddyyyy");
			}
		}//if
		else
			format.append("MMddyyyy");
		
		if( timeIndex > -1 )
		{
			format.append(" HH:mm");
			
			int secondsSep = date.indexOf(':', timeIndex+1);
			if( secondsSep > -1 )
			{
				format.append(":ss");
				
				int millisSep = date.indexOf(':', secondsSep+1);
				if( millisSep > -1 )
					format.append(":SS");
				
				//if we nanos, cut them off... SDF doesnt support
				int nanosSep = date.indexOf('.', secondsSep+1);
				{
					if( nanosSep > -1 )
						date = date.substring(0, nanosSep);
				}
			}//if
			
			if( amPMIndex > -1 )
				format.append(" a");

			if( tzGMTIndex > -1 )
				format.append(" z");
			
		}//if

		
		
		//System.out.println("format->" + format);
		//System.out.println("date->" + date);
		
		SimpleDateFormat sdf = new SimpleDateFormat(format.toString());
		//sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
		if( tz != null )
			sdf.setTimeZone(tz);
		sdf.setLenient(true); //lenient will do things like convert Feb 31 to Mar 3
		retVal = (java.util.Date) sdf.parseObject(date);

		return retVal;
	}
	
	//////////////////////////////////////////////////////////	
	/** 
	 * attempts to convert the object passed in to a java.sql.Timestamp,
	 */
	public static java.sql.Timestamp convertToTimestamp(Object value)
	{
		java.sql.Timestamp retVal = null;
		if( value == null )
			return retVal;
		
		//try
		{
			if ( value instanceof java.sql.Timestamp)
			{
				retVal = (java.sql.Timestamp) value;
			}//if
			else if( value instanceof java.util.Date)
			{
				long millis = ((java.util.Date)value).getTime();
				retVal = new java.sql.Timestamp(millis);
			}
			else
			{
				String strVal = value.toString();

				try
				{
					java.util.Date date = stringToDate(strVal);
					retVal = new java.sql.Timestamp(date.getTime());
				}
				catch(ParseException pe)
				{
					Logger.log("caught parseexception trying to convert to timestamp  value->" + value + ", pe->" + pe);
					pe.printStackTrace();
					return null;
				}
			}//else
		} // try
		//catch(NumberFormatException e)  ?? dont think anything here would throw a numberformatexception..
		{
			//	Logger.log("caught NumberFormatException->" + e);
		}
		return retVal;
	}
	
	//////////////////////////////////////////////////////////	
	/** 
	 * attempts to convert the object passed in to a java.sql.Time
	 */
	public static java.sql.Time convertToTime(Object value)
	{
		return convertToTime(value, true);
	}
	
	//////////////////////////////////////////////////////////	
	/** 
	 * attempts to convert the object passed in to a java.sql.Time,
	 * @param allowRollover - if false, values will not be allowed to roll over,
	 * ex) 11:60:00 can be rolled over to 12:00:00
	 * @throws java.lang.IllegalArgumentException if there in an error in 
	 * the conversion
	 */
	public static java.sql.Time convertToTime(Object value, boolean allowRollover)
	{
		if( value == null )
			return null;
		
		java.sql.Time retVal = null;
		
		if ( value instanceof java.sql.Time)
		{
			retVal = (java.sql.Time) value;
		}//if
		else
		{
			if ( value instanceof Number )
			{
				long val = ((Number)value).longValue();
				retVal = new java.sql.Time(val);
			}
			else
			{
				String strVal = value.toString();
				if ( !strVal.equals("") )
				{
					//check if we need to prepend a zero
					if ( strVal.charAt(1) == ':' )
						strVal = '0' + strVal;
					//check if we need to add on the millis
					if( strVal.length() < 8 )
						strVal += ":00";
					
					if ( allowRollover == false )
					{
						StringTokenizer st = new StringTokenizer(strVal, ":");
						if ( st.hasMoreElements() && Integer.parseInt((String) st.nextElement()) > 23 )
							throw new java.lang.IllegalArgumentException("Hour is invalid.");
						if ( st.hasMoreElements() && Integer.parseInt((String) st.nextElement()) > 59 )
							throw new java.lang.IllegalArgumentException("Minutes are invalid.");
						if( st.hasMoreElements() && Integer.parseInt((String) st.nextElement()) > 59 )
							throw new java.lang.IllegalArgumentException("Seconds are invalid.");
					}//if
						
					retVal = java.sql.Time.valueOf(strVal);
				}
			}
		}//else
		
		return retVal;
	}
	
	///////////////////////////////////////////////////////////////
	/**
	 * reformats from a date of the form 2002-01-01 12:33.00.0
	 * to 01/01/2002 12:33
	 * if the suppressTime parameter is true, the time will be 
	 * omitted.
	 */
	public static String formatDateString(String s, boolean suppressTime)
	{
		if( s == null )
			return null;

		//check if string is already formatted by the mask, 
		//or if its coming in unformatted from the DB
		//this is intended to allow the user to set the value
		//manully in the correct format
		if( s.indexOf('-') == -1 )
			return s;

		StringBuffer retVal = new StringBuffer(32);
		String year, month, date, hr, min;
		StringTokenizer st = new StringTokenizer(s, "- :");
		if( st.hasMoreTokens() && st.countTokens() > 2 )
		{
			year = st.nextToken();
			month = st.nextToken();
			date = st.nextToken();
			retVal.append(month).append("/").append(date).append("/").append(year);
			if( suppressTime == false )
			{
				hr = st.nextToken();
				min = st.nextToken();
				retVal.append(" ").append(hr).append(":").append(min);
			}//if
		}//if
		else
			return s;

		return retVal.toString();
	}//method
	
	/**
	 * returns a new Date
	 * truncates the time information off of a date, resetting it to
	 * the default of 00:00:00.0 (midnight)
	 */
	public static java.util.Date truncateTime(java.util.Date date)
	{
		if( date == null )
			return null;
		Calendar cal = new GregorianCalendar();
		cal.setTime(date);
		return truncateTime(cal).getTime();
	}//method
	
	/**
	 * returns a new Calendar
	 * truncates the time information off of a date, resetting it to
	 * the default of 00:00:00.0 (midnight)
	 */
	public static Calendar truncateTime(Calendar cal)
	{
		if( cal == null )
			return null;

		Calendar calendar = new GregorianCalendar();
		calendar.setTime(cal.getTime());
		//have to reset the time to AM, clearing the HOURoDAY doesnt do this
		calendar.set(Calendar.AM_PM, Calendar.AM);
		calendar.clear(Calendar.HOUR);
		calendar.clear(Calendar.HOUR_OF_DAY);
		calendar.clear(Calendar.MINUTE);
		calendar.clear(Calendar.SECOND);
		calendar.clear(Calendar.MILLISECOND);
		return calendar;
	}//method
	
	/**
	 * returns a new Date
	 * truncates the seconds and milliseconds information off of a date, resetting them to 0
	 */
	public static java.util.Date truncateSeconds(java.util.Date date)
	{
		if( date == null )
			return null;
		Calendar cal = new GregorianCalendar();
		cal.setTime(date);
		return truncateSeconds(cal).getTime();
	}//method

	/**
	 * returns a new Calendar
	 * truncates the seconds and milliseconds information off of a date, resetting them to 0
	 */
	public static Calendar truncateSeconds(Calendar cal)
	{
		if( cal == null )
			return null;
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(cal.getTime());
		calendar.clear(Calendar.SECOND);
		calendar.clear(Calendar.MILLISECOND);
		return calendar;
	}//method
	
	/**
	 * given a date, will return the number of complete years
	 * passed since then. For future dates, the number will be 
	 * negative or 0
	 */
	public static int calculateAge(java.util.Date dob)
	{
		return calculateYearsDiff(dob, getCurrentDate(), true);
	}//method
	
	/**
	 * returns the number of complete years passed between the two dates.
	 * if lessThanDate is greater than greaterThanDate, the result will be
	 * negative (or 0). Otherwise the result will be positive (or 0)
	 * @param ignoreTime - specifies whether to factor time into the caomparisspn or not. 
	 * for example, to calc a persons age, the time is generally not used
	 */
	public static int calculateYearsDiff(java.util.Date lessThanDate, java.util.Date greaterThanDate, boolean ignoreTime)
	{
		int monthsDiff = calculateMonthsDiff(lessThanDate, greaterThanDate, ignoreTime);
		return (int) ((double)monthsDiff / (double)12);
	}//method
	
	/**
	 * returns the number of complete months passed between the two dates.
	 * if lessThanDate is greater than greaterThanDate, the result will be
	 * negative (or 0). Otherwise the result will be positive (or 0)
	 * @param ignoreTime - specifies whether to factor time into the caomparisspn or not.
	 */
	public static int calculateMonthsDiff(java.util.Date lessThanDate, java.util.Date greaterThanDate, boolean ignoreTime)
	{
		if( lessThanDate == null )
			lessThanDate = new java.util.Date(0);
		
		if( greaterThanDate == null )
			greaterThanDate = new java.util.Date(0);
		
		//have to ensure the dates are ordered for the actualAge calc to be correct
		boolean flipFlopped = false;
		if( lessThanDate.after(greaterThanDate) )
		{
			java.util.Date tempD = lessThanDate;
			lessThanDate = greaterThanDate;
			greaterThanDate = tempD;
			flipFlopped = true;
		}//if
		
		if( ignoreTime )
		{
			lessThanDate = truncateTime(lessThanDate);
			greaterThanDate = truncateTime(greaterThanDate);
		}
		
		Calendar oldCal = new GregorianCalendar();
		oldCal.setTime(lessThanDate);
		
		Calendar newCal = new GregorianCalendar();
		newCal.setTime(greaterThanDate);
		
		Calendar testDate = new GregorianCalendar();
    testDate.setTime(lessThanDate);
		
		int yearsDiff = newCal.get(Calendar.YEAR)- oldCal.get(Calendar.YEAR);
		testDate.add(Calendar.MONTH, yearsDiff);
		yearsDiff = (testDate.after(newCal)) ? yearsDiff - 1 : yearsDiff;
		
		
		testDate.setTime(lessThanDate);
		int months = newCal.get(Calendar.MONTH)- oldCal.get(Calendar.MONTH);
		testDate.add(Calendar.MONTH, months);
		months = (testDate.after(newCal)) ? months - 1 : months;
		
		months += (12 * yearsDiff);
		
		if( flipFlopped )
			months = -months;
		
		return months;
	}//method
	
	// ------------------------------------------------------------
	/**
	 * calculate the difference between the two dates in terms of the returnUnit. 
	 * if greaterThanDate is grather than lessThanDate then the return will be positive, 
	 * otherwise it will be negative or 0. 
	 * @param ignoreTime - tells the conversion to ignore time information in the comparison, 
	 * which is often desirable when comparing days, months, and years
	 * @param returnUnit - the unit to use for the comparison, should be one of the DATE_PART
	 * constants from this class. 
	 * 
	 */
	public static long dateDiff(int returnUnit, java.util.Date lessThanDate, java.util.Date greaterThanDate, boolean ignoreTime)
	{
		if( lessThanDate == null )
			lessThanDate = new java.util.Date(0);
		
		if( greaterThanDate == null )
			greaterThanDate = new java.util.Date(0);
		
		if( ignoreTime )
		{
			lessThanDate = truncateTime(lessThanDate);
			greaterThanDate = truncateTime(greaterThanDate);
		}
		
		//since months and years dont have an exact conversion factor from milliseconds
		//ie leap years and variable length months, we have to calc them specially
		if( returnUnit == DATEPART_YEAR )
			return calculateYearsDiff(lessThanDate, greaterThanDate, false);
		if( returnUnit == DATEPART_MONTH )
			return calculateMonthsDiff(lessThanDate, greaterThanDate, false);
		
		//otherwise, we should be able to convert directly from the millis
		long millisApart = greaterThanDate.getTime() - lessThanDate.getTime();
		return (long)msToPart(returnUnit, millisApart);
		/*
		if( returnUnit == DATEPART_DAY )
			return (long) ((double)millisApart / (double)MILLISECONDS_PER_DAY);
		else if( returnUnit == DATEPART_HOUR )
			return (long) ((double)millisApart / (double)MILLISECONDS_PER_HOUR);
		else if( returnUnit == DATEPART_MINUTE )
			return (long) ((double)millisApart / (double)MILLISECONDS_PER_MINUTE);
		else if( returnUnit == DATEPART_SECOND )
			return (long) ((double)millisApart / (double)MILLISECONDS_PER_SECOND);
		return millisApart;
		 */
	}
	// ------------------------------------------------------------
	/** 
	 * Convert Milliseconds to another unit
	 * @param returnUnit	What to convert to
	 * @param ms	Milliseconds
	 * @return long other unit or ms
	 */
	public static double	msToPart(int returnUnit, long ms)
	{
		double retVal = ms;
		if( returnUnit == DATEPART_DAY )
		{	retVal=  (retVal / MILLISECONDS_PER_DAY);}
		else if( returnUnit == DATEPART_HOUR )
		{	retVal=  (retVal / MILLISECONDS_PER_HOUR);}
		else if( returnUnit == DATEPART_MINUTE )
		{	retVal=  (retVal / MILLISECONDS_PER_MINUTE);}
		else if( returnUnit == DATEPART_SECOND )
		{	retVal=   (retVal / MILLISECONDS_PER_SECOND);}
		return retVal;
	}
	
	////////////////////////////////////////////////////////////////////
	/**
	 * returns true iff the date given falls on a weekday
	 */
	public static boolean isWeekday(java.util.Date aDate)
	{
		if( aDate == null )
			throw new java.lang.NullPointerException();
		
		Calendar cal = new java.util.GregorianCalendar();
		cal.setTime(aDate);
		int dayOweek = cal.get(Calendar.DAY_OF_WEEK);
		if( dayOweek == Calendar.SATURDAY || dayOweek == Calendar.SUNDAY )
			return false;
		return true;
	}//method
	
	////////////////////////////////////////////////////////////////////
	/**
	 * convenience method to return the complete date as a string on the 24hr clock
	 */
	public static String getCompleteDateString(long millis)
	{
		java.sql.Timestamp ts = new java.sql.Timestamp(millis);
		return ts.toString();
	}
	
	////////////////////////////////////////////////////////////////////
	/**
	 * returns a new date which is set to the very last millisecond of the 
	 * day of the date passed in. All passed values are in millis for maximum flexibility
	 */
	public static long getEndOfDay(long millis)
	{
		Calendar cal = new GregorianCalendar();
		cal.setTimeInMillis(millis);
		cal = truncateTime(cal);
		//Logger.log("cal1->" + getCompleteDateString(cal.getTime()) );
		cal.add(Calendar.DATE, 1);
		//Logger.log("cal2->" + getCompleteDateString(cal.getTime()) );
		cal.add(Calendar.MILLISECOND, -1);
		//Logger.log("cal3->" + getCompleteDateString(cal.getTime()) );
		return cal.getTimeInMillis();
	}//method
	
	/**
	 * returns a new date which is set to the very last millisecond of the 
	 * minute of the date passed in. All passed values are in millis for maximum flexibility
	 */
	public static long getEndOfMinute(long millis)
	{
		Calendar cal = new GregorianCalendar();
		cal.setTimeInMillis(millis);
		//clear any seconds or millis
		cal.clear(Calendar.SECOND);
		cal.clear(Calendar.MILLISECOND);
		cal.add(Calendar.MINUTE, 1);
		cal.add(Calendar.MILLISECOND, -1);
		return cal.getTimeInMillis();
	}//method
	
	////////////////////////////////////////////////////////////////////
	/**
	 *
	 */
	public static String datePartToString(int part, int val)
	{
		String retVal = null;
		if( part == Calendar.MONTH)
		{
			switch (val)
			{
				case Calendar.JANUARY:
					retVal = "January";
					break;
				case Calendar.FEBRUARY:
					retVal = "February";
					break;
				case Calendar.MARCH:
					retVal = "March";
					break;
				case Calendar.APRIL:
					retVal = "April";
					break;
				case Calendar.MAY:
					retVal = "May";
					break;
				case Calendar.JUNE:
					retVal = "June";
					break;
				case Calendar.JULY:
					retVal = "July";
					break;
				case Calendar.AUGUST:
					retVal = "August";
					break;
				case Calendar.SEPTEMBER:
					retVal = "September";
					break;
				case Calendar.OCTOBER:
					retVal = "October";
					break;
				case Calendar.NOVEMBER:
					retVal = "November";
					break;
				case Calendar.DECEMBER:
					retVal = "December";
					break;
				default:
					retVal = "ERROR";
			}//switch
		}
		else if( part == Calendar.DAY_OF_WEEK )
		{
			switch (val)
			{
				case Calendar.MONDAY:
					retVal = "Monday";
					break;
				case Calendar.TUESDAY:
					retVal = "Tuesday";
					break;
				case Calendar.WEDNESDAY:
					retVal = "Wednesday";
					break;
				case Calendar.THURSDAY:
					retVal = "Thrusday";
					break;
				case Calendar.FRIDAY:
					retVal = "Friday";
					break;
				case Calendar.SATURDAY:
					retVal = "Saturday";
					break;
				case Calendar.SUNDAY:
					retVal = "Sunday";
					break;
				default:
					retVal = "ERROR";
			}//switch
		}
		else
		{
			retVal = Integer.toString(val);
			//throw new FeatureNotImplementedException("this datepart not explicitely supported yet, part->" + part);
			System.err.println("Warning, this datepart not explicitely supported yet, part->" + part +", returning tostring retVal->" + retVal);
		}
		return retVal;
	}//method
	
	////////////////////////////////////////////////////////////////////
	/**
	 * return the current year as an int
	 */
	public static int getCurrentYear()
	{
		Calendar defCalendar = new GregorianCalendar();
		defCalendar.setTimeInMillis(System.currentTimeMillis());
		return defCalendar.get(Calendar.YEAR);
	}//method

	////////////////////////////////////////////////////////////////////
	/**
	 * rounds the date passed in to the nearest 15 minute increment value
	 * NOTE: the seconds and milliseconds and truncated from the return value and not considered in the rounding process
	 */
	public static java.util.Date roundToNearest15MinuteIncrement(java.util.Date date)
	{
		if( date == null )
			return null;
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(date);
		//clear the seconds and millis
		calendar.clear(Calendar.SECOND);
		calendar.clear(Calendar.MILLISECOND);

		int hours = calendar.get(Calendar.HOUR);
		int minutes = calendar.get(Calendar.MINUTE);
		if( minutes <= 7 )
			minutes = 0;
		else if( minutes <= 22 )
			minutes = 15;
		else if( minutes <= 37 )
			minutes = 30;
		else if( minutes <= 52 )
			minutes = 45;
		else //need to round up to next hour
		{
			hours++;
			minutes = 0;
		}
		calendar.set(Calendar.HOUR, hours);
		calendar.set(Calendar.MINUTE, minutes);
		return calendar.getTime();
	}//method

	/**
	 * for the date given, return the timezone designation for the current locale for that date
	 * on a winXP machine, if the clock is set to adjust for DST, this will be a 3 digit code (ex CDT, CST, MST, etc)
	 * if the clock is not set to to adjust for DST, it will be a GMT offset, ex) GMT-06:00
	 */
	public static String getTimeZoneShortName(java.util.Date aDate)
	{
		Calendar cal = new GregorianCalendar();
		TimeZone tz = cal.getTimeZone();
		boolean dst = tz.inDaylightTime(aDate);
		return tz.getDisplayName(dst, TimeZone.SHORT);
	}
	
	////////////////////////////////////////////////////////////////////
	/**
	 * given a string for the TZ.. (CST, CDT, MST, etc..) returns the offset to UTC time
	 */
	public static int getUTCOffsetForTimezone(String timezoneStr)
	{
		int utcOffset = 0;
		if( "CST".equals(timezoneStr) )
			utcOffset = -6;
		else if( "CDT".equals(timezoneStr) )
			utcOffset = -5;
		else if( "MST".equals(timezoneStr) )
			utcOffset = -7;
		else if( "MDT".equals(timezoneStr) )
			utcOffset = -6;
		else if( "PST".equals(timezoneStr) )
			utcOffset = -8;
		else if( "PDT".equals(timezoneStr) )
			utcOffset = -7;
		else if( "EST".equals(timezoneStr) )
			utcOffset = -5;
		else if( "EDT".equals(timezoneStr) )
			utcOffset = -4;
		else if( "AKST".equals(timezoneStr) )
			utcOffset = -9;
		else if( "AKDT".equals(timezoneStr) )
			utcOffset = -8;
		else if( "HAST".equals(timezoneStr) )
			utcOffset = -10;
		else if( "HADT".equals(timezoneStr) )
			utcOffset = -9;
		else if( "GMT".equals(timezoneStr) )
			utcOffset = 0;
		else if( "UTC".equals(timezoneStr) )
			utcOffset = 0;
		else
		{
			//windows return "GMT-06:00" when the auto adjust for tx is turned off
			int idx = timezoneStr.indexOf("-");
			if( idx >=0 )
			{
				String tmp = timezoneStr.substring(idx, idx+3);
				try
				{
					idx = Integer.parseInt(tmp);
				}
				catch( NumberFormatException nfe )
				{
					Logger.log("FAILED TO CONVERT TIMEZONE  timezoneStr->" + timezoneStr);
					Logger.log("nfe->" + nfe);
				}
			}
			else
				Logger.log("FAILED TO CONVERT TIMEZONE  timezoneStr->" + timezoneStr);
		}
		return utcOffset;
	}//method

	/**
	 * for the date given, calculate the UTC date based on the local timezone
	 */
	public static java.util.Date calculateUTC(java.util.Date aDate)
	{
		return calculateUTC(aDate, TimeZone.getDefault());
	}

	/**
	 * for the date given and a timezone given, calculate the UTC date
	 */
	public static java.util.Date calculateUTC(java.util.Date aDate, String timezoneStr)
	{
		return calculateUTC(aDate, TimeZone.getTimeZone(timezoneStr));
	}

	/**
	 * for the date given and a timezone given, calculate the UTC date
	 */
	public static java.util.Date calculateUTC(java.util.Date aDate, TimeZone tz)
	{
		SimpleDateFormat dateFormatGmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		dateFormatGmt.setTimeZone(TZ_GMT);
		String formattedDateString = dateFormatGmt.format(aDate);
		//System.err.println("formattedDateString->" + formattedDateString);

		SimpleDateFormat dateFormatLocal = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		dateFormatGmt.setTimeZone(tz);
		java.util.Date utcDate = null;
		try
		{
			utcDate = dateFormatLocal.parse(formattedDateString);
		} catch (ParseException ex)
		{
			Logger.log("ex->" + ex);
			Thread.dumpStack();
		}
		return utcDate;
	}

	/**
	 * given a date representing a UTC value, return the date representing the local time
	 * NOTE: Date stores it's value internally in UTC already, so the date passed in
	 * would actually have an incorrect UTC value. For instance, if a string in UTC was used
	 * to create the date object useing DateFormat.parse(String s)
	 * Essentially we are converting it to the correct value
	 * with the function.
	 */
	public static java.util.Date calculateLocalTime(java.util.Date utcDate)
	{
		SimpleDateFormat dateFormatLocal = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String formattedDateString = dateFormatLocal.format(utcDate);
		//System.err.println("formattedDateString->" + formattedDateString);

		SimpleDateFormat dateFormatUTC = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		dateFormatUTC.setTimeZone(TimeZone.getTimeZone("GMT"));
		java.util.Date localDate = null;
		try
		{
			localDate = dateFormatUTC.parse(formattedDateString);
		} catch (ParseException ex)
		{
			Logger.log("ex->" + ex);
			Thread.dumpStack();
		}
		return localDate;

	}

	////////////////////////////////////////////////////////////////////
	/**
	 * returns a date that is equal to midnight on the first day of the week for the week the given date falls in
	 */
	public static java.util.Date getFirstDateOfWeek(java.util.Date aDate)
	{
		Calendar defCalendar = new GregorianCalendar();
		defCalendar.setTime(aDate);
		Calendar cal = truncateTime(defCalendar);

		int dow = cal.get(Calendar.DAY_OF_WEEK);
		int daysOffset = dow - Calendar.SUNDAY; //assumes the days of week constancts are consecutive (which they are)
		if( daysOffset > 0 )
			cal.add(Calendar.DATE, -daysOffset);

		return cal.getTime();
	}//method
	
	////////////////////////////////////////////////////////////////////
	/**
	* returns jan 1, midnight for the year of the given date
	*/
	public static java.util.Date getFirstDateOfYear(java.util.Date aDate)
	{
		Calendar defCalendar = new GregorianCalendar();
		defCalendar.setTime(aDate);
		Calendar cal = truncateTime(defCalendar);
		int doyOffset = cal.get(Calendar.DAY_OF_YEAR) - 1; //first day of year is 1
		if( doyOffset > 0 )
			cal.add(Calendar.DATE, -doyOffset);
		return cal.getTime();
	}//method

	////////////////////////////////////////////////////////////////////
	/**
	* returns dec 31, 23:59 for the year of the given date
	*/
	public static java.util.Date getLastDateOfYear(java.util.Date aDate)
	{
		Calendar defCalendar = new GregorianCalendar();
		defCalendar.setTime(aDate);
		Calendar cal = truncateTime(defCalendar);
		int nextYear = cal.get(Calendar.YEAR) + 1;
		cal.clear(); //reset everything
		cal.set(Calendar.YEAR, nextYear); //set the year, this should put us a midnight, jan1 of next year
		cal.add(Calendar.MILLISECOND, -1); //back off a milli, which should be the last millisecond of the prior year
		return cal.getTime();
	}//method
	
	////////////////////////////////////////////////////////////////////
	/**
	* returns the first date of the month, with time set to 00:00, for the date given
	*/
	public static java.util.Date getFirstDateOfMonth(java.util.Date aDate)
	{
		Calendar cal = new GregorianCalendar();
		cal.setTime(aDate);
		cal = truncateTime(cal);
		cal.set(Calendar.DAY_OF_MONTH, 1);
		return cal.getTime();
	}//method

	////////////////////////////////////////////////////////////////////
	/**
	* returns the last date of the month, with time set to 23:59 for the date given
	*/
	public static java.util.Date getLastDateOfMonth(java.util.Date aDate)
	{
		aDate = getFirstDateOfMonth(aDate);
		Calendar cal = new GregorianCalendar();
		cal.setTime(aDate);
		int nextMonth = cal.get(Calendar.MONTH) + 1;
		cal.set(Calendar.MONTH, nextMonth);
		cal.add(Calendar.MILLISECOND, -1); //back off a milli, which should be the last millisecond of the prior month
		return cal.getTime();
	}//method
	
	////////////////////////////////////////////////////////////////////
	/**
	 * return the number of hours (fractional) between the startDate and endDate that fall between the hours specified by hourStart and hourEnd
	 */
	public static double getHoursBetween(java.util.Date startDate, java.util.Date endDate, int hourStart, int hourEnd)
	{
		long minutesBetween = getMinutesBetween(startDate, endDate, hourStart, hourEnd);
		return (double) minutesBetween / (double) 60;
	}

	////////////////////////////////////////////////////////////////////
	/**
	 * return the number of minutes between the startDate and endDate that fall between the hours specified by hourStart and hourEnd
	 */
	public static long getMinutesBetween(java.util.Date startDate, java.util.Date endDate, int hourCountStart, int hourCountStop)
	{

		if( startDate.compareTo(endDate) > 0 )
		{
			//return a negative value?
			//return 0-getMinutesBetween(endDate, startDate, hourCountStart, hourCountStop);
			return 0;
		}

		if( hourCountStart > hourCountStop )
			return getMinutesBetween(startDate, endDate, hourCountStart, 24) + getMinutesBetween(startDate, endDate, 0, hourCountStop);

		if( hourCountStart == hourCountStop )
		{
			return 0;
		}

		long minutesIn = 0;

		Calendar startCal = new GregorianCalendar();
		startCal.setTime(startDate);

		Calendar endCal = new GregorianCalendar();
		endCal.setTime(endDate);

		Calendar countStartCal = truncateTime(startCal);
		countStartCal.set(Calendar.HOUR_OF_DAY, hourCountStart);

		int julianStart = getJulian(startCal);
		int julianEnd = getJulian(endCal);

		//figure out where to start counting from for this day
		int startCountHour = Math.max(hourCountStart, startCal.get(Calendar.HOUR_OF_DAY));

		//if there are minutes set for the first hour and it is within range.. add the minutes
		//in and reset to the next whole hour
		int startMinutes = startCal.get(Calendar.MINUTE);

		if( startMinutes > 0 )
		{
			int startCalHour = startCal.get(Calendar.HOUR_OF_DAY);
			if ( startCalHour >= hourCountStart && startCalHour < hourCountStop)
			{
				if( julianStart == julianEnd && startCalHour == endCal.get(Calendar.HOUR_OF_DAY) )
				{
					return endCal.get(Calendar.MINUTE) - startMinutes;
				}

				minutesIn += (60-startMinutes);
			}
			startCal = truncateTime(startCal);
			startCal.set(Calendar.HOUR_OF_DAY, startCalHour+1);
			//since we reset the hour.. we want to start over since it could throw some invariants off
			return minutesIn + getMinutesBetween(startCal.getTime(), endDate, hourCountStart, hourCountStop);
		}

		//if the end date is not the same date as today, set the end hour to the hourcountstop.. otherwise we want the min
		//of that or the ending time
		int stopCountHour = hourCountStop;
		if( julianStart == julianEnd )
			stopCountHour = Math.min(hourCountStop, endCal.get(Calendar.HOUR_OF_DAY));

		if( startCountHour >= hourCountStart && startCountHour < hourCountStop && startCountHour < stopCountHour)
			minutesIn += (stopCountHour - startCountHour) * 60;

		//if we span more than one day, increment to the next day and call recursively
		if( getJulian(startCal) != getJulian(endCal) )
		{
			int doy = startCal.get(Calendar.DAY_OF_YEAR);
			Calendar newStartCal = truncateTime(startCal);
			newStartCal.set(Calendar.DAY_OF_YEAR, doy+1);
			return minutesIn + getMinutesBetween(newStartCal.getTime(), endDate, hourCountStart, hourCountStop);
		}

		//if there are still minutes left that are within range.. add them in
		int stopHour = endCal.get(Calendar.HOUR_OF_DAY);
		if( stopHour >= hourCountStart && stopHour < hourCountStop )
			minutesIn += endCal.get(Calendar.MINUTE);

		//int startHour = startCal.get(Calendar.HOUR_OF_DAY);
		//int endHour = startCal.get(Calendar.HOUR_OF_DAY);
		return minutesIn;
	}
	////////////////////////////////////////////////////////////////////
	/**
	 *
	 */
	/*public static void testMinutesBetween() throws ParseException
	{
		java.util.Date d1 = DateUtility.stringToDate("2009-12-18 12:00:00.0");
		java.util.Date d2 = DateUtility.stringToDate("2009-12-18 12:15:00.0");
		System.err.println("getHoursBetween 0 ->" + getHoursBetween(d1, d2, 0, 12));

		d1 = DateUtility.stringToDate("2009-12-18 12:15:00.0");
		d2 = DateUtility.stringToDate("2009-12-18 13:45:00.0");
		System.err.println("getHoursBetween 0 ->" + getHoursBetween(d1, d2, 0, 12));

		d1 = DateUtility.stringToDate("2009-12-18 23:30:00.0");
		d2 = DateUtility.stringToDate("2009-12-19 00:45:00.0");
		System.err.println("getHoursBetween .75->" + getHoursBetween(d1, d2, 0, 12));

		d1 = DateUtility.stringToDate("2009-12-18 12:45:00.0");
		d2 = DateUtility.stringToDate("2009-12-19 14:15:00.0");
		System.err.println("getHoursBetween 12->" + getHoursBetween(d1, d2, 0, 12));

		d1 = DateUtility.stringToDate("2009-12-18 09:45:00.0");
		d2 = DateUtility.stringToDate("2009-12-19 14:15:00.0");
		System.err.println("getHoursBetween 14.25->" + getHoursBetween(d1, d2, 0, 12));

		d1 = DateUtility.stringToDate("2009-12-18 09:00:00.0");
		d2 = DateUtility.stringToDate("2009-12-18 14:00:00.0");
		System.err.println("getHoursBetween 5->" + getHoursBetween(d1, d2, 6, 18));

		d1 = DateUtility.stringToDate("2009-12-18 04:30:00.0");
		d2 = DateUtility.stringToDate("2009-12-18 14:00:00.0");
		System.err.println("getHoursBetween 8->" + getHoursBetween(d1, d2, 6, 18));

		d1 = DateUtility.stringToDate("2009-12-18 15:30:00.0");
		d2 = DateUtility.stringToDate("2009-12-18 19:30:00.0");
		System.err.println("getHoursBetween 2.5->" + getHoursBetween(d1, d2, 6, 18));

		d1 = DateUtility.stringToDate("2009-12-18 18:00:00.0");
		d2 = DateUtility.stringToDate("2009-12-18 19:30:00.0");
		System.err.println("getHoursBetween 0->" + getHoursBetween(d1, d2, 6, 18));

		d1 = DateUtility.stringToDate("2009-12-18 00:00:00.0");
		d2 = DateUtility.stringToDate("2009-12-21 00:00:00.0");
		System.err.println("getHoursBetween 36->" + getHoursBetween(d1, d2, 6, 18));

		d1 = DateUtility.stringToDate("2009-12-18 00:00:00.0");
		d2 = DateUtility.stringToDate("2009-12-21 23:59:00.0");
		System.err.println("getHoursBetween 48->" + getHoursBetween(d1, d2, 6, 18));

		d1 = DateUtility.stringToDate("2009-12-21 04:00:00.0");
		d2 = DateUtility.stringToDate("2009-12-21 10:00:00.0");
		System.err.println("getHoursBetween 0->" + getHoursBetween(d1, d2, 12, 0));

		d1 = DateUtility.stringToDate("2009-12-21 23:00:00.0");
		d2 = DateUtility.stringToDate("2009-12-22 00:15:00.0");
		System.err.println("getHoursBetween .25->" + getHoursBetween(d1, d2, 0, 12));

		d1 = DateUtility.stringToDate("2009-12-18 07:15:00.0");
		d2 = DateUtility.stringToDate("2009-12-18 07:30:00.0");
		System.err.println("getHoursBetween .25 ->" + getHoursBetween(d1, d2, 0, 12));

		d1 = DateUtility.stringToDate("2009-12-18 07:15:00.0");
		d2 = DateUtility.stringToDate("2009-12-18 08:30:00.0");
		System.err.println("getHoursBetween 1.25 ->" + getHoursBetween(d1, d2, 0, 12));

		d1 = DateUtility.stringToDate("2009-12-18 07:15:00.0");
		d2 = DateUtility.stringToDate("2009-12-18 08:30:00.0");
		System.err.println("getHoursBetween .75 ->" + getHoursBetween(d1, d2, 0, 8));

		d1 = DateUtility.stringToDate("2009-12-18 07:15:00.0");
		d2 = DateUtility.stringToDate("2009-12-18 07:30:00.0");
		System.err.println("getHoursBetween 0 ->" + getHoursBetween(d1, d2, 7, 7));
		
	}//method*/

	////////////////////////////////////////////////////////////////////
	/**
	 * convenience to return a date string formatted like yyyyMMdd using SimpleDateFormat class
	 */
	public static String getFormattedDateStringYYYYMMDD(java.util.Date aDate)
	{
		if( aDate == null )
			return null;
		String formatString = "yyyyMMdd";
		SimpleDateFormat sdf = new SimpleDateFormat(formatString);
		//sdf.setLenient(true); //lenient will do things like convert Feb 31 to Mar 3
		String retVal = sdf.format(aDate);
		return retVal;
	}//method
	
	
	/**
	 * convenience to return data formated like: yyyy-MM-dd HH:mm
	 */
	public static String getFormattedDateStringYearFirst(java.util.Date aDate, boolean includeTime)
	{
		if( aDate == null )
			return null;
		String formatString = "yyyy-MM-dd";
		
		if( includeTime )
			formatString += " HH:mm";
		
		SimpleDateFormat sdf = new SimpleDateFormat(formatString);
		//sdf.setLenient(true); //lenient will do things like convert Feb 31 to Mar 3
		String retVal = sdf.format(aDate);
		return retVal;
	}//method
	
	
	////////////////////////////////////////////////////////////////////
	/**
	 * returns true iff the two dates represent the same day
	 */
	public static boolean isSameDay(java.util.Date d1, java.util.Date d2)
	{
		Calendar c1 = new GregorianCalendar();
		Calendar c2 = new GregorianCalendar();
		c1.setTime(d1);
		c2.setTime(d2);
		if( c1.get(Calendar.DAY_OF_YEAR) == c2.get(Calendar.DAY_OF_YEAR) && c1.get(Calendar.YEAR) == c2.get(Calendar.YEAR) )
			return true;
		return false;
	}//method
	
	////////////////////////////////////////////////////////////////////
	/**
	 * from a given date, return the next midnight. 
	 */
	public static java.util.Date getNextMidnight(java.util.Date date)
	{
		java.util.Date retVal = DateUtility.truncateTime(date);
		//if( retVal.equals(date) )
		//	return retVal;
		retVal = DateUtility.addDatePart(retVal, Calendar.DATE, 1);
		return retVal;
	}//method

	////////////////////////////////////////////////////////////////////
	/**
	 * serializes a date to string format
	 */
	public static String serializeDate(java.util.Date d)
	{
		if( d == null )
			return null;
		return new SimpleDateFormat(UTIL_DATE_TO_STRING_SIMPLEDATEFORMAT).format(d);
	}//method

	////////////////////////////////////////////////////////////////////
	/**
	 * deserializes a string produced with serializeDate
	 */
	public static java.util.Date deSerializeDateString(String s)
	{
		if( s == null )
			return null;
		try
		{
			return new SimpleDateFormat(UTIL_DATE_TO_STRING_SIMPLEDATEFORMAT).parse(s);
		} catch (ParseException ex)
		{
			Logger.log("Unexpected exception with date format: s-> " + s + "< + ex->" + ex);
			ex.printStackTrace();
			return null;
		}
	}//method

	////////////////////////////////////////////////////////////////////
	/**
	 *
	 */
	public static String getDateDisplayString(java.util.Date d, boolean includeTime)
	{
		String dateFormat = "yyyy-MM-dd";
		if( includeTime )
			dateFormat += " HH:mm";
		SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
		return sdf.format(d);
	}//method
	
	////////////////////////////////////////////////////////////////////
	/**
	 * tests whether the date is between the start and end dates (inclusive)
	 * @param dateToCheck
	 * @param beginDate
	 * @param endDate
	 * @param ignoreTime tells it to truncate the times on all the dates prior to comparison
	 */
	public static boolean isDateBetween(java.util.Date dateToCheck, java.util.Date beginDate, java.util.Date endDate, boolean ignoreTime)
	{
		if( ignoreTime )
		{
			dateToCheck = truncateTime(dateToCheck);
			beginDate = truncateTime(beginDate);
			endDate = truncateTime(endDate);
		}
		
		if( dateToCheck.before(beginDate) )
			return false;
		
		if( dateToCheck.after(endDate) )
			return false;
		
		return true;
	}
	
	/**
	 * takes a date and returns it formatted for use in an HTTP query, which should be of the format 
	 */
	public static String getFormattedDateStringHTTP(java.util.Date d)
	{
		//String formatString = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX";
		//String formatString = "yyyy-MM-dd'T'HH:mm:ssXXX";
		//DateFormat df = new SimpleDateFormat(formatString);
		
		String s = new SimpleDateFormat(DATETIME_FORMAT_STR_HTTP_NTZ).format(d);
		//System.err.println("s->" + s);
		return s;
	}
	
	/**
	 * returns a string in the form HH:mm
	 */
	public static String getTimeString24Hour(Date d)
	{
		if( d == null )
			return null;
		
		/*Calendar c = new GregorianCalendar();
		c.setTime(d);
		StringBuilder sb = new StringBuilder();
		int hour = c.get(Calendar.HOUR_OF_DAY);
		int minute = c.get(Calendar.MINUTE);
		if( hour < 10 )
			sb.append("0");
		sb.append(hour);
		sb.append(":");
		if( minute < 10 )
			sb.append("0");
		sb.append(minute);
		return sb.toString();
		*/
		String s = new SimpleDateFormat("HH:mm").format(d);
		return s;
	}
	
	////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////
	public static void main(String[] args)
		throws java.text.ParseException
	{
		System.err.println("ooo->" + DateUtility.convertToTimestamp("19790224"));
		//dow mon dd hh:mm:ss zzz yyyy
		//String dateStr = new java.util.Date().toString();
		//System.err.println("ooo->" + convertToUtilDate(dateStr));
		
		getTimeString24Hour(new Date());
		
		System.err.println("ooo->" + getFirstDateOfMonth(new java.util.Date()));
		System.err.println("ooo->" + getLastDateOfMonth(new java.util.Date()));
		
		
		getFormattedDateStringHTTP(new java.util.Date());


		//testMinutesBetween();
		
		/*java.util.Date date1 = DateUtility.stringToDate("2011-11-07 18:00");
		java.util.Date date2 = DateUtility.stringToDate("2011-11-08 02:00");
		long minutesBetween = test(date1, date2, 6, 18);
		System.err.println("minutesBetween->" + minutesBetween);*/




		/*java.util.Date date2 = DateUtility.stringToDate("2011-09-20T18:23:33-05:00");

		Object o = calculateUTC(new java.util.Date(), "CDT");
		System.err.println("o->" + o);

		Logger.log("getCurrentUTCDate->" + getCurrentUTCDate());
		Logger.log("calculateLocalTime->" + calculateLocalTime(getCurrentUTCDate()));

		java.util.Date date = DateUtility.stringToDate("2010-11-07 00:00:00.0");
		date = calculateUTC(date, "CDT");
		//date = calculateLocalTime(date);
		System.err.println("date->" + date);


		date = DateUtility.stringToDate("2010-11-07 23:00:00.0");
		Calendar cal = new GregorianCalendar();
		cal.setTime(date);
		System.err.println("getJulian(cal);->" + getJulian(cal));

		date = DateUtility.stringToDate("2010-11-08 02:00:00.0");
		cal.setTime(date);
		System.err.println("getJulian(cal);->" + getJulian(cal));
		*/

	}

	
	public static void xmain(String[] args)
		throws java.text.ParseException
	{
		
		Calendar cal = new GregorianCalendar();
		java.util.Date date = DateUtility.stringToDate("2010-04-23 23:00:00.0");
		


		//java.util.Date date = DateUtility.stringToDate("2000-01-01 01:00:00.0");
		cal.setTime(date);

		/*System.err.println("ooo->" + cal.get(Calendar.ZONE_OFFSET) / MILLISECONDS_PER_HOUR);
		System.err.println("ooo->" + cal.get(Calendar.DST_OFFSET) / MILLISECONDS_PER_HOUR);
		//System.err.println("ooo->" + cal.get(Calendar.get);

		date = DateUtility.stringToDate("2010-01-01 01:00:00.0");
		cal.setTime(date);
		System.err.println("julian for " + date + " " +getJulianOffsetY2K(cal));

		date = DateUtility.stringToDate("2000-01-01 12:00:00.0");
		cal.setTime(date);
		System.err.println("julian for " + date + " " +getJulianOffsetY2K(cal));

		
		date = DateUtility.stringToDate("2000-01-01 23:00:00.0");
		cal.setTime(date);
		System.err.println("julian for " + date + " " +getJulianOffsetY2K(cal));*/

		//2451545

		cal = new GregorianCalendar();
		//cal.set(Calendar.ZONE_OFFSET, Calendar.ZONE_OFFSET);
		//cal.set(Calendar.DST_OFFSET, Calendar.DST_OFFSET);
		System.err.println(cal.getTime());

		//long currentMillisOffset = System.currentTimeMillis() + Calendar.ZONE_OFFSET + Calendar.DST_OFFSET;
		long currentMillisOffset = System.currentTimeMillis();
		System.err.println("ooo->" + new java.util.Date(currentMillisOffset));

		currentMillisOffset = System.currentTimeMillis() - cal.get(Calendar.ZONE_OFFSET) - cal.get(Calendar.DST_OFFSET);
		System.err.println("ooo->" + new java.util.Date(currentMillisOffset));
		cal.setTimeInMillis(currentMillisOffset);
		System.err.println("ooo->" + cal.getTime());

		cal = new GregorianCalendar();
		cal.setTimeZone(TimeZone.getTimeZone("GMT"));
		System.err.println("offset->" + cal.get(Calendar.ZONE_OFFSET));
		cal.setTimeInMillis(System.currentTimeMillis());
		System.err.println("ooo->" + cal.getTime());

		date = DateUtility.stringToDate("2000-02-01 23:00:00.0");
		cal.setTimeZone(TimeZone.getTimeZone("GMT"));
		cal.setTime(date);
		System.err.println("ooo->" + cal.get(Calendar.DATE));
		System.err.println("ooo->" + cal.getTime());
	}
	
	
} // class

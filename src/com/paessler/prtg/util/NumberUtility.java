package com.paessler.prtg.util;

import java.math.BigDecimal;

/******************************************************************************
 *  A library of static math utility functions
 * @author  JR Andreassen
 *  @version 0.1
 *****************************************************************************/
public abstract class NumberUtility
{

	

	//////////////////////////////////////////////////////////
	/**
	 * Parse integer from String.
	 * Convinience method to catch exceptions etc.
	 */	
	public static long getLong(String str, long defval)
	{
		long retVal = defval;
		try
		{
			retVal = Long.parseLong(str);
		}
		catch(NumberFormatException nfe) {}
		
		return retVal;
	}

	//////////////////////////////////////////////////////////
	/**
	 * Parse integer from String.
	 * Convinience method to catch exceptions etc.
	 */	
	public static int getInt(String str, int defval)
	{
		int retVal = defval;
		try
		{
			retVal = Integer.parseInt(str);
		}
		catch(NumberFormatException nfe) {}
		
		return retVal;
	}
	
	//////////////////////////////////////////////////////////
	
	/**
	 * Parse integer from String.
	 * Convinience method to catch exceptions etc.
	 */	
	public static Integer getInteger(String str, int defval)
	{
		Integer retVal = new Integer(defval);
		try
		{
			retVal = Integer.valueOf(str);
		}
		catch(NumberFormatException nfe) {}
		
		return retVal;
	}

	//////////////////////////////////////////////////////////
	/** 
	 *	attempts to convert the object passed in to a BigDecimal.
	 *	The conversion may throw a java.lang.NumberFormatException
	 *	if the type cannot be converted.
	 *	It is left uncaught so the user can decicde how to deal with it.
	 */
	public static BigDecimal convertToBigDecimal(Object value)
	{
		//java.lang.NumberFormatException
		BigDecimal retVal = null;
		if( value == null )
			return retVal;
		
		//System.err.println("value.getClass().getName()->" + value.getClass().getName());
		//System.err.println("value->" + value);
		
		if ( value instanceof BigDecimal )
		{
			retVal = (BigDecimal) value;
		}
		else if( value instanceof Number )
		{
			//NOTE, have seen this issue come and go under different jre versions.. but in some cases using the double constructor
			//for big decimal will change the data..
			//ex) doulbe -97.75389  changes to -97.75389099121094
			//using the string constrctor is more accurate..
			//retVal = new BigDecimal(((Number)value).doubleValue());
			retVal = new BigDecimal(((Number)value).toString());
		}
		else
		{
			//remove commas and dollar signs from the string value
			String strVal = MathUtility.cleanUpNumberString(value.toString());
			retVal = new BigDecimal(strVal);
		}//if
		
		return retVal;
	}

	//////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////
	/**
	 *	attempts to convert the object passed in to a Long.
	 *	The conversion may throw a java.lang.NumberFormatException if the type cannot be converted.
	 *	It is left uncaught so the user can decicde how to deal with it.
	 *	NOTE: if the value is any type of number, it will be cast possibly resulting in loss of precision without
	 *	throwing an exception
	 */
	public static Long convertToLong(Object value)
	{
		//java.lang.NumberFormatException
		Long retVal = null;
		if( value == null )
			return retVal;

		//System.err.println("value.getClass().getName()->" + value.getClass().getName());
		//System.err.println("value->" + value);

		if ( value instanceof Long )
		{
			retVal = (Long) value;
		}
		else if ( value instanceof Number )
		{
			retVal = new Long(((Number)value).longValue());
		}
		else if ( value instanceof Boolean )
		{
			//there are cases where a int field is used for a boolean, so we might
			//need to convert a boolean to an int.
			retVal = new Long( ((Boolean)value).booleanValue() ? 1 : 0 );
		}
		else
		{
			//remove commas and dollar signs from the string value
			String strVal = MathUtility.cleanUpNumberString(value.toString());
			Double d = Double.valueOf(strVal); //convert to double first so values like 1.0 will convert
			retVal = Long.valueOf(d.longValue());
		}//if

		return retVal;
	}
	//////////////////////////////////////////////////////////
	
	/** 
	 *	attempts to convert the object passed in to a Double.
	 *	The conversion may throw a java.lang.NumberFormatException
	 *	if the type cannot be converted.
	 *	It is left uncaught so the user can decicde how to deal with it.
	 */
	public static Double convertToDouble(Object value)
	{
		Double retVal = null;
		if( value == null )
			return retVal;
		
		if ( value instanceof Double )
		{
			retVal = (Double) value;
		}
		else if ( value instanceof Number )	
		{
			retVal = new Double(((Number)value).doubleValue());
		}
		else
		{
			//remove commas and dollar signs from the string value
			String strVal = MathUtility.cleanUpNumberString(value.toString());
			retVal = Double.valueOf(strVal);
		}//if
		
		return retVal;
	}
	
	//////////////////////////////////////////////////////////
	
	/** 
	 *	attempts to convert the object passed in to a Float.
	 *	The conversion may throw a java.lang.NumberFormatException
	 *	if the type cannot be converted.
	 *	It is left uncaught so the user can decicde how to deal with it.
	 */
	public static Float convertToFloat(Object value)
	{
		Float retVal = null;
		if( value == null )
			return retVal;
		
		if ( value instanceof Float )
		{
			retVal = (Float) value;
		}
		else if ( value instanceof Number )	
		{
			retVal = new Float(((Number)value).floatValue());
		}
		else
		{
			//remove commas and dollar signs from the string value
			String strVal = MathUtility.cleanUpNumberString(value.toString());
			retVal = Float.valueOf(strVal);
		}//if
		
		return retVal;
	}
	
	//////////////////////////////////////////////////////////
	/**
	 *	attempts to convert the object passed in to a Integer.
	 *	The conversion may throw a java.lang.NumberFormatException
	 *	if the type cannot be converted.
	 *	It is left uncaught so the user can decicde how to deal with it.
	 */
	public static Byte convertToByte(Object value, byte defVal)
	{
		Byte retVal = new Byte(defVal);
		if( value == null )
			return retVal;

		if ( value instanceof Byte )
		{
			retVal = (Byte) value;
		}
		else if ( value instanceof Number )
		{
			retVal = new Byte(((Number)value).byteValue());
		}
		else if ( value instanceof Boolean )
		{
			//there are cases where a int field is used for a boolean, so we might
			//need to convert a boolean to an int.
			retVal = new Byte( (byte)( ((Boolean)value).booleanValue() ? 1 : 0 ));
		}
		else
		{
			//remove commas and dollar signs from the string value
			String strVal = MathUtility.cleanUpNumberString(value.toString());
			retVal = Byte.valueOf(strVal);
		}//if

		return retVal;
	}
	//////////////////////////////////////////////////////////
	/**
	 *	attempts to convert the object passed in to a primitive int.
	 *	if there is an error in the conversion, defVal will be returned
	 */
	public static byte tobyte(Object value, byte defVal)
	{
		byte retVal = defVal;
		try
		{
			Byte ret = convertToByte(value, defVal);
			if (ret != null)
				retVal = ret.byteValue();
		}
		catch(java.lang.NumberFormatException nfe)
		{
			//Log.logException(nfe);
		}
		return retVal;
	}
	//////////////////////////////////////////////////////////
	/**
	 *	attempts to convert the object passed in to a Integer.
	 *	The conversion may throw a java.lang.NumberFormatException
	 *	if the type cannot be converted.
	 *	It is left uncaught so the user can decicde how to deal with it.
	 */
	public static Short convertToShort(Object value, short defVal)
	{
		Short retVal = new Short(defVal);
		if( value == null )
			return retVal;

		if ( value instanceof Short )
		{
			retVal = (Short) value;
		}
		else if ( value instanceof Number )
		{
			retVal = new Short(((Number)value).shortValue());
		}
		else if ( value instanceof Boolean )
		{
			//there are cases where a int field is used for a boolean, so we might
			//need to convert a boolean to an int.
			retVal = new Short( (short)( ((Boolean)value).booleanValue() ? 1 : 0 ));
		}
		else
		{
			//remove commas and dollar signs from the string value
			String strVal = MathUtility.cleanUpNumberString(value.toString());
			Double d = Double.valueOf(strVal);
			retVal = Short.valueOf(d.shortValue());
		}//if

		return retVal;
	}
	//////////////////////////////////////////////////////////
	/**
	 *	attempts to convert the object passed in to a primitive int.
	 *	if there is an error in the conversion, defVal will be returned
	 */
	public static short toShort(Object value, short defVal)
	{
		short retVal = defVal;
		try
		{
			Short ret = convertToShort(value, defVal);
			if (ret != null)
				retVal = ret.shortValue();
		}
		catch(java.lang.NumberFormatException nfe)
		{
			//Log.logException(nfe);
		}
		return retVal;
	}
	
	//////////////////////////////////////////////////////////
	/** 
	*	attempts to convert the object passed in to a Integer.
	*	The conversion may throw a java.lang.NumberFormatException
	*	if the type cannot be converted.
	*	It is left uncaught so the user can decicde how to deal with it.
	*/
	public static Integer convertToInteger(Object value)
	{
		Integer retVal = null;
		if( value == null )
			return retVal;
		
		if ( value instanceof Integer )
		{
			retVal = (Integer) value;
		}
		else if ( value instanceof Number )	
		{
			retVal = new Integer(((Number)value).intValue());
		}
		else if ( value instanceof Boolean )
		{
			//there are cases where a int field is used for a boolean, so we might
			//need to convert a boolean to an int.
			retVal = new Integer( ((Boolean)value).booleanValue() ? 1 : 0 );
		}
		else 
		{
			//remove commas and dollar signs from the string value
			String strVal = MathUtility.cleanUpNumberString(value.toString());
			Double d = Double.valueOf(strVal); //convert to double first so values like 1.0 will convert
			retVal = Integer.valueOf(d.intValue());
		}//if
		
		return retVal;
	}
	
	//////////////////////////////////////////////////////////
	/** 
	*	attempts to convert the object passed in to a primitive int.
	*	if there is an error in the conversion, defVal will be returned
	*/
	public static Integer convertToInteger(Object value, int defVal)
	{
		Integer retVal = new Integer(defVal);
		try
		{
			Integer ret = convertToInteger(value);
			if( ret != null )
				retVal = ret;
		}
		catch(java.lang.NumberFormatException nfe)
		{
			//Log.logException(nfe);
		}
		return retVal;
	}	

	//////////////////////////////////////////////////////////
	/** 
	 *	attempts to convert the object passed in to a primitive int.
	 *	if there is an error in the conversion, defVal will be returned
	 */
	public static int convertToInt(Object value, int defVal)
	{ 
		int retVal = defVal;
		try
		{
			Integer ret = convertToInteger(value);
			if (ret != null)
				retVal = ret.intValue();
		}
		catch(java.lang.NumberFormatException nfe)
		{
			//Log.logException(nfe);
		}
		return retVal;
	}	
	
	//////////////////////////////////////////////////////////
	/** 
	 *	a speedier version for just converting Integer to int
	 *	if there is an error in the conversion, defVal will be returned
	 */
	public static int convertToInt(Integer i, int defVal)
	{
		if( i == null )
			return defVal;
		return i.intValue();
	}
	
	//////////////////////////////////////////////////////////
	/** 
	 *	attempts to convert the object passed in to a primitive double.
	 *	if there is an error in the conversion, defVal will be returned
	 */
	public static double convertToDoublePrim(Object value, double defVal)
	{
		double retVal = defVal;
		try
		{
			Double ret = convertToDouble(value);
			if (ret != null)
				retVal = ret.doubleValue();
		}
		catch(java.lang.NumberFormatException nfe)
		{
			//Log.logException(nfe);
		}
		return retVal;
	}

	//////////////////////////////////////////////////////////
	/**
	 *	attempts to convert the object passed in to a primitive double.
	 *	if there is an error in the conversion, defVal will be returned
	 */
	public static long convertToLongPrim(Object value, long defVal)
	{
		long retVal = defVal;
		try
		{
			Long ret = convertToLong(value);
			if (ret != null)
				retVal = ret.longValue();
		}
		catch(java.lang.NumberFormatException nfe)
		{
			//Log.logException(nfe);
		}
		return retVal;
	}
	
	//////////////////////////////////////////////////////////
	/** 
	 *	attempts to convert the object passed in to primitive float,
	 *	if there is an error in the conversion, defVal will be returned
	 */
	public static float convertToFloatPrim(Object value, float defVal)
	{
		float retVal = defVal;
		try
		{
			Float ret = convertToFloat(value);
			if (ret != null)
				retVal = ret.floatValue();
		}
		catch(java.lang.NumberFormatException nfe)
		{
			//Log.logException(nfe);
		}
		return retVal;
	}

	//////////////////////////////////////////////////////////
	/**
	 *	returns true iff the string passed in is not null and is an integer
	 */
	public static boolean isInteger(String str)
	{
		if( str == null )
			return false;

		try
		{
			Integer.parseInt(str);
		}
		catch(java.lang.NumberFormatException nfe)
		{
			return false;
		}
		return true;
	}
	
	//////////////////////////////////////////////////////////
	/**
	 * returns true iff the string passed in is not null and is a decimal values
	 * this differs from Double.parseDouble in that it wont accept exponents, subscrips like 'd' and 'f', and NaN and infinity designations.
	 * essentially it will take an optional +- sign plus option digits plus optional decimal point plus optional digits
	 */
	public static boolean isBasicDecimal(String str)
	{
		if( str == null )
			return false;

		return str.matches(RegExUtil.BASIC_DECIMAL_NUMBER);
	}

	////////////////////////////////////////////////////////////////////
	/**
	 * rounds the number passed in to the nearest quarter value.
	 * ie, a number with a decimal value of either .0, .25, .5, .75, 1.0
	 */
	public static double roundToQuarter(double num)
	{
		double wholeNumberPart = Math.floor(num);

		//pull off the data to the left of the decimal
		double decimalPart = num - wholeNumberPart;

		if( decimalPart < .125 )
			decimalPart = 0;
		else if( decimalPart < .375 )
			decimalPart = .25;
		else if( decimalPart < .625 )
			decimalPart = .5;
		else if( decimalPart < .875 )
			decimalPart = .75;
		else
			decimalPart = 1;

		return decimalPart + wholeNumberPart;

	}//method

	////////////////////////////////////////////////////////////////////
	/**
	 * returns true iff the double passed in is a whole number
	 */
	public static boolean isWholeNumber(double d)
	{
		return Math.rint(d) == d;
	}//method

	////////////////////////////////////////////////////////////////////
	/**
	 * returns true iff the float passed in is a whole number
	 */
	public static boolean isWholeNumber(float f)
	{

		return Math.rint(f) == f;
	}//method
	//////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////
	
	
	public static void main(String[] args)
	{
		//float num = (float) -97.75389;
		/*double num = (double) -97.75389;
		System.err.println("num->" + num);
		BigDecimal bd = convertToBigDecimal(num);
		System.err.println("bd->" + bd);*/

		/*for( float i=0; i < 10000000; i++ )
		{
			if( !isWholeNumber(i) )
				System.err.println("raaaaaa->" + i);
			//if( i > 100000 && i % 100000 == 0 )
			//	System.err.println("i->" + i);
		}*/
		


	}
	
} // class

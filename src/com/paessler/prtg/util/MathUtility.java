package com.paessler.prtg.util;

import java.math.RoundingMode;
import java.text.DecimalFormat;

/******************************************************************************
 *  A library of static math utility functions
 * @author  JR Andreassen
 *  @version 0.1
 *****************************************************************************/
public abstract class MathUtility
{




	// ---------------------------------------------------------
	/**
	 * A convienience method that takes a String rep of a number and
	 * returns it with commas inserted to make it easier to read
	 */	
	public static String makeReadableNumber(String str)
	{	
		if( str == null )
			return null;
		
		StringBuffer sb = new StringBuffer(str.length() + 10);
		
		//we want to start at the the decimal point, or the end
		int decIndex = str.lastIndexOf('.');
		int ndx = decIndex - 1;
		
		if( ndx < 0 )
			ndx = str.length() - 1;
		
		//append everything from the decimal point on over
		if( decIndex > 0 && decIndex < str.length()-1 )
			sb.append( str.substring(decIndex, str.length()) );
		
		int lastIndex = str.length() - 1;
		int counter = 0;
		
		for( int i=ndx; i>=0; i-- )
		{
			counter++;
			
			sb.insert(0, str.charAt(i));
			
			//
			if( i == 0 && str.charAt(i) == '-' )
				break;
				
			if( counter == 3 )
			{
				counter = 0;
				if ( i > 0 )
				{
					//insert the comma if its not in front of a negative sign
					if( !(i == 1 && str.charAt(0) == '-') )
						sb.insert(0, ',');
				}//if	
			}//if
		}//for
		
		return sb.toString();
	}//method
	
	//////////////////////////////////////////////////////////
	
	/**
	 * A convienience method that takes a string and 
	 * returns a new string with all commas and dollar signs removed, 
	 * as well as trimming it. 
	 */	
	public static String cleanUpNumberString(String numberString)
	{
		if( numberString.indexOf(',') != -1 )
			numberString = StringUtility.removeAllInstances(numberString, ",");
		if( numberString.indexOf('$') != -1 )
			numberString = StringUtility.removeAllInstances(numberString, "$");
		
		return numberString.trim();
	}

	////////////////////////////////////////////////////////////////////
	/**
	 * rounds the number give to the specified decimal places
	 * NOTE: this method is probably not very efficient for a heavy load
	 */
	public static double round(double number, int decimalPlaces)
	{
		return Double.valueOf(roundToString(number, decimalPlaces));
	}

	////////////////////////////////////////////////////////////////////
	/**
	 * rounds the number give to the specified decimal places
	 * NOTE: this method is probably not very efficient for a heavy load
	 */
	public static String roundToString(double number, int decimalPlaces)
	{
		//create a format the has one extra place on the whole number side in case we get rounded up
		double floorValue = Math.floor(number);
		int floorLength = Double.toString(floorValue).length() + 1;
		String format = StringUtility.replicate('#', floorLength);

		//now tack on the decimal places to the format (use a 0 so it will print 0s ie, print 12.50 instead of 12.5) 
		if( decimalPlaces > 0 )
			format += '.' + StringUtility.replicate('0', decimalPlaces);
		//System.err.println("format->" + format);

		//decimal format will round using RoundingMode.HALF_EVEN by default
		DecimalFormat dFormat = new DecimalFormat(format);
		dFormat.setRoundingMode(RoundingMode.HALF_UP); //round 5+ up
		String s = dFormat.format(number);
		return s;
	}
	
	//////////////////////////////////////////////////////////

	public static void main (String[] args)
	{
		System.err.println("ooo->" + roundToString(10.2344, 2));
		System.err.println("ooo->" + roundToString(10.25, 2));
		System.err.println("ooo->" + roundToString(11, 2));
		System.err.println("ooo->" + roundToString(9.6, 0));
		System.err.println("ooo->" + roundToString(12.5, 2));
		System.err.println("ooo->" + roundToString(99.9, 0));
		System.err.println("ooo->" + roundToString(99.9, 1));


	}
	
} // class

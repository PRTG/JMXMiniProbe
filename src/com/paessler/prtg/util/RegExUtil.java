package com.paessler.prtg.util;

/**
 * CLASS: RegExUtil
 *
 * @author JR Andreassen
 */
public abstract class RegExUtil
{

	/**
	 * email checker regular expression
	 * any set if characters followed by @ folled by anything followd by a dot and at least two alpha chars
	 * this version is case-insensitive
	 */
	public static final String EMAIL_ADDRESS_REGEX = ".+@.+\\.[a-zA-Z][a-zA-Z]+";

	/** us postal codes, accepts 5 digits, and optionally a dash and 4 more digits 
	 * NOTE: this one will take it with or witout the dash, since some states (OK,AL) return the zip codes on NLETS checks as one 9 digit string
	 */
	public static final String US_ZIPCODE_REGEX = "^\\d{5}(-*\\d{4})?$";

	//public static final String CANADA_ZIPCODE_REGEX = "[a-zA-Z][0-9][a-zA-Z][ ]?[0-9][a-zA-Z][0-9]";
	/**
	 * canadian postal codes with or without a space separating the two parts. Basically letter digit letter {space} digit letter digit.
	 * however some letters are not allowed to avoid confusing OCR equipment
	 */
	public static final String CANADA_ZIPCODE_REGEX = "[ABCEGHJ-NPRSTVXY]{1}[0-9]{1}[ABCEGHJ-NPRSTV-Z]{1}[ ]?[0-9]{1}[ABCEGHJ-NPRSTV-Z]{1}[0-9]{1}";

	/** mexico simply has a 5 digit zip */
	public static final String MEXICO_ZIPCODE_REGEX = "\\d{5}";
	
	public static final String OPTIONAL_DIGITS = "(\\p{Digit}+)";
	public static final String BASIC_DECIMAL_NUMBER = "[+-]?" + OPTIONAL_DIGITS +"?(\\.)?(" + OPTIONAL_DIGITS + "?)";
	
	/** the inverse of the set of legal XML chars */
	public static final String ILLEGAL_XML_CHAR_REGEX = "[^\\u0009\\u000A\\u000D\u0020-\\uD7FF\\uE000-\\uFFFD\\u10000-\\u10FFF]";
	
	/** matches UN or NA plus 4 digits */
	public static final String HAZMAT_UN_NUMBER_REGEX = "(UN|NA)\\d{4}";
	
	public static void testBasicDecNumber()
	{
		//String digits     = "(\\p{Digit}+)";
		//String regEx = "[+-]?" + digits+"?(\\.)?("+digits+"?)";
		String regEx = BASIC_DECIMAL_NUMBER;

		System.err.println("regEx-> " + regEx);
		String newValue = "82f"; //note: 82f will pass a double.valueof check
		System.err.println(newValue + " matches(regex)->" + newValue.matches(regEx));

		newValue = "82";
		System.err.println(newValue + " matches(regex)->" + newValue.matches(regEx));

		newValue = "82.0";
		System.err.println(newValue + " matches(regex)->" + newValue.matches(regEx));

		newValue = "82.";
		System.err.println(newValue + " matches(regex)->" + newValue.matches(regEx));

		newValue = "-4.22";
		System.err.println(newValue + " matches(regex)->" + newValue.matches(regEx));

		newValue = ".2222";
		System.err.println(newValue + " matches(regex)->" + newValue.matches(regEx));
		
		newValue = "";
		System.err.println(newValue + " matches(regex)->" + newValue.matches(regEx));
	}
	
	public static void testZipCode()
	{
		System.err.println("78759->" + "78759".matches(US_ZIPCODE_REGEX) );
		System.err.println("78759-1234->" + "78759-1234".matches(US_ZIPCODE_REGEX) );

		System.err.println("78759->" + "78759".matches(MEXICO_ZIPCODE_REGEX) );
		
		System.err.println("K1A OB1->" + "K1A 0B1".matches(CANADA_ZIPCODE_REGEX) );
		System.err.println("K1A0B1->" + "K1A0B1".matches(CANADA_ZIPCODE_REGEX) );
		System.err.println("K1A0B1->" + "K1A0B1".matches(CANADA_ZIPCODE_REGEX) );
	}
	//public static final String NUMERIC_MONTH_ALLOWS_PRECEEDING_ZERO = "^(0?[1-9]|1[012])$";
	//public static final String NUMERIC_MONTH_ALLOWS_NO_PRECEEDING_ZERO = "^([1-9]|1[012])$";
	//
	
	

	////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////
	public static void main(String[] arg)
	{
		/*System.err.println("t->" + "UN1234".matches(HAZMAT_UN_NUMBER_REGEX) );
		System.err.println("t->" + "NA1234".matches(HAZMAT_UN_NUMBER_REGEX) );
		System.err.println("f->" + "NU1234".matches(HAZMAT_UN_NUMBER_REGEX) );
		System.err.println("f->" + "UN123".matches(HAZMAT_UN_NUMBER_REGEX) );
		System.err.println("f->" + "UN12345".matches(HAZMAT_UN_NUMBER_REGEX) );
		System.err.println("f->" + "XX1234".matches(HAZMAT_UN_NUMBER_REGEX) );
		System.err.println("f->" + "1234".matches(HAZMAT_UN_NUMBER_REGEX) );
		System.err.println("f->" + "UNNA1234".matches(HAZMAT_UN_NUMBER_REGEX) );
		System.err.println("f->" + "NA123".matches(HAZMAT_UN_NUMBER_REGEX) );
		System.err.println("f->" + "NA12345".matches(HAZMAT_UN_NUMBER_REGEX) );*/
		
		//System.err.println("X->" + "this is a cat dog".matches(".*(cat|dog).*") );
		
		/*System.err.println("0->" + "0".matches(NUMBER_NO_PRECEEDING_ZERO) );
		System.err.println("1->" + "1".matches(NUMBER_NO_PRECEEDING_ZERO) );
		System.err.println("2->" + "3".matches(NUMBER_NO_PRECEEDING_ZERO) );
		System.err.println("3->" + "3".matches(NUMBER_NO_PRECEEDING_ZERO) );
		System.err.println("4->" + "4".matches(NUMBER_NO_PRECEEDING_ZERO) );
		System.err.println("5->" + "5".matches(NUMBER_NO_PRECEEDING_ZERO) );
		System.err.println("6->" + "6".matches(NUMBER_NO_PRECEEDING_ZERO) );
		System.err.println("7->" + "7".matches(NUMBER_NO_PRECEEDING_ZERO) );
		System.err.println("8->" + "8".matches(NUMBER_NO_PRECEEDING_ZERO) );
		System.err.println("9->" + "9".matches(NUMBER_NO_PRECEEDING_ZERO) );
		System.err.println("10->" + "10".matches(NUMBER_NO_PRECEEDING_ZERO) );
		System.err.println("11->" + "11".matches(NUMBER_NO_PRECEEDING_ZERO) );
		System.err.println("12->" + "12".matches(NUMBER_NO_PRECEEDING_ZERO) );
		
		System.err.println("13->" + "13".matches(NUMBER_NO_PRECEEDING_ZERO) );
		System.err.println("02->" + "02".matches(NUMBER_NO_PRECEEDING_ZERO) );*/
	}
}//class

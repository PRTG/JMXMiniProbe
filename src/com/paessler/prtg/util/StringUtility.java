package com.paessler.prtg.util;

import java.nio.charset.Charset;
import java.text.CharacterIterator;
import java.text.StringCharacterIterator;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.StringTokenizer;
import java.util.Vector;

/******************************************************************************
 *  A library of static String utility functions
 *  @author  JR Andreassen
 *  @version 1.0
 *****************************************************************************/
public abstract class StringUtility
{
	/** Constant for use as prefix for String encoded HEX */
	public static final String HEX_STRING_PREFIX = "0x";
	
	/** Constant for use as Blank String */
	public static final String BlankString256 = "                                                                                                                                                                                                                                                               ";
	public static final String Hyphen256			= "---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------";
	public static final String Underscore256	= "_______________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________";
	public static final String STRING_ALL_TABS = "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t";
	/** Constant for use as Blank String */
	public static final String BlankString = " ";
	/** Constant for use as Empty String */
	public static final String EmptyString = "";


	/** Constant for use as Line Separator */
	public static final String LINE_SEPARATOR 	= System.getProperty(SystemUtility.SYS_PROPERTY_LINE_SEPARATOR);


	//-----------------------------------------------------------
	/** Constants for String elements/characters */
	public static final String Period				= ".";
	public static final String Comma				= ",";
	public static final String Colon				= ":";
	public static final String SemiColon		= ";";
	public static final String CaretSign		= "^";
	public static final String Quote				= "'";
	public static final String DoubleQuote	= "\"";


	public static final String QuestionMark	= "?".intern();
	public static final String Bang					= "!".intern();
	public static final String AtSign				= "@".intern();
	public static final String Asterisk			= "*".intern();
	public static final String HashMark			= "#".intern();
	public static final String PercentSign	= "%".intern();
	public static final String DollarSign		= "$".intern();
	public static final String Tilde				= "~".intern();
	public static final String Bar					= "|".intern();
	public static final String Plus					= "+".intern();
	public static final String Equal				= "=".intern();
	public static final String Hyphen				= "-".intern();
	public static final String Underscore		= "_".intern();
	public static final String ForwardSlash	= "/".intern();
	public static final String BackSlash		= "\\".intern();
	// 
	public static final String NewLine			= "\n".intern();
	public static final String CarriageRet	= "\r".intern();
	public static final String HorizTab			= "\t".intern();
//	public static final String VertTab			= "\v".intern();

	public static final String LeftParen		= "(".intern();
	public static final String RightParen		= ")".intern();

	public static final String LeftCurlyBrace	= "{".intern();
	public static final String RightCurlyBrace= "}".intern();

	public static final String LeftSquareBrace= "[".intern();
	public static final String RightSquareBrace= "]".intern();

	/** String Constant for logical operation : A > B*/
	public static final String GreaterThan		= ">".intern();
	/** String Constant for logical operation : A < B*/
	public static final String LessThan				= "<".intern();


	/** set of characters that are safe to break on when word wrapping */
	//public static final String BREAKEABLE_WORDWRAP_CHARS = " \t\n\f\r*-";
	
	/** a string that is unlikely to occur naturally, useful for temporary 
	 * replacements in mutilstep operations like variable substitution and optional
	 * removal
	 */
	public static final String UNLIKELY_TO_OCCUR_STR = "Q#TGEF@#$!AC!@sdg34-!@#~~24";
	
	/** Constant for use as Invalid poss or not found with indexOf() etc */
	public static final int	STRING_INVALID_POSSITION	= -1;
	/** Constant for use as Invalid poss or not found with indexOf() etc */
	public static final int	STRING_COMPARE_MATCH	= 0;

	private static Charset utf8Charset = Charset.forName("UTF-8");

	//////////////////////////////////////////////////////////
	/**
	 * Determine TermiatorType
	 */
	public static boolean isLineTerminatorCRLF()
	{	return (StringUtility.LINE_SEPARATOR.length() == 2);
	}	
	

	//---------------------------------------------------------------------------
	/**
	 * This method returns a HashSet of Characters with each 	 		
	 * unique character from the string represented. 
	 */
	public static HashSet<?> getUniqueChars(String str)
	{
		HashSet<Character> set = new HashSet<Character>();

		for( int i=0; i < str.length(); i++ )
		{	set.add( new Character(str.charAt(i)) );	}
		return set;

	}//method

	// ---------------------------------------------------
	/*
	 * Strips all the characters in the list from the string.
	 * @param tostrip		The string to strip
	 * @param	char_set	Set of characters to strip.
	 * @return String Stripped string or parameter 'tostrip'
	**/
	public static String stripchars(String tostrip, String char_set)
		throws Exception
	{
		if(tostrip == null || char_set == null)
		{	return tostrip;	}
		
		StringBuilder retVal = new StringBuilder();
		int i = 0;
		byte[]	chars = char_set.getBytes();
		CharacterIterator citer = new StringCharacterIterator(tostrip);
		char curr = CharacterIterator.DONE;
		if(citer != null)
		{
			for(;(curr = citer.current()) != CharacterIterator.DONE;citer.next() )
			{
				for (i = 0; i < chars.length; i++)
				{
					if(chars[i] == curr)
					{	
						curr = CharacterIterator.DONE; 
						break;
					}
				}
				if(curr != CharacterIterator.DONE)
				{	retVal.append(curr);	}
			}
		}
		return retVal.toString();
	}
	//---------------------------------------------------------------------------
	public static final String stripcharsNoThrow(String tostrip, String char_set)
	{
		String retVal = tostrip;
		try
		{	retVal = stripchars(tostrip, char_set);	}
		catch (Exception ex)
		{	}
		return retVal;
	}
	//---------------------------------------------------------------------------
	/**
	 * Convinience method to do replacements ans checks
	 * instances of searchStr with replaceStr in the origStr. 
	 */
	public static String replace(String src, char oldchar, char newchar)
	{	String retVal = src;
		if (src != null && src.indexOf(oldchar) != STRING_INVALID_POSSITION)
		{	retVal = src.replace(oldchar, newchar);
		}
		return retVal;
	}
	
	/**
	 * same as String.indexOf, but this version is case-insensitive
	 * @param stringToCheck string you want to seach through
	 * @param stringToLookFor the substring you are looking for in the other string
	 */
	public static int indexOfCaseInsensitive(String stringToCheck, String stringToLookFor, int fromIndex)
	{
		stringToCheck = stringToCheck.toLowerCase();
		stringToLookFor = stringToLookFor.toLowerCase();
		return stringToCheck.indexOf(stringToLookFor, fromIndex);
	}

	/**
	 * Returns a new string that is the result of replacing all
	 * instances of searchStr with replaceStr in the origStr.
	 * @param origStr			String to be updated
	 * @param searchStr		String to substitute
	 * @param replaceStr	String to substitute with.
	 * @return String Substituted string.
	 */
	public static String searchReplace(String origStr, String searchStr, String replaceStr)
	{
		return searchReplace(origStr, searchStr, replaceStr, true);
	}

	//---------------------------------------------------------------------------
	/**
	 * Returns a new string that is the result of replacing all 
	 * instances of searchStr with replaceStr in the origStr. 
	 * @param origStr			String to be updated
	 * @param searchStr		String to substitute
	 * @param replaceStr	String to substitute with.
	 * @param caseSensitive
	 * @return String Substituted string.
	 */
	public static String searchReplace(String origStr, String searchStr, String replaceStr, boolean caseSensitive)
	{
		if( origStr == null || searchStr == null )
			return origStr;
		
		if( replaceStr == null )
			replaceStr = "";

		int begIndex = 0;
		int endIndex = 0;
		int searchLength = searchStr.length();

		StringBuilder returnString = new StringBuilder(origStr.length() * 2);

		while( true )
		{
			//find the index of the substring occurence
			if( caseSensitive )
				endIndex = origStr.indexOf(searchStr, begIndex);
			else
				endIndex = indexOfCaseInsensitive(origStr, searchStr, begIndex);
			
			if( endIndex == STRING_INVALID_POSSITION )
			{	break;	}

			//append this part to the returnString
			returnString.append( origStr.substring(begIndex, endIndex) );

			//append the replacement string
			returnString.append(replaceStr);

			//fix the indeces for the next go
			begIndex = searchLength + endIndex;

		}//while

		//now add the ending on
		endIndex = origStr.length();		
		returnString.append( origStr.substring(begIndex, endIndex) );

		return returnString.toString();
	}//method

	////////////////////////////////////////////////////////////////////
	/**
	 * util method to extract var names from strings
	 * finds the next sequence enclosed by startPatternChar and endPatterChar, and returns the text between them
	 * ex) findVariablePattern("xxx%COMPUTERNAME%xxx", '%', '%', 0) -> "COMPUTERNAME"
	 * returns null when there are no matching patterns, or of the srcString is null
	 */
	public static String findVariablePattern(String srcString, char startPatternChar, char endPatterChar, int startIdx)
	{
		if( srcString == null )
			return null;

		startIdx = srcString.indexOf(startPatternChar, startIdx);
		if( startIdx == -1 )
			return null;

		int endIdx = srcString.indexOf(endPatterChar, startIdx+1);
		if( endIdx == -1 )
			return null;

		String varString = srcString.substring(startIdx+1, endIdx);

		return varString;
	}//method


	/**
	 * finds all instances of the stringToFind and replaces them with an upper case version
	 * the find operation is case-insensitive
	 */
	public static String upperCaseAllInstances(String origStr, String stringToFind)
	{
		String upperedString = stringToFind.toUpperCase();
		String newString = searchReplace(origStr, stringToFind, upperedString, false);
		return newString;
	}

	/**
	 * same as upperCaseAllInstances, but takes a list of strings that you want to be uppercased
	 */
	public static String upperCaseAllInstances(String origStr, String listOfStringsToFind, String separatorChar)
	{
		String newString = origStr;
		StringTokenizer st = new StringTokenizer(listOfStringsToFind, separatorChar);
		while( st.hasMoreTokens() )
		{
			String stringToFind = st.nextToken();
			newString = upperCaseAllInstances(newString, stringToFind);
		}
		return newString;
	}
	
	//---------------------------------------------------------------------------
	/**
	 * This method removes all instances of the strToRemove given from the origStr given
	 * @param origStr			String to be updated
	 * @param strToRemove	String to remove
	 * @return String Substituted string.
	 */
	public static String removeAllInstances(String origStr, String strToRemove)
	{
		if( origStr == null )
		{	return null;		}

		int begIndex = 0;
		int endIndex = 0;
		int removeLength = strToRemove.length();

		StringBuilder returnString = new StringBuilder(origStr.length() * 2);

		while( true )
		{
			//find the index of the substring occurence
			endIndex = origStr.indexOf(strToRemove, begIndex);
			if( endIndex == STRING_INVALID_POSSITION )
			{	break;	}

			//append this part to the returnString
			returnString.append( origStr.substring(begIndex, endIndex) );

			//fix the indeces for the next go
			begIndex = removeLength + endIndex;

		}//while

		//now add the ending on
		endIndex = origStr.length();
		returnString.append( origStr.substring(begIndex, endIndex) );

		return returnString.toString();

	}//method

	//---------------------------------------------------------------------------
	/**
	 * This method returns the number of times the character ch occurs in the src String
	 * @param src	String to search
	 * @param ch	Character to look for
	 * @return int Number of times the character occurs in the string.
	 */
	public static int occurs(String src, char ch)
	{
		int retval = 0;
		if(src != null)
		{	
			byte theBytes[] = src.getBytes();

			for(int i=0; i < theBytes.length; i++)
			{	if(theBytes[i] == ch)
				{	retval++;	}
			}
		}
		return retval;
	}
	//---------------------------------------------------------------------------
	/**
	 * This method returns the number of times the string str occurs in the src String
	 * @param src	String to search
	 * @param str	String to look for
	 * @return int Number of times the String occurs in the string.
	 */
	public static int occurs(String src, String str)
	{
		int retVal = 0;
		if(str.length() < 1)
		{	retVal = 1;}
		else if(src != null && str != null)
		{	int tofindStrLen = str.length();
			int fndIdx = src.indexOf(str);	
			while(fndIdx != STRING_INVALID_POSSITION && src.length() > 0)
			{	src = src.substring(fndIdx+tofindStrLen);
			retVal++;
				fndIdx = src.indexOf(str);
			}
		}
		return retVal;
	}

	//---------------------------------------------------------------------------
	/**
	 * This method returns a String that is the result of replicating the
	 * char ch count times. 
	 * @param ch		Character to replicate
	 * @param count	Number of times to replicate
	 * @return String resulting string
	 */
	public static String replicate(char ch, int count)
	{
		StringBuilder retval = new StringBuilder(count+1);

//		for(int i=0; i < count; i++)
//		{	retval.append(ch);}
		appendChars(retval, ch, count);
		return retval.toString();
	}

	//---------------------------------------------------------------------------
	/**
	 * This method returns a String that is the result of replicating the
	 * char ch count times. 
	 * @param buff	StringBuilder to use to produce the String
	 * @param ch		Character to replicate
	 * @param count	Number of times to replicate
	 */
	public static void appendChars(StringBuilder buff, char ch, int count)
	{
		for(int i=0; i < count; i++)
		{	buff.append(ch);	}
	}

	//---------------------------------------------------------------------------
	/**
	 * This method returns a String of blanks count long
	 * char ch count times.
	 * @param count	Number of times to replicate
	 * @return String String of count blanks
	 */
	public static String getRepeatedStr(String superstr, char torepeat, int count)
	{
		String retVal = null;
		if( superstr != null && count <= superstr.length() )
		{	retVal =  superstr.substring(0, count);}
		else
		{	retVal =  replicate(torepeat, count);}
		return retVal;
	}
	//---------------------------------------------------------------------------
	/**
	 * This method returns a String of blanks count long
	 * char ch count times. 
	 * @param count	Number of times to replicate
	 * @return String String of count blanks
	 */
	public static String getBlanks(int count)
	{	return getRepeatedStr(BlankString256, CharacterUtility.Blank, count);
/*		if( count > BlankString256.length() )
			return replicate(' ', count);
		return BlankString256.substring(0, count);
*/	}
	//---------------------------------------------------------------------------
	/**
	 * This method returns a String of blanks count long
	 * char ch count times.
	 * @param count	Number of times to replicate
	 * @return String String of count blanks
	 */
	public static String getHyphens(int count)
	{	return getRepeatedStr(Hyphen256, CharacterUtility.Hyphen, count);	}
	//---------------------------------------------------------------------------
	/**
	 * This method returns a String of blanks count long
	 * char ch count times.
	 * @param count	Number of times to replicate
	 * @return String String of count blanks
	 */
	public static String getUnderscore(int count)
	{	return getRepeatedStr(Underscore256, CharacterUtility.Underscore, count);	}

	//---------------------------------------------------------------------------
	/**
	 * This method returns a String of Tabs count long
	 * char ch count times.
	 * @param count	Number of times to replicate
	 * @return String String of count blanks
	 */
	public static String getTabs(int count)
	{	return getRepeatedStr(STRING_ALL_TABS, CharacterUtility.HorizTab, count);
	}

	
	//---------------------------------------------------------------------------
	/**
	 * Pad a string(at end) to given length.
	 * @param str		String to pad.
	 * @param pad		Character to pad with
	 * @param count	Final legnt after padding
	 * @return String String of count blanks
	 */
	public static String padString(String str, char pad, int count)
	{
		return padString(str, pad, count, false);
	}
	
	//---------------------------------------------------------------------------
	/**
	 * Pad a string to given length.
	 * @param str		String to pad.
	 * @param pad		Character to pad with
	 * @param count	Final legnt after padding
	 * @param prePad if true, pads the beginning of the string instead of the end
	 * @return String String of count blanks
	 */
	public static String padString(String str, char pad, int count, boolean prePad)
	{
		String retVal = str;
		if(str == null)
		{
			retVal = StringUtility.EmptyString;
		}
		int len = count - retVal.length();
		if (len > 0)
		{	
			String padStr = null;
			if (pad == CharacterUtility.Blank)
			{
				padStr =  StringUtility.getBlanks(len);
			}
			else
			{
				padStr = StringUtility.replicate(pad, len);
			}
		
			if( prePad == true )
				retVal = padStr + retVal;
			else	
				retVal = retVal + padStr;
		}//if
	
		return retVal;
	}


	//---------------------------------------------------------------------------
	/**
	 * This method returns a new String that contains the rightmost
	 * length characters if the origStr String. If the length given is
	 * greater than the length of the origStr, then a copy of the origStr
	 * is returned
	 * @param origStr	String to extract from
	 * @param length	Number of character from the end.
	 * @return String Extracted string
	 */
	public static String right(String origStr, int length)
	{
		String retVal = origStr;
		
		if( origStr != null )
		{
			if ( origStr.length() > length )
				retVal = origStr.substring(origStr.length() - length);
		}
		return retVal;
	}
	
	//---------------------------------------------------------------------------
	/**
	 * This method returns a new String that contains the leftmostmost
	 * length characters if the origStr String. If the length given is
	 * greater than the length of the origStr, then a copy of the origStr
	 * is returned
	 * @param origStr	String to extract from
	 * @param length	Number of character from the beginning.
	 * @return String Extracted string
	 */
	public static String left(String origStr, int length)
	{
		String retVal = origStr;

		if( origStr != null )
		{	
			if( origStr.length() > length )
			{	retVal = origStr.substring(0, length);	}
		}
		return retVal;
	}
	
	//---------------------------------------------------------------------------
	/**
	 * This method returns a new String that is a substring of the origStr
	 * starting at the offset given and continuing length characters or until 
	 * the end of the string is reached. 
	 *
	 * @param origStr	String to extract from
	 * @param offset	Starting point.
	 * @param length	Number of character to extract.
	 * @return String Extracted string
	 * @throws IndexOutOfBoundsException if offset DNE in origStr
	 */
	public static String substringOffsetLength(String origStr, int offset, int length)
	{
		if ( origStr == null )
		{	throw new IndexOutOfBoundsException("StringUtility.substringOffsetLength - origStr is null");		}
			
		
		int origLength = origStr.length();
		int endIndex = offset + length;
		
		if ( offset >= origLength )
		{	throw new IndexOutOfBoundsException("StringUtility.substringOffsetLength - offset >= origLength:" + offset + ">=" +origLength);}
		
		
		if( endIndex > origLength-1 )
		{	endIndex = origLength-1;}
		
		return origStr.substring(offset, endIndex);
	}

	//---------------------------------------------------------------------------
	/**
	 * This method replaces variables in a string
	 * <PRE>
	 * "abcdefg... <variableName>"
	 * Where variableName is a valid key in a hash table/resource bundle.
	 * </PRE>
	 * @param srcStr				Source String
	 * @param prop					Property Source
	 * @param startVarToken	Start Variable name token
	 * @param endVarToken		End Variable name token
	 * @return String String with all substitutions performed. 
	 * Note: Variables not found in the property will be eliminated.
	 */
	/*
	public static String performVarSubst(String srcStr, Hashtable props)
	{	return performVarSubst(srcStr, props, CharacterUtility.GreaterThan, CharacterUtility.LessThan);
	}
	*/
/*
	public static String performVarSubst(String srcStr, Hashtable props, char startVarToken, char endVarToken)
	{
		if (srcStr == null || props == null)
		{	if(Log.DEBUG )
			{	Log.printDebug("(srcStr=\'"+ srcStr +"\', props) Either srcStr or props is null");	}
			return null;	
		}
		int openpos	= StringUtility.occurs(srcStr, startVarToken);
		int closepos= StringUtility.occurs(srcStr, endVarToken);	
		if (openpos != closepos)
		{	if(Log.DEBUG )
			{	Log.printDebug("("+ srcStr +") missmatched '"+startVarToken+endVarToken+"'");	}
			return null;	
		}
		openpos	= srcStr.indexOf(startVarToken);
		if (openpos == STRING_INVALID_POSSITION)
		{	return srcStr;
		}
		closepos = STRING_INVALID_POSSITION;
		String tmpStr = null, varName = null;
		StringBuilder buf = new StringBuilder();
		while(openpos > STRING_INVALID_POSSITION)
		{
			buf.append(srcStr.substring(closepos+1, openpos));
			closepos= srcStr.indexOf(endVarToken, openpos+1);
			if(closepos == STRING_INVALID_POSSITION)
			{	break;}
			varName = srcStr.substring(openpos+1, closepos);
			if (varName != null)
			{	tmpStr = ObjectUtility.toString(props.get(varName));	}	

			if (tmpStr != null)
			{	buf.append(tmpStr);	}
			
			openpos	= srcStr.indexOf(startVarToken, closepos+1);
			tmpStr = null;
			varName = null;
		}
		if (closepos+1 < srcStr.length())
		{	buf.append(srcStr.substring(closepos+1));
		}
		return buf.toString();
	}
 */
	//---------------------------------------------------------------------------
	

	
	// ///////////////////////////////////////////////////
	// ***************************************************
	// ///////////////////////////////////////////////////
	
/*	
	public static void main(String[] args)
	{
		String testString;
//		System.err.println("testString->" + testString);
//		testString = removeOptionals(testString, "{", "}", UNLIKELY_TO_OCCUR_STR);
//		System.err.println("testString->" + testString);
		
		//testString = "{123{56}89}";
		//int i = findMatching(testString, 1,  "{", "}");
		//System.err.println("i->" + i);
		
		testString = "This is a \ttest\r";
		//System.err.println("testString->" + testString);
		//testString = testString.replace("#test", "ReplacementStr");
		//System.err.println("testString->" + testString);
//		testString = removeOptionals(testString, "#", "", UNLIKELY_TO_OCCUR_STR);
//		System.err.println("testString->" + testString);
		
		testString = replaceWhiteSpaceChars(testString, "#");
		System.err.println("testString->" + testString);

		
		System.exit(0);
		 
	}
	
*/
// ---------------------------------------------------------------------------
/**
 * Encode bytes as a hex String
 * Assumes a prefix of '0x'
 * @param valarr Byte Array to encode as a Hex String
 * @param len Length to encode
 * @return String The encoded string
 */
	public static  String toHexString(byte[] valarr)
	{	return toHexString(valarr, valarr.length);}
	
// ---------------------------------------------------------------------------
/**
 * Encode bytes as a hex String
 * Assumes a prefix of '0x'
 * @param valarr Byte Array to encode as a Hex String
 * @param len Length to encode
 * @return String The encoded string
 */
	public static  String toHexString(byte[] valarr, int len)
	{	return toHexString(valarr, 0, len);}
// ---------------------------------------------------------------------------
/**
 * Encode bytes as a hex String
 * Assumes a prefix of '0x'
 * @param valarr Byte Array to encode as a Hex String
 * @param len Length to encode
 * @return String The encoded string
 */
	public static  String toHexString(byte[] valarr, int start, int len)
	{	return toHexString(valarr, start, len, "0x");}

// ---------------------------------------------------------------------------
/**
 * Encode bytes as a hex String
 * @param valarr Byte Array to encode as a Hex String
 * @param len Length to encode
 * @param prefix Hex String prefix
 * @return String The encoded string
 */
	public static  String toHexString(byte[] valarr, int start, int len, String prefix)
	{
		return toHexString(valarr, start, len, prefix, null);
	}

// ---------------------------------------------------------------------------
/**
 * Encode bytes as a hex String
 * @param valarr Byte Array to encode as a Hex String
 * @param len Length to encode
 * @param prefix Hex String prefix
 * @return String The encoded string
 */
	public static  String toHexString(byte[] valarr, int start, int len, String prefix, String separator)
	{	String retVal = null;
			if (valarr != null && valarr.length > 0)
			{	StringBuilder strBuff = new StringBuilder(prefix);
				if (len < 0)
				{	len = valarr.length;		}
				for(int i =start ; i < len; i++)
				{	
					if (separator != null && i > 0)
					{strBuff.append(separator);	}
					strBuff.append(toHexString(valarr[i], true));	
				}
				retVal =  strBuff.toString();
			}
			return retVal;
	}


// ---------------------------------------------------------------------------
	public static  String toHexStringOld(byte[] valarr, int len, String prefix)
	{	String retVal = null;
		byte currByte = 0;	
			if (valarr != null && valarr.length > 0)
			{	StringBuilder strBuff = new StringBuilder(prefix);
				if (len 	< 0)
				{	len = valarr.length;		}
				String tmpStr= null;
				for(int i =0 ; i < len; i++)
				{	currByte = valarr[i];
					tmpStr = Integer.toHexString(currByte);
					if(tmpStr.length() > 2)
					{ tmpStr = tmpStr.substring(tmpStr.length() - 2);	}
					else if(tmpStr.length() == 1) // byte < 16
					{ strBuff.append(digits[0]);	}
					strBuff.append(tmpStr);
				}
				retVal =  strBuff.toString();
			}
			return retVal;
	}
	

// ---------------------------------------------------------------------------
    /**
     * Creates a string representation of the integer argument as an
     * unsigned integer in base&nbsp;16.
     * <p>
     * The unsigned integer value is the argument plus 2<sup>32</sup> if 
     * the argument is negative; otherwise, it is equal to the argument. 
     * This value is converted to a string of ASCII digits in hexadecimal 
     * (base&nbsp;16) with no extra leading <code>0</code>s. If the 
     * unsigned magnitude is zero, it is represented by a single zero 
     * character <tt>'0'</tt> (<tt>'&#92;u0030'</tt>); otherwise, the first 
     * character of the representation of the unsigned magnitude will 
     * not be the zero character. The following characters are used as 
     * hexadecimal digits:
     * <blockquote><pre>
     * 0123456789abcdef
     * </pre></blockquote>
     * These are the characters <tt>'&#92;u0030'</tt> through <tt>'&#92;u0039'</tt> 
     * and <tt>'u\0039'</tt> through <tt>'&#92;u0066'</tt>. If the uppercase 
     * letters are desired, the {@link java.lang.String#toUpperCase()} 
     * method may be called on the result:
     * <blockquote><pre>
     * Long.toHexString(n).toUpperCase()
     * </pre></blockquote>
     *
     * @param   i   a byte.
     * @param   len  Length of result
		 * @param		prepad Prepend with leading Zeros.
     * @return  the string representation of the unsigned integer value
     *          represented by the argument in hexadecimal (base&nbsp;16).
     */
    public static String toHexString(byte i, boolean prepad) 
    {	return toUnsignedString(i, 4, 2, prepad); }
		
// ---------------------------------------------------------------------------
    public static String toHexString(short i, boolean prepad) 
    {	return toUnsignedString(i, 4, 4, prepad); }
		
// ---------------------------------------------------------------------------
    public static String toHexString(int i, boolean prepad) 
		{	return toUnsignedString(i, 4, 8, prepad); }

// ---------------------------------------------------------------------------
	/**
	 * Convert the integer to an unsigned number.
	 */
	private static String toUnsignedString(int i, int shift, int len, boolean prepad) 
	{
		char[] buf = new char[32];
		int charPos = 32;
		int radix = 1 << shift;
		int mask = radix - 1;
		do {
			  buf[--charPos] = digits[i & mask];
			  i >>>= shift;
				len--;
		} while (i != 0 && len > 0);
		// prepad with Zero's
		while (prepad && len > 0)
		{	buf[--charPos] = digits[0];
			len--;
		}
		return new String(buf, charPos, (32 - charPos));
	}
// ---------------------------------------------------------------------------
	/**
	 * All possible chars for representing a number as a String
	 */
	final static char[] digits = {
	'0' , '1' , '2' , '3' , '4' , '5' ,
	'6' , '7' , '8' , '9' , 'A' , 'B' ,
	'C' , 'D' , 'E' , 'F' , 'G' , 'H' ,
	'I' , 'J' , 'K' , 'L' , 'M' , 'N' ,
	'O' , 'P' , 'Q' , 'R' , 'S' , 'T' ,
	'U' , 'V' , 'W' , 'X' , 'Y' , 'Z'
	};

		// ------------------------------------------------------------------------
	/**
	 * This method finds all Strings that match a pattern.
	 * It does the equivalent of key.indexOf(pattern);
	 * @param e						Enumeration of strings to search
	 * @param pattern			Pattern/substring to look for
	 * @return Vector			Vector of keys of the matching elements
	 */
	public static Vector<String> findMatching(Enumeration<?> e, String pattern)
	{ Vector<String> retVal = new Vector<String>();
		String tmpStr, tmpStrCmp;
		pattern = pattern.toUpperCase();
		while (e.hasMoreElements()) 
		{	tmpStr = (String)e.nextElement();
			if (tmpStr != null)
			{	tmpStrCmp = tmpStr.toUpperCase();
				if (tmpStrCmp.indexOf(pattern) != StringUtility.STRING_INVALID_POSSITION)
				{	retVal.add(tmpStr);	
//					if(Log.DEBUG && myLog != null && myLog.getLevel() > 4)
//					{	myLog.println("StringUtility.findMatching(" + pattern + ") Match =>" + tmpStr);			}
				}
			}
		} // while
		return retVal;
	}

	// ---------------------------------------------------------------------------
	/**
	 * This method finds a string between token's
	 * <PRE>
	 * getStringBetween("....yyyxxxzzz....", 0, "yyy", "zzz");
	 * Should then return "xxx".
	 * </PRE>
	 * @param srcStr				Source String
	 * @param startidx			Starting point within string
	 * @param startstr	Start token
	 * @param endstr		End token
	 * @return String the string between the token's or null
	 */
	public static String getStringBetween(String srcStr, int startidx,  String startstr, String endstr)
	{	String retVal = null;
		int beginidx = srcStr.indexOf(startstr, startidx);
		if(beginidx != -1)
		{
			beginidx += startstr.length();
			int endidx = srcStr.indexOf(endstr, beginidx);
			if (endidx != -1)
			{	retVal = srcStr.substring(beginidx, endidx);
			}
		}
		return retVal;
	}
// 	String tmpstr = StringUtility.getStringBetween("com.ibm.db2.jcc.am.SqlDataException: Error for batch element #677: DB2 SQL Error: SQLCODE=-413, SQLSTATE=22003, SQLERRMC=null, DRIVER=4.11.69",	0, "Error for batch element #", ":");

// ---------------------------------------------------------------------------
	/**
	 * Make String Lower Case
	 * @param	val String to convert to Lower
	 * @return	LowerCase string
	 */
	public static String toLowerCase(String val)
	{
		String retVal = val;
		if (retVal != null)
		{	retVal = retVal.toLowerCase();}
		return retVal;
	}
// ---------------------------------------------------------------------------
	/**
	 * Make String upperCase
	 * @param	val String to convert to upper
	 * @return	UpperCase string
	 */
	public static String toUpperCase(String val)
	{
		String retVal = val;
		if (retVal != null)
		{	retVal = retVal.toUpperCase();}
		return retVal;
	}
	// ---------------------------------------------------------------------------
	/**
	 * Trim string to size
	 * @param	val 	String to trim
	 * @param	maxlen	max length
	 * @return	UpperCase string
	 */
	public static String trim(String val, int maxlen)
	{
		String retVal = val;
		if (retVal != null)
		{	
			retVal = val.trim();
			if(retVal.length() > maxlen)
			{	retVal = retVal.substring(0, maxlen);}
		}
		return retVal;
	}
// ---------------------------------------------------------------------------
	/**
	 * Produce Comma separated string of the elements in the Vector.
	 * @param	vect Vector of elements
	 * @return	String representation of the list
	 */
	public static String toString(List<?> vect)
	{	return toString(vect, ", ");
	}	
// ---------------------------------------------------------------------------
	/**
	 * Produce <separator> separated string of the elements in the Vector.
	 * @param	vect Vector of elements.
	 * @param	separator Separator String.
	 * @return	String representation of the list.
	 */
	public static String toString(List<?> vect, String separator)
	{	String retVal = null;
		if (vect != null && vect.size() > 0)
		{	StringBuilder strBuff = new StringBuilder();
			for(int i = 0; i < vect.size(); i++)
			{	// Check need for Separator
				if( i != 0)
				{	strBuff.append(separator);	}
				strBuff.append(vect.get(i).toString());
			}
			retVal = strBuff.toString();
		}
		return retVal;
	}
	
	/**
	 * returns a separated list, taking care of not including empty or null values
	 * returns null if no items are empty in the list
	 */
	public static String toSeparatedList(String separator, String... items)
	{
		StringBuilder sb = new StringBuilder();
		for( String item : items )
		{
			if( item == null || item.length() == 0 )
				continue;
			if( sb.length() > 0 )
				sb.append(separator);
			sb.append(item);
		}
		if( sb.length() == 0 )
			return null;
		return sb.toString();
	}
	
	/**
	 * Convert to a readable string format by replacing special characters
	 * with constant values, for instance \n becomes [NWLN]
	 * @param str		String to convert
	 */	 
	public static String toReadableString(String str)
	{
		if( str == null )
			return null;
		return ByteUtility.toReadableString(str.getBytes());
	}
	
	//--------------------------------------------------------------------------
	/** Borrowed from Apache.Commons
	 * <p>Wraps a single line of text, identifying words by <code>' '</code>.</p>
	 * 
	 * <p>New lines will be separated by the system property line separator.
	 * Very long words, such as URLs will <i>not</i> be wrapped.</p>
	 * 
	 * <p>Leading spaces on a new line are stripped.
	 * Trailing spaces are not stripped.</p>
	 *
	 * <pre>
	 * WordUtils.wrap(null, *) = null
	 * WordUtils.wrap("", *) = ""
	 * </pre>
	 *
	 * @param str  the String to be word wrapped, may be null
	 * @param wrapLength  the column to wrap the words at, less than 1 is treated as 1
	 * @return a line with newlines inserted, <code>null</code> if null input
	 */
	public static String wrap(String str, int wrapLength)
	{
		return wrap(str, wrapLength, null, false);
	}

	/**
	 * Borrowed from Apache.Commons
	 * <p>Wraps a single line of text, identifying words by <code>' '</code>.</p>
	 * 
	 * <p>Leading spaces on a new line are stripped.
	 * Trailing spaces are not stripped.</p>
	 * 
	 * <pre>
	 * WordUtils.wrap(null, *, *, *) = null
	 * WordUtils.wrap("", *, *, *) = ""
	 * </pre>
	 *
	 * @param str  the String to be word wrapped, may be null
	 * @param wrapLength  the column to wrap the words at, less than 1 is treated as 1
	 * @param newLineStr  the string to insert for a new line, 
	 *  <code>null</code> uses the system property line separator
	 * @param wrapLongWords  true if long words (such as URLs) should be wrapped
	 * @return a line with newlines inserted, <code>null</code> if null input
	 */
	public static String wrap(String str, int wrapLength, String newLineStr, boolean wrapLongWords)
	{
		if( str == null )
		{
			return null;
		}
		if( newLineStr == null )
		{
			newLineStr = System.getProperty("line.separator");
		}
		if( wrapLength < 1 )
		{
			wrapLength = 1;
		}
		int inputLineLength = str.length();
		int offset = 0;
		StringBuilder wrappedLine = new StringBuilder(inputLineLength + 32);

		while( inputLineLength - offset > wrapLength )
		{
			if( str.charAt(offset) == ' ' )
			{
				offset++;
				continue;
			}
			int spaceToWrapAt = str.lastIndexOf(' ', wrapLength + offset);

			if( spaceToWrapAt >= offset )
			{
				// normal case
				wrappedLine.append(str.substring(offset, spaceToWrapAt));
				wrappedLine.append(newLineStr);
				offset = spaceToWrapAt + 1;

			}
			else
			{
				// really long word or URL
				if( wrapLongWords )
				{
					// wrap really long word one line at a time
					wrappedLine.append(str.substring(offset, wrapLength + offset));
					wrappedLine.append(newLineStr);
					offset += wrapLength;
				}
				else
				{
					// do not wrap really long word, just extend beyond limit
					spaceToWrapAt = str.indexOf(' ', wrapLength + offset);
					if( spaceToWrapAt >= 0 )
					{
						wrappedLine.append(str.substring(offset, spaceToWrapAt));
						wrappedLine.append(newLineStr);
						offset = spaceToWrapAt + 1;
					}
					else
					{
						wrappedLine.append(str.substring(offset));
						offset = inputLineLength;
					}
				}
			}
		}

		// Whatever is left in line is short enough to just pass through
		wrappedLine.append(str.substring(offset));

		return wrappedLine.toString();
	}
	
	////////////////////////////////////////////////////////////////////
	/**
	 * returns true iff the string given contains non-alpha numeric characters
	 */
	public static boolean containsNonAlphaNumerics(String str)
	{
		if( str == null )
			return false;
		char[] chars = str.toCharArray();
		int len = chars.length;
		for( int i=0; i < len; i++ )
		{
			if( !Character.isLetterOrDigit(chars[i]) )
				return true;
		}//for
		return false;
	}//method
	
	////////////////////////////////////////////////////////////////////
	/**
	 * Strips all non alphanumeric characters from the string given 
	 * (using Character.isLetterOrDigit)
	 */
	public static String stripNonAlphaNumerics(String str)
	{
		if( str == null )
			return null;
		char[] chars = str.toCharArray();
		int len = chars.length;
		StringBuilder sb = new StringBuilder(len);
		for( int i=0; i < len; i++ )
		{
			if( Character.isLetterOrDigit(chars[i]) )
				sb.append(chars[i]);
		}//for
		return sb.toString();
	}//method
	
	////////////////////////////////////////////////////////////////////
	/**
	 * Strips all non numeric characters from the string given 
	 * (using Character.isDigit)
	 */
	public static String stripNonNumerics(String str)
	{
		if( str == null )
			return null;
		char[] chars = str.toCharArray();
		int len = chars.length;
		StringBuilder sb = new StringBuilder(len);
		for( int i=0; i < len; i++ )
		{
			if( Character.isDigit(chars[i]) )
				sb.append(chars[i]);
		}//for
		return sb.toString();
	}//method
	
	
	/**
	 * returns a std formatted name string
	 * @param lastFirst if true, will reutrn the name like: LNAME, FNAME MNAME
	 * otherwise the format will be FNAME MNAME LNAME 
	 */
	public static String makeNameString(String fname, String mname, String lname, boolean lastFirst)
	{
		String retVal = null;
		
		//fName = ObjectUtility.coalesce(fname, "");
		//lname = ObjectUtility.coalesce(lname, "");
		if( lastFirst )
		{
			retVal = lname + ", " + fname;
			if( mname != null && mname.length() > 0 )
				retVal += " " + mname;
		}//if
		else
		{
			retVal = fname;
			if( mname != null && mname.length() > 0 )
				retVal += " " + mname;
			retVal += " " + lname;
		}//else
		return retVal.trim();
	}//method
	
	////////////////////////////////////////////////////////////////////
	/**
	 * replaces all newline characters with the newChar,
	 * all carriege returns are removed
	 */
	public static String makeOneLine(String s, char newChar)
	{
		if( s == null )
			return null;
		s = s.replace('\n', newChar);
		s = s.replace('\r', '\u0000');
		return s;
	}//method
	
	////////////////////////////////////////////////////////////////////
	/**
	 * returns true iff two the strings are equal or both strings are null
	 */
	public static boolean isEqual(String s1, String s2)
	{
		if( s1 == s2 )
			return true;
		
		if( s1 == null || s2 == null )
			return false;
		
		return s1.equals(s2);
	}//method
	
	////////////////////////////////////////////////////////////////////
	/**
	 * returns true iff the object passed in is an emptry string
	 * NOTE: will return false if the object is null
	 */
	public static boolean isEmptyString(Object o)
	{
		return (o instanceof String && o != null && ((String)o).length() == 0 );
	}//method

	////////////////////////////////////////////////////////////////////
	/**
	 * returns true if the string passed in is null or is empty or contains only blanks
	 * equivalent to if( s == null || s.trim().length() == 0 )
	 */
	public static boolean isEmptyOrNull(String s)
	{
		if( s == null || s.trim().length() == 0 )
			return true;
		return false;
	}//method
	
	////////////////////////////////////////////////////////////////////
	/**
	 * returns the trimmed string, or null of the string is null or empty
	 */
	public static String getTrimmedString(String s)
	{
		if( s == null )
			return null;
		
		s = s.trim();
		if( s.length() == 0 )
			return null;
		return s;
	}//method

	////////////////////////////////////////////////////////////////////
	/**
	 * returns the revers of the string passed in
	 */
	public static String reverse(String s)
	{
		if( s == null )
			return null;

		char[] charArr = s.toCharArray();
		int len = charArr.length;
		char[] reversString = new char[len];
		int pos = 0;
		for( int i = len-1; i>=0; i-- )
			reversString[pos++] = charArr[i];

		return new String(reversString);
	}//method


	////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////
	// -------------------------------------------------------------
	/**
	 * returns the first non null value or null
	 */
	public static String coalesce(String val1, String val2)
	{
		String retVal = val1;
		if(retVal == null)
		{retVal = val2;}
		return retVal;
	}
	// -------------------------------------------------------------
	/**
	 * returns the first non null value or null
	 */
	public static String coalesce(String val1, String val2, String val3)
	{
		String retVal = val1;
		if(retVal == null)
		{retVal = val2;}
		if(retVal == null)
		{retVal = val3;}
		return retVal;
	}

	////////////////////////////////////////////////////////////////////
	/**
	 * remove preceding chars
	 */
	public static String removePrecedingChars(String s, char c)
	{
		if ( s == null )
			return null;

		int firstKeepIndex = 0;
		int stringLen = s.length();
		char[] strArr = s.toCharArray();
		for( int i=0; i < stringLen; i++ )
		{
			if( strArr[i] == c )
				firstKeepIndex++;
			else
				break;
		}
		String returnString = s;
		if( firstKeepIndex > 0 )
			returnString = s.substring(firstKeepIndex);
		return returnString;
	}//method

	////////////////////////////////////////////////////////////////////
	/**
	 * remove trailing chars
	 */
	public static String removeTrailingChars(String s, char c)
	{
		if ( s == null )
			return null;

		int stringLen = s.length();
		int lastIndex = stringLen - 1;
		int lastKeepIndex = lastIndex;

		char[] strArr = s.toCharArray();
		for( int i = lastIndex; i >= 0; i-- )
		{
			if( strArr[i] == c )
				lastKeepIndex--;
			else
				break;
		}//for
		String returnString = s;
		if( lastKeepIndex < lastIndex )
			returnString = s.substring(0, lastKeepIndex+1);
		return returnString;
	}//method

	////////////////////////////////////////////////////////////////////
	/**
	 * returns the bytelength of the string for a UTF-8 encoding. Characters within the ASCII set take up 1 byte in UTF-8, both those beyond can take up more
	 */
	public static int getStringByteLengthUTF8(String val)
	{
		if( val == null )
			return -1;
		return val.getBytes(utf8Charset).length;
	}//method

	////////////////////////////////////////////////////////////////////
	/**
	 * replace any non-ascii characters in the string given
	 */
	public static String replaceNonASCII(String val, String replacement)
	{
		if( val == null )
			return val;
		return val.replaceAll("[^\\p{ASCII}]", replacement);
	}//method
	
	////////////////////////////////////////////////////////////////////
	/**
	 * replace any control characters in the string given
	 */
	public static String replaceControlChars(String val, String replacement)
	{
		if( val == null )
			return val;
		return val.replaceAll("[\\p{Cntrl}]", replacement);
	}//method
	
	////////////////////////////////////////////////////////////////////
	/**
	* replace any control characters in the string given
	*/
	public static String replaceWhiteSpaceChars(String val, String replacement)
	{
		if( val == null )
		return val;
		return val.replaceAll("[\\p{Space}]", replacement);
	}//method
	
	
	////////////////////////////////////////////////////////////////////
	/**
	 * replaces various smart characters and others with equivalent 'normal characters'
	 * MS-word is bad about using special chars that can mess up XML files and whatnot  
	 */
	public static String replaceSmartCharacters(String s)
	{
		if( s == null )
			return s;
		s = s.replace( (char)145, '\'');
		s = s.replace( (char)8216, '\''); // left single quote
		s = s.replace( (char)146, '\'');
		s = s.replace( (char)8217, '\''); // right single quote
		s = s.replace( (char)147, '\"');
		s = s.replace( (char)148, '\"');
		s = s.replace( (char)8220, '\"'); // left double
		s = s.replace( (char)8221, '\"'); // right double
		s = s.replace( (char)8211, '-' ); // em dash??
		s = s.replace( (char)149,  '*' ); // bullet  
		s = s.replace( (char)8226, '*' ); // bullet (one of many)  
		s = s.replace( (char)150, '-' );
		return s;
	}//method

	////////////////////////////////////////////////////////////////////
	/**
	 * strips the string of the outer chars given. Can be used to strip off quotes, brackets, parentheses, etc.
	 * if the string starts and ends with the  outerChar substring, the outer instances of the substring will be stripped off
	 * NOTE: this is a one pass, so nested outer values will not strip with a single call.
	 * For example, stripOuterChars("[[raaaa]]", "[", "]") will yield   "[raaaa]"
	 */
	public static String stripOuterChars(String s, String beginningChars, String endingChars)
	{
		if( s.startsWith(beginningChars) && s.endsWith(endingChars) )
		{
			int startIdx = beginningChars.length();
			int endIdx = s.length() - endingChars.length();
			s = s.substring(startIdx, endIdx);
		}
		return s;
	}//method
	
	//////////////////////////////////////////////////////////////////
	/**
	 * returns a string in the MLA (Modern Language Association) format for citations. 
	 * ie  <lname>, <fname> <mname>, <suffix> 
	 * 
	 * lName is the only required parameter. If the other parameters are empty they will not be included in the return, nor will their formatting  
	 */
	public static String getPersonNameMLAFormat(String fName, String mName, String lName, String suffix)
	{
		if( lName == null )
			return null;
		
		StringBuilder sb = new StringBuilder(lName);
		
		if( fName != null )
		{
			sb.append(", ").append(fName);
			if( mName != null )
				sb.append(" ").append(mName);
			
			if( suffix != null )
				sb.append(", ").append(suffix);
		}
		return sb.toString().trim();
	}
	// ****************************************************************
	// ****************************************************************
	// -------------------------------------------------------------
	// -------------------------------------------------------------
	// -------------------------------------------------------------
	// -------------------------------------------------------------
	/** ****************************************************************
	 * Compute the distance between two strings.
	 * @param source	What to match, source
	 * @param target	Strings to match
	 * @return int Distance, lower value is better
	 * ref: http://www.codeproject.com/Questions/419563/Get-the-nearest-Match-of-the-string-in-list-of-str
	 * ****************************************************************
	 */
    public static int getLevenshteinDistance(String source, String target)
    {
        int n = source.length();
        int m = target.length();
 
        // Step 1
        if (n == 0)
        {   return m;}
 
        if (m == 0)
        {   return n; }
 
        int[][] d = new int[n + 1][ m + 1];
        // Step 2
        for (int i = 0; i <= n; d[i][ 0] = i++)
        {
        }
 
        for (int j = 0; j <= m; d[0][ j] = j++)
        {
        }
 
        // Step 3
        for (int i = 1; i <= n; i++)
        {
            //Step 4
            for (int j = 1; j <= m; j++)
            {
                // Step 5
                int cost = (target.charAt(j - 1) == source.charAt(i - 1)) ? 0 : 1;
 
                // Step 6
                d[i][j] = Math.min(
                    Math.min(d[i - 1][ j] + 1, d[i][ j - 1] + 1),
                    d[i - 1][ j - 1] + cost);
            }
        }
        // Step 7
        return d[n][ m];
    }	
	/** ****************************************************************
	 * FInd the closest matched string .
	 * @param source	What to match, source
	 * @param targets	List of Strings to match
	 * @return int Distance, lower value is better
	 * ****************************************************************
	 */
    public static int getClosestLevenshteinDistance(String source, List<String> targets)
    {
    	int retVal = -1;
    	int closestMatchVal = Integer.MAX_VALUE;
    	int tmpval;
    	int currIdx = 0;
    	for(String curr : targets )
    	{
    		tmpval = getLevenshteinDistance(source, curr);
    		if(tmpval < closestMatchVal)
    		{	closestMatchVal = tmpval;
    			retVal = currIdx;
    		}
    		++currIdx;
    	}
        return retVal;
    }
    
	/**
	 * convenience method to compare two objects for equality without the hassle of dealing with nulls
	 * returns true iff both values are null, one == two, one.equals(two), or one.toString().equals(two.toString())
	 * 
	 */
	public static boolean equals(Object one, Object two, boolean ignoreCase)
	{
		if( one == two )
			return true;
		
		//since they are not outright equal we know both values aren't null, so if either is null we know they cant be equal 
		if( one == null || two == null )
			return false;
		
		//assert one != null : "Bad logic in comparison";
		//assert two != null : "Bad logic in comparison";
		
		if( one.equals(two) )
			return true;
		
		String str1 = one.toString();
		String str2 = two.toString();
		
		if( ignoreCase )
		{
			if( str1.equalsIgnoreCase(str2) )
				return true;
		}
		else if( str1.equals(str2) )
			return true;
		
		return false;
	}
  	
	// ****************************************************************
	// ****************************************************************
	////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////
	// -------------------------------------------------------------
	// -------------------------------------------------------------
	// -------------------------------------------------------------
	// -------------------------------------------------------------
	/*public static void main(String[] chars)
	{
		/ *String text = "Red";
		List<String> strList = new ArrayList<String>();
		strList.add("RED_Red");
		strList.add("MUL_Multicolored");
		System.err.println("matched "+getClosestLevenshteinDistance(text, strList));* /
		
		System.err.println("t->" + equals(null, null, true));
		System.err.println("f->" + equals(null, 1, true));
		System.err.println("f->" + equals(1, null, true));
		System.err.println("t->" + equals(1, "1", true));
		System.err.println("t->" + equals("a", "A", true));
		System.err.println("f->" + equals("a", "A", false));
	}*/
	
} // class

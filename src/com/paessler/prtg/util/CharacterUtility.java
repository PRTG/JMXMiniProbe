package com.paessler.prtg.util;

import java.text.CharacterIterator;
import java.text.StringCharacterIterator;

/******************************************************************************
 *  A library of static byte utility functions
 *  @author JR Andreassen
 *  @version 0.1
 *****************************************************************************/
public abstract class CharacterUtility
{
	/** Constant for use as Invalid poss or not found with indexOf() etc */
		public static final int	INVALID_POSSITION	= -1;

		//-----------------------------------------------------------
		public static final char char_0		= '0';
		public static final char char_1		= '1';
		public static final char char_2		= '2';
		public static final char char_3		= '3';
		public static final char char_4		= '4';
		public static final char char_5		= '5';
		public static final char char_6		= '6';
		public static final char char_7		= '7';
		public static final char char_8		= '8';
		public static final char char_9		= '9';
		//-----------------------------------------------------------
		public static final char char_A		= 'A';
		public static final char char_a		= 'a';

		public static final char char_F		= 'F';
		public static final char char_f		= 'f';

		public static final char char_Z		= 'Z';
		public static final char char_z		= 'z';
		
		//-----------------------------------------------------------
		public static final char QuestionMark	= '?';
		public static final char Blank				= ' ';
		public static final char Period				= '.';
		public static final char Comma				= ',';
		public static final char Colon				= ':';
		public static final char SemiColon		= ';';
		public static final char Bang					= '!';
		public static final char CaretSign		= '^';
		public static final char AtSign				= '@';
		public static final char Asterisk			= '*';
		public static final char HashMark			= '#';
		public static final char PercentSign	= '%';
		public static final char DollarSign		= '$';
		public static final char Tilde				= '~';
		public static final char Bar					= '|';
		public static final char Plus					= '+';
		public static final char Equal				= '=';
		public static final char Minus				= '-';
		public static final char Hyphen				= Minus;
		public static final char Underscore		= '_';
		public static final char ForwardSlash	= '/';
		public static final char BackSlash		= '\\';
		public static final char Quote				= '\'';
		public static final char LeftParen		= '(';
		public static final char RightParen		= ')';

		public static final char LineFeed			= '\n';
		public static final char CarriageRet	= '\r';
		public static final char HorizTab			= '\t';

		public static final char LeftCurlyBrace	= '{';
		public static final char RightCurlyBrace= '}';

		public static final char LeftSquareBrace= '[';
		public static final char RightSquareBrace= ']';

		public static final char GreaterThan		= '<';
		public static final char LessThan				= '>';

	//-----------------------------------------------------------
	/**
	 * Convert character to a readable String
	 * @param ch		Character to convert
	 * @return String	String representation of the character
	 */
	 public static String	toReadableString(char ch)
	 {	String retVal = null;
			if(ch <= ByteUtility.ASCII_CONTROL_US)
			{	retVal = ByteUtility.controlTable[ch];}
			else
			{	retVal = String.valueOf(ch);	}
	 	return retVal;
	 }
	//-----------------------------------------------------------
	/**
	 * Convert character to a readable String
	 * @param ch		Character Array to convert
	 * @return String	String representation of the character array
	 */
	 public static String	toReadableString(char[] charr)
	 {	StringBuffer retVal = new StringBuffer();
			for(int i = 0; i < charr.length; i++)
			{  
				if(charr[i] <= ByteUtility.ASCII_CONTROL_US)
				{	retVal.append(LeftSquareBrace);
					retVal.append(ByteUtility.controlTable[charr[i]]);
					retVal.append(RightSquareBrace);
				}
				else
				{	retVal.append(charr[i]);	}
			} // while
	 	return retVal.toString();
	 }

	 //-----------------------------------------------------------
	/**
	 * Find the Index of a character(case sensitive) in a character sequence
	 * @param src		Sequence of characters to look through
	 * @param what	What character to look for in the string	
	 * @return int	Index of the character to look for.	
	 */
	 public static int	indexOf(char[] src, char what)
	 {	return indexOf(src, what, true); } 
	 
	//-----------------------------------------------------------
	/**
	 * Find the Index of a character in a character sequence
	 * @param src				Sequence of characters to look through
	 * @param what			What character to look for in the string	
	 * @param casesens	What character to look for in the string	
	 * @return int			Index of the character to look for.	
	 */
	 public static int	indexOf(char[] src, char what, boolean casesens)
	 {	int retVal = CharacterIterator.DONE;
			for (int i = 0; i < src.length;i++)
			{	if(equals(what, src[i], casesens))
				{	retVal = i;
					break;
				}
			}
			return retVal; 
	 }

	//-----------------------------------------------------------
	/**
	 * Scans until Character.isWhitespace() fails
	 * @param itr		Iterator Source 
	 * @return int	Count of chars skipped
	 */
	 public static int	eatWhitespace(CharacterIterator itr)
	 {	int retVal= 0;
			char curr = itr.current();
			while (Character.isWhitespace(curr) && curr != CharacterIterator.DONE)
			{	retVal++;
				curr = itr.next();
			}
			return retVal; 
	 }
	//-----------------------------------------------------------
	/**
	 * Scans until iter.current == term
	 * @param itr		Iterator Source 
	 * @param term	Terminating char value
	 * @return String	Sequence/String of characters scanned
	 */
	 public static String	scanAllUntil(CharacterIterator itr, char term)
	 {	StringBuffer retVal = new StringBuffer();
			char curr = itr.current();
			while (curr != term && curr != CharacterIterator.DONE)
			{	retVal.append(curr);
				curr = itr.next();
			}
			return retVal.toString(); 
	 }

	 //-----------------------------------------------------------
	 /**
	  * Converts a CharacterIterator to String
	  * *** NOTE *** Does not Advance iterator.
	  * @param itr		Iterator Source 
	  * @return String	String representation of characters
	  */
	  public static String	toString(CharacterIterator itr)
	  {	StringBuffer retVal = new StringBuffer();
	 		int currIdx = itr.getIndex(); 
	 		for(char curr = itr.current();curr != CharacterIterator.DONE; curr = itr.next())
	 		{	retVal.append(curr);
	 		}
			itr.setIndex(currIdx);
	 		return retVal.toString(); 
	  }
		
	 //-----------------------------------------------------------
	 /**
	  * Gets substring (n characters from startingpoint.
	  * *** NOTE *** Advances iterator.
	  * @param itr		Iterator Source 
	  * @param cnt		Number of characters to fetch
	  * @return String	Sub-Sequence/String of characters scanned
	  */
	  public static String	substring(CharacterIterator itr, int cnt)
	  {	StringBuffer retVal = new StringBuffer();
	 		char curr = itr.current();
//	 		int currIdx = itr.getIndex(); 
	 		for(int i = 0; i < cnt && curr != CharacterIterator.DONE; i++)
	 		{	retVal.append(curr);
	 			curr = itr.next();
	 		}
//			itr.setIndex(currIdx);
	 		return retVal.toString(); 
	  }
	 
	//-----------------------------------------------------------
	/**
	 * Scans/ignores until iter.current == term
	 * @param itr		Iterator Source 
	 * @param term	Terminating char value
	 * @return int	Count of chars skipped
	 */
	 public static int	ignoreAllUntil(CharacterIterator itr, char term)
	 {	int retVal= 0;
			char curr = itr.current();
			while (curr != term && curr != CharacterIterator.DONE)
			{	retVal++;
				curr = itr.next();
			}
			return retVal; 
	 }
	 
	 //-----------------------------------------------------------
	 public static class javaIdentTermTest implements CharacterTest
	 {
		 /**
		  *	Tests wether character is a terminator
		  */
		 public boolean isTerminatorCharacter(char ch)
		 {	return !Character.isJavaIdentifierPart(ch);}
	 } // public static class javaIdentTermTest

	//-----------------------------------------------------------
	/**
	 * Convert/extract number, scans until Character.isJavaIdentifierPart() fails
	 * @param itr		Iterator Source 
	 * @return String	Number
	 */
	 public static String	getIdentifier(CharacterIterator itr)
	 {	return getTerminatedString(itr, new javaIdentTermTest());
	 }
	 
	 //-----------------------------------------------------------
	 /**
	  * Find starting index of String.
		* It will return the starting Index of the matching string.
		* ! NOTE ! Curren possition will be left at the character <b>after</b> of the matching string.
		* IE save starting pos before calling if it's needed.
	  * @param iter				Iterator Source 
	  * @param str				Iterator Source 
		* @param casesens		Flag for wether to do a case sensitive match
	  * @param exactmatch	Flag for exact match
	  * @return int				The starting index of the match or 'CharacterIterator.DONE' if failed.
	  */
	  public static int	findStartIndex(CharacterIterator iter, String str, boolean casesens, boolean exactmatch)
	  {	int retVal = CharacterIterator.DONE;
	  	CharacterIterator strIter = new StringCharacterIterator(str);
	  	char litCurr			= strIter.current();
	  	int strStartIdx		= strIter.getIndex();
	  	int iterStartIdx 	= iter.getIndex();
	  	char lastnonmatch = CharacterIterator.DONE;
	  	int startMatch 		= CharacterIterator.DONE;
	  	char itrCurr			= iter.current();
//	  	if( Log.DEBUG) // && myLog != null && myLog.getLevel() > 5 )
//	  	{	System.out.println("CharacterUtility.findStartIndex(itr, \""+str+"\", casese="+casesens+", exact="+exactmatch+")");}
	  	while(retVal == CharacterIterator.DONE)
	  	{	
	  		
	  		if (litCurr == CharacterIterator.DONE)
	  		{
	  			if (exactmatch)
	  			{	
	  				if (Character.isWhitespace(lastnonmatch))
	  				{
	  					if(itrCurr == CharacterIterator.DONE)
	  					{	retVal = startMatch;}
	  					else if (Character.isWhitespace(itrCurr))
	  					{	retVal = startMatch;}
							
	  				} // if (Character.isWhitespace(lastnonmatch))
	  			 } // if (isExactMatch())
	  			 else
	  			 { retVal = startMatch; }
	  		
	  			 // Success
	  			if (retVal != CharacterIterator.DONE)
	  			{	break;}
	  			
	  			strIter.setIndex(strStartIdx);
	  			litCurr				= strIter.current();
	  			lastnonmatch	= itrCurr;
					startMatch 		= CharacterIterator.DONE;
	  		} // if (litCurr == CharacterIterator.DONE)
	  		else if(!CharacterUtility.equals(litCurr, itrCurr, casesens))
	  		{	lastnonmatch = itrCurr;
	  			strIter.setIndex(strStartIdx);
	  			litCurr		= strIter.current();
					startMatch= CharacterIterator.DONE;
	  		} // else if(litCurr != itrCurr)
	  		else if(CharacterUtility.equals(litCurr, itrCurr, casesens))
	  		{	litCurr = strIter.next();
		  		if (startMatch == CharacterIterator.DONE)
		  		{	startMatch = iter.getIndex();}
	  		} // else if(litCurr == itrCurr)
	  		
	  		if (itrCurr == CharacterIterator.DONE)
	  		{	break;}
	  		else
	  		{	itrCurr = iter.next();}
	  	} // while
			return retVal;		
	  } // findStartIndex
	  
	// ---------------------------------------------------------------------------
	/**
	 * This method finds a matching pair of token's
	 * <PRE>
	 * findMatching("....(xxx)....", 0, "(", ")");
	 * Should then return the possition index of ')'.
	 * </PRE>
	 * ! NOTE ! Curren possition will be left at the character <b>after</b> of the matching string.
	 * IE save starting pos before calling if it's needed.
	 * @param iter					Source CharacterIterator
	 * @param starttoken		Start token
	 * @param endtoken			End token
	 * @return int ending Token Index
	 */
	public static int findMatching(CharacterIterator iter, char starttoken, char endtoken)
	{	int retVal = CharacterIterator.DONE;

		int level = 0;
		
// System.out.println("CharacterUtility.findMatching(Iter["+iter.getIndex()+"=\'"+iter.current()+"\']) starttoken="+starttoken+", endtoken="+endtoken);
		for(char itrCurr = iter.current(); itrCurr != CharacterIterator.DONE;itrCurr = iter.next())
		{	
// System.out.println("CharacterUtility.findMatching(Iter["+iter.getIndex()+"=\'"+iter.current()+"\'])");
			if(CharacterUtility.equals(itrCurr, endtoken))
			{
				if (level == 0)			
				{	retVal = iter.getIndex();
					break;	
				}
				else
				{ level--;	}
			}
			else 	if(CharacterUtility.equals(itrCurr, starttoken))
			{ level++;	}
		}
		return retVal;
	}
	 
	 
	//-----------------------------------------------------------
	/**
	 * Convert/extract number, scans until Character.isJavaIdentifierPart() fails
	 * @param itr		Iterator Source 
	 * @return String	Number
	 */
	 public static String	getTerminatedString(CharacterIterator itr, CharacterTest test)
	 {	StringBuffer retVal= new StringBuffer();
			char curr = itr.current();
			while (!test.isTerminatorCharacter(curr) && curr != CharacterIterator.DONE)
			{	retVal.append(curr);
				curr = itr.next();
			}
			return retVal.toString(); 
	 }
	//-----------------------------------------------------------
	/**
	 * Convert/extract number, scans until Character.isDigit() fails
	 * @param itr		Iterator Source 
	 * @return String	Number
	 */
	 public static String	getNumber(CharacterIterator itr)
	 {	StringBuffer retVal= new StringBuffer();
			char curr = itr.current();
			while (Character.isDigit(curr) && curr != CharacterIterator.DONE)
			{	retVal.append(curr);
				curr = itr.next();
			}
			return retVal.toString(); 
	 }

	 //-----------------------------------------------------------
	 /**
	  * Returns the next character in the sequence without
		* advancing the current.
	  * @param itr		Iterator Source 
	  * @return char	Next character
	  */
	  public static char	peek(CharacterIterator itr)
	  {	int savePos = itr.getIndex();
			char retVal = itr.next();
			itr.setIndex(savePos);
	 		return retVal; 
	  }
	 //-----------------------------------------------------------
	 /**
	  * Get ASCII value for the given character
	  * @param ch			Iterator Source 
	  * @return byte	Ascii value of the character
	  */
/*		
	  *** NOT ACCURATE		 
	  public static byte	getASCII(char ch)
	  {	byte retVal= (byte) ch;
	 		return retVal; 
	  }
*/
	  //-----------------------------------------------------------
	  /**
	   * Get ASCII value for the given character
	   * @param ch			Iterator Source 
	   * @return byte	Ascii value of the character
	   */
/*
 *** NOT ACCURATE		 
	   public static byte	getASCII(Character ch)
	   {	byte retVal= (byte) ch.charValue();
	  		return retVal; 
	   }
*/	 
	 
	 //-----------------------------------------------------------
	 /**
	  * Equality check with case sensitivity option
	  * @param a							Character a
	  * @param b							Character b
	  * @return boolean equality
	  */
	 public static boolean equals(char a, char b)
	 {	return equals(a, b, false); }
	 
	 //-----------------------------------------------------------
	 /**
	  * Equality check with case sensitivity option
	  * @param a							Character a
	  * @param b							Character b
	  * @param casesensitive	Flag to check case insensitive
	  * @return boolean equality
	  */
	 public static boolean equals(char a, char b, boolean casesensitive)
	 {	boolean retVal = (a==b);
			if (!retVal && !casesensitive)
			{	retVal = (Character.toUpperCase(a) == Character.toUpperCase(b));}
			return retVal; 
	 }

	////////////////////////////////////////////////////////////////////
	/**
	 * returns true iff the char given is in the ascii set
	 */
	public static boolean isASCIIChar(char c)
	{
		return c >= 0 && c <= 127;
		//use the posix regular expression..
		//return Character.toString(c).matches("\\p{ASCII}");
	}//method
	 
} // class


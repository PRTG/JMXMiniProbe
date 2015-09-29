package com.paessler.prtg.util;

import com.paessler.prtg.jmx.Logger;

/******************************************************************************
 *  A library of static byte utility functions
 *  @author JR Andreassen
 *  @version 0.1
 *****************************************************************************/
public abstract class ByteUtility
{
	public static final int	INVALID_POSSITION	= -1;
			//-----------------------------------------------------------
			/** byte constants for ASCII Control codes: ^@ NUL (used for padding) */
			public static final byte ASCII_CONTROL_NULL	= 0x00;
			/** byte constants for ASCII Control codes: ^A SOH  (Start of Header ??)*/
			public static final byte ASCII_CONTROL_SOH	= 0x01;
			/** byte constants for ASCII Control codes: ^B STX (Start of Text) */
			public static final byte ASCII_CONTROL_STX	= 0x02;
			/** byte constants for ASCII Control codes: ^C ETX (End of Text) */
			public static final byte ASCII_CONTROL_ETX	= 0x03;
			/** byte constants for ASCII Control codes: ^D EOT (End of Transmission) */
			public static final byte ASCII_CONTROL_EOT	= 0x04;
			/** byte constants for ASCII Control codes: ^E ENQ (Enquiry) */
			public static final byte ASCII_CONTROL_ENQ	= 0x05;
			/** byte constants for ASCII Control codes: ^F ACK (Acknowledge) */
			public static final byte ASCII_CONTROL_ACK	= 0x06;
			/** byte constants for ASCII Control codes: ^G BEL (Bell / System beep) */
			public static final byte ASCII_CONTROL_BEL	= 0x07;
			/** byte constants for ASCII Control codes: ^H BEL (Backspace) */
			public static final byte ASCII_CONTROL_BS		= 0x08;
			/** byte constants for ASCII Control codes: ^I TAB/HT (Horizontal Tab) */
			public static final byte ASCII_CONTROL_TAB	= 0x09;
			/** byte constants for ASCII Control codes: ^J LF (Line Feed) */
			public static final byte ASCII_CONTROL_LF		= 0x0A;
			/** byte constants for ASCII Control codes: ^K VT (Vertical Tab) */
			public static final byte ASCII_CONTROL_VT		= 0x0B;
			/** byte constants for ASCII Control codes: ^L FF (Form Feed) */
			public static final byte ASCII_CONTROL_FF		= 0x0C;
			/** byte constants for ASCII Control codes: ^M CR (Carriage Return) */
			public static final byte ASCII_CONTROL_CR		= 0x0D;
			/** byte constants for ASCII Control codes: ^N SO (Shift Out) */
			public static final byte ASCII_CONTROL_SO		= 0x0E;
			/** byte constants for ASCII Control codes: ^O SI (Shift In) */
			public static final byte ASCII_CONTROL_SI		= 0x0F;
			/** byte constants for ASCII Control codes: ^P DLE (Data Link Escape) */
			public static final byte ASCII_CONTROL_DLE 	= 0x10;
			/** byte constants for ASCII Control codes: ^Q DC1, XON (Device Control 1) */
			public static final byte ASCII_CONTROL_DC1	= 0x11;
			/** byte constants for ASCII Control codes: ^R DC2 (Device Control 2) */
			public static final byte ASCII_CONTROL_DC2	= 0x12;
			/** byte constants for ASCII Control codes: ^S DC3, XOFF (Device Control 3) */
			public static final byte ASCII_CONTROL_DC3	= 0x13;
			/** byte constants for ASCII Control codes: ^T DC4 (Device Control 4) */
			public static final byte ASCII_CONTROL_DC4	= 0x14;
			/** byte constants for ASCII Control codes: ^U NAK (Negative Acknowledge) */
			public static final byte ASCII_CONTROL_NAK	= 0x15;
			/** byte constants for ASCII Control codes: ^V SYN (Synchronus Idle) */
			public static final byte ASCII_CONTROL_SYN	= 0x16;
			/** byte constants for ASCII Control codes: ^W ETB (End Transmission Block) */
			public static final byte ASCII_CONTROL_ETB	= 0x17;
			/** byte constants for ASCII Control codes: ^X CAN (Cancel) */
			public static final byte ASCII_CONTROL_CAN	= 0x18;
			/** byte constants for ASCII Control codes: ^Y EM (End of Medium) */
			public static final byte ASCII_CONTROL_EM		= 0x19;
			/** byte constants for ASCII Control codes: ^Z SUB, [EOF] (Substitute) */
			public static final byte ASCII_CONTROL_SUB	= 0x1A;
			/** byte constants for ASCII Control codes: ^[ ESC (Escape, Alter mode, SEL) */
			public static final byte ASCII_CONTROL_ESC	= 0x1B;
			/** byte constants for ASCII Control codes: ^\ FS (File Separator) */
			public static final byte ASCII_CONTROL_FS		= 0x1C;
			/** byte constants for ASCII Control codes: ^] GS (Group Separator) */
			public static final byte ASCII_CONTROL_GS		= 0x1D;
			/** byte constants for ASCII Control codes: ^^ RS (Record Separator) */
			public static final byte ASCII_CONTROL_RS		= 0x1E;
			/** byte constants for ASCII Control codes: ^_ US (Unit Separator) */
			public static final byte ASCII_CONTROL_US		= 0x1F;

			/** byte constants for ASCII Control codes: DEL Delete */
			public static final byte ASCII_CONTROL_DEL		= 0x7F;
			

			//-----------------------------------------------------------
			/** String constants for ASCII Control codes: ^used for padding */
			public static final String ASCII_CTRL_STRING_NULL	= "NUL".intern();
			/** String constants for ASCII Control codes: Start of Header */
			public static final String ASCII_CTRL_STRING_SOH		= "SOH".intern();
			/** String constants for ASCII Control codes: Start of Text */
			public static final String ASCII_CTRL_STRING_STX		= "STX".intern();
			/** String constants for ASCII Control codes: End of Text */
			public static final String ASCII_CTRL_STRING_ETX		= "ETX".intern();
			/** String constants for ASCII Control codes: End of Transmission */
			public static final String ASCII_CTRL_STRING_EOT		= "EOT".intern();
			/** String constants for ASCII Control codes: Enquiry */
			public static final String ASCII_CTRL_STRING_ENQ		= "ENQ".intern();
			/** String constants for ASCII Control codes: Acknowledge */
			public static final String ASCII_CTRL_STRING_ACK		= "ACK".intern();
			/** String constants for ASCII Control codes: Bell / System beep */
			public static final String ASCII_CTRL_STRING_BEL		= "BEL".intern();
			/** String constants for ASCII Control codes: Backspace */
			public static final String ASCII_CTRL_STRING_BS		= "BS".intern();
			/** String constants for ASCII Control codes: Horizontal Tab */
			public static final String ASCII_CTRL_STRING_TAB		= "TAB".intern();
			/** String constants for ASCII Control codes: Line Feed */
			public static final String ASCII_CTRL_STRING_LF		= "LF".intern();
			/** String constants for ASCII Control codes: Vertical Tab */
			public static final String ASCII_CTRL_STRING_VT		= "VT".intern();
			/** String constants for ASCII Control codes: Form Feed */
			public static final String ASCII_CTRL_STRING_FF		= "FF".intern();
			/** String constants for ASCII Control codes: Carriage Return */
			public static final String ASCII_CTRL_STRING_CR		= "CR".intern();
			/** String constants for ASCII Control codes: Shift Out */
			public static final String ASCII_CTRL_STRING_SO		= "SO".intern();
			/** String constants for ASCII Control codes: Shift In */
			public static final String ASCII_CTRL_STRING_SI		= "SI".intern();
			/** String constants for ASCII Control codes: Data Link Escape */
			public static final String ASCII_CTRL_STRING_DLE 	= "DLE".intern();
			/** String constants for ASCII Control codes: Device Control 1 */
			public static final String ASCII_CTRL_STRING_DC1		= "DC1".intern();
			/** String constants for ASCII Control codes: Device Control 2 */
			public static final String ASCII_CTRL_STRING_DC2		= "DC2".intern();
			/** String constants for ASCII Control codes: Device Control 3 */
			public static final String ASCII_CTRL_STRING_DC3		= "DC3".intern();
			/** String constants for ASCII Control codes: Device Control 4 */
			public static final String ASCII_CTRL_STRING_DC4		= "DC4".intern();
			/** String constants for ASCII Control codes: Negative Acknowledge */
			public static final String ASCII_CTRL_STRING_NAK		= "NAK".intern();
			/** String constants for ASCII Control codes: Synchronus Idle */
			public static final String ASCII_CTRL_STRING_SYN		= "SYN".intern();
			/** String constants for ASCII Control codes: End Transmission Block */
			public static final String ASCII_CTRL_STRING_ETB		= "ETB".intern();
			/** String constants for ASCII Control codes: Cancel */
			public static final String ASCII_CTRL_STRING_CAN		= "CAN".intern();
			/** String constants for ASCII Control codes: End of Medium */
			public static final String ASCII_CTRL_STRING_EM		= "EM".intern();
			/** String constants for ASCII Control codes: Substitute */
			public static final String ASCII_CTRL_STRING_SUB		= "SUB".intern();
			/** String constants for ASCII Control codes: Escape, Alter mode, SEL */
			public static final String ASCII_CTRL_STRING_ESC		= "ESC".intern();
			/** String constants for ASCII Control codes: File Separator */
			public static final String ASCII_CTRL_STRING_FS		= "FS".intern();
			/** String constants for ASCII Control codes: Group Separator */
			public static final String ASCII_CTRL_STRING_GS		= "GS".intern();
			/** String constants for ASCII Control codes: Record Separator */
			public static final String ASCII_CTRL_STRING_RS		= "RS".intern();
			/** String constants for ASCII Control codes: Unit Separator */
			public static final String ASCII_CTRL_STRING_US		= "US".intern();
			/** String constants for ASCII Control codes: Unit Separator */
			public static final String ASCII_CTRL_STRING_DEL		= "DEL".intern();

			/** Conversion table for String constants for ASCII Control codes */
		protected static final String[]	controlTable =
		{
			ASCII_CTRL_STRING_NULL,		ASCII_CTRL_STRING_SOH,
			ASCII_CTRL_STRING_STX,		ASCII_CTRL_STRING_ETX,
			ASCII_CTRL_STRING_EOT,		ASCII_CTRL_STRING_ENQ,
			ASCII_CTRL_STRING_ACK,		ASCII_CTRL_STRING_BEL,
			ASCII_CTRL_STRING_BS,			ASCII_CTRL_STRING_TAB,
			ASCII_CTRL_STRING_LF,			ASCII_CTRL_STRING_VT,
			ASCII_CTRL_STRING_FF,			ASCII_CTRL_STRING_CR,
			ASCII_CTRL_STRING_SO,			ASCII_CTRL_STRING_SI,
			ASCII_CTRL_STRING_DLE,		ASCII_CTRL_STRING_DC1,
			ASCII_CTRL_STRING_DC2,		ASCII_CTRL_STRING_DC3,
			ASCII_CTRL_STRING_DC4,		ASCII_CTRL_STRING_NAK,
			ASCII_CTRL_STRING_SYN,		ASCII_CTRL_STRING_ETB,
			ASCII_CTRL_STRING_CAN,		ASCII_CTRL_STRING_EM,
			ASCII_CTRL_STRING_SUB,		ASCII_CTRL_STRING_ESC,
			ASCII_CTRL_STRING_FS,			ASCII_CTRL_STRING_GS,
			ASCII_CTRL_STRING_RS,			ASCII_CTRL_STRING_US
		};
		protected static final String[]	controlTablewbrace =
		{
			CharacterUtility.LeftSquareBrace + ASCII_CTRL_STRING_NULL + CharacterUtility.RightSquareBrace,
			CharacterUtility.LeftSquareBrace + ASCII_CTRL_STRING_SOH + CharacterUtility.RightSquareBrace,
			CharacterUtility.LeftSquareBrace + ASCII_CTRL_STRING_STX + CharacterUtility.RightSquareBrace,
			CharacterUtility.LeftSquareBrace + ASCII_CTRL_STRING_ETX + CharacterUtility.RightSquareBrace,
			CharacterUtility.LeftSquareBrace + ASCII_CTRL_STRING_EOT + CharacterUtility.RightSquareBrace,
			CharacterUtility.LeftSquareBrace + ASCII_CTRL_STRING_ENQ + CharacterUtility.RightSquareBrace,
			CharacterUtility.LeftSquareBrace + ASCII_CTRL_STRING_ACK + CharacterUtility.RightSquareBrace,
			CharacterUtility.LeftSquareBrace + ASCII_CTRL_STRING_BEL + CharacterUtility.RightSquareBrace,
			CharacterUtility.LeftSquareBrace + ASCII_CTRL_STRING_BS + CharacterUtility.RightSquareBrace,
			CharacterUtility.LeftSquareBrace + ASCII_CTRL_STRING_TAB + CharacterUtility.RightSquareBrace,
			CharacterUtility.LeftSquareBrace + ASCII_CTRL_STRING_LF + CharacterUtility.RightSquareBrace,
			CharacterUtility.LeftSquareBrace + ASCII_CTRL_STRING_VT + CharacterUtility.RightSquareBrace,
			CharacterUtility.LeftSquareBrace + ASCII_CTRL_STRING_FF + CharacterUtility.RightSquareBrace,
			CharacterUtility.LeftSquareBrace + ASCII_CTRL_STRING_CR + CharacterUtility.RightSquareBrace,
			CharacterUtility.LeftSquareBrace + ASCII_CTRL_STRING_SO + CharacterUtility.RightSquareBrace,
			CharacterUtility.LeftSquareBrace + ASCII_CTRL_STRING_SI + CharacterUtility.RightSquareBrace,
			CharacterUtility.LeftSquareBrace + ASCII_CTRL_STRING_DLE + CharacterUtility.RightSquareBrace,
			CharacterUtility.LeftSquareBrace + ASCII_CTRL_STRING_DC1 + CharacterUtility.RightSquareBrace,
			CharacterUtility.LeftSquareBrace + ASCII_CTRL_STRING_DC2 + CharacterUtility.RightSquareBrace,
			CharacterUtility.LeftSquareBrace + ASCII_CTRL_STRING_DC3 + CharacterUtility.RightSquareBrace,
			CharacterUtility.LeftSquareBrace + ASCII_CTRL_STRING_DC4 + CharacterUtility.RightSquareBrace,
			CharacterUtility.LeftSquareBrace + ASCII_CTRL_STRING_NAK + CharacterUtility.RightSquareBrace,
			CharacterUtility.LeftSquareBrace + ASCII_CTRL_STRING_SYN + CharacterUtility.RightSquareBrace,
			CharacterUtility.LeftSquareBrace + ASCII_CTRL_STRING_ETB + CharacterUtility.RightSquareBrace,
			CharacterUtility.LeftSquareBrace + ASCII_CTRL_STRING_CAN + CharacterUtility.RightSquareBrace,
			CharacterUtility.LeftSquareBrace + ASCII_CTRL_STRING_EM + CharacterUtility.RightSquareBrace,
			CharacterUtility.LeftSquareBrace + ASCII_CTRL_STRING_SUB + CharacterUtility.RightSquareBrace,
			CharacterUtility.LeftSquareBrace + ASCII_CTRL_STRING_ESC + CharacterUtility.RightSquareBrace,
			CharacterUtility.LeftSquareBrace + ASCII_CTRL_STRING_FS + CharacterUtility.RightSquareBrace,
			CharacterUtility.LeftSquareBrace + ASCII_CTRL_STRING_GS + CharacterUtility.RightSquareBrace,
			CharacterUtility.LeftSquareBrace + ASCII_CTRL_STRING_RS + CharacterUtility.RightSquareBrace,
			CharacterUtility.LeftSquareBrace + ASCII_CTRL_STRING_US + CharacterUtility.RightSquareBrace
		};

		//-----------------------------------------------------------
		/**
		 * Convert character to a readable String
		 * @param retVal		Buffer to use, also retVal
		 * @param bytearr		Byte Array to convert
		 * @param len				Number of bytes to convert
		 * @return String	String representation of the Byte array
		 */
		 public static String	toArrayDefString(byte[] bytearr, int len)
		 {	
			 StringBuilder retVal = new StringBuilder(len*10);
				retVal.append("{");
			 	for(int i = 0; i < len; i++)
			 	{
			 		if(i != 0)
			 		{	retVal.append(",");}
					retVal.append(bytearr[i]);
				}
				retVal.append("}");
				return retVal.toString();
		 }
	//-----------------------------------------------------------
	/**
	 * Convert character to a readable String
	 * @param retVal		Buffer to use, also retVal
	 * @param bytearr		Byte Array to convert
	 * @param len				Number of bytes to convert
	 * @return String	String representation of the Byte array
	 */
	 public static String	toReadableString(StringBuffer retVal, byte[] bytearr, int len)
	 {	
		 	for(int i = 0; i < len; i++)
		 	{
				toReadableValue(retVal, bytearr[i]);
			}
			return retVal.toString();
	 }
	//-----------------------------------------------------------
	/**
	 * Convert character to a readable String
	 * @param retVal		Buffer to use, also retVal
	 * @param bytearr		Byte Array to convert
	 * @return String	String representation of the Byte array
	 */
	 public static String	toReadableString(StringBuffer retVal, byte[] bytearr)
	 { 
		 return toReadableString(retVal, bytearr, bytearr.length);
	 }
	//-----------------------------------------------------------
	/**
	 * Convert character to a readable String
	 * @param bytearr		Byte Array to convert
	 * @return String	String representation of the Byte array
	 */
	 public static String	toReadableString(byte[] bytearr)
	 { 
		 return toReadableString(new StringBuffer(), bytearr);
	 }
	//-----------------------------------------------------------
	/**
	 * Convert character to a readable String
	 * @param ch		Byte Array to convert
	 * @return String	String representation of the Byre array
	 */
	 public static String	toReadableString(byte[] bytearr, byte termchar)
	 {	StringBuffer retVal = new StringBuffer();
		 	for(int i = 0; bytearr[i] != termchar && i < bytearr.length; i++)
		 	{
				toReadableValue(retVal, bytearr[i]);
			}
			return retVal.toString();
	 }
	 
	//-----------------------------------------------------------
	/**
	 * Convert character to a readable String
	 * @param ch		Byte Array to convert
	 * @return String	String representation of the Byre array
	 */
	 public static String	toReadableValue(byte byteval)
	 {
			StringBuffer retVal = new StringBuffer();
			toReadableValue(retVal, byteval);
			return retVal.toString();
	 }
	//-----------------------------------------------------------
	/**
	 * Convert character to a readable String
	 * @param ch		Byte Array to convert
	 * @return boolean	Is it a printable value
	 *  true => Non Control
	 */
	 public static boolean toReadableValue(StringBuffer buff, byte byteval)
	 {
		 boolean retVal = false;
		 int tmpval = byteval;
		 if(byteval < 0)
		 {  tmpval = 256 + byteval; }
	 		if(tmpval <= ASCII_CONTROL_US)
	 		{	//buff.append(CharacterUtility.LeftSquareBrace);
				buff.append(controlTablewbrace[tmpval]);
				//buff.append(CharacterUtility.RightSquareBrace);
			}
	 		else if (tmpval == ASCII_CONTROL_DEL)
	 		{	buff.append(CharacterUtility.LeftSquareBrace);
				buff.append(ASCII_CTRL_STRING_DEL);
				buff.append(CharacterUtility.RightSquareBrace);
			}
	 		else if (tmpval > ASCII_CONTROL_DEL)
	 		{	buff.append(CharacterUtility.LeftSquareBrace);
				buff.append(StringUtility.HEX_STRING_PREFIX).append(StringUtility.toHexString(byteval, true));
				buff.append(CharacterUtility.RightSquareBrace);
			}
	 		else
	 		{	buff.append((char)tmpval);
				retVal = true;
			}
		 return retVal;
	 }
			
	//-----------------------------------------------------------
	/**
	 * Find the index of a byte value
	 * @param src			Source array
	 * @param val			What to look for
	 * @return int		Count of occurances
	 */
	public static int	occurs(byte[] src, byte val)
	{	
		int retVal = 0;
		if (src != null)
		{
//System.out.println("ByteUtility.occurs(byte["+src.length+"], "+ val +")");
				for(int i = 0; i < src.length; i++)
				{	if (src[i] == val)
					{	retVal++;	}
				}
			} // if valid
			return retVal;
	}
	
	//-----------------------------------------------------------
	/**
	 * Find the index of a byte value
	 * @param src			Source array
	 * @param val			What to look for
	 * @return int		Index of occurance
	 */
	public static int	indexOf(byte[] src, byte val, int startidx)
	{	
		int retVal = INVALID_POSSITION;
		if (src != null && startidx > -1 && startidx <= src.length)
		{
//System.out.println("ByteUtility.indexOf(byte["+src.length+"], "+ val +")");
				for(int i = startidx; retVal == INVALID_POSSITION && i < src.length; i++)
				{	if (src[i] == val)
					{	retVal = i;	}
				}
			} // if valid
			return retVal;
	}
	//-----------------------------------------------------------
	/**
	 * Find the index of a byte value
	 * @param src			Source array
	 * @param val			What to look for
	 * @return int		Index of occurance
	 */
	public static int	indexOf(byte[] src, byte val)
	{
		return indexOf(src, val, 0);
	}
	//-----------------------------------------------------------
	/**
	 * Return a slice of an array
	 * @param src			Source array
	 * @param start		Where to start
	 * @param cnt			How many
	 * @return byte[]	array slice	
	 */
	public static byte[]	slice(byte[] src, int start, int cnt)
	{	return slice(src, new byte[cnt], start, cnt);	}
	//-----------------------------------------------------------
	/**
	 * Return a slice of an array
	 * @param src			Source array
	 * @param dest		Destination array
	 * @param start		Where to start
	 * @param cnt			How many
	 * @return byte[]	array slice	
	 */
	public static byte[]	slice(byte[] src, byte[] dest, int start, int cnt)
	{	return slice(src, dest, start, cnt, 0);	}
	
	//-----------------------------------------------------------
	/**
	 * Return a slice of an array
	 * @param src					Source array
	 * @param dest				Destination array
	 * @param start				Where to start
	 * @param cnt					How many
	 * @param destoffset	Startingpoint in destination
	 * @return byte[]			array slice	
	 */
	public static byte[]	slice(byte[] src, byte[] dest, int start, int cnt, int destoffset)
	{	
		byte retVal[] = dest;

//System.out.println("ByteUtility.slice(byte["+src.length+"], byte["+dest.length+"], "+ start +", "+ cnt +", "+ destoffset +")");
// ##&&## Switch to use ArrayUtility.sysArrayCopy when it's tested
/*				if(!ArrayUtility.sysArrayCopy(src, start, dest, destoffset, cnt))
				{	retVal = null;}
*/
		if (src != null && src.length >= (start+cnt))
		{
				for(int i = 0; i < cnt && start < src.length; i++)
				{	retVal[i+destoffset] = src[start++];
				}
			} // if valid
			return retVal;
	}
	//-----------------------------------------------------------
	/**
	 * Compare byte for byte
	 * @param src		Source array
	 * @param from	What to look for
	 * @param to		What to replace with on match
	 * @return byte[]	array with replaced values	
	 */
	public static byte[]	replace(byte[] src, byte from, byte to)
	{	
		byte retVal[] = null;
		if (src != null)
		{	
			retVal = new byte[src.length];
			for(int i=0; i < src.length; i++)
			{	
				if (src[i] == from)
				{	retVal[i] = to;	}
				else
				{	retVal[i] = src[i];}
			}
		}
		return retVal;
	}
	//-----------------------------------------------------------
	/**
	 * Compare n bytes byte for byte
	 * @param arra			Source array
	 * @param arrb			Source array
	 * @param len				number of bytes to compare
	 * @return boolean	equality
	 */
	public static boolean	compare(byte[] arra, byte[] arrb, int len)
	{	int i = 0;
		boolean retVal = false;
		if (arra != null && arrb != null)
		{	
			if (arra.length >= len  && arrb.length >= len)
			{	retVal = true;
				for(i=0; retVal && i < len; i++)
				{	if (arra[i] != arrb[i])
					{	retVal = false;	}
				}
//Log.printDebug("ret="+retVal+", arra["+(i-1)+"]="+arra[i-1]+", arrb["+(i-1)+"]="+arrb[i-1]);
			}
		}
		return retVal;
	}
	//-----------------------------------------------------------
	/**
	 * Compare byte for byte
	 * @param arra			Source array
	 * @param arrb			Source array
	 * @return boolean	equality
	 */
	public static boolean	compare(byte[] arra, byte[] arrb)
	{	int len = -1;
		boolean retVal = false;
		if (arra == null || arrb == null)
		{	return retVal;	}

		len = arra.length;
		if (len != arrb.length)
		{	return retVal;	}
		
		retVal = true;
		for(int i=0; retVal && i < len; i++)
		{	if (arra[i] != arrb[i])
			{	retVal = false;	}
		}
		return retVal;
	}
	//-----------------------------------------------------------
	/**
	 * Create String from Array of byte
	 * @param srcbytes			Source array
	 * @param arrb			Source array
	 * @return boolean	equality
	 */
	public static String	toString(byte[] srcbytes, int start)
	{ return toString(srcbytes, start, srcbytes.length - start);}
	
	//-----------------------------------------------------------
	/**
	 * Compare byte for byte
	 * @param srcbytes			Source array
	 * @param arrb			Source array
	 * @return boolean	equality
	 */
	public static String	toString(byte[] srcbytes, int start, int length)
	{	String retVal = null;
		if (srcbytes != null )
		{	try
			{	retVal = new String(srcbytes, start, length);	}
			catch(Exception e)		
			{	
				Logger.log("("+start+", "+length+") "+e);
			}
		}
		return retVal;
	}	
	//-----------------------------------------------------------
	/**
	 * Compare byte for byte
	 * @param srcbytes			Source array
	 * @param arrb			Source array
	 * @return boolean	equality
	 */
	public static String	toStringDebug(byte[] srcbytes, int len)
	{	String retVal = null;
		if (srcbytes != null && len > 0)
		{	
			StringBuffer strbuff = new StringBuffer(len);
			int i = 0;
			try
			{	
				for (; i < len; i++)
				{
					strbuff.append((char) srcbytes[i]);
				}
				retVal = strbuff.toString();
			}
			catch(Exception e)		
			{	Logger.log("(i="+i+") strBuff=>"+strbuff+ " " + e);
			}
		}
		return retVal;
	}	
	
// ---------------------------------------------------------------------------
/**
 * Encode bytes as a hex String
 * Assumes a prefix of '0x'
 * @param valarr Byte Array to encode as a Hex String
 * @param len Length to encode
 * @return String The encoded string
 */
	public static  String toHexString(byte[] valarr)
	{	return toHexString(valarr, 0, valarr.length);}
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
	{	return StringUtility.toHexString(valarr, start, len, StringUtility.HEX_STRING_PREFIX);}

	// ---------------------------------------------------------------------------
/**
 * Decode bytes from a hex String
 * Assumes a prefix of '0x'
 * @param valarr Byte Array to encode as a Hex String
 * @param len Length to encode
 * @return String The encoded string
 */
	public static byte[] toByteArray(String hexstring)
	{	
		byte[] retVal = null;
		if(hexstring != null)
		{
			byte[] strbytes = hexstring.getBytes();
			int currbyte = 0, strIdx = 0, strlen = hexstring.length();
			if(hexstring.startsWith(StringUtility.HEX_STRING_PREFIX) && (strlen % 2) ==0 )
			{
				retVal = new byte[(strlen / 2)-1];
				for(int i = 2; i < strlen; currbyte++)
				{
					retVal[currbyte] = (byte)hexToInt((char)strbytes[i++], (char)strbytes[i++]);
				}
			}
		}
		return retVal;
	}

	// -----------------------------------------------------------
	public static final int hexToInt(char ch)
	{
		byte hexchar = (byte) ch;
		int retVal = 0;
		hexchar = (byte)Character.toUpperCase((char)hexchar);
		if(CharacterUtility.char_0 <= hexchar && hexchar <= CharacterUtility.char_9)
		{		retVal = (hexchar - CharacterUtility.char_0);	}
		else 	if(CharacterUtility.char_A <= hexchar && hexchar <= CharacterUtility.char_F)
		{		retVal = 10 + (hexchar - CharacterUtility.char_A);	}
		return retVal;
	}
	
	// -----------------------------------------------------------
	public static final int hexToInt(char msbyte, char lsbyte)
	{
		int retVal = 0;
		retVal = (hexToInt(msbyte) << 4);
		retVal += hexToInt(lsbyte);
		return retVal;
	}
	
	public static void main (String args[])
	{
		System.out.println("Convert \'0x0\' =>"+ ByteUtility.hexToInt('0'));
		System.out.println("Convert \'0x1\' =>"+ ByteUtility.hexToInt('1'));
		System.out.println("Convert \'0x9\' =>"+ ByteUtility.hexToInt('9'));
		System.out.println("Convert \'0xA\' =>"+ ByteUtility.hexToInt('A'));
		System.out.println("Convert \'0xF\' =>"+ ByteUtility.hexToInt('F'));
		System.out.println("Convert \'0x10\' =>"+ ByteUtility.hexToInt('1', '0'));
		System.out.println("Convert \'0x2A\' =>"+ ByteUtility.hexToInt('2', 'A'));
		System.out.println("Convert \'0xFF\' =>"+ ByteUtility.hexToInt('F', 'F'));
		String str = new String("0xFFD8FFE000104A46494600010200000100010000FFDB004300080606070605080707070909080A0C140D0C0B0B0C1912130F141D1A1F1E1D1A1C1C20242E2720222C231C1C2837292C30313434341F27393D38323C2E333432FFDB0043010909090C0B0C180D0D1832211C213232323232323232323232323232323232323232323232323232323232323232323232323232323232323232323232323232FFC00011080134066903012200021101031101FFC4001F0000010501010101010100000000000000000102030405060708090A0BFFC400B5100002010303020403050504040000017D01020300041105122131410613516107227114328191A1082342B1C11552D1F02433627282090A161718191A25262728292A3435363738393A434445464748494A535455565758595A636465666768696A737475767778797A838485868788898A92939495969798999AA2A3A4A5A6A7A8A9AAB2B3B4B5B6B7B8B9BAC2C3C4C5C6C7C8C9CAD2D3D4D5D6D7D8D9DAE1E2E3E4E5E6E7E8E9EAF1F2F3F4F5F6F7F8F9FAFFC4001F0100030101010101010101010000000000000102030405060708090A0BFFC400B51100020102040403040705040400010277000102031104052131061241510761711322328108144291A1B1C109233352F0156272D10A162434E125F11718191A262728292A35363738393A434445464748494A535455565758595A636465666768696A737475767778797A82838485868788898A92939495969798999AA2A3A4A5A6A7A8A9AAB2B3B4B5B6B7B8B9BAC2C3C4C5C6C7C8C9CAD2D3D4D5D6D7D8D9DAE2E3E4E5E6E7E8E9EAF2F3F4F5F6F7F8F9FAFFDA000C03010002110311003F00F7FA28A2800A28A2800A28A2800A28A2800A28A2800A28A2800A28A2800A28A2800A28A2800A28A2800A28A2800A28A2800A28A2800A28A2800A28A2800A28A2800A28A2800A28A2800A28A2800A28A2800A28A2800A28A2800A28A2800A28A2800A28A2800A28A2800A28A2800A28A2800A28A2800A28A2800A28A2800A28A8E79E1B5B796E2E258E182242F249230554503249278000E734012514514005145140051451400514514005145140051451400514514005145140051451400514514005145140051451400514514005145140051451400514514005145140051451400514514005145140051451400514514005145140051451400514514005145140051451400514514005145140051451400514514005145140051451400514514005145140051451400514514005145140051451400514514005145140051451400514514005145140051451400514514005145140051451400514514005145140051451400514514005145140051451400514514005145140051451400514514005145140051451400514514005145140051451400514514005145140051451400514514005145140051451400514514005145140051451400514514005145140051451400514514005145140051451400514514005145140051451400514514005145140051451400514514005145140051451400514514005145140051451400514514005145140051451400514514005145140051451400514514005145140051451400514514005145140051451400573FE3BFF009279E25FFB055D7FE8A6AE82B9FF001C7EF3C17AA598FF00597F17F67C44F4592E08810B7FB21A45248C9C03804F1401D0514514005145140051451400514514005145140051451400514514005145140051451400514514005145140051451400514514005145140051451400514514005145140051451400514514005145140051451400514514005145140051451400514514005145140051451400514514005145140051451400514514005145140051451400514514005145140051451400514514005145140051451400514514005145140051451400514514005145140051451400514514005145140051451400514514005145140051451400514514005145140051451400514514005145140051451400514514005145140051451400514514005145140051451400514514005145140051451400514514005145140051451400514514005145140051451400514514005145140051451400514514005145140051451400514514005145140051451400514514005145140051451400514514005145140051451400514514005145140051451400514514005145140051451400514514005145140051451400514514005145140051451400514514005145140051451400573F7DFF00131F1A69766BCC7A644FA84C47056470D0420E7AAB2B5D120720C6B9201C36E4F3C36B6F2DC5C4B1C304485E492460AA8A064924F0001CE6B1FC3504D25BCFACDE4524579AA3898C72A9578210310C241E54AAFCCCB92048F291C1A00DCA28A2800A28A2800A28A2800A28A2800A28A2800A28A2800A28A2800A28A2800A28A2800A28A2800A28A2800A28A2800A28A2800A28A2800A28A2800A28A2800A28A2800A28A2800A28A2800A28A2800A28A2800A28A2800A28A2800A28A2800A28A2800A28A2800A28A2800A28A2800A28A2800A28A2800A28A2800A28A2800A28A2800A28A2800A28A2800A28A2800A28A2800A28A2800A28A2800A28A2800A28A2800A28A2800A28A2800A28A2800A28A2800A28A2800A28A2800A28A2800A28A2800A28A2800A28A2800A28A2800A28A2800A28A2800A28A2800A28A2800A28A2800A28A2800A28A2800A28A2800A28A2800A28A2800A28A2800A28A2800A28A2800A28A2800A28A2800A28A2800A28A2800A28A2800A28A2800A28A2800A28A2800A28A2800A28A2800A28A2800A28A2800A28A2800A28A2800A28A2800A28A2800A28A2800A28A2800A28A2800A28A2800A28A2800A28A2800A28A2800A28A2800A28A2800A28A2800A28A2800A28A2800A28A2800A28A2800A28A2800A28A2800A28A2800A28A2800A28A2800A28A2800A28A8E79E1B5B796E2E258E182242F249230554503249278000E7340187AF7FC4DB51B3F0F27CD0CBFE95A863902DD0F119EBFEB5F0BB5861E359C7515D0561F86A09A4B79F59BC8A48AF35471318E552AF04206218483CA955F999724091E523835B94005145140051451400514514005145140051451400514514005145140051451400514514005145140051451400514514005145140051451400514514005145140051451400514514005145140051451400514514005145140051451400514514005145140051451400514514005145140051451400514514005145140051451400514514005145140051451400514514005145140051451400514514005145140051451400514514005145140051451400514514005145140051451400514514005145140051451400514514005145140051451400514514005145140051451400514514005145140051451400514514005145140051451400514514005145140051451400514514005145140051451400514514005145140051451400514514005145140051451400514514005145140051451400514514005145140051451400514514005145140051451400514514005145140051451400514514005145140051451400514514005145140051451400514514005145140051451400514514005145140051451400514514005145140051451400573FAF7FC4DB51B3F0F27CD0CBFE95A863902DD0F119EBFEB5F0BB5861E359C7515B93CF0DADBCB71712C70C112179249182AA28192493C000739AC7F0D413496F3EB37914915E6A8E2631CAA55E0840C430907952ABF332E48123CA4706803728A2A9EA5AB69BA35BADC6A9A85A58C0CE1164BA996252D82700B1033804E3D8D005CA2B9FFF0084EFC1FF00F435E87FF83187FF008AA3FE13AF09B71178934A9E43C2C36F7692C921ECA8884B3B1E8154124F0013401D0515CFFF00C265A5FF00CFAEB9FF00822BDFFE3347FC265A5FFCFAEB9FF822BDFF00E33401D0515CFF00FC265A5FFCFAEB9FF822BDFF00E3347FC265A5FF00CFAEB9FF00822BDFFE33401D0515CFFF00C265A5FF00CFAEB9FF00822BDFFE3347FC265A5FFCFAEB9FF822BDFF00E33401D051597A578834FD66E2E2DED3ED6B3DBA23CB1DD594D6CC15CB05204A8A48251C6467A1AD4A0028A28A0028A28A0028A28A0028A28A0028A28A0028A28A0028A28A0028A28A0028A28A0028A28A0028A28A0028A28A0028A28A0028A28A0028A28A0028A28A0028A28A0028A28A0028A28A0028A28A0028A2B9FF00F84A52FB8F0FD8CFAC29E05D40CA968A4F42666203AE7218C4242BB4823380403A0A8E09E1BAB78AE2DE58E682540F1C91B0657523208238208E73587FD8379AB7CFE21BDF3613CFF66DA131DB01FDD90FDF9F82CA77158DC6331035D0500145145001451450014514500145145001451450014514500145145001451450014514500145145001451450014514500145145001451450014514500145145001451450014514500145145001451450014514500145145001451450014514500145145001451450014514500145145001451450014514500145145001451450014514500145145001451450014514500145145001451450014514500145145001451450014514500145145001451450014514500145145001451450014514500145145001451450014514500145145001451450014514500145145001451450014514500145155EFEFADF4CD3AE6FEF24F2ED6D6279A67DA4ED4504B1C0E4E003D2802C515CFF00FC243AA7FD099AE7FDFEB2FF00E48AD4D27528759D1AC754B759160BDB78EE2359000C15D43007048CE0FA9A00B94514500145145001451450014514500145145001451450014514500145145001451450014514500145159FA9EB7A768FE52DEDC6D9A6CF936F1A34B34D8C6ED91202EF80413B41C0E4E050068515CFFF0068F896F7FE3CF4182C233F219353BC5F310FF7C45087575191C19509208F946189FD85AA5F7FC863C413BC7D1ADF4C8FEC51B8EA0960CD306CF759541000231BB7006C5F5FD9E996725E5FDDC1696B1E37CD3C82345C900658F032481F8D63FF00C26BA0C9CD9DD4FA8C7D0CDA659CD7B1A9FEE97851943743B49CE0838C11562C7C2BA169F791DEC3A640F7E99C5F4E0CD727208E667CC8783B796E071D38AD8A00E7FF00E121D4A7E2CFC29AABABFF00A99AE24B786320FDD6706432A2F42418F781FC19F968FB4F8C24F93FB2B43B7DDC79DFDA734DE5FF00B5E5F909BF1D76EE5CF4DC3AD741450073FF0061F1649F24BAFE9491B70CD6FA43AC8A3B942F70CA1BD0B2B0CF5047147FC23DAA7FD0E7AE7FDF9B2FFE47AE828A00E0F5AF0AD9DEEA9A768725F6B331BE49AE2FE49355B80B3411ED12284571182EF2C60A840BB0C9B7690B5B9FF085683271796B3EA31F510EA77935EC6A7FBC1267650DD46E0338246704D1E18FF898FDABC48FCFF6A6CFB2FB59A67C9FFBEB73CBC80C3CEDA7EE0AE828039FFF008413C1FF00F42A687FF82E87FF0089AB9A6F86B41D1AE1AE34BD134DB19D90A3496B6A91315C838254038C8071EC2B528A0028A28A0028A28A0028A28A0028A2B2F5AD4A6B3486D6C1639753BB709046E090ABB94492B0047C91AB6E3CAE4ED40C19D68029DA7FC4CFC697B79F763D2223A7A0E85A494453CA4FAA8516E14F073E66411B4D74154F4BD361D26C16D21691C07791E49082D248EE5DDCE001966666C0000CE000302AE5001451450014514500145145001451450014514500145145001451450014514500145145001451450014514500145145001451450014514500145145001451450014514500145145001451597A96BB6DA7DC2D946925EEA6E8244D3ED590CEC9920B90CCA15060FCCC40CE1412C40201A95873F881AE2E25B4D06D63D52E21731CF20B858EDED9C1E524900621F83F2A2B9076EE0A181A8FFB12F359FDE78866C4278FECBB49C9B6C0E3F78FB51E6C8DD956C4643005095DC7720821B5B78ADEDE28E18224091C71A8554503000038000E3140187FF08C7F68FCFE24BCFED6FF00A74F2BCAB31FF6C72DBFA29FDEB4986195DBD2BA0A28A0028A28A0028A28A0028A28A0028A28A0028A28A0028A28A0028A28A0028A28A0028A28A0028A28A0028A28A0028A28A0028A28A0028A28A0028A28A0028A28A0028A28A0028A28A0028A28A0028A28A0028A28A0028A28A0028A28A0028A28A0028A28A0028A28A0028A28A0028A28A0028A28A0028A28A0028A28A0028A28A0028A28A0028A28A0028A28A0028A28A0028A28A0028A28A0028A28A0028A28A0028A28A0028A28A0028A28A0028A28A0028A28A0028A28A0028A28A0028A28A0028A28A0028A28A0028A28A0028A28A0028A28A0028A28A0028A28A0028A28A0028A28A0028A2B1EFB5877BC934BD253CFD44604923C4C60B50403BA461805802A4441B7B6E5FBAA4BA8058D4F59B3D2BCA8E67F32EEE322D6CE320CD72C31908A48CE32324E15472C5541229C1A45CEA37115F6BDE5F9B138682C6DAE1DEDE12A7219B217CE7DC0302EB842ABB4020BB57173A6F86EE1E092EAEF55D72E9159902AC97732024265502AC7103950C42461989621998993ECBAEEB5CDF5C7F6458B7FCBADA386B9917D249BA4790482B182CA402B350049AAEA935CBDC68DA33C9FDA8C9B1AE4445A2B2DCB9DEEC46C2EA0AB08B3B9B72640425C6A5858DBE99A75B5859C7E5DADAC490C29B89DA8A005193C9C003AD163636FA759C76B6B1F970A648058B1249259998E4B312492C4924924924D58A0028A28A0028A28A0028A28A0028A28A0028A28A0028A28A0028A28A0028A28A0028A2A39E786D6DE5B8B8963860890BC9248C155140C9249E00039CD004959FA9EB16FA5F951BA4F71753E7C8B5B688C92484633C0E1572541762A8A597730C8ACFF00B5EA3E21FF00905CFF0061D25B95D490AB4D3E3BC28E8C9E5938C48D9DC01DAB86492B434CD134ED1FCD6B2B7DB34D8F3AE24769669B19DBE64AE4BBE01206E270381814019FF65D775AE6FAE3FB22C5BFE5D6D1C35CC8BE924DD23C824158C16520159AB434CD0F4BD1BCD3A758416F24D833CCA9FBC9C8CE1A473F348DC93B989249249C935A1450014514500145145001451450015CFF0089FF00E263F65F0DA73FDA9BFED5ED6698F3BFEFADC9170430F3B70FB86BA0AE7F41FF0089B6A379E217F9A197FD174FCF205BA1E641D7FD6BE5B729C3C6B01EA2803A0A28A2800A28A2800A28A2800A28A2800A28AC7BED73179269BA44706A1AAC7832C067F2D2DD4804199C2B14C82368DA59B3C0DA199402E6A5A943A65BAC922C92C92388E0B7880324F210484404819C024924000166214122BE89A6CD676FF69D41A39757B944379321257701F723C80444A4B051EE49CB333134DD156CEE1AFEEA792F353910AC970E5B6A82412B1464911270BC2F2762962EC3756A5001451450014514500145154ECF56D3750B8BAB7B2D42D2E67B47D9731C332BB42D9230E01CA9CA9183E87D2802E514514005145140051451400514514005145140051451400514514005145140051451400514514005145140051451400514514005145140051451400553D4B55B1D22DD67BFB98E047711C61B9695C8242228E5DCE0E154127B03597FDB779ACFEEFC3D0E213CFF006A5DC04DB6073FBB4DC8F3646DC32E2321890E4AED3734DD0E1B0B86BC96EAEEFEFD90A35D5DC81982E470AAA02460E172115776D52D92334014FF00E271AF7FD42F4893FDF1797087FEF9FB3E71FED3ED6FF964E38D4D374AB1D22DDA0B0B68E04773248579695C800BBB1E5DCE06598927B935728A0028A28A0028A28A0028A28A0028A28A0028A28A0028A28A0028A28A0028A28A0028A28A0028A28A0028A28A0028A28A0028A28A0028A28A0028A28A0028A28A0028A28A0028A28A0028A28A0028A28A0028A28A0028A28A0028A28A0028A28A0028A28A0028A28A0028A28A0028A28A0028A28A0028A28A0028A28A0028A28A0028A28A0028A28A0028A28A0028A28A0028A28A0028A28A0028A28A0028A28A0028A28A0028A28A0028A28A0028A28A0028A28A0028A28A0028A28A0028A28A0028A28A0028A28A0028A28A0028A28A0028A28A0028A28A0028A28A0028A28A0028A28A0028A28A0028ACBD4BC43A669570B6B713C8F76C82416B6B049713ECC91BCC71AB384C8C6EC633819C915CFDE6A5E28D6359B5D2AC963D0A374FB45CC8E12E6EA284301820168A277604267CC0CA246F95A3DA402E7887C536F6978348B7BCF2AEDF89648A233CB10C02562894334B36D60DB42B04521DC6DDAB2163A3EA971671DB3BFF00626983245A5A4BE65D4A09258CB7073B59B736FD997DDF3098E6B5345F0E691E1E49974BB18E079DCBCF3125E59D8B336649189773966E589C66B528029E9BA558E916ED0585B47023B99242BCB4AE4005DD8F2EE7032CC493DC9AB945140051451400514514005145140051451400514514005145140051451400514514005145473CF0DADBCB71712C70C112179249182AA28192493C000739A00AFA96A50E996EB248B24B248E2382DE200C93C841211012067009249000059885048CF834DBBD52E22BFD61A48E3471241A58286388A9CA3C84025E5079C0631A9DB80CC824268F04DA95C43E21BF8A482792DCA5A5A3A956B585CAB1120EA656288581E176855E8CEFB9400514514005145140051451400514514005145140187E259E692DE0D1ACE5922BCD51CC224898ABC108199A60472A557E556C10247881E0D6C410436B6F15BDBC51C304481238E350AA8A06000070001C62B0F41FF89B6A379E217F9A197FD174FCF205BA1E641D7FD6BE5B729C3C6B01EA2BA0A0028A28A0028A2B3F58D4FF00B2ECD1E387ED1753CA96F6D006DA64918E0738242819762012A88CD838C5006851587A6EBB773EB2DA3EA1A4C96B7896E6E1E582749EDF6EE0AA037CAE09C9C6F8D72524C6E0B9AD89E786D6DE5B8B8963860890BC9248C155140C9249E00039CD004955EFAFECF4CB392F2FEEE0B4B58F1BE69E411A2E48032C7819240FC6B1FF00B7AF356F93C3D65E6C278FED2BB063B603FBD18FBF3F05586D0B1B8CE2506AC58E8091DE47A8EA73FF00696A71E7CAB89A25516C0821960503F76A7279CB39180CEDB46002BF9BACEBDCDAB4FA25876965811AEE71D4322B165897A71221739605232A09D8B1B1B7D3ACE3B5B58FCB8532402C5892492CCCC725989249624924924926AC51400514514005155EFAFECF4CB392F2FEEE0B4B58F1BE69E411A2E48032C7819240FC6B1FFB6B54D4BE4D1F469E28DBEEDF6A6BE44607424424F9CCC0FF00032C618038700A9201D0561CFE2BD345C4B67A719357BF89CA496BA76D91A26079123121223C36048CB9DA40C918A8FF00E11A7D43E7F106A53EA0C7836D016B6B403A15312B1322B00372CAD203CE02862B5B90410DADBC56F6F1470C112048E38D42AA28180001C000718A00C3FEC7BFD6B9F113C0B6BFC3A6D94B27964F43E7487699D48FE028A98660CAF85611F8460867B7975B8628E282FD234B18E350AB1D8C60880003030C19E5C10197CED87EE0A93C43FF001389478662FBB7717997EFD4476BB82B21C721A51BD17EEF02460731E0F4140051451400514514005145140051451400514514005145140051451400514514005145140051451400514514005145140051451400514561CFAEB5E5C4B61A0A477B711B98A7BA0CAD6F64E0E08930C0B38C1FDDA739DA18C6183500686A5A943A65BAC922C92C92388E0B7880324F210484404819C0249240001662141232FF00B26E35FF00DE7886180D81F9A2D21A30EA0F6370D92B2B0EA14008AC4FFAC2A8E2E69BA15B69F70D7B23C97BA9BA18DF50BA5433B2641080AAA854181F2A80339620B124EA50014514500145145001451450014514500145145001451450014514500145145001451450014514500145145001451450014514500145145001451450014514500145145001451450014514500145145001451450014514500145145001451450014514500145145001451450014514500145145001451450014514500145145001451450014514500145145001451450014514500145145001451450014514500145145001451450014514500145145001451450014514500145145001451450014514500145145001451450014514500145145001451450014514500145145001451450014553D4B56D3746B75B8D5350B4B1819C22C97532C4A5B04E0162067009C7B1ACBFF84B6DEE78D2B4CD57546EA0DBDA98A364FEFA4D318E2753C636B9C839008C9001D0515CFF00D9FC53A8F1737B63A3C27864B006EA6E390CB2CAAA8B93C1530B700E1B246D3FE10ED2EE7E6D63CFD6E43CB7F69BF9B193D98400085180E372A038CE492CC4801FF09659DF7EEFC3C9FDB731E3CCB4706DA33FF4D27FB8304AE5577480302108A3FB2B58D5F9D66FFEC76E7FE5C74A99D33FEFDC61646E4061B0458C956DE2BA0ACFD4F53FB0F956F6F0FDA750B8C8B7B60DB77631B9D9B07646B91B9B071900066655600CF74B7D07C9D1B41B3823BFBEF3270CE095C2EC592E266CEE9586E8C1C9DEECC3240DCEBA9A6E9B0E996ED1C6D24B248E649EE2520C93C84005DC800670000000000154050008F4CD33EC3E6DC5C4DF69D42E306E2E4AEDDD8CED455C9D91AE4ED5C9C6492599999B42800A28A2800A28A2800A28A2800A28AC39FC5FA1C3712DBC77725ECF0B949E3D3ADE5BC68181C62410AB18CE41003633838E87001B94567E8FAB26B166F30B69ED668A57867B5B9DA2585D4F460ACC391B58609CAB291C115A1400514563DF7892CED6F24B0B58E7D4B528F1BECEC543BC79008F318909165791E632EEC1DB93C5006C5155EC6FADF51B38EEAD64F3217C804A952082432B29C1560410548041041008AB1400514514005145140051451400573F0FFC549A8DBDE8F9B44B6CBDB86FBB793654A4C0778D30761390ECDBC0012376356FF89FDE4BE1E8FE6B03132EAB2A750AC1716E0F6691589246595074532238E82800A28A2800A28A2800A28A2800A28A2800A28A2800AC3F12CF3496F068D672C915E6A8E611244C55E0840CCD3023952ABF2AB608123C40F06B6279E1B5B796E2E258E182242F249230554503249278000E7358FA14135E3BEBD7F1491DC5DA0FB35BCCA43D95B9553E5107A3B30DEFC6724292C2353401B10410DADBC56F6F1470C112048E38D42AA28180001C000718A928A2800A28AA7A96A50E996EB248B24B248E2382DE200C93C84121101206700924900005988504800352D4A1D32DD64916496491C4705BC40192790824220240CE0124920000B310A0915F4DD3664B86D4B5268E5D4A442988C931DB46483E547900E320167201720120008898F1EA50D9EA933DC2C9ABF890A6C7B2D380905946D86116E62A91020062F2143295E06151129DC5AEB1E26D723D3B53B8822B0B6FDEDEDAE9EEED1AB7CA56DE591B6F9DE6292C576055424323178A45008FC3BE225D59F51D4B438A3D52F351B8DF113232C16F6F1A844492E02B0524032F938DEAD7182806E7AE820D0269EE22BBD6F5193509E270F1C11A982D6360720888312C7215B32349B597726CE95B10410DADBC56F6F1470C112048E38D42AA28180001C000718A92800A28A2800A28AC39F5F9A7B896D344D3A4D42789CA493C8C60B58D81C10652A4B1C865C46B26D65DAFB3AD006C4F3C36B6F2DC5C4B1C304485E492460AA8A064924F0001CE6B0FFB7AF356F93C3D65E6C278FED2BB063B603FBD18FBF3F05586D0B1B8CE2506A483C350C97115E6B37126AD791B892333802081C1C831423E552A73B5CEE900382E456E50063D8F86ECED6F23BFBA927D4B528F3B2F2F983BC79041F2D4009165783E5AAEEC0DD93CD6C514500154F55D4A1D234BB8BF9D64748532238802F2B74544048DCECC42A8EE481DEAE573F6BFF13FD73EDE79D374C95E3B3FE12F74BE6433391D76A82D1AE7192642411E5B5005CD174D9ACD26BABF68E5D4EEDCBCF221242AEE631C4A481F246ADB470B93B9CA8676AD4A28A0028A28A0028A28A0028A28A0028A28A0028A28A0028A28A0028A28A0028A28A0028A28A0028A28A0028A28A0028A28A002ABDF5FD9E996725E5FDDC1696B1E37CD3C82345C900658F032481F8D53D4B5B86CEE174FB6F2EEB579503C362B2056DA491E63F5291020E5C83D3003315531D8E8EEF791EA9AB3F9FA88C98E34958C16A08236C6A700B0058194AEF6DCDF75484500AFB354F1072EF3E93A51E5044DB6EEE54F1870573029193853E67CCA73132953B90410DADBC56F6F1470C112048E38D42AA28180001C000718A928A0028A28A0028A28A0028A28A0028A28A0028A28A0028A28A0028A28A0028A28A0028A28A0028A28A0028A28A0028A28A0028A28A0028A28A0028A28A0028A28A0028A28A0028A28A0028A28A0028A28A0028A28A0028A28A0028A28A0028A28A0028A28A0028A28A0028A28A0028A28A0028A28A0028A28A0028A28A0028A28A0028A28A0028A28A0028A28A0028A28A0028A28A0028A28A0028A28A0028A28A0028A28A0028A28A0028A28A0028A28A0028A28A0028A28A0028A28A0028A28A0028A28A0028A28A0028A28A0028A28A0028A28A0028A28A0028A8E79E1B5B796E2E258E182242F249230554503249278000E73587FF09A6853FCBA65DFF6C4C7811E94A6EB0C7EE8764CA45B8F43232AF04E7009001D0515CFFDA3C53A8F36D6563A3C27957BF26EA6E382AD144CA8B93C8613370065724ED3FE11AB8BBE755F10EAB74A7E630DBCA2CE347F5430859768E405691F83CEE2035006A6A5AB69BA35BADC6A9A85A58C0CE1164BA996252D82700B1033804E3D8D65FF00C261A75C7CBA4C17DABB37FAB6B0B666865F5DB70DB60E39CE641C82BF7B8AB9A6F86F45D22E1AEAC34BB482EDD0A4974B1033CA0904EF90FCEE490092C492793935A9401CFF00DAFC537DFF001EDA5D8E970BFDD96FEE0CF3478EBBA08BE439230313F0082790528FF847F51BCE756F11DF4AAFFEB2DAC156CE138E9B59733AF404E26E4E7F84EDAE828A00CBD37C37A2E9170D7561A5DA4176E8524BA58819E5048277C87E7724804962493C9C9AD4A28A0028A28A00A7AAEA50E91A5DC5FCEB23A429911C401795BA2A20246E766215477240EF55F4DD3664B86D4B5268E5D4A442988C931DB46483E547900E320167201720120008894F4DFF008A8351875D7E2C6DFCD8F4F8FA8932769B93DBE60A44657FE59BB1C9F376A74140051451400572F69AA3EA7F12B51B282F7FD1746D3E28E7B65461BA7B86DE0B127076A44B8C03FEB5B9EA2B7352D4A1D32DD64916496491C4705BC40192790824220240CE0124920000B310A091E7FE17D52D34DF12EBCECF26A5AB3A5BDB5F269D13DC16BC532C8E378188E21E7AC31B4CCB81032920464D007A6557BEBFB3D32CE4BCBFBB82D2D63C6F9A790468B9200CB1E064903F1AC7FF008AA752FF009F1D16DDBEB777254FFDF31C5228FF00AECB93DC2FCD62C7C33A759DE477D209EF6FD3256EEFA6699D18821CC618ED8B7679118453C0C6000002BFFC24B7177C695E1ED56E94FCA26B88859C68FE8E262B2ED1C12CB1BF078DC415A3EC9E29BEFF008F9D52C74B85FEF45616E679A3C74DB3CBF21C919398380481C80F47FC24FF00DA3F2786ECFF00B5BFE9EFCDF2ACC7FDB6C36FE8C3F74B261861B6F5A3FB0B54BEFF0090C7882778FA35BE991FD8A371D412C19A60D9EEB2A8200046376E00CFD4F4BF0D69DE57FC24DAB4F7F35C642C1A8DE33ADE15C6156D131148C32B854889DDB4E0B1C9B905F6AF3DBC56DA1F87A3D3ECE3411C536A2E21558F18468E08F731000C98DFC92381C1CED931E1AF07711C105ADD5EF256085A5BBBD29D58850D2CCC371666F988C9627A9A3CEF12EABF2C76B06896AFD659E45B8BB03A10235CC48DD4ABEF900C0CA1C90A005A5AA7873FB475AD735A8035D7946E64655B5B48CAFC8ACAA4921981452CEEC4ED500801541FF091DC5FF1A0E913DFC6DF72F67716D684F5E1CE647523055E38DD1B230D8C9162C7C33A759DE477D209EF6FD3256EEFA6699D18821CC618ED8B7679118453C0C60003628039FF00EC0BFD439D735B9E651F761D33CCB08C1ECC4A48652DC918F336118F9323756C58D859E99671D9D85A41696B1E76430462345C924E147032493F8D58A28039FF000E7C9AAF8A205F9618B551E5C63854DF6B6F236076CBBBB1F56663D49AE82B9FF0CFEF6FBC477A9CDBDCEAADE53FF7BCA86181F8EA3124322F3D76E464104F414005145140051451400565EBBA94DA7D924764B1BEA778E6DEC23941D8D36C6605C82308AA8CCDCE70A42E5880752B9FD2BFE26FE21BDD64F36F69E669B65EFB5C7DA1FB1E64458F0471F67DCA70F401A9A6E9B0E996ED1C6D24B248E649EE2520C93C84005DC80067000000000015405000B945140051451400514514005145140051451400514565EBBA94DA7D924764B1BEA778E6DEC23941D8D36C6605C82308AA8CCDCE70A42E58804029DFFF00C4F75C8B4B4FF8F3D3A586EEF5FA89241978A1047465658E56E41004630CB21C74154F4BD361D26C16D21691C07791E49082D248EE5DDCE001966666C0000CE000302AE50014565EA5E23D2349B85B5BBBE8C5E3A078ECE2065B8917246521405D87072429C0527A03587A9EB9E22B9F2AD2C74DFECCB8B9CAC497724725CBE31B982C65E38E350CA7CE767C1017CA72C8180353C4BE27B4F0DDBC01D24B8BFBB731D9D9C28EF24CC0658ED4566D8A3E6660A703A0248538FA6F86352D42E1B50D75E3B79E642B2B5ABB25DBAE432C66746061881C7EE6227E6404CB26E7DDB1A0786A1D1916E2EAE24D4B58742B71A9DC806593736E28BFF3CE2DDC88D7E51EE724E86ABA943A4697717F3AC8E90A64471005E56E8A88091B9D988551DC903BD0053B896CFC35A75A58E97A7411B4F29B7B1B2814431B4843487240C2280AEEC704E01C066214D8D1B4CFECAD3921926FB4DDBE24BBBB2BB5AE66C00D211938CE000B9C2A85518550047A6E9B325C36A5A93472EA52214C46498EDA3241F2A3C8071900B3900B9009000444D4A0028A2B9FFF0084AEDEFBF77A0DB4FABC8DF726814ADA63A6E3704796CA0E0308CBB8E7084A90003A0AC7BEF1259DADE49616B1CFA96A51E37D9D8A8778F2011E6312122CAF23CC65DD83B7278AAFFD8FAC6A7FF219D5BCAB73CFD8F4ADF6FD79DAF3EEF31B69C6193CACE0EE520ED1B1636167A659C767616905A5AC79D90C1188D172493851C0C924FE34018FFD8F7FAD73E227816D7F874DB2964F2C9E87CE90ED33A91FC051530CC195F0AC3720821B5B78ADEDE28E18224091C71A8554503000038000E31525140051451400514514018FAF5F5C47F63D374F9365FDFCBB11C28630C4BCCB2E0E7185F9549057CC78830C35685858DBE99A75B5859C7E5DADAC490C29B89DA8A005193C9C003AD63E83FF00136D46F3C42FF3432FFA2E9F9E40B743CC83AFFAD7CB6E53878D603D457414005145140051451400514514005145140051451400514514005145140051451400514514005145140051451400514557BEBEB7D3ACE4BABA93CB8530090A5892480AAAA325989200500924800126802C573FFDAD71AFFEEFC3D3406C0FCB2EAEB207507B8B75C15958742C4845623FD6157407D8EE3C49FBDBE33DB690DC269EC815AED3FBD7008DCAA78C440AFCB91267798D3A0A00A7A6E9769A4DBB43689200EE5E47965796491B0065DDC96638000249C0503A002AE514500145145001451450014514500145145001451450014514500145145001451450014514500145145001451450014514500145145001451450014514500145145001451450014514500145145001451450014514500145145001451450014514500145145001451450014514500145145001451450014514500145145001451450014514500145145001451450014514500145145001451450014514500145145001451450014514500145145001451450014514500145145001451450014514500145145001451450014514500151CE266B7956DE48E39CA111BC885D55B1C12A082467B6467D4549450073FF0063F187FD07743FFC134DFF00C9547D8FC61FF41DD0FF00F04D37FF00255741450073FF0063F187FD07743FFC134DFF00C9547F636BD73F3DE78AA78241C05D32CA18A323D4899666DDD790C0631C03927A0A28039FFF008466EA5F92F7C51AE5D5B9FBF0EF82DF77A7EF2089241CE0FCAC338C1C82413FE10BD11FE5B94BEBD84FDEB7BFD4AE6EA17F4DD14B232360F2320E08047205741450061C1E0BF0ADADC45716FE1AD1A19E270F1C91D844AC8C0E41042E41079CD6E5145001451450014514500145145001451450015CFDF7FC543A9C9A5273A6D94A17530DD2E18C6196DF1D4AE1E3773F748C261C3385B1AE5F5C0F2F48D364F2F55BE8A4304C5415B745DA1E620F0DB0C8984FE26651C2EE65D0B1B1B7D3ACE3B5B58FCB8532402C5892492CCCC725989249624924924926802C514563DF6BDE5DE49A7E9B653EA37E980E918D90C2480479B29F957AA92A3749B5830461401B15CBEB3E37B3B0DF6FA55ACFAD6A3E69B64B7B223679FCFEEDE52422B0232CA09755CB95DAA48927D266BBB796EFC53A9C7F638D0C925940E61B48D00C9F35C90D300372B6E2B1B2F26206A4D12C3ED5E46A37369F6586DF747A658797E5A5AC2328AFB38C48E9CF2018D5B600A7CC2E019EFE17FB45E5D6B9E2CD57CD8C447FD0D24F2ACED600016466E1A45386DE5C84906DDD1808A16E784A08747F07457777147A70991AFAE2395445F6557F9844E4E30228F64409C6162030A00031FC51AE4DE21B2B6D0B44B591ADF59B8166DA9DCC656D4C5B1E4955464492878A3601930843E5650471D041E1A864B88AF359B89356BC8DC49199C01040E0E418A11F2A9539DAE774801C17228023FEDEBCD5BE4F0F5979B09E3FB4AEC18ED80FEF463EFCFC1561B42C6E338941A3FE113B3BEFDE7885FF00B6E63CF977680DB467FE99C1F706096C336E900620B91524FE2BD345C4B67A719357BF89CA496BA76D91A26079123121223C36048CB9DA40C918A8FF00B3FC41A9F1A9EA3069F6ADF7AD74C0C64C742A6E5F04A9193948E37048C3FCB9600B9A978874CD2AE16D6E2791EED90482D6D6092E27D9923798E356709918DD8C67033922A9FF00C545ACFF00D402D0FF00D739EF1BFF0042862E47FD35DCADFC0C2B534DD2AC748B7682C2DA3811DCC9215E5A572002EEC7977381966249EE4D5CA00CFD3344D3B47F35ACADF6CD363CEB891DA59A6C676F992B92EF80481B89C0E06056851450014514500145158FE2ABEB8D3FC2FA8CD6527977ED1186C8ED0737321F2E11CF1CC8C839E0679E334015FC13FBCF0858DEF4FED1F3352D9FF3CFED3234FB33DF6F99B73C6719C0CE2BA0AAF61636FA669D6D61671F976B6B12430A6E276A2801464F27000EB562800A28A2800A28A28032FC47A94DA4F87EF2EED5637BC0823B48E404AC970E4242870470D2322E7200CE49039AB1A4E9B0E8DA358E976ED2341656F1DBC6D21058AA28504E0019C0F41597AA7FA4F8D3C3F66FC470C577A8291D4C88238003FECEDBA9091D72179C020F41400514514005145140051451400514567EA7AEE8FA2795FDADAAD8D879D9F2FED770916FC6338DC467191D3D450068515CFF00FC25B6F73C695A66ABAA37506DED4C51B27F7D2698C713A9E31B5CE41C8046483ED7E29BEFF8F6D2EC74B85FEECB7F70679A3C75DD045F21C918189F80413C829401B93CF0DADBCB71712C70C112179249182AA28192493C000739AE6EDEFECED2593C4DE21BB834C5B8FF0044B34BE9042228433329F9F05649400ECBF29016352B98C939E749D47C45AC5EE9BAAEB73DE6930C4D6FAA5ADBC4B6B0C9332C6C91A63330511B6E7CC9825D40246F45E934DF0DE8BA45C35D586976905DBA1492E962067941209DF21F9DC9201258924F27268029FF00C245797DF2E8DA0DF5C678F3EFD0D8C2ADD486120F3BA742B1329240C8F98A9FD93AEEA3CEA7ADFD9613D6D74A8847F29FBC8F33EE76C0E03C6216EA78246DE828A00CB48348F0AE8D777091476967023DD5D48AA599F6AE5E473CB48E42E4B1CB1C739351F876C6E21D3A3BED4E3C6B579146F7C4B06D8F8CF94A46408D0B305009EEC4B333336C57177A352F0BA4761A6EB7A6AC52BC9FD9DA6CDA734D72F96DC628F17118289B801F2AAC681771014B5007597D7D6FA759C9757527970A601214B124901555464B312400A0124900024D67D8D8DC5EDE47AAEAB1F973264DA59960C2D01041662321A620905864282514905DE4C383C33E26B9BF8B53D5759D19AFB60556874A666B20536BADB3BCA4293963BD9096C8DC0AAAA0D4FF00844ADEE79D5753D57546E845C5D18A364FEE3C3088E2753CE7721C838248C000162FBC4FA5D9DE496093FDB3534C674FB3FDECE0900AEE51FEAD4E57E77DA8372E5866ABFDA3C53A8F36D6563A3C27957BF26EA6E382AD144CA8B93C8613370065724EDD8B1B0B3D32CE3B3B0B482D2D63CEC8608C468B9249C28E064927F1AB1401CFFF00C21DA5DCFCDAC79FADC8796FED37F36327B3080010A301C6E540719C925989E828A2800A28A2800A28A2800A28A2800AC3F16CF32787E5B4B49648AF35075B1B792262248DA53B4CA80724C6A5E5206388C9C80091B95CFCDFF135F1A5B46BF3DAE8F134D29EC2EA51B63008FE2588CDB94E30278CE0E41500DC82086D6DE2B7B78A38608902471C6A155140C0000E00038C54945140051451400514514005145140051451400514514005145140051451400514514005145140051451400514561CFABDCEA3712D8E83E5F9B13959EFAE6DDDEDE12A70557057CE7DC0A908D842ADB882023005CD4F564D37CA892DA7BCBC9B261B4B6DBE648171B9BE6655555C8CB310325467732835EC749B87BC8F52D5EE7ED176B9686DD31E45992083E5FCA199B69DA647C9E5B688D5D92AC699A359E95E6C90A799777183757920066B9619C1918019C64E00C2A8E142A800685001451450014514500145145001451450014514500145145001451450014514500145145001451450014514500145145001451450014514500145145001451450014514500145145001451450014514500145145001451450014514500145145001451450014514500145145001451450014514500145145001451450014514500145145001451450014514500145145001451450014514500145145001451450014514500145145001451450014514500145145001451450014514500145145001451450014565EA5E25D0746B85B7D535BD36C67640EB1DD5D244C5724640620E3208CFB1AA7FF099E90DCC49AACF19E566B7D1EEE58E41D991D222AEA7A8652411C8245007414573FF00DB9ADCFF00BBB6F095F4531FBAF7F776D1C23D7734524AE38E9846E719C0C907FC56177FF403D2F6FF00D76BFF00333FF7E3663FE059CFF0E3900E82B2F52F11E91A4DC2DADDDF462F1D03C7671032DC48B923290A02EC3839214E0293D01AA7FF0008D5C5DF3AAF88755BA53F3186DE51671A3FAA1842CBB47202B48FC1E77101AB534DD274DD1ADDADF4BD3ED2C60672ED1DAC2B1296C019214019C0033EC2802BE9B7F7DA95C34E6C64B2D3C21118BA5C4F3B120870A0FEED36FF000B8DE4B1CAA6DF9F528A2800A28A2800A28A2800A28A2800A28A2800A28A2800A28A2800A28A2800A28A2800AA7A96A50E996EB248B24B248E2382DE200C93C841211012067009249000059885048B95CFE8DFF13E9D3C413FCF68D87D210F0161641FBE2BDA47CB609E5632A308CD202017344D366B3B7FB4EA0D1CBABDCA21BC99092BB80FB91E40222525828F724E59998DCBEBEB7D3ACE4BABA93CB8530090A5892480AAAA3259892005009248001268BEBEB7D3ACE4BABA93CB8530090A5892480AAAA325989200500924800126B2F4AD29AE5EDF5AD6ADA33AC14DD1C6FB5C69E197062888C8CE090CE3973E8A1114023FB3DFF88BF79732CFA7E90DCC76D11920BB980E8EF22B06894F5F2C00FC2EE6197886C58D859E99671D9D85A41696B1E76430462345C924E147032493F8D58AE1CF892F3C69793695E1A8E787481BE3BCD7D94AA3AE0A1166C08DF26EDC3CCFBA9B3387CAE4024D6F5C8754D67FB1ADED6EF52B3891FED76D67183F699036044CEC420886D977EF640EC9E582E5658EB53FE11E7D63F7BE26682F57F834F8837D923079C3A9389D81C7CEE00F954AA21CE74346D0F4BF0F69C961A45841656AB83B214C6E2001B98F566C019639271C9AD0A00CBD57C3FA7EB3716F7177F6B59EDD1D2292D6F66B660AE54B0262752412887073D0553FF8413C1FFF0042A687FF0082E87FF89AE828A008E0821B5B78ADEDE28E18224091C71A8554503000038000E315251450014514500145145001451450015CFEB7FE99E23F0F6983955965D42647FB8F1C29B0023BB096781C02303CBCE4155CF415CFFF00C7F78FA3961E63D334F960B87EDE64EF13AA023F89560DCC0E081246790DC007414514500145145001451450073F63FBCF1F6B72A7CD1A69F630338E42C81EE5CA13FDE0B246C475C3A9E8457415CFF87BFE439E2CFF00B0AC7FFA456B5D050014515CDA447C537177F69B8BBB5B3B3B87B65B5B6B992DE63229E6495A365600820A203828C1CEE2CA2300E8279E1B5B796E2E258E182242F249230554503249278000E73587FF0009B78764FF008F2D43FB4F1F7FFB2A192FBCBF4DFE42BECCF38DD8CE0E33835241E0EF0E417115D1D1ED27BC89C3ADE5DA7DA2E37039526693739230304B700003000ADCA00E7FFE123BDB9FF9077867559E37E22B8B811DAC64F4CBAC8E264507A9F289C0CA8618C9E4F8B2FBFD6DD695A4C67E468EDE37BC931DDD257F2D55B9E0344E01193B81DA3A0A28039FFF00844E29FE6D4759D72F661C093FB41ED70BE9B6DBCA43CE792A5B9C670001A1A6685A3E89E6FF0064E956361E763CCFB25BA45BF19C676819C64F5F535A145001597ABEA535ABDA58D8AC6FA85EBB243E602C912AAE5E57507251781C632CE8A5977EE16355D4A1D234BB8BF9D64748532238802F2B74544048DCECC42A8EE481DEABE91A6CD6AF777D7CD1BEA17AEAF37964B244AAB8489188C945E4F38CB3BB055DFB40058D2B4D8748D2EDEC20691D214C192520BCADD59DC80373B312CC7B924F7AB94514005155EFAFECF4CB392F2FEEE0B4B58F1BE69E411A2E48032C7819240FC6B1FF00E263E23FF9FED1F4C1FEEC773760FE6D0C654FFB32E4FF00CB2D9F38058BED5AE1EF24D3748B6FB45DAE166B87C791664804799F30666DA7708D32795DC635757AB1A66929A6F9B2BDCCF79793604D7773B7CC902E76AFCAAAAAAB938550064B1C6E6626C58D859E99671D9D85A41696B1E76430462345C924E147032493F8D58A0028A28A0028A28A0028A28A0028A28A0028A28A0028A28A002B9FF07FFA468F36A6FF00F1F1A95DCB75283C3A7CDB12375E8B2471A471BA8E8D1B672724F415873F84B4A9EE259836A50195CBB25A6A9736F1EE272C424722A824924903924939249A00DCA2B9FF00F84374BFF9FAD73FF07B7BFF00C7A8FF00841FC3527379A441A8C9D04DA996BD9147F743CC5982F53B41C64938C934007FC26DE1D93FE3CB50FED3C7DFFECA864BEF2FD37F90AFB33CE37633838CE0D1E66B3AEFEE24B09F45B03C4CF2DC27DAE41DD13CA66545391FBCDFBC61805525641D05473CF0DADBCB71712C70C112179249182AA28192493C000739A0020821B5B78ADEDE28E18224091C71A8554503000038000E3152573FFF0009C786A4E2CF57835193A9874C0D7B228FEF14843305E837118C90339228FF0084B229FE5D3B46D72F661C98FF00B3DED70BEBBAE7CA43CE380C5B9CE3009001D0515CFF00F6CEBD73F259F85678241C96D4EF618A323D0185A66DDD382A0633C83804FB678C3FE805A1FF00E0E66FFE45A00E828AE7FED3E3093E4FECAD0EDF771E77F69CD3797FED797E426FC75DBB973D370EB47D8FC61FF41DD0FF00F04D37FF00255007414573FF0063F187FD07743FFC134DFF00C9547D8FC61FF41DD0FF00F04D37FF00255007414573FF0063F187FD07743FFC134DFF00C9547D8FC61FF41DD0FF00F04D37FF00255007414573FF0063F187FD07743FFC134DFF00C9547D8FC61FF41DD0FF00F04D37FF00255007414573FF0063F187FD07743FFC134DFF00C9547D8FC61FF41DD0FF00F04D37FF00255007414573FF00F1585A7FD00F54DDFF005DAC3CBC7FDFFDF9FF0080E31FC59E0FED7F115AFF00C7EF85FED1BBEE7F655FC736DF5DFE78871DB1B7777CE38C8074155EFAFADF4EB392EAEA4F2E14C024296249202AAA8C9662480140249200049AE5F50F889A5E999B5BD867D3F543B40B5D447931A97DDB0BDC0DD085211DB21D8E11800CE36557B1D77449EF23D5AEAF67D62F573B0E936F737D6768482362189193CC0A482EC048439FBA8CA800363ECF7FE22FDE5CCB3E9FA43731DB446482EE603A3BC8AC1A253D7CB003F0BB9865E21B90410DADBC56F6F1470C112048E38D42AA28180001C000718AC3FF008487529F8B3F0A6AAEAFFEA66B892DE18C83F759C190CA8BD09063DE07F067E5A37F8C2EBE4F2343D331CF9DE74D7DBBFD9F2F6C38F5DDB8F4C6D39C800E828AE7FF00B3BC533FEEEE7C45631427EF3D869663987A6D696595073D728DC67183820FF847B54FFA1CF5CFFBF365FF00C8F401D0515CFF00FC2236B2FCF7BAA6B975707EFCDFDAB3DBEEF4FDDC0C918E303E5519C64E49249FF086E97FF3F5AE7FE0F6F7FF008F5007414573FF00F086E97FF3F5AE7FE0F6F7FF008F51FF00086E97FF003F5AE7FE0F6F7FF8F5007414573FFF00086E97FF003F5AE7FE0F6F7FF8F51FF089C507CDA76B3AE594C78327F683DD657D36DCF9A839C72143718CE090403A0A2B9FFF00847B54FF00A1CF5CFF00BF365FFC8F47F6778A60FDDDB788AC65847DD7BFD2CC931F5DCD14B121E7A61178C672724807414573FE778C2DFF0075F61D0EFB6FFCBC7DB26B4DFF00F6CBCA976E3A7DF6CE33C6700FED9D7ADBE4BCF0ACF3C8790DA65EC32C607A133342DBBAF0148C6392720007414573FF00F0964507CBA8E8DAE594C7911FF67BDD657D775B79A839CF0583719C60824FF84DBC3B1FFC7EEA1FD999FB9FDAB0C963E67AECF3D537E38CEDCE32338C8A00E828A8E09E1BAB78AE2DE58E682540F1C91B0657523208238208E73525001451450014514500145145001451450014514500145145001451450014514500145145001451450014514500145145001451450014514500145145001451450014514500145145001451450014514500145145001451450014514500145145001451450014514500145145001451450014514500145145001451450014514500145154F52D5B4DD1ADD6E354D42D2C606708B25D4CB1296C138058819C0271EC6802E515CFFF00C277E0FF00FA1AF43FFC18C3FF00C551FF00099697FF003EBAE7FE08AF7FF8CD007414573FFF000944B3FCDA778735CBD847064F212D70DE9B6E5E273C6390A579C6720807F6CEBD73F259F85678241C96D4EF618A323D0185A66DDD382A0633C8380403A0A2B9FF00B678C3FE805A1FFE0E66FF00E45A3EC7E30FFA0EE87FF8269BFF0092A803A0A2B9FF00EC3D6E7FDE5CF8B6FA298FDE4B0B4B68E11E9B5658E571C75CBB739C606003FE1138A7F9B51D675CBD987024FED07B5C2FA6DB6F290F39E4A96E719C0000074159FA9EBBA3E89E57F6B6AB6361E767CBFB5DC245BF18CE37119C6474F5159FFF00083F86A4E6F348835193A09B532D7B228FEE8798B305EA76838C9271926B434CD0B47D13CDFEC9D2AC6C3CEC799F64B748B7E338CED0338C9EBEA68033FF00E138F0D49C59EAF06A327530E981AF6451FDE2908660BD06E231920672451FF0975ACBF259697AE5D5C1FB90FF00654F6FBBD7F793AA4638C9F9986718192403D051401CFF00F6FEAF27C91783F55491B856B8B8B458D4F62E526660BEA5558E3A0278A3ED9E30FF00A01687FF008399BFF916BA0A28039FFB378C24F9FF00B5743B7DDCF93FD9934DE5FF00B3E679E9BF1D376D5CF5DA3A51FD87ADCFFBCB9F16DF4531FBC961696D1C23D36ACB1CAE38EB976E738C0C01D051401CFF00FC22BF68FF009096BDAE5F6DFF0057FE99F64D9EBFF1EC22DD9E3EF6EC638C64E4FF00842B41938BCB59F518FA88753BC9AF6353FDE0933B286EA37019C1233826BA0A28029E9BA4E9BA35BB5BE97A7DA58C0CE5DA3B5856252D803242803380067D855CA28A0028A28A0028A28A0028A28A0028A28A0028A28A0028A28A0028A28A0028A28A0028A28A0028A28A0028A2B1F58BEB8795349D2E4DBA8CDB1A4902822D602D8694E7203101C46083B9C7DD2AAE5402BDF7FC543A9C9A5273A6D94A17530DD2E18C6196DF1D4AE1E3773F748C261C3385E82ABD8D8DBE9D671DADAC7E5C29920162C492496666392CC4924B124924924935628039FB0FF89EEB92EA8FFF001E7A74B35A5927512483092CC41E8CACB244BC020090E596418B1E21F13E97E19B3136A13FEFA5E2DAD22F9E7BA7C801228FABB12CA38E991920735CDCFA7092E25B0F01EA3269C62731DE35A4B1B595A3038DA2278E451283963147E5E724C8CA5909EA34DD02C74DB86BC0B25CEA0E8524BEBA6F327652412A18FDC42C3779681501270A280393FEC6D5BE20FEFF00C4097DA4E80DCA68CE220F72BFC266E1997E524919565623011A11249DE410436B6F15BDBC51C304481238E350AA8A06000070001C62A4A2800A28A2800A28A2800A28A2800A28A2800A28A2800A28A28031EFB55BC7BC934ED2EC2792E8603DDCF094B6B7C8043124A9978CFCB1E791B59A3CEE1734DD361D32DDA38DA496491CC93DC4A4192790800BB9000CE00000000002A80A001728A0028A28A0028A28A0028A28A00E7FC3DFF21CF167FD8563FF00D22B5AE82B9FD3BF71E39D7ADA3F96196D2CEF5D7AE6673344CD9F7482218E9F2E719249E82800AE7FFE40DE2FF4B4D73FF1DBC8E3FC49DF0C7ECABF67FEF495D0567EB7A67F6C68F3D92CDE44CDB64827DBBBC999183C726DC8DDB5D55B69E0E307826803428ACFD1F53FED4B377921FB3DD412BDBDCC05B718E45383CE012A461D4900B23AB606715A140051451400514561DFCF36ABAA7F6359CB2476F1A16D46E60621E3FBA5205618DAEEAC58B03B9140FBA64470011D8FFC543A9C7AABF3A6D94A5B4C2BD2E18C655AE33D4AE1E4441F748CBE5C3215E82A382086D6DE2B7B78A38608902471C6A155140C0000E00038C567EA5AFD8E9B70B665A4B9D41D03C7636ABE64ECA49018A8FB88586DF31CAA0246585006A563DF6BE91DE49A769907F696A71E3CDB78655516C08055A7627F76A7238C339192A8DB4E2BFD975DD6B9BEB8FEC8B16FF975B470D7322FA493748F2090563059480566AD8B1B1B7D3ACE3B5B58FCB8532402C5892492CCCC7259892496249249249268033EC74471791EA3AB5DFDBB514CF96C8AD14108C11FBB84BB056C16CB92CE77B0DC14851B14514005145140051451400514514005145140051451400514514005151CF3C36B6F2DC5C4B1C304485E492460AA8A064924F0001CE6B0FF00E1294BEE3C3F633EB0A78175032A5A293D0999880EB9C8631090AED208CE0100E82A39E786D6DE5B8B8963860890BC9248C155140C9249E00039CD61FD87C4BA8FCD79ABC1A5467910E9912CB2291C60CD3295753C9C085483819201DD241E10D0E1B88AE24B492F6785C3C126A3712DE340C0E73199998C6720125719C0CF418008FF00E12EB3BAE346B3BED6BBF996110F2597B959E46485F078215CB673C7CAD837F8B2FBE78934AD223EAAB70AF7B2383D03846896361DC2B48093C1C0CB741450073FFF0008BCB3FCBA8F88F5CBD847223F3D2D70DEBBAD92273C6782C579CE32011241E0EF0E417115D1D1ED27BC89C3ADE5DA7DA2E37039526693739230304B700003000ADCA2800A28A2800A28A2800A28A2800A28A2800A2A9EA5AB69BA35BADC6A9A85A58C0CE1164BA996252D82700B1033804E3D8D65FF00C255F68FF906E83AE5F6DFF59FE87F64D9E9FF001F262DD9E7EEEEC639C646403A0A2B9FFF008AC2EFFE807A5EDFFAED7FE667FEFC6CC7FC0B39FE1C727FC22BF68FF9096BDAE5F6DFF57FE99F64D9EBFF001EC22DD9E3EF6EC638C64E4034353D7747D13CAFED6D56C6C3CECF97F6BB848B7E319C6E2338C8E9EA2B3FFE12AFB47FC83741D72FB6FF00ACFF0043FB26CF4FF8F9316ECF3F777631CE3233A1A6685A3E89E6FF0064E956361E763CCFB25BA45BF19C676819C64F5F53526A5AA5A6936EB35DBC803B848D2289E59246C1384440598E01240070149E809A00CBFF008AC2EFFE807A5EDFFAED7FE667FEFC6CC7FC0B39FE1C73CFFF0067EA9E28F92DFC41AACD60FCCB7C8FF618D18F3BAD163459255C676B3C8D1E1D1B331520741FD9B79E20F9F5D87ECD623FD5E9B15C13E603DEE0AE037076984168FEF64C995D9D050063E8FE15D0B4195E7D374C821BA937F9974C0C93CBB9B7B6F95B2EF96E7E627A0F4151CFE15B13712DD69F35DE937723991A5B09762B3B1F99DA13989DCE7059D18F4E72148DCA28030E0D4B52D3AE22B6D796D0C73388E1D42D43246F213C2491B12622490AA77B862319566446DCA8E7821BAB796DEE228E6825429247228657523041078208E3158FE179E6FB15CE97732C93DC693706C9E7918B34CA11248DD98F2CE62923DE703E7DF818C5006E51451400514514005145140051451400514514005145140051451401873F8474592E25BCB5B28F4ED42473235F58288276727396651FBC1BB0C55C32B103729C51A3DE5F26B37DA2DFDC4776F696F05C4774B1796CE9234A815D4120B8F2492CBB41DFC22E39DCAE7ECFFE4A1EB3FF0060AB0FFD1B77401D0514514005145140051451400514514005145140051451400514514005145140051451400514514005145140051451400514514005145140051451400514553D4B56D3746B75B8D5350B4B1819C22C97532C4A5B04E0162067009C7B1A00B94573FF00F09DF83FFE86BD0FFF000630FF00F1547FC277E0FF00FA1AF43FFC18C3FF00C55007414573FF00F09DF83FFE86BD0FFF000630FF00F1547FC271E1A938B3D5E0D464EA61D3035EC8A3FBC5210CC17A0DC463240CE48A00E828AE7FFE132D2FFE7D75CFFC115EFF00F19A3FE132D2FF00E7D75CFF00C115EFFF0019A00E828AE7FF00E12AFB47FC83741D72FB6FFACFF43FB26CF4FF008F9316ECF3F777631CE32327FC243AA7FD099AE7FDFEB2FF00E48A00E828AE7FFE121D53FE84CD73FEFF00597FF2451FDA9E25B9F9ECFC3504118E0AEA7A92C5213EA042932EDE9C960739E00C1201D0515CFF00DB3C61FF00402D0FFF0007337FF22D1F6CF187FD00B43FFC1CCDFF00C8B401D0515CFF00D9BC6127CFFDABA1DBEEE7C9FECC9A6F2FFD9F33CF4DF8E9BB6AE7AED1D28FB1F8C3FE83BA1FFE09A6FF00E4AA00E828AE7FFB2FC4B73F25E789608231C86D334D58A427D0999E65DBD780A0E71C81904FF847B54FFA1CF5CFFBF365FF00C8F401D0515CFF00FC23DAA7FD0E7AE7FDF9B2FF00E47A3FE10FB07F9A7BFD72598F2F27F6CDD47BDBB9DB1C8A8B93D9555474000E2803A0A2B9FF00F84374BFF9FAD73FF07B7BFF00C7A8FF00841FC3527379A441A8C9D04DA996BD9147F743CC5982F53B41C64938C93401D0561CFE34F0ADADC4B6F71E25D1A19E272924725FC4AC8C0E0820B641078C547FF082783FFE854D0FFF0005D0FF00F135B90410DADBC56F6F1470C112048E38D42AA28180001C000718A00C3FF84EFC1FFF00435E87FF0083187FF8AA3FE132D2FF00E7D75CFF00C115EFFF0019AE828A00E7FF00E130B07F960B0D72598F091FF635D47BDBB0DD246A8B93DD995475240E68FF00848754FF00A1335CFF00BFD65FFC915D051401CFFF006CEBD73F259F85678241C96D4EF618A323D0185A66DDD382A0633C83804FB678C3FE805A1FFE0E66FF00E45AE828A00E7FC9F185C7EF7EDDA1D8EEFF00977FB1CD77B3FEDAF9B16ECF5FB8B8CE39C649FD97E25B9F92F3C4B04118E43699A6AC5213E84CCF32EDEBC050738E40C83D051401CFFF00C2397EFF002CFE2DD72584F0F1E2D63DEBDC6E8E0575C8EEACAC3A820F347FC21BA5FF00CFD6B9FF0083DBDFFE3D5D051401CFFF00C20DE167F9AE741B1BD98FDEB8BF885D4CFE9BA59773B6070324E000070055CD37C35A0E8D70D71A5E89A6D8CEC851A4B5B54898AE41C12A01C64038F615A9450014514500145145001451450014514500145145001451450014514500145145001451450014514500145145001451450014514500145145001451450014514500145145001451450014514500145151CF3C36B6F2DC5C4B1C304485E492460AA8A064924F0001CE68029EA7A9FD87CAB7B787ED3A85C645BDB06DBBB18DCECD83B235C8DCD838C8003332AB1A3699FD95A724324DF69BB7C497776576B5CCD801A4232719C001738550AA30AA00A7A1C135DDC5CEB77D14893DC3B25A473290D6F6A080A307054C8544AC080C372A367CB5C6A5F5FD9E996725E5FDDC1696B1E37CD3C82345C900658F032481F8D0058AE7FED379E24F96C9BECDA23F5BE494ACD74BDC4200F9636ED36EDC4025000C92D1F63B8F127EF6F8CF6DA43709A7B2056BB4FEF5C02372A9E31102BF2E4499DE634E82802382086D6DE2B7B78A38608902471C6A155140C0000E00038C5494514005145140051451400514514005145140051451400514514005145140051451400514514005145140051451401CFBFF00A2FC4387673FDA3A549E6EEFE1FB34A9B36FA67ED726739E8B8C60E7A0AE7F5FFDDF883C2B2A7CB23EA12C0CE382D19B49DCA13FDD2D1C6C474CA29EA0574140051451401877F04DA56A9FDB36714925BC885751B68149793EE849D546773A2A952A06E7523EF18D10EC413C3756F15C5BCB1CD04A81E392360CAEA46410470411CE6A4AC39FC310FDA25B9D3B51D4B4A9E572D21B49C3467272D88650F12966F98B2A0627273F336403728AE7FF00E11ED53FE873D73FEFCD97FF0023D67EA763AA58F956F6FE2DD72E750B8C8B7B611D92EEC6373B37D98EC8D7237360E32000CCCAAC01B1ACDF5C3EFD2349936EAB3C4489B6865B343902670783820ED4FE36523850EC91C9A9691E1C48749B65925BBD9BE2B0B6066B893731FDE3739019F39964217736598139ACFD37C0CB6F6EC353D7B59D42E257334D2A5D359EE9481BDBFD1F6120E1400ECFB1555548039E834DD2AC748B7682C2DA3811DCC9215E5A572002EEC7977381966249EE4D0065FD975DD6B9BEB8FEC8B16FF975B470D7322FA493748F2090563059480566AD4D374AB1D22DDA0B0B68E04773248579695C800BBB1E5DCE06598927B935728A0028A28A0028A28A0028A28A0028A28A0028A28A0028A28A0028ACBD4B5FB1D36E16CCB4973A83A078EC6D57CC9D949203151F710B0DBE63954048CB0AA7F65D775AE6FAE3FB22C5BFE5D6D1C35CC8BE924DD23C824158C16520159A802E6A5E21D334AB85B5B89E47BB64120B5B5824B89F6648DE638D59C2646376319C0CE48AA7FF001516B3FF00500B43FF005CE7BC6FFD0A18B91FF4D772B7F030AD4D374AB1D22DDA0B0B68E04773248579695C800BBB1E5DCE06598927B935728030E0F0969097115DDDC326A579138912E75190DC34720392F186CAC249C122308381C00A00DCA28A0028A28A0028A28A0028A28A0028A28A0028A8E79E1B5B796E2E258E182242F249230554503249278000E73587FF00095DBDF7EEF41B69F5791BEE4D0295B4C74DC6E08F2D941C06119771CE1095200074154F52D5B4DD1ADD6E354D42D2C606708B25D4CB1296C138058819C0271EC6B2FEC3E25D47E6BCD5E0D2A33C8874C89659148E3066994ABA9E4E042A41C0C900EEB9A6F87B4CD2AE1AEADE091EED90C66EAEA792E27D99076092466709919DB9C672719268029FFC245797DF2E8DA0DF5C678F3EFD0D8C2ADD486120F3BA742B1329240C8F98A9FD93AEEA3CEA7ADFD9613D6D74A8847F29FBC8F33EE76C0E03C6216EA78246DE828A00CBD37C39A469370D75696318BC742925E4A4CB7122E41C3CCE4BB0E06016380A07402B528A2800A2A9EA5A943A65BAC922C92C92388E0B7880324F210484404819C0249240001662141232FF00B1A5D7BF7FE204DD68FCA68CE11E151FC266E0F992752464C6A7180CC8242007F6DDE6B3FBBF0F43884F3FDA9770136D81CFEED3723CD91B70CB88C8624392BB4DCD3740B1D36E1AF02C973A83A1492FAE9BCC9D94904A863F710B0DDE5A054049C28AD4A2800A28A2800A28A2800AE7FC23FE95A75DEB27FE63176F7A98FBAD0E1638180EA37431C4C41E7733703EE893C4B3CD25BC1A359CB24579AA3984491315782103334C08E54AAFCAAD82048F103C1AD882086D6DE2B7B78A38608902471C6A155140C0000E00038C500494567EA7AEE8FA2795FDADAAD8D879D9F2FED770916FC6338DC467191D3D4567FF00C277E0FF00FA1AF43FFC18C3FF00C55007414573FF00F09DF83FFE86BD0FFF000630FF00F1547FC265A5FF00CFAEB9FF00822BDFFE33401D0515CFFF00C265A5FF00CFAEB9FF00822BDFFE3347FC255F68FF00906E83AE5F6DFF0059FE87F64D9E9FF1F262DD9E7EEEEC639C646403A0A2B9FF00F848754FFA1335CFFBFD65FF00C9147F6BF88AEBFE3CBC2FF67DBF7FFB56FE38777A6CF204D9EF9DDB7B633CE003A0A2B9FF00B678C3FE805A1FFE0E66FF00E45A3C9F185C7EF7EDDA1D8EEFF977FB1CD77B3FEDAF9B16ECF5FB8B8CE39C64807414573FF63F187FD07743FF00C134DFFC9547FC23DAA7FD0E7AE7FDF9B2FF00E47A00E828AE7FFE119BA97E4BDF146B9756E7EFC3BE0B7DDE9FBC8224907383F2B0CE307209073F53D23C35A3F94B7BA96B8B34D9F26DE3D6AFE59A6C6376C89252EF80413B41C0E4E05007615CFF00867FD327D5F5A1F343A85DFF00A248DCB1B78D1635C1FF009E6CEB2C898241597775635CFF00FC2063C41FF2136D56CB4A6E1B4CBAD4A6BA92703AF9A5E6922553938080BA9547122B7CABD07FC209E0FF00FA15343FFC1743FF00C4D006C5F5FD9E996725E5FDDC1696B1E37CD3C82345C900658F032481F8D63FFC277E0FFF00A1AF43FF00C18C3FFC5558B1F09F86F4CBC8EF2C3C3FA55A5D479D9341651C6EB9041C301919048FC6B628039FFF0084E7C2CFF2DB6BD637B31FBB6F6128BA99FD76C516E76C0E4E01C0049E01A3FE132D2FFE7D75CFFC115EFF00F19AE828A00E7FFE130B07F960B0D72598F091FF00635D47BDBB0DD246A8B93DD995475240E68FF848754FFA1335CFFBFD65FF00C915D051401CFF00F6FEAF27C91783F55491B856B8B8B458D4F62E526660BEA5558E3A0278A3ED9E30FF00A01687FF008399BFF916BA0A28039FFB678C3FE805A1FF00E0E66FFE45A3ED9E30FF00A01687FF008399BFF916BA0A28039FFB1F8C3FE83BA1FF00E09A6FFE4AA3EC7E30FF00A0EE87FF008269BFF92ABA0A28039FFB1F8C3FE83BA1FF00E09A6FFE4AA3EC7E30FF00A0EE87FF008269BFF92ABA0A28039FFEC3D6E7FDE5CF8B6FA298FDE4B0B4B68E11E9B5658E571C75CBB739C606003FE11ED53FE873D73FEFCD97FF0023D741450073FF00F08F6A9FF439EB9FF7E6CBFF0091E8FF00847B54FF00A1CF5CFF00BF365FFC8F5D051401CFFF00C23DAA7FD0E7AE7FDF9B2FFE47A3FE11ED53FE873D73FEFCD97FF23D741450073FFF00086E97FF003F5AE7FE0F6F7FF8F51FF086E97FF3F5AE7FE0F6F7FF008F5741450073FF00F086E97FF3F5AE7FE0F6F7FF008F51FF00086E97FF003F5AE7FE0F6F7FF8F5741450073FFF00086E97FF003F5AE7FE0F6F7FF8F51FF086E97FF3F5AE7FE0F6F7FF008F5741450073FF00F083F86A4E6F348835193A09B532D7B228FEE8798B305EA76838C92719268FF8413C1FFF0042A687FF0082E87FF89AE828A00E7FFE104F07FF00D0A9A1FF00E0BA1FFE268FF8413C1FFF0042A687FF0082E87FF89AE828A00E7FFE104F07FF00D0A9A1FF00E0BA1FFE26AE69BE1AD0746B86B8D2F44D36C676428D25ADAA44C5720E09500E3201C7B0AD4A2800A28A2800A28A2800A28A2800A28A2800A28A2800A28A2800A28A2800A28A2800A28A2800A28A2800A28A2800A28A2800A28A2800A28A2800A28A2800A28A2800A28A2800A28A2800A28A2800A28A2800A28A2800A28A2800A28A2800A28A2800A28A2800A28A2800A28A2800A28A2800A28A2800A28A2800A28A2800A28A2800A28A2800A28A2800A28A2800A28A2800AE7EE7FE2A4D45AC97F79A25BEF4BD3D16EA60401083FC51AFCFE60E016DA9960254AB1E24BEB8B5D3A3B5B093CBD4B50945A5A3ED07CB7604B4983C1F2D1649369C6ED9B7396144D35BE81676DA6E9B6BE75D4BBBECD6BE61CC8739792473921416DCF236492DFC4EEAAC017352D4A1D32DD64916496491C4705BC40192790824220240CE0124920000B310A0914EC6C6E2F6F23D57558FCB99326D2CCB061680820B31190D310482C3214128A482EF249A6E9B325C36A5A93472EA52214C46498EDA3241F2A3C8071900B3900B9009000444D4A0028A28A0028A28A0028A28A0028A28A0028A28A0028A28A0028A28A0028A28A0028A28A0028A28A0028A28A0028A28A0028A28A00E7FC67F2F87D253C470EA1633CAE7A471A5DC4EEEC7B2AAAB3127800127815D0565F8974D9B59F0AEAFA5DBB46B3DED94D6F1B484850CE85413804E327D0D58D27528759D1AC754B759160BDB78EE2359000C15D43007048CE0FA9A00B9451450014515CFFF006CCBAF7EE3C3EFBAD1F87D650A3C2A3F8843C9F324E801C18D4E725990C6402E6A5ADC36770BA7DB79775ABCA81E1B15902B6D248F31FA94881072E41E980198AA9934CD33EC3E6DC5C4DF69D42E306E2E4AEDDD8CED455C9D91AE4ED5C9C6492599999A4D374D874CB768E369259247324F71290649E42002EE4003380000000000AA028005CA0028A28A0028A28A0028A28A0028A28A0028A28A0028A28A0028A8E79E1B5B796E2E258E182242F249230554503249278000E73587F68D675DF96D629F45B03C8BB9427DAE41D46C89959514E41CC9F38C3298D490C002E6A5AFD8E9B70B665A4B9D41D03C7636ABE64ECA49018A8FB88586DF31CAA024658553FB2EBBAD737D71FD9162DFF2EB68E1AE645F4926E91E4120AC60B2900ACD5A9A6E9563A45BB41616D1C08EE6490AF2D2B90017763CBB9C0CB3124F726AE50053D374AB1D22DDA0B0B68E04773248579695C800BBB1E5DCE06598927B935728A2800A28AC7BEF16786F4CBC92CEFF00C41A55A5D478DF0CF7B1C6EB9008CA93919041FC6803628AE7FF00E133D21B9893559E33CACD6FA3DDCB1C83B323A4455D4F50CA48239048A3FE121D53FE84CD73FEFF00597FF245007414573FF69F1849F27F656876FBB8F3BFB4E69BCBFF006BCBF2137E3AEDDCB9E9B875A3FB3BC533FEEEE7C45631427EF3D869663987A6D696595073D728DC6718382003A0A2B9FF00F8466EA5F92F7C51AE5D5B9FBF0EF82DF77A7EF2089241CE0FCAC338C1C82413FE10BD11FE5B94BEBD84FDEB7BFD4AE6EA17F4DD14B232360F2320E08047205007415CFF00FC277E0FFF00A1AF43FF00C18C3FFC551FF082783FFE854D0FFF0005D0FF00F1347F695E7883E4D0A6FB3588FF0059A94B6E4F980F6B70D80DC1DC2621A3FBB81265B60057BAF887E18B3F219EFA7961B89560B7B8B6B19E786791BA2472C68C8ED904614939047506B2E7F18F88F55B8962F0F7863528A085CABDCDFD96C67C1E0AC52CB0028C390C242C3690D1AE549EA34DD02C74DB86BC0B25CEA0E8524BEBA6F327652412A18FDC42C3779681501270A2B528038382DE517115D6A1E12F13EAD771B89165BFBBB2755753F2BAC22E0448E3180C88A7AF392C4EE7F6A7896E7E7B3F0D41046382BA9EA4B1484FA810A4CBB7A72581CE780304F41450073FF6CF187FD00B43FF00C1CCDFFC8B46FF00185D7C9E4687A6639F3BCE9AFB77FB3E5ED871EBBB71E98DA7391D051401CFFD8FC61FF41DD0FF00F04D37FF002551F63F187FD07743FF00C134DFFC955D051401CFFD8FC61FF41DD0FF00F04D37FF0025563DF5B78827BC92C34DF13EAB2DFF0002E268ADECD6CEC99806C3068CC8783958C3336366F650E1CEC7DB2E3C49FBAB113DB690DCBEA0AE15AED3FBB6E41DCAA79CCA42FCB831E77891362C6C2CF4CB38ECEC2D20B4B58F3B2182311A2E49270A3819249FC68039B8FC132C5AA4DA9AF8A75937F2A796D70D0D93384E3E4526DF2A9950768C0CE4E3249373FE11ED53FE873D73FEFCD97FF23D741450073FFF0008F6A9FF00439EB9FF007E6CBFF91E8FF844E29FE6D4759D72F661C093FB41ED70BE9B6DBCA43CE792A5B9C670001D051401CFFF00C21BA5FF00CFD6B9FF0083DBDFFE3D47FC21BA5FFCFD6B9FF83DBDFF00E3D5D051401CFF00FC21BA5FFCFD6B9FF83DBDFF00E3D59FA9E83E0ED3BCA4D474B8355BF9F22D21BF6FB75DCE46329119D99B68CE4F21172598A8DCD5A1FF00093FF68FC9E1BB3FED6FFA7BF37CAB31FF006DB0DBFA30FDD2C986186DBD6B434CD33EC3E6DC5C4DF69D42E306E2E4AEDDD8CED455C9D91AE4ED5C9C64925999998039FD27E1D786EDE29E6BFF000E68725D5D4BE73C69631B4507CAAA238F2B9DA02824E06E62CD85DDB4687FC209E0FF00FA15343FFC1743FF00C4D741450067E99A168FA279BFD93A558D879D8F33EC96E916FC6719DA067193D7D4D685158F7DE2CF0DE9979259DFF8834AB4BA8F1BE19EF638DD720119527232083F8D006C515CFF00FC25D6B2FC965A5EB975707EE43FD953DBEEF5FDE4EA918E327E6619C60649009FDB9ADCFF00BBB6F095F4531FBAF7F776D1C23D7734524AE38E9846E719C0C9001D0515CFFDB3C61FF402D0FF00F07337FF0022D1F63F187FD07743FF00C134DFFC95401D0515CFFF00C23DAA7FD0E7AE7FDF9B2FFE47A3FE10DD2FFE7EB5CFFC1EDEFF00F1EA00E82B1EFBC59E1BD32F24B3BFF1069569751E37C33DEC71BAE40232A4E464107F1AAFFF00082F84DB997C37A54F21E5A6B8B4496490F76777059D8F52CC49279249AD8B1B0B3D32CE3B3B0B482D2D63CEC8608C468B9249C28E064927F1A00C7FF84B6DEE78D2B4CD57546EA0DBDA98A364FEFA4D318E2753C636B9C839008C907DA7C5977F2C5A7695A746FCACD7174F712443A80F0A22A96EC42CD804E416039E828A00E7FF00E11FD46F39D5BC477D2ABFFACB6B055B384E3A6D65CCEBD01389B939FE13B6B434CD0F4BD1BCD3A758416F24D833CCA9FBC9C8CE1A473F348DC93B989249249C935A145001451450014514500145145001451450014514500145145001451450014514500145145001451450014514500145145001451450014514500145145001451450014514500145145001451450014514500145145001451450014514500145145001451450014514500145145001451450014514500145145001451450014514500145145001451450014514500145145001451450014514500145145001451450014514500145145001451450014514500145145001451450014514500145145001451450014514500145145001451450014515C9EAFE25BBB8D52D344D0ADE4796EAE1A19751CA7956C91F33B20392EE9C203B0C62475566C8294014E0D61F59F16EA32E949E7DCD9EFD32DA47898C16C0303712C8C31962EA8821CEF3E52B02A92971D4699A3DBE97E6C88F3DC5D4F8F3EEAE6532492119C727855C9621142A2966DAA326A4D2B4AB1D0F4BB7D334CB68EDACEDD3645127451FCC9272493C92493926AE500145145001451450014514500145145001451450014514500145145001451450014514500145145001451450014514500145145001451450015CFF00877FE2572DD787E6F91A09659AC81E0496ACDB9420E81622FE4ED07E508870A1D0574159FA9E8F6FAA79523BCF6F75067C8BAB694C72464E33C8E1972149460C8C557729C0A00D0AC7BEF1259DADE49616B1CFA96A51E37D9D8A8778F2011E6312122CAF23CC65DD83B7278AAFFF00087E9D71F36AD3DF6AECDFEB16FEE59A197D375BAED838E3188C72037DEE6B62C6C2CF4CB38ECEC2D20B4B58F3B2182311A2E49270A3819249FC68031FFB0EE35AFDE7897C89603D34888896D011D19D9915A56EA704041F2FC859039E828A2800A28A2800A28A2800A28A2800A28A2800A28A2800A28A2800A28A28033F5BD2535CD1E7D365B99EDE39B6867836EEC0604A90EACACAD8DACAC082A482306B3FFB2FC4B6DF259F89609E33C96D4F4D596407D0185E15DBD382A4E73C91803A0A28039FFB1F8C3FE83BA1FF00E09A6FFE4AA3EC7E30FF00A0EE87FF008269BFF92ABA0A28039FFF00847B54FF00A1CF5CFF00BF365FFC8F47FC2236B2FCF7BAA6B975707EFCDFDAB3DBEEF4FDDC0C918E303E5519C64E4924F41450073FFF000837859FE6B9D06C6F663F7AE2FE217533FA6E965DCED81C0C9380001C015B1636167A659C767616905A5AC79D90C1188D172493851C0C924FE35628A0028A28A0028A28A0028A28A00E7EE3FE2A2D62F34B7FF904D8EC4BB51FF2F3332EFF0025BFE99AA346CC0643F98149C2BAB7415CFDC07F0FEB179A9AC13CFA6DFEC6B94B685A59219D57679BB1416756458D0851F298D4ED219D97434CD7747D6FCDFEC9D56C6FFC9C799F64B849766738CED2719C1EBE86803428A28A0028A28A0028A28A002B9FFF0091AFFEC5FF00FD38FF00F73FFE8DFF00AE7FEB4BEFF8A9A5934D87FE41514A16F67EA2764605ADD07465C8DB29395C6E8F058B98FA0A0028A28A0028AE6F55F881E10D152E1AFF00C47A6A3DB3EC96149D64955B76D23CB4CBE41EA31C739E86B2EEBE24C4FE42E87E18F11EB3F6A895EDAE21D39E1B6766FB81A5942ED53C12F82A01CF3CD0077155EFAFECF4CB392F2FEEE0B4B58F1BE69E411A2E48032C7819240FC6B87FED4F895ABFC90787EC742ECE2EEE526CAF768E68D9B6C9CF0AD0328DA4963F72AC58F85B5F5BC8F51927D2A2BF8F3B65BEF3F54756208778DD9E158376705238D5781DB0AA01B1FF00091DC5FF001A0E913DFC6DF72F67716D684F5E1CE647523055E38DD1B230D8C907FC231FDA3F3F892F3FB5BFE9D3CAF2ACC7FDB1CB6FE8A7F7AD26186576F4A3FE11ED4A7E6F3C57AABABFFAE86DE3B786320FDE5422332A2F500893781FC79F9A8FF842B41938BCB59F518FA88753BC9AF6353FDE0933B286EA37019C1233826802C5F78AFC3FA75E496575AD58C77C9806CC4EAD392402AAB1025D988230A01272300E6ABFFC255F68FF00906E83AE5F6DFF0059FE87F64D9E9FF1F262DD9E7EEEEC639C646762C6C2CF4CB38ECEC2D20B4B58F3B2182311A2E49270A3819249FC6AC50073FF0069F165DFCB169DA569D1BF2B35C5D3DC4910EA03C288AA5BB10B3601390580E4FEC1D5AEBE6D43C517DF3F12C1610C56F0B2F4C292AF326475225DD924A95E00E828A00E7FFE109F0EC9FF001FBA7FF69E3EE7F6ACD25F797EBB3CF67D99E33B719C0CE702B62C6C2CF4CB38ECEC2D20B4B58F3B2182311A2E49270A3819249FC6AC51400514514005145140051451400514514005145140051451400514514005145140051451400514514005145140051451400514514005145140051451400514514005145140051451400514514005145140051451400514514005145140051451400514514005145140051451400514514005145140051451400514514005145140051451400514514005145140051451400514514005145140051451400514514005145140051451400514514005145140051451400514514005145140051451400514514005145140051451400514514005145140051451400514514005145140051451400514573FFF00235FFD8BFF00FA71FF00EE7FFD1BFF005CFF00D68057BED72DF56B3925B3BFF2F434C0B8D4AD9CB1B8248021B62992CC490A5D32413B13321263B9A0694D022DF5E5B4705D1430DBDBA6D09656DBBE48502FCAA76AA17DA482C30095540B1DB7FC549A8ADEB7EF344B7D8F643A2DD4C0926623F8A35F93CB3C02DB9F0C044F5D05001451450014514500145145001451450014514500145145001451450014514500145145001451450014514500145145001451450014514500145145001451450014514500145145001451450014514500145145001451450014514500145145001451450014514500145145001451450014514500145145001451450014514500159FA9E85A3EB7E57F6B695637FE4E7CBFB5DBA4BB338CE37038CE074F415A1450073FFF00085E889F2DB25F59423EEDBD86A5736B0A7AED8A2915172793803249279268FF00845E583E5D3BC47AE59427931F9E97596F5DD7292B8E31C060BC6719249E828A00E7FF00E11ED53FE873D73FEFCD97FF0023D1FF0008F6A9FF00439EB9FF007E6CBFF91EBA0A28039FFF00847B54FF00A1CF5CFF00BF365FFC8F51CFE17D46E6DE581FC69E20092214631AD9A300463865B7054FB8208ED5D251401CBD87842EF4CD3ADAC2CFC5DAE476B6B12430A7976676A2801464DBE4E001D6AC7FC23DAA7FD0E7AE7FDF9B2FFE47AE828A00E3ED7E1F4516A33DE5E78A3C577FE76E2619B5678A35627395587663B8007CA01E9D31A1FF00085683271796B3EA31F510EA77935EC6A7FBC1267650DD46E0338246704D741450067E99A168FA279BFD93A558D879D8F33EC96E916FC6719DA067193D7D4D6851450014514500145145001451450014514500145145001451450014514500145145001451450014514500145145001451450014514500145145001451450014514500145145001451450014514500145145001451450014514500145145001451450014514500145145001451450014514500145145001451450014514500145145001451450014514500145145001451450014514500145145001451450014514500145145001451450014514500145145001451450014514500145145001451450014514500145145001451450014514500145145001451450014514500145145001451450014514500145145001451450014514500145158FAB5F5C3DE41A469B26CBB9FE6B8995431B3830DFBDC1F977332EC40DDC96DAEB1BAD0057BFF00F8A9FED3A4C3FF00208F9E0D42E07FCB7EAAF6F19FCD5DC7DDE517E7DC622FFF00E2A7FB4E930FFC823E78350B81FF002DFAABDBC67F35771F77945F9F718A392157787C3BA31921B581F3A94F148C1E34652DB04B92DE7C8CCACCDCB056672CACF1B1E820821B5B78ADEDE28E18224091C71A8554503000038000E31401251451400514514005145140051451400514514005145140051451400514514005145140051451400514514005145140051451400514514005145140051451400514514005145140051451400514514005145140051451400514514005145140051451400514514005145140051451400514514005145140051451400514514005145140051451400514514005145140051451400514514005145140051451400514514005145140051451400514514005145140051451400514514005145140051451400514514005145140051451400514514005145140051451400514514005145140051451400514514005145140051451400514514005145140051451400514514005145140051451400514514005145140051451400514514005145140051451400514514005145140051451400514514005145140051451400514514005145140051451400514514005145140051451400514514005145140051451400514514005145140051451400514514005145140051451400514514005145140051451400514514005145140051451400514514014F55D4A1D234BB8BF9D64748532238802F2B74544048DCECC42A8EE481DEB0E1FB668B6B6F6A3C89BC47ACCA65B971968639044A1E5DA30DE4A2A2228E0B1312B30672F478CEFECEC5BC3E353BB82DB4D97554FB4BCF208D3F7714B3479638C6258A23D46718E4120DCF0FC135C3DCEBD771490DC6A4917976F2A957B6B75526389C71F3EE791DB8C83215CB0406803434BD361D26C16D21691C07791E49082D248EE5DDCE001966666C0000CE000302AE5145001451450014514500145145001451450014514500145145001451450014514500145145001451450014514500145145001451450014514500145145001451450014514500145145001451450014514500145145001451450014514500145145001451450014514500145145001451450014514500145145001451450014514500145145001451450014514500145145001451450014514500145145001451450014514500145145001451450014514500145145001451450014514500145145001451450014514500145145001451450014514500145145001451450014514500145145001451450014514500145145001451450014514500145145001451450014514500145145001451450014514500145145001451450014514500145145001451450014514500145145001451450014514500145145001451450014514500145145001451450014514500145145001451450014514500145145001451450014514500145145001451450014514500145145001451450014514500145145001451450014514500145145001451450014514500145145001451450014514500145145001451450014514500145145001451450014514500145145001451450014514500145145001451450014514500145145001451450014514500145145001451450014514500145145001451450014514500145145001451450014514500145145001451450014514500145145001451450014514500145145001451450014514500145145001451450014514500145145001451450014514500145145001451450014514500145145001451450014514500145145001451450014514500145145001451450014514500145145001451450014514500145145001451450014514500145145001451450014514500145145001451450014514500145145001451450014514500145145001451450014514500145145001451450014514500145145001451450014514500145145001451450014514500145145001451450014514500145145001451450014514500145145001451450014514500145145001451450014514500145145001451450014514500145145001451450014514500145145007FFD9");
		byte[] byteArr = toByteArray(str);
		System.out.println("Convert =>"+ str);
		System.out.println("Convert =>"+ toHexString(byteArr));
		
//	byte[] toByteArray(String hexstring)
	}
	
} // class


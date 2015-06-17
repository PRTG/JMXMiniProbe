package com.paessler.prtg.util.io;

import java.io.File;

import com.paessler.prtg.util.StringUtility;

public class FileUtility {
	// -----------------------------------------------------------------------------------------------------------
	/**
	 * Check for file existence
	 * @param fqp Fully qualified path of the file to check for
	 * @return true => exists
	 */
	public static boolean fileExists(String fqp)
	{
		boolean retVal = false;
		if(fqp != null)
		{
			File tmpfile = new File(fqp);
			retVal = tmpfile.exists();
			// Check for quoted path
			if(!retVal)
			{
				int idx = fqp.indexOf(StringUtility.DoubleQuote);
				if(idx != StringUtility.STRING_INVALID_POSSITION)
				{
					fqp = fqp.replace(StringUtility.DoubleQuote, StringUtility.EmptyString);
					tmpfile = new File(fqp);
					retVal = tmpfile.exists();
				}
			}
		}
		return retVal;
	}

}

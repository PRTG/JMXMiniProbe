
/*
 * Copyright (c) 2014, Paessler AG <support@paessler.com>
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 * 3. Neither the name of the copyright holder nor the names of its contributors
 *    may be used to endorse or promote products derived from this software without
 *    specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
 * ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package com.paessler.prtg.jmx.channels;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.ConvertUtilsBean;
import org.apache.commons.beanutils.Converter;

import com.google.gson.annotations.SerializedName;
import com.paessler.prtg.util.NumberUtility;
import com.paessler.prtg.util.enums.EnumMapUtil;

public abstract class Channel {
	
	public static enum Mode  {@SerializedName("counter") COUNTER, @SerializedName("Integer") INTEGER, @SerializedName("float") FLOAT};
	
	public static final String UNIT_STR_BANDWIDTH	= "BytesBandwidth";
	public static final String UNIT_STR_MEMORY 		= "BytesMemory";
	public static final String UNIT_STR_DISK 		= "BytesDisk";
	public static final String UNIT_STR_FILE 		= "BytesFile";
	public static final String UNIT_STR_TRESPONSE	= "TimeResponse";
	public static final String UNIT_STR_TSEC		= "TimeSeconds";
	public static final String UNIT_STR_THOURS		= "TimeHours";
	public static final String UNIT_STR_TEMP		= "Temperature";
	public static final String UNIT_STR_PERCENT 	= "Percent";
	public static final String UNIT_STR_COUNT 		= "Count";
	public static final String UNIT_STR_CPU 		= "CPU (*)";
	public static final String UNIT_STR_CUSTOM		= "Custom";
	public static String[] getUnitSerializedNames(){
		return new String[] {
				UNIT_STR_BANDWIDTH,		UNIT_STR_MEMORY,
				UNIT_STR_DISK,			UNIT_STR_FILE,
				UNIT_STR_TRESPONSE,		UNIT_STR_TSEC,
				UNIT_STR_THOURS,		UNIT_STR_TEMP,
				UNIT_STR_PERCENT,		UNIT_STR_COUNT,
				UNIT_STR_CPU,			UNIT_STR_CUSTOM
		};
	}
	// --------------------------------------
	public static String getUnitSerializedNameString(){
		StringBuilder retVal = new StringBuilder();
		boolean isfirst = true;
		for(String curr: getUnitSerializedNames()){
			if(!isfirst){
				retVal.append(", ");
			}
			retVal.append(curr);
			isfirst = false;
		}
		return retVal.toString();
	}
	// --------------------------------------
	public static String getUnitStrings(){
		StringBuilder retVal = new StringBuilder();
		boolean isfirst = true;
		for(Unit curr: Unit.values()){
			if(!isfirst){
				retVal.append(", ");
			}
			retVal.append(curr.toString());
			isfirst = false;
		}
		return retVal.toString();
	}
	// --------------------------------------
	public static enum Unit  {@SerializedName("BytesBandwidth") BANDWIDTH,
			@SerializedName(UNIT_STR_MEMORY)	MEMORY,
			@SerializedName(UNIT_STR_DISK)		DISK,
			@SerializedName(UNIT_STR_FILE) 		FILE,
			@SerializedName(UNIT_STR_TRESPONSE) TIME_RESPONSE,
			@SerializedName(UNIT_STR_TSEC)		TIME_SECONDS,
			@SerializedName(UNIT_STR_THOURS) 	TIME_HOURS,
			@SerializedName(UNIT_STR_TEMP) 		TEMP,
			@SerializedName(UNIT_STR_PERCENT) 	PERCENT,
			@SerializedName(UNIT_STR_COUNT) 	COUNT,
			@SerializedName(UNIT_STR_CPU ) 		CPU,
			@SerializedName(UNIT_STR_CUSTOM) 	CUSTOM};
	public static Map<String, Unit> unitMap = (Map<String, Unit>)EnumMapUtil.getEnumValuesMap(Unit.class.getEnumConstants());  
    
	public static Unit toUnit(String val){
		Unit retVal = null;
		if(val != null){
			retVal = unitMap.get(val);
			if(retVal == null){
				int ival = NumberUtility.convertToInt(val, -1);;
//				if(ival > 0){
//					retVal = Unit.ival
//				}
			}
		}
		return retVal;
	}
	static{
		ConvertUtils.register(new Converter(){

			@Override
			public Object convert(Class toType, Object object) {
				Object retVal = null;
		        if (object != null && toType.equals(Unit.class)) {
		        	retVal = toUnit(object.toString());
			    }
		        return retVal;
			}
			
		}, Unit.class);		
	}
    public String name;
    /* The type of the value. Please make sure that it matches the provided value, otherwise PRTG will show 0 values. */
    public Mode mode;
    /**	
     * BytesBandwidth
BytesMemory
BytesDisk
BytesFile
TimeResponse
TimeSeconds
TimeHours
Temperature
Percent
Count
CPU (*)
Custom (define the name of the unit using the additional field customunit)*/
    /** The unit of the value. Setting the correct unit type instead of custom units helps PRTG display received values in a better way.*/
    public Unit unit;
    public String customunit = null;
    public int showchart = 1;
    public int showtable = 1;
/*    
SpeedSize
VolumeSize	—	Size used for the display value. E.g., if you have a value of 50000 and use Kilo as size the display is 50 kilo #. Default is One (value used as returned). For the Bytes and Speed units this is overridden by the setting in the user interface.	One
Kilo
Mega
Giga
Tera
Byte
KiloByte
MegaByte
GigaByte
TeraByte
Bit
KiloBit
MegaBit
GigaBit
TeraBit
SpeedTime	—	See above, used when displaying the speed. Default is Second.	Second
Minute
Hour
Day    
    
decimalMode	—	Init value for the Decimal Places option. If 0 is used in the float mode (i.e., use integer), the default is Automatic; otherwise (i.e., for float) default is All. Note: You can change this initial setting later in the Channel settings of the sensor.	Automatic
All
Custom
decimalDigits	—	If you define Custom as decimalMode, specify the number of digits after the delimiter.	Integer
ValueLookup	—	Define if you want to use a lookup file (e.g., to view integer values as status texts). Please enter the ID of the lookup file you want to use, or omit this element to not use lookups. See PRTG Manual: Define Lookups for more information. Note: This setting will be considered only on the first sensor scan, when the channel is newly created; it is ignored on all further sensor scans (and may be omitted). You can change this initial setting later in the Channel settings of the sensor.	Any string
*/    
    /** Define an upper error limit for the channel. If enabled, the sensor will be set to a "Down" status if this value is overrun and the LimitMode is activated. Note: Please provide the limit value in the unit of the base data type, just as used in the <Value> element of this section. While a sensor shows a "Down" status triggered by a limit, it will still receive data in its channels. The values defined with this element will be considered only on the first sensor scan, when the channel is newly created; they are ignored on all further sensor scans (and may be omitted). You can change this initial setting later in the Channel settings of the sensor.	Integer */
    public Integer	limitMaxError = null;	
    /** Define an upper warning limit for the channel. If enabled, the sensor will be set to a "Warning" status if this value is overrun and the LimitMode is activated. Note: Please provide the limit value in the unit of the base data type, just as used in the <Value> element of this section. The values defined with this element will be considered only on the first sensor scan, when the channel is newly created; they are ignored on all further sensor scans (and may be omitted). You can change this initial setting later in the Channel settings of the sensor.	Integer */
    public Integer	limitMaxWarning = null;
    /** Define a lower warning limit for the channel. If enabled, the sensor will be set to a "Warning" status if this value is undercut and the LimitMode is activated. Note: Please provide the limit value in the unit of the base data type, just as used in the <Value> element of this section. The values defined with this element will be considered only on the first sensor scan, when the channel is newly created; they are ignored on all further sensor scans (and may be omitted). You can change this initial setting later in the Channel settings of the sensor.	Integer */
    public Integer	limitMinWarning = null;
    /** Define a lower error limit for the channel. If enabled, the sensor will be set to a "Down" status if this value is undercut and the LimitMode is activated. Note: Please provide the limit value in the unit of the base data type, just as used in the <Value> element of this section. While a sensor shows a "Down" status triggered by a limit, it will still receive data in its channels. The values defined with this element will be considered only on the first sensor scan, when the channel is newly created; they are ignored on all further sensor scans (and may be omitted). You can change this initial setting later in the Channel settings of the sensor.	Integer */
    public Integer	limitMinError = null;
    /** Define an additional message. It will be added to the sensor's message when entering a "Down" status that is triggered by a limit. Note: The values defined with this element will be considered only on the first sensor scan, when the channel is newly created; they are ignored on all further sensor scans (and may be omitted). You can change this initial setting later in the Channel settings of the sensor.*/
    public String	limitErrorMsg = null;
    /** Define an additional message. It will be added to the sensor's message when entering a "Warning" status that is triggered by a limit. Note: The values defined with this element will be considered only on the first sensor scan, when the channel is newly created; they are ignored on all further sensor scans (and may be omitted). You can change this initial setting later in the Channel settings of the sensor. */
    public String	limitWarningMsg = null;
    /** Define if the limit settings defined above will be active. Default is 0 (no; limits inactive). If 0 is used the limits will be written to the sensor channel settings as predefined values, but limits will be disabled. Note: This setting will be considered only on the first sensor scan, when the channel is newly created; it is ignored on all further sensor scans (and may be omitted). You can change this initial setting later in the Channel settings of the sensor.	0 (= no)  1 (= yes) */
    public Integer		limitMode = null;
    /** If enabled for at least one channel, the entire sensor is set to warning status. Default is 0 (no).	0 (= no) 1 (= yes)*/
    public Integer	warning = null;
    /** Text the sensor returns in the Message field with every scanning interval. There can be one message per sensor, regardless of the number of channels. Default is OK. */
    public String 	message	= null;
    /** The type of error. Note: The type is not necessarily shown in PRTG.	Data: The monitored device returned a value but the sensor could not process it.*/
    public String 	error = null;

    // ------------------------------------------------------------------------------------------------
    
    // ------------------------------------------------------------------------------------------------
    public Channel(String name,  Unit unit, Mode mode) {
        this.name = name;
        this.mode = mode;
        this.unit = unit;
    }

	// -----------------------------------------------------
	public String getName() 		{return name;}
	public void setName(String name) {this.name = name;	}

	// -----------------------------------------------------
	public Mode getMode() {return mode;	}
	public void setMode(Mode mode) {this.mode = mode;}

	// -----------------------------------------------------
	public Unit getUnit() {	return unit;}
	public void setUnit(Unit unit) {this.unit = unit;}

	// -----------------------------------------------------
	public String getCustomunit() {	return customunit;}
	public void setCustomunit(String customunit) {this.customunit = customunit;}

	// -----------------------------------------------------
	public int getShowchart() {	return showchart;}
	public void setShowchart(int showchart) {this.showchart = showchart;}

	// -----------------------------------------------------
	public int getShowtable() {	return showtable;}
	public void setShowtable(int showtable) {this.showtable = showtable;}

	// -----------------------------------------------------
	public Integer getLimitMaxError() {	return limitMaxError;}
	public void setLimitMaxError(Integer limitMaxError) {this.limitMaxError = limitMaxError;}

	// -----------------------------------------------------
	public Integer getLimitMaxWarning() {return limitMaxWarning;}
	public void setLimitMaxWarning(Integer limitMaxWarning) {this.limitMaxWarning = limitMaxWarning;}

	// -----------------------------------------------------
	public Integer getLimitMinWarning() {return limitMinWarning;}
	public void setLimitMinWarning(Integer limitMinWarning) {this.limitMinWarning = limitMinWarning;}

	// -----------------------------------------------------
	public Integer getLimitMinError() {	return limitMinError;}
	public void setLimitMinError(Integer limitMinError) {this.limitMinError = limitMinError;}

	// -----------------------------------------------------
	public String getLimitErrorMsg() {return limitErrorMsg;}
	public void setLimitErrorMsg(String limitErrorMsg) {this.limitErrorMsg = limitErrorMsg;	}

	// -----------------------------------------------------
	public String getLimitWarningMsg() {return limitWarningMsg;}
	public void setLimitWarningMsg(String limitWarningMsg) {this.limitWarningMsg = limitWarningMsg;}

	// -----------------------------------------------------
	public Integer getWarning() {return warning;}
	public void setWarning(Integer warning) {this.warning = warning;}

	// -----------------------------------------------------
	public String getMessage() {return message;}
	public void setMessage(String message) {this.message = message;}
	
	// -----------------------------------------------------
	public String getError() {return error;}
	public void setError(String error) {this.error = error;	}
	// ----------------------------------
	public Integer getLimitMode() {return limitMode;	}
	public void setLimitMode(Integer limitMode) {
		this.limitMode = limitMode;
	}
	
}

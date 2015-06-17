package com.paessler.prtg.util.enums;

import java.util.HashMap;
import java.util.Map;
import java.lang.reflect.Field;

import com.paessler.prtg.util.gson.GsonUtil;

public class EnumMapUtil {

	public static <T extends Enum> Map<String, T> getEnumValuesMap(T[] enums){
		Map<String, T> retVal = new HashMap<String, T>();
    	String tmp;
    	if(enums != null){
    		
//	    	for(T curr: enums.getClass().getEnumConstants()){
	    	for(T curr: enums){
	    		tmp = curr.toString();
	    		retVal.put(tmp, curr);
	    		tmp = GsonUtil.getSerializedName(curr);
	    		if(tmp != null){
		    		retVal.put(tmp, curr);
	    		}
	    	}
    	}
    	return retVal;
    }

}

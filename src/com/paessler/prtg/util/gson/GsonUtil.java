package com.paessler.prtg.util.gson;

import java.lang.reflect.Field;

import com.google.gson.annotations.SerializedName;

public class GsonUtil {

	//http://programwith.com/question_1356017_gson-how-to-get-serialized-name

	public static<T extends Enum> String getSerializedName(T enumval){
		String retVal = null;
		if(enumval != null){
			try {
				Class clazz = enumval.getClass();
				Field field = clazz.getDeclaredField(enumval.toString());
				if(field != null){
					SerializedName sName = field.getAnnotation(SerializedName.class);
					if(sName != null){
						retVal = sName.value();
					}
				}
			} catch (NoSuchFieldException e) {
				// TODO Auto-generated catch block
//				e.printStackTrace();
			} catch (SecurityException e) {
				// TODO Auto-generated catch block
//				e.printStackTrace();
			} 			
		}
		return retVal;
	}
	
}

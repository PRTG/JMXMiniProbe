package com.paessler.prtg.jmx.definitions;

import java.util.HashMap;
import java.util.Map;

public class RadioFieldDefinition extends FieldDefinition {
    public static String FIELDTYPE_RADIO  = "radio";

    public Map<Object, Object> options;
	// --------------------------------------------------------------
	public Map<Object, Object> getOptions() {return options;}
	public void setOptions(Map<Object, Object> options) {
		this.options = options;
	}
	// ----------------------
    public void addOption(Object field, Object value){
    	if(field != null && value != null){
        	options.put(field, value);
    	}
    }
	// --------------------------------------------------------------
    public RadioFieldDefinition(String name, String caption, String help, Object defaultValue) {
    	super(FIELDTYPE_RADIO, name, caption, help, defaultValue);
        options = new HashMap<Object, Object>();

    }
}

package com.paessler.prtg.jmx.definitions;

public class IPPortField extends IntegerField {
	public static final int MIN_PORT_VALUE = 1;
	public static final int MAX_PORT_VALUE = 65536;
    // ---------------------------
    public IPPortField(String name, String caption, String help) {
    	super(name, caption, help);
    	setMinimum(MIN_PORT_VALUE);
    	setMaximum(MAX_PORT_VALUE);
    }
	// --------------------------------------------------------------
    public IPPortField(String name, String caption) {
    	this(name, caption, null);
    }

}

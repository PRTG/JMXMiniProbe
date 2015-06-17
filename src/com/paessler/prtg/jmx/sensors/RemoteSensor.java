package com.paessler.prtg.jmx.sensors;

import java.util.Vector;

import com.google.gson.JsonObject;
import com.paessler.prtg.jmx.definitions.PingSensorDefinition;
import com.paessler.prtg.jmx.definitions.SensorConstants;
import com.paessler.prtg.jmx.sensors.profile.Profile;

public abstract class RemoteSensor<T> extends Sensor {
	//----------------------------------------------------------------------
	protected String	remoteHost;
	protected int 		remotePort;
	protected String 	remoteProtocol;
	protected long 		timeout = 1000;
	protected String	vectorPropertyName = "namevector";
	protected String	vectorSeparator = ",";
	Vector<T> vectorOfValues = null;
	// ----------------------------------------------------
	public RemoteSensor(){
		super();
	}
	
	// ----------------------------------------------------
	public RemoteSensor(RemoteSensor<T> toclone){
		super(toclone);
		remoteHost = toclone.remoteHost;
		remotePort = toclone.remotePort;
		remoteProtocol = toclone.remoteProtocol;
		timeout = toclone.timeout;
		vectorPropertyName = toclone.vectorPropertyName;
		vectorSeparator = toclone.vectorSeparator;
		getVectorOfValues().addAll(toclone.getVectorOfValues());
	}

	// ----------------------------------------------------
	public Vector<T> getVectorOfValues() 
	{
		if(vectorOfValues == null){
			vectorOfValues = new Vector<T>(); 
		}
		return vectorOfValues;	
	}
	// ---------------------------
	public void setVectorOfValues(Vector<T> vect) {
		this.vectorOfValues = vect;
	}
	// ---------------------------
	protected void addVectorEntry(T value){
		getVectorOfValues().add(value);
	}
	// ----------------------------------------------------
	
	// --------------------------------------------------------
	@Override
	public String toString() {return super.toString()+"|"+(remoteProtocol != null ?remoteProtocol+":" : "")+remoteHost+":"+remotePort;}
	// ----------------------------------------------------------------------
	//----------------------------------------------------------------------
	private boolean initialized = false;
	public void		setInitialized(boolean val) {initialized = val;};
	public boolean	isInitialized() 			{return initialized;};
	
	//----------------------------------------------------------------------
	public String getVectorPropertyName() {	return vectorPropertyName;}
	public void   setVectorPropertyName(String vectorPropertyName) {this.vectorPropertyName = vectorPropertyName;}
	// ----------------------
	public String getVectorSeparator() {	return vectorSeparator;}
	public void setVectorSeparator(String vectorSeparator) {this.vectorSeparator = vectorSeparator;}
	// ----------------------
	public String getRemoteHost() {	return remoteHost;}
	public void setRemoteHost(String remoteHost) {	this.remoteHost = remoteHost;}
	// ----------------------
	public int getRemotePort() {return remotePort;}
	public void setRemotePort(int remotePort) {	this.remotePort = remotePort;	}
	// ----------------------
	public String getRemoteProtocol() {	return remoteProtocol;}
	public void setRemoteProtocol(String remoteProtocol) {	this.remoteProtocol = remoteProtocol;}
	// ----------------------
	public long getTimeout() {return timeout;}
	public void setTimeout(long timeout) {	this.timeout = timeout;	}
	//----------------------------------------------------------------------
	protected void init(){
	}
	// ---------------------------
	protected void addVectorEntry(String value, boolean inoutonly){
	}
	//----------------------------------------------------------------------
	public void loadVectorFromJson(String jsonstring) {
	    if (jsonstring != null && !jsonstring.isEmpty()) {
			String[] intarr = jsonstring.split(getVectorSeparator());
			for(String curr: intarr){
				addVectorEntry(curr, false);
			}
	    }
	}

	//----------------------------------------------------------------------
	@Override
	public void loadFromJson(JsonObject json)  throws Exception{
		//  Local parameters
    	String tmpval = getJsonElementString(json, SensorConstants.REMOTE_HOST);
        if (tmpval != null && !tmpval.isEmpty()) {
        	setRemoteHost(tmpval);
        }
    	tmpval = getJsonElementString(json, SensorConstants.REMOTE_PROTOCOL);
        if (tmpval != null && !tmpval.isEmpty()) {
        	setRemoteProtocol(tmpval);
        }
		
        setRemotePort(getJsonElementInt(json, SensorConstants.REMOTE_PORT, getRemotePort()));
        setTimeout(getJsonElementLong(json, SensorConstants.REMOTE_PORT, getTimeout()));
        
        loadVectorFromJson(getJsonElementString(json, getVectorPropertyName()));        
		// Delegate
		super.loadFromJson(json);
	}
	// ----------------------------------------------------------------------
    @Override
    public void loadFrom(Profile profile) {
    	super.loadFrom(profile);
    	
    	setSensorName(profile.getName());
    	setKind(profile.getKind());
    	String tmptag = profile.getTag();
    	Object prop = profile.getProperty(SensorConstants.REMOTE_HOST);
    	if(prop != null){
    		getDefinition().setFieldDefaultValue(SensorConstants.REMOTE_HOST, prop.toString());
    	}
    	prop = profile.getProperty(SensorConstants.REMOTE_PORT);
    	if(prop != null){
    		getDefinition().setFieldDefaultValue(SensorConstants.REMOTE_PORT, prop.toString());
    	}
    	prop = profile.getProperty(SensorConstants.REMOTE_HOST);
    	if(prop != null){
    		getDefinition().setFieldDefaultValue(SensorConstants.REMOTE_HOST, prop.toString());
    	}
    	
    }
	//----------------------------------------------------------------------

}

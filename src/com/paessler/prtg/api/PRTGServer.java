package com.paessler.prtg.api;

import com.paessler.prtg.jmx.SHA1Generator;

public class PRTGServer {
    // -------------------------------------------------------------------------------
    public String host;
    public String port;
    public String webprotocol = "https";
    public String gid;
    public String key;
    public String keyHash;
    public int protocol = 1;
    public int debugLevel=0;

    // -------------------------------------------------------------------------------
    public String getHost() {
		return host;
	}
	public void setHost(String host) {
		this.host = host;
	}
	public String getPort() {
		return port;
	}
	public void setPort(String port) {
		this.port = port;
	}
	public String getWebprotocol() {
		return webprotocol;
	}
	public void setWebprotocol(String webprotocol) {
		this.webprotocol = webprotocol;
	}
	public String getGid() {
		return gid;
	}
	public void setGid(String gid) {
		this.gid = gid;
	}
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
		this.keyHash = SHA1Generator.getHash(key);
	}
	public String getKeyHash() {
		return keyHash;
	}
	// ------------------------
	public int getProtocol() {
		return protocol;
	}
	public void setProtocol(int protocol) {
		this.protocol = protocol;
	}
    // -------------------------------------------------------------------------------
    public int getDebugLevel(){	return debugLevel;}
    public void setDebugLevel(int level){debugLevel = level;}
    public void setDebugLevel(String level){
    	int ilevel = 0;
    	try{
    		ilevel = Integer.parseInt(level);
        	setDebugLevel(ilevel);
    	}
    	catch(Exception e)
    	{} // Ignore and default
   	}
    // -------------------------------------------------------------------------------
    // -------------------------------------------------------------------------------
    public String getURLHost(){
    	StringBuffer retVal = new StringBuffer(getHost());
    	String tmp = getPort();
    	if(tmp != null)
    		retVal.append(':').append(getPort());
    	return retVal.toString();
    }
    // -------------------------------------------------------------------------------
    public String getURLPrefix(String path){
//    	URL url = new URL(getWebprotocol(), getURLHost(), path);
    	StringBuffer retVal = new StringBuffer(getWebprotocol()).append("://");
    	retVal.append(getURLHost());
    	retVal.append(path);
    	retVal.append("?gid=");
    	retVal.append(getGid());
    	retVal.append("&key=");
    	retVal.append(getKeyHash());
    	retVal.append("&protocol=");
    	retVal.append(getProtocol());
    	return retVal.toString();
    }

}

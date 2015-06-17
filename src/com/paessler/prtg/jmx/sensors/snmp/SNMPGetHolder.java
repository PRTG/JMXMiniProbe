package com.paessler.prtg.jmx.sensors.snmp;

import java.util.List;
import java.util.Vector;

import org.snmp4j.PDU;

import com.paessler.prtg.util.snmp.OIDHolder;
import com.paessler.prtg.util.snmp.SNMPUtil;

public class SNMPGetHolder{
	public	int 		ifIndex;
	public	OIDHolder	ifHolder;
	public	String		ifName;
	public 	String		ifDescription;

	public List<OIDHolder> channels;
	// ------------------------------------
	public SNMPGetHolder(){
		ifIndex = -1;
		ifHolder = null;
		channels = new Vector<OIDHolder>();
	}
	public SNMPGetHolder(int idx, boolean inoutonly){
		this();
		ifIndex = idx;
		ifHolder = SNMPUtil.getIFOIDHolder(ifIndex);
		channels = SNMPUtil.getIFOIDHolderInst(channels, ifIndex, inoutonly);
	}
	// ------------------------------------
	public SNMPGetHolder(OIDHolder holder){
		this();
		channels.add(holder);
	}
	
	//----------------------------------------------------------------------
	public static void addVariableBindings(PDU pdu, Vector<SNMPGetHolder> vect){
		OIDHolder tmp;
		// request Sub-if name/desc
		if(vect != null){
	    	for(SNMPGetHolder curr :vect){
	    		tmp = curr.ifHolder;
	    		pdu.add(tmp.getVariableBinding());
	    		tmp = tmp.getDescriptionHolder();
	    		if(tmp != null){
	        		pdu.add(tmp.getVariableBinding());
	    		}
	    	}
		}
		
	}

	// -----------------------------------------------------------
	public static PDU getPDU(Vector<SNMPGetHolder> vect){
		PDU retVal = new PDU();
    	for(SNMPGetHolder curr :vect){
    		for(OIDHolder currh : curr.channels){
    			retVal.add(currh.getVariableBinding());
    		}
    	}
    	return retVal;
	}

} // class SNMPGetHolder

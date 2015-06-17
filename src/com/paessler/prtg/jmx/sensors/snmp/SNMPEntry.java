package com.paessler.prtg.jmx.sensors.snmp;

import java.util.Vector;

import org.snmp4j.PDU;
import org.snmp4j.smi.Null;
import org.snmp4j.smi.OID;
import org.snmp4j.smi.Variable;
import org.snmp4j.smi.VariableBinding;

import com.paessler.prtg.jmx.Logger;
import com.paessler.prtg.jmx.channels.Channel;
import com.paessler.prtg.jmx.channels.Channel.Mode;
import com.paessler.prtg.jmx.channels.LongChannel;
import com.paessler.prtg.jmx.channels.Channel.Unit;
import com.paessler.prtg.util.snmp.OIDHolder;
import com.paessler.prtg.util.snmp.OIDHolder.SNMPDataType;
import com.paessler.prtg.util.snmp.SNMPUtil;

public class SNMPEntry{
	
	public SNMPEntry(SNMPDataType dtype, String name, OID oid, String oidString, String descr){
		this.name = name;
		this.oidString = oidString;
		this.oid = oid;		
		this.description = descr;
		setDataType(dtype);
	}
	// -----------------------------------------
	public SNMPEntry(SNMPDataType dtype, String name, String oidString, String descr){
		this(dtype, name, new OID(oidString), oidString, descr);
	}
	// -----------------------------------------
	public String		name;
	public String		oidString;
	public OID 			oid;
	public String 		description;
	public SNMPDataType dataType;
	public int			mpyFactor = 1;
	public int			divFactor = 1;
	public Channel.Mode	mode = Channel.Mode.INTEGER;
	public Channel.Unit	unit = Channel.Unit.COUNT;
	// --------------------------------
	public Channel.Mode getMode() 			{return mode;}
	public void setMode(Channel.Mode mode)	{this.mode = mode;}
	// --------------------------------
	public Channel.Unit getUnit() 			{return unit;	}
	public void setUnit(Channel.Unit unit)	{this.unit = unit;}
	// -------------------------------
	public int getMpyFactor()				{return mpyFactor;}
	public void setMpyFactor(int mpyFactor)	{this.mpyFactor = mpyFactor;}

	// -------------------------------
	public int getDivFactor() 				{return divFactor;}
	public void setDivFactor(int divFactor) {this.divFactor = divFactor;}
	// ------------------------------------------
	public SNMPDataType getDataType() 		{return dataType;}
	// ----------------------------------
	public void setDataType(SNMPDataType dataType) {
		switch(dataType){
		case INT:
		case LONG:
			break;
		case OCTIN:
		case OCTOUT:
//			retVal = new LongChannel(name, getUnit(), value, getMode());
			setMode(Mode.COUNTER);
			setUnit(Unit.BANDWIDTH);
			break;
		case TICKS:
			if(getUnit() != Unit.TIME_SECONDS 
			|| getUnit() != Unit.TIME_HOURS 
			|| getUnit() != Unit.TIME_RESPONSE){
				setUnit(Unit.TIME_SECONDS);
				if(getDivFactor() == 1){
					setDivFactor(100);
				}
			}
			break;
		case COUNT:
//			retVal = new LongChannel(name, getUnit(), value);
			break;
//			case STRING:
//				retVal = new LongChannel(name, Unit.BANDWIDTH, var.toLong());
//				break;
//			lchannel  = new LongChannel("Packet Loss", Unit.PERCENT, stats.loss);
	
		}
		this.dataType = dataType;
	}

	// ------------------------------------------
	public String getName() 			{return name;}
	// ----------------------------------
	public void setName(String name) 	{this.name = name;}
	// ----------------------------------
	public String getOidString() 		{return oidString;	}
	// ----------------------------------
	public void setOidString(String oidString) {this.oidString = oidString;	}
	// ----------------------------------
	public OID getOid() 				{return oid;}
	// ----------------------------------
	public void setOid(OID oid) 		{this.oid = oid;}
	// ----------------------------------
	public String getDescription() 		{return description;}
	// ----------------------------------
	public void setDescription(String description) {this.description = description;	}

	// ----------------------------------
	public long getValue(long val){
		long retVal = val;
		if(getDivFactor() != 1){
			retVal = retVal / getDivFactor();
		}
		if(getMpyFactor() != 1){
			retVal = retVal * getMpyFactor();
		}
		return retVal;
	}
	// -----------------------------------------------
	public Channel getChannelInfo(Variable var){
		Channel retVal = null;
		long value = -1;
		if(var instanceof Null){
			String tmp = var.toString();
			retVal = new LongChannel(getName(), Unit.BANDWIDTH, -1);
			String logString = "OID["+getOidString()+"] "+getName()+" returned: "+tmp;
			retVal.setMessage(logString);
			retVal.setWarning(1);
            Logger.log(logString );
		} else {
			if(getDataType() == SNMPDataType.OTHER){
				setDataType(SNMPUtil.getDataType(var));
			}
			value = getValue(var.toLong());
//			public enum SNMPDataResponseType {STRING,INT,LONG,FLOAT,TICKS,OCTIN,OCTOUT,COUNT};
			retVal = new LongChannel(name, getUnit(), value, getMode());
		}
		return retVal;
	}
	
	//----------------------------------------------------------------------
	public static void addVariableBindings(PDU pdu, Vector<SNMPEntry>  vect){
		OIDHolder tmp;
    	for(SNMPEntry curr :vect){
    		pdu.add(new VariableBinding(curr.getOid()));
    	}
		
	}
	// -----------------------------------------------------------
	public static PDU getPDU(Vector<SNMPEntry> vect){
		PDU retVal = new PDU();
		addVariableBindings(retVal, vect);
    	return retVal;
	}
	
} // SNMPEntry

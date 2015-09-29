package com.paessler.prtg.jmx.sensors.snmp;

import java.util.List;
import java.util.Vector;

import org.snmp4j.PDU;
import org.snmp4j.smi.Null;
import org.snmp4j.smi.OID;
import org.snmp4j.smi.Variable;
import org.snmp4j.smi.VariableBinding;

import com.paessler.prtg.jmx.Logger;
import com.paessler.prtg.jmx.channels.Channel;
import com.paessler.prtg.jmx.channels.Channel.Mode;
import com.paessler.prtg.jmx.channels.FloatChannel;
import com.paessler.prtg.jmx.channels.LongChannel;
import com.paessler.prtg.jmx.channels.Channel.Unit;
import com.paessler.prtg.jmx.sensors.profile.Attribute;
import com.paessler.prtg.jmx.sensors.profile.Profile;
import com.paessler.prtg.util.snmp.OIDHolder;
import com.paessler.prtg.util.snmp.OIDHolder.SNMPDataType;
import com.paessler.prtg.util.snmp.SNMPUtil;

public class SNMPEntry
	extends Attribute<String>
{
    // -------------------------------------------
	@Override
    public String toObjectType(Object objectstr){
//		Integer retVal = Integer.valueOf(objectstr.toString()); 
    	return (objectstr != null ? objectstr.toString() : null);
    }
    // -------------------------------------------
    @Override
    public int compareTo(Attribute<String> other){
    	return object.compareTo(other.getObject());
    }
	
    // -------------------------------------------
    // -------------------------------------------
	public SNMPEntry(SNMPDataType dtype, String name, OID oid, String oidString, String comment){
		super();
		setName(name);
		setDescription(name);
		setOidString(oidString);
		this.oid = oid;		
		setComment(comment);
		setDataType(dtype);
	}
	// -----------------------------------------
	public SNMPEntry(SNMPDataType dtype, String name, String oidString, String descr){
		this(dtype, name, new OID(oidString), oidString, descr);
	}
	// -----------------------------------------
	public SNMPEntry(Attribute<?> attr){
		super(attr);
		setName(attr.getComment());
		setDataType(SNMPDataType.OTHER);
	}
	
	// -----------------------------------------
//	public SNMPEntry(Attribute attribute){
//		this(SNMPDataType.OTHER, attribute.getDescription(), new OID(attribute.getObject()), attribute.getObject(), attribute.getComment());
//	}
	// -----------------------------------------
	public String		name;
	public OID 			oid;
	public SNMPDataType dataType;
	// ------------------------------------------
	public SNMPDataType getDataType() 		{return dataType;}
	// ----------------------------------
	public void setDataType(SNMPDataType dataType) {
		Unit eunit =getUnitEnum();
		switch(dataType){
		case INT:
		case LONG:
			break;
		case OCTIN:
		case OCTOUT:
//			retVal = new LongChannel(name, getUnit(), value, getMode());
			setModeEnum(Mode.COUNTER);
			setUnitEnum(Unit.BANDWIDTH);
			break;
		case TICKS:
			if(eunit != Unit.TIME_SECONDS 
			|| eunit != Unit.TIME_HOURS 
			|| eunit != Unit.TIME_RESPONSE){
				setUnitEnum(Unit.TIME_SECONDS);
				if(getDiv() == 1.0d){
					setDiv(100.0d);
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
	
		case OTHER:
			{
				if(eunit != null){
				  switch(eunit) {
				  		case CPU:
				  			setDataType(SNMPDataType.FLOAT);
				  			setCustomUnit("%");
				  		default:
				  }
				}
			}
			break;
		default:
			break;
			
		}
		this.dataType = dataType;
	}

	// ------------------------------------------
	public String getName() 			{return name;}
	// ----------------------------------
	public void setName(String name) 	{this.name = name;}
	// ----------------------------------
	public String getOidString() 		{return getObject();	}
	// ----------------------------------
	public void setObject(String object) {super.setObject(object);	setOid(new OID(object));}

	public void setOidString(String oidString) {setObject(oidString);}
	// ----------------------------------
	public OID getOid() 				{return oid;}
	// ----------------------------------
	public void setOid(OID oid) 		{this.oid = oid;}
	// ----------------------------------

	// -----------------------------------------------
	public Channel getChannel(Variable var){
		Channel retVal = null;
		long value = -1;
		if(var instanceof Null){
			String tmp = var.toString();
			retVal = new LongChannel(getName(), Unit.BANDWIDTH, -1);
			String logString = "OID["+getObject()+"] "+getName()+" returned: "+tmp;
			retVal.setMessage(logString);
			retVal.setWarning(1);
            Logger.log(logString );
		} else {
			SNMPDataType type = getDataType();
			String valueStr = var.toString();
//			var.
			switch(type){
				case FLOAT:
					{
						float valueFloat = Float.parseFloat(valueStr);
						retVal = super.getChannel(valueFloat);
					}
					break;
				case OTHER:
					setDataType(SNMPUtil.getDataType(var));
				case LONG:
				case INT:
				default:
					retVal = super.getChannel(var.toLong());
			}
		}
		if(retVal != null && retVal.getUnit() == Unit.CUSTOM){
			retVal.setCustomunit(getCustomUnit());
		}
		return retVal;
	}
	
	//----------------------------------------------------------------------
	public static void addVariableBindings(PDU pdu, List<SNMPEntry>  vect){
		OIDHolder tmp;
    	for(SNMPEntry curr :vect){
    		pdu.add(new VariableBinding(curr.getOid()));
    	}
		
	}
	// -----------------------------------------------------------
	public static PDU getPDU(List<SNMPEntry> vect){
		PDU retVal = new PDU();
		addVariableBindings(retVal, vect);
    	return retVal;
	}
	
} // SNMPEntry

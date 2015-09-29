package com.paessler.prtg.util.snmp;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.snmp4j.CommunityTarget;
import org.snmp4j.PDU;
import org.snmp4j.Snmp;
import org.snmp4j.Target;
import org.snmp4j.TransportMapping;
import org.snmp4j.event.ResponseEvent;
import org.snmp4j.mp.SnmpConstants;
import org.snmp4j.smi.Address;
import org.snmp4j.smi.Counter32;
import org.snmp4j.smi.Counter64;
import org.snmp4j.smi.Gauge32;
import org.snmp4j.smi.GenericAddress;
import org.snmp4j.smi.Integer32;
import org.snmp4j.smi.Null;
import org.snmp4j.smi.OID;
import org.snmp4j.smi.OctetString;
import org.snmp4j.smi.TimeTicks;
import org.snmp4j.smi.UdpAddress;
import org.snmp4j.smi.UnsignedInteger32;
import org.snmp4j.smi.Variable;
import org.snmp4j.smi.VariableBinding;
import org.snmp4j.transport.DefaultUdpTransportMapping;

import com.paessler.prtg.util.snmp.OIDHolder.SNMPDataType;

public class SNMPUtil {
	public static enum SNMPCounterType {Counter32bit, Counter64bit};
	public static final String	STARTING_OID = "1.3.6.1.2.1.2.1.0";
	private static TransportMapping<?> transport = null;
	private static Snmp	snmp = null;
	
	private static UdpAddress localUDPAddress = null;
	
	public static UdpAddress getUdpAddress() {return localUDPAddress;}
	public static void setUdpAddress(UdpAddress udpaddr) {localUDPAddress = udpaddr;}
	/** Source and return address of the form "<ip>/<port>" */
	public static void setUdpAddress(String udpaddr) {setUdpAddress(new UdpAddress(udpaddr));}
	// -----------------------------------------------------
	// 1.3.6.1.2.1.1.1.0	Value: Linux TS-870 4.1.2
	public static final String	SYSNAME_ID 		= "hostName";
	public static final String	SYSNAME_OID 	= "1.3.6.1.4.1.9.2.1.3.0";
	public static final String	SYSNAME_STRING	= "Host Name";
	public static final String	IFnNAME_ID 		= "if[%d] ";
	public static final String	IFnNAME_OID 	= "1.3.6.1.2.1.31.1.1.1.1";
	public static final String	IFnNAME_STRING	= "if[%d] ";
	public static final String	IFnMAC_ID 		= "ifMAC";
	public static final String	IFnMAC_OID 		= "3.6.1.2.1.2.2.1.6";
	public static final String	IFnMAC_STRING	= "if[%d] MAC Address";
	
	public static final String	IFnDESCR_ID 	= "Descr";
//	public static final String	IFnDESCR_OID 	= "1.3.6.1.2.1.2.2.1.2";
	public static final String	IFnDESCR_OID 	= "1.3.6.1.4.1.9.2.2.1.1.28"; // Cisco
	public static final String	IFnDESCR_STRING	= "description";
//	public static final String	IFnDESCR_OID 	= "1.3.6.1.2.1.2.2.1.2";
//	public static final String	IFnDESCR_STRING	= "if[%d]";
	// -----------------------------------------------------
//	public static final String	UPTIME_ID 		= "sysORDUpTime";
//	public static final String	UPTIME_OID 		= "1.3.6.1.2.1.1.9.1.4";
	public static final String	UPTIME_IDX 		= "sysUpTime";
	public static final String	UPTIME_OIDX 		= "1.3.6.1.2.1.1.3.0";
	public static final String	UPTIME_STRINGX	= "System Uptime";

	public static final String	UPTIME_ID 		= "hrSystemUptime.0";
	public static final String	UPTIME_OID 		= "1.3.6.1.2.1.25.1.1.0";
	public static final String	UPTIME_STRING	= "System Uptime";
	
	
	
	
	public static final String	IFHCINOCT_OID	= "1.3.6.1.2.1.31.1.1.1.6";
	public static final String	IFHCINOCT_ID	= "HCInOctets";
	public static final String	IFHCINOCT_STRING= "HC Octets In";
	
	public static final String	IFINOCT_OID		= "1.3.6.1.2.1.2.2.1.10";
	public static final String	IFINOCT_ID		= "InOctets";
	public static final String	IFINOCT_STRING	= "Octets In";

	public static final String	IFHCOUTOCT_OID	= "1.3.6.1.2.1.31.1.1.1.10";
	public static final String	IFHCOUTOCT_ID	= "OutOctets";
	public static final String	IFHCOUTOCT_STRING= "HC Octets Out";
	
	public static final String	IFOUTOCT_OID	= "1.3.6.1.2.1.2.2.1.16";
	public static final String	IFOUTOCT_ID		= "OutOctets";
	public static final String	IFOUTOCT_STRING	= "Octets Out";

	public static final String	IFERRORS_ID		= "InErrors";
	public static final String	IFERRORS_OID	= "1.3.6.1.2.1.2.2.1.14";
	public static final String	IFERRORS_STRING	= "Error Packets";

	
	// --------------------------------------------------------------------
	public static OID makeOID(String oidstr, int index){
		OID retVal = new OID(oidstr);
		if(index > 0){
			retVal.append(index);
		}
		return retVal;
	}
	// ------------------------------------------
	public static OIDHolder makeOIDHolder(OIDHolder.SNMPDataType type, String oidstr, String id, String desc, int index){
		OIDHolder retVal = null;
		OID oid = new OID(oidstr);
		String nametmp = id;
		if(index > -1){
			oid.append(index);
			if(nametmp.contains("%")){
				nametmp = String.format(id, index);
			}
			if(desc.contains("%")){
				desc	= String.format(desc, index);
			}
		}
//			retVal = new VariableBinding(oid, nametmp);
		retVal = new OIDHolder(type, nametmp, oid, desc);
		return retVal;
	}
	// ------------------------------------------
	public static OIDHolder makeOIDHolder(OIDHolder.SNMPDataType type, String oidstr, String id, String desc){
		return makeOIDHolder(type, oidstr, id, desc, -1);
	}
	// ------------------------------------------
	// ------------------------------------------
	private static Map<String, OIDHolder> oidMap;
	// ------------------------------------------
	protected static boolean addEntry(OIDHolder entry){
		oidMap.put(entry.getName(), entry);
		oidMap.put(entry.getOidString(), entry);
		return true;
	}
	// ------------------------------------------
	protected static OIDHolder addEntry(String key){
		OIDHolder retVal = null;
		if(key != null){
			retVal = oidMap.get(key);	
		}
		return retVal;
	}
	// ------------------------------------------
	public static OIDHolder getOIDHolder(String key){
		OIDHolder retVal = null;
		if(key != null){
			retVal = oidMap.get(key);	
		}
		return retVal;
	}
	
	
	// ------------------------------------------
	public static String getStringVar(Variable var){
		String retVal = null;
		if(!(var instanceof Null)){
			retVal = var.toString();
		}
		return retVal;
	}
	// ------------------------------------------
	public static Variable getVariableBinding(PDU pdu, OID varoid){
		Variable retVal = null;
		if(pdu != null && varoid != null){
			retVal = pdu.getVariable(varoid);
		}
		return retVal;
	}
	// ------------------------------------------
	public static String getVariableValue(PDU pdu, OID varoid){
		String retVal = null;
		Variable var = getVariableBinding(pdu, varoid);
		if(var != null){
			retVal = var.toString(); 
		}
		return retVal;
	}
	// ------------------------------------------
	public static Variable getVariableBinding(PDU pdu, String varname){
		Variable retVal = null;
		OIDHolder oidh = getOIDHolder(varname);
		if(pdu != null){
			if(oidh != null){
				retVal = getVariableBinding(pdu, oidh.getOid());
			} else {
				retVal = getVariableBinding(pdu, new OID(varname));
			}
		}
		return retVal;
	}
	// ------------------------------------------
	public static String getVariableValue(PDU pdu, String varname){
		String retVal = null;
		Variable var = getVariableBinding(pdu, varname);
		if(var != null){
			retVal = var.toString(); 
		}
		return retVal;
	}

	
	// ------------------------------------------
	// ------------------------------------------
	static{
		oidMap = new LinkedHashMap<String, OIDHolder>();
//		addEntry(getOIDHolder(String oidstr, String id, String desc, int index));
		// -------------------------------------------------------------------
		addEntry(makeOIDHolder(OIDHolder.SNMPDataType.STRING, SYSNAME_OID, 	SYSNAME_ID, 	SYSNAME_STRING));
		OIDHolder tmph = makeOIDHolder(OIDHolder.SNMPDataType.STRING, IFnDESCR_OID,	IFnDESCR_ID,	IFnDESCR_STRING);
		addEntry(tmph);
		tmph.setDescriptionHolder(makeOIDHolder(OIDHolder.SNMPDataType.STRING, IFnNAME_OID, 	IFnNAME_ID, 	IFnNAME_STRING));
		addEntry(tmph.getDescriptionHolder());
		
		// -------------------------------------------------------------------
		addEntry(makeOIDHolder(OIDHolder.SNMPDataType.TICKS,	UPTIME_OID,		UPTIME_ID, 		UPTIME_STRING));
		addEntry(makeOIDHolder(OIDHolder.SNMPDataType.OCTIN,	IFINOCT_OID, 	IFINOCT_ID,		IFINOCT_STRING));
		addEntry(makeOIDHolder(OIDHolder.SNMPDataType.OCTIN,	IFHCINOCT_OID,	IFHCINOCT_ID,	IFHCINOCT_STRING));
		addEntry(makeOIDHolder(OIDHolder.SNMPDataType.OCTOUT,	IFOUTOCT_OID,	IFOUTOCT_ID,	IFOUTOCT_STRING));
		addEntry(makeOIDHolder(OIDHolder.SNMPDataType.OCTOUT,	IFHCOUTOCT_OID,	IFHCOUTOCT_ID,	IFHCOUTOCT_STRING));
		addEntry(makeOIDHolder(OIDHolder.SNMPDataType.COUNT,	IFERRORS_OID,	IFERRORS_ID,	IFERRORS_STRING));
		// -------------------------------------------------------------------
	}
	// -----------------------------------------------------
	public static List<OIDHolder>  getSYSOIDHolder(List<OIDHolder> retVal){
		retVal.add(getOIDHolder(UPTIME_OID));
		return retVal;
	}
	// -----------------------------------------------------
	public static OIDHolder  getIFOIDHolder(int index, SNMPCounterType type){
		OIDHolder retVal = null; 
		OIDHolder meta = null;

		switch(type){
			case  Counter32bit:
				retVal	= makeOIDHolder(OIDHolder.SNMPDataType.STRING, IFnNAME_OID, IFnNAME_ID, IFnNAME_STRING, index);
				meta 	= makeOIDHolder(OIDHolder.SNMPDataType.STRING, IFnDESCR_OID, IFnDESCR_ID, IFnDESCR_STRING, index);
				break;
			case  Counter64bit:
				retVal	= makeOIDHolder(OIDHolder.SNMPDataType.STRING, IFnNAME_OID, IFnNAME_ID, IFnNAME_STRING, index);
				meta 	= makeOIDHolder(OIDHolder.SNMPDataType.STRING, IFnDESCR_OID, IFnDESCR_ID, IFnDESCR_STRING, index);
				break;
		}
		retVal.setDescriptionHolder(meta);
		return retVal;
	}	
	// -----------------------------------------------------
	public static List<OIDHolder>  getIFOIDHolder(List<OIDHolder> retVal, int index){
		retVal.add(makeOIDHolder(OIDHolder.SNMPDataType.OCTIN, 	IFINOCT_OID, 	IFINOCT_ID,		IFINOCT_STRING, index));
		retVal.add(makeOIDHolder(OIDHolder.SNMPDataType.OCTIN,	IFHCINOCT_OID,	IFHCINOCT_ID,	IFHCINOCT_STRING, index));
		retVal.add(makeOIDHolder(OIDHolder.SNMPDataType.OCTOUT, IFOUTOCT_OID,	IFOUTOCT_ID,	IFOUTOCT_STRING, index));
		retVal.add(makeOIDHolder(OIDHolder.SNMPDataType.OCTOUT,	IFHCOUTOCT_OID,	IFHCOUTOCT_ID,	IFHCOUTOCT_STRING, index));
		retVal.add(makeOIDHolder(OIDHolder.SNMPDataType.COUNT,	IFERRORS_OID,	IFERRORS_ID,	IFERRORS_STRING, index));
		return retVal; 
	}
	
	// -----------------------------------------------------
	public static List<OIDHolder>  getIFOIDHolderInst(List<OIDHolder> retVal, int index, SNMPCounterType type, boolean inoutonly){
		if(!inoutonly){
			retVal.add(makeOIDHolder(OIDHolder.SNMPDataType.OCTIN, 	IFINOCT_OID, 	IFINOCT_ID,		IFINOCT_STRING, index));
			retVal.add(makeOIDHolder(OIDHolder.SNMPDataType.OCTOUT, IFOUTOCT_OID,	IFOUTOCT_ID,	IFOUTOCT_STRING, index));
			retVal.add(makeOIDHolder(OIDHolder.SNMPDataType.COUNT,	IFERRORS_OID,	IFERRORS_ID,	IFERRORS_STRING, index));
		} 
		
		switch(type){
			case  Counter32bit:
				retVal.add(makeOIDHolder(OIDHolder.SNMPDataType.OCTIN,	IFINOCT_OID,	IFINOCT_ID,		IFINOCT_STRING, index));
				retVal.add(makeOIDHolder(OIDHolder.SNMPDataType.OCTOUT,	IFOUTOCT_OID,	IFOUTOCT_ID,	IFOUTOCT_STRING, index));
				break;
			case  Counter64bit:
				retVal.add(makeOIDHolder(OIDHolder.SNMPDataType.OCTIN,	IFHCINOCT_OID,	IFHCINOCT_ID,	IFHCINOCT_STRING, index));
				retVal.add(makeOIDHolder(OIDHolder.SNMPDataType.OCTOUT,	IFHCOUTOCT_OID,	IFHCOUTOCT_ID,	IFHCOUTOCT_STRING, index));
				break;
		}
		
		return retVal; 
	}
	

	
	// ------------------------------------------
	public static VariableBinding getOIDVar(String oidstr, String name, int index){
		VariableBinding retVal = null;
		OID oid = new OID(oidstr);
		String nametmp = name;
		if(name.contains("%")){
			oid.append(index);
			nametmp = String.format(name, index);
		}
//			retVal = new VariableBinding(oid, nametmp);
		retVal = new VariableBinding(oid);
		return retVal;
	}
	// -----------------------------------------------------
	public static PDU  getMetaInfoOIDs(PDU retVal, int index){
		retVal.add(getOIDVar(SYSNAME_OID, 	SYSNAME_STRING, index));
		retVal.add(getOIDVar(IFnNAME_OID, 	IFnNAME_STRING, index));
		retVal.add(getOIDVar(IFnDESCR_OID,	IFnDESCR_STRING, index));
		return retVal; 
	}
	// -----------------------------------------------------
	public static PDU  getMetaInfoOIDs(int index){
		PDU retVal = new PDU();
		return getMetaInfoOIDs(retVal, index);
	}
	// -----------------------------------------------------
	public static PDU  getIFInfoOIDs(PDU retVal, int index){
		retVal.add(getOIDVar(UPTIME_OID, 	UPTIME_STRING, index));
		retVal.add(getOIDVar(IFINOCT_OID, 	IFINOCT_STRING, index));
		retVal.add(getOIDVar(IFHCINOCT_OID, IFINOCT_STRING, index));
		retVal.add(getOIDVar(IFOUTOCT_OID,	IFOUTOCT_STRING, index));
		retVal.add(getOIDVar(IFHCOUTOCT_OID,IFHCOUTOCT_STRING, index));
		retVal.add(getOIDVar(IFERRORS_OID,	IFERRORS_STRING, index));
		return retVal; 
	}
	// -----------------------------------------------------
	public static List<VariableBinding>  getIFInfoOIDs(List<VariableBinding> retVal, int index){
		retVal.add(getOIDVar(UPTIME_OID, 	UPTIME_STRING, index));
		retVal.add(getOIDVar(IFINOCT_OID, 	IFINOCT_STRING, index));
		retVal.add(getOIDVar(IFHCINOCT_OID, IFINOCT_STRING, index));
		retVal.add(getOIDVar(IFOUTOCT_OID,	IFOUTOCT_STRING, index));
		retVal.add(getOIDVar(IFHCOUTOCT_OID,IFHCOUTOCT_STRING, index));
		retVal.add(getOIDVar(IFERRORS_OID,	IFERRORS_STRING, index));
		return retVal; 
	}
	// -----------------------------------------------------
	public static PDU  getIFInfoOIDs(int index){
		PDU retVal = new PDU();
		return getIFInfoOIDs(retVal, index);
	}
	 // -----------------------------------------------------
	//
	public static boolean isListning(){return transport != null;}
	protected static TransportMapping<?> getTransportMapping()
	   throws IOException
	{
		TransportMapping<?> retVal = transport;
		if(retVal == null){
			try {
				if(getUdpAddress() != null){
					retVal = new DefaultUdpTransportMapping(getUdpAddress());
				} else {
					retVal = new DefaultUdpTransportMapping();
				}
			} catch (IOException e) {
				System.out.println("SNMPUtil.getTransportMapping(): Failed to create Transport Mapping "+getUdpAddress());
				throw e;
			}
			transport = retVal;
		}
		return retVal;
	}
	// ---------------------------------------------------------
//	public 
	public static void startSNMPListen(){
		if(!isListning()){
			try {
		        transport = getTransportMapping();
		        snmp = new Snmp(transport);
		        // Do not forget this line!
		        transport.listen();
			} catch (IOException e) {
				System.out.println("SNMPUtil.startSNMPListen(): Failed to create Transport Mapping "+getUdpAddress());
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	 // -----------------------------------------------------
	public static void stopSNMPListen() {
		if(isListning()){
			try {
				snmp.close();
				transport.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	        snmp = null;
			transport = null;
		}
	}
	
	 // -----------------------------------------------------
	public static boolean init(){
		startSNMPListen();
		return isListning();
	}
	 // -----------------------------------------------------
	public static int getVersion(String versionstring){
		int retVal = SnmpConstants.version2c;
		if(versionstring != null){
			versionstring = versionstring.trim();
//			if(versionstring.contains('2'))){
//				retVal = SnmpConstants.version2c;
//			} else 
				if("1".equals(versionstring)){
				retVal = SnmpConstants.version1;
			} else 
				if("3".equals(versionstring)){
				retVal = SnmpConstants.version3;
			}  
		}
		return retVal;
	}
	 // -----------------------------------------------------
	public static Target getTarget(String address, String community, int version) {
		Address targetAddress = GenericAddress.parse(address);
		CommunityTarget target = new CommunityTarget();
		target.setCommunity(new OctetString(community));
		target.setAddress(targetAddress);
		target.setRetries(2);
		target.setTimeout(1500);
		target.setVersion(version);
		return target;
	}	
	public static Target getTarget(String protocol, String host, String port, String community, int version) {
		StringBuilder targetaddress = new StringBuilder();
		targetaddress.append((protocol != null && !protocol.isEmpty()? protocol : "udp"));
		targetaddress.append(":");
		targetaddress.append(host);
		targetaddress.append("/");
		targetaddress.append(port);
		return SNMPUtil.getTarget(targetaddress.toString(), community, version);
	}
	 // -----------------------------------------------------
	public static ResponseEvent get(Target target, PDU pdu) throws IOException {
		ResponseEvent retVal = null;
		if(pdu != null){
		 	pdu.setType(PDU.GET);
		 	retVal = snmp.send(pdu, target, null);
			if(retVal == null) {
				throw new RuntimeException("GET timed out");
			}
		}
		return retVal;	
	}	
	 // -----------------------------------------------------
	public static ResponseEvent getNoThrow(Target target, PDU pdu) {
		ResponseEvent retVal = null;
		try {
			retVal = get(target, pdu);
		} catch (Exception e) {
		}
		return retVal;	
	}
	 // -----------------------------------------------------
	public static ResponseEvent get(Target target, OID oids[]) throws IOException {
		ResponseEvent retVal = null;
		PDU pdu = new PDU();
	 	for (OID oid : oids) {
	 	     pdu.add(new VariableBinding(oid));
	 	}
		return get(target, pdu);	
	}	

	// -----------------------------------------------------
	public static List<VariableBinding> walk(Target target, OID oid) {
        List<VariableBinding> ret = new ArrayList<VariableBinding>();

        PDU requestPDU = new PDU();
        requestPDU.add(new VariableBinding(oid));
        requestPDU.setType(PDU.GETNEXT);
        boolean finished = false;
        try{
            while (!finished) {
                VariableBinding vb = null;

                ResponseEvent respEvt = snmp.send(requestPDU, target);
                PDU responsePDU = respEvt.getResponse();
                if (responsePDU != null) {
                    vb = responsePDU.get(0);
                }

                if (responsePDU == null) {
                    finished = true;
                } else if (responsePDU.getErrorStatus() != 0) {
                    finished = true;
                } else if (vb.getOid() == null) {
                    finished = true;
                } else if (vb.getOid().size() < oid.size()) {
                    finished = true;
                } else if (oid.leftMostCompare(oid.size(), vb.getOid()) != 0) {
                    finished = true;
                } else if (Null.isExceptionSyntax(vb.getVariable().getSyntax())) {
                    finished = true;
                } else if (vb.getOid().compareTo(oid) <= 0) {
                    finished = true;
                } else {
                    ret.add(vb);

                    // Set up the variable binding for the next entry.
                    requestPDU.setRequestID(new Integer32(0));
                    requestPDU.set(0, vb);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ret;
    }	
	// -----------------------------------------------------
	public static String getAsString(Target target, OID oid) throws IOException {
		ResponseEvent event = get(target, new OID[]{oid});
		return event.getResponse().get(0).getVariable().toString();
	}
	
	// -----------------------------------------------------
	public static void dump(List<? extends VariableBinding> list){
		int idx = 0;
		for(VariableBinding var: list){
			System.out.println(++idx+ "\t" +var.getOid()+ "\t" +var.getVariable());
		}
	}
	
	// -----------------------------------------------------
	public static SNMPDataType getDataType(Variable var){
		SNMPDataType retVal = SNMPDataType.INT;
/*		
 * 	org.snmp4j.smi.Variable implementations
 * 		AbstractVariable, BitString, 
 * 		Counter32, Counter64, Gauge32, Integer32, UnsignedInteger32, 
 * 		TimeTicks, 
 * 		OctetString,
 * 		GenericAddress, IpAddress, Null, OID, Opaque, SMIAddress, SshAddress, TcpAddress, TlsAddress, 
 * 		TransportIpAddress, TsmSecurityParameters, UdpAddress, VariantVariable
 * 	SNMPDataType: STRING,INT,LONG,FLOAT,TICKS,OCTIN,OCTOUT,COUNT, OTHER
 */
		if(var instanceof TimeTicks) {
			retVal = SNMPDataType.TICKS;
		} else if(var instanceof Counter64 || var instanceof Counter32) {
			retVal = SNMPDataType.COUNT;
		} else if(var instanceof Integer32 || var instanceof UnsignedInteger32) {
			retVal = SNMPDataType.COUNT;
		} else if(var instanceof Gauge32) {
			retVal = SNMPDataType.COUNT;
		} else if(var instanceof OctetString) {
			retVal = SNMPDataType.STRING;
		}
		return retVal;
	}
	// -----------------------------------------------------
	public static Map<OID, Variable> convertToMap(PDU pdu){
		Map<OID, Variable> retVal = null;
		if(pdu != null){
			retVal = new HashMap<OID, Variable>();
			Vector<? extends VariableBinding> vect = pdu.getVariableBindings();
			for(VariableBinding curr : vect){
				retVal.put(curr.getOid(), curr.getVariable());
			}
		}
		return retVal;
	}
	// -----------------------------------------------------
	public static void dump(PDU pdu){
		if(pdu != null){
			System.out.println("PDU:\t" +pdu.toString());
			dump(pdu.getVariableBindings());
		} else { 
			System.out.println("PDU:\t IS NULL/ No response");
		}
	}
	// -----------------------------------------------------
	public static void dump(ResponseEvent resp){
		dump(resp.getResponse());
	}
	
	// device=1.3.6.1.2.1.2.2.1.3.1,status=1.3.6.1.2.1.2.2.1.8.1,unit64=1.3.6.1.2.1.31.1.1.1.15.1
	// device=1.3.6.1.2.1.2.2.1.3.2,status=1.3.6.1.2.1.2.2.1.8.2,unit64=1.3.6.1.2.1.31.1.1.1.15.2
	// device=1.3.6.1.2.1.2.2.1.3.3,status=1.3.6.1.2.1.2.2.1.8.3,unit64=1.3.6.1.2.1.31.1.1.1.15.3
	// device=1.3.6.1.2.1.2.2.1.3.4,status=1.3.6.1.2.1.2.2.1.8.4,unit64=1.3.6.1.2.1.31.1.1.1.15.4
	// device=1.3.6.1.2.1.2.2.1.3.5,status=1.3.6.1.2.1.2.2.1.8.5,unit64=1.3.6.1.2.1.31.1.1.1.15.5
	// device=1.3.6.1.2.1.2.2.1.3.6,status=1.3.6.1.2.1.2.2.1.8.6,unit64=1.3.6.1.2.1.31.1.1.1.15.6
	// device=1.3.6.1.2.1.2.2.1.3.7,status=1.3.6.1.2.1.2.2.1.8.7,unit64=1.3.6.1.2.1.31.1.1.1.15.7
//    OID oids[] = {new OID("1.3.6.1.2.1.31.1.1.1.6.1"),new OID("1.3.6.1.2.1.31.1.1.1.10.1"),
//    			  new OID("1.3.6.1.2.1.2.2.1.3.1"),new OID("1.3.6.1.2.1.2.2.1.8.1"),new OID("1.3.6.1.2.1.31.1.1.1.15.1")};
	// -----------------------------------------------------
	public static ResponseEvent testGetVect(Target target){
		ResponseEvent retVal = null;
        OID oids[] = {new OID("1.3.6.1.2.1.1.5"), new OID("1.3.6.1.2.1.31.1.1.1.6.1"),new OID("1.3.6.1.2.1.31.1.1.1.10.1"),
  			  new OID("1.3.6.1.2.1.2.2.1.3.1"),new OID("1.3.6.1.2.1.2.2.1.8.1"),new OID("1.3.6.1.2.1.31.1.1.1.15.1")};
  
	  try {
		  retVal = get(target, oids);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
      	dump(retVal);
		return retVal;
	}
	// -----------------------------------------------------
	public static ResponseEvent testGetMeta(Target target, int index){
		ResponseEvent retVal = null;
		PDU pdu = getMetaInfoOIDs(index);
	  try {
		  retVal = get(target, pdu);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
      	dump(retVal);
		return retVal;
	}
	// -----------------------------------------------------
	public static ResponseEvent testGetVect(Target target, int index){
		ResponseEvent retVal = null;
		PDU pdu = getIFInfoOIDs(index);
	  try {
		  retVal = get(target, pdu);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
      	dump(retVal);
		return retVal;
	}
	// -----------------------------------------------------
	public static void testWalk(Target target){
		List<? extends VariableBinding> ret = walk(target, new OID("1.3.6.1.2.1.31.1.1.1.1"));
		dump(ret);
	}
	
	// -----------------------------------------------------
    public static void main(String[] args) {
    	startSNMPListen();
    	Target target = SNMPUtil.getTarget("udp:192.168.0.5/161", "public-win", SnmpConstants.version2c);
    	ResponseEvent resp = null;
//    	resp = testGetVect(target);
    	testGetMeta(target, 2);
    	testGetVect(target, 2);
//    	testWalk(target);
        
    	stopSNMPListen();    	
    }	
}

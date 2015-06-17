package com.paessler.prtg.util.net;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.UnknownHostException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * @author  JR Andreassen
 */
public abstract class Net_Const 
{
	// -----------------------------------------------------------------------------
	public static final String	NET_LOCAL_HOST	= "localhost";
	public static final String	NET_LOCAL_HOST_IP	= "127.0.0.1";
	public static InetAddress		MY_INET_ADDRESS = null;

	//http://stackoverflow.com/questions/106179/regular-expression-to-match-hostname-or-ip-address
	//public static String VALID_IP_ADDR_REGEX = "^(([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])\\.){3}([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])$";

	// -----------------------------------------------------------------------------
	static
	{
		try
		{
			MY_INET_ADDRESS = InetAddress.getLocalHost();
//			System.out.println("My IP: "+myinetaddr.getHostAddress()+" ["+myinetaddr.toString()+"]\n");
		}
		catch(Exception e)
		{
			System.out.println("Exception: "+e);
		}
	}
	
	private static Map<String, String> resolvedHost2AddressMap = new HashMap<String, String>();

	////////////////////////////////////////////////////////////////////
	/**
	 * returns true iff the host passed in is the local host. The host can either be an IP or name
	 */
	public static boolean isLocalHost(String hostNameOrIP)
	{
		//for perfomance, return right away if we get 127.0.0.1 or localhost passed in
		if( NET_LOCAL_HOST.equalsIgnoreCase(hostNameOrIP) || NET_LOCAL_HOST_IP.equals(hostNameOrIP) )
			return true;

		//otherwise, resolve an IP address from the name passed in, and loop through all the local interfaces to see if any of them match it
		try
		{
			//cache the addresses once we have them, so it doesnt have to try a dns lookup everytime which often fails when the conn is down
			String addrToCheck = resolvedHost2AddressMap.get(hostNameOrIP);
			if( addrToCheck == null )
			{
				//InetAddress.getByName should accept an IP or name
				InetAddress inetAddressToCheck = InetAddress.getByName(hostNameOrIP);
				//get the IP address
				addrToCheck = inetAddressToCheck.getHostAddress();
				if( addrToCheck != null )
					resolvedHost2AddressMap.put(hostNameOrIP, addrToCheck);
			}
			
			if( addrToCheck == null )
			{
				System.err.println("isLocalHost: could not find an address");
				return false;
			}
			
			//loop through all the network interfaces and see if any of the IPs match
			Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
			while (interfaces.hasMoreElements())
			{
				NetworkInterface networkInterface = interfaces.nextElement();
				Enumeration<InetAddress> addresses = networkInterface.getInetAddresses();
				while (addresses.hasMoreElements())
				{
					InetAddress inetAddress = addresses.nextElement();
					//String hostName = inetAddress.getHostName();
					String hostAddr = inetAddress.getHostAddress();

					if( addrToCheck.equals(hostAddr) )
						return true;

					//System.err.println("hostName->" + hostName + ", hostAddr->" + hostAddr);
					//hostName->localhost, hostAddr->127.0.0.1
					//hostName->THPDEVL11.TLE.DPS, hostAddr->172.30.44.30
					//hostName->THPDEVL11.TLE.DPS, hostAddr->166.128.120.98
					//hostName->THPDEVL11.TLE.DPS, hostAddr->170.193.37.80
				}
			}
		}
		catch (UnknownHostException ex)
		{
			System.err.println("ex->" + ex);
			//ex.printStackTrace();
			return false;
		}
		catch(java.net.SocketException se)
		{
			System.err.println("se->" + se);
			//se.printStackTrace();
			return false;
		}
		catch(Exception e)
		{
			System.err.println("e->" + e);
			//e.printStackTrace();
			return false;
		}

		return false;
	}//method

	////////////////////////////////////////////////////////////////////
	/**
	 *
	 */
	/*public void getInetAddress
	{

	}//method*/

	public static void main(String[] args) throws Exception
	{

		/*InetAddress addr = InetAddress.getByName("HQTHPDBAIS");
		System.err.println("addr->" + addr.getHostName() + "  ADDR:"  + addr.getHostAddress() + " LB->" + addr.isLoopbackAddress());
		byte[] addrArray = {127, 0, 0, 1};
		//byte[] addrArray = {1, 0, 0, 127};
		addr = InetAddress.getByAddress(addrArray);
		System.err.println("addr->" + addr.getHostName() + "  ADDR:"  + addr.getHostAddress() + " LB->" + addr.isLoopbackAddress());

		addr = InetAddress.getByName("127.0.0.1");
		System.err.println("127.0.0.1 addr->" + addr.getHostName() + "  ADDR:"  + addr.getHostAddress() + " LB->" + addr.isLoopbackAddress());

		addr = InetAddress.getByName("170.193.37.80");
		System.err.println("170.193.37.80 addr->" + addr.getHostName() + "  ADDR:"  + addr.getHostAddress() + " LB->" + addr.isLoopbackAddress());

		addr = InetAddress.getByName("172.30.44.30");
		System.err.println("172.30.44.30 addr->" + addr.getHostName() + "  ADDR:"  + addr.getHostAddress() + " LB->" + addr.isLoopbackAddress());

		addr = InetAddress.getByName("166.128.120.98");
		System.err.println("166.128.120.98 addr->" + addr.getHostName() + "  ADDR:"  + addr.getHostAddress() + " LB->" + addr.isLoopbackAddress());

		addr = InetAddress.getByName("localhost");
		System.err.println("localhost addr->" + addr.getHostName() + "  ADDR:"  + addr.getHostAddress() + " LB->" + addr.isLoopbackAddress());

		addr = InetAddress.getByName("THPDEVL11");
		System.err.println("THPDEVL11 addr->" + addr.getHostName() + "  ADDR:"  + addr.getHostAddress() + " LB->" + addr.isLoopbackAddress());

		addr = InetAddress.getByName("THPDEVL11.TLE.DPS");
		System.err.println("THPDEVL11.TLE.DPS addr->" + addr.getHostName() + "  ADDR:"  + addr.getHostAddress() + " LB->" + addr.isLoopbackAddress());
		*/

		/*Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
		while (interfaces.hasMoreElements())
		{
			NetworkInterface networkInterface = interfaces.nextElement();
			Enumeration<InetAddress> addresses = networkInterface.getInetAddresses();
			while (addresses.hasMoreElements())
			{
				InetAddress inetAddress = addresses.nextElement();
				String hostName = inetAddress.getHostName();
				String hostAddr = inetAddress.getHostAddress();

				System.err.println("hostName->" + hostName + ", hostAddr->" + hostAddr);
				//System.err.println("THPDEVL11 addr->" + addr.getHostName() + "  ADDR:"  + addr.getHostAddress() + " LB->" + addr.isLoopbackAddress());
			}
		}*/

		//java.net.UnknownHostException
		//addr = InetAddress.getByName("BLAAAA");
		//System.err.println("BLAAAA addr->" + addr.getHostName() + "  ADDR:"  + addr.getHostAddress() + " LB->" + addr.isLoopbackAddress());

		/*try
		{
			InetAddress[] localAddresses = InetAddress.getAllByName(NET_LOCAL_HOST);
			for( int i=0; i < localAddresses.length; i++ )
			{
				System.err.println("ooo->" + localAddresses[i].getAddress());
				for( int j =0; j < localAddresses[i].getAddress().length; j++ )
				{
					System.err.println("part->" + localAddresses[i].getAddress()[j]);
				}
			}//for
		}
		catch(java.net.UnknownHostException uhe)
		{
			System.err.println("uhe->" + uhe);
			uhe.printStackTrace();
		}*/

		InetAddress addr = InetAddress.getByName("thpdevl01");
		System.err.println("THPDEVL11 addr->" + addr.getHostName() + "  ADDR:"  + addr.getHostAddress() + " LB->" + addr.isLoopbackAddress());

		System.err.println("isLocalHost(localhost)->" + isLocalHost("localhost"));
		System.err.println("isLocalHost(127.0.0.1)->" + isLocalHost("127.0.0.1"));
		System.err.println("isLocalHost(170.193.37.80)->" + isLocalHost("170.193.37.80"));
		System.err.println("isLocalHost(172.30.44.30)->" + isLocalHost("172.30.44.30"));
		System.err.println("isLocalHost(166.128.120.98)->" + isLocalHost("166.128.120.98"));
		System.err.println("isLocalHost(THPDEVL11)->" + isLocalHost("THPDEVL11"));
		System.err.println("isLocalHost(thpdevl11)->" + isLocalHost("thpdevl11"));
		System.err.println("isLocalHost(THPDEVL11.TLE.DPS)->" + isLocalHost("THPDEVL11.TLE.DPS"));

		System.err.println("isLocalHost(201.1.1.0)->" + isLocalHost("201.1.1.0"));
		System.err.println("isLocalHost(bla)->" + isLocalHost("bla"));

	}

} // 

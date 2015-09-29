/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.paessler.prtg.util.net;

import java.security.KeyStore;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;
import javax.security.auth.x500.X500Principal;

/*
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
*/
/**
 *
 * @author JR Andreassen
 */
// http://javaskeleton.blogspot.in/2010/07/avoiding-peer-not-authenticated-with.html
public class TrustManagerLocal 
				implements X509TrustManager
{

	private static X509TrustManager sunJSSEX509TrustManager; 
	private static TrustManagerLocalHostnameVerifier	trustManagerLocalHostVerifier;
	private static TrustManagerLocal trustManagerLocalinstance;
	private static Set<String> verifiedHostList;

	static
	{
		trustManagerLocalinstance = null;
		trustManagerLocalHostVerifier = null;
		verifiedHostList = new HashSet<String>();
		initTrustManager();
	};
	//--------------------------------------------------------------------------------------
	//--------------------------------------------------------------------------------------
	public static void initTrustManager()
	{
		TrustManagerFactory tmf = null;
		try
		{
			tmf = TrustManagerFactory.getInstance("SunX509", "SunJSSE");
			tmf.init((KeyStore)null);
			TrustManager tms[] = tmf.getTrustManagers();

			/*  
			 * Iterate over the returned trustmanagers, look  
			 * for an instance of X509TrustManager.  If found,  
			 * use that as our "default" trust manager.  
			 */
			for( int i = 0; i < tms.length; i++ )
			{
				if( tms[i] instanceof X509TrustManager )
				{
					sunJSSEX509TrustManager = (X509TrustManager) tms[i];
					break;
				}
			}

		}
		catch(Exception e)
		{
//			Log.printInfo("initTrustManager()" +e);
			System.out.println("initTrustManager()" +e);
		}

	}

	// --------------------------------------------------------------------------
	public static boolean addVerifiedHost(String host)
	{
		boolean retVal = false;
		if(host != null)
		{
			verifiedHostList.add(host);
			retVal = true;
		}
		return retVal;
	}
	
	public static void addVerifiedHosts(Collection<String> hosts)
	{
		if( hosts != null )
			verifiedHostList.addAll(hosts);
	}
	
	// --------------------------------------------------------------------------
	public static X509TrustManager getInstance()
	{
		if(trustManagerLocalinstance == null)
		{
			try
			{
				trustManagerLocalHostVerifier = new TrustManagerLocalHostnameVerifier();
				trustManagerLocalinstance = trustManagerFactory();

				addVerifiedHost("COMTRNOA1");
				addVerifiedHost("apps.dot.state.tx.us");
			}
			catch (NoSuchAlgorithmException ex)
			{
				Logger.getLogger(TrustManagerLocal.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
		return trustManagerLocalinstance;
	}

	//--------------------------------------------------------------------------------------
	protected static TrustManagerLocal trustManagerFactory()
					throws NoSuchAlgorithmException
	{
		TrustManagerFactory tmf = null; 
//		SSLContext ctx = SSLContext.getInstance("JKS");
		SSLContext ctx = SSLContext.getInstance("TLS");
		TrustManagerLocal retVal = new TrustManagerLocal();
 // Install the all-trusting trust manager
		try
		{
			tmf = TrustManagerFactory.getInstance("SunX509", "SunJSSE");
			SSLSocketFactory sslsf =	HttpsURLConnection.getDefaultSSLSocketFactory();
//			sslsf.
//			ctx.init(null, new TrustManager[]{retVal}, null);
			ctx.init(null, new TrustManager[]{retVal}, new SecureRandom());
//			SSLSocketFactory ssf = HttpsURLConnection.getDefaultSSLSocketFactory();
			HttpsURLConnection.setDefaultHostnameVerifier(trustManagerLocalHostVerifier);
			HttpsURLConnection.setDefaultSSLSocketFactory(ctx.getSocketFactory());

		}
		catch (Exception e) {}

//		SSLSocketFactory ssf = new SSLSocketFactory(ctx);
//		ssf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);




		return retVal;
	}

// --------------------------------------------------------------------------------------
// --------------------------------------------------------------------------------------
// --------------------------------------------------------------------------------------
public static  class TrustManagerLocalHostnameVerifier
				implements HostnameVerifier
{

	        public boolean verify(String hostname)
					{
						boolean retVal = false;
						if (hostname.startsWith("LOCALHOST"))
						{	retVal = true;}
//							else if(hostname.startsWith("COMM"))
//							{	retVal = true;}
						else if(verifiedHostList.contains(hostname))
						{	retVal = true;}

	            return retVal;
	        }
					// ------------------------------------------------
	        @Override
					public boolean verify(String hostnameFQN,
	                javax.net.ssl.SSLSession sslSession)
					{
						boolean retVal = false;
						String hostname = hostnameFQN.toUpperCase();
						retVal = verify(hostname);
						if(!retVal)
						{
							int dot_idx = hostname.indexOf('.');
							if(dot_idx != -1)
							{	hostname = hostname.substring(0, dot_idx);}
							retVal = verify(hostname);
						}
	          return retVal;
	        }
}
// --------------------------------------------------------------------------------------
// --------------------------------------------------------------------------------------
	// --------------------------------------------------------------------------
	@Override
	public void checkClientTrusted(X509Certificate[] xcs, String string)
					throws CertificateException
			{
//				Log.printInfo("checkClientTrusted("+xcs+", "+string+")");
			if(sunJSSEX509TrustManager != null)
			{ sunJSSEX509TrustManager.checkClientTrusted(xcs, string);}
//				super.checkClientTrusted(xcs, string);
			}
	// --------------------------------------------------------------------------
	@Override
	public X509Certificate[] getAcceptedIssuers()
	{
		X509Certificate[] retVal = new X509Certificate[0];
		if(sunJSSEX509TrustManager != null)
		{ retVal = sunJSSEX509TrustManager.getAcceptedIssuers();}
		return retVal;
	}

	// --------------------------------------------------------------------------
	public boolean checkPreVerified(String name)
	{
		boolean retVal = false;

		return retVal;
	}
	// --------------------------------------------------------------------------
	public boolean checkx500Name(String name)
	{
		boolean retVal = false;
				String tmpHostName = name.toUpperCase();
				for(String curr : verifiedHostList)
				{
					if(tmpHostName.contains(curr))
					{
						retVal = true;
						break;
					}
				}
		return retVal;
	}
	// --------------------------------------------------------------------------
	@Override
	public void checkServerTrusted(X509Certificate[] xcs, String string)
						throws CertificateException
	{
		boolean success = false;
		try
		{
			if(sunJSSEX509TrustManager != null)
			{sunJSSEX509TrustManager.checkServerTrusted(xcs, string);}
			return;
		}
		catch(Exception e)
		{
		}
				X509Certificate cs = null;
				if(xcs != null)
				{	cs = xcs[0];}
				if(cs != null)
				{
//					Principal p = cs.getSubjectDN();
					Collection<List<?>> c = cs.getIssuerAlternativeNames();
					X500Principal x500p = cs.getIssuerX500Principal();
					String x500N = x500p.getName();
					if(x500N.contains("DC=TLE,DC=DPS"))
					{	success = true;}
					else if(x500N.contains("OU=TXDPS,O=TLE"))
					{	success = true;}
// 					Principal iDN = cs.getIssuerDN();
//					cs.
//					if(cs.checkValidity())
					if(!success)
						success = checkPreVerified(x500N);
					if(!success)
						success = checkx500Name(x500N);
				}

//				super.checkServerTrusted(xcs, string);
				if (!success)
				{
//					Log.printInfo("checkServerTrusted("+xcs+", "+string+") Fail");
					System.out.println("checkServerTrusted("+xcs+", "+string+") Fail");
					throw new CertificateException();
				}
	}
//---------------------------------------------------
/*	
	public static void doURI(URI uri)
	{
		try
		{
			
//			ResponseInputStream ris = URLUtil.getURLStream(uri, int connectionTimeout, int readTimeout, int retryAttempts)
			ResponseInputStream ris = URLUtil.getURLStream(uri.toURL());
			StreamUtility.echoStream(ris, System.out);

		}
		catch(Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
//---------------------------------------------------
	public static void doURI(String uri)
	{
		try
		{
			
//			ResponseInputStream ris = URLUtil.getURLStream(uri, int connectionTimeout, int readTimeout, int retryAttempts)
			ResponseInputStream ris = URLUtil.getURLStream(URLUtil.getURL(uri));
			StreamUtility.echoStream(ris, System.out);

		}
		catch(Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
// ---------------------------------------------------
	public static void main (String[] args)
	{
			TrustManagerLocal inst = (TrustManagerLocal)TrustManagerLocal.getInstance();
//			TrustManagerLocal.trustManagerLocalHostVerifier.verify("comtrnoa1.tle.dps", null);
//			TrustManagerLocal.trustManagerLocalHostVerifier.verify("comtrnoa1.tle", null);
//			TrustManagerLocal.trustManagerLocalHostVerifier.verify("comtrnoa1.", null);
			
			doURI("https://COMMCVE:50443/comm/inq/IC_Ping.xml?radioNumber=0909");
			doURI("https://COMTRNOA1:50443/comm/inq/IC_Ping.xml?radioNumber=0909");
			doURI("https://apps.dot.state.tx.us/apps/dpsinquiry/dps_search_process.asp?searchtype=cert_cert&searchdata=000020443C&echo=IQZY245632400017");
			doURI("https://ondemand.ufcu.org");
	}
*/	
}

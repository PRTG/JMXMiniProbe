package com.paessler.prtg.util.net.http;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLEncoder;

import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.AuthCache;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpHead;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.client.BasicAuthCache;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;

public class HTTPUtil {
	// ---------------------------------------------------------------
	public enum Method{POST,GET,HEAD};
	public enum Authentication{NONE,BASIC,DIGEST};
	// ----------------------------
	public static CredentialsProvider  getCredentialProvider(Authentication auth, URI uri, String uid, String pwd){
        CredentialsProvider retVal = new BasicCredentialsProvider();
        AuthScope authscope = new AuthScope(uri.getHost(), uri.getPort());
		switch(auth){
		// https://hc.apache.org/httpcomponents-client-ga/httpclient/examples/org/apache/http/examples/client/ClientPreemptiveDigestAuthentication.java
		// test cases: http://httpbin.org/ 
/*			case DIGEST:{
	            // Generate DIGEST scheme object, initialize it and add it to the local
	            // auth cache
	            DigestScheme digestAuth = new DigestScheme();
	            // Suppose we already know the realm name
	            digestAuth.overrideParamter("realm", "some realm");
	            // Suppose we already know the expected nonce value
	            digestAuth.overrideParamter("nonce", "whatever");
				}
*/			case BASIC:
				retVal.setCredentials(authscope,
		                new UsernamePasswordCredentials(uid, pwd));
				break;
		}
		return retVal;
	}
	// ----------------------------------------------------------
	private static AuthCache myauthcache = null;
	public static AuthCache getAuthCache(){
		AuthCache retVal = myauthcache;
		if(myauthcache == null){
			retVal = myauthcache = new BasicAuthCache();
		}
		return retVal;
	}
	// ----------------------------------------------------------
	public static CloseableHttpResponse execute(CloseableHttpClient cli, HttpUriRequest  req) throws ClientProtocolException, IOException{
		CloseableHttpResponse retVal = null;
        // Add AuthCache to the execution context
        HttpClientContext localContext = HttpClientContext.create();
        localContext.setAuthCache(getAuthCache());
 
		retVal = cli.execute(req, localContext);
        return retVal;
	}
	
	// ---------------------------------------------------------------
	public static HttpClientBuilder  getCloseableHttpClientBuilder(Authentication auth, URI uri, String uid, String pwd){
		HttpClientBuilder retVal = null;
		if(retVal == null){
			retVal =  HttpClients.custom()
//					.setConnectionManager(mgr)
//					.setConnectionManagerShared(true)
	                .setDefaultCredentialsProvider(getCredentialProvider(auth, uri, uid, pwd));
		}
		return retVal;
	}
	// ---------------------------------------------------------------
	public static HttpClientBuilder  getCloseableHttpClientBuilder(PoolingHttpClientConnectionManager mgr, Authentication auth, URI uri, String uid, String pwd){
		HttpClientBuilder retVal = getCloseableHttpClientBuilder(auth, uri, uid, pwd);
		if(retVal != null){
			retVal = retVal.setConnectionManager(mgr)
					.setConnectionManagerShared(true);
		}
		return retVal;
	}
	// ---------------------------------------------------------------
	public static CloseableHttpClient  getCloseableHttpClient(Authentication auth, URI uri, String uid, String pwd){
		CloseableHttpClient retVal = null;
		HttpClientBuilder builder = getCloseableHttpClientBuilder(auth, uri, uid, pwd);
		if(builder != null){
			retVal =  builder.build();
		}
		return retVal;
	}
	// ---------------------------------------------------------------
	public static CloseableHttpClient  getCloseableHttpClient(PoolingHttpClientConnectionManager mgr, Authentication auth, URI uri, String uid, String pwd){
		CloseableHttpClient retVal = null;
		HttpClientBuilder builder = getCloseableHttpClientBuilder(mgr, auth, uri, uid, pwd);
		if(builder != null){
			retVal =  builder.build();
		}
		return retVal;
	}
	// ---------------------------------------------------------------
	// ---------------------------------------------------------------
	public static HttpUriRequest   getHttpRequest(URI uri, Method method){
		HttpUriRequest retVal = null;
		switch(method){
			case GET:
				retVal = new HttpGet(uri);
				break;
			case POST:{
					HttpPost tmp = new HttpPost(uri);
//					tmp.setData
					retVal = tmp;
				}
				break;
			case HEAD:
				retVal = new HttpHead(uri);
				break;
		}
		return retVal;
	}
	
	//	----------------------------------------------------------------------------------------------------
	//	----------------------------------------------------------------------------------------------------
	//	----------------------------------------------------------------------------------------------------
    // -------------------------------------------------------------------------------
    public static String encode(String string) {
        try {
            String ret = URLEncoder.encode(string, "utf-8");
            return ret;
        } catch (UnsupportedEncodingException e) {
            return string;
        }
    }

}

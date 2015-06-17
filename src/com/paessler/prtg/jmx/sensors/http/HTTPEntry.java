/*
 * Copyright (c) 2015, Paessler AG <support@paessler.com>
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 * 3. Neither the name of the copyright holder nor the names of its contributors
 *    may be used to endorse or promote products derived from this software without
 *    specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
 * ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.paessler.prtg.jmx.sensors.http;

import java.net.URI;
import java.net.URISyntaxException;

import org.apache.http.HttpHost;
import org.apache.http.client.AuthCache;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.BasicAuthCache;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;

import com.paessler.prtg.util.net.http.HTTPUtil;
import com.paessler.prtg.util.net.http.HTTPUtil.Authentication;
import com.paessler.prtg.util.net.http.HTTPUtil.Method;

public class HTTPEntry {
	
	private static final PoolingHttpClientConnectionManager connPool;
	public static PoolingHttpClientConnectionManager getConnPool() {return connPool;}
	static {

        connPool = new PoolingHttpClientConnectionManager();
        // Increase max total connection to 200
 //       connPool.setMaxTotal(10);//configurable through app.properties
        // Increase default max connection per route to 50
//        connPool.setDefaultMaxPerRoute(20);//configurable through app.properties

	}
	
	// ----------------------------
	public String 			description = null;
	public	URI  			uri = null;
	public	Method			method = Method.POST;
	public	String 			data = null;
	public	Authentication	authentication = Authentication.NONE;
	public	String 			username = null;
	public	String 			password = null;
	public	HttpHost 		target = null;
	
	//	--------------------------------------------------
	public String getDescription() {if(description == null){toString();} return description;	}
	public void setDescription(String description) {this.description = description;	}
	// -----------------------------------------------------
	public void clearDescription(){ description= null;}
	public String toString(){
		if(description == null){
			StringBuilder str = new StringBuilder();
//			str.append("["+method+ "] ");
			str.append(uri.getScheme()+"://");
			str.append(uri.getHost());
			if(uri.getPort() > 0){
				str.append(":"+uri.getPort());
			}
			if(uri.getPath() != null){
				str.append(uri.getPath());
			}
			description = str.toString();
		}
		return description;
	}

	//	--------------------------------------------------
	public URI getUri() {return uri;}
	public void setUri(URI uri) {this.uri = uri;clearDescription();}

	//	--------------------------------------------------
	public Method getMethod() {	return method;	}
	public void setMethod(Method method) {	this.method = method;	clearDescription();}
	public void setMethod(String method) {	setMethod(Method.valueOf(method));	}
	
	//	--------------------------------------------------
	public String getData() {return data;}
	public void setData(String data) {this.data = data;}

	//	--------------------------------------------------
	public Authentication getAuthentication() {	return authentication;}
	public void setAuthentication(Authentication authentication) {
		this.authentication = authentication;
	}
	//	--------------------------------------------------
	public String getUsername() {return username;}
	public void setUsername(String username) {this.username = username;}
	//	--------------------------------------------------
	public String getPassword() {return password;}
	public void setPassword(String password) {this.password = password;}

	// ----------------------------------------------------------
	private AuthCache myauthcache = null;
	public AuthCache getAuthCache(){
		AuthCache retVal = myauthcache;
		if(myauthcache == null){
			retVal = myauthcache = new BasicAuthCache();
		}
		return retVal;
	}
	// ---------------------------------------------------------------
	public CloseableHttpClient  getCloseableHttpClient(){
		CloseableHttpClient retVal = HTTPUtil.getCloseableHttpClient(getConnPool(), getAuthentication(), getUri(), getUsername(), getPassword());;
		return retVal;
	}
	// ---------------------------------------------------------------
	public HttpUriRequest   getHttpRequest(){
		
		HttpUriRequest retVal = HTTPUtil.getHttpRequest(getUri(), getMethod());
		return retVal;
	}
	
	//	----------------------------------------------------------------------------------------------------
	//	----------------------------------------------------------------------------------------------------
	//	----------------------------------------------------------------------------------------------------
	public HTTPEntry(){	}
	
	public HTTPEntry(URI uri, Method method){
		this.uri = uri;
		this.method = method;
	}
	
	// -----------------------------------------------------
	public static HTTPEntry httpEntryFactory(String urlstring, String httpmethodstring, String authenticationstring){
		HTTPEntry retVal = null;
		URI uri = null;
		Method httpmethod = null;
		Authentication authmethod = null;

		try {
			uri = new URI(urlstring);
			httpmethod = Method.valueOf(httpmethodstring);
			authmethod = Authentication.valueOf(authenticationstring);
			retVal = new HTTPEntry(uri, httpmethod);
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return retVal;
	}
}


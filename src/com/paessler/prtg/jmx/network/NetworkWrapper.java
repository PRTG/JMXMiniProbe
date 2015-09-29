/*
 * Copyright (c) 2014, Paessler AG <support@paessler.com>
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

package com.paessler.prtg.jmx.network;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.conn.ssl.X509HostnameVerifier;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import com.paessler.prtg.jmx.Logger;

import javax.net.ssl.*;

import java.io.*;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

public class NetworkWrapper {
    public static HttpClient wrapClient(HttpClient base) {
        try {
            SSLContext ctx = SSLContext.getInstance("TLSv1.2");
            X509TrustManager tm = new X509TrustManager() {
                public void checkClientTrusted(X509Certificate[] xcs, String string) throws CertificateException {
                }
                public void checkServerTrusted(X509Certificate[] xcs, String string) throws CertificateException {
                }
                public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }
            };
            X509HostnameVerifier verifier = new X509HostnameVerifier() {
                public void verify(String string, SSLSocket ssls) throws IOException {
                }

                public void verify(String string, X509Certificate xc) throws SSLException {
                }

                public void verify(String string, String[] strings, String[] strings1) throws SSLException {
                }

                public boolean verify(String string, SSLSession ssls) {
                    return true;
                }
            };
            ctx.init(null, new TrustManager[]{tm}, null);
            SSLSocketFactory ssf = new SSLSocketFactory(ctx);
            ssf.setHostnameVerifier(verifier);
            ClientConnectionManager ccm = base.getConnectionManager();
            SchemeRegistry sr = ccm.getSchemeRegistry();
            sr.register(new Scheme("https", ssf, 443));
            return new DefaultHttpClient(ccm, base.getParams());
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public static DefaultHttpClient getClient( int timeout) {
        DefaultHttpClient client = new DefaultHttpClient();
        wrapClient(client);
        return client;
    }

    public static void post(String url, String Data) throws IOException {
        post(url, Data, 5000);
    }

    public static void post(String url, String data, int timeout) throws IOException {
        DefaultHttpClient httpClient = getClient(timeout);
        Logger.log("Uploading " + data + " to " + url);
        HttpPost post = new HttpPost(url);
        post.setEntity(new StringEntity(data));
        post.setHeader("Accept", "application/json");
        post.setHeader("Content-type", "application/json");
        HttpResponse response = httpClient.execute(post);
        if (response == null)
            throw new InterruptedIOException("The network is not working properly");

        StatusLine statusLine = response.getStatusLine();
        int statusCode = statusLine.getStatusCode();
        if (statusCode >= 200 && statusCode <= 299) {
            // Great!
        } else {
            String reason = responseToString(response);
            if (reason == null || reason.length() == 0)
                throw new HttpResponseException(statusCode, statusLine.getReasonPhrase());
            else
                throw new HttpResponseException(statusCode, reason);
        }
    }

    public static String fetch( String url) throws IOException {
        return fetch( url, 5000);
    }

    public static String fetch(String url, int timeout) throws IOException {
        System.out.println("Fetching " + url);
        DefaultHttpClient httpClient = getClient(timeout);
        HttpGet get = new HttpGet(url);
        HttpResponse response = null;
        response = httpClient.execute(get);
        if (response == null)
            throw new InterruptedIOException("The network is not working properly");

        StatusLine statusLine = response.getStatusLine();
        int statusCode = statusLine.getStatusCode();
        if (statusCode >= 200 && statusCode <= 299) {
            return responseToString(response);
        } else {
            String reason = responseToString(response);
            if (reason == null || reason.length() == 0)
                throw new HttpResponseException(statusCode, statusLine.getReasonPhrase());
            else
                throw new HttpResponseException(statusCode, reason);
        }
    }

    public static String responseToString(HttpResponse response) throws IOException {
        StringBuilder builder = new StringBuilder();
        HttpEntity responseEntity = response.getEntity();
        InputStream content = responseEntity.getContent();
        BufferedReader reader = new BufferedReader(new InputStreamReader(content));
        String line;
        while ((line = reader.readLine()) != null) {
            builder.append(line);
        }

        responseEntity.consumeContent();
        return builder.toString();
    }


}

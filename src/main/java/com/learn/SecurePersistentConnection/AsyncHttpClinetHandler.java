package com.learn.SecurePersistentConnection;

import javax.net.ssl.SSLContext;

import com.ning.http.client.AsyncHttpClient;
import com.ning.http.client.AsyncHttpClientConfig;
import com.ning.http.client.AsyncHttpClientConfig.Builder;

import Utils.ConnectionUtil;

public class AsyncHttpClinetHandler {
	
	public AsyncHttpClinetHandler() {
		initialize();
	}

	/**
	 * Please make all the configuration from properties file
	 */
	private static AsyncHttpClient asyncHttpClient;
	private static Builder builder;
	boolean isSSLEnabled=true;
	boolean isValidCert=true;
	boolean isProxyAuth=true;

	void initialize(){
		if(asyncHttpClient == null){
			synchronized(AsyncHttpClinetHandler.class){
				builder = new AsyncHttpClientConfig.Builder();
				builder.setCompressionEnforced(true)
				.setAllowPoolingConnections(true)//Enforce HTTP compression.
				.setRequestTimeout(3600)//maximum time in millisecond an AsyncHttpClient waits until the response is completed.
				;
				if(isSSLEnabled){
					builder.setAllowPoolingSslConnections(true);
					SSLContext sslContext=ConnectionUtil.getSSLContext();
					if(sslContext!=null){
						builder.setSSLContext(sslContext);
					}else{
						App.LOGGER.error("SSL Context not defined. Please configure SSLCertificate Properly");
						//Only if something wrong with certificate we invalidate the process
						isValidCert=false;
					}
				}
				if(isProxyAuth){
					if(ConnectionUtil.getProxyServer()!=null){
						builder.setProxyServer(ConnectionUtil.getProxyServer());
					}
				}

				asyncHttpClient = new AsyncHttpClient(
						builder.build());

			}
		}
	}
}

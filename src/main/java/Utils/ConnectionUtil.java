package Utils;

import java.io.InputStream;
import java.net.Socket;
import java.security.KeyStore;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLEngine;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509ExtendedTrustManager;

import com.learn.SecurePersistentConnection.App;

import com.ning.http.client.ProxyServer;
import com.ning.http.client.ProxyServer.Protocol;

public class ConnectionUtil {
	/**
	 * Create Proxy Server
	 * @return
	 */
	public static ProxyServer proxyServer;
	public static ProxyServer getProxyServer(){
		Protocol protocol =ConnectionConstants.Is_Secure_Endpoint?
				ProxyServer.Protocol.HTTPS:ProxyServer.Protocol.HTTP;
		if(ConnectionConstants.Is_Proxy_Auth) {
			proxyServer=new ProxyServer(protocol,ConnectionConstants.Proxy_Address,
					ConnectionConstants.Proxy_Port,ConnectionConstants.Proxy_UserName,
					ConnectionConstants.Proxy_Password);
		} else {
			proxyServer=new ProxyServer(protocol,
					ConnectionConstants.Proxy_Address,ConnectionConstants.Proxy_Port);
		}
		if(ConnectionConstants.Is_Log_Enalbled) {
			App.LOGGER.info(" creating proxy server");
		}
		return proxyServer;

	}

	public static SSLContext getSSLContext(){
		  if(ConnectionConstants.Is_Log_Enalbled){
	            App.LOGGER.info("getSSLContext()");
	        }
	        SSLContext sslContext=null;
	        try{
	            TrustManagerFactory tmf = TrustManagerFactory
	                    .getInstance(TrustManagerFactory.getDefaultAlgorithm());
	            // Using null here initialises the TMF with the default trust store.
	            tmf.init((KeyStore) null);
	            if(ConnectionConstants.Is_Log_Enalbled){
	                App.LOGGER.info("TrustManagerFactory default");
	            }
	            // Get hold of the default trust manager
	            X509ExtendedTrustManager defaultTm = null;
	            for (TrustManager tm : tmf.getTrustManagers()) {
	                if (tm instanceof X509ExtendedTrustManager) {
	                    defaultTm = (X509ExtendedTrustManager) tm;
	                    break;
	                }
	            }
	       
	            if(ConnectionConstants.Is_Log_Enalbled){
	                App.LOGGER.info("X509TrustManager default");
	            }
	         
	            InputStream myKeys = ConnectionUtil.class
	            		.getResourceAsStream("/trustcertificate.jks");
	            if(ConnectionConstants.Is_Log_Enalbled){
	                App.LOGGER.info("certificate from file ");
	            }

	            // Do the same with your trust store this time
	            // Adapt how you load the keystore to your needs
	            KeyStore myTrustStore = KeyStore.getInstance(KeyStore.getDefaultType());
	            //PK
	            myTrustStore.load(myKeys, ConnectionConstants.SSL_CERTIFICATE_KEY.toCharArray());
	            //myTrustStore.load(keystoreStream, ConfigParams.SSL_CERTIFICATE_KEY.toCharArray());
	            //keystoreStream.close();
	            myKeys.close();

	            tmf = TrustManagerFactory
	                    .getInstance(TrustManagerFactory.getDefaultAlgorithm());
	            tmf.init(myTrustStore);
	            if(ConnectionConstants.Is_Log_Enalbled){
	                App.LOGGER.info("TrustManagerFactory from certificate ");
	            }
	            // Get hold of the default trust manager
	            X509ExtendedTrustManager myTm = null;
	            for (TrustManager tm : tmf.getTrustManagers()) {
	                if (tm instanceof X509ExtendedTrustManager) {
	                    myTm = (X509ExtendedTrustManager) tm;
	                    break;
	                }
	            }
	            if(ConnectionConstants.Is_Log_Enalbled){
	                App.LOGGER.info("X509TrustManager from certificate ");
	            }
	            // Wrap it in your own class.
	            final X509ExtendedTrustManager finalDefaultTm = defaultTm;
	            final X509ExtendedTrustManager finalMyTm = myTm;
	       
	            X509ExtendedTrustManager customTm= new X509ExtendedTrustManager(){

					
					public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
						 if(ConnectionConstants.Is_Log_Enalbled)
						 {
		                        App.LOGGER.info("checkClientTrusted()");
						 }
		                    // If you're planning to use client-cert auth,
		                    // do the same as checking the server.
		                    finalMyTm.checkClientTrusted(chain, authType);					
					}

					
					public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
						try {
	                        if(ConnectionConstants.Is_Log_Enalbled){
	                            App.LOGGER.info("checkServerTrusted()");
	                        }
	                        finalMyTm.checkServerTrusted(chain, authType);
	                    } catch (CertificateException e) {
	                        // This will throw another CertificateException if this fails too.
	                        finalMyTm.checkServerTrusted(chain, authType);
	                    }					
					}

					
					public X509Certificate[] getAcceptedIssuers() {
						 if(ConnectionConstants.Is_Log_Enalbled){
		                        App.LOGGER.info("getAcceptedIssuers()");
						 }
		                    // If you're planning to use client-cert auth,
		                    // merge results from "defaultTm" and "myTm".
		                    return finalMyTm.getAcceptedIssuers();
					}


					@Override
					public void checkClientTrusted(X509Certificate[] chain, String authType, Socket socket)
							throws CertificateException {
						// TODO Auto-generated method stub
						
					}


					@Override
					public void checkClientTrusted(X509Certificate[] chain, String authType, SSLEngine engine)
							throws CertificateException {
						// TODO Auto-generated method stub
						
					}


					@Override
					public void checkServerTrusted(X509Certificate[] chain, String authType, Socket socket)
							throws CertificateException {
						// TODO Auto-generated method stub
						
					}


					@Override
					public void checkServerTrusted(X509Certificate[] chain, String authType, SSLEngine engine)
							throws CertificateException {
						// TODO Auto-generated method stub
						
					}

	            	
	            };
	            
	            sslContext = SSLContext.getInstance("TLS");
	            sslContext.init(null, new TrustManager[] { customTm }, null);
	           
	        }catch(Exception ex){
	            if(ConnectionConstants.Is_Log_Enalbled){
	                App.LOGGER.error("Exception in ssl context creation " +ex);
	            }
	        }
	        if(ConnectionConstants.Is_Log_Enalbled){
	            App.LOGGER.error("-----------> Giving sslContext");
	        }
	        return sslContext;
	    }

		
}

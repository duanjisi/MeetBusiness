package com.atgc.cotton.socket;

import android.os.AsyncTask;

import com.atgc.cotton.http.HttpUrl;

import org.java_websocket.client.DefaultSSLWebSocketClientFactory;
import org.java_websocket.drafts.Draft_17;

import java.net.URI;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

/**
 * Created by Johnny on 2017-09-20.
 */
public class SocketHandler {
    private static SocketHandler handler = null;


    public synchronized static SocketHandler getInstance() {
        if (handler == null) {
            handler = new SocketHandler();
        }
        return handler;
    }

    public void sendMessage(String msg){
        SocketConnector socketConnector = new SocketConnector();
        socketConnector.sendString = msg;
        socketConnector.execute();
    }

//    public SocketHandler(String output) {
//        SocketConnector socketConnector = new SocketConnector();
//        socketConnector.sendString = output;
//        socketConnector.execute();
//    }

    private class SocketConnector extends AsyncTask<Void, Void, Void> {
        private String sendString;

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                URI uri = new URI(HttpUrl.WS_URL);
                WebSocketExampleClient webSocketExampleClient = new WebSocketExampleClient(uri, new Draft_17());
                //This part is needed in case you are going to use self-signed certificates
                TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
                    public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                        return new java.security.cert.X509Certificate[]{};
                    }

                    public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                    }

                    public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                    }
                }};

                try {
                    SSLContext sc = SSLContext.getInstance("TLS");
                    sc.init(null, trustAllCerts, new java.security.SecureRandom());

                    //Otherwise the line below is all that is needed.
                    //sc.init(null, null, null);
                    webSocketExampleClient.setWebSocketFactory(new DefaultSSLWebSocketClientFactory(sc));
                } catch (Exception e) {
                    e.printStackTrace();
                }

                webSocketExampleClient.connectBlocking();
                webSocketExampleClient.send(sendString);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}

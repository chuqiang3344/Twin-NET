package com.tyaer.net.httpclient.manager;


import javax.net.ssl.*;
import javax.security.cert.CertificateException;
import javax.security.cert.X509Certificate;
import java.security.SecureRandom;

public class HttpsSupport {

    public static void init() throws Exception {
        System.out.println("HttpURLConnectionDownloader设置支持https");
        TrustManager[] trustAllCerts = new TrustManager[1];
        trustAllCerts[0] = new MyX509TrustManager();

        SSLContext sc = SSLContext.getInstance("SSL");
        sc.init(null, trustAllCerts, new SecureRandom());
        HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

        HostnameVerifier allHostsValid = new MyHostnameVerifier();
        HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
    }

    static class MyX509TrustManager implements X509TrustManager {
        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
            return null;
        }

        public void checkServerTrusted(X509Certificate[] certs, String authType) throws CertificateException {
        }

        public void checkClientTrusted(X509Certificate[] certs, String authType) throws CertificateException {
        }

        @Override
        public void checkClientTrusted(
                java.security.cert.X509Certificate[] arg0, String arg1)
                throws java.security.cert.CertificateException {
            // TODO Auto-generated method stub

        }

        @Override
        public void checkServerTrusted(
                java.security.cert.X509Certificate[] arg0, String arg1)
                throws java.security.cert.CertificateException {
            // TODO Auto-generated method stub

        }
    }

    static class MyHostnameVerifier implements HostnameVerifier {
        @Override
        public boolean verify(String s, SSLSession sslSession) {
            return true;
        }
    }
}

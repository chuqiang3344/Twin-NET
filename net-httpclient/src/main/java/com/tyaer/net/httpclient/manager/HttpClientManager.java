package com.tyaer.net.httpclient.manager;

import com.tyaer.net.httpclient.config.HttpConfig;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.config.AuthSchemes;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.ConnectionKeepAliveStrategy;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.client.LaxRedirectStrategy;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.ssl.SSLContexts;
import org.apache.log4j.Logger;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Arrays;

/**
 * HttpClient连接池管理
 * Created by Twin on 2016/8/16.
 */
public class HttpClientManager {

    private static final Logger logger = Logger.getLogger(HttpClientManager.class);

    /**
     * 获取连接的最大等待时间
     */
    private int CONNECTION_TIMEOUT;
    /**
     * 读取超时时间
     */
    private int SOCKET_TIMEOUT;
    /**
     * 获取连接的最大等待时间
     */
    private int CONNECTION_REQUEST_TIMEOUT;

    // private static HttpParams httpParams;
    private PoolingHttpClientConnectionManager connectionManager;
    private ConnectionKeepAliveStrategy myStrategy;
    private HttpRequestRetryHandler myRequestRetryHandler;
    private RequestConfig defaultRequestConfig;
    private volatile CloseableHttpClient CLOSEABLEHTTPCLIENT_INSTANCE;

    public HttpClientManager() {
        init();
    }

    /**
     * 简单设置
     *
     * @return
     */
    public static RequestConfig getRequestConfigBuild() {
        int time = 5000;
        RequestConfig build = RequestConfig.custom()
                .setConnectionRequestTimeout(time)
                .setSocketTimeout(time)
                .setConnectTimeout(time)
                .setCookieSpec(CookieSpecs.STANDARD_STRICT).build();
        return build;
    }

    private void init() {
        CONNECTION_TIMEOUT = HttpConfig.CONNECTION_TIMEOUT;
        SOCKET_TIMEOUT = HttpConfig.SOCKET_TIMEOUT;
        CONNECTION_REQUEST_TIMEOUT = 10000;

//        connectionManager = getHttpConnectionManager(); //Http
        connectionManager = getHttpsConnectionManager(); //Https
//            connectionManager = new PoolingHttpClientConnectionManager();//默认
        /**
         * 最大连接数
         */
        connectionManager.setMaxTotal(HttpConfig.MAX_TOTAL_CONNECTIONS);//最大连接数
        /**
         * 每个路由最大连接数,设置每个主机地址的并发数
         * 对每个指定连接的服务器（指定的ip）可以创建并发20 socket进行访问
         */
        connectionManager.setDefaultMaxPerRoute(HttpConfig.MAX_ROUTE_CONNECTIONS);
//            connectionManager.setMaxPerRoute(new HttpRoute(new HttpHost("weibo.com")), 150);//每个路由器对每个服务器允许最大的并发访问

        // 连接回收策略,启动线程，5秒钟清空一次失效连接
//        new IdleConnectionMonitorThread(connectionManager).start();//// TODO: 2016/12/8 开启线程

        // 连接存活策略
        myStrategy = new ConnectionKeepAliveStrategyImpl();
        // 请求重试策略
        myRequestRetryHandler = new HttpRequestRetryHandlerImpl(2);//retryTimes

        //RequestConfig基本设置
        defaultRequestConfig = RequestConfig
                .custom()
                //获取cookie时必须设置STANDARD，连续请求；光手动set可以设置为IGNORE_COOKIES
                .setCookieSpec(CookieSpecs.IGNORE_COOKIES)
                .setExpectContinueEnabled(true)//期望连接
//                .setStaleConnectionCheckEnabled(true) //检查旧连接
                .setTargetPreferredAuthSchemes(Arrays.asList(AuthSchemes.NTLM, AuthSchemes.DIGEST))
                .setProxyPreferredAuthSchemes(Arrays.asList(AuthSchemes.BASIC))
                .build();

        CLOSEABLEHTTPCLIENT_INSTANCE = generateClient();
    }

    private PoolingHttpClientConnectionManager getHttpConnectionManager() {
        Registry<ConnectionSocketFactory> registry = RegistryBuilder.<ConnectionSocketFactory>create()
//                .register("http", PlainConnectionSocketFactory.INSTANCE)
//                .register("https", buildSSLConnectionSocketFactory())
                .build();
        PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager(registry);
        return connectionManager;
    }

    private PoolingHttpClientConnectionManager getHttpsConnectionManager() {
        Registry<ConnectionSocketFactory> registry = RegistryBuilder.<ConnectionSocketFactory>create()
//                .register("http", PlainConnectionSocketFactory.INSTANCE)
//                .register("https", buildSSLConnectionSocketFactory()) //自定义
                .register("http", PlainConnectionSocketFactory.getSocketFactory())
                .register("https", SSLConnectionSocketFactory.getSocketFactory()) //默认配置
                .build();
        PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager(registry);
        return connectionManager;
    }

    private SSLConnectionSocketFactory buildSSLConnectionSocketFactory() {
        try {
//            return new SSLConnectionSocketFactory(SSLContexts.createDefault());
            return new SSLConnectionSocketFactory(createIgnoreVerifySSL()); // 优先绕过安全证书
        } catch (KeyManagementException e) {
            logger.error("ssl connection fail", e);
        } catch (NoSuchAlgorithmException e) {
            logger.error("ssl connection fail", e);
        }
        return SSLConnectionSocketFactory.getSocketFactory();
    }

    private SSLContext createIgnoreVerifySSL() throws NoSuchAlgorithmException, KeyManagementException {
        // 实现一个X509TrustManager接口，用于绕过验证，不用修改里面的方法
        X509TrustManager trustManager = new X509TrustManager() {
            @Override
            public void checkClientTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {

            }

            @Override
            public void checkServerTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {

            }

            @Override
            public X509Certificate[] getAcceptedIssuers() {
                return new X509Certificate[0];
            }
        };
//        SSLContext sc = SSLContext.getInstance("TLS"); //默认
        SSLContext sc = SSLContext.getInstance("SSLv3");//TODO 不兼容低版本？https://idol001.com/news/7004/detail/580d81fd7a11731f778b4725/
//        SSLContext sc = SSLContext.getInstance("TLSv1.2,TLSv1.1,TLSv1.0,SSLv3,SSLv2Hello");
//        SSLContext sc = SSLContext.getInstance("SSLv2Hello");
//        SSLContext sc = SSLContext.getDefault();
        sc.init(null, new TrustManager[]{trustManager}, null);
        return sc;
    }

    /**
     * SSL/TLS定制
     *
     * @return
     */
    private SSLConnectionSocketFactory getSSLConnectionSocketFactory() {
        SSLContext sslContext = null;
        try {
            sslContext = new SSLContextBuilder().loadTrustMaterial(null, new TrustStrategy() {
                // 信任所有 always return true，trust every certificate type
                public boolean isTrusted(X509Certificate[] chain, String authType)
                        throws CertificateException {
                    return true;
                }
            }).build();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        }
        SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslContext);
        return sslsf;
    }

    public CloseableHttpClient getHttpClient() {
        if (CLOSEABLEHTTPCLIENT_INSTANCE == null) {
            synchronized (HttpClientManager.class) {
                if (CLOSEABLEHTTPCLIENT_INSTANCE == null) {
                    CLOSEABLEHTTPCLIENT_INSTANCE = generateClient();
                }
            }
        }
        return CLOSEABLEHTTPCLIENT_INSTANCE;
    }

    private CloseableHttpClient generateClient() {
        CloseableHttpClient httpClient = HttpClients
                .custom()
                .setConnectionManager(connectionManager)
//                          .setSSLSocketFactory(getSSLConnectionSocketFactory()) //认证https，若同时设置了connectionManager，该设置无效？
                .setDefaultRequestConfig(getDefaultRequestConfig())
                .setDefaultCookieStore(new BasicCookieStore())
                .setRedirectStrategy(new LaxRedirectStrategy())// 声明重定向策略对象
//                          .disableRedirectHandling() //关闭重定向
                .setRetryHandler(myRequestRetryHandler) //重试请求.setRetryHandler(new DefaultHttpRequestRetryHandler(retryTimes, true))
                .setKeepAliveStrategy(myStrategy) //// TODO: 2016/8/16  需要测试
                .build();//刷新配置
        return httpClient;
    }

    /**
     * 全局设置
     * 设置在 CloseableHttpClient
     *
     * @return
     */
    private RequestConfig getDefaultRequestConfig() {
        return getRequestConfig().build();
    }

    /**
     * request设置，局部设置
     * 还有后续设置，所以不能作为单例模式
     *
     * @return
     */
    public RequestConfig.Builder getRequestConfig() {
        RequestConfig.Builder builder = RequestConfig
                .copy(defaultRequestConfig)
                .setConnectTimeout(CONNECTION_TIMEOUT)
                .setSocketTimeout(SOCKET_TIMEOUT)
//                .setCookieSpec(CookieSpecs.IGNORE_COOKIES)//去除关键词任务Warn TODO: 2016/11/26 忽略Cookie的政策。
                .setConnectionRequestTimeout(CONNECTION_REQUEST_TIMEOUT);
        return builder;
    }
}


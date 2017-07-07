package com.tyaer.net.httpclient.counselor;

/**
 * Created by Twin on 2017/4/24.
 */

import com.tyaer.net.httpclient.bean.RequestBean;
import com.tyaer.net.httpclient.bean.ResponseBean;
import com.tyaer.net.httpclient.handle.RequestHandle;
import com.tyaer.net.httpclient.handle.ResponseHandle;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.Header;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.AuthCache;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.LayeredConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.BasicAuthCache;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AbuyunDownloader {

    //https://www.abuyun.com/http-proxy/pro-manual.html
    // 代理服务器
    final static String proxyHost = "proxy.abuyun.com";
    final static Integer proxyPort = 9010;
    // 代理隧道验证信息
    final static String proxyUser = "HDB28N060T1D40WP";
    final static String proxyPass = "9F3E8CE0270A8757";
    private static final Logger logger = Logger.getLogger(AbuyunDownloader.class);
    private static final String LOCATION_KEY = "Location";
    private static PoolingHttpClientConnectionManager cm = null;
    //    private static HttpRequestRetryHandler httpRequestRetryHandler = null;
    private static HttpHost proxy = null;
    private static CredentialsProvider credsProvider = null;
    private static RequestConfig reqConfig = null;
    private static ResponseHandle responseHandle = new ResponseHandle();

    static {
        ConnectionSocketFactory plainsf = PlainConnectionSocketFactory.getSocketFactory();
        LayeredConnectionSocketFactory sslsf = SSLConnectionSocketFactory.getSocketFactory();

        Registry registry = RegistryBuilder.create()
                .register("http", plainsf)
                .register("https", sslsf)
                .build();

        cm = new PoolingHttpClientConnectionManager(registry);
        cm.setMaxTotal(800);
        cm.setDefaultMaxPerRoute(500);

        proxy = new HttpHost(proxyHost, proxyPort, "http");

        credsProvider = new BasicCredentialsProvider();
        credsProvider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(proxyUser, proxyPass));

        reqConfig = RequestConfig.custom()
                .setConnectionRequestTimeout(5000)
                .setConnectTimeout(5000)
                .setSocketTimeout(5000)
                .setExpectContinueEnabled(false)
                .setProxy(new HttpHost(proxyHost, proxyPort))
                .build();
    }

    public static ResponseBean doRequest(HttpRequestBase httpReq) {
        String url = httpReq.getURI().toString();
        ResponseBean responseBean = new ResponseBean();
        responseBean.setStatusCode(0);
        responseBean.setUrl(httpReq.getURI().toString());
        CloseableHttpResponse httpResp = null;

        int statusCode = 0;
        String html = null;
        try {
            setHeaders(httpReq);

            httpReq.setConfig(reqConfig);

            CloseableHttpClient httpClient = HttpClients.custom()
                    .setConnectionManager(cm)
                    .setDefaultCredentialsProvider(credsProvider)
                    .build();

            AuthCache authCache = new BasicAuthCache();
            authCache.put(proxy, new BasicScheme());
            HttpClientContext localContext = HttpClientContext.create();
            localContext.setAuthCache(authCache);
            httpResp = httpClient.execute(httpReq, localContext);

            statusCode = httpResp.getStatusLine().getStatusCode();
            if (statusCode == 200) {
                statusCode = 666;
//                byte[] contentBytes = httpHandle.readToByteBuffer(response.getEntity()).toByteArray();
                byte[] contentBytes = IOUtils.toByteArray(httpResp.getEntity().getContent());
                String charset = responseHandle.getHtmlCharset(httpResp, contentBytes);
                html = new String(contentBytes, charset);
                statusCode = 200;
            } else {
                html = statusCode + ": " + url;
                logger.error(html);
            }
        } catch (Exception e) {
//            e.printStackTrace();
            responseBean.setExceptionInfo(e.getMessage());
        } finally {
            if (httpResp != null) {
                try {
                    EntityUtils.consume(httpResp.getEntity());
                } catch (IOException e) {
//                    e.printStackTrace();
                    EntityUtils.consumeQuietly(httpResp.getEntity());
                }
                try {
                    httpResp.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            httpReq.releaseConnection();
            httpReq.abort();
        }
        responseBean.setStatusCode(statusCode);
        responseBean.setRawText(html);
        return responseBean;
    }

    /**
     * 设置请求头
     *
     * @param httpReq
     */
    private static void setHeaders(HttpRequestBase httpReq) {
        httpReq.setHeader("Accept-Encoding", null);
        httpReq.setHeader("Proxy-Switch-Ip","yes");
    }


//    public static ResponseBean sendGetRequest(String targetUrl, String cookie,int retryNum) {
//        if (retryNum <= 0) {
//            ResponseBean responseBean = sendGetRequest(targetUrl,cookie);
//            return responseBean;
//        } else {
//            while (retryNum > 0) {
//                ResponseBean responseBean = sendGetRequest(targetUrl,cookie);
//                if (responseBean.getStatusCode() == 200) {
//                    return responseBean;
//                } else {
//                    retryNum--;
//                }
//            }
//        }
//        return null;
//    }

    public static void doPostRequest() {
        try {
            // 要访问的目标页面
            HttpPost httpPost = new HttpPost("https://test.abuyun.com/proxy.php");

            // 设置表单参数
            List params = new ArrayList();
            params.add(new BasicNameValuePair("method", "next"));
            params.add(new BasicNameValuePair("params", "{\"broker\":\"abuyun\":\"site\":\"https://www.abuyun.com\"}"));

            httpPost.setEntity(new UrlEncodedFormEntity(params, "utf-8"));

            doRequest(httpPost);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static ResponseBean sendGetRequest(String targetUrl, String cookie) {
        // 要访问的目标页面
//        String targetUrl = "https://test.abuyun.com/proxy.php";
        //String targetUrl = "http://proxy.abuyun.com/switch-ip";
        //String targetUrl = "http://proxy.abuyun.com/current-ip";
        HttpGet httpGet = new HttpGet(targetUrl);
        try {
            if (StringUtils.isNotBlank(cookie)) {
                httpGet.setHeader("Cookie", cookie);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        ResponseBean responseBean = doRequest(httpGet);
        int retryNum_max = 3;
        int retryNum = 0;
        while ((responseBean.getStatusCode() != 200
                && responseBean.getExceptionInfo().contains("SocketTimeoutException: Read timed out"))
                && retryNum < retryNum_max) {
//            logger.info("AbuyunDownloader重试请求:" + (retryNum + 1));
            httpGet = new HttpGet(targetUrl);
            try {
                if (StringUtils.isNotBlank(cookie)) {
                    httpGet.setHeader("Cookie", cookie);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            responseBean = doRequest(httpGet);
            retryNum++;
        }
        if (retryNum == retryNum_max) {
            logger.error("AbuyunDownloader重试后失败：" + responseBean);
        }
        return responseBean;
    }

    public String sendReqGetLocation(RequestBean requestBean, int retryNum) {
        ResponseBean responseBean = null;
        while (retryNum > 0) {
            responseBean = sendReqGetLocation(requestBean);
            if (responseBean == null || responseBean.getStatusCode() != 302) {
                logger.info("retry " + responseBean);
                retryNum--;
            } else {
                break;
            }
        }
        if (retryNum == 0) {
            logger.error("sendReqGetLocation 获取失败！");
        }
        return (String) responseBean.getExtra(LOCATION_KEY);
    }

    private ResponseBean sendReqGetLocation(RequestBean requestBean) {
        ResponseBean responseBean = new ResponseBean();
        HttpRequestBase httpRequestBase = RequestHandle.gethttpRequestBase(requestBean, reqConfig);
        if (httpRequestBase == null) {
            return null;
        }
        httpRequestBase.setHeader("Proxy-Switch-Ip","yes"); //手动切换代理IP
        CloseableHttpResponse response = null;
        try {
            CloseableHttpClient httpClient = HttpClients.custom()
                    .setConnectionManager(cm)
                    .setDefaultCredentialsProvider(credsProvider)
                    .disableRedirectHandling()
                    .build();
            AuthCache authCache = new BasicAuthCache();
            authCache.put(proxy, new BasicScheme());
            HttpClientContext localContext = HttpClientContext.create();
            localContext.setAuthCache(authCache);
            response = httpClient.execute(httpRequestBase, localContext);

            int statusCode = response.getStatusLine().getStatusCode();
            responseBean.setStatusCode(statusCode);
            if (statusCode == 302) {
                Header locationHeader = response.getFirstHeader("Location"); //
                if (locationHeader != null) {
                    String location = locationHeader.getValue();
                    responseBean.putExtra(LOCATION_KEY, location);
                }
//                logger.debug(EntityUtils.toString(response.getEntity(), "utf-8"));
            } else if (statusCode == 429) {
                logger.warn("并发数超过限制，结束当前单个线程减少并发数！");
//                Thread.currentThread().interrupt();
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } else {
                logger.warn("非302页面！" + statusCode);
            }
        } catch (IOException e) {
            responseBean.setExceptionInfo(e.getMessage());
        } finally {
            try {
                if (response != null) {
                    EntityUtils.consume(response.getEntity());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            httpRequestBase.releaseConnection();
            httpRequestBase.abort();
        }
        return responseBean;
    }
}

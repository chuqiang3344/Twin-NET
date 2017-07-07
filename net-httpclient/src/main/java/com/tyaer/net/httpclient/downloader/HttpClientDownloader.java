package com.tyaer.net.httpclient.downloader;

import com.tyaer.net.httpclient.bean.RequestBean;
import com.tyaer.net.httpclient.bean.ResponseBean;
import com.tyaer.net.httpclient.handle.HttpHandle;
import com.tyaer.net.httpclient.handle.RequestHandle;
import com.tyaer.net.httpclient.handle.ResponseHandle;
import com.tyaer.net.httpclient.manager.HttpClientManager;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.http.HttpHost;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.concurrent.*;

/**
 * Http工具类
 *
 * @author Twin
 */
public class HttpClientDownloader {
    private static final Logger logger = Logger.getLogger(HttpClientDownloader.class);
    private static HttpHandle httpHandle;
    private static ResponseHandle responseHandle;
    private static HttpClientManager HTTPCLIENTMANAGER;

    static {
        HTTPCLIENTMANAGER = new HttpClientManager();
        httpHandle = new HttpHandle();
        responseHandle = new ResponseHandle();
    }

    private String CUSTOMIZE_CHARSET;

    public HttpClientDownloader setCharset(String charset) {
        this.CUSTOMIZE_CHARSET = charset;
        return this;
    }

    /**
     * 最高级请求方法
     *
     * @param requestBean
     * @return
     */
    public ResponseBean sendRequest(RequestBean requestBean) {
        RequestConfig.Builder requestConfigBuilder = HTTPCLIENTMANAGER.getRequestConfig();
        if (requestBean.getHttpHost() != null) {
            requestConfigBuilder.setProxy(requestBean.getHttpHost());
        }
        RequestConfig requestConfig = requestConfigBuilder.build();
        HttpRequestBase httpRequestBase = RequestHandle.gethttpRequestBase(requestBean, requestConfig);
        if (httpRequestBase == null) {
            return null;
        } else {
            return sendRequstGetPage(httpRequestBase);
        }
    }

    /**
     * 重试请求
     *
     * @param url
     * @param retryNum 尝试次数
     * @return
     */
    public ResponseBean sendRequest(String url, int retryNum) {
        if (retryNum <= 0) {
            ResponseBean responseBean = sendRequest(url);
            return responseBean;
        } else {
            while (retryNum > 0) {
                ResponseBean responseBean = sendRequest(url);
                if (responseBean.getStatusCode() == 200) {
                    return responseBean;
                } else {
                    retryNum--;
                }
            }
        }
        return null;
    }

    public ResponseBean sendRequest(String url) {
        // 搜索链接
        HttpGet httpGet;
        try {
            httpGet = new HttpGet(url);
        } catch (IllegalArgumentException e) {
            logger.error(ExceptionUtils.getMessage(e));
            return null;
        }
        RequestConfig.Builder requestConfigBuilder = HTTPCLIENTMANAGER.getRequestConfig();
        httpGet.setConfig(requestConfigBuilder.build());
        return sendRequstGetPage(httpGet);
    }

    /**
     * 超级请求发送
     *
     * @param request
     * @return
     */
    public ResponseBean sendRequstGetPage(HttpRequestBase request) {
        ResponseBean page = new ResponseBean();
        String url = request.getURI().toString();
        page.setUrl(url);
//        RequestConfig config = request.getConfig();
//        if (config != null) {
//            page.getRequest().setHttpHost(config.getProxy());
//        }
        CloseableHttpClient httpClient = HTTPCLIENTMANAGER.getHttpClient();
//        CloseableHttpClient httpClient = HttpClients.createDefault();
        /**设置请求头*/
        RequestHandle.setHttpRequestHeader(request, page);
        CloseableHttpResponse response = null;
        int statusCode = 0;
        String html = null;
        long startTime = Calendar.getInstance().getTimeInMillis();
        try {
            response = callableAndFuture(httpClient, request);
//            response = httpClient.execute(request);
            long ping = Calendar.getInstance().getTimeInMillis() - startTime;
            page.setPing(ping);
            statusCode = httpHandle.getStatusCode(response);
//            if (statusCode == 200||statusCode == 500) {   //TODO http://bbs.cntv.cn/forum-932-1.html
            if (statusCode == 200) {
                statusCode = 666;
//                byte[] contentBytes = httpHandle.readToByteBuffer(response.getEntity()).toByteArray();
                byte[] contentBytes = IOUtils.toByteArray(response.getEntity().getContent());
                String charset;
                if (CUSTOMIZE_CHARSET != null) {
                    charset = CUSTOMIZE_CHARSET;
                } else {
//                    charset = httpHandle.getCharset(response.getEntity(), contentBytes);//我的
                    charset = responseHandle.getHtmlCharset(response, contentBytes);
                }
                html = new String(contentBytes, charset);
                page.setCharset(charset);
                statusCode = 200;
            } else {
                html = statusCode + ": " + url;
                logger.error(html);
            }
        } catch (Exception e) {
//            e.printStackTrace();
//            logger.debug(e);
            html = ExceptionUtils.getMessage(e);//TODO：getMessage看不到完整的错误信息，不好排查问题。
            logger.error("request fail url: " + url + "\n" + statusCode + ": " + html);
            long ping = Calendar.getInstance().getTimeInMillis() - startTime;
            page.setPing(ping);
        } finally {
            if (response != null) {
                try {
                    //ensure the connection is released back to pool
                    EntityUtils.consume(response.getEntity());// TODO: 2016/9/17 异常，导致无法回收连接？
                } catch (IOException e) {
                    logger.warn("close response fail", e);
                    EntityUtils.consumeQuietly(response.getEntity());//继续尝试关闭
                }
                try {
                    response.close();
                } catch (IOException e) {
                    logger.error(ExceptionUtils.getMessage(e));
                }
            }
            //httpclient必须releaseconnection，但不是abort。因为releaseconnection是归还连接到连接池，而abort是直接抛弃这个连接，而且占用连接池的数目。
            request.releaseConnection();
            request.abort();// TODO 关键？连接池资源回收
        }
        page.setStatusCode(statusCode);
        page.setRawText(html);
        return page;
    }


    public ResponseBean sendRequest(String url, String cookie) {
        // 搜索链接
        HttpGet httpGet;
        try {
            httpGet = new HttpGet(url);
//            httpGet = new HttpGet(UrlUtils.transformURI(url));
        } catch (IllegalArgumentException e) {
            logger.error(ExceptionUtils.getMessage(e));
            return null;
        }
        RequestConfig.Builder requestConfigBuilder = HTTPCLIENTMANAGER.getRequestConfig();
        httpGet.setConfig(requestConfigBuilder.build());
        if (StringUtils.isNotBlank(cookie)) {
            httpGet.setHeader("Cookie", cookie);
        }
        return sendRequstGetPage(httpGet);
    }

    /**
     * 使用代理的get请求
     */
    public ResponseBean sendRequest(String url, HttpHost httpHost) {
        HttpGet httpGet = null;
        try {
            httpGet = new HttpGet(url);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        RequestConfig.Builder requestConfigBuilder = HTTPCLIENTMANAGER.getRequestConfig();
        if (httpHost != null) {
            requestConfigBuilder.setProxy(httpHost);
        }
        RequestConfig requestConfig = requestConfigBuilder.build();
        httpGet.setConfig(requestConfig);
        return sendRequstGetPage(httpGet);
    }

    /**
     * 使用代理的get请求
     *
     * @throws Exception
     */
    public ResponseBean sendRequest(String url, HttpHost httpHost, String cookie) {
        // 搜索链接
        HttpGet httpGet;
        try {
            httpGet = new HttpGet(url);
        } catch (IllegalArgumentException e) {
            logger.error(ExceptionUtils.getMessage(e));
            return null;
        }
        RequestConfig.Builder requestConfigBuilder = HTTPCLIENTMANAGER.getRequestConfig();
        if (httpHost != null) {
            requestConfigBuilder.setProxy(httpHost);
        }
        RequestConfig requestConfig = requestConfigBuilder.build();
        httpGet.setConfig(requestConfig);
        //设置cookies
        if (StringUtils.isNotBlank(cookie)) {
            httpGet.setHeader("Cookie", cookie);
        }
        return sendRequstGetPage(httpGet);
    }

    public ResponseBean sendGetRequest(String url, String headers) {
        HttpGet httpGet = new HttpGet(url);
        HashMap<String, String> headersMap = RequestHandle.str2map(headers);
        headersMap.remove("Content-Length");
        for (String key : headersMap.keySet()) {
            httpGet.addHeader(key, headersMap.get(key));
        }
        return sendRequstGetPage(httpGet);
    }


    private CloseableHttpResponse callableAndFuture(CloseableHttpClient httpClient, HttpRequestBase request) throws Exception {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        ExecuteRequsetTask executeRequsetTask = new ExecuteRequsetTask(httpClient, request);
        Future<CloseableHttpResponse> future = executorService.submit(executeRequsetTask);
        CloseableHttpResponse closeableHttpResponse = null;
        try {
            closeableHttpResponse = future.get(60, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            throw e;
//            e.printStackTrace();
        } catch (ExecutionException e) {
            throw e;
        } catch (TimeoutException e) {
            logger.error("线程阻塞！ " + request.getURI().toString() + "，" + request.getConfig().getProxy());
            throw e;
        } finally {
            future.cancel(true);
            executorService.shutdownNow();
//            System.out.println(future.cancel(true));
//            System.out.println(future.isCancelled());
//            System.out.println(future.isDone());
        }
        return closeableHttpResponse;
    }

    /**
     * Callable
     */
    static class ExecuteRequsetTask implements Callable<CloseableHttpResponse> {
        CloseableHttpClient httpClient;
        HttpRequestBase request;

        public ExecuteRequsetTask(CloseableHttpClient httpClient, HttpRequestBase request) {
            this.httpClient = httpClient;
            this.request = request;
        }

        @Override
        public CloseableHttpResponse call() throws Exception {
//            System.out.println("子线程在进行处理...");
            CloseableHttpResponse response = httpClient.execute(request, HttpClientContext.create());
            return response;
        }
    }
}

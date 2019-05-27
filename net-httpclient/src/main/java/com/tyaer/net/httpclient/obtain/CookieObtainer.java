package com.tyaer.net.httpclient.obtain;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.tyaer.net.httpclient.bean.RequestBean;
import com.tyaer.net.httpclient.handle.RequestHandle;
import com.tyaer.net.httpclient.utils.CookieUtils;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.CookieStore;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Twin on 2017/1/10.
 */
public class CookieObtainer {
    private static final Logger logger = Logger.getLogger(CookieObtainer.class);

    // 创建一个本地Cookie存储的实例
    private CookieStore cookieStore;

    //创建一个本地上下文信息
    private HttpContext localContext;

    private CloseableHttpClient httpClient;

    public CookieObtainer() {
        init();
    }

    private void init() {
        cookieStore = new BasicCookieStore();
        localContext = new BasicHttpContext();
        //在本地上下问中绑定一个本地存储
        localContext.setAttribute(HttpClientContext.COOKIE_STORE, cookieStore);

        RequestConfig requestConfig = RequestHandle.getRequestConfig();
        httpClient = HttpClients.custom()
            .setDefaultCookieStore(cookieStore)
            .setDefaultRequestConfig(requestConfig)
            .build();
    }

    /**
     * 通过连续请求获取cookie
     *
     * @param requestBeans
     * @return
     */
    public String obtainCookies(List<RequestBean> requestBeans) {
        Map<String, String> cookies = new HashMap<>();
        if (!cookieStore.getCookies().isEmpty()) {
            init();
        }
        for (RequestBean requestBean : requestBeans) {
            cookies.putAll(sendRequest(requestBean));
            logger.debug(getCookieStore());
        }
        close();
        Map<String, String> cookieMap = CookieUtils.getCookieMap(cookieStore.getCookies());
        cookies.putAll(cookieMap);
        return CookieUtils.getCookieStr(cookies);
    }

    public String obtainUrlCookies(List<String> urls) {
        ArrayList<RequestBean> requestBeans = new ArrayList<>();
        for (String url : urls) {
            requestBeans.add(new RequestBean(url));
        }
        return obtainCookies(requestBeans);
    }

    public Map<String, String> sendRequest(RequestBean requestBean) {
        Map<String, String> cookieByResponse = Maps.newHashMap();
        HttpRequestBase httpRequestBase = RequestHandle.gethttpRequestBase(requestBean);
        if (httpRequestBase == null) {
            return cookieByResponse;
        }
        CloseableHttpResponse response = null;
        try {
            response = httpClient.execute(httpRequestBase, localContext);
            cookieByResponse = getCookieByResponse(response);
            //logger.info("cookieByResponse:" + cookieByResponse);
            //logger.debug(EntityUtils.toString(response.getEntity(), "utf-8"));
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (response != null) {
                    EntityUtils.consume(response.getEntity());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            httpRequestBase.releaseConnection();
        }
        return cookieByResponse;
    }

    /**
     * 可忽略的cookie列表
     */
    private static final List<String> COOKIE_IGNORE_LIST = Lists.newArrayList("Path", "Expires", "Domain");

    private Map<String, String> getCookieByResponse(HttpResponse response) {
        HashMap<String, String> cookies = new HashMap<>();
        Header[] headers = response.getHeaders("Set-Cookie");
        for (Header header : headers) {
            String value = header.getValue();
            String[] split = value.split("; ");
            for (String one : split) {
                String[] ck = one.split("=");
                if (ck.length == 2) {
                    if (!COOKIE_IGNORE_LIST.contains(ck[0])) {
                        cookies.put(ck[0], ck[1]);
                    }
                }
            }
        }
        return cookies;
    }

    private String getCookieStore() {
        List<Cookie> cookies = cookieStore.getCookies();
        String cookieStr = CookieUtils.getCookieStr(cookies);
        return cookieStr;
    }

    private void close() {
        try {
            httpClient.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //    /**
    //     * 发送post请求
    //     *
    //     * @param url
    //     * @param params
    //     * @return
    //     */
    //    public ResponseBean sendPostRequest(String url, Map<String, String> params) {
    //        HttpPost httpPost = new HttpPost(url);
    //        List<BasicNameValuePair> nvps = new ArrayList<>();
    //        for (String key : params.keySet()) {
    //            if (key != null) {
    //                nvps.add(new BasicNameValuePair(key, params.get(key)));
    //            }
    //        }
    //        try {
    //            httpPost.setEntity(new UrlEncodedFormEntity(nvps, "UTF-8"));
    //        } catch (UnsupportedEncodingException e) {
    //            e.printStackTrace();
    //        }
    //        return sendRequstGetPage(httpPost);
    //    }
    //
    //    public ResponseBean sendPostRequest(String url, String headers, String form) {
    //        HashMap<String, String> headersMap = RequestHandle.str2map(headers);
    //        HashMap<String, String> formMap = RequestHandle.str2map(form);
    //        return sendPostRequest(url, headersMap, formMap);
    //    }
    //
    //    public ResponseBean sendPostRequest(String url, Map<String, String> headersMap, Map<String, String> formMap) {
    //        HttpPost httpPost = new HttpPost(url);
    //        headersMap.remove("Content-Length");// 不移除则:Caused by: org.apache.http.ProtocolException: Content-Length header already present
    //        for (String key : headersMap.keySet()) {
    //            httpPost.addHeader(key, headersMap.get(key));
    //        }
    ////        httpPost.setEntity(formMap2UrlEncodedFormEntity(formMap));
    //        httpPost.setEntity(RequestHandle.formMap2StringEntity(formMap));
    //        return sendRequstGetPage(httpPost);
    //    }
    //
    //
    //    public ResponseBean sendPostRequest(String url, Map<String, String> headersMap, String formMap) {
    //        HttpPost httpPost = new HttpPost(url);
    //        headersMap.remove("Content-Length");// 不移除则:Caused by: org.apache.http.ProtocolException: Content-Length header already present
    //        for (String key : headersMap.keySet()) {
    //            httpPost.addHeader(key, headersMap.get(key));
    //        }
    //        StringEntity reqEntity = null;
    //        try {
    //            reqEntity = new StringEntity(formMap);
    //        } catch (UnsupportedEncodingException e) {
    //            e.printStackTrace();
    //        }
    //        httpPost.setEntity(reqEntity);
    //        return sendRequstGetPage(httpPost);
    //    }

}

package com.tyaer.net.httpclient.handle;

import com.tyaer.net.httpclient.bean.HttpMethodType;
import com.tyaer.net.httpclient.bean.RequestBean;
import com.tyaer.net.httpclient.bean.ResponseBean;
import com.tyaer.net.httpclient.manager.HttpClientManagerParams;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.cookie.Cookie;
import org.apache.http.cookie.CookieOrigin;
import org.apache.http.cookie.CookieSpec;
import org.apache.http.cookie.CookieSpecProvider;
import org.apache.http.cookie.MalformedCookieException;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.cookie.DefaultCookieSpec;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HttpContext;
import org.apache.log4j.Logger;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Twin on 2017/5/16.
 */
public class RequestHandle {
    private static final Logger logger = Logger.getLogger(RequestHandle.class);

    public static HttpRequestBase gethttpRequestBase(RequestBean requestBean) {
        return gethttpRequestBase(requestBean, null);
    }

    public static HttpRequestBase gethttpRequestBase(RequestBean requestBean, RequestConfig requestConfig) {
        String url = requestBean.getUrl();
        HttpRequestBase httpRequestBase = null;
        HttpMethodType httpMethodType = requestBean.getHttpMethodType();
        try {
            switch (httpMethodType) {
                case GET:
                    // 搜索链接
                    httpRequestBase = new HttpGet(url);
                    break;
                case POST:
                    httpRequestBase = new HttpPost(requestBean.getUrl());
                    HashMap<String, String> parameter = requestBean.getParameters();
                    if (requestBean.isUrlIsEncode()) {
                        ((HttpPost) httpRequestBase).setEntity(formMap2StringEntity(parameter));
                    } else {
                        ((HttpPost) httpRequestBase).setEntity(formMap2UrlEncodedFormEntity(parameter, requestBean.getUrlEncodeCharset()));
                    }
                    break;
                default:
                    logger.error("HttpMethodType类型错误或尚未支持！");
                    return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (httpRequestBase == null) {
            return null;
        }
        HashMap<String, String> headers = requestBean.getHeaders();
        if (headers != null) {
            for (String key : headers.keySet()) {
                httpRequestBase.addHeader(key, headers.get(key));
            }
        } else {
            //使用默认设置
            RequestHandle.setHttpRequestHeader(httpRequestBase, null);
        }
        if (requestConfig != null) {
            httpRequestBase.setConfig(requestConfig);
        } else {
            httpRequestBase.setConfig(getRequestConfig());
        }
        return httpRequestBase;
    }

    public static RequestConfig getRequestConfig() {
        int time = 5000;
        return getRequestConfig(time, time);
    }

    public static RequestConfig getRequestConfig(int connectTimeout, int socketTimeout) {
        int time = 5000;
        return RequestConfig.custom()
                .setConnectionRequestTimeout(time)//获取连接的最大等待时间
                .setConnectTimeout(connectTimeout)//连接超时时间
                .setSocketTimeout(socketTimeout)//读取超时时间
                .setCookieSpec(CookieSpecs.STANDARD_STRICT)
                .build();
    }

    public static void setHttpRequestHeader(HttpRequestBase httpRequestBase, ResponseBean page) {
        if (httpRequestBase.getHeaders("User-Agent").length == 0) {
            // 搜索头
            String userAgent = HttpClientManagerParams.getRandomUsersAgent();
//        String userAgent = "Mozilla/5.0";
//        String userAgent = "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1; .NET CLR 1.1.4322; Alexa Toolbar; TencentTraveler 4.0)";
//        page.getRequest().setUserAgent(userAgent);
//        System.out.println(userAgent);
            httpRequestBase.addHeader("User-Agent", userAgent);//TODO 请求头与目标网站的兼容性,例如：http://sz.ganji.com/fang5/
            httpRequestBase.setHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
            // httpGet.addHeader("Accept-Encoding", "gzip,deflate,sdch");//乱码
            httpRequestBase.setHeader("Accept-Language", "zh-CN,zh;q=0.8");
            httpRequestBase.setHeader("Cache-Control", "no-cache");
            httpRequestBase.setHeader("Pragma", "no-cache");
//          httpRequestBase.setHeader("Connection", "Keep-Alive");
            httpRequestBase.setHeader("Connection", "close"); //会有传输中断异常TruncatedChunkException
        }
    }

    public static HashMap<String, String> str2map(String str) {
        if (StringUtils.isBlank(str)) {
            return null;
        }
        HashMap<String, String> map = new HashMap<>();
        String[] split = str.split("[\n&]");
        for (String s : split) {
            if (StringUtils.isNotBlank(s)) {
                String[] split1 = s.split("[:=]", 2);
                String key = split1[0].trim();
                String value;
                if (split1.length == 1 || StringUtils.isBlank(split1[1])) {
                    value = "";
                } else {
                    value = split1[1].trim();
                }
//                if(StringUtils.isNotBlank(value)){
//                    map.put(key, value);
//                }
                map.put(key, value);
            }
        }
        return map;
    }

    public static UrlEncodedFormEntity formMap2UrlEncodedFormEntity(Map<String, String> formMap, String charset) {
        if (formMap == null) {
            return null;
        }
        List<BasicNameValuePair> nvps = new ArrayList<>();
        for (String key : formMap.keySet()) {
            nvps.add(new BasicNameValuePair(key, formMap.get(key)));
        }
        UrlEncodedFormEntity urlEncodedFormEntity = null;
        try {
            urlEncodedFormEntity = new UrlEncodedFormEntity(nvps, charset);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return urlEncodedFormEntity;
    }

    public static StringEntity formMap2StringEntity(Map<String, String> formMap) {
        if (formMap == null) {
            return null;
        } else {
            StringBuilder stringBuilder = new StringBuilder();
            for (String key : formMap.keySet()) {
                stringBuilder.append(key + "=" + formMap.get(key));
                stringBuilder.append("&");
            }
            StringEntity reqEntity = null;
            try {
                String string = stringBuilder.toString();
                reqEntity = new StringEntity(string);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            return reqEntity;
        }
    }



}

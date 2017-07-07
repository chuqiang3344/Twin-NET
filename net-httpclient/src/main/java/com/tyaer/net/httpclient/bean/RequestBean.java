package com.tyaer.net.httpclient.bean;

import com.tyaer.net.httpclient.handle.RequestHandle;
import org.apache.http.HttpHost;

import java.util.HashMap;

/**
 * Created by Twin on 2016/9/22.
 */
public class RequestBean {
    private String url;
    //    private String type = "get";
    private HttpMethodType httpMethodType = HttpMethodType.GET;

    private int retriesNum = 3;

    private HashMap<String, String> headers;
    private HashMap<String, String> parameters;
    private HttpHost httpHost;

    // Url的参数是否已经Encode
    private boolean urlIsEncode = true;
    private String urlEncodeCharset = "utf-8";


    public RequestBean(String url, HttpMethodType httpMethodType, HashMap<String, String> headers, HashMap<String, String> parameters) {
        init(url, httpMethodType, headers, parameters);
    }

    public RequestBean(String url, HttpMethodType httpMethodType, String headers, String parameters) {
        init(url, httpMethodType, RequestHandle.str2map(headers), RequestHandle.str2map(parameters));
    }

    public RequestBean(String url) {
        init(url, HttpMethodType.GET, null, null);
    }

    /**
     * 放在ResponseBean里面，作为回传显示
     */
    public RequestBean() {
    }

    private void init(String url, HttpMethodType httpMethodType, HashMap<String, String> headers, HashMap<String, String> parameters) {
        this.url = url.trim();
        this.httpMethodType = httpMethodType;
        if (headers == null) {
            this.headers = getDefaultHeaders();
        } else {
            this.headers = headers;
            // 不移除则:Caused by: org.apache.http.ProtocolException: Content-Length header already present
            headers.remove("Content-Length");
        }
        this.parameters = parameters;
    }

    private HashMap<String, String> getDefaultHeaders() {
        HashMap<String, String> defaultHeaders = new HashMap<>();
        defaultHeaders.put("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Maxthon/5.0.4.3000 Chrome/47.0.2526.73 Safari/537.36");
//        defaultHeaders.put("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:53.0) Gecko/20100101 Firefox/53.0");
//        defaultHeaders.put("Accept-Encoding", "");
//        defaultHeaders.put("Accept-Encoding", "gzip,deflate,sdch");//乱码
        defaultHeaders.put("Accept-Language", "zh-CN,zh;q=0.8");
        defaultHeaders.put("Cache-Control", "no-cache");
        defaultHeaders.put("Pragma", "no-cache");
//        httpRequestBase.setHeader("Connection", "Keep-Alive");
        defaultHeaders.put("Connection", "close");
        return defaultHeaders;
    }

    public String getUrlEncodeCharset() {
        return urlEncodeCharset;
    }

    public void setUrlEncodeCharset(String urlEncodeCharset) {
        this.urlEncodeCharset = urlEncodeCharset;
    }

    public boolean isUrlIsEncode() {
        return urlIsEncode;
    }

    public void setUrlIsEncode(boolean urlIsEncode) {
        this.urlIsEncode = urlIsEncode;
    }

    public void updateHeaders(String key, String value) {
        if (headers != null) {
            headers.put(key, value);
        }
    }

    public void updateParameters(String key, String value) {
        if (parameters != null) {
            parameters.put(key, value);
        }
    }

    public HashMap<String, String> getHeaders() {
        return headers;
    }

    public void setHeaders(HashMap<String, String> headers) {
        this.headers = headers;
    }

    public HashMap<String, String> getParameters() {
        return parameters;
    }

    public void setParameters(HashMap<String, String> parameters) {
        this.parameters = parameters;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public HttpMethodType getHttpMethodType() {
        return httpMethodType;
    }

    public void setHttpMethodType(HttpMethodType httpMethodType) {
        this.httpMethodType = httpMethodType;
    }

    @Override
    public String toString() {
        return "RequestBean{" +
                "url='" + url + '\'' +
                ", httpMethodType=" + httpMethodType +
                ", retriesNum=" + retriesNum +
                ", headers=" + headers +
                ", parameters=" + parameters +
                ", httpHost=" + httpHost +
                ", urlIsEncode=" + urlIsEncode +
                ", urlEncodeCharset='" + urlEncodeCharset + '\'' +
                '}';
    }

    public HttpHost getHttpHost() {
        return httpHost;
    }

    public void setHttpHost(HttpHost httpHost) {
        this.httpHost = httpHost;
    }

    public int getRetriesNum() {
        return retriesNum;
    }

    public void setRetriesNum(int retriesNum) {
        this.retriesNum = retriesNum;
    }

}

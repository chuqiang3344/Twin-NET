package com.tyaer.net.proxy;

import com.tyaer.net.httpclient.bean.ResponseBean;
import com.tyaer.net.httpclient.downloader.HttpClientDownloader;
import org.apache.http.HttpHost;
import org.junit.Test;

/**
 * Created by Twin on 2017/2/22.
 */
public class Proxy_Test {
    private static HttpClientDownloader httpClientDownloader =new HttpClientDownloader();
    public static void main(String[] args) {
//        HttpHost httpHost = new HttpHost("39.86.60.118", 9999);
        HttpHost httpHost = null;
        String targetUrl = "http://www.ip181.com/";
        ResponseBean responseBean = httpClientDownloader.sendRequest(targetUrl, httpHost);
        System.out.println(responseBean);
    }

    @Test
    public void foreign_proxy() {
//        HttpHost httpHost=null;
        HttpHost httpHost = new HttpHost("svnserver", 808);
//        String url = "https://www.google.com.hk/search?q=%E7%BD%97%E6%9B%BC%E8%92%82%E5%85%8B&num=50&safe=strict&tbm=nws&ei=YRBRWJeWE8Kg0gSg2IDADw&start=0&sa=N&biw=1441&bih=423&dpr=1";
//        String url = "https://twitter.com/search?f=tweets&vertical=default&q=上海&src=typd";
        String url = "https://cn.nytimes.com/zh-hant";
        ResponseBean responseBean = httpClientDownloader.sendRequest(url, httpHost);
        System.out.println(responseBean);
    }
}

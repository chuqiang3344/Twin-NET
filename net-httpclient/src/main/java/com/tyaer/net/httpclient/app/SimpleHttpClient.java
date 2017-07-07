package com.tyaer.net.httpclient.app;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.CookieStore;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;

import java.util.List;

/**
 * Created by Twin on 2017/5/27.
 */
public class SimpleHttpClient {
    public static void main(String[] args) {
        CloseableHttpClient client = HttpClients.createDefault();
        String url="http://sz.ganji.com/fang5/";
        HttpGet httpGet = new HttpGet(url);
        httpGet.setConfig(RequestConfig.custom().setCookieSpec(CookieSpecs.STANDARD_STRICT).build());
//        httpGet.setHeader("Host", "www.kuaidaili.com");
////        httpGet.setHeader("User-Agent", "Mozilla/5.0(Windows;U;WindowsNT6.1;rv:2.2)Gecko/20110201");
////        httpGet.setHeader("User-Agent", "Mozilla/5.0 (Linux; Android 5.0.1; MX4 Build/LRX22C) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/37.0.0.0 Mobile MQQBrowser/6.8 TBS/036872 Safari/537.36 MicroMessenger/6.3.30.920 NetType/WIFI Language/zh_CN");
////        httpGet.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/39.0.2171.95 Safari/537.36 MicroMessenger/6.5.2.501 NetType/WIFI WindowsWechat");
//        httpGet.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Maxthon/5.0.2.1000 Chrome/47.0.2526.73 Safari/537.36");
//        httpGet.setHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
////        httpGet.setHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
//        httpGet.setHeader("Connection", "keep-alive");
//        httpGet.setHeader("Accept-Encoding", "gzip, deflate");
//        httpGet.setHeader("Accept-Language", "zh-CN");
//        httpGet.setHeader("Upgrade-Insecure-Requests", "1");
//        httpGet.setHeader("X-DevTools-Emulate-Network-Conditions-Client-Id", "4CABA570-97DD-4045-BA5C-436BB14EFE72");
////        httpGet.setHeader("Referer", "https://www.baidu.com/");
////        httpGet.setHeader("Origin", "https://login.sina.com.cn");

        String result=null;
        try {
            // 创建一个本地Cookie存储的实例
            CookieStore cookieStore = new BasicCookieStore();
            //创建一个本地上下文信息
            HttpContext localContext = new BasicHttpContext();
            //在本地上下问中绑定一个本地存储
            localContext.setAttribute(ClientContext.COOKIE_STORE, cookieStore);
            HttpResponse response = client.execute(httpGet, localContext);
            HttpEntity entity = response.getEntity();
            List<Cookie> cookies = cookieStore.getCookies();
//            result = HttpHelper.getCookieStr(cookies);
            System.out.println(response.getStatusLine().getStatusCode());
            result= IOUtils.toString(entity.getContent(),"utf-8");

            EntityUtils.consume(entity);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            httpGet.releaseConnection();
        }
//        System.out.println("cookie:" + result);
//        return result;
        System.out.println(result);
    }
}

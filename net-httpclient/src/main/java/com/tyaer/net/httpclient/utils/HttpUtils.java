package com.tyaer.net.httpclient.utils;

import com.tyaer.net.httpclient.handle.RequestHandle;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.cookie.Cookie;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.*;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.util.EntityUtils;
import org.junit.Test;

import javax.net.ssl.SSLContext;
import java.io.*;
import java.nio.charset.Charset;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Twin on 2016/11/16.
 */
public class HttpUtils {
    private static CloseableHttpClient client =createSSLClientDefault();
//    private static CloseableHttpClient client =HttpClients.createDefault();

    private static CookieStore cookieStore;
    private static HttpContext localContext;

    static {
        // 创建一个本地Cookie存储的实例
        cookieStore = new BasicCookieStore();
        //创建一个本地上下文信息
        localContext = new BasicHttpContext();
        //在本地上下问中绑定一个本地存储
        localContext.setAttribute(HttpClientContext.COOKIE_STORE, cookieStore);
    }

    /**
     * 描述：针对https采用SSL的方式创建httpclient
     *
     * @return
     */
    public static CloseableHttpClient createSSLClientDefault() {
        try {
            SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(null, new TrustStrategy() {
                //信任所有
                public boolean isTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                    return true;
                }
            }).build();
            SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslContext);
            return HttpClients.custom().setSSLSocketFactory(sslsf).build();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        }
        return HttpClients.createDefault();
    }

    public String downLoadImg(String url){
        HttpGet httpGet = new HttpGet(url);
        String result = null;
        try {
            CloseableHttpResponse httpResponse = client.execute(httpGet);
            InputStream inputStream = httpResponse.getEntity().getContent();
            File file = new File("./file/baidu.jpg");
            OutputStream outputStream = new FileOutputStream(file);
            IOUtils.copy(inputStream,outputStream);
            outputStream.flush();

            outputStream.close();
            inputStream.close();
            result=file.getAbsolutePath();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            httpGet.releaseConnection();
        }
        return result;
    }

    public static String getCookieStr(List<Cookie> cookieList) {
        StringBuffer buffer = new StringBuffer();
        for (Cookie cookie : cookieList) {
            buffer.append(cookie.getName()).append("=").append(cookie.getValue()).append(";");
        }
        String cookie_str = buffer.toString();
        return cookie_str;
    }


    public static String post(String loginUrl, HashMap<String,String> paramsMap){
        HttpPost post = new HttpPost(loginUrl);
//        post.setConfig(RequestConfig.custom().setCookieSpec(CookieSpecs.STANDARD_STRICT).build());
//        post.setHeader("User-Agent", "Mozilla/5.0(Windows;U;WindowsNT6.1;rv:2.2)Gecko/20110201");
//        post.setHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
        post.setHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
//        post.setHeader("Referer", "http://weixin.sogou.com/antispider/?from=%2fweixin%3Ftype%3d2%26query%3d%E8%87%AA%E8%A1%8C%E8%BD%A6%26ie%3dutf8%26_sug_%3dy%26_sug_type_%3d");
//        post.setHeader("Host", "weixin.sogou.com");
//        post.setHeader("Origin", "http://tieba.baidu.com");
//        String cookie="BAIDUID=36BED8CAD0D134B4A713ED4D0F0B8119:FG=1;BDUSS=BvYlFBSmc1a1JqUUFXamR3NGxVMFNubWh2Y3VNb3NFYnpoVzRDQjRXWDRHVkZZSVFBQUFBJCQAAAAAAAAAAAEAAACd8KMIY2h1cWlhbmczMzQ0AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAPiMKVj4jClYNV;BIDUPSID=36BED8CAD0D134B4A713ED4D0F0B8119;PSTM=1479118073;BDRCVFR[laz3IxV61qm]=mbxnW11j9Dfmh7GuZR8mvqV;BD_HOME=1;H_PS_PSSID=17942;__bsi=13197132025009865138_00_14_N_N_97_0303_C02F_N_N_N_0;BD_UPN=1123314251;";
//        String cookie="BAIDUID=08B969F39AABC5AF0CDFF942237DB6E1:FG=1; BIDUPSID=08B969F39AABC5AF0CDFF942237DB6E1; PSTM=1479108894; BDRCVFR[EzYDMnK0Xs6]=mk3SLVN4HKm; BDRCVFR[laz3IxV61qm]=mk3SLVN4HKm; TIEBAUID=512142465a71521c730a710a; TIEBA_USERTYPE=9d2ff1ebe93dd527e01c2f37; bdshare_firstime=1479118177616; Hm_lvt_287705c8d9e2073d13275b18dbd746dc=1479118179; Hm_lpvt_287705c8d9e2073d13275b18dbd746dc=1479118179; H_PS_PSSID=1450_21422_17948_21080_18559_21455_21409_21378_21526_21190_21306; BDUSS=R2UU1YVzVKTW1zaW52eFBlenhVbVRlLVlLVVZJWGpEakRmN3lCeVpQSUNJMUZZSVFBQUFBJCQAAAAAAAAAAAEAAACWZkiOOTZhMzc2NAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAKWKVgClilYMF; wise_device=0; LONGID=2387109526";
//        String cookie="ABTEST=0|1484097170|v1; IPLOC=CN4403; SUID=FBFD0FB7771A910A0000000058758692; SUID=FBFD0FB73320910A0000000058758693; weixinIndexVisited=1; SUV=0048178AB70FFDFB58758693A293E694; JSESSIONID=aaaPQEfJP1gZHh4NNx7Kv; PHPSESSID=vcc6af770cq97jsevfkoa2btu2; SUIR=1484097221";
//        String cookie="ABTEST=0|1484097170|v1; IPLOC=CN4403; SUID=FBFD0FB7771A910A0000000058758692; SUID=FBFD0FB73320910A0000000058758693; weixinIndexVisited=1; SUV=0048178AB70FFDFB58758693A293E694; JSESSIONID=aaaPQEfJP1gZHh4NNx7Kv; PHPSESSID=vcc6af770cq97jsevfkoa2btu2; SUIR=1484097221";
        String cookie="UM_distinctid=15ba4010d5b0-0a2882b91-6c337361-13c680-15ba4010d5c4e9; yqt365=userSId_yqt365_sztesthxzh853_42259; CNZZDATA1260848629=386922560-1493105038-null%7C1494324526; JSESSIONID=67A36423522B6D2A9BCE1ADA3775D2FA";

        post.setHeader("Cookie",cookie);

        List<NameValuePair> params = new ArrayList<NameValuePair>();
        for (String key : paramsMap.keySet()) {
            params.add(new BasicNameValuePair(key, paramsMap.get(key)));
        }
//        String req="admin.userId=25384&timeDomain=3&searchKeyword=%E4%B8%8A%E6%B5%B7%E7%A8%8E%E5%8A%A1&paixu=1&secondSearchWord=&starttime=2017-05-07+00%3A00%3A00&endtime=2017-05-09+23%3A59%3A59&newlstSelect=2&filterOrigina=5&duplicateShow=0&otherAttribute=0&searchPipeiType=2";
//        String req="admin.userId=25384&timeDomain=3&searchKeyword=深圳滑坡事故&paixu=1&secondSearchWord=&starttime=2017-05-07+00%3A00%3A00&endtime=2017-05-09+23%3A59%3A59&newlstSelect=2&filterOrigina=5&duplicateShow=0&otherAttribute=0&searchPipeiType=2";
//        String req="admin.userId=25384&timeDomain=3&searchKeyword=深圳滑坡事故&paixu=1&secondSearchWord=&starttime=2017-05-07+00%3A00%3A00&endtime=2017-05-09+23%3A59%3A59&newlstSelect=2&filterOrigina=5&duplicateShow=0&otherAttribute=0&searchPipeiType=2";
        String form="admin.userId:25384\n" +
                "timeDomain:3\n" +
                "searchKeyword:深圳滑坡事故\n" +
                "paixu:1\n" +
                "secondSearchWord:\n" +
                "starttime:2017-05-07+00%3A00%3A00\n" +
                "endtime:2017-05-09+23%3A59%3A59\n" +
                "newlstSelect:2\n" +
                "filterOrigina:5\n" +
                "duplicateShow:0\n" +
                "otherAttribute:0\n" +
                "searchPipeiType:2";
        String req=form.replace("\n","&").replace(":","=");

        StringEntity reqEntity= null;
        try {
            reqEntity = new StringEntity(req);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        post.setEntity(reqEntity);

        String result = "";
        try {
            UrlEncodedFormEntity eparams = new UrlEncodedFormEntity(params, "gb2312");
//            post.setEntity(eparams);
//            HttpResponse response = client.execute(post, localContext);

            HttpResponse response = client.execute(post);
//            HttpResponse response = HttpClients.createDefault().execute(post);

            HttpEntity entity = response.getEntity();
//            List<Cookie> cookies = cookieStore.getCookies();
//            String cookieStr = getCookieStr(cookies);
//            System.out.println(cookieStr);
            result=IOUtils.toString(entity.getContent(),"utf-8");
            System.out.println("result :"+result);
            EntityUtils.consume(entity);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            post.releaseConnection();
        }
//        System.out.println("cookie:" + result);
        return result;
    }

    public static String get(String url,String cookie){
        HttpGet httpGet = new HttpGet(url);
        httpGet.setConfig(RequestConfig.custom().setCookieSpec(CookieSpecs.STANDARD_STRICT).build());
//        httpGet.setHeader("Host", "www.kuaidaili.com");
        if(cookie!=null){
            httpGet.setHeader("Cookie", cookie);
        }
//        httpGet.setHeader("User-Agent", "Mozilla/5.0(Windows;U;WindowsNT6.1;rv:2.2)Gecko/20110201");
//        httpGet.setHeader("User-Agent", "Mozilla/5.0 (Linux; Android 5.0.1; MX4 Build/LRX22C) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/37.0.0.0 Mobile MQQBrowser/6.8 TBS/036872 Safari/537.36 MicroMessenger/6.3.30.920 NetType/WIFI Language/zh_CN");
//        httpGet.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/39.0.2171.95 Safari/537.36 MicroMessenger/6.5.2.501 NetType/WIFI WindowsWechat");
        httpGet.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Maxthon/5.0.2.1000 Chrome/47.0.2526.73 Safari/537.36");
        httpGet.setHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
//        httpGet.setHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
        httpGet.setHeader("Connection", "keep-alive");
        httpGet.setHeader("Accept-Encoding", "gzip, deflate");
        httpGet.setHeader("Accept-Language", "zh-CN");
        httpGet.setHeader("Upgrade-Insecure-Requests", "1");
        httpGet.setHeader("X-DevTools-Emulate-Network-Conditions-Client-Id", "4CABA570-97DD-4045-BA5C-436BB14EFE72");
//        httpGet.setHeader("Referer", "https://www.baidu.com/");
//        httpGet.setHeader("Origin", "https://login.sina.com.cn");

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
            result=IOUtils.toString(entity.getContent(),"utf-8");

            EntityUtils.consume(entity);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            httpGet.releaseConnection();
        }
//        System.out.println("cookie:" + result);
        return result;
    }

    public static String httpRequest(String url) {

        RequestConfig config = RequestConfig.custom().setSocketTimeout(30000)
                .setConnectionRequestTimeout(30000).build();
        HttpClientBuilder httpClientBuilder = HttpClientBuilder.create()
                .setDefaultRequestConfig(config)
                .setRetryHandler(new DefaultHttpRequestRetryHandler());
        CloseableHttpClient closeableHttpClient = httpClientBuilder.build();
        HttpGet HttpGet = new HttpGet(url);
        HttpResponse resp = null;
        String html = "";
        try {
            resp = closeableHttpClient.execute(HttpGet);
        } catch (IOException e) {
            System.out.println("Request Error");
        }
        if (resp != null && resp.getStatusLine().getStatusCode() == 200) {
            HttpEntity entity = resp.getEntity();
            try {
                html = EntityUtils.toString(entity, Charset.forName("utf-8"));
            } catch (ParseException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            HttpGet.releaseConnection();
            try {
                closeableHttpClient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return html;
    }



    public static void main(String[] args) {
//        String url = "http://weibo.com/n/%E6%A2%A8%E8%A7%86%E9%A2%91?from=feed&amp;loc=at";
//        String url = "http://weibo.com/n/%E6%A2%A8%E8%A7%86%E9%A2%91?from=feed&amp;loc=at";
//        try {
////            url= URLEncoder.encode(url,"utf-8");
//            url= URLDecoder.decode(url,"utf-8");
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }
//        System.out.println(url);
//        String cookie = "SINAGLOBAL=8349599072244.019.1487581065415; login_sid_t=db78595b4fd6651582f19e1230120a6e; YF-Ugrow-G0=1eba44dbebf62c27ae66e16d40e02964; YF-V5-G0=1da707b8186f677d9e4ad50934b777b3; _s_tentry=-; Apache=2893894922453.91.1489991631074; ULV=1489991631078:5:3:1:2893894922453.91.1489991631074:1488505463768; __utma=15428400.1213720716.1489993964.1489993964.1489993964.1; __utmc=15428400; __utmz=15428400.1489993964.1.1.utmcsr=(direct)|utmccn=(direct)|utmcmd=(none); YF-Page-G0=7b9ec0e98d1ec5668c6906382e96b5db; SUB=_2AkMvkwuFf8NhqwJRmP4Ry2jqao1yzQ7EieKZz_peJRMxHRl-yj83qnJctRAQ8otN258M1ShgfkobIkBg97WFTw..; SUBP=0033WrSXqPxfM72-Ws9jqgMF55529P9D9W5nlxOXXKbBuNRjplAyI_Sn";
//
////        String url="http://mp.weixin.qq.com/mp/getmasssendmsg?__biz=MTI0MDU3NDYwMQ==#wechat_webview_type=1&wechat_redirect";
//        String s = get(url,cookie);
//        System.out.println(s);

        String url="http://www.yqt365.com/getStatList.action";
        String form="admin.userId:25384\n" +
                "timeDomain:3\n" +
                "searchKeyword:深圳滑坡事故\n" +
                "paixu:1\n" +
                "secondSearchWord:\n" +
                "starttime:2017-05-07 00:00:00\n" +
                "endtime:2017-05-09 23:59:59\n" +
                "newlstSelect:2\n" +
                "filterOrigina:5\n" +
                "duplicateShow:0\n" +
                "otherAttribute:0\n" +
                "searchPipeiType:2";

        System.out.println(post(url, RequestHandle.str2map(form)));

    }

    @Test
    public void tt(){
        String url = "https://idol001.com/news/7004/detail/580d81fd7a11731f778b4725/";
//        String cookie="_ydclearance=00adc2408cdfe7c2ae43ab38-a1c3-418a-811d-7f2212b778b8-1490758681; channelid=0; sid=1490750826307234; _ga=GA1.2.293650114.1490751478; Hm_lvt_7ed65b1cc4b810e9fd37959c9bb51b31=1490751478; Hm_lpvt_7ed65b1cc4b810e9fd37959c9bb51b31=1490751484\n";
        String cookie=null;
        String rawText1 = get(url,null);
        System.out.println(rawText1);
        String rawText = httpRequest(url);
        System.out.println(rawText);
    }
}

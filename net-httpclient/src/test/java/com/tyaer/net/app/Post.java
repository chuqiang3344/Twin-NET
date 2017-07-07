package com.tyaer.net.app;

import com.tyaer.net.httpclient.bean.HttpMethodType;
import com.tyaer.net.httpclient.bean.RequestBean;
import com.tyaer.net.httpclient.downloader.HttpClientDownloader;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.junit.Test;

import java.io.UnsupportedEncodingException;

/**
 * Created by Twin on 2017/5/9.
 */
public class Post {
    static HttpClientDownloader httpClientDownloader=new HttpClientDownloader();

    public static void main(String[] args) {

        String url = "http://weibo.com/n/%E6%A2%A8%E8%A7%86%E9%A2%91?from=feed&amp;loc=at";
        HttpPost post = new HttpPost(url);
        post.setHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
//        post.setHeader("Referer", "http://weixin.sogou.com/antispider/?from=%2fweixin%3Ftype%3d2%26query%3d%E8%87%AA%E8%A1%8C%E8%BD%A6%26ie%3dutf8%26_sug_%3dy%26_sug_type_%3d");
//        post.setHeader("Host", "weixin.sogou.com");
//        post.setHeader("Origin", "http://tieba.baidu.com");
//        String cookie="BAIDUID=36BED8CAD0D134B4A713ED4D0F0B8119:FG=1;BDUSS=BvYlFBSmc1a1JqUUFXamR3NGxVMFNubWh2Y3VNb3NFYnpoVzRDQjRXWDRHVkZZSVFBQUFBJCQAAAAAAAAAAAEAAACd8KMIY2h1cWlhbmczMzQ0AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAPiMKVj4jClYNV;BIDUPSID=36BED8CAD0D134B4A713ED4D0F0B8119;PSTM=1479118073;BDRCVFR[laz3IxV61qm]=mbxnW11j9Dfmh7GuZR8mvqV;BD_HOME=1;H_PS_PSSID=17942;__bsi=13197132025009865138_00_14_N_N_97_0303_C02F_N_N_N_0;BD_UPN=1123314251;";
//        String cookie="BAIDUID=08B969F39AABC5AF0CDFF942237DB6E1:FG=1; BIDUPSID=08B969F39AABC5AF0CDFF942237DB6E1; PSTM=1479108894; BDRCVFR[EzYDMnK0Xs6]=mk3SLVN4HKm; BDRCVFR[laz3IxV61qm]=mk3SLVN4HKm; TIEBAUID=512142465a71521c730a710a; TIEBA_USERTYPE=9d2ff1ebe93dd527e01c2f37; bdshare_firstime=1479118177616; Hm_lvt_287705c8d9e2073d13275b18dbd746dc=1479118179; Hm_lpvt_287705c8d9e2073d13275b18dbd746dc=1479118179; H_PS_PSSID=1450_21422_17948_21080_18559_21455_21409_21378_21526_21190_21306; BDUSS=R2UU1YVzVKTW1zaW52eFBlenhVbVRlLVlLVVZJWGpEakRmN3lCeVpQSUNJMUZZSVFBQUFBJCQAAAAAAAAAAAEAAACWZkiOOTZhMzc2NAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAKWKVgClilYMF; wise_device=0; LONGID=2387109526";
//        String cookie="ABTEST=0|1484097170|v1; IPLOC=CN4403; SUID=FBFD0FB7771A910A0000000058758692; SUID=FBFD0FB73320910A0000000058758693; weixinIndexVisited=1; SUV=0048178AB70FFDFB58758693A293E694; JSESSIONID=aaaPQEfJP1gZHh4NNx7Kv; PHPSESSID=vcc6af770cq97jsevfkoa2btu2; SUIR=1484097221";
//        String cookie="ABTEST=0|1484097170|v1; IPLOC=CN4403; SUID=FBFD0FB7771A910A0000000058758692; SUID=FBFD0FB73320910A0000000058758693; weixinIndexVisited=1; SUV=0048178AB70FFDFB58758693A293E694; JSESSIONID=aaaPQEfJP1gZHh4NNx7Kv; PHPSESSID=vcc6af770cq97jsevfkoa2btu2; SUIR=1484097221";
        String cookie="UM_distinctid=15ba4010d5b0-0a2882b91-6c337361-13c680-15ba4010d5c4e9; yqt365=userSId_yqt365_sztesthxzh853_42259; CNZZDATA1260848629=386922560-1493105038-null%7C1494319098; JSESSIONID=0CAB95804A1FC73B547151E24734B50B";
        post.setHeader("Cookie",cookie);
        String req="admin.userId=25384&timeDomain=3&searchKeyword=%E4%B8%8A%E6%B5%B7%E7%A8%8E%E5%8A%A1&paixu=1&secondSearchWord=&starttime=2017-05-07+00%3A00%3A00&endtime=2017-05-09+23%3A59%3A59&newlstSelect=2&filterOrigina=5&duplicateShow=0&otherAttribute=0&searchPipeiType=2";
        StringEntity reqEntity= null;
        try {
            reqEntity = new StringEntity(req);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        post.setEntity(reqEntity);
        String result = "";
        try {
//            UrlEncodedFormEntity eparams = new UrlEncodedFormEntity(params, "utf-8");
//            post.setEntity(eparams);
//            HttpResponse response = client.execute(post, localContext);

//            HttpResponse response = client.execute(post);
            HttpResponse response = HttpClients.createDefault().execute(post);

            HttpEntity entity = response.getEntity();
//            List<Cookie> cookies = cookieStore.getCookies();
//            String cookieStr = getCookieStr(cookies);
//            System.out.println(cookieStr);
            result= IOUtils.toString(entity.getContent(),"utf-8");
            System.out.println("result :"+result);
            EntityUtils.consume(entity);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            post.releaseConnection();
        }
//        System.out.println("cookie:" + result);
    }

    @Test
    public void login(){
        String url="http://www.yqt365.com/ajaxCheckLogin.action";
        String headers="Accept:*/*\n" +
                "Accept-Encoding:gzip, deflate\n" +
                "Accept-Language:zh-CN\n" +
                "Connection:keep-alive\n" +
                "Content-Length:43\n" +
                "Content-Type:application/x-www-form-urlencoded; charset=UTF-8\n" +
                "Cookie:JSESSIONID=28F799917A4BD544D5BED067BB82E13F; UM_distinctid=15c0ba0d0d9433-0045b6519-6c357361-13c680-15c0ba0d0da51d; CNZZDATA1260848629=2025313091-1494838408-http%253A%252F%252Fwww.yqt365.com%252F%7C1494838408\n" +
                "DNT:1\n" +
                "Host:www.yqt365.com\n" +
                "Origin:http://www.yqt365.com\n" +
                "Referer:http://www.yqt365.com/logout.action\n" +
                "User-Agent:Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Maxthon/5.0.3.4000 Chrome/47.0.2526.73 Safari/537.36\n" +
                "X-DevTools-Emulate-Network-Conditions-Client-Id:57BA7498-2DB5-4289-A5D3-48D00B4754D1\n" +
                "X-Requested-With:XMLHttpRequest";
        String form="username:sztest467yy\n" +
                "password:123456\n" +
                "vcode:";

        RequestBean post = new RequestBean(url, HttpMethodType.POST, headers, form);
        System.out.println(httpClientDownloader.sendRequest(post));
    }

    @Test
    public void sgwx(){
        String url="http://xhpfmapi.zhongguowangshi.com/v400/search/getSearchList";
        String headers="Content-Type: application/json; charset=utf-8\n" +
                "Content-Length: 33\n" +
                "Host: xhpfmapi.zhongguowangshi.com\n" +
                "Connection: Keep-Alive\n" +
                "Accept-Encoding: gzip\n" +
                "User-Agent: okhttp/3.4.2";
        String form="pn=1&keyword=123";
        String for1m="123123啊实打实的";
        System.out.println(for1m);
        RequestBean post = new RequestBean(url, HttpMethodType.POST, headers, form);
        System.out.println(httpClientDownloader.sendRequest(post));
    }


    @Test
    public void str(){
        String xx="阿萨德";
        System.out.println(xx);
    }

}

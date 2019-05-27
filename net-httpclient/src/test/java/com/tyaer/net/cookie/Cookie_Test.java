package com.tyaer.net.cookie;

import com.tyaer.net.httpclient.bean.HttpMethodType;
import com.tyaer.net.httpclient.bean.RequestBean;
import com.tyaer.net.httpclient.obtain.CookieObtainer;
import org.junit.Test;

import java.util.ArrayList;

/**
 * Created by Twin on 2017/1/10.
 */
public class Cookie_Test {

    public static void main(String[] args) {
        CookieObtainer cookieObtainer = new CookieObtainer();
        ArrayList<String> list = new ArrayList<>();
        list.add("http://weixin.sogou.com/");
        list.add("http://pb.sogou.com/pv.gif?uigs_t=1483950070110&uigs_productid=weixin&type=weixin_search_pc&pagetype=index&wuid=undefined&uigs_uuid=1483950069921410&login=0&uigs_refer=&");
        list.add("http://weixin.sogou.com/antispider/?from=%2fweixin%3Ftype%3d2%26query%3d%E5%91%A8%E6%98%9F%E9%A9%B0%26ie%3dutf8%26_sug_%3dn%26_sug_type_%3d1%26w%3d01015002%26oq%3d%26ri%3d4%26sourceid%3dsugg%26sut%3d0%26sst0%3d1484046908753%26lkt%3d0%2C0%2C0%26p%3d40040108");
        list.add("http://weixin.sogou.com/antispider/thank.php");
        System.out.println(cookieObtainer.obtainUrlCookies(list));
    }

    @Test
    public void test1(){
        CookieObtainer cookieObtainer = new CookieObtainer();
        ArrayList<String> list = new ArrayList<>();
        //list.add("http://weixin.sogou.com/weixin?type=2&ie=utf8&_sug_=n&_sug_type_=&query=深圳");
        list.add("https://www.kuaidi100.com/");
        System.out.println(cookieObtainer.obtainUrlCookies(list));
    }

    @Test
    public void test11(){
        CookieObtainer cookieObtainer = new CookieObtainer();

        String url="http://www.yqt365.com/ajaxCheckLogin.action";
        String url2="http://www.yqt365.com/login.action";
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
        String form="username:szystest2820\n" +
                "password:123456\n" +
                "vcode:";
        ArrayList<RequestBean> requestBeans = new ArrayList<>();
        requestBeans.add(new RequestBean(url, HttpMethodType.POST,headers,form));
        requestBeans.add(new RequestBean(url2, HttpMethodType.POST,headers,form));
        String obtain = cookieObtainer.obtainCookies(requestBeans);
        System.out.println(obtain);
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        String obtain2= cookieObtainer.obtainCookies(requestBeans);
        System.out.println(obtain2);
    }
}

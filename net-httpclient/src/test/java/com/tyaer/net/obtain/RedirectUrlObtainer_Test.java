package com.tyaer.net.obtain;

import com.tyaer.net.httpclient.bean.HttpMethodType;
import com.tyaer.net.httpclient.bean.RequestBean;
import com.tyaer.net.httpclient.obtain.RedirectUrlObtainer;

/**
 * Created by Twin on 2017/5/17.
 */
public class RedirectUrlObtainer_Test {

    public static void main(String[] args) {
        RedirectUrlObtainer redirectionUrlObtainer = new RedirectUrlObtainer();
        String headers = "Accept:text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8\n" +
                "Accept-Encoding:gzip, deflate\n" +
                "Accept-Language:zh-CN\n" +
                "Cache-Control:max-age=0\n" +
                "Cookie:YF-Page-G0=5c7144e56a57a456abed1d1511ad79e8; SUB=_2AkMuRcBMf8NxqwJRmP4Tz2_kZIVzzgHEieKYGTGXJRMxHRl-yT83ql4YtRCN6cnPyYfdjtVzRFfJADX9rLuxBA..; SUBP=0033WrSXqPxfM72-Ws9jqgMF55529P9D9W5LKdP2u2iK6XDUfmSzhSSa; _s_tentry=-; Apache=6961700716055.93.1494830965696; SINAGLOBAL=6961700716055.93.1494830965696; ULV=1494830965768:1:1:1:6961700716055.93.1494830965696:; YF-Ugrow-G0=56862bac2f6bf97368b95873bc687eef; YF-V5-G0=02157a7d11e4c84ad719358d1520e5d4; login_sid_t=f392c86ca839816f52a68193bd29ab6f; UOR=,,www.yqt365.com\n" +
                "DNT:1\n" +
                "Host:weibo.com\n" +
                "Proxy-Connection:keep-alive\n" +
                "Upgrade-Insecure-Requests:1\n" +
                "User-Agent:Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Maxthon/5.0.3.4000 Chrome/47.0.2526.73 Safari/537.36\n" +
                "X-DevTools-Emulate-Network-Conditions-Client-Id:AD031C63-E971-409D-AB2A-A6196AE713CD";

        RequestBean requestBean = new RequestBean("http://weibo.com/1931655382", HttpMethodType.GET, headers, null);
        String s = redirectionUrlObtainer.sendReqGetLocation(requestBean);
        System.out.println(s);
//        System.out.println(httpClientDownloader.sendRequest(requestBean));
    }
}

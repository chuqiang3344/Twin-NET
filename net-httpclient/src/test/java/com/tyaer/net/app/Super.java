package com.tyaer.net.app;

import com.tyaer.net.httpclient.bean.HttpMethodType;
import com.tyaer.net.httpclient.bean.RequestBean;
import com.tyaer.net.httpclient.bean.ResponseBean;
import com.tyaer.net.httpclient.downloader.HttpClientDownloader;
import org.junit.Test;

/**
 * Created by Twin on 2017/7/1.
 */
public class Super {
    static HttpClientDownloader httpClientDownloader=new HttpClientDownloader();


    @Test
    public void get(){

        String url="http://data.wxb.com/rank/article?category=-1&page=16&pageSize=100&type=1&order=index_scores-desc";
        String headers="Accept:application/json, text/plain, */*\n" +
                "Accept-Encoding:gzip, deflate\n" +
                "Accept-Language:zh-CN\n" +
                "Connection:keep-alive\n" +
                "Cookie:PHPSESSID=11vnqs0597lv7u8m2nu0egq0f4; pgv_pvi=448918528; pgv_si=s6540231680; visit-wxb-id=fd691c2aa691bd9f33997188c2c05b1d; IESESSION=alive; _qddamta_4009981236=3-0; tencentSig=7483732992; wxb_fp_id=613954537; Hm_lvt_5859c7e2fd49a1739a0b0f5a28532d91=1499042574; Hm_lpvt_5859c7e2fd49a1739a0b0f5a28532d91=1499042918; _qddaz=QD.mhwgrq.lkaowr.j4nf8te8; _qdda=3-1.41iu70; _qddab=3-q0tglo.j4nf91ok\n" +
                "DNT:1\n" +
                "Host:data.wxb.com\n" +
                "Referer:http://data.wxb.com/rankArticle\n" +
                "User-Agent:Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Maxthon/5.0.4.3000 Chrome/47.0.2526.73 Safari/537.36\n" +
                "X-DevTools-Emulate-Network-Conditions-Client-Id:4F7D5D8F-5263-4635-806F-2361A9154B19\n" +
                "X-Requested-With:XMLHttpRequest";
//        String form="username:sztest467yy\n" +
//                "password:123456\n" +
//                "vcode:";
        String form=null;

        RequestBean post = new RequestBean(url, HttpMethodType.GET, headers, form);
        ResponseBean responseBean = httpClientDownloader.sendRequest(post);
//        System.out.println(responseBean);
        System.out.println(responseBean.getRawText());

    }
}

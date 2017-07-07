package com.tyaer.net.app;

import com.tyaer.net.httpclient.bean.RequestBean;
import com.tyaer.net.httpclient.bean.ResponseBean;
import com.tyaer.net.httpclient.downloader.HttpClientDownloader;
import org.junit.Test;

/**
 * Created by Twin on 2017/6/23.
 */
public class Url_Test {

    private static HttpClientDownloader httpClientDownloader = new HttpClientDownloader();

    @Test
    public void cc(){
        String url="http://bbs.heyuan.cc/forum.php?mod=forumdisplay&fid=89";
        ResponseBean responseBean = httpClientDownloader.sendRequest(new RequestBean(url));
        System.out.println(responseBean);
    }
}

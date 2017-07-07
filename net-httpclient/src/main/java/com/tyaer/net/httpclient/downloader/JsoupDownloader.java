package com.tyaer.net.httpclient.downloader;

import org.jsoup.Connection;
import org.jsoup.Jsoup;

import java.io.IOException;

/**
 * Created by Twin on 2017/5/16.
 */
public class JsoupDownloader {

    public static void main(String[] args) {
//        String url="http://blog.sina.com.cn/s/blog_61d8d9640102vygw.html";
//        String url = "https://tieba.baidu.com/p/5131904151";
//        String url = "https://www.baidu.com/baidu?word=阿萨德&ie=utf-8&tn=myie2dg&ch=6";
        String url = "https://idol001.com/news/7004/detail/580d81fd7a11731f778b4725/";
        Connection connect = Jsoup.connect(url);
        connect.header("Accept-Encoding","gzip, deflate");
        try {
            Connection.Response execute = connect.execute();
            String body = execute.body();
            System.out.println(body);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

package com.tyaer.net.selenium.downloader;

import org.openqa.selenium.htmlunit.HtmlUnitDriver;

/**
 * Created by Twin on 2017/3/16.
 */
public class HtmlUnitDownloader extends DownloaderAbs {

    @Override
    protected void init() {
        driver = new HtmlUnitDriver(true);
        ((HtmlUnitDriver) driver).setJavascriptEnabled(true);
    }

    public static void main(String[] args) {

        HtmlUnitDownloader htmlUnitDownloader = new HtmlUnitDownloader();
//        String url = "http://society.qq.com/a/20170308/015629.htm#p=3";
//        String url = "https://passport.baidu.com/v2/?login&tpl=mn&u=http%3A%2F%2Fwww.baidu.com%2F";
        String url = "http://weibo.com/xuezhiqian?refer_flag=1001030101_&is_all=1";
//        String url = "http://blog.csdn.net/five3/article/details/19085303";
        long waitingTime = 3000;
        System.out.println(htmlUnitDownloader.download(url, waitingTime));
        htmlUnitDownloader.close();
    }
}

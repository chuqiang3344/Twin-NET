package com.tyaer.net.selenium.downloader;


import org.openqa.selenium.phantomjs.PhantomJSDriver;

/**
 * Created by Twin on 2016/11/14.
 */
public class PhantomjsDownloader extends DownloaderAbs {

    @Override
    protected void init() {
        String classPath = PhantomjsDownloader.class.getClassLoader().getResource("//").toString();
        String value = classPath + "phantomjs-2.1.1-windows/bin/phantomjs.exe";
        String replace = value.replace("file:/", "");
        LOGGER.info("phantomjs.exe path:" + replace);
        System.setProperty("phantomjs.binary.path", replace);
        driver = new PhantomJSDriver();
    }

    public static void main(String[] args) {
        PhantomjsDownloader phantomjsDownloader = new PhantomjsDownloader();
        String url = "http://society.qq.com/a/20170308/015629.htm#p=3";
        long waitingTime = 3000;
        System.out.println(phantomjsDownloader.download(url, waitingTime));
        phantomjsDownloader.close();
    }
}

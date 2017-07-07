package com.tyaer.net.phantomjs;


import org.apache.log4j.Logger;
import org.openqa.selenium.phantomjs.PhantomJSDriver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

/**
 * Created by Twin on 2016/11/14.
 */
public class PhantomjsDownloader {
    private static final Logger LOGGER = Logger.getLogger(PhantomjsDownloader.class);
    private PhantomJSDriver driver;

    public PhantomjsDownloader() {
        init();
    }

    public static void main(String[] args) throws IOException {
        PhantomjsDownloader phantomjsHelper = new PhantomjsDownloader();
//        String url = "http://www.toutiao.com/search/?keyword=教育局举报";
        String url = "http://www.site-digger.com/html/articles/20110516/proxieslist.html";
//        String url = "http://weibo.com/1239246050/EyinWrrtf?filter=hot&root_comment_id=4081738542586091&type=comment";
        long waitingTime = 10000;
//        System.out.println(phantomjsHelper.download(url, 0));
        System.out.println(phantomjsHelper.download(url, waitingTime));
        phantomjsHelper.close();
    }

    private void setCookie(){
//        driver.manage().
    }

    public String download(String url, long waitingTime) {
        if (driver != null) {
            driver.get(url);
            try {
//                driver.wait(waitingTime);
                Thread.sleep(waitingTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            String pageSource = driver.getPageSource();
            return pageSource;
        }
        return null;
    }

    private void close() {
        driver.close();
    }

    private void init() {
        String classPath = PhantomjsDownloader.class.getClassLoader().getResource("//").toString();
        String value = classPath + "phantomjs-2.1.1-windows/bin/phantomjs.exe";
        String replace = value.replace("file:/", "");
        LOGGER.info("phantomjs.exe path:" + replace);
        System.setProperty("phantomjs.binary.path", replace);
        driver = new PhantomJSDriver();
    }

    public String getAjaxCotnent(String url) throws IOException {

        System.setProperty("phantomjs.binary.path", "D:/phantomjs-2.1.1-windows/bin/phantomjs.exe");
        URL resource = this.getClass().getClassLoader().getResource("js/demo.js");
        System.out.println(resource);
        String path = resource.getPath();
        System.out.println(path);
        String s = "phantomjs.exe " + path + " " + url;
        System.out.println(s);
//        Process p = rt.exec(s);//这里我的codes.js是保存在c盘下面的phantomjs目录
//        Process p = runtime.exec("phantomjs.exe C:\\phantomjs\\hello.js");//这里我的codes.js是保存在c盘下面的phantomjs目录
//        Process p = rt.exec("phantomjs.exe c:/phantomjs/codes.js "+url);//这里我的codes.js是保存在c盘下面的phantomjs目录
//        Process p = runtime.exec("notepad");
        Runtime runtime = Runtime.getRuntime();
        Process p = runtime.exec("phantomjs");
//        Process p = runtime.exec("date");
        InputStream is = p.getInputStream();
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        StringBuffer sbf = new StringBuffer();
        String tmp = "";
        while ((tmp = br.readLine()) != null) {
            sbf.append(tmp);
        }
        //System.out.println(sbf.toString());
        return sbf.toString();
    }
}

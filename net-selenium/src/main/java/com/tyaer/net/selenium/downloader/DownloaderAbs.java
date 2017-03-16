package com.tyaer.net.selenium.downloader;

import org.apache.log4j.Logger;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;

import java.util.Map;

/**
 * Created by Twin on 2017/3/16.
 */
public abstract class DownloaderAbs {

    protected static final Logger LOGGER = Logger.getLogger(DownloaderAbs.class);
    protected WebDriver driver;

    public DownloaderAbs() {
        init();
    }

    protected abstract void init();

    public void setCookie(Map<String,String> map){
        for (Map.Entry<String, String> cookieEntry : map.entrySet()) {
            Cookie cookie = new Cookie.Builder(cookieEntry.getKey(), cookieEntry.getValue()).build();
            driver.manage().addCookie(cookie);
        }
    }

    public String download(String url, long waitingTime) {
        if (driver != null) {
            driver.get(url);
            try {
                Thread.sleep(waitingTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            String pageSource = driver.getPageSource();
            return pageSource;
        }
        return null;
    }

    protected void close() {
        driver.close();
    }
}

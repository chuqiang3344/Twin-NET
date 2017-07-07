package com.tyaer.net.selenium.downloader;

import com.tyaer.net.selenium.utils.CookieUtils;
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

    /**
     * 由于要设置cookie，所以在设置之前必须就建立连接。
     * 否则会报错。Exception in thread "main" org.openqa.selenium.InvalidCookieDomainException: You may only set cookies for the current domain
     * 故第一句get是不能少的。
     */

    private void setCookie(String cookie) {
        Map<String, String> stringStringMap = CookieUtils.analyzeCookies(cookie);
        setCookie(stringStringMap);
    }

    private void setCookie(Map<String, String> map) {
        try {
            for (Map.Entry<String, String> cookieEntry : map.entrySet()) {
//            Cookie cookie = new Cookie.Builder(cookieEntry.getKey(), cookieEntry.getValue()).build();
                Cookie cookie = new Cookie(cookieEntry.getKey(), cookieEntry.getValue());
                driver.manage().addCookie(cookie);
            }
        } catch (Exception e) {
            LOGGER.error(e);
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

    public String download(String url, String cookie, long waitingTime) {
        if (driver != null) {
            driver.get(url);
            try {
                Thread.sleep(waitingTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            setCookie(cookie);
            String pageSource = driver.getPageSource();
            return pageSource;
        }
        return null;
    }

    public void close() {
        LOGGER.info("Quit webDriver" + driver);
        driver.close();
        driver.quit();
        driver = null;
    }
}

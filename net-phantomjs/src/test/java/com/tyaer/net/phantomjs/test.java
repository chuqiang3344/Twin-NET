package com.tyaer.net.phantomjs;

import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriver;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Twin on 2016/11/14.
 */
public class test {
    static {
        System.setProperty("phantomjs.binary.path", "D:/phantomjs-2.1.1-windows/bin/phantomjs.exe");
    }

    public static void main(String[] args) {
        InputStream inputStream = test.class.getClassLoader().getResourceAsStream("js/hello.js");
        try {
            String script = IOUtils.toString(inputStream, "utf-8");
            System.out.println(script);
            PhantomJSDriver driver = new PhantomJSDriver();
//            driver.executeScript(script,1,2,3);
            driver.executePhantomJS(script,1,2,3);
            String pageSource = driver.getPageSource();
            System.out.println(pageSource);
            driver.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void s() throws IOException {
        String script = IOUtils.toString(test.class.getClassLoader().getResourceAsStream("js/hello.js"), "utf-8");
        WebDriver webDriver=new PhantomJSDriver();
        JavascriptExecutor  jse = (JavascriptExecutor)webDriver;
        System.out.println(jse.executeScript(script));
    }
}

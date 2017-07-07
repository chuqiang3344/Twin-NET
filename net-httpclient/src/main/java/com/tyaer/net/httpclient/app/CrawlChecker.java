package com.tyaer.net.httpclient.app;

import com.tyaer.net.httpclient.bean.RequestBean;
import com.tyaer.net.httpclient.bean.ResponseBean;
import com.tyaer.net.httpclient.downloader.HttpClientDownloader;
import org.apache.commons.io.FileUtils;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Created by Twin on 2017/5/27.
 */
public class CrawlChecker {
    private static HttpClientDownloader httpClientDownloader=new HttpClientDownloader();

    public static boolean check(String url,String key){
        RequestBean requestBean = new RequestBean(url);
        ResponseBean responseBean = httpClientDownloader.sendRequest(requestBean);
        if(responseBean.getStatusCode()!=200){
            System.out.println("请求失败！");
            return false;
        }
        String rawText = responseBean.getRawText();
        boolean contains = rawText.contains(key);
        if(!contains){
//            System.out.println(rawText);
        }
        return contains;
    }

    public static void main(String[] args) {
        String url="http://sz.58.com/zufang/";
        String key="光精装2房 家私家电全齐温馨住家";
        System.out.println(check(url, key));
    }

    @Test
    public void checks(){
        try {
            List<String> lines = FileUtils.readLines(new File("./file/check.txt"), "utf-8");
            for (int i = lines.size()-1; i >=0; i--) {
                String line=lines.get(i);
                System.out.println(line);
                String[] split = line.split(" ");
                System.out.println(check(split[0], split[1]));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

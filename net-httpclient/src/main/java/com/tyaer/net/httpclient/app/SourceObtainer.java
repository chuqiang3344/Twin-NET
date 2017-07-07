package com.tyaer.net.httpclient.app;

import com.tyaer.net.httpclient.bean.RequestBean;
import com.tyaer.net.httpclient.bean.ResponseBean;
import com.tyaer.net.httpclient.downloader.HttpClientDownloader;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;


/**
 * Created by Twin on 2017/7/2.
 */
public class SourceObtainer {
    private static final Logger logger = Logger.getLogger(SourceObtainer.class);

    private static HttpClientDownloader httpClientDownloader = new HttpClientDownloader();

    public static void main(String[] args) {
        String domain = "http://b2b.netsun.com";
        SourceObtainer sourceObtainer = new SourceObtainer();
        String source = sourceObtainer.getSource(domain);
        System.out.println(source);
    }


    public ConcurrentHashMap<String, String> getSources(List<String> domainList, int threadNum) {
//        LinkedBlockingQueue<String> queue = new LinkedBlockingQueue<>();
//        queue.addAll(domain);
        ConcurrentHashMap<String, String> concurrentHashMap = new ConcurrentHashMap<>();
        ThreadPoolExecutor executorService = (ThreadPoolExecutor) Executors.newFixedThreadPool(threadNum);
        for (int i = 0; i < domainList.size(); i++) {
            String domain = domainList.get(i);
            executorService.execute(() -> {
                String source = getSource(domain);
                if (source == null) source = "";
                concurrentHashMap.put(domain, source);
            });
        }
        while (true) {
            if (executorService.getActiveCount() == 0) {
                break;
            } else {
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        executorService.shutdownNow();
        return concurrentHashMap;
    }


    public static String getSource(String domain) {
        if (StringUtils.isBlank(domain)) {
            return null;
        }
        if (!domain.contains("http")) {
            domain = "http://" + domain;
        }
        ResponseBean responseBean = httpClientDownloader.sendRequest(new RequestBean(domain));
        String rawText = responseBean.getRawText();
        Document document = Jsoup.parse(rawText);
//        logger.info(responseBean.getCharset());
//        logger.info(rawText);
        String title = document.title();
        if (StringUtils.isNotBlank(title)) {
            String[] split = title.split("[ _|-—-・―：＊，【】�,/:()]");
            String webSiteName = null;
            for (String s : split) {
                if (webSiteName == null && StringUtils.isNotBlank(s)) {
                    webSiteName = s;
                }
                if (s.contains("网") && s.length() <= 10) {
                    webSiteName = s;
                    break;
                }
            }
            logger.info(domain + " " + Arrays.toString(split)+"=>"+webSiteName);
            return webSiteName;
        } else {
            return null;
        }
    }
}

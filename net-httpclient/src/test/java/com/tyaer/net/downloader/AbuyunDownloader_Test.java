package com.tyaer.net.downloader;

import com.tyaer.net.httpclient.bean.ResponseBean;
import com.tyaer.net.httpclient.counselor.AbuyunDownloader;
import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Twin on 2017/5/19.
 */
public class AbuyunDownloader_Test {
    private static final Logger logger=Logger.getLogger(AbuyunDownloader_Test.class);

    static AbuyunDownloader abuyunDownloader=new AbuyunDownloader();

    public static void main(String[] args) {
        String targetUrl = "http://www.ip181.com/";
//        String targetUrl = "https://test.abuyun.com/proxy.php";
//        String targetUrl = "http://weibo.com/u/6183832373?is_hot=1";
        String cookie = "";
//        String cookie = "SRF=1493169059;SRT=D.QqHBTrs-UqiuPdRtOeYoWr9NUPvt43bG4FW8dOPu5bEzMdbbNs9aiDEANbHi5mYNUCsuPDXqVdkfSGMNA-WFM%21uKSGBcSPAtOrkBNrkzMrX3UQ93Nd91ANtl%2AB.vAflW-P9Rc0lR-ykKDvnJqiQVbiRVPBtS%21r3J8sQVqbgVdWiMZ4siOzu4DbmKPVsTbfsUQm3PG9tJeJnAqYaPcP3NDA-;SUB=_2AkMvo3yUf8NxqwJRmP8SyGPgbY5yzQHEieKZ_41PJRMxHRl-yT83qkhdtRCgFFNNdpfeT8OKAKKHGH7mzwIjJA..;SUBP=0033WrSXqPxfM72-Ws9jqgMF55529P9D9W5jZ5mq7Tzx2s0C8_VewH46;";
        ExecutorService executorService = Executors.newFixedThreadPool(100);
        for (int i = 0; i < 20; i++) {
            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    for (int j = 0; j < 100; j++) {
                        ResponseBean responseBean = abuyunDownloader.sendGetRequest(targetUrl, cookie);
                        String html = responseBean.getRawText();
//                    System.out.println(document);
                        try {
                            Document document = Jsoup.parse(html);
                            String text = document.select("div.container.mt40 div.row>div:nth-child(2)").get(0).text();
                            System.out.println(text);
//                            logger.info(text);
                        } catch (Exception e) {
                            e.printStackTrace();
                            System.out.println(responseBean);
                        }
                    }
                }
            });
        }
        executorService.shutdown();
    }
}

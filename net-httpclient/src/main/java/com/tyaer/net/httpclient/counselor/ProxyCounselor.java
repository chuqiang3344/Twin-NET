package com.tyaer.net.httpclient.counselor;

import com.tyaer.net.httpclient.bean.ResponseBean;
import com.tyaer.net.httpclient.downloader.HttpClientDownloader;
import org.apache.http.HttpHost;
import org.apache.log4j.Logger;

import java.util.Calendar;
import java.util.Queue;


/**
 * 请求策略
 * Created by Twin on 2017/2/20.
 */
public class ProxyCounselor {
    private static final Logger logger = Logger.getLogger(ProxyCounselor.class);
    private static final long pause_time = 4 * 60 * 1000;
    private static volatile boolean useProxy = false;
    private static volatile long useProxy_time = 0;
    private static HttpClientDownloader httpClientDownloader;

    static {
        httpClientDownloader = new HttpClientDownloader();
    }

    /**
     * 代理队列
     */
    private Queue<HttpHost> proxy_queue;

    public ProxyCounselor(Queue<HttpHost> proxy_queue) {
        this.proxy_queue = proxy_queue;
    }


    /**
     * 快速模式 or 代理模式
     *
     * @param url
     * @param cookie
     * @return
     */
    public ResponseBean getArticleHtml(String url, String cookie) {
        ResponseBean responseBean;
        if (useProxy) {
            if (Calendar.getInstance().getTimeInMillis() - useProxy_time > pause_time) {
                logger.warn("###重新启用快速访问模式...");
                useProxy = false;
                useProxy_time = 0;
                return getArticleHtml(url, cookie);
            } else {
                HttpHost httpHost = proxy_queue.poll();
                responseBean = httpClientDownloader.sendRequest(url, httpHost, cookie);
            }
        } else {
            responseBean = httpClientDownloader.sendRequest(url, cookie);
            if (responseBean.getStatusCode() == 501) {
                logger.warn("###启用切换代理访问模式...");
                useProxy = true;
                useProxy_time = Calendar.getInstance().getTimeInMillis();
                return getArticleHtml(url, cookie);
            }
        }
        return responseBean;
    }

    /**
     * 使用代理IP和本机IP请求
     *
     * @param url
     * @param cookie
     * @param retryNum 尝试次数，最后一次不使用代理。
     * @return
     */
    public ResponseBean mixProxyRequestBefore(String url, String cookie, int retryNum) {
        ResponseBean responseBean = null;
        while (retryNum > 0) {
            if (retryNum > 1) {
                HttpHost httpHost = proxy_queue.poll();
                if (httpHost == null) {
                    logger.warn("queue无可用代理！");
                }
                responseBean = httpClientDownloader.sendRequest(url, httpHost, cookie);
            } else {
                responseBean = httpClientDownloader.sendRequest(url, cookie);
            }
            if (responseBean.getStatusCode() == 200) {
                break;
            } else {
                retryNum--;
            }
        }
        return responseBean;
    }

    /**
     * 使用代理IP和本机IP请求，第一次使用本机IP
     *
     * @param url
     * @param cookie
     * @param retryNum
     * @return
     */
    public ResponseBean mixProxyRequestAfter(String url, String cookie, int retryNum) {
        ResponseBean responseBean = httpClientDownloader.sendRequest(url, cookie);
        while (retryNum > 0) {
            if (responseBean.getStatusCode() == 200) {
                break;
            } else {
                HttpHost httpHost = proxy_queue.poll();
                if (httpHost == null) {
                    logger.warn("queue无可用代理！");
                }
                responseBean = httpClientDownloader.sendRequest(url, httpHost, cookie);
                retryNum--;
            }
        }
        return responseBean;
    }
}

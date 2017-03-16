package com.tyaer.net.httpclient.manager;

/**
 * Created by Twin on 2017/3/16.
 */

import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.log4j.Logger;

import java.util.concurrent.TimeUnit;

/**
 * 这个线程负责使用连接管理器清空失效连接和过长连接
 */
public class IdleConnectionMonitor extends Thread {

    private static final Logger logger = Logger.getLogger(IdleConnectionMonitor.class);

    private HttpClientConnectionManager connMgr;
    private volatile boolean shutdown = false;

    public IdleConnectionMonitor(HttpClientConnectionManager connMgr) {
        this.connMgr = connMgr;
    }

    @Override
    public void run() {
        while (!shutdown) {
            try {
                synchronized (this) {
                    wait(5000);
//						System.out.println("清空失效连接...");
                    // 关闭失效连接
                    connMgr.closeExpiredConnections();
                    // 关闭空闲超过30秒的连接
                    connMgr.closeIdleConnections(30, TimeUnit.SECONDS);
                }
            } catch (Exception e) {
                // terminatee
                logger.error("连接管理器清理线程异常：" + e);
            }
        }
    }

    public void shutdown() {
        shutdown = true;
        synchronized (this) {
            notifyAll();
        }
    }
}

package com.tyaer.net.httpclient.manager;

/**
 * Created by Twin on 2017/3/16.
 */

import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpRequest;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.protocol.HttpContext;

import javax.net.ssl.SSLException;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.UnknownHostException;

/**
 * 请求重试机制
 */
public class HttpRequestRetryHandlerImpl implements HttpRequestRetryHandler {

    /**
     * 下载失败后，马上进行重试的次数(通过使用httpclient自带的requestretryhandler机制)
     */
    private int retryTimes;

    public HttpRequestRetryHandlerImpl(int retryTimes) {
        this.retryTimes = retryTimes;
    }

    @Override
    public boolean retryRequest(IOException exception, int executionCount, HttpContext context) {
        if (executionCount >= retryTimes) {
            // 超过三次则不再重试请求
            return false;
        }
        if (exception instanceof InterruptedIOException) {
            // Timeout
            return false;
        }
        if (exception instanceof UnknownHostException) {
            // Unknown host
            return false;
        }
        if (exception instanceof ConnectTimeoutException) {
            // Connection refused
            return false;
        }
        if (exception instanceof SSLException) {
            // SSL handshake exception
            return false;
        }
        HttpClientContext clientContext = HttpClientContext.adapt(context);
        HttpRequest request = clientContext.getRequest();
        boolean idempotent = !(request instanceof HttpEntityEnclosingRequest);
        if (idempotent) {
//                System.out.println("重试请求...");
            // Retry if the request is considered idempotent 重试，如果该请求被认为是幂等
            return true;
        }
        return false;
    }
}

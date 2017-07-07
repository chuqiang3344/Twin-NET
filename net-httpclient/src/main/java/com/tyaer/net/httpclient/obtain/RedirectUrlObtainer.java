package com.tyaer.net.httpclient.obtain;

import com.tyaer.net.httpclient.bean.RequestBean;
import com.tyaer.net.httpclient.handle.RequestHandle;
import org.apache.http.Header;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

import java.io.IOException;

/**
 * Created by Twin on 2017/5/16.
 */
public class RedirectUrlObtainer {
    private static final Logger logger = Logger.getLogger(RedirectUrlObtainer.class);

    private static CloseableHttpClient httpClient;

    static {
        PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager();
        connectionManager.setMaxTotal(100);
        connectionManager.setDefaultMaxPerRoute(100);

        httpClient = HttpClients.custom()
                .setConnectionManager(connectionManager)
                .setDefaultRequestConfig(RequestHandle.getRequestConfig())
//                .setRedirectStrategy(new LaxRedirectStrategy())// 声明重定向策略对象
                .disableRedirectHandling() //TODO 关闭重定向
                .build();
    }

    public String sendReqGetLocation(RequestBean requestBean) {
        String location = null;
        HttpRequestBase httpRequestBase = RequestHandle.gethttpRequestBase(requestBean);
        if (httpRequestBase == null) {
            return null;
        }
        CloseableHttpResponse response = null;
        try {
            response = httpClient.execute(httpRequestBase);
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == 302) {
                Header locationHeader = response.getFirstHeader("Location"); //
                if (locationHeader != null) {
                    location = locationHeader.getValue();
                }
//                logger.debug(EntityUtils.toString(response.getEntity(), "utf-8"));
            } else {
                logger.warn("非302页面！" + statusCode);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (response != null) {
                    EntityUtils.consume(response.getEntity());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            httpRequestBase.releaseConnection();
            httpRequestBase.abort();
        }
        return location;
    }
}

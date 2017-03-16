package com.tyaer.net.httpclient.manager;

import org.apache.http.HeaderElement;
import org.apache.http.HeaderElementIterator;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.conn.ConnectionKeepAliveStrategy;
import org.apache.http.message.BasicHeaderElementIterator;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;

/**
 * Created by Twin on 2017/3/16.
 * 超时类的扩展
 * http长连接策略 可以根据须要定制所须要的长连接策略，可根据服务器指定的超时时间，也可根据主机名自己指定超时时间；
 */
public class ConnectionKeepAliveStrategyImpl implements ConnectionKeepAliveStrategy {

    // 如果服务器有超时则使用
    @Override
    public long getKeepAliveDuration(final HttpResponse response, final HttpContext context) {
//            Args.notNull(response, "HTTP response");
        // 遍历response的header
        HeaderElementIterator it = new BasicHeaderElementIterator(response.headerIterator(HTTP.CONN_KEEP_ALIVE));
        while (it.hasNext()) {
            final HeaderElement he = it.nextElement();
            final String param = he.getName();
            final String value = he.getValue();
            if (value != null && param.equalsIgnoreCase("timeout")) {//如果头部包含timeout信息，则使用
                try {
                    //超时时间设置为服务器指定的值
                    long timeout = Long.parseLong(value) * 1000;
//                        System.out.println("timeout:"+timeout);
                    return timeout;
                } catch (NumberFormatException ignore) {
                }
            }
        }
        //获取主机
        HttpHost target = (HttpHost) context.getAttribute(HttpClientContext.HTTP_TARGET_HOST);
        if ("webservice.webxml.com.cn".equalsIgnoreCase(target.getHostName())) {
            // 如果访问webservice.webxml.com.cn主机则设置长连接时间为5秒
            return 5 * 1000;
        } else {
            // 其他为30秒
            return 30 * 1000;
        }
    }
}

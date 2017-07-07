package com.tyaer.net.httpclient.utils;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.Header;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.client.LaxRedirectStrategy;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Twin on 2016/12/19.
 */
public class UrlUtils {
    public static void main(String[] args) {
//        String url="http://weixin.sogou.com/weixin?query=%E6%91%A9%E6%8B%9C&_sug_type_=&_sug_=n&type=2&page=2&ie=utf8";
//        String url="http://weixin.sogou.com/weixinwap?page=1&_rtype=json&ie=utf8&type=2&t=1483061392733&query=%E6%91%A9%E6%8B%9C&pg=webSearchList&_sug_=y&_sug_type_=&tsn=1&";
//        analyzeParameters(url);

        String url = "http://gocn.southcn.com/tpzx2010/qxpic/";
        String transformUrl = get302Url(url);
        System.out.println(transformUrl);

    }

    public static String get302Url(String loginUrl) {

//        PoolingHttpClientConnectionManager connectionManager=new PoolingHttpClientConnectionManager();
//        connectionManager.setMaxTotal(100);
//        connectionManager.setDefaultMaxPerRoute(20);

        CloseableHttpClient client = HttpClients.custom()
//                .setConnectionManager(connectionManager)
                .setRedirectStrategy(new LaxRedirectStrategy())// 声明重定向策略对象
                .disableRedirectHandling() //关闭重定向
                .build();

        String location = null;
        HttpGet httpGet = new HttpGet(loginUrl);
        httpGet.setHeader("Connection", "close"); //会有传输中断异常TruncatedChunkException
        CloseableHttpResponse httpResponse = null;
        try {
            // 执行post请求
            httpResponse = client.execute(httpGet, new HttpClientContext());
//            printResponse(httpResponse);
            Header locationHeader = httpResponse.getFirstHeader("Location");
            if (locationHeader != null) {
                location = locationHeader.getValue();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // 关闭流并释放资源
            if (httpResponse != null) {
                try {
                    httpResponse.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            httpGet.releaseConnection();
            try {
                client.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return location;
    }

    /**
     * 不好用，有问题
     *
     * @param urlString
     * @return
     */
    public static URI transformURI(String urlString) {
        if (null == urlString || urlString.isEmpty()) {
            return null;
        }
        //防止传入的urlString首尾有空格
        urlString = urlString.trim();
        //转化String url为URI,解决url中包含特殊字符的情况
        URI uri = null;
        try {
            URL url = new URL(urlString);
            //这里如果会强制将urlString转换为UTF-8格式，如百度贴吧的链接key为gb2312则不能使用此方法转换。
            uri = new URI(url.getProtocol(), url.getHost(), url.getPath(), url.getQuery(), null);
//            url=new URI()
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return uri;
    }

    /**
     * 分析url参数
     *
     * @param url
     * @return
     */
    public static Map<String, String> analyzeParameters(String url) {
        System.out.println(url);
        HashMap<String, String> hashMap = new HashMap<>();
        String[] split = url.split("\\?");
        if (split.length > 1) {
            String parameters_str = split[1];
            String[] parameters = parameters_str.split("&");
            for (String parameter : parameters) {
                String[] split1 = parameter.split("=");
                if (split1.length > 1) {
                    hashMap.put(split1[0], split1[1]);
                }
            }
        }
        System.out.println(hashMap);
        return hashMap;
    }



    /**
     * canonicalizeUrl
     * <br>
     * Borrowed from Jsoup.
     *
     * @param url url
     * @param refer refer
     * @return canonicalizeUrl
     */
    public static String canonicalizeUrl(String url, String refer) {
        URL base;
        try {
            try {
                base = new URL(refer);
            } catch (MalformedURLException e) {
                // the base is unsuitable, but the attribute may be abs on its own, so try that
                URL abs = new URL(refer);
                return abs.toExternalForm();
            }
            // workaround: java resolves '//path/file + ?foo' to '//path/?foo', not '//path/file?foo' as desired
            if (url.startsWith("?"))
                url = base.getPath() + url;
            URL abs = new URL(base, url);
            return encodeIllegalCharacterInUrl(abs.toExternalForm());
        } catch (MalformedURLException e) {
            return "";
        }
    }

    /**
     *
     * @param url url
     * @return new url
     */
    public static String encodeIllegalCharacterInUrl(String url) {
        //TODO more charator support
        return url.replace(" ", "%20");
    }

    public static String getHost(String url) {
        String host = url;
        int i = StringUtils.ordinalIndexOf(url, "/", 3);
        if (i > 0) {
            host = StringUtils.substring(url, 0, i);
        }
        return host;
    }

    private static Pattern patternForProtocal = Pattern.compile("[\\w]+://");

    public static String removeProtocol(String url) {
        return patternForProtocal.matcher(url).replaceAll("");
    }

    public static String getDomain(String url) {
        String domain = removeProtocol(url);
        int i = StringUtils.indexOf(domain, "/", 1);
        if (i > 0) {
            domain = StringUtils.substring(domain, 0, i);
        }
        return domain;
    }

    public static String removePort(String domain) {
        int portIndex = domain.indexOf(":");
        if (portIndex != -1) {
            return domain.substring(0, portIndex);
        }else {
            return domain;
        }
    }

    /**
     * allow blank space in quote
     */
    private static Pattern patternForHrefWithQuote = Pattern.compile("(<a[^<>]*href=)[\"']([^\"'<>]*)[\"']", Pattern.CASE_INSENSITIVE);

    /**
     * disallow blank space without quote
     */
    private static Pattern patternForHrefWithoutQuote = Pattern.compile("(<a[^<>]*href=)([^\"'<>\\s]+)", Pattern.CASE_INSENSITIVE);

    public static String fixAllRelativeHrefs(String html, String url) {
        html = replaceByPattern(html, url, patternForHrefWithQuote);
        html = replaceByPattern(html, url, patternForHrefWithoutQuote);
        return html;
    }

    public static String replaceByPattern(String html, String url, Pattern pattern) {
        StringBuilder stringBuilder = new StringBuilder();
        Matcher matcher = pattern.matcher(html);
        int lastEnd = 0;
        boolean modified = false;
        while (matcher.find()) {
            modified = true;
            stringBuilder.append(StringUtils.substring(html, lastEnd, matcher.start()));
            stringBuilder.append(matcher.group(1));
            stringBuilder.append("\"").append(canonicalizeUrl(matcher.group(2), url)).append("\"");
            lastEnd = matcher.end();
        }
        if (!modified) {
            return html;
        }
        stringBuilder.append(StringUtils.substring(html, lastEnd));
        return stringBuilder.toString();
    }

    private static final Pattern patternForCharset = Pattern.compile("charset\\s*=\\s*['\"]*([^\\s;'\"]*)");

    public static String getCharset(String contentType) {
        Matcher matcher = patternForCharset.matcher(contentType);
        if (matcher.find()) {
            String charset = matcher.group(1);
            if (Charset.isSupported(charset)) {
                return charset;
            }
        }
        return null;
    }


}

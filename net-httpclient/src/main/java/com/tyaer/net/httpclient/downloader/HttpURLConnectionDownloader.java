package com.tyaer.net.httpclient.downloader;

import com.tyaer.net.httpclient.manager.HttpsSupport;
import org.apache.commons.io.IOUtils;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

/**
 * Created by Twin on 2017/3/22.
 */
public class HttpURLConnectionDownloader {

    static {
        try {
            HttpsSupport.init();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void setDefaultHeaders(HttpURLConnection huc) {
        huc.setConnectTimeout(10000);
        huc.setReadTimeout(15000);
        huc.setRequestProperty("User-Agent", "Mozilla/5.0 (compatible; MSIE 9.0; Windows NT 6.1; WOW64; Trident/5.0)");
        huc.setRequestProperty("Accept-Language", "zh-CN,zh;q=0.8");
//        huc.setInstanceFollowRedirects(true);//是否连接遵循重定向
    }

    public static String read(String urlstr, String encode, String cookie, String post, boolean redir) throws Exception {
        URL url = new URL(urlstr);
        HttpURLConnection huc = (HttpURLConnection) url.openConnection();
        setDefaultHeaders(huc);
        huc.setInstanceFollowRedirects(redir);
        if (post != null && post.trim().length() > 0) {
            huc.setRequestMethod("POST");
            huc.setDoOutput(true);
            OutputStream inputstream = huc.getOutputStream();
            inputstream.write(post.getBytes("US-ASCII"));
            inputstream.close();
            huc.connect();
        }
        if (cookie != null) {
            huc.setRequestProperty("Cookie", cookie);
        }
        InputStream inputStream = huc.getInputStream();
        String html = IOUtils.toString(inputStream, encode);
//        System.out.println(html);
        inputStream.close();
        return html;
    }

    public static String read(String urlstr, String encode, String cookie) {
        String html = null;
        InputStream inputStream = null;
        try {
            URL url = new URL(urlstr);
            HttpURLConnection huc = (HttpURLConnection) url.openConnection();
            setDefaultHeaders(huc);
            huc.setInstanceFollowRedirects(true);//是否连接遵循重定向
            if (cookie != null) {
                huc.setRequestProperty("Cookie", cookie);
            }
            huc.connect();
            inputStream = huc.getInputStream();
            html = IOUtils.toString(inputStream, encode);
            huc.disconnect();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return html;
    }

    public static String readHttps(String urlstr, String encode, String cookie) {
        String html = null;
        InputStream inputStream = null;
        try {
            URL url = new URL(urlstr);
            HttpURLConnection huc = (HttpURLConnection) url.openConnection();
//            HttpsURLConnection
            huc.setRequestProperty("User-Agent", "Mozilla/5.0 (compatible; MSIE 9.0; Windows NT 6.1; WOW64; Trident/5.0)");
            huc.setRequestProperty("Accept-Language", "zh-CN,zh;q=0.8");
            huc.setInstanceFollowRedirects(true);//是否连接遵循重定向
            if (cookie != null) {
                huc.setRequestProperty("Cookie", cookie);
            }
            huc.connect();
            inputStream = huc.getInputStream();
            html = IOUtils.toString(inputStream, encode);
            huc.disconnect();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return html;
    }

    public static void main(String[] args) {
//        String url = "http://weibo.com/n/%E6%A2%A8%E8%A7%86%E9%A2%91?from=feed&amp;loc=at";
//        String url = "http://www.kuaidaili.com/free/inha/1";
//        String url = "https://tieba.baidu.com/p/5131904151";
//        String url = "https://www.baidu.com/baidu?word=阿萨德&ie=utf-8&tn=myie2dg&ch=6";
//        String url = "https://tieba.baidu.com/f?kw=%C0%EE%D2%E3&fr=ala0&tpl=5";
//        String url = "https://www.cqsq.com/list/28";
        String url = "http://sz.ganji.com/fang5/";
        try {
//            String cookie = "SINAGLOBAL=8349599072244.019.1487581065415; login_sid_t=db78595b4fd6651582f19e1230120a6e; YF-Ugrow-G0=1eba44dbebf62c27ae66e16d40e02964; YF-V5-G0=1da707b8186f677d9e4ad50934b777b3; _s_tentry=-; Apache=2893894922453.91.1489991631074; ULV=1489991631078:5:3:1:2893894922453.91.1489991631074:1488505463768; __utma=15428400.1213720716.1489993964.1489993964.1489993964.1; __utmc=15428400; __utmz=15428400.1489993964.1.1.utmcsr=(direct)|utmccn=(direct)|utmcmd=(none); YF-Page-G0=7b9ec0e98d1ec5668c6906382e96b5db; SUB=_2AkMvkwuFf8NhqwJRmP4Ry2jqao1yzQ7EieKZz_peJRMxHRl-yj83qnJctRAQ8otN258M1ShgfkobIkBg97WFTw..; SUBP=0033WrSXqPxfM72-Ws9jqgMF55529P9D9W5nlxOXXKbBuNRjplAyI_Sn";
//            String html = read(url, "UTF-8", cookie, null, true);
//            String html = read(url, "UTF-8", cookie);
            String encode = "UTF-8";
//            String encode = "GBK";
//            String html = read(url, encode, null);
            String html = readHttps(url, encode, null);
            System.out.println(html);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static class NullHostNameVerifier implements HostnameVerifier {
        /*
         * (non-Javadoc)
         *
         * @see javax.net.ssl.HostnameVerifier#verify(java.lang.String,
         * javax.net.ssl.SSLSession)
         */
        @Override
        public boolean verify(String arg0, SSLSession arg1) {
            // TODO Auto-generated method stub
            return true;
        }
    }
}

package com.tyaer.net.app;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;


public class HttpClientHelper {

	public static void main(String[] args) {
		HttpClientHelper httpClientHelper = new HttpClientHelper();
		String url = "https://idol001.com/news/7004/detail/580d81fd7a11731f778b4725/";
		String s = httpClientHelper.HttpRequest(url);
		System.out.println(s);
	}

	public String HttpRequest(String url) {

		RequestConfig config = RequestConfig.custom().setSocketTimeout(30000)
				.setConnectionRequestTimeout(30000).build();
		HttpClientBuilder httpClientBuilder = HttpClientBuilder.create()
				.setDefaultRequestConfig(config)
				.setRetryHandler(new DefaultHttpRequestRetryHandler());
		CloseableHttpClient closeableHttpClient = httpClientBuilder.build();
		HttpGet HttpGet = new HttpGet(url);
		HttpResponse resp = null;
		String html = "";
		try {
			resp = closeableHttpClient.execute(HttpGet);
		} catch (IOException e) {
			System.out.println("Request Error");
		}
		if (resp != null && resp.getStatusLine().getStatusCode() == 200) {
			HttpEntity entity = resp.getEntity();
			try {
				html = EntityUtils.toString(entity, Charset.forName("utf-8"));
			} catch (ParseException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			HttpGet.releaseConnection();
			try {
				closeableHttpClient.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return html;
	}

	public String HttpRequest(String url, Map<String, String> map) {

		RequestConfig config = RequestConfig.custom().setSocketTimeout(30000)
				.setConnectionRequestTimeout(30000).build();
		HttpClientBuilder httpClientBuilder = HttpClientBuilder.create()
				.setDefaultRequestConfig(config)
				.setRetryHandler(new DefaultHttpRequestRetryHandler());
		CloseableHttpClient closeableHttpClient = httpClientBuilder.build();
		HttpGet HttpGet = new HttpGet(url);
		HttpResponse resp = null;
		String html = "";
		if (map != null) {
			Set<String> set = map.keySet();
			for (String key : set) {
				HttpGet.setHeader(key, map.get(key));
			}
		}
		try {
			resp = closeableHttpClient.execute(HttpGet);
		} catch (IOException e) {
			System.out.println("Request Error");
		}
		if (resp != null) {
			HttpEntity entity = resp.getEntity();
			try {
				html = EntityUtils.toString(entity, Charset.forName("utf-8"));
			} catch (ParseException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				EntityUtils.consume(entity);
			} catch (IOException e) {
				e.printStackTrace();
			}
			HttpGet.releaseConnection();
			try {
				closeableHttpClient.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return html;
	}

	public String post(String url, Map<String, String> map, String req,
			HttpHost proxy) {
		RequestConfig config = RequestConfig.custom().setSocketTimeout(6000)
				.setConnectionRequestTimeout(6000).build();
		HttpClientBuilder httpClientBuilder = null;
		if (proxy != null) {
			httpClientBuilder = HttpClientBuilder.create()
					.setDefaultRequestConfig(config).setProxy(proxy)
					.setRetryHandler(new DefaultHttpRequestRetryHandler());
		} else {
			httpClientBuilder = HttpClientBuilder.create()
					.setDefaultRequestConfig(config)
					.setRetryHandler(new DefaultHttpRequestRetryHandler());
		}
		CloseableHttpClient closeableHttpClient = httpClientBuilder.build();
		HttpPost post = new HttpPost(url);
		if (map != null) {
			Set<String> set = map.keySet();
			for (String key : set) {
				post.setHeader(key, map.get(key));
			}
		}
		HttpResponse resp = null;
		String html = "";
		try {
			StringEntity reqEntity = new StringEntity(req);
			post.setEntity(reqEntity);
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		try {
			resp = closeableHttpClient.execute(post);
		} catch (IOException e) {
			html = "Read timed out";
		}
		if (resp != null) {
			HttpEntity entity = resp.getEntity();
			try {
				html = EntityUtils.toString(entity, Charset.forName("utf-8"));
			} catch (ParseException e) {
				html = "Read timed out";
			} catch (IOException e) {
				html = "Read timed out";
			}
			try {
				EntityUtils.consume(entity);
			} catch (IOException e) {
				e.printStackTrace();
			}
			post.releaseConnection();
			try {
				closeableHttpClient.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return html;
	}
}

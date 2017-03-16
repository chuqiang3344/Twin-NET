package com.tyaer.net.selenium.bean;

public class Page {

    private Request request;

    private String rawText;

    private String url;

    private int statusCode;

    private boolean needCycleRetry;

    @Override
    public String toString() {
        return "Page{" +
                "request=" + request +
                ", rawText='" + rawText + '\'' +
                ", url='" + url + '\'' +
                ", statusCode=" + statusCode +
                ", needCycleRetry=" + needCycleRetry +
                '}';
    }

    public Request getRequest() {
        return request;
    }

    public void setRequest(Request request) {
        this.request = request;
    }

    public String getRawText() {
        return rawText;
    }

    public void setRawText(String rawText) {
        this.rawText = rawText;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public boolean isNeedCycleRetry() {
        return needCycleRetry;
    }

    public void setNeedCycleRetry(boolean needCycleRetry) {
        this.needCycleRetry = needCycleRetry;
    }
}

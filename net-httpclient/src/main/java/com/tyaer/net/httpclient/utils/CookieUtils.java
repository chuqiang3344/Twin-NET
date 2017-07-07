package com.tyaer.net.httpclient.utils;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.CookieStore;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.cookie.BasicClientCookie;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Twin on 2017/3/20.
 */
public class CookieUtils {

    /**
     * 分析cookies参数
     *
     * @param cookies
     * @return
     */
    public static Map<String, String> analyzeCookies(String cookies) {
        HashMap<String, String> hashMap = new HashMap<>();
        String[] split = cookies.split(";");
        for (String cookie : split) {
            System.out.println(cookie);
            String[] kv = cookie.split("=");
            if (kv.length > 1) {
                hashMap.put(kv[0], kv[1]);
            }
        }
        return hashMap;
    }

    public static CookieStore generateCookieStore(String cookieStr) {
        CookieStore cookieStore = new BasicCookieStore();
        if(StringUtils.isNotBlank(cookieStr)){
            Map<String, String> map = analyzeCookies(cookieStr);
            for (String key : map.keySet()) {
                BasicClientCookie cookie = new BasicClientCookie(key, map.get(key));
//                cookie.setDomain(site.getDomain());
                cookieStore.addCookie(cookie);
            }
        }
        return cookieStore;
    }

    public static String getCookieStr(List<Cookie> cookieList) {
        StringBuffer buffer = new StringBuffer();
        for (Cookie cookie : cookieList) {
            buffer.append(cookie.getName()).append("=").append(cookie.getValue()).append(";");
        }
        String cookie_str = buffer.toString();
        return cookie_str;
    }

    public static void main(String[] args) {
        String cookie="ALC=ac%3D2%26bt%3D1490194718%26cv%3D5.0%26et%3D1521730718%26scf%3D%26uid%3D6108180470%26vf%3D0%26vs%3D0%26vt%3D0%26es%3D83aa6fc09f889a3bb7bbb4651812137f;ALF=1521730718;LT=1490194719;SUB=_2A2511uFODeRxGeBP61oQ-C7IzDyIHXVWolWGrDV_PUJbm9BeLUPCkW8bSz5v-3WDFWaUERqBWdbh7CCn1w..;SUBP=0033WrSXqPxfM725Ws9jqgMF55529P9D9W5ykf6kg2mCLg_E_Ep99F5j5NHD95QceK5ReKn7ShM7Ws4Dqcj_i--ci-zfiKnpi--NiKnpi-8Fi--Xi-zRi-zci--Xi-z4iKyFi--ci-82iKyh;sso_info=v02m6alo5qztKWRk5ilkKOUpY6DhKWRk5yljoOEpZCUiKWRk5ClkKOgpZCjmKWRk5ClkKOkpY6EiKWRk5ilkJSQpY6EjKadlqWkj5OYsYyDoLGOg4C0jbOAwA==;tgc=TGT-NjEwODE4MDQ3MA==-1490194718-gz-8B62536101FC48937B6D860FE9C00A7B-1;";
        analyzeCookies(cookie);
    }
}

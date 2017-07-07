package com.tyaer.net.selenium.utils;

import java.util.HashMap;
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
//        System.out.println(cookies);
        String[] split = cookies.split(";");
//        System.out.println();
        for (String cookie : split) {
//            System.out.println(cookie);
            String[] kv = cookie.split("=");
            if (kv.length > 1) {
                hashMap.put(kv[0].trim(), kv[1].trim());
            }
        }
        return hashMap;
    }
}

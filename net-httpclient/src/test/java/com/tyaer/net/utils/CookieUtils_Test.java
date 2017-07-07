package com.tyaer.net.utils;

import org.junit.Test;

import java.util.Map;

import static com.tyaer.net.httpclient.utils.CookieUtils.analyzeCookies;

/**
 * Created by Twin on 2017/3/20.
 */
public class CookieUtils_Test {
    @Test
    public void analyzeCookies_Test() {
        String cookies = "ABTEST=5|1482203525|v1; IPLOC=CN4403; SUID=A1C110B7721A910A000000005858A185; SUID=7AFE0FB71E20910A000000005858A186; SUV=00576323B70FFEBF5858A186C04CB255; weixinIndexVisited=1; PHPSESSID=3q2kl6qcpas0qevj8m40mj2e76; SUIR=1482997507; SNUID=3807F64EF8FCBD3EBD864CFFF9A377FE; JSESSIONID=aaasujALLhsu4gdGrs7Kv; sct=2; seccodeErrorCount=1|Thu, 29 Dec 2016 11:11:30 GMT; seccodeRight=success; successCount=2|Thu, 29 Dec 2016 11:08:01 GMT; refresh=1";
        Map<String, String> stringStringMap = analyzeCookies(cookies);
        System.out.println(stringStringMap);
    }
}

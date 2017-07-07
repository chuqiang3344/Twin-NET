package com.tyaer.net.app;


import com.tyaer.net.httpclient.bean.HttpMethodType;
import com.tyaer.net.httpclient.bean.RequestBean;
import com.tyaer.net.httpclient.bean.ResponseBean;
import com.tyaer.net.httpclient.downloader.HttpClientDownloader;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Test;

/**
 * Created by Twin on 2017/3/1.
 */
public class HttpClientDownloader_Test {
    static HttpClientDownloader httpClientDownloader = new HttpClientDownloader();

    public static void main(String[] args) {
//        ResponseBean responseBean = HttpHelper.sendRequest("http://weibo.com/p/1005051931655382/follow?relate=fans&from=100505&wvr=6&mod=headfans&current=fans#place","SINAGLOBAL=1316671036183.834.1486689445310; wb_g_upvideo_6108180470=1; wb_publish_fist100_6108180470=1; wvr=6; YF-Page-G0=00acf392ca0910c1098d285f7eb74a11; SSOLoginState=1488328781; _s_tentry=login.sina.com.cn; UOR=,,login.sina.com.cn; Apache=5801257388666.272.1488328783792; YF-V5-G0=3737d4e74bd7e1b846a326489cdaf5ab; ULV=1488328783902:24:1:5:5801257388666.272.1488328783792:1488274552042; YF-Ugrow-G0=56862bac2f6bf97368b95873bc687eef; WBtopGlobal_register_version=ad0d6baf029c6e6e; SCF=ApZe9mqcNg15KUTXMMH6QQfcobOg9XbVVRNxjnB0KFclDlCWm_cjUr13d0VRDVambTWH-Ts-OSA43T9wgUcXKc0.; SUB=_2A251svXWDeRxGeBP61oQ-C7IzDyIHXVWxmAerDV8PUJbmtBeLUjdkW82aA_UGe07ZEVSMmX_1Kyu7b2rqg..; SUBP=0033WrSXqPxfM725Ws9jqgMF55529P9D9W5ykf6kg2mCLg_E_Ep99F5j5JpX5o2p5NHD95QceK5ReKn7ShM7Ws4Dqcj_i--ci-zfiKnpi--NiKnpi-8Fi--Xi-zRi-zci--Xi-z4iKyFi--ci-82iKyh; SUHB=0HTR7gYT2bY3Qw; ALF=1519810545");
//        ResponseBean responseBean = HttpHelper.sendRequest("http://m.weibo.cn/u/2640113513","_T_WM=253a47b6813cf51100a406158744e690; M_WEIBOCN_PARAMS=featurecode%3D20000180%26oid%3D4077215773654155%26luicode%3D10000011%26lfid%3D1005052640113513%26fid%3D1076032640113513%26uicode%3D10000011");

//        ResponseBean responseBean = httpClientDownloader.setCharset("utf-8").sendRequest("http://m.weibo.cn/container/getIndex?type=uid&value=2640113513&containerid=1076032640113513");
//        String cookie = "YF-V5-G0=69afb7c26160eb8b724e8855d7b705c6; _s_tentry=-; Apache=4397365853656.0835.1484723950063; SINAGLOBAL=4397365853656.0835.1484723950063; ULV=1484723950066:1:1:1:4397365853656.0835.1484723950063:; YF-Page-G0=046bedba5b296357210631460a5bf1d2; SUB=_2AkMvI80sf8NhqwJRmP0RxGPka4V1ww7EieKZfzz3JRMxHRl-yT83qkkptRBxtDWvOJvqp2I1Y85XsOvjpRF2kw..; SUBP=0033WrSXqPxfM72-Ws9jqgMF55529P9D9WhXP5zlV.UkuM3K5Slq3ADu; YF-Ugrow-G0=9642b0b34b4c0d569ed7a372f8823a8e; WBtopGlobal_register_version=c689c52160d0ea3b";
        String cookie = "SINAGLOBAL=8349599072244.019.1487581065415; login_sid_t=db78595b4fd6651582f19e1230120a6e; YF-Ugrow-G0=1eba44dbebf62c27ae66e16d40e02964; YF-V5-G0=1da707b8186f677d9e4ad50934b777b3; _s_tentry=-; Apache=2893894922453.91.1489991631074; ULV=1489991631078:5:3:1:2893894922453.91.1489991631074:1488505463768; __utma=15428400.1213720716.1489993964.1489993964.1489993964.1; __utmc=15428400; __utmz=15428400.1489993964.1.1.utmcsr=(direct)|utmccn=(direct)|utmcmd=(none); YF-Page-G0=7b9ec0e98d1ec5668c6906382e96b5db; SUB=_2AkMvkwuFf8NhqwJRmP4Ry2jqao1yzQ7EieKZz_peJRMxHRl-yj83qnJctRAQ8otN258M1ShgfkobIkBg97WFTw..; SUBP=0033WrSXqPxfM72-Ws9jqgMF55529P9D9W5nlxOXXKbBuNRjplAyI_Sn";
//        String url = "http://weibo.com/3211467125/F22PDlaMG";
        String url = "http://weibo.com/6011323221/EEbslfJzy";
//        String url = "http://weibo.com/p/aj/v6/mblog/mbloglist?ajwvr=6&domain=100505&is_search=0&visible=0&is_all=1&is_tag=0&profile_ftype=1&page=1&pagebar=1&pl_name=Pl_Official_MyProfileFeed__23&id=1005051725985180&script_uri=/p/1005051725985180/home&feed_type=0&pre_page=0&domain_op=100505";
//        String url = "http://d.weibo.com/1087030002_2975_1003_0#";
//        String url = "http://weibo.com/n/%E6%A2%A8%E8%A7%86%E9%A2%91";
//        String url = "http://weibo.com/n/%E6%A2%A8%E8%A7%86%E9%A2%91?from=feed&amp;loc=at";
//        String url = "http://weibo.com/p/1005051931655382?profile_ftype=1&is_all=1#_0";
//        url= URLDecoder.decode(url);
//        String url = "http://weibo.com/pearvideo?nick=梨视频";
//        String url = "http://weibo.com/n/梨视频?from=feed&amp;loc=at";
//        String url = "http://weibo.com/n/%E6%A2%A8%E8%A7%86%E9%A2%91?from=feed&loc=at";
//        String url = "http://weibo.com/n/%E6%A2%A8%E8%A7%86%E9%A2%91?from=feed&amp;loc=at";
//        String url = "http://weibo.com/n/梨视频?from=feed&loc=at";
//        String url = "http://weibo.com/pearvideo?from=feed&loc=at&nick=梨视频";
//        String url = "http://weibo.com/n/%E6%A2%A8%E8%A7%86%E9%A2%91";
//        String url = "http://weibo.com/n/梨视频";
        System.out.println(url);

        ResponseBean responseBean = httpClientDownloader.sendRequest(url, cookie);
        responseBean.out();

    }

    @Test
    public void get() {
//        String url = "http://sz.58.com/ershoufang/?PGTID=0d200001-0000-4bb4-e537-d0206a80ef6f&ClickID=1";
//        String url = "https://tieba.baidu.com/f?kw=贴吧";
//        String url = "http://www.yymhw.com/zjyy/shsy/";
//        String url = "http://bbs.0762.net/forum-2-1.html";
//        String url = "http://bbs.ts.cn/forum-379-1.html";
//        String url = "http://bbs.iqilu.com/forum-98-1.html";
//        String url = "https://idol001.com/news/7004/detail/580d81fd7a11731f778b4725/";
        String url = "http://news.cnpc.com.cn/system/2016/03/29/001586300.shtml";
//        String url = "https://kyfw.12306.cn/otn/";
        RequestBean requestBean = new RequestBean(url);
//        String cookie = null;
//        String cookie = "__jsluid=3f7497068df968a660d4a77e9e75ad0c; __jsl_clearance=1498870859.172|0|jJsFEtvW414Yq4VtofLvNoO206s%3D; qVfi_2132_saltkey=ovf6bm2Y; qVfi_2132_lastvisit=1498867261; qVfi_2132_atarget=1; qVfi_2132_st_t=0%7C1498871806%7C272fada1cc015c25f20734a5dc94f3f8; qVfi_2132_forum_lastvisit=D_324_1498870865D_403_1498870870D_379_1498870988D_357_1498871806; qVfi_2132_visitedfid=357D379D403D324; qVfi_2132_sid=Y1lcy1; qVfi_2132_sendmail=1; qVfi_2132_lastact=1498871807%09plugin.php%09";
        String cookie = "__jsluid=3f7497068df968a660d4a77e9e75ad0c; qVfi_2132_saltkey=ovf6bm2Y; qVfi_2132_lastvisit=1498867261; qVfi_2132_atarget=1; qVfi_2132_visitedfid=357D379D403D324; qVfi_2132_st_t=0%7C1498874383%7C299a601dc6083279adec605bb111679c; qVfi_2132_forum_lastvisit=D_324_1498870865D_403_1498870870D_379_1498870988D_357_1498874383; qVfi_2132_sid=Y1lcy1; qVfi_2132_lastact=1498874444%09forum.php%09ajax; __jsl_clearance=1498874685.762|0|oLs%2BV6LZkzOJQpUH%2FbDqW7%2BnpIo%3D";

//        requestBean.updateHeaders("Cookie",cookie);
//        requestBean.updateHeaders("User-Agent","Mozilla/5.0 (Windows NT 6.1; WOW64; rv:53.0) Gecko/20100101 Firefox/53.0");

//        String cookie="f=n; id58=c5/ns1klOSyU7VYKAyk1Ag==; als=0; ipcity=sz%7C%u6DF1%u5733%7C0; commonTopbar_myfeet_tooltip=end; 58home=sz; f=n; city=sz; 58tj_uuid=1405ab68-a8c8-40bb-b245-caeac8de9dd8; new_session=0; new_uv=2; utm_source=; spm=; init_refer=; commontopbar_city=4%7C%u6DF1%u5733%7Csz";
//        ResponseBean responseBean = httpClientDownloader.sendRequest(url, cookie);
        ResponseBean responseBean = httpClientDownloader.sendRequest(requestBean);
        responseBean.out();
    }

    @Test
    public void ip181() {
        String url = "http://1212.ip138.com/ic.asp";
        ResponseBean responseBean = httpClientDownloader.sendRequest(url);
        System.out.println(responseBean);
//        System.out.println(rawText);
//        System.out.println(rawText);
//        System.out.println(document.select("div"));
//        System.out.println(document.select("div.row"));
    }

    @Test
    public void https() {
//        String url = "http://1212.ip138.com/ic.asp";
        String url = "https://idol001.com/news/7004/detail/580d81fd7a11731f778b4725/";
        ResponseBean responseBean = httpClientDownloader.sendRequest(url);
        System.out.println(responseBean);
//        System.out.println(rawText);
//        System.out.println(rawText);
//        System.out.println(document.select("div"));
//        System.out.println(document.select("div.row"));
    }

    @Test
    public void superx(){
        String url="http://sz.ganji.com/fang5/";
        String heards="Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8\n" +
                "Accept-Encoding: gzip, deflate\n" +
                "Accept-Language: zh-CN\n" +
                "Cache-Control: max-age=0\n" +
                "Connection: keep-alive\n" +
                "Cookie: statistics_clientid=me; GANJISESSID=b82dd0f6c96dee8003e3112f65dbabdd; ganji_uuid=7022116495923864173297; ganji_xuuid=1918ebca-1ac4-48d5-db56-e2f702055111.1495870011250; gj_footprint=%5B%5B%22%5Cu4e8c%5Cu624b%5Cu623f%5Cu51fa%5Cu552e%22%2C%22http%3A%5C%2F%5C%2Fsz.ganji.com%5C%2Ffang5%5C%2F%22%5D%2C%5B%22%5Cu79df%5Cu623f%22%2C%22http%3A%5C%2F%5C%2Fsz.ganji.com%5C%2Ffang1%5C%2F%22%5D%5D; STA_DS=1; ganji_login_act=1495870741812; lg=1; __utma=32156897.1858568152.1495870011.1495870011.1495870011.1; __utmb=32156897.6.10.1495870011; __utmc=32156897; __utmz=32156897.1495870011.1.1.utmcsr=(direct)|utmccn=(direct)|utmcmd=(none); _gl_tracker=%7B%22ca_source%22%3A%22-%22%2C%22ca_name%22%3A%22-%22%2C%22ca_kw%22%3A%22-%22%2C%22ca_id%22%3A%22-%22%2C%22ca_s%22%3A%22self%22%2C%22ca_n%22%3A%22-%22%2C%22ca_i%22%3A%22-%22%2C%22sid%22%3A55611325315%7D\n" +
                "DNT: 1\n" +
                "Host: sz.ganji.com\n" +
                "Upgrade-Insecure-Requests: 1\n" +
                "User-Agent: Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Maxthon/5.0.3.4000 Chrome/47.0.2526.73 Safari/537.36\n" +
                "X-DevTools-Emulate-Network-Conditions-Client-Id: BC89FF44-7286-4F50-81E7-C86BBC7E801D\n";
        String forms="";
        RequestBean requestBean = new RequestBean(url, HttpMethodType.GET, heards, forms);
        ResponseBean responseBean = httpClientDownloader.sendRequest(requestBean);
        System.out.println(responseBean);
    }

    @Test
    public void simple() {
        String url = "http://d.weibo.com/1087030002_2975_5008_0";
        String cookie = "SINAGLOBAL=8349599072244.019.1487581065415; login_sid_t=db78595b4fd6651582f19e1230120a6e; YF-Ugrow-G0=1eba44dbebf62c27ae66e16d40e02964; YF-V5-G0=1da707b8186f677d9e4ad50934b777b3; _s_tentry=-; Apache=2893894922453.91.1489991631074; ULV=1489991631078:5:3:1:2893894922453.91.1489991631074:1488505463768; __utma=15428400.1213720716.1489993964.1489993964.1489993964.1; __utmc=15428400; __utmz=15428400.1489993964.1.1.utmcsr=(direct)|utmccn=(direct)|utmcmd=(none); YF-Page-G0=7b9ec0e98d1ec5668c6906382e96b5db; SUB=_2AkMvkwuFf8NhqwJRmP4Ry2jqao1yzQ7EieKZz_peJRMxHRl-yj83qnJctRAQ8otN258M1ShgfkobIkBg97WFTw..; SUBP=0033WrSXqPxfM72-Ws9jqgMF55529P9D9W5nlxOXXKbBuNRjplAyI_Sn";
        String rawText = httpClientDownloader.sendRequest(url, cookie).getRawText();
    }

    @Test
    public void simple2() {
//        String url="http://www.66ip.cn/getzh.php?getzh=2016110738991&getnum=5000&isp=0&anonymoustype=3&start=&ports=&export=&ipaddress=&area=1&proxytype=2&api=https";
//        String url="http://www.kuaidaili.com/free/inha/1";
//        String rawText = httpClientDownloader.sendRequest(url).getRawText();

        String url = "http://www.kuaidaili.com/free/inha/1";
        String cookie = "_ydclearance=7c659a5062c60a8a0e0aca1b-4405-4fdc-89d3-ee3c745e6b68-1490758192; channelid=0; sid=1490750217273441; _ga=GA1.2.864946765.1490750993; Hm_lvt_7ed65b1cc4b810e9fd37959c9bb51b31=1490750993; Hm_lpvt_7ed65b1cc4b810e9fd37959c9bb51b31=1490751047";
        String rawText = httpClientDownloader.sendRequest(url, cookie).getRawText();
        System.out.println(rawText);
    }

    @Test
    public void post() {
        String url = "http://www.yqt365.com/searchListMore.action";
//        String url="https://www.baidu.com/index.php?tn=maxthon2&ch=2";
        String headers = "Accept:*/*\n" +
                "Accept-Encoding:gzip, deflate\n" +
                "Accept-Language:zh-CN\n" +
                "Cache-Control:no-cache\n" +
                "Connection:keep-alive\n" +
                "Content-Length:274\n" +
                "Content-Type:application/x-www-form-urlencoded; charset=UTF-8\n" +
                "Cookie:UM_distinctid=15ba4010d5b0-0a2882b91-6c337361-13c680-15ba4010d5c4e9; yqt365=userSId_yqt365_sztesthxzh853_42259; CNZZDATA1260848629=386922560-1493105038-null%7C1494324526; JSESSIONID=041108CF6D1013492C36A80BD336E882\n" +
                "DNT:1\n" +
                "Host:www.yqt365.com\n" +
                "Origin:http://www.yqt365.com\n" +
                "Pragma:no-cache\n" +
                "Referer:http://www.yqt365.com/searchList.action\n" +
                "User-Agent:Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Maxthon/5.0.3.4000 Chrome/47.0.2526.73 Safari/537.36\n" +
                "X-DevTools-Emulate-Network-Conditions-Client-Id:153F9E11-1F46-414E-9A60-7B21CD08B456\n" +
                "X-Requested-With:XMLHttpRequest";

        String form = "checkedIds:\n" +
                "selectedId:214942347305137453152326\n" +
                "kw.id:\n" +
                "searchKeyword:深圳滑坡事故\n" +
                "searchPipeiType:2\n" +
                "pinglunShow:\n" +
                "zhaiyaoShow:1\n" +
                "duplicateShow:0\n" +
                "paixu:1\n" +
                "highLightKeywords:\n" +
                "endtime:2017-05-09 23:59:59\n" +
                "starttime:2017-05-07 00:00:00\n" +
                "clickFilterOrigina:5\n" +
                "filterOrigina:5\n" +
                "clickPaixu:1\n" +
                "clickOtherAttribute:0\n" +
                "clickDuplicateShow:-1\n" +
                "otherAttribute:0\n" +
                "fontSizeType:1\n" +
                "clickFontSizeType:\n" +
                "newlstSelect:2\n" +
                "clickNewlstSelect:-1\n" +
                "comblineflg:2\n" +
                "clickComblineflg:-1\n" +
                "clickZhaiyaoShow:\n" +
                "dataParam:0\n" +
                "defaultLen:3days\n" +
                "secondSearchWord:\n" +
                "toolbarSwitch:0\n" +
                "page:1\n" +
                "total:0\n" +
                "searchType:2\n" +
                "isClickOnlySearchRootWb:0";

        RequestBean post = new RequestBean(url, HttpMethodType.POST, headers, form);
        System.out.println(httpClientDownloader.sendRequest(post));
    }

    @Test
    public void post1() {
        String url = "http://www.yqt365.com/getStatList.action";

        String headers = "" +
//                "Accept:*/*\n" +
//                "Accept-Encoding:gzip, deflate\n" +
//                "Accept-Language:zh-CN\n" +
//                "Cache-Control:no-cache\n" +
//                "Connection:keep-alive\n" +
//                "Content-Length:274\n" +
                "Content-Type:application/x-www-form-urlencoded; charset=UTF-8\n" +
//                "Cookie:UM_distinctid=152c0a90e46f0-003ee6985-6c357361-13c680-15c0a90e4701c1; yqt365=userSId_yqt365_sztesthxzh853_73409; CNZZDATA1260848629=1842548146-1494822859-http%253A%252F%252Fwww.yqt365.com%252F%7C1494833687;\n" +
                "Cookie:yqt365=userSId_yqt365_szystest2820_68920;\n" +
//                "DNT:1\n" +
//                "Host:www.yqt365.com\n" +
//                "Origin:http://www.yqt365.com\n" +
//                "Pragma:no-cache\n" +
//                "Referer:http://www.yqt365.com/searchList.action\n" +
//                "User-Agent:Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Maxthon/5.0.3.4000 Chrome/47.0.2526.73 Safari/537.36\n" +
//                "X-DevTools-Emulate-Network-Conditions-Client-Id:153F9E11-1F46-414E-9A60-7B21CD08B456\n" +
//                "X-Requested-With:XMLHttpRequest"+
                "";

//        String form="admin.userId:25384\n" +
//                "timeDomain:3\n" +
//                "searchKeyword:深圳滑坡事故\n" +
//                "paixu:1\n" +
//                "secondSearchWord:\n" +
//                "starttime:2017-05-07+00%3A00%3A00\n" +
//                "endtime:2017-05-09+23%3A59%3A59\n" +
//                "newlstSelect:2\n" +
//                "filterOrigina:5\n" +
//                "duplicateShow:0\n" +
//                "otherAttribute:0\n" +
//                "searchPipeiType:2";

        String form = "admin.userId:25384\n" +
                "timeDomain:3\n" +
                "searchKeyword:%E6%B7%B1%E5%9C%B3%E6%BB%91%E5%9D%A1%E4%BA%8B%E6%95%85\n" +
                "paixu:1\n" +
                "secondSearchWord:\n" +
                "starttime:2017-05-07 00:00:00\n" +
                "endtime:2017-05-09 23:59:59\n" +
                "newlstSelect:2\n" +
                "filterOrigina:5\n" +
                "duplicateShow:0\n" +
                "otherAttribute:0\n" +
                "searchPipeiType:2";

        String form1 = "admin.userId=25384&timeDomain=3&searchKeyword=%E6%B7%B1%E5%9C%B3%E6%BB%91%E5%9D%A1%E4%BA%8B%E6%95%85&paixu=1&secondSearchWord=&starttime=2017-05-07+00%3A00%3A00&endtime=2017-05-09+23%3A59%3A59&newlstSelect=2&filterOrigina=5&duplicateShow=0&otherAttribute=0&searchPipeiType=2";
//        System.out.println(HttpHandle.str2map(form1));

//        System.out.println(httpClientDownloader.sendPostRequest(url, headers, form));

        RequestBean post = new RequestBean(url, HttpMethodType.POST, headers, form1);
        System.out.println(httpClientDownloader.sendRequest(post));

//        System.out.println(httpClientDownloader.sendPostRequest(url, headers, form1));
//        String replace = form.replace("\n", "&").replace(":","=");
////        System.out.println(replace);
////        System.out.println(form1);
//        System.out.println(httpClientDownloader.sendPostRequest(url, HttpHandle.str2map(headers), replace));
//        System.out.println(httpClientDownloader.sendPostRequest(url, HttpHandle.str2map(headers), form1));
    }

    @Test
    public void gets() {
//        String url="http://mp.weixin.qq.com/s?timestamp=1483424934&src=3&ver=1&signature=8FMywOyklIRV5dig44az5bBwvhJ5cUKL520w28iRz9QkHWlc7Shqsl9v99gkxt5uhRU18pnLGHbO2AGq9yC2dr4HN9CC5Jf8d7qsJTHzENYmiRGB1TyIWBi*jxWXE*fKxu8xx2qSDhDM8ynlSOTrVvvVV6G-tOdyTcEwNtfB9ic=&uin=Nzc0NDc1ODQw";
        String url = "http://weixin.sogou.com/weixin?usip=&query=%E6%91%A9%E6%8B%9C&ft=&tsn=1&et=&interation=&type=2&wxid=&page=1&ie=utf8";
//        String url="https://www.baidu.com/index.php?tn=maxthon2&ch=2";
        String headers = "Accept:text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8\n" +
                "Accept-Encoding:gzip, deflate\n" +
                "Accept-Language:zh-CN\n" +
                "Cache-Control:no-cache\n" +
                "Cookie:ABTEST=0|1492413158|v1; IPLOC=CN4403; SUID=1CFC0FB7771A910A0000000058F46AE6; SUID=1CFC0FB73020910A0000000058F46AE6; weixinIndexVisited=1; SUV=00D35AA3B70FFC1C58F46AE7F49FF385; SUIR=7CAA5FE651551F941250EE17519BDAC1; SNUID=29F60CB304064B3D23F719D804409A54; JSESSIONID=aaa-YxlbdS9rQe5UjR2Vv; PHPSESSID=i0u9i9avgn3tpc582u6dl82sm7; sct=25\n" +
                "DNT:1\n" +
                "Host:weixin.sogou.com\n" +
                "Pragma:no-cache\n" +
                "Proxy-Connection:keep-alive\n" +
//                "Referer:http://weixin.sogou.com/weixin?usip=&query=%E6%91%A9%E6%8B%9C&ft=&tsn=1&et=&interation=&type=2&wxid=&page=1&ie=utf8\n" +
//                "Referer:http://weixin.sogou.com/weixin?type=2&query=%E6%91%A9%E6%8B%9C&ie=utf8&s_from=input&_sug_=n&_sug_type_=1&w=01015002&oq=&ri=2&sourceid=sugg&sut=0&sst0=1494549586064&lkt=0%2C0%2C0&p=40040108\n" +
                "Referer:http://weixin.sogou.com/weixin\n" +
//                "Referer:http://weixin.sogou.com/weixin?usip=&query=%E6%91%A9%E6%8B%9C&ft=&tsn=1&et=&interation=&type=2&wxid=&page=1&ie=utf8\n" +
//                "Referer:http://weixin.sogou.com\n" +
                "Upgrade-Insecure-Requests:1\n" +
                "User-Agent:Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Maxthon/5.0.3.4000 Chrome/47.0.2526.73 Safari/537.36\n" +
                "X-DevTools-Emulate-Network-Conditions-Client-Id:7AB19B6A-504E-4978-9DFB-687197AD1D3C" +
                "";

//        System.out.println(httpClientDownloader.sendGetRequest(url, headers));
//        System.out.println(httpClientDownloader.sendRequest(url));
        RequestBean get = new RequestBean(url, HttpMethodType.GET, headers, null);
        System.out.println(httpClientDownloader.sendRequest(get));
    }

    @Test
    public void gets1() {
//        String url="http://mp.weixin.qq.com/s?timestamp=1483424934&src=3&ver=1&signature=8FMywOyklIRV5dig44az5bBwvhJ5cUKL520w28iRz9QkHWlc7Shqsl9v99gkxt5uhRU18pnLGHbO2AGq9yC2dr4HN9CC5Jf8d7qsJTHzENYmiRGB1TyIWBi*jxWXE*fKxu8xx2qSDhDM8ynlSOTrVvvVV6G-tOdyTcEwNtfB9ic=&uin=Nzc0NDc1ODQw";
//        String url="http://myfavlink.sinaapp.com/js/cnc.js";
        String url = "https://www.baidu.com/link?url=-iSPSukEy2EpTK_hWjPTxhXBrRTtjvqmp7xjNq7c98uJIcUsYhKF2gIkyXejzlhudO3t3crtbvtC3lc01-tI7V4k3_KzlChQXksutsz6nmSXT-iHTFOCWi4BKxhoDOmf&wd=&eqid=d5ff3163000055980000000659099907";
//        String url="https://www.baidu.com/index.php?tn=maxthon2&ch=2";
        String headers = "Accept:text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8\n" +
                "Accept-Encoding:gzip, deflate\n" +
                "Accept-Language:zh-CN\n" +
                "Cache-Control:no-cache\n" +
                "Connection:keep-alive\n" +
                "Cookie:BAIDUID=6CF5D61A8640C1382FE60E6763ABE329:FG=1; BIDUPSID=6CF5D61A8640C1382FE60E6763ABE329; PSTM=1492222408; ispeed_lsm=2; BDRCVFR[gztQtCol733]=aeXf-1x8UdYcs; BD_HOME=0; BDRCVFR[C0p6oIjvx-c]=pmJ1DbybSPmfAd9XZwCUv3z; BD_CK_SAM=1; PSINO=7; H_PS_PSSID=1456_21104_18560_22073; BD_UPN=17314353; H_PS_645EC=d6db%2FBpmmxRbTn1TfLNnnY9%2FDruF%2BPIEc%2BTn9AYgEsEItajTJJEqTf940rw; BDSVRTM=0\n" +
                "DNT:1\n" +
                "Host:www.baidu.com\n" +
                "Pragma:no-cache\n" +
                "Upgrade-Insecure-Requests:1\n" +
                "User-Agent:Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Maxthon/5.0.3.4000 Chrome/47.0.2526.73 Safari/537.36\n" +
                "X-DevTools-Emulate-Network-Conditions-Client-Id:40AE5851-913D-4FE4-9514-22A13E2AD5E7";

        System.out.println(httpClientDownloader.sendGetRequest(url, headers));
    }

    @Test
    public void getHeader() {

    }
}

package com.tyaer.net.selenium.phantomjs;

import com.tyaer.net.selenium.downloader.PhantomjsDownloader;
import org.junit.Test;

/**
 * Created by Twin on 2017/3/20.
 */
public class PhantomjsDownloader_Test {
    public static void main(String[] args) {

    }

    @Test
    public void tt(){
        PhantomjsDownloader phantomjsDownloader = new PhantomjsDownloader();
//        String url = "http://www.site-digger.com/html/articles/20110516/proxieslist.html";
        String url = "http://a.mp.uc.cn/article.html?uc_param_str=frdnsnpfvecpntnwprdssskt&zzd_from=ucnews-iflow&dl_type=2&app=ucnews-iflow#!wm_aid=f6bf6ebda68d411b9be5c83b50f96311!!wm_id=bd06760f55cd4410bf0c9d6075d928b2";
        String download = phantomjsDownloader.download(url, 1000);
        System.out.println(download);
        phantomjsDownloader.close();
    }

    @Test
    public void sina(){
        PhantomjsDownloader phantomjsDownloader = new PhantomjsDownloader();
        String cookie = "YF-V5-G0=69afb7c26160eb8b724e8855d7b705c6; _s_tentry=-; Apache=4397365853656.0835.1484723950063; SINAGLOBAL=4397365853656.0835.1484723950063; ULV=1484723950066:1:1:1:4397365853656.0835.1484723950063:; YF-Page-G0=046bedba5b296357210631460a5bf1d2; SUB=_2AkMvI80sf8NhqwJRmP0RxGPka4V1ww7EieKZfzz3JRMxHRl-yT83qkkptRBxtDWvOJvqp2I1Y85XsOvjpRF2kw..; SUBP=0033WrSXqPxfM72-Ws9jqgMF55529P9D9WhXP5zlV.UkuM3K5Slq3ADu; YF-Ugrow-G0=9642b0b34b4c0d569ed7a372f8823a8e; WBtopGlobal_register_version=c689c52160d0ea3b";
        String download = phantomjsDownloader.download("http://weibo.com/1239246050/EyinWrrtf?filter=hot&root_comment_id=4081738542586091&type=comment",cookie, 1000);
        System.out.println(download);
        phantomjsDownloader.close();
    }
}

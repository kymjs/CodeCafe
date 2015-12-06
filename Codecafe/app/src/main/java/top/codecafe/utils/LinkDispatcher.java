package top.codecafe.utils;

import android.content.Context;

import top.codecafe.activity.BlogDetailActivity;
import top.codecafe.activity.BrowserActivity;
import top.codecafe.activity.MobelBrowserActivity;

/**
 * 链接分发调度器
 * 特殊链接,特殊处理
 *
 * @author kymjs (http://www.kymjs.com/) on 12/5/15.
 */
public class LinkDispatcher {
    public static void dispatch(Context cxt, String url) {
        if (url.contains("kymjs.com")) {
            BlogDetailActivity.goinActivity(cxt, url, null);
        } else if (url.contains("github.io") || url.contains("github.com")) {
            MobelBrowserActivity.goinActivity(cxt, url, getActionTitle(url, ""));
        } else {
            BrowserActivity.goinActivity(cxt, url);
        }
    }


    /**
     * 设置ActionBar的title
     * 特殊站点优待
     *
     * @param url      站点的url
     * @param defTitle 如果不在优待范围内
     */
    public static String getActionTitle(String url, String defTitle) {
        String title;
        //还有哪些移动页面已经适配的网站呢?
        if (url.contains("github.io")) {
            title = "GitHub博客";
        } else if (url.contains("github.com")) {
            title = "GitHub项目";
        } else if (url.contains("kymjs")) {
            title = "开源实验室";
        } else if (url.contains("droidyue")) {
            title = "技术小黑屋";
        } else if (url.contains("androidweekly")) {
            title = "Android开发技术周报";
        } else {
            title = defTitle;
        }
        return title;
    }
}

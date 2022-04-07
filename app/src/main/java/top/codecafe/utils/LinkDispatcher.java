package top.codecafe.utils;

import android.text.TextUtils;
import android.webkit.WebView;

import com.kymjs.api.Api;
import com.kymjs.common.ContextTrojan;
import com.kymjs.model.Blog;
import com.kymjs.model.BlogList;
import com.kymjs.rxvolley.RxVolley;
import com.kymjs.rxvolley.client.HttpCallback;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import top.codecafe.activity.BrowserActivity;
import top.codecafe.activity.MobelBrowserActivity;

/**
 * 链接分发调度器
 * 特殊链接,特殊处理
 *
 * @author kymjs (https://kymjs.com/) on 12/5/15.
 */
public class LinkDispatcher {
    /**
     * 分发已知适配手机页面的url,对于未知页面调用#BrowserActivity显示
     */
    private static void dispatchUrl(String url, String title) {
        if (!TextUtils.isEmpty(getActionTitle(url, title))) {
            MobelBrowserActivity.goinActivity(ContextTrojan.getApplicationContext(), url, getActionTitle(url, title));
        } else {
            BrowserActivity.goinActivity(ContextTrojan.getApplicationContext(), url);
        }
    }

    /**
     * 对不同的链接调用不同的Activity去展示
     *
     * @param webView 当前视图中的webview
     * @param url     链接
     */
    public static void dispatch(String url, WebView webView, String defaultTitle) {
        if (ConstKt.getOslabBlogList().isEmpty()) {
            new RxVolley.Builder().url(Api.BLOG_LIST)
                    .contentType(RxVolley.Method.GET)
                    .cacheTime(600)
                    .callback(new HttpCallback() {
                        @Override
                        public void onSuccessInAsync(byte[] t) {
                            super.onSuccessInAsync(t);
                            ConstKt.setOslabBlogList(ConstKt.getGson().fromJson(new String(t), BlogList.class).getItems());
                        }
                    })
                    .doTask();
        }

        if (webView != null) {
            String remoteHost = getHost(url);
            String currentHost = getHost(webView.getUrl());
            if (currentHost.equals(remoteHost)) {
                webView.loadUrl(url);
                return;
            }
        }
        dispatchUrl(url, defaultTitle);
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
        } else if (url.contains("kymjs.com")) {
            title = "开源实验室";
            for (Blog blog : ConstKt.getOslabBlogList()) {
                if (url.contains("kymjs.com") && url.contains(blog.getLink().substring("https://kymjs.com/".length()))) {
                    if (!TextUtils.isEmpty(blog.getTitle())) {
                        title = blog.getTitle();
                    }
                    break;
                }
            }
        } else if (url.contains("droidyue")) {
            title = "技术小黑屋";
        } else if (url.contains("androidweekly")) {
            title = "Android开发技术周报";
        } else if (url.contains("mp.weixin.qq.com")) {
            title = "微信公众号";
        } else {
            title = defTitle;
        }
        return title;
    }

    /**
     * 获取一个url的域名
     */
    public static String getHost(String url) {
        Pattern pattern = Pattern.compile("(http|https|ftp|svn)://[a-zA-Z0-9].?]");
        Matcher matcher = pattern.matcher(url);
        if (matcher.find()) return matcher.group();
        else return "";
    }
}

package top.codecafe.delegate;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.text.TextUtils;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.kymjs.common.DensityUtils;
import com.kymjs.gallery.KJGalleryActivity;
import com.kymjs.themvp.view.AppDelegate;

import top.codecafe.R;
import top.codecafe.inter.OnWebViewImageListener;
import top.codecafe.utils.LinkDispatcher;
import top.codecafe.widget.EmptyLayout;


/**
 * 浏览器视图的参数设置类
 *
 * @author kymjs (https://kymjs.com/) on 12/5/15.
 */
public class BrowserDelegateOption {
    private AppDelegate viewDelegate;

    BrowserDelegateOption(AppDelegate delegate) {
        viewDelegate = delegate;
    }

    public void initWebView() {
        init();
        EmptyLayout emptyLayout = viewDelegate.get(R.id.emptylayout);
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) emptyLayout
                .getLayoutParams();
        params.height = DensityUtils.getScreenH();
        emptyLayout.setLayoutParams(params);
    }

    private WebView getWebView() {
        WebView webView = viewDelegate.get(R.id.webview);
        return webView;
    }

    private void init() {
        addWebImageShow(getWebView().getContext(), getWebView());
        getWebView().setVerticalScrollBarEnabled(false);
        getWebView().setHorizontalScrollBarEnabled(false);
        WebView.setWebContentsDebuggingEnabled(true);

        WebSettings webSettings = getWebView().getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setPluginState(WebSettings.PluginState.ON);
        webSettings.setLoadsImagesAutomatically(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setBuiltInZoomControls(false);
        webSettings.setDomStorageEnabled(true);// 打开本地缓存提供JS调用,至关重要
        webSettings.setAppCacheMaxSize(1024 * 1024 * 8);// 实现8倍缓存
        webSettings.setAppCacheEnabled(true);
        String appCachePath = getWebView().getContext().getCacheDir().getAbsolutePath();
        webSettings.setAppCachePath(appCachePath);
        webSettings.setDatabaseEnabled(true);
        webSettings.setAllowFileAccess(true);
        webSettings.setSavePassword(false);
        webSettings.setMediaPlaybackRequiresUserGesture(false);
        // 允许HTTP和HTTPS混合模式
        webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        // 设置缓存模式
        checkOpenWebCache(getWebView().getContext(), webSettings);

        // 允许跨域cookie读取
        CookieManager.getInstance().setAcceptThirdPartyCookies(getWebView(), true);
        getWebView().setOverScrollMode(View.OVER_SCROLL_NEVER);// 滚动到顶部或者底部时阴影

        getWebView().setWebViewClient(new MyWebViewClient());
        getWebView().setWebChromeClient(new MyWebChromeClient());
    }

    public static void checkOpenWebCache(Context context, WebSettings webSettings) {
        webSettings.setDomStorageEnabled(true);// 开启DOM缓存
        webSettings.setDatabaseEnabled(true);// 开启数据库缓存

        String webCachePath = getWebCachePath(context);
        if (TextUtils.isEmpty(webCachePath)) {
            webSettings.setAppCacheEnabled(false);
        } else {
            webSettings.setAppCachePath(webCachePath);
            webSettings.setAppCacheEnabled(true);
        }

        webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);
    }

    public static String getWebCachePath(Context context) {
        try {
            return context.getApplicationContext().getCacheDir().getAbsolutePath();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    private class MyWebChromeClient extends WebChromeClient {

        @Override
        public void onProgressChanged(WebView view, int newProgress) { // 进度
            super.onProgressChanged(view, newProgress);
            if (newProgress > 90) {
                EmptyLayout emptyLayout = viewDelegate.get(R.id.emptylayout);
                emptyLayout.dismiss();
            }
        }

        @Override
        public void onReceivedTitle(WebView view, String title) {
            super.onReceivedTitle(view, title);
            viewDelegate.getActivity().setTitle(LinkDispatcher.getActionTitle(view.getUrl(), title));
        }
    }

    private class MyWebViewClient extends WebViewClient {

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            EmptyLayout emptyLayout = viewDelegate.get(R.id.emptylayout);
            emptyLayout.dismiss();
            String test = "javascript:function hideBottom(){" +
                    "var obj1 = document.getElementsByClassName('app-open-drawer')[0]; obj1.remove();" +
                    "var obj2 = document.getElementsByClassName('open-button')[0]; obj2.remove();" +
                    "var obj3 = document.getElementsByClassName('author-info-block')[0]; obj3.remove();" +
                    "var obj4 = document.getElementsByClassName('wechat-img')[0]; obj4.remove();" +
                    "var obj5 = document.getElementsByClassName('main-header-box')[0]; obj5.remove();" +
                    "}";
            getWebView().loadUrl(test);
            getWebView().loadUrl("javascript:hideBottom();");
        }

        @Nullable
        @Override
        public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
            if (request.getUrl().toString().contains("recommend_api")) {
                return new WebResourceResponse("text/html", "utf-8", null);
            }
            return super.shouldInterceptRequest(view, request);
        }

        @Override
        @RequiresApi(api = Build.VERSION_CODES.M)
        public void onReceivedError(final WebView view, WebResourceRequest request, WebResourceError error) {
            super.onReceivedError(view, request, error);
            if (request.getUrl().toString().contains("recommend_api")) {
                return;
            }
            final EmptyLayout emptyLayout = viewDelegate.get(R.id.emptylayout);
            emptyLayout.setOnLayoutClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    view.loadUrl(view.getUrl());
                    emptyLayout.setErrorType(EmptyLayout.NETWORK_LOADING);
                }
            });
            emptyLayout.setErrorType(EmptyLayout.NETWORK_ERROR);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            LinkDispatcher.dispatch(url, view, null);
            return true;
        }
    }

    /**
     * 添加网页的点击图片展示支持
     */
    @JavascriptInterface
    @SuppressLint("JavascriptInterface")
    public static void addWebImageShow(final Context cxt, WebView wv) {
        wv.addJavascriptInterface(new OnWebViewImageListener() {
            @Override
            @JavascriptInterface
            public void showImagePreview(String bigImageUrl) {
                if (!TextUtils.isEmpty(bigImageUrl)) {
                    KJGalleryActivity.toGallery(cxt, bigImageUrl);
                }
            }
        }, "mWebViewImageListener");
    }

    /**
     * 设置html中图片支持点击预览
     *
     * @param body html内容
     * @return 修改后的内容
     */
    public static String setImagePreview(String body) {
        // 过滤掉 img标签的width,height属性
        body = body.replaceAll("(<img[^>]*?)\\s+width\\s*=\\s*\\S+", "$1");
        body = body.replaceAll("(<img[^>]*?)\\s+height\\s*=\\s*\\S+", "$1");
        // 添加点击图片放大支持
        body = body.replaceAll("(<img[^>]+src=\")(\\S+)\"",
                "$1$2\" onClick=\"mWebViewImageListener.showImagePreview('$2')\"");
        return body;
    }
}

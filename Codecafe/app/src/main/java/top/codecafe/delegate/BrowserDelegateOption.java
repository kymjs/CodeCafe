package top.codecafe.delegate;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;
import android.widget.ZoomButtonsController;

import com.kymjs.frame.view.AppDelegate;
import com.kymjs.gallery.KJGalleryActivity;
import com.kymjs.kjcore.utils.DensityUtils;
import com.kymjs.kjcore.utils.StringUtils;

import top.codecafe.R;
import top.codecafe.inter.OnWebViewImageListener;
import top.codecafe.utils.LinkDispatcher;
import top.codecafe.widget.EmptyLayout;

/**
 * 浏览器视图的参数设置类
 *
 * @author kymjs (http://www.kymjs.com/) on 12/5/15.
 */
public class BrowserDelegateOption {
    private AppDelegate viewDelegate;

    BrowserDelegateOption(AppDelegate delegate) {
        viewDelegate = delegate;
    }

    public void initWebView() {
        WebView webView = viewDelegate.get(R.id.webview);
        initWebView(webView);
        webView.setWebViewClient(new MyWebViewClient());
        webView.setWebChromeClient(new MyWebChromeeClient());
        EmptyLayout emptyLayout = viewDelegate.get(R.id.emptylayout);
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) emptyLayout
                .getLayoutParams();
        params.height = DensityUtils.getScreenH(viewDelegate.getActivity());
        emptyLayout.setLayoutParams(params);
    }

    private class MyWebChromeeClient extends WebChromeClient {
        @Override
        public void onProgressChanged(WebView view, int newProgress) { // 进度
            super.onProgressChanged(view, newProgress);
            if (newProgress > 60) {
                EmptyLayout emptyLayout = viewDelegate.get(R.id.emptylayout);
                emptyLayout.dismiss();
            }
        }

        @Override
        public void onReceivedTitle(WebView view, String title) {
            super.onReceivedTitle(view, title);
            viewDelegate.getActivity().setTitle(LinkDispatcher.getActionTitle(view.getUrl(),
                    title));
        }
    }

    private class MyWebViewClient extends WebViewClient {
        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            EmptyLayout emptyLayout = viewDelegate.get(R.id.emptylayout);
            emptyLayout.dismiss();
            WebView webView = viewDelegate.get(R.id.webview);
            if (!webView.getSettings().getLoadsImagesAutomatically()) {
                webView.getSettings().setLoadsImagesAutomatically(true);
            }
        }

        @Override
        public void onReceivedError(final WebView view, WebResourceRequest request,
                                    WebResourceError error) {
            super.onReceivedError(view, request, error);
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
            LinkDispatcher.dispatch(view, url);
            return true;
        }
    }

    @SuppressLint({"SetJavaScriptEnabled"})
    public static void initWebView(WebView webView) {
        WebSettings settings = webView.getSettings();
        settings.setDefaultFontSize(15);
        settings.setJavaScriptEnabled(true);
        settings.setSupportZoom(true);
        settings.setBuiltInZoomControls(true);

        int sysVersion = Build.VERSION.SDK_INT;
        if (sysVersion >= 19) {
            webView.getSettings().setLoadsImagesAutomatically(true);
        } else {
            webView.getSettings().setLoadsImagesAutomatically(false);
        }

        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        if (sysVersion >= 11) {
            settings.setDisplayZoomControls(false);
        } else {
            ZoomButtonsController zbc = new ZoomButtonsController(webView);
            zbc.getZoomControls().setVisibility(View.GONE);
        }
        addWebImageShow(webView.getContext(), webView);
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
                if (!StringUtils.isEmpty(bigImageUrl)) {
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

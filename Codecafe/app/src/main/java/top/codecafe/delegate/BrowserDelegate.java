package top.codecafe.delegate;

import android.webkit.WebView;

import com.kymjs.frame.view.AppDelegate;

import top.codecafe.R;

/**
 * 浏览器视图
 *
 * @author kymjs (http://www.kymjs.com/) on 12/5/15.
 */
public class BrowserDelegate extends AppDelegate {
    @Override
    public int getRootLayoutId() {
        return R.layout.layout_blog_detail;
    }

    public void setContentUrl(String url) {
        WebView webView = get(R.id.webview);
        webView.loadUrl(url);
    }

    @Override
    public void initWidget() {
        super.initWidget();
        new BrowserDelegateOption(this).initWebView();
    }
}

package top.codecafe.delegate;

import android.view.View;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;

import com.kymjs.themvp.view.AppDelegate;

import top.codecafe.R;

/**
 * 浏览器视图
 *
 * @author kymjs (https://kymjs.com/) on 12/5/15.
 */
public class BrowserDelegate extends AppDelegate {

    public LinearLayout mLayoutBottom;
    public WebView webView;

    @Override
    public int getRootLayoutId() {
        return R.layout.layout_browser;
    }

    public void setContentUrl(String url) {
        webView.loadUrl(url);
    }

    @Override
    public void initWidget() {
        super.initWidget();
        webView = get(R.id.webview);
        mLayoutBottom = (LinearLayout) View.inflate(getActivity(),
                R.layout.layout_browser_bottombar, null);
        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams
                .WRAP_CONTENT);
        params.leftMargin = 60;
        params.rightMargin = 60;
        params.bottomMargin = 30;
        params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        ((RelativeLayout) get(R.id.browser_root)).addView(mLayoutBottom, 1, params);
        mLayoutBottom.setVisibility(View.GONE);

        new BrowserDelegateOption(this).initWebView();
    }

    @Override
    public int getOptionsMenuId() {
        return R.menu.menu_share;
    }
}

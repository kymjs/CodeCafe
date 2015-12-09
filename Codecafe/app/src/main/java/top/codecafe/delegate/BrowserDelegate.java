package top.codecafe.delegate;

import android.content.Intent;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.kymjs.frame.view.AppDelegate;

import top.codecafe.R;

/**
 * 浏览器视图
 *
 * @author kymjs (http://www.kymjs.com/) on 12/5/15.
 */
public class BrowserDelegate extends AppDelegate implements View.OnClickListener {

    private Animation animBottomIn, animBottomOut;
    private GestureDetector mGestureDetector;
    private int flag = 0; // 双击事件需要
    public LinearLayout mLayoutBottom;
    public WebView webView;

    private String currentUrl;

    @Override
    public int getRootLayoutId() {
        return R.layout.layout_browser;
    }

    public void setContentUrl(String url) {
        webView.loadUrl(url);
        currentUrl = url;
    }

    public void setCurrentUrl(String currentUrl) {
        this.currentUrl = currentUrl;
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
        mGestureDetector = new GestureDetector(getActivity(), new MyGestureListener());
        webView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return mGestureDetector.onTouchEvent(event);
            }
        });
        initBarAnim();

        mLayoutBottom.findViewById(R.id.browser_bottombar_collect).setOnClickListener(this);
        mLayoutBottom.findViewById(R.id.browser_bottombar_shared).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.browser_bottombar_collect:
                break;
            case R.id.browser_bottombar_shared:
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, currentUrl);
                sendIntent.setType("text/plain");
                getActivity().startActivity(Intent.createChooser(sendIntent, "发送到:"));
                break;
        }
    }

    /**
     * 初始化上下栏的动画并设置结束监听事件
     */
    private void initBarAnim() {
        animBottomIn = AnimationUtils.loadAnimation(getActivity(), R.anim.anim_bottom_in);
        animBottomOut = AnimationUtils.loadAnimation(getActivity(), R.anim.anim_bottom_out);
        animBottomIn.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mLayoutBottom.setVisibility(View.VISIBLE);
            }
        });
        animBottomOut.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mLayoutBottom.setVisibility(View.GONE);
            }
        });
    }

    private class MyGestureListener extends GestureDetector.SimpleOnGestureListener {

        @Override
        public boolean onDoubleTap(MotionEvent e) {// webview的双击事件
            if (flag % 2 == 0) {
                flag++;
                mLayoutBottom.setVisibility(View.VISIBLE);
                mLayoutBottom.startAnimation(animBottomIn);
            } else {
                flag++;
                mLayoutBottom.startAnimation(animBottomOut);
            }
            return super.onDoubleTap(e);
        }
    }

}

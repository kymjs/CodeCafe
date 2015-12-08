package top.codecafe.delegate;

import android.graphics.Bitmap;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.CoordinatorLayout.LayoutParams;
import android.support.v7.graphics.Palette;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.WebView;
import android.widget.LinearLayout;

import com.kymjs.api.Api;
import com.kymjs.kjcore.Core;
import com.kymjs.kjcore.http.HttpCallBack;
import com.kymjs.kjcore.utils.KJLoger;

import top.codecafe.R;

/**
 * 开源实验室博客详情界面
 *
 * @author kymjs (http://www.kymjs.com/) on 12/4/15.
 */
public class BlogDetailDelegate extends BaseDetailDelegate {

    private Animation animBottomIn, animBottomOut;
    private GestureDetector mGestureDetector;
    private int flag = 0; // 双击事件需要
    public LinearLayout mLayoutBottom;

    @Override
    public int getContentLayoutId() {
        return R.layout.layout_blog_detail;
    }

    public void setContent(String text) {
        WebView webView = get(R.id.webview);
        webView.loadDataWithBaseURL(null, text, "text/html", "UTF-8", null);
    }

    public void setContentUrl(String url) {
        WebView webView = get(R.id.webview);
        webView.loadUrl(url);
    }

    @Override
    public void initWidget() {
        super.initWidget();
        initActionbarImage();
        mLayoutBottom = (LinearLayout) View.inflate(getActivity(),
                R.layout.layout_browser_bottombar, null);
        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams
                .WRAP_CONTENT);
        params.leftMargin = 60;
        params.rightMargin = 60;
        params.bottomMargin = 20;
        params.gravity = Gravity.BOTTOM;
        ((CoordinatorLayout) getRootView()).addView(mLayoutBottom, params);
        mLayoutBottom.setVisibility(View.GONE);
        new BrowserDelegateOption(this).initWebView();
        mGestureDetector = new GestureDetector(getActivity(), new MyGestureListener());
        WebView webView = get(R.id.webview);
        webView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return mGestureDetector.onTouchEvent(event);
            }
        });
        initBarAnim();
    }

    /**
     * 初始化状态栏的显示
     * 首先访问网络请求最新的图片地址,加载图片,根据图片主题设置actionbar颜色
     */
    public void initActionbarImage() {
        Core.get(Api.ZONE_IMAGE, new HttpCallBack() {
            @Override
            public void onSuccess(String t) {
                super.onSuccess(t);
                KJLoger.debug("===title图片" + t);
                new Core.Builder().url(t)
                        .view(get(R.id.actionbar_image))
                        .errorBitmapRes(R.mipmap.def_zone_image)
                        .loadBitmapRes(R.mipmap.def_zone_image)
                        .callback(new HttpCallBack() {
                            @Override
                            public void onSuccess(Bitmap b) {
                                super.onSuccess(b);
                                Palette.from(b).generate(new Palette.PaletteAsyncListener() {
                                    @Override
                                    public void onGenerated(final Palette palette) {
                                        int defaultColor = rootView.getResources().getColor
                                                (android.R.color.white);
                                        int titleColor = palette.getLightVibrantColor(defaultColor);
                                        CollapsingToolbarLayout collapsingToolbar = get(R.id
                                                .collapsing_toolbar);
                                        collapsingToolbar.setExpandedTitleColor(titleColor);
                                    }
                                });
                            }
                        }).doTask();
            }
        });
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

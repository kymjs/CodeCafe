package top.codecafe.delegate;

import android.graphics.Bitmap;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.graphics.Palette;
import android.view.Gravity;
import android.widget.RelativeLayout;

import com.kymjs.api.Api;
import com.kymjs.core.Core;
import com.kymjs.core.bitmap.client.BitmapCore;
import com.kymjs.core.client.HttpCallback;
import com.kymjs.core.toolbox.Loger;

import java.util.Map;

import top.codecafe.R;

/**
 * 开源实验室博客详情界面
 *
 * @author kymjs (http://www.kymjs.com/) on 12/4/15.
 */
public class BlogDetailDelegate extends BaseDetailDelegate {

    /**
     * 伪多继承(dai)
     */
    private BrowserDelegate browserDelegate = new BrowserDelegate();

    @Override
    public int getContentLayoutId() {
        return browserDelegate.getRootLayoutId();
    }

    @Override
    public void initWidget() {
        super.initWidget();
        initActionbarImage();
        browserDelegate.setRootView(rootView);
        browserDelegate.initWidget();

        ((RelativeLayout) get(R.id.browser_root)).removeView(browserDelegate.mLayoutBottom);
        CoordinatorLayout.LayoutParams params = new CoordinatorLayout.LayoutParams(CoordinatorLayout
                .LayoutParams.MATCH_PARENT, CoordinatorLayout.LayoutParams.WRAP_CONTENT);
        params.leftMargin = 60;
        params.rightMargin = 60;
        params.bottomMargin = 30;
        params.gravity = Gravity.BOTTOM;
        ((CoordinatorLayout) getRootView()).addView(browserDelegate.mLayoutBottom, params);
    }

    /**
     * 初始化状态栏的显示
     * 首先访问网络请求最新的图片地址,加载图片,根据图片主题设置actionbar颜色
     */
    public void initActionbarImage() {
        Core.get(Api.ZONE_IMAGE, new HttpCallback() {
            @Override
            public void onSuccess(String t) {
                super.onSuccess(t);
                Loger.debug("===title图片" + t);
                new BitmapCore.Builder().url(t)
                        .view(get(R.id.actionbar_image))
                        .errorResId(R.mipmap.def_zone_image)
                        .loadResId(R.mipmap.def_zone_image)
                        .callback(new HttpCallback() {
                            @Override
                            public void onSuccess(Map<String, String> headers, Bitmap b) {
                                super.onSuccess(headers, b);
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

    public void setContent(String text) {
        browserDelegate.webView.loadDataWithBaseURL(null, text, "text/html", "UTF-8", null);
    }

    public void setContentUrl(String url) {
        browserDelegate.setContentUrl(url);
    }

    public void setCurrentUrl(String currentUrl) {
        browserDelegate.setCurrentUrl(currentUrl);
    }
}

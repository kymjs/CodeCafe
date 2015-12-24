package top.codecafe.delegate;

import android.graphics.Bitmap;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.graphics.Palette;
import android.view.Gravity;
import android.widget.RelativeLayout;

import com.kymjs.api.Api;
import com.kymjs.core.bitmap.client.BitmapCore;
import com.kymjs.rxvolley.RxVolley;
import com.kymjs.rxvolley.rx.Result;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import top.codecafe.R;

/**
 * 开源实验室博客详情界面
 *
 * @author kymjs (http://www.kymjs.com/) on 12/4/15.
 */
public class BlogDetailDelegate extends BaseDetailDelegate {

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
        new RxVolley.Builder().url(Api.ZONE_IMAGE).getResult()
                .filter(new Func1<Result, Boolean>() {
                    @Override
                    public Boolean call(Result result) {
                        return result != null
                                && result.data != null
                                && result.data.length != 0;
                    }
                })
                .map(new Func1<Result, String>() {
                    @Override
                    public String call(Result result) {
                        return new String(result.data);
                    }
                })
                .flatMap(new Func1<String, Observable<Bitmap>>() {
                    @Override
                    public Observable<Bitmap> call(String t) {
                        return new BitmapCore.Builder().url(t)
                                .view(get(R.id.actionbar_image))
                                .loadResId(R.mipmap.def_zone_image)
                                .errorResId(R.mipmap.def_zone_image)
                                .getResult();
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Bitmap>() {
                    @Override
                    public void call(Bitmap b) {
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
                });

//        RxVolley.get(Api.ZONE_IMAGE, new HttpCallback() {
//            @Override
//            public void onSuccess(String t) {
//                super.onSuccess(t);
//                Loger.debug("===title图片" + t);
//                new BitmapCore.Builder().url(t)
//                        .view(get(R.id.actionbar_image))
//                        .errorResId(R.mipmap.def_zone_image)
//                        .loadResId(R.mipmap.def_zone_image)
//                        .callback(new HttpCallback() {
//                            @Override
//                            public void onSuccess(Map<String, String> headers, Bitmap b) {
//                                Palette.from(b).generate(new Palette.PaletteAsyncListener() {
//                                    @Override
//                                    public void onGenerated(final Palette palette) {
//                                        int defaultColor = rootView.getResources().getColor
//                                                (android.R.color.white);
//                                        int titleColor = palette.getLightVibrantColor
// (defaultColor);
//                                        CollapsingToolbarLayout collapsingToolbar = get(R.id
//                                                .collapsing_toolbar);
//                                        collapsingToolbar.setExpandedTitleColor(titleColor);
//                                    }
//                                });
//                            }
//                        }).doTask();
//            }
//        });
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

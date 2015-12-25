package top.codecafe.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.kymjs.api.Api;
import com.kymjs.model.osc.OSCBlogEntity;
import com.kymjs.rxvolley.RxVolley;
import com.kymjs.rxvolley.rx.Result;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import top.codecafe.R;
import top.codecafe.delegate.BrowserDelegateOption;
import top.codecafe.utils.XmlUtils;
import top.codecafe.widget.EmptyLayout;

/**
 * @author kymjs (http://www.kymjs.com/) on 12/10/15.
 */
public class OSCBlogDetailActivity extends BlogDetailActivity {

    public static final String KEY_BLOG_ID = "osc_blog_id";
    private int blogId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        blogId = getIntent().getIntExtra(KEY_BLOG_ID, 0);
        super.onCreate(savedInstanceState);
    }

    /**
     * 读取缓存内容
     */
    protected void readCache() {
        Observable.just(RxVolley.getCache(Api.OSC_BLOG_DETAIL + blogId))
                .filter(new Func1<byte[], Boolean>() {
                    @Override
                    public Boolean call(byte[] cache) {
                        httpCache = cache;
                        return cache != null && cache.length != 0;
                    }
                })
                .map(new Func1<byte[], String>() {
                    @Override
                    public String call(byte[] bytes) {
                        contentHtml = XmlUtils.toBean(OSCBlogEntity.class, bytes).getBlog()
                                .getBody();
                        return parserHtml(contentHtml);
                    }
                })
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String content) {
                        contentHtml = content;
                        emptyLayout.dismiss();
                        viewDelegate.setContent(content);
                        viewDelegate.setCurrentUrl(url);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                    }
                });
    }

    @Override
    public void doRequest() {
        new RxVolley.Builder().url(Api.OSC_BLOG_DETAIL + blogId).getResult()
                .map(new Func1<Result, String>() {
                    @Override
                    public String call(Result result) {
                        contentHtml = XmlUtils.toBean(OSCBlogEntity.class, result.data)
                                .getBlog().getBody();
                        return contentHtml = parserHtml(contentHtml);
                    }
                })
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        if (!new String(httpCache).equals(s) && viewDelegate != null) {
                            if (contentHtml != null) {
                                viewDelegate.setContent(contentHtml);
                            } else {
                                viewDelegate.setContent(s);
                            }
                        }
                        emptyLayout.dismiss();
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        emptyLayout.setErrorType(EmptyLayout.NETWORK_ERROR);
                    }
                });

//        RxVolley.get(Api.OSC_BLOG_DETAIL + blogId, new HttpCallback() {
//            @Override
//            public void onSuccessInAsync(byte[] t) {
//                super.onSuccessInAsync(t);
//                contentHtml = XmlUtils.toBean(OSCBlogEntity.class, t).getBlog().getBody();
//                contentHtml = parserHtml(contentHtml);
//            }
//
//            @Override
//            public void onSuccess(String t) {
//                super.onSuccess(t);
//                if (!new String(httpCache).equals(t) && viewDelegate != null) {
//                    if (contentHtml != null) {
//                        viewDelegate.setContent(contentHtml);
//                    } else {
//                        viewDelegate.setContent(t);
//                    }
//                }
//                emptyLayout.dismiss();
//            }
//
//            @Override
//            public void onFailure(int errorNo, String strMsg) {
//                super.onFailure(errorNo, strMsg);
//                emptyLayout.setErrorType(EmptyLayout.NETWORK_ERROR);
//            }
//        });
    }

    /**
     * 跳转到博客详情界面
     *
     * @param url   传递要显示的博客的地址
     * @param title 传递要显示的博客的标题
     */
    public static void goinActivity(Context cxt, int id, @NonNull String url, @Nullable String
            title) {
        Intent intent = new Intent(cxt, OSCBlogDetailActivity.class);
        intent.putExtra(KEY_BLOG_URL, url);
        if (TextUtils.isEmpty(title))
            title = cxt.getString(R.string.kymjs_blog_name);
        intent.putExtra(KEY_BLOG_TITLE, title);
        intent.putExtra(KEY_BLOG_ID, id);
        cxt.startActivity(intent);
    }

    /**
     * 对博客数据加工,适应手机浏览
     */
    public static String parserHtml(String html) {
        if (!TextUtils.isEmpty(html)) {
            html = BrowserDelegateOption.setImagePreview(html);
        }
        String commonStyle = "<link rel=\"stylesheet\" type=\"text/css\" " +
                "href=\"file:///android_asset/template/common.css\">";
        html = commonStyle + html;
        return html;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        System.gc();
    }
}

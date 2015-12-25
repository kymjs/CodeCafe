package top.codecafe.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.ActionBar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;

import com.kymjs.base.backactivity.BaseBackActivity;
import com.kymjs.rxvolley.RxVolley;
import com.kymjs.rxvolley.rx.Result;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import top.codecafe.R;
import top.codecafe.delegate.BlogDetailDelegate;
import top.codecafe.delegate.BrowserDelegateOption;
import top.codecafe.inter.IRequestVo;
import top.codecafe.widget.EmptyLayout;

/**
 * 博客详情界面
 *
 * @author kymjs (http://www.kymjs.com/) on 12/4/15.
 */
public class BlogDetailActivity extends BaseBackActivity<BlogDetailDelegate> implements IRequestVo {

    public static final String KEY_BLOG_URL = "blog_url_key";
    public static final String KEY_BLOG_TITLE = "blog_title_key";
    protected String url;

    protected EmptyLayout emptyLayout;
    protected WebView webView;
    protected String contentHtml = null;
    protected byte[] httpCache = null;

    @Override
    protected Class<BlogDetailDelegate> getDelegateClass() {
        return BlogDetailDelegate.class;
    }

    @Override
    protected void bindEvenListener() {
        super.bindEvenListener();
        webView = viewDelegate.get(R.id.webview);
        emptyLayout = viewDelegate.get(R.id.emptylayout);
        emptyLayout.setOnLayoutClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doRequest();
                emptyLayout.setErrorType(EmptyLayout.NETWORK_LOADING);
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        url = intent.getStringExtra(KEY_BLOG_URL);
        String title = intent.getStringExtra(KEY_BLOG_TITLE);
        CollapsingToolbarLayout collapsingToolbar = viewDelegate.get(R.id.collapsing_toolbar);
        if (title != null) {
            collapsingToolbar.setTitle(title);
        } else {
            collapsingToolbar.setTitle(getString(R.string.kymjs_blog_name));
        }

        readCache();
        doRequest();
    }

    /**
     * 读取缓存内容
     */
    protected void readCache() {
        Observable.just(RxVolley.getCache(url))
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
                        return parserHtml(new String(bytes));
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
    protected void onRestart() {
        super.onRestart();
        // 不知道为什么点击完webview图片后,再返回,webview内容就没了,只好再设置一次
        viewDelegate.setContent(contentHtml);
    }

    @Override
    protected void onResume() {
        super.onResume();
        webView.onResume();
    }


    @Override
    protected void onPause() {
        super.onPause();
        webView.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        webView.destroy();
        System.gc();
    }


    @Override
    protected void initToolbar() {
        super.initToolbar();
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void doRequest() {
        if (TextUtils.isEmpty(url)) return;
        new RxVolley.Builder().url(url).getResult()
                .map(new Func1<Result, String>() {
                    @Override
                    public String call(Result result) {
                        return contentHtml = parserHtml(new String(result.data));
                    }
                })
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        if (!new String(httpCache).equals(s)
                                && viewDelegate != null
                                && contentHtml != null) {
                            viewDelegate.setContent(contentHtml);
                        }
                        emptyLayout.dismiss();
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        emptyLayout.setErrorType(EmptyLayout.NETWORK_ERROR);
                    }
                });

//        RxVolley.get(url, new HttpCallback() {
//                    @Override
//                    public void onSuccessInAsync(byte[] t) {
//                        super.onSuccessInAsync(t);
//                        contentHtml = parserHtml(new String(t));
//                    }
//
//                    @Override
//                    public void onSuccess(String t) {
//                        super.onSuccess(t);
//                        if (!new String(httpCache).equals(t) && viewDelegate != null
//                                && contentHtml != null) {
//                            viewDelegate.setContent(contentHtml);
//                        }
//                        emptyLayout.dismiss();
//                    }
//
//                    @Override
//                    public void onFailure(int errorNo, String strMsg) {
//                        super.onFailure(errorNo, strMsg);
//
//                    }
//                }
//        );
    }

    /**
     * 跳转到博客详情界面
     *
     * @param url   传递要显示的博客的地址
     * @param title 传递要显示的博客的标题
     */
    public static void goinActivity(Context cxt, @NonNull String url, @Nullable String title) {
        Intent intent = new Intent(cxt, BlogDetailActivity.class);
        intent.putExtra(KEY_BLOG_URL, url);
        if (TextUtils.isEmpty(title))
            title = cxt.getString(R.string.kymjs_blog_name);
        intent.putExtra(KEY_BLOG_TITLE, title);
        cxt.startActivity(intent);
    }

    public static final String regEx_script =
            "<[\\s]*?script[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?script[\\s]*?>";
    public static final String regEx_header =
            "<[\\s]*?header[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?header[\\s]*?>";
    public static final String regEx_footer =
            "<[\\s]*?footer[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?footer[\\s]*?>";

    /**
     * 对博客数据加工,适应手机浏览
     */
    public static String parserHtml(String html) {
        html = html.replaceAll(regEx_script, "");
        html = html.replaceAll(regEx_header, "");
        html = html.replaceAll(regEx_footer, "");
        html = html.replaceAll("<img src=\"/", "<img src=\"http://kymjs.com/");
        html = html.replaceAll("<a href=\"/donate\">", "<a href=\"http://kymjs.com/donate\">");
        html = BrowserDelegateOption.setImagePreview(html);
        String commonStyle = "<link rel=\"stylesheet\" type=\"text/css\" " +
                "href=\"file:///android_asset/template/common.css\">";
        html = commonStyle + html;
        return html;
    }
}

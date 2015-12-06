package top.codecafe.activity;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.ActionBar;
import android.view.MenuItem;
import android.view.View;

import com.kymjs.base.backactivity.BaseBackActivity;
import com.kymjs.kjcore.Core;
import com.kymjs.kjcore.http.HttpCallBack;
import com.kymjs.kjcore.utils.StringUtils;

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
    private String url;

    private EmptyLayout emptyLayout;

    @Override
    protected Class<BlogDetailDelegate> getDelegateClass() {
        return BlogDetailDelegate.class;
    }

    @Override
    protected void bindEvenListener() {
        super.bindEvenListener();
        emptyLayout = viewDelegate.get(R.id.emptylayout);
        emptyLayout.setOnLayoutClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doRequest();
            }
        });

        Intent intent = getIntent();
        url = intent.getStringExtra(KEY_BLOG_URL);
        String title = intent.getStringExtra(KEY_BLOG_TITLE);
        CollapsingToolbarLayout collapsingToolbar = viewDelegate.get(R.id.collapsing_toolbar);
        if (title != null) {
            collapsingToolbar.setTitle(title);
        } else {
            collapsingToolbar.setTitle(getString(R.string.kymjs_blog_name));
        }
        doRequest();
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
        if (StringUtils.isEmpty(url)) return;
        Core.get(url, new HttpCallBack() {
            String contentHtml = null;

            @Override
            public void onSuccessInAsync(byte[] t) {
                super.onSuccessInAsync(t);
                contentHtml = parserHtml(new String(t));
            }

            @Override
            public void onSuccess(String t) {
                super.onSuccess(t);
                if (contentHtml != null) {
                    viewDelegate.setContent(contentHtml);
                } else {
                    viewDelegate.setContent(t);
                }
                emptyLayout.dismiss();
            }

            @Override
            public void onFailure(int errorNo, String strMsg) {
                super.onFailure(errorNo, strMsg);
                emptyLayout.setErrorType(EmptyLayout.NETWORK_ERROR);
            }
        });
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
        if (StringUtils.isEmpty(title))
            title = cxt.getString(R.string.kymjs_blog_name);
        intent.putExtra(KEY_BLOG_TITLE, title);
        cxt.startActivity(intent);
    }

    public static final String regEx_script = "<[\\s]*?script[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?script[\\s]*?>";
    public static final String regEx_header = "<[\\s]*?header[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?header[\\s]*?>";
    public static final String regEx_footer = "<[\\s]*?footer[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?footer[\\s]*?>";

    /**
     * 对博客数据加工,适应手机浏览
     */
    public static String parserHtml(String html) {
        html = html.replaceAll(regEx_script, "");
        html = html.replaceAll(regEx_header, "");
        html = html.replaceAll(regEx_footer, "");
        html = html.replaceAll("<img src=\"/", "<img src=\"http://kymjs.com/");
        html = BrowserDelegateOption.setImagePreview(html);
        String commonStyle = "<link rel=\"stylesheet\" type=\"text/css\" " +
                "href=\"file:///android_asset/template/common.css\">";
        html = commonStyle + html;
        return html;
    }
}

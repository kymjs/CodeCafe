package top.codecafe.activity;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;

import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.kymjs.base.backactivity.BaseBackActivity;

import top.codecafe.R;
import top.codecafe.delegate.BlogDetailDelegate;
import top.codecafe.utils.LinkDispatcher;
import top.codecafe.utils.Tools;

/**
 * 对手机页面做了适配的网站
 *
 * @author kymjs (https://kymjs.com/) on 12/5/15.
 */
public class MobelBrowserActivity extends BaseBackActivity<BlogDetailDelegate> {

    public static final String KEY_BLOG_URL = "blog_url_key";
    public static final String KEY_BLOG_TITLE = "blog_title_key";
    private String title;

    @Override
    protected Class<BlogDetailDelegate> getDelegateClass() {
        return BlogDetailDelegate.class;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        String url = intent.getStringExtra(KEY_BLOG_URL);
        title = intent.getStringExtra(KEY_BLOG_TITLE);
        CollapsingToolbarLayout collapsingToolbar = viewDelegate.get(R.id.collapsing_toolbar);
        title = LinkDispatcher.getActionTitle(url, title);
        if (title != null) {
            collapsingToolbar.setTitle(title);
        } else {
            collapsingToolbar.setTitle(getString(R.string.app_name));
        }

        viewDelegate.setContentUrl(url);
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
        if (item.getItemId() == R.id.action_share && viewDelegate != null && viewDelegate.getWebView() != null) {
            Tools.shareUrl(this, title, viewDelegate.getWebView().getUrl());
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 跳转到博客详情界面
     *
     * @param url   传递要显示的博客的地址
     * @param title 传递要显示的博客的标题
     */
    public static void goinActivity(Context cxt, @NonNull String url, @Nullable String title) {
        Intent intent = new Intent(cxt, MobelBrowserActivity.class);
        intent.putExtra(KEY_BLOG_URL, url);
        intent.putExtra(KEY_BLOG_TITLE, title);
        if (cxt instanceof Application) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        cxt.startActivity(intent);
    }
}

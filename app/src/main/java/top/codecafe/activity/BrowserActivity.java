package top.codecafe.activity;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;

import com.kymjs.base.backactivity.BaseBackActivity;

import top.codecafe.R;
import top.codecafe.delegate.BrowserDelegate;
import top.codecafe.utils.Tools;

/**
 * 全局浏览器界面
 *
 * @author kymjs (https://kymjs.com/) on 12/5/15.
 */
public class BrowserActivity extends BaseBackActivity<BrowserDelegate> {
    public static final String KEY_URL = "browser_url";

    @Override
    protected Class<BrowserDelegate> getDelegateClass() {
        return BrowserDelegate.class;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        String url = intent.getStringExtra(KEY_URL);
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
        if (item.getItemId() == R.id.action_share && viewDelegate != null && viewDelegate.webView != null) {
            Tools.shareUrl(this, viewDelegate.webView.getUrl());
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onResume() {
        super.onResume();
        viewDelegate.webView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        viewDelegate.webView.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (viewDelegate != null && viewDelegate.webView != null) {
            viewDelegate.webView.removeAllViews();
            viewDelegate.webView.destroy();
        }
    }

    /**
     * 跳转到博客详情界面
     *
     * @param url 传递要显示的文章的地址
     */
    public static void goinActivity(Context cxt, @NonNull String url) {
        Intent intent = new Intent(cxt, BrowserActivity.class);
        intent.putExtra(KEY_URL, url);
        if (cxt instanceof Application) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        cxt.startActivity(intent);
    }
}

package top.codecafe.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.view.MenuItem;

import com.kymjs.base.backactivity.BaseBackActivity;

import top.codecafe.delegate.BrowserDelegate;

/**
 * 全局浏览器界面
 *
 * @author kymjs (http://www.kymjs.com/) on 12/5/15.
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
        viewDelegate.webView.destroy();
    }
    
    /**
     * 跳转到博客详情界面
     *
     * @param url 传递要显示的文章的地址
     */
    public static void goinActivity(Context cxt, @NonNull String url) {
        Intent intent = new Intent(cxt, BrowserActivity.class);
        intent.putExtra(KEY_URL, url);
        cxt.startActivity(intent);
    }
}

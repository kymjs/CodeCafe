package top.codecafe.activity;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.view.MenuItem;
import android.view.View;

import com.kymjs.base.backactivity.BaseBackActivity;
import com.kymjs.kjcore.Core;
import com.kymjs.kjcore.http.HttpCallBack;
import com.kymjs.kjcore.http.KJHttp;
import com.kymjs.kjcore.utils.StringUtils;
import com.kymjs.model.XituBlog;

import top.codecafe.R;
import top.codecafe.delegate.BrowserDelegate;
import top.codecafe.inter.IRequestVo;
import top.codecafe.utils.LinkDispatcher;
import top.codecafe.widget.EmptyLayout;

/**
 * 来自稀土掘金数据的博客详情界面
 *
 * @author kymjs (http://www.kymjs.com/) on 12/4/15.
 */
public class XituDetailActivity extends BaseBackActivity<BrowserDelegate> implements IRequestVo {
    public static final String KEY_XITU_DATA = "xitu_data_key";
    private XituBlog data;

    private EmptyLayout emptyLayout;
    private String contentUrl;

    @Override
    protected Class<BrowserDelegate> getDelegateClass() {
        return BrowserDelegate.class;
    }

    @Override
    protected void bindEvenListener() {
        super.bindEvenListener();
        emptyLayout = viewDelegate.get(R.id.emptylayout);
        emptyLayout.setOnLayoutClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doRequest();
                emptyLayout.setErrorType(EmptyLayout.NETWORK_LOADING);
            }
        });

        Intent intent = getIntent();
        data = intent.getParcelableExtra(KEY_XITU_DATA);
        if (data != null) {
            doRequest();
            contentUrl = new String(KJHttp.getCache(data.getLink()));
            if (!StringUtils.isEmpty(contentUrl)) {
                viewDelegate.setContentUrl(contentUrl);
            }
        }
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
        Core.get(data.getLink(), new HttpCallBack() {

            @Override
            public void onSuccessInAsync(byte[] t) {
                super.onSuccessInAsync(t);
                contentUrl = parserHtml(new String(t));
            }

            @Override
            public void onSuccess(String t) {
                super.onSuccess(t);
                if (contentUrl != null) {
                    viewDelegate.setContentUrl(contentUrl);
                    setTitle(LinkDispatcher.getActionTitle(contentUrl, data.getTitle()));
                } else {
                    emptyLayout.setErrorType(EmptyLayout.NETWORK_ERROR);
                }
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
     * @param data 传递要显示的博客信息
     */
    public static void goinActivity(Context cxt, @NonNull XituBlog data) {
        Intent intent = new Intent(cxt, XituDetailActivity.class);
        intent.putExtra(KEY_XITU_DATA, data);
        cxt.startActivity(intent);
    }

    /**
     * 对博客数据加工,适应手机浏览
     */
    public String parserHtml(String html) {
        String title = data.getTitle();
        int sub = title.indexOf(']');
        if (sub < 0) return "";
        title = title.substring(sub + 1).trim();

        int index = html.indexOf(title + "</h2>");
        if (index < 0) return "";
        html = html.substring(0, index).trim();

        int star = html.lastIndexOf("<a href=\"");
        if (star < 0) return "";
        html = html.substring(star + 9);

        int end = html.indexOf("\"");
        if (end < 0) return "";
        html = html.substring(0, end);
        return html;
    }
}

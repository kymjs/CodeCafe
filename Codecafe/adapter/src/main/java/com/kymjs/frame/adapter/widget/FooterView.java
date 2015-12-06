package com.kymjs.frame.adapter.widget;

import android.content.Context;
import android.content.res.Resources;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * @author kymjs (http://www.kymjs.com/) on 10/5/15.
 */
public class FooterView extends LinearLayout {
    protected String loadingText = "加载中…";
    protected String noMoreText = "已加载全部";
    protected String noDataText = "暂无内容";

    private final TextView textView;
    private final ProgressBar progressBar;

    public FooterView(Context context) {
        super(context);
        textView = new TextView(context);
        progressBar = new ProgressBar(context);
        init(context);
    }


    public void init(Context context) {
        this.setOrientation(HORIZONTAL);
        this.setGravity(Gravity.CENTER);
        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.CENTER;
        params.height = dip2px(23);
        this.setLayoutParams(params);

        progressBar.setIndeterminate(true);
        LayoutParams progressBarparams = new LayoutParams(LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT);
        progressBarparams.width = progressBarparams.height = dip2px(22);
        progressBar.setLayoutParams(progressBarparams);

        LayoutParams textViewparams = new LayoutParams(LayoutParams.WRAP_CONTENT, dip2px(20));
        textView.setTextColor(0xff666666);
        textView.setTextSize(16);
        textView.setLayoutParams(textViewparams);

        this.addView(progressBar);
        this.addView(textView);
    }

    public void setText(CharSequence s) {
        textView.setText(s);
    }

    public void setTextColor(int color) {
        textView.setTextColor(color);
    }

    public void setTextSize(float size) {
        textView.setTextSize(size);
    }

    public void setNoDataState() {
        this.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);
        textView.setVisibility(View.VISIBLE);
        textView.setText(noDataText);
    }

    public void setNoMoreState() {
        this.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);
        textView.setVisibility(View.VISIBLE);
        textView.setText(noMoreText);
    }

    public void setLoadingState() {
        this.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.VISIBLE);
        textView.setVisibility(View.VISIBLE);
        textView.setText(loadingText);
    }

    public ProgressBar getProgressBar() {
        return progressBar;
    }

    public void setInVisibleState() {
        this.setVisibility(View.GONE);
    }

    public void setLoadingText(String loadingText) {
        this.loadingText = loadingText;
    }

    public void setNoMoreText(String noMoreText) {
        this.noMoreText = noMoreText;
    }

    public void setNoDataText(String noDataText) {
        this.noDataText = noDataText;
    }


    public int dip2px(float dpValue) {
        Resources r = getResources();
        float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dpValue, r.getDisplayMetrics());
        return (int) px;
    }
}

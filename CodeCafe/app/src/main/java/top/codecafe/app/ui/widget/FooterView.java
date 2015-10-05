package top.codecafe.app.ui.widget;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import top.codecafe.app.R;
import top.codecafe.kjframe.utils.DensityUtils;

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
        params.height = DensityUtils.dip2px(context, 23);
        this.setLayoutParams(params);

        progressBar.setIndeterminateDrawable(getResources().getDrawable(R.drawable
                .progressloading));
        progressBar.setIndeterminate(true);
        LayoutParams progressBarparams = new LayoutParams(LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT);
        progressBarparams.width = progressBarparams.height = DensityUtils.dip2px(context, 22);
        progressBar.setLayoutParams(progressBarparams);

        LayoutParams textViewparams = new LayoutParams(LayoutParams.WRAP_CONTENT,
                DensityUtils.dip2px(context, 20));
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

}

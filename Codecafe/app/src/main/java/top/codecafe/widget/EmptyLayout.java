package top.codecafe.widget;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;


import top.codecafe.R;


/**
 * 网络加载中、加载失败显示的控件
 *
 * @author kymjs (http://www.kymjs.com/) on 6/3/15.
 */
public class EmptyLayout extends LinearLayout implements
        View.OnClickListener {

    public static final int NETWORK_ERROR = 1; // 网络错误
    public static final int NETWORK_LOADING = 2; // 加载中
    public static final int NODATA = 3; // 没有数据
    public static final int HIDE_LAYOUT = 4; // 隐藏
    private int mErrorState = NETWORK_LOADING;

    private OnClickListener listener;
    private boolean clickEnable = true;
    private String strNoDataContent = "";

    private TextView tv;
    public ImageView img;
    private ProgressBar animProgress;

    public EmptyLayout(Context context) {
        super(context);
        init();
    }

    public EmptyLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        View view = View
                .inflate(getContext(), R.layout.view_empty_layout, null);
        img = (ImageView) view.findViewById(R.id.img_error_layout);
        tv = (TextView) view.findViewById(R.id.tv_error_layout);
        animProgress = (ProgressBar) view.findViewById(R.id.animProgress);

        setBackgroundColor(-1);
        setOnClickListener(this);
        if (getVisibility() == View.GONE) {
            setErrorType(HIDE_LAYOUT);
        } else {
            setErrorType(NETWORK_LOADING);
        }

        img.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (clickEnable && listener != null) {
                    listener.onClick(v);
                }
            }
        });
        this.addView(view);
    }

    @Override
    public void onClick(View v) {
        if (clickEnable && listener != null) {
            listener.onClick(v);
        }
    }

    public void dismiss() {
        mErrorState = HIDE_LAYOUT;
        setVisibility(View.GONE);
    }

    public int getErrorState() {
        return mErrorState;
    }

    public boolean isLoadError() {
        return mErrorState == NETWORK_ERROR;
    }

    public boolean isLoading() {
        return mErrorState == NETWORK_LOADING;
    }

    public void setErrorMessage(String msg) {
        tv.setText(msg);
    }

    public void setErrorImag(int imgResource) {
        img.setImageResource(imgResource);
    }

    public void setNoDataContent(String noDataContent) {
        strNoDataContent = noDataContent;
    }

    public void setOnLayoutClickListener(OnClickListener listener) {
        this.listener = listener;
    }

    public void setTvNoDataContent() {
        if (TextUtils.isEmpty(strNoDataContent)) {
            tv.setText(R.string.load_again);
        } else {
            tv.setText(strNoDataContent);
        }
    }

    public void setErrorType(int i) {
        setVisibility(View.VISIBLE);
        switch (i) {
            case NETWORK_ERROR:
                mErrorState = NETWORK_ERROR;
                tv.setText(R.string.load_again);
                img.setImageResource(R.mipmap.page_icon_network);
                img.setVisibility(View.VISIBLE);
                animProgress.setVisibility(View.GONE);
                setVisibility(View.VISIBLE);
                clickEnable = true;
                break;
            case NETWORK_LOADING:
                mErrorState = NETWORK_LOADING;
                animProgress.setVisibility(View.VISIBLE);
                img.setVisibility(View.GONE);
                tv.setText(R.string.loading);
                clickEnable = false;
                setVisibility(View.VISIBLE);
                break;
            case NODATA:
                mErrorState = NODATA;
                img.setImageResource(R.mipmap.page_icon_empty);
                img.setVisibility(View.VISIBLE);
                animProgress.setVisibility(View.GONE);
                setTvNoDataContent();
                clickEnable = true;
                setVisibility(View.VISIBLE);
                break;
            case HIDE_LAYOUT:
                dismiss();
                break;
            default:
                break;
        }
    }

    @Override
    public void setVisibility(int visibility) {
        if (visibility == View.GONE) {
            mErrorState = HIDE_LAYOUT;
        }
        super.setVisibility(visibility);
    }
}

package top.codecafe.delegate;

import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup.LayoutParams;

import com.kymjs.frame.view.AppDelegate;

import top.codecafe.R;

/**
 * ActionBar下拉显示图片的基类
 *
 * @author kymjs (http://www.kymjs.com/) on 12/4/15.
 */
public abstract class BaseDetailDelegate extends AppDelegate {
    @Override
    public int getRootLayoutId() {
        return R.layout.delegate_detail;
    }

    public abstract int getContentLayoutId();

    @Override
    public void initWidget() {
        super.initWidget();
        NestedScrollView nestedScrollView = get(R.id.nested_scrollview);
        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        nestedScrollView.addView(View.inflate(getActivity(), getContentLayoutId(), null), params);
        nestedScrollView.invalidate();
    }

    @Override
    public Toolbar getToolbar() {
        super.getToolbar();
        Toolbar toolbar = get(R.id.toolbar);
        CollapsingToolbarLayout collapsingToolbar = get(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle(rootView.getResources().getString(R.string.blog_detail));
        int titleColor = rootView.getResources().getColor(android.R.color.white);
        collapsingToolbar.setCollapsedTitleTextColor(titleColor);
        collapsingToolbar.setExpandedTitleColor(titleColor);
        return toolbar;
    }
}

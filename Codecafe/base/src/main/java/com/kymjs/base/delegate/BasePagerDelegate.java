package com.kymjs.base.delegate;

import android.content.Context;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.kymjs.base.R;
import com.kymjs.base.model.ViewPageInfo;
import com.kymjs.base.utils.ScrollTask;
import com.kymjs.frame.view.AppDelegate;

import java.util.ArrayList;
import java.util.List;

/**
 * 可以左右滑动的基类视图
 *
 * @author kymjs (http://www.kymjs.com/) on 11/27/15.
 */
public abstract class BasePagerDelegate extends AppDelegate {

    public BaseViewPageAdapter mViewPageAdapter;
    public float titleHeight;

    @Override
    public int getRootLayoutId() {
        return R.layout.base_pager_delegate;
    }

    @Override
    public void initWidget() {
        super.initWidget();
        TabLayout tabLayout = get(R.id.tabLayout);
        ViewPager viewPager = get(R.id.viewPager);

        AppCompatActivity activity = getActivity();

        //-6f,底部线条高度
        titleHeight = activity.getResources().getDimension(R.dimen.base_pager_title_height) - 6f;

        mViewPageAdapter = new BaseViewPageAdapter(activity);
        viewPager.setAdapter(mViewPageAdapter);

        mViewPageAdapter.add(getViewPageInfos());
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
    }

    protected abstract List<ViewPageInfo> getViewPageInfos();

    public class BaseViewPageAdapter extends FragmentPagerAdapter {

        private List<ViewPageInfo> mTabs;
        private Context mContext;

        public BaseViewPageAdapter(FragmentActivity activity) {
            super(activity.getSupportFragmentManager());
            this.mContext = activity;
            mTabs = new ArrayList<>();
        }

        public void add(ViewPageInfo viewPageInfo) {
            mTabs.add(viewPageInfo);
            notifyDataSetChanged();
        }

        public void add(List<ViewPageInfo> viewPageInfos) {
            this.mTabs.addAll(viewPageInfos);
            notifyDataSetChanged();
        }

        @Override
        public Fragment getItem(int position) {
            ViewPageInfo info = mTabs.get(position);
            return Fragment.instantiate(mContext, info.clss.getName(), info.args);
        }

        @Override
        public int getCount() {
            return mTabs.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mTabs.get(position).title;
        }
    }

    public void scrollShowTitle() {
        new ScrollTask(get(R.id.tabLayout), (int) titleHeight).execute(10);
    }

    public void scrollHideTitle() {
        new ScrollTask(get(R.id.tabLayout), (int) titleHeight).execute(-10);
    }
}

package com.kymjs.base;

import com.kymjs.frame.presenter.FragmentPresenter;
import com.kymjs.frame.view.IDelegate;

/**
 * 主界面内容Fragment
 *
 * @author kymjs (http://www.kymjs.com/) on 11/27/15.
 */
public abstract class MainFragment<T extends IDelegate> extends FragmentPresenter<T> {

    private boolean isInit = true;

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (getUserVisibleHint() && isInit) {
            isInit = false;
            lazyLoad();
        }
    }

    protected void lazyLoad() {
    }

    public void onChange() {
    }
}

package com.kymjs.base;

import com.kymjs.themvp.presenter.FragmentPresenter;
import com.kymjs.themvp.view.IDelegate;

/**
 * 主界面内容Fragment
 *
 * @author kymjs (https://kymjs.com/) on 11/27/15.
 */
public abstract class MainFragment<T extends IDelegate> extends FragmentPresenter<T> {

    public void onChange() {
    }
}

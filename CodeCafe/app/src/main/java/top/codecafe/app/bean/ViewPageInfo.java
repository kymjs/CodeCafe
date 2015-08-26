package top.codecafe.app.bean;

import android.os.Bundle;

public final class ViewPageInfo {

    public final Class<?> clss;
    public final Bundle args;
    public final String title;

    public ViewPageInfo(String _title, Class<?> _class, Bundle _args) {
    	title = _title;
        clss = _class;
        args = _args;
    }
}
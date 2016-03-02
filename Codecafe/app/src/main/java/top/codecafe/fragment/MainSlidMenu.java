package top.codecafe.fragment;

import android.os.Bundle;
import android.view.View;

import com.kymjs.core.bitmap.client.BitmapCore;
import com.kymjs.frame.presenter.FragmentPresenter;
import com.kymjs.model.Event;
import com.kymjs.rxvolley.rx.RxBus;

import top.codecafe.AppConfig;
import top.codecafe.R;
import top.codecafe.activity.MainActivity;
import top.codecafe.delegate.MainSlidMenuDelegate;

/**
 * 侧滑界面逻辑代码
 *
 * @author kymjs (http://www.kymjs.com/) on 11/27/15.
 */
public class MainSlidMenu extends FragmentPresenter<MainSlidMenuDelegate> implements View
        .OnClickListener {

    @Override
    protected Class<MainSlidMenuDelegate> getDelegateClass() {
        return MainSlidMenuDelegate.class;
    }

    @Override
    protected void bindEvenListener() {
        super.bindEvenListener();
        viewDelegate.setOnClickListener(this,
                R.id.menu_item_tag1,
                R.id.menu_item_tag2,
                R.id.menu_item_tag3,
                R.id.menu_item_tag4,
                R.id.menu_rootview);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        new BitmapCore.Builder()
                .view(viewDelegate.get(R.id.menu_header))
                .url(AppConfig.getAvatarURL())
                .errorResId(R.mipmap.def_avatar)
                .doTask();
    }

    @Override
    public void onClick(View v) {
        Event event = new Event();
        event.setAction(MainActivity.MENU_CLICK_EVEN);
        event.setObject(v);
        RxBus.getDefault().post(event);
    }
}

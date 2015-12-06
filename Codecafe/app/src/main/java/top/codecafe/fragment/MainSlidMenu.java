package top.codecafe.fragment;

import android.view.View;

import com.kymjs.frame.presenter.FragmentPresenter;

import de.greenrobot.event.EventBus;
import top.codecafe.R;
import top.codecafe.activity.MainActivity;
import top.codecafe.delegate.MainSlidMenuDelegate;
import top.codecafe.model.Event;

/**
 * 侧滑界面逻辑代码
 *
 * @author kymjs (http://www.kymjs.com/) on 11/27/15.
 */
public class MainSlidMenu extends FragmentPresenter<MainSlidMenuDelegate> implements View.OnClickListener {

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
                R.id.menu_item_tag4);
    }

    @Override
    public void onClick(View v) {
        Event event = new Event();
        event.setAction(MainActivity.MENU_CLICK_EVEN);
        event.setObject(v);
        EventBus.getDefault().post(event);
    }
}

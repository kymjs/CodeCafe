package top.codecafe.fragment;

import android.os.Bundle;
import android.view.View;

import androidx.fragment.app.FragmentActivity;

import com.kymjs.core.bitmap.client.BitmapCore;
import com.kymjs.model.Event;
import com.kymjs.rxvolley.rx.RxBus;
import com.kymjs.themvp.presenter.FragmentPresenter;

import top.codecafe.AppConfig;
import top.codecafe.R;
import top.codecafe.delegate.MainSlidMenuDelegate;

/**
 * 侧滑界面逻辑代码
 *
 * @author kymjs (https://kymjs.com/) on 11/27/15.
 */
public class MainSlidMenu extends FragmentPresenter<MainSlidMenuDelegate> {

    @Override
    protected Class<MainSlidMenuDelegate> getDelegateClass() {
        return MainSlidMenuDelegate.class;
    }

    @Override
    protected void bindEvenListener() {
        super.bindEvenListener();
        FragmentActivity activity = getActivity();
        if (activity instanceof View.OnClickListener) {
            viewDelegate.setOnClickListener((View.OnClickListener) activity,
                    R.id.menu_item_tag1,
                    R.id.menu_item_tag2,
                    R.id.menu_rootview);
        }
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
}

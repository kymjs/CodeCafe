package top.codecafe.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.View;

import com.kymjs.base.BaseFrameActivity;
import com.kymjs.base.MainFragment;

import de.greenrobot.event.EventBus;
import top.codecafe.R;
import top.codecafe.delegate.MainDelegate;
import top.codecafe.fragment.BlogPagerFragment;
import top.codecafe.fragment.MainSlidMenu;
import top.codecafe.model.Event;

/**
 * 主界面
 */
public class MainActivity extends BaseFrameActivity<MainDelegate> {

    public static final String MENU_CLICK_EVEN = "slid_menu_click_event";

    private MainFragment currentFragment; //当前内容所显示的Fragment
    private MainFragment content1;

    private boolean isOnKeyBacking;
    private Handler mMainLoopHandler = new Handler(Looper.getMainLooper());
    private Runnable onBackTimeRunnable = new Runnable() {
        @Override
        public void run() {
            isOnKeyBacking = false;
            viewDelegate.cancleExit();
        }
    };

    @Override
    protected Class<MainDelegate> getDelegateClass() {
        return MainDelegate.class;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (viewDelegate.menuIsOpen()) {
                viewDelegate.changeMenuState();
            } else if (isOnKeyBacking) {
                mMainLoopHandler.removeCallbacks(onBackTimeRunnable);
                isOnKeyBacking = false;
                finish();
            } else {
                isOnKeyBacking = true;
                viewDelegate.showExitTip();
                mMainLoopHandler.postDelayed(onBackTimeRunnable, 2000);
            }
            return true;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }

    /**
     * 用Fragment替换内容区
     *
     * @param targetFragment 用来替换的Fragment
     */
    public void changeFragment(MainFragment targetFragment) {
        if (targetFragment.equals(currentFragment)) {
            return;
        }
        FragmentTransaction transaction = getSupportFragmentManager()
                .beginTransaction();
        if (!targetFragment.isAdded()) {
            transaction.add(R.id.main_content, targetFragment, targetFragment.getClass()
                    .getName());
        }
        if (targetFragment.isHidden()) {
            transaction.show(targetFragment);
            targetFragment.onChange();
        }
        if (currentFragment != null && currentFragment.isVisible()) {
            transaction.hide(currentFragment);
        }
        currentFragment = targetFragment;
        transaction.commit();
    }

    /**
     * @see MainSlidMenu#onClick(View v)
     */
    public void onEvent(Event event) {
        if (!MENU_CLICK_EVEN.equals(event.getAction())) return;
        View view = event.getObject();
        switch (view.getId()) {
            case R.id.menu_item_tag1:
                changeFragment(content1);
                break;
            case R.id.menu_item_tag2:
                viewDelegate.toast("切换第二个界面");
                break;
            case R.id.menu_item_tag3:
                viewDelegate.toast("切换第三个界面");
                break;
            case R.id.menu_item_tag4:
                viewDelegate.toast("切换第四个界面");
                break;
            default:
                break;
        }
        viewDelegate.changeMenuState();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        content1 = new BlogPagerFragment();

        changeFragment(content1);
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}

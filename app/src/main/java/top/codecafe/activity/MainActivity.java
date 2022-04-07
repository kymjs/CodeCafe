package top.codecafe.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.KeyEvent;
import android.view.View;

import androidx.fragment.app.FragmentTransaction;

import com.kymjs.base.BaseFrameActivity;
import com.kymjs.base.MainFragment;

import top.codecafe.R;
import top.codecafe.delegate.MainDelegate;
import top.codecafe.fragment.BlogListFragment;
import top.codecafe.fragment.XituFragment;

/**
 * 主界面
 */
public class MainActivity extends BaseFrameActivity<MainDelegate> implements View.OnClickListener {

    private MainFragment currentFragment; //当前内容所显示的Fragment
    private final MainFragment content1 = new BlogListFragment();
    private final MainFragment content2 = new XituFragment();

    private boolean isOnKeyBacking;
    private final Handler mMainLoopHandler = new Handler(Looper.getMainLooper());
    private final Runnable onBackTimeRunnable = new Runnable() {
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
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.main_content, content1, content1.getClass().getName())
                .commit();
    }

    /**
     * 根据MainSlidMenu点击选择不同的响应
     */
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.menu_item_tag1:
                changeFragment(content1);
                break;
            case R.id.menu_item_tag2:
                changeFragment(content2);
                break;
            default:
                break;
        }
        viewDelegate.changeMenuState();
    }
}

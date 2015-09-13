package top.codecafe.kjframe;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import java.lang.ref.SoftReference;

import top.codecafe.kjframe.http.KJAsyncTask;
import top.codecafe.kjframe.ui.I_KJActivity;
import top.codecafe.kjframe.ui.SupportFragment;

/**
 * 应用基类
 *
 * @author kymjs (http://www.kymjs.com/) on 15/8/21.
 */
public abstract class KJActivity extends AppCompatActivity implements View.OnClickListener,
        I_KJActivity {

    public static int DESTROY = 0, STOP = 1, PAUSE = 2, RESUME = 3;
    public static final int WHICH_MSG = 0X37210;

    public int activityState;

    protected SupportFragment currentSupportFragment;
    private ThreadDataCallBack callback;
    private ThreadDataHandler threadHandle = new ThreadDataHandler(this);


    /**
     * 一个私有回调类，线程中初始化数据完成后的回调
     */
    private interface ThreadDataCallBack {
        void onSuccess();
    }

    // 当线程中初始化的数据初始化完成后，调用回调方法
    private class ThreadDataHandler extends Handler {
        private final SoftReference<KJActivity> mOuterInstance;

        public ThreadDataHandler(KJActivity outer) {
            mOuterInstance = new SoftReference<KJActivity>(outer);
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            if (msg.what == WHICH_MSG) {
                mOuterInstance.get().callback.onSuccess();
            }
        }
    }

    /**
     * 如果调用了initDataFromThread()，则当数据初始化完成后将回调该方法。
     */
    protected void threadDataInited() {
    }

    @Override
    public void initData() {
    }

    /**
     * 在线程中初始化数据，注意不能在这里执行UI操作
     */
    @Override
    public void initDataFromThread() {
        callback = new ThreadDataCallBack() {
            @Override
            public void onSuccess() {
                threadDataInited();
            }
        };
    }

    @Override
    public void initWidget() {
    }

    @Override
    public void widgetClick(View v) {
    }

    @Override
    public void onClick(@NonNull View v) {
        widgetClick(v);
    }

    protected <T extends View> T bindView(int id) {
        return (T) findViewById(id);
    }

    protected void bindViewClick(int... array) {
        if (array == null) return;
        for (int id : array) {
            findViewById(id).setOnClickListener(this);
        }
    }

    protected <T extends View> T bindView(int id, boolean click) {
        T view = (T) findViewById(id);
        if (click) {
            view.setOnClickListener(this);
        }
        return view;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRootView();
        KJAsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                initDataFromThread();
                threadHandle.sendEmptyMessage(WHICH_MSG);
            }
        });
        initData();
        initWidget();
    }

    @Override
    protected void onResume() {
        super.onResume();
        activityState = RESUME;
    }

    @Override
    protected void onPause() {
        super.onPause();
        activityState = PAUSE;
    }

    @Override
    protected void onStop() {
        super.onStop();
        activityState = STOP;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        activityState = DESTROY;
    }

    /**
     * 用Fragment替换视图
     *
     * @param resView        将要被替换掉的视图
     * @param targetFragment 用来替换的Fragment
     */
    public void changeFragment(int resView, SupportFragment targetFragment) {
        if (targetFragment.equals(currentSupportFragment)) {
            return;
        }
        android.support.v4.app.FragmentTransaction transaction = getSupportFragmentManager()
                .beginTransaction();
        if (!targetFragment.isAdded()) {
            transaction.add(resView, targetFragment, targetFragment.getClass()
                    .getName());
        }
        if (targetFragment.isHidden()) {
            transaction.show(targetFragment);
            targetFragment.onResume();
        }
        if (currentSupportFragment != null
                && currentSupportFragment.isVisible()) {
            transaction.hide(currentSupportFragment);
        }
        currentSupportFragment = targetFragment;
        transaction.commit();
    }
}

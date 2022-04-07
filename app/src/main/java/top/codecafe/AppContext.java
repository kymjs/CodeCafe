package top.codecafe;

import android.app.Application;

import com.kymjs.common.ContextTrojan;
import com.kymjs.common.Log;
import com.kymjs.crash.CustomActivityOnCrash;
import com.kymjs.okhttp3.OkHttpStack;
import com.kymjs.rxvolley.RxVolley;
import com.kymjs.rxvolley.http.RequestQueue;

import okhttp3.OkHttpClient;

/**
 * @author kymjs (https://kymjs.com/) on 11/17/15.
 */
public class AppContext extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        ContextTrojan.setContext(this);
        Log.setEnable(BuildConfig.DEBUG);
        CustomActivityOnCrash.install(this);
        RxVolley.setRequestQueue(RequestQueue.newRequestQueue(getCacheDir(),
                new OkHttpStack(new OkHttpClient())));
    }
}

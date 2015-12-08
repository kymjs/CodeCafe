package top.codecafe;

import android.app.Application;

import com.kymjs.crash.CustomActivityOnCrash;
import com.kymjs.kjcore.Core;

/**
 * @author kymjs (http://www.kymjs.com/) on 11/17/15.
 */
public class AppContext extends Application {
//    @Override
//    protected void attachBaseContext(Context base) {
//        super.attachBaseContext(base);
//        Nuwa.init(this);
//        Nuwa.loadPatch(this, Environment.getExternalStorageDirectory().getAbsolutePath().concat("/pat2ch.jar"));
//    }
//
    @Override
    public void onCreate() {
        super.onCreate();
        CustomActivityOnCrash.install(this);
        Core.initialize();
    }
}

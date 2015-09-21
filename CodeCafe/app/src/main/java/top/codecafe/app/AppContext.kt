package top.codecafe.app

import android.app.Activity
import android.app.Application
import android.content.Intent
import android.os.Bundle
import android.util.Log
import top.codecafe.app.ui.CrashDefaultActivity
import top.codecafe.app.utils.kjlog
import java.io.PrintWriter
import java.io.StringWriter
import java.lang.ref.WeakReference

/**
 * @author kymjs (http://www.kymjs.com/) on 15/8/21.
 */
public class AppContext : Application() {

    public val TAG: String = "CRASHABLE"

    private var lastActivityCreated = WeakReference<Activity>(null)

    override fun onCreate() {
        super.onCreate()
        Thread.setDefaultUncaughtExceptionHandler {(thread, throwable) ->
            Log.d(TAG, "App has crashed, executing UncaughtExceptionHandler", throwable)

            Thread() {
                val intent = Intent(this@AppContext, javaClass<CrashDefaultActivity>())
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

                val sw = StringWriter()
                val pw = PrintWriter(sw)
                throwable.printStackTrace(pw)
                intent.putExtra(AppConfig.EXTRA_EXCEPTION, sw.toString())

                startActivity(intent)
                val lastActivity = lastActivityCreated.get()
                if (lastActivity != null) {
                    lastActivity.finish()
                    lastActivityCreated.clear()
                }
                android.os.Process.killProcess(android.os.Process.myPid())
                System.exit(10)
            }.start()

        }

        registerActivityLifecycleCallbacks(object : Application.ActivityLifecycleCallbacks {
            override fun onActivityCreated(activity: Activity?, savedInstanceState: Bundle?) {
                if (activity !is CrashDefaultActivity && activity != null) {
                    lastActivityCreated = WeakReference(activity)
                }
            }

            override fun onActivityStarted(activity: Activity) {
            }

            override fun onActivityResumed(activity: Activity) {
            }

            override fun onActivityPaused(activity: Activity) {
            }

            override fun onActivityStopped(activity: Activity) {
            }

            override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
            }

            override fun onActivityDestroyed(activity: Activity) {
            }
        })
    }
}

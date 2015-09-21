package top.codecafe.app.ui

import top.codecafe.app.AppConfig
import top.codecafe.app.R
import top.codecafe.app.utils.skipActivity
import top.codecafe.kjframe.KJActivity

/**
 * 异常信息界面
 *
 * @author kymjs (http://www.kymjs.com/) on 9/21/15.
 */
public class CrashDefaultActivity : KJActivity() {

    override fun setRootView() {
        setContentView(R.layout.activity_crash)
    }

    override fun initWidget() {
        findViewById(R.id.upload_and_restart).setOnClickListener { uploadAndRestart() }
    }

    fun uploadAndRestart() {
        val crashInfo: String = getIntent().getStringExtra(AppConfig.EXTRA_EXCEPTION)
        skipActivity(javaClass<MainActivity>())
    }
}

package top.codecafe.app.ui

import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.support.v4.widget.DrawerLayout
import android.view.KeyEvent
import android.view.View
import android.view.animation.Animation
import android.widget.ImageView
import android.widget.TextView
import rx.functions.Action1
import top.codecafe.app.R
import top.codecafe.app.ui.fragment.Topic
import top.codecafe.app.ui.fragment.MainSlidMenu
import top.codecafe.app.ui.widget.menu.MaterialMenuDrawable
import top.codecafe.app.ui.widget.menu.MaterialMenuIcon
import top.codecafe.app.utils.KJAnimations
import top.codecafe.kjframe.KJActivity
import top.codecafe.kjframe.ui.SupportFragment

public class MainActivity : KJActivity() {

    private var drawerLayout: DrawerLayout? = null
    public var tvTitle: TextView? = null
    public var tvDoubleClickTip: TextView? = null
    private var imgBack: ImageView? = null

    private var materialMenuIcon: MaterialMenuIcon? = null

    private var content1: SupportFragment = Topic()
    private var content2: SupportFragment = Topic()
    private var content3: SupportFragment = Topic()
    private var content4: SupportFragment = Topic()
    private var content5: SupportFragment = Topic()
    private var menuFragment: MainSlidMenu? = null

    private var titleBarHeight: Float = 0f
    private var isOpen: Boolean = false;
    private var isOnKeyBacking: Boolean = false


    protected val mMainLoopHandler: Handler = Handler(Looper.getMainLooper())

    /**
     * 响应侧滑菜单MainSlidMenu中的item点击事件
     */
    public val changeContentSubscribers: Action1<Int> = Action1() { tag ->
        when (tag) {
            1 -> changeFragment(content1)
            2 -> changeFragment(content2)
            3 -> changeFragment(content3)
            4 -> changeFragment(content4)
            5 -> changeFragment(content5)
        }
    }

    override fun setRootView() {
        setContentView(R.layout.activity_main)
    }

    override fun initWidget() {
        drawerLayout = bindView(R.id.drawer_layout)
        tvTitle = bindView(R.id.titlebar_text_title, true)
        tvDoubleClickTip = bindView(R.id.titlebar_text_exittip)
        imgBack = bindView(R.id.titlebar_img_back, true)
        materialMenuIcon = MaterialMenuIcon(this, Color.WHITE, MaterialMenuDrawable.Stroke.THIN)

        drawerLayout?.setDrawerListener(DrawerLayoutStateListener());
        menuFragment = getSupportFragmentManager().findFragmentById(R.id.main_menu) as MainSlidMenu?;
        changeFragment(content1)
    }

    public fun menuIsOpen(): Boolean = isOpen

    override fun initData() {
        titleBarHeight = getResources().getDimension(R.dimen.titlebar_height)
    }

    override fun widgetClick(v: View?) {
        when (v?.getId()) {
            R.id.titlebar_img_back -> changeMenuState()
            R.id.titlebar_text_title -> changeMenuState()
        }
    }

    fun changeMenuState() {
        if (isOpen) {
            drawerLayout?.closeDrawers()
        } else {
            drawerLayout?.openDrawer(menuFragment?.fragmentRootView)
        }
    }

    fun changeFragment(targetFragment: SupportFragment) {
        drawerLayout?.closeDrawers()
        changeFragment(R.id.main_content, targetFragment)
        targetFragment.onResume();
    }

    inner class DrawerLayoutStateListener : DrawerLayout.SimpleDrawerListener() {
        override fun onDrawerSlide(drawerView: View?, slideOffset: Float) {
            materialMenuIcon?.setTransformationOffset(
                    MaterialMenuDrawable.AnimationState.BURGER_ARROW,
                    if (isOpen) {
                        2 - slideOffset
                    } else {
                        slideOffset
                    })
        }

        override fun onDrawerOpened(drawerView: View?) {
            isOpen = true
        }

        override fun onDrawerClosed(drawerView: View?) {
            isOpen = false
        }
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        materialMenuIcon?.syncState(savedInstanceState)
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        materialMenuIcon?.onSaveInstanceState(outState)
        super.onSaveInstanceState(outState)
    }

    /**
     * 取消退出
     */
    private fun cancleExit() {
        val anim = KJAnimations.getTranslateAnimation(0f, 0f, -titleBarHeight, 0f, 300)
        tvTitle?.startAnimation(anim)
        val anim2 = KJAnimations.getTranslateAnimation(0f, 0f, 0f, -titleBarHeight, 300)
        anim2.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation) {
            }

            override fun onAnimationRepeat(animation: Animation) {
            }

            override fun onAnimationEnd(animation: Animation) {
                tvDoubleClickTip?.setVisibility(View.GONE)
            }
        })
        tvDoubleClickTip?.startAnimation(anim2)
    }

    /**
     * 显示退出提示
     */
    private fun showExitTip() {
        tvDoubleClickTip?.setVisibility(View.VISIBLE)
        val anim = KJAnimations.getTranslateAnimation(0f, 0f, 0f, -titleBarHeight, 300)
        tvTitle?.startAnimation(anim)
        val anim2 = KJAnimations.getTranslateAnimation(0f, 0f, -titleBarHeight, 0f, 300)
        tvDoubleClickTip?.startAnimation(anim2)
    }

    private val onBackTimeRunnable = object : Runnable {
        override fun run() {
            isOnKeyBacking = false
            cancleExit()
        }
    }

    override fun onKeyDown(keyCode: Int, event: android.view.KeyEvent): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (isOnKeyBacking) {
                mMainLoopHandler.removeCallbacks(onBackTimeRunnable)
                isOnKeyBacking = false
                System.exit(0)
            } else {
                isOnKeyBacking = true
                showExitTip()
                mMainLoopHandler.postDelayed(onBackTimeRunnable, 2000)
            }
            return true
        } else {
            return super.onKeyDown(keyCode, event)
        }
    }
}
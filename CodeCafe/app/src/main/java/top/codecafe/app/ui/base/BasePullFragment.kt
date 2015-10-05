package top.codecafe.app.ui.base;

import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.widget.RelativeLayout.LayoutParams
import top.codecafe.app.R
import top.codecafe.app.ui.MainActivity
import top.codecafe.app.ui.fragment.TopicPagerFragment
import top.codecafe.app.ui.widget.EmptyLayout
import top.codecafe.app.ui.widget.PagerSlidingTabStrip
import top.codecafe.app.utils.DefaultItemDivider
import top.codecafe.kjframe.KJActivity
import top.codecafe.app.utils.KJAnimations
import top.codecafe.app.utils.kjlog
import top.codecafe.app.utils.screenHeight

/**
 * 包含下拉界面的基类
 * @author kymjs (http://www.kymjs.com/) on 8/26/15.
 */
public abstract class BasePullFragment : BaseMainFragment(), SwipeRefreshLayout.OnRefreshListener,
        BasePullUpRecyclerAdapter.OnPullUpListener {

    public var recyclerView: RecyclerView? = null
    public var refreshLayout: SwipeRefreshLayout? = null
    public var emptyLayout: EmptyLayout? = null

    override fun inflaterView(layoutInflater: LayoutInflater, viewGroup: ViewGroup, bundle: Bundle?): View? {
        val rootView: View = layoutInflater.inflate(R.layout.base_frag_pull, null)
        return rootView
    }

    override fun initWidget(parentView: View?) {
        super<BaseMainFragment>.initWidget(parentView)
        recyclerView = bindView(R.id.recyclerView)
        refreshLayout = bindView(R.id.swiperefreshlayout)
        emptyLayout = bindView(R.id.emptylayout)

        refreshLayout?.setOnRefreshListener(this)
        refreshLayout?.setColorSchemeResources(R.color.swiperefresh_color1,
                R.color.swiperefresh_color2,
                R.color.swiperefresh_color3,
                R.color.swiperefresh_color4)

        recyclerView?.setItemAnimator(DefaultItemAnimator())
        recyclerView?.addItemDecoration(DefaultItemDivider());

        val scrollListener: TabStripListener = TabStripListener()
        if (parentFragment is BasePagerFragment) {
            scrollListener.initListener(parentFragment as BasePagerFragment);
        }
        recyclerView?.addOnScrollListener(scrollListener);

        requestData()
    }

    /** 设置顶部正在加载的状态  */
    open fun setSwipeRefreshLoadingState() {
        refreshLayout?.setRefreshing(true)
        // 防止多次重复刷新
        refreshLayout?.setEnabled(false)
    }

    /** 设置顶部加载完毕的状态  */
    open fun setSwipeRefreshLoadedState() {
        refreshLayout?.setRefreshing(false)
        refreshLayout?.setEnabled(true)
        emptyLayout?.dismiss()
    }

    open fun requestData() {
    }

    override fun onBottom(state: Int) {

    }

    abstract fun getRecyclerAdapter(): BaseRecyclerAdapter<*>;

    open class TabStripListener : RecyclerView.OnScrollListener() {
        public var pageFragment: BasePagerFragment? = null
        var top: Int = 0;

        private var isAniming = false //支付按钮的显示或隐藏动画是否正在执行中
        private var isShowing = true //支付按钮是否正在显示
        private var prevScrollY = 0 //上次recyclerView滚动时的Y值
        private var offsetY: Float = 0f //floatButton按钮显示在屏幕的Y轴坐标
        private var scrollY = 0

        private var downAnimation: Animation? = null
        private var upAnimation: Animation? = null

        open fun initListener(pageFragment: BasePagerFragment) {
            this.pageFragment = pageFragment

            offsetY = pageFragment.activity.screenHeight() * 0.3f
            downAnimation = KJAnimations.getTranslateAnimation(0f, 0f, 0f, offsetY, 300)
            upAnimation = KJAnimations.getTranslateAnimation(0f, 0f, offsetY, 0f, 300)
            downAnimation!!.setAnimationListener(getAnimationListener())
            upAnimation!!.setAnimationListener(getAnimationListener())
        }

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            //顶部TagStrip的滚动控制
            top += dy
            val tabStrip: PagerSlidingTabStrip? = pageFragment?.getTabStrip()
            val tabStripHeight: Int = tabStrip?.measuredHeight as Int
            if (top > tabStripHeight) {
                top = tabStripHeight
            }
            if (top < 0) {
                top = 0;
            }
            val params: LayoutParams? = tabStrip?.layoutParams as LayoutParams
            params?.topMargin = -top;
            tabStrip?.layoutParams = params

            //底部floatButton的滚动控制
            scrollY += dy
            var floatButton: View? = null
            if (pageFragment is TopicPagerFragment) {
                floatButton = (pageFragment as TopicPagerFragment).getFloatingButton()
            }
            if (prevScrollY > scrollY) {
                showBuyButton(floatButton)
            } else {
                hideBuyButton(floatButton)
            }
            prevScrollY = scrollY
        }

        /**
         * 显示floatButton
         * @param v floatButton
         */
        private fun showBuyButton(v: View?) {
            if (!isAniming && !isShowing) {
                isShowing = true
                v?.startAnimation(upAnimation)
            }
        }

        /**
         * 隐藏floatButton
         * @param v floatButton
         */
        private fun hideBuyButton(v: View?) {
            if (!isAniming && isShowing) {
                isShowing = false
                v?.startAnimation(downAnimation)
            }
        }

        /**
         * 底部floatButton动画监听器，用于修改isAniming值

         * @return
         */
        private fun getAnimationListener(): Animation.AnimationListener {
            return object : Animation.AnimationListener {
                override fun onAnimationStart(animation: Animation) {
                    isAniming = true
                }

                override fun onAnimationEnd(animation: Animation) {
                    isAniming = false
                }

                override fun onAnimationRepeat(animation: Animation) {
                }
            }
        }

        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
        }
    }
}

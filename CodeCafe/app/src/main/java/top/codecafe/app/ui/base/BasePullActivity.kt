package top.codecafe.app.ui.base

import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.RecyclerView
import top.codecafe.app.R
import top.codecafe.app.ui.widget.swipeback.SwipeBackActivity
import top.codecafe.app.utils.DefaultItemDivider
import top.codecafe.kjframe.KJActivity

/**
 * 能下拉刷新的Recycler
 * @author kymjs (http://www.kymjs.com/) on 8/27/15.
 */
public abstract class BasePullActivity : SwipeBackActivity(), SwipeRefreshLayout.OnRefreshListener {

    public var recyclerView: RecyclerView? = null

    public var refreshLayout: SwipeRefreshLayout? = null

    override fun setRootView() {
        setContentView(R.layout.base_frag_pull)
    }

    override fun initWidget() {
        super<SwipeBackActivity>.initWidget()
        recyclerView = bindView(R.id.recyclerView)
        refreshLayout = bindView(R.id.swiperefreshlayout)

        recyclerView?.setItemAnimator(DefaultItemAnimator())
        recyclerView?.addItemDecoration(DefaultItemDivider());
        
        refreshLayout?.setOnRefreshListener(this)
        refreshLayout?.setColorSchemeResources(R.color.swiperefresh_color1,
                R.color.swiperefresh_color2,
                R.color.swiperefresh_color3,
                R.color.swiperefresh_color4)

        requestData()
    }

    open fun requestData() {
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
    }

    public abstract fun getRecyclerAdapter(): BaseRecyclerAdapter<*>;
}

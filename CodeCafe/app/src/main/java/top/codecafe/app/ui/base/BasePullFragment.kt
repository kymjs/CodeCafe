package top.codecafe.app.ui.base;

import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import top.codecafe.app.R
import top.codecafe.app.ui.widget.EmptyLayout
import top.codecafe.app.utils.DefaultItemDivider

/**
 * 包含下拉界面的基类
 * @author kymjs (http://www.kymjs.com/) on 8/26/15.
 */
public abstract class BasePullFragment : BaseMainFragment(), SwipeRefreshLayout.OnRefreshListener {

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

    abstract fun getRecyclerAdapter(): BaseRecyclerAdapter<*>;
}

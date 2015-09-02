package top.codecafe.app.ui.base

import android.os.Bundle
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.RecyclerView
import top.codecafe.app.R
import top.codecafe.app.ui.widget.CircleRefreshLayout
import top.codecafe.app.utils.DefaultItemDivider
import top.codecafe.kjframe.KJActivity

/**
 * 能下拉刷新的Recycler
 * @author kymjs (http://www.kymjs.com/) on 8/27/15.
 */
public abstract class BasePullActivity : KJActivity(), CircleRefreshLayout.OnCircleRefreshListener {

    public var recyclerView: RecyclerView? = null

    public var refreshLayout: CircleRefreshLayout? = null

    override fun setRootView() {
        setContentView(R.layout.base_frag_pull)
    }

    override fun initWidget() {
        super<KJActivity>.initWidget()
        recyclerView = bindView(R.id.recyclerView)
        refreshLayout = bindView(R.id.swiperefreshlayout)

        recyclerView?.setItemAnimator(DefaultItemAnimator())
        recyclerView?.addItemDecoration(DefaultItemDivider());
        refreshLayout?.setOnRefreshListener(this)
    
        requestData()
    }

    open fun requestData() {
    }


    public abstract fun getRecyclerAdapter(): BaseRecyclerAdapter<*>;
}

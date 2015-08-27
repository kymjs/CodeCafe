package top.codecafe.app.ui.base;

import android.os.Bundle
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.RecyclerView.Adapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import top.codecafe.app.R
import top.codecafe.app.ui.widget.CircleRefreshLayout
import top.codecafe.app.utils.DefaultItemDivider

/**
 * 包含下拉界面的基类
 * @author kymjs (http://www.kymjs.com/) on 8/26/15.
 */
public abstract class BasePullFragment : BaseMainFragment(), CircleRefreshLayout.OnCircleRefreshListener {

    public var recyclerView: RecyclerView? = null

    public var refreshLayout: CircleRefreshLayout? = null

    override fun inflaterView(layoutInflater: LayoutInflater, viewGroup: ViewGroup, bundle: Bundle?): View? {
        val rootView: View = layoutInflater.inflate(R.layout.base_frag_pull, null)
        return rootView
    }

    override fun initWidget(parentView: View?) {
        super<BaseMainFragment>.initWidget(parentView)
        recyclerView = bindView(R.id.recyclerView)
        refreshLayout = bindView(R.id.swiperefreshlayout)

        refreshLayout?.setOnRefreshListener(this)

        recyclerView?.setItemAnimator(DefaultItemAnimator())
        recyclerView?.addItemDecoration(DefaultItemDivider());
        recyclerView?.setAdapter(getRecyclerAdapter())

        requestData()
    }

    fun requestData() {
    }

    abstract fun getRecyclerAdapter(): Adapter<*>;
}

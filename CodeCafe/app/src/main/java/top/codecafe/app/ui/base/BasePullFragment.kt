package top.codecafe.app.ui.base;

import android.graphics.Rect
import android.os.Bundle
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.RecyclerView.Adapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import top.codecafe.app.R
import top.codecafe.app.ui.widget.CircleRefreshLayout

/**
 * 包含下拉界面的基类
 * @author kymjs (http://www.kymjs.com/) on 8/26/15.
 */
public abstract class BasePullFragment : BaseMainFragment(), CircleRefreshLayout.OnCircleRefreshListener {

    var recyclerView: RecyclerView? = null

    var refreshLayout: CircleRefreshLayout? = null

    override fun inflaterView(layoutInflater: LayoutInflater, viewGroup: ViewGroup, bundle: Bundle?): View? {
        val rootView: View = layoutInflater.inflate(R.layout.base_frag_pull, null)
        return rootView
    }

    override fun initWidget(parentView: View?) {
        super<BaseMainFragment>.initWidget(parentView)
        recyclerView = bindView(R.id.recyclerView)
        refreshLayout = bindView(R.id.swiperefreshlayout)

        refreshLayout?.setOnRefreshListener(this)

        recyclerView?.setLayoutManager(LinearLayoutManager(outsideAty))
        recyclerView?.setItemAnimator(DefaultItemAnimator())
        recyclerView?.addItemDecoration(DividerItemDecoration());
        recyclerView?.setAdapter(getRecyclerAdapter())

        setContentData()
    }

    fun setContentData() {
    }
    
    abstract fun getRecyclerAdapter(): Adapter<*>;

    /**
     * set RecyclerView divider height 20px
     */
    inner class DividerItemDecoration : RecyclerView.ItemDecoration() {
        override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
            super.getItemOffsets(outRect, view, parent, state);
            outRect.set(0, 0, 0, 20);//每个item的底部偏移
        }
    }

}

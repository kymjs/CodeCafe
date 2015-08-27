package top.codecafe.app.ui.fragment

import android.os.Bundle
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.StaggeredGridLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import top.codecafe.app.R

import top.codecafe.app.ui.base.BaseMainFragment
import top.codecafe.app.utils.DefaultItemDivider

/**
 * ui控件库
 * @author kymjs (http://www.kymjs.com/) on 8/27/15.
 */
public class WidgetCategoryFragment : BaseMainFragment() {
    private var recyclerView: RecyclerView? = null

    override fun inflaterView(layoutInflater: LayoutInflater, viewGroup: ViewGroup, bundle: Bundle?): View? {
        val rootView: View = layoutInflater.inflate(R.layout.frag_main_mood, null)
        return rootView
    }

    override fun initWidget(parentView: View?) {
        super.initWidget(parentView)
        recyclerView = bindView(R.id.recyclerView)
        recyclerView?.setItemAnimator(DefaultItemAnimator())
        recyclerView?.addItemDecoration(DefaultItemDivider())
        recyclerView?.setLayoutManager(StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL))
    }

    override fun onResume() {
        super.onResume()
        setTitle("控件库")
    }
}

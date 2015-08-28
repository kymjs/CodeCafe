package top.codecafe.app.ui.fragment

import android.os.Bundle
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.StaggeredGridLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import top.codecafe.app.R
import top.codecafe.app.adapter.WidgetCategoryAdapter
import top.codecafe.app.bean.WidgetCategory
import top.codecafe.app.ui.WidgetListActivity
import top.codecafe.app.ui.base.BaseMainFragment
import top.codecafe.app.utils.DefaultItemDivider
import top.codecafe.app.utils.showActivity
import top.codecafe.app.utils.toast
import java.util.ArrayList

/**
 * ui控件库
 * @author kymjs (http://www.kymjs.com/) on 8/27/15.
 */
public class WidgetCategoryFragment : BaseMainFragment() {
    private var recyclerView: RecyclerView? = null
    private val datas: ArrayList<WidgetCategory> = ArrayList()

    override fun inflaterView(layoutInflater: LayoutInflater, viewGroup: ViewGroup, bundle: Bundle?): View? {
        val rootView: View = layoutInflater.inflate(R.layout.frag_main_widget_category, null)
        return rootView
    }

    override fun initData() {
        for (i in 0..50) {
            var data: WidgetCategory = WidgetCategory()
            data.name = "hello$i"
            data.type = "type类型$i"
            data.size = i % 4
            datas.add(data)
        }
    }

    override fun initWidget(parentView: View?) {
        super.initWidget(parentView)
        recyclerView = bindView(R.id.recyclerView)
        recyclerView?.setItemAnimator(DefaultItemAnimator())
        recyclerView?.addItemDecoration(DefaultItemDivider())
        recyclerView?.setLayoutManager(StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL))
        val adapter: WidgetCategoryAdapter = WidgetCategoryAdapter(recyclerView, datas)
        adapter.setOnItemClickListener { view, any, i -> showActivity(javaClass<WidgetListActivity>()) }
        recyclerView?.setAdapter(adapter)
    }

    override fun onResume() {
        super.onResume()
        setTitle("控件库")
    }
}

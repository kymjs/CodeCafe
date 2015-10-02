package top.codecafe.app.ui.base

import android.os.Bundle
import android.support.v4.view.ViewPager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import top.codecafe.app.R
import top.codecafe.app.adapter.ViewPageFragmentAdapter
import top.codecafe.app.ui.widget.PagerSlidingTabStrip
import java.util.ArrayList

/**
 * 左右滑动Fragment的基类
 *
 * @author kymjs (http://www.kymjs.com/) on 8/26/15.
 */
public abstract class BasePagerFragment : BaseMainFragment() {
    private var tabStrip: PagerSlidingTabStrip? = null
    private var mPager: ViewPager? = null

    private var adapter: ViewPageFragmentAdapter? = null

    private val pagerDataArray: ArrayList<PagerData> = ArrayList()

    override fun inflaterView(layoutInflater: LayoutInflater, viewGroup: ViewGroup, bundle: Bundle?): View? {
        val rootView: View = layoutInflater.inflate(R.layout.base_frag_pager, null)
        return rootView
    }

    public fun getTabStrip(): PagerSlidingTabStrip? = tabStrip

    public fun getPagerView(): ViewPager? = mPager

    public fun getAdapter(): ViewPageFragmentAdapter? = adapter

    abstract fun initContentData(pagerDataArray: ArrayList<PagerData>)

    override fun initWidget(parentView: View?) {
        super.initWidget(parentView)
        tabStrip = bindView(R.id.base_page_fragment_group)
        mPager = bindView(R.id.base_page_fragment_pager)
        adapter = ViewPageFragmentAdapter(getChildFragmentManager(), tabStrip, mPager)

        initContentData(pagerDataArray)

        for (item in pagerDataArray) {
            adapter?.addTab(item.title, item.clazz, item.bundle)
        }

        mPager?.setOffscreenPageLimit(pagerDataArray.size())
    }

    open class PagerData {
        var title: String = ""
        var clazz: Class<*>? = null
        var bundle: Bundle? = null

        constructor(title: String, clazz: Class<*>, bundle: Bundle = Bundle()) {
            this.title = title
            this.clazz = clazz
            this.bundle = bundle
        }
    }
}

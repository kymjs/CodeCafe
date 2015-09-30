package top.codecafe.app.ui.fragment

import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v4.view.ViewPager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import top.codecafe.app.R
import top.codecafe.app.ui.base.BasePagerFragment
import top.codecafe.app.ui.base.BasePagerFragment.PagerData
import top.codecafe.app.ui.base.OnFloatButtonClickListener
import top.codecafe.app.ui.widget.PagerSlidingTabStrip
import top.codecafe.kjframe.ui.SupportFragment
import java.util.ArrayList

/**
 * 话题圈
 * @author kymjs (http://www.kymjs.com/) on 8/13/15.
 */
public class TopicPagerFragment : BasePagerFragment() {
    var floatButton: FloatingActionButton? = null

    override fun inflaterView(layoutInflater: LayoutInflater, viewGroup: ViewGroup, bundle: Bundle?): View? {
        val rootView: View = layoutInflater.inflate(R.layout.base_frag_floatbtn, null)
        return rootView
    }

    override fun initWidget(parentView: View?) {
        super.initWidget(parentView)
        floatButton = bindView(R.id.base_page_float_button)
        floatButton?.setOnClickListener { v ->
            val currentFrag: Fragment? = getAdapter()?.getItem(getPagerView()?.currentItem!!)
            if (currentFrag is OnFloatButtonClickListener) {
                currentFrag.onFloatButtonClick(floatButton)
            }
        }
    }

    override fun initContentData(pagerDataArray: ArrayList<PagerData>) {
        pagerDataArray.add(PagerData("今日话题", TopicList::class.java))
        pagerDataArray.add(PagerData("心情广场", MoodPlaza::class.java))
    }

    override fun onResume() {
        super.onResume()
        setTitle("话题圈子")
    }
}

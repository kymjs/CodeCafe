package top.codecafe.app.ui.fragment

import android.os.Bundle
import android.support.v4.view.ViewPager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import top.codecafe.app.ui.fragment.BaseMainFragment
import top.codecafe.app.ui.fragment.MoodPlaza
import top.codecafe.app.ui.fragment.TopicList
import top.codecafe.app.R
import top.codecafe.app.adapter.ViewPageFragmentAdapter
import top.codecafe.app.ui.widget.PagerSlidingTabStrip

/**
 * 话题圈

 * @author kymjs (http://www.kymjs.com/) on 8/13/15.
 */
public class Topic : BaseMainFragment() {

    private var mGroup: PagerSlidingTabStrip? = null
    private var mPager: ViewPager? = null

    private var adapter: ViewPageFragmentAdapter? = null

    override fun inflaterView(layoutInflater: LayoutInflater, viewGroup: ViewGroup, bundle: Bundle?): View? {
        val rootView: View = layoutInflater.inflate(R.layout.frag_main_topic, null)
        return rootView
    }

    override fun onResume() {
        super.onResume()
        setTitle("话题圈子")
    }

    override fun initWidget(parentView: View?) {
        super.initWidget(parentView)
        mGroup = bindView(R.id.myworks_group)
        mPager = bindView(R.id.myworks_pager)
        
        mPager?.setOffscreenPageLimit(2)
        adapter = ViewPageFragmentAdapter(getChildFragmentManager(), mGroup, mPager)
        mPager?.setAdapter(adapter)
        mGroup?.setViewPager(mPager)

        val bundle1 = Bundle()
        bundle1.putInt("tag", 1)
        adapter?.addTab("今日话题", "tag1", javaClass<TopicList>(), bundle1)

        val bundle2 = Bundle()
        bundle2.putInt("tag", 2)
        adapter?.addTab("心情广场", "tag2", javaClass<MoodPlaza>(), bundle2)
    }
}

package top.codecafe.app.ui.fragment

import android.os.Bundle
import android.view.View
import top.codecafe.app.ui.base.BasePagerFragment
import top.codecafe.app.ui.base.BasePagerFragment.PagerData
import java.util.ArrayList

/**
 * 话题圈
 * @author kymjs (http://www.kymjs.com/) on 8/13/15.
 */
public class TopicPagerFragment : BasePagerFragment() {
    override fun initContentData(pagerDataArray: ArrayList<PagerData>) {
        pagerDataArray.add(PagerData("今日话题", TopicList::class.java))
        pagerDataArray.add(PagerData("心情广场", MoodPlaza::class.java))
    }

    override fun onResume() {
        super.onResume()
        setTitle("话题圈子")
    }
}

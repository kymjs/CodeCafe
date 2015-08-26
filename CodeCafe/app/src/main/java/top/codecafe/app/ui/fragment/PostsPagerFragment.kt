package top.codecafe.app.ui.fragment

import top.codecafe.app.ui.base.BasePagerFragment
import java.util.*

/**
 * 技术热点
 * @author kymjs (http://www.kymjs.com/) on 8/26/15.
 */
public class PostsPagerFragment : BasePagerFragment() {
    override fun initContentData(pagerDataArray: ArrayList<BasePagerFragment.PagerData>) {
        pagerDataArray.add(BasePagerFragment.PagerData("今日话题", javaClass<TopicList>()))
        pagerDataArray.add(BasePagerFragment.PagerData("心情广场", javaClass<MoodPlaza>()))
    }

    override fun onResume() {
        super.onResume()
        setTitle("技术热点")
    }
}
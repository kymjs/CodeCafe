package top.codecafe.app.ui.fragment

import top.codecafe.app.ui.base.BasePagerFragment
import top.codecafe.app.ui.base.BasePagerFragment.PagerData
import java.util.ArrayList

/**
 * 技术资讯
 * @author kymjs (http://www.kymjs.com/) on 8/26/15.
 */
public class PostsPagerFragment : BasePagerFragment() {
    override fun initContentData(pagerDataArray: ArrayList<PagerData>) {
        pagerDataArray.add(PagerData("技术博客", BlogFragment::class.java))
        pagerDataArray.add(PagerData("新闻资讯", MoodPlaza::class.java))
    }

    override fun onResume() {
        super.onResume()
        setTitle("技术热点")
    }
}
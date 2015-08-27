package top.codecafe.app.ui.fragment

import top.codecafe.app.ui.base.BasePagerFragment
import top.codecafe.app.ui.base.BasePagerFragment.PagerData
import java.util.ArrayList

/**
 * 技术热点
 * @author kymjs (http://www.kymjs.com/) on 8/26/15.
 */
public class PostsPagerFragment : BasePagerFragment() {
    override fun initContentData(pagerDataArray: ArrayList<PagerData>) {
        pagerDataArray.add(PagerData("技术博客", javaClass<BlogFragment>()))
        pagerDataArray.add(PagerData("新闻资讯", javaClass<MoodPlaza>()))
    }

    override fun onResume() {
        super.onResume()
        setTitle("技术热点")
    }
}
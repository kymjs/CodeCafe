package top.codecafe.delegate;

import com.kymjs.base.delegate.BasePagerDelegate;
import com.kymjs.base.model.ViewPageInfo;

import java.util.ArrayList;
import java.util.List;

import top.codecafe.R;
import top.codecafe.fragment.XituFragment;

/**
 * 业界资讯栏目的ViewPager视图层
 *
 * @author kymjs (http://www.kymjs.com/) on 11/27/15.
 */
public class NewsPagerDelegate extends BasePagerDelegate {
    @Override
    protected List<ViewPageInfo> getViewPageInfos() {
        List<ViewPageInfo> viewPageInfos = new ArrayList<>();

        viewPageInfos.add(new ViewPageInfo(getActivity().getString(R.string.news_title1),
                "post1", XituFragment.class, null));
        viewPageInfos.add(new ViewPageInfo(getActivity().getString(R.string.news_title2),
                "post2", XituFragment.class, null));
        return viewPageInfos;
    }
}

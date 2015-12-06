package top.codecafe.delegate;

import com.kymjs.base.delegate.BasePagerDelegate;
import com.kymjs.base.model.ViewPageInfo;

import java.util.ArrayList;
import java.util.List;

import top.codecafe.R;
import top.codecafe.fragment.BlogFragment;
import top.codecafe.fragment.XituFragment;

/**
 * 帖子栏目的ViewPager视图
 *
 * @author kymjs (http://www.kymjs.com/) on 11/27/15.
 */
public class PostPagerDelegate extends BasePagerDelegate {
    @Override
    protected List<ViewPageInfo> getViewPageInfos() {
        List<ViewPageInfo> viewPageInfos = new ArrayList<>();

        viewPageInfos.add(new ViewPageInfo(getActivity().getString(R.string.title1),
                "blog1", BlogFragment.class, null));
        viewPageInfos.add(new ViewPageInfo(getActivity().getString(R.string.title2),
                "blog2", XituFragment.class, null));
        return viewPageInfos;
    }
}

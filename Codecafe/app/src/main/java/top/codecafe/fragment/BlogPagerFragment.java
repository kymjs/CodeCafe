package top.codecafe.fragment;

import com.kymjs.base.MainFragment;

import de.greenrobot.event.EventBus;
import top.codecafe.delegate.BlogPagerDelegate;
import top.codecafe.model.Event;

/**
 * 首页技术帖子ViewPager界面
 *
 * @author kymjs (http://www.kymjs.com/) on 11/27/15.
 */
public class BlogPagerFragment extends MainFragment<BlogPagerDelegate> {

    private boolean titleIsShow = true;

    public static final String CHANGE_PAGER_BLOG_TITLE_EVEN = "blog_should_change_pager_title_event";

    @Override
    protected Class<BlogPagerDelegate> getDelegateClass() {
        return BlogPagerDelegate.class;
    }

    @Override
    protected void bindEvenListener() {
        super.bindEvenListener();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    public void onEvent(Event event) {
        if (!CHANGE_PAGER_BLOG_TITLE_EVEN.equals(event.getAction())) return;
        if (event.arg > 0 && titleIsShow) {
            viewDelegate.scrollHideTitle();
            titleIsShow = false;
        } else if (event.arg < 0 && !titleIsShow) {
            viewDelegate.scrollShowTitle();
            titleIsShow = true;
        }
    }
}

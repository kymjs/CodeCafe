package top.codecafe.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.kymjs.api.Api;
import com.kymjs.frame.adapter.BasePullUpRecyclerAdapter;
import com.kymjs.frame.adapter.RecyclerHolder;
import com.kymjs.kjcore.Core;
import com.kymjs.kjcore.http.KJHttp;
import com.kymjs.kjcore.http.Request;
import com.kymjs.model.XituBlog;
import com.kymjs.model.XituBlogList;

import java.util.ArrayList;

import top.codecafe.R;
import top.codecafe.activity.XituDetailActivity;
import top.codecafe.utils.XmlUtils;

/**
 * 稀土的数据rss
 *
 * @author kymjs (http://www.kymjs.com/) on 12/3/15.
 */
public class XituFragment extends MainListFragment<XituBlog> {

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        byte[] cache = KJHttp.getCache(Api.XITU_BLOG_LIST);
        if (cache != null) {
            datas = parserInAsync(cache);
            adapter.refresh(datas);
            viewDelegate.mEmptyLayout.dismiss();
        }
    }

    @Override
    protected ArrayList<XituBlog> parserInAsync(byte[] t) {
        ArrayList<XituBlog> datas = XmlUtils.toBean(XituBlogList.class, t).getChannel()
                .getItemArray();
        for (XituBlog data : datas) {
            data.format();
        }
        return datas;
    }

    @Override
    protected BasePullUpRecyclerAdapter<XituBlog> getAdapter() {
        return new BasePullUpRecyclerAdapter<XituBlog>(recyclerView, datas, R.layout.item_blog) {
            @Override
            public void convert(RecyclerHolder holder, final XituBlog item, int position, boolean
                    isScrolling) {
                holder.setText(R.id.item_blog_tv_title, item.getTitle());
                holder.setText(R.id.item_blog_tv_description, item.getDescription());
                holder.setText(R.id.item_blog_tv_date, item.getPubDate());
                holder.setText(R.id.item_blog_tv_author, item.getAuthor());
            }
        };
    }

    @Override
    public void doRequest() {
        new Core.Builder().url(Api.XITU_BLOG_LIST)
                .contentType(Request.HttpMethod.GET)
                .cacheTime(10)
                .callback(callBack)
                .doTask();
    }

    @Override
    public void onItemClick(View view, Object data, int position) {
        XituDetailActivity.goinActivity(getActivity(), (XituBlog) data);
    }
}

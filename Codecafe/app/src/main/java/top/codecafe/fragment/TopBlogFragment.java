package top.codecafe.fragment;

import android.view.View;
import android.widget.ImageView;

import com.kymjs.api.Api;
import com.kymjs.frame.adapter.BasePullUpRecyclerAdapter;
import com.kymjs.frame.adapter.RecyclerHolder;
import com.kymjs.kjcore.Core;
import com.kymjs.kjcore.http.HttpParams;
import com.kymjs.kjcore.http.Request;
import com.kymjs.model.osc.OSCBlog;
import com.kymjs.model.osc.OSCBlogList;

import java.util.ArrayList;

import top.codecafe.R;
import top.codecafe.activity.OSCBlogDetailActivity;
import top.codecafe.utils.XmlUtils;

/**
 * OSC博客列表
 *
 * @author kymjs (http://www.kymjs.com/) on 12/10/15.
 */
public class TopBlogFragment extends MainListFragment<OSCBlog> {

    @Override
    protected ArrayList<OSCBlog> parserInAsync(byte[] t) {
        OSCBlogList list = XmlUtils.toBean(OSCBlogList.class, t);
        ArrayList<OSCBlog> datas = null;
        if (list != null) {
            datas = list.getBloglist();
            for (OSCBlog data : datas) data.format();
        }
        return datas;
    }

    @Override
    protected BasePullUpRecyclerAdapter<OSCBlog> getAdapter() {
        return new BasePullUpRecyclerAdapter<OSCBlog>(recyclerView, datas, R.layout.item_blog) {
            @Override
            public void convert(RecyclerHolder holder, final OSCBlog item, int position, boolean
                    isScrolling) {
                holder.setText(R.id.item_blog_tv_title, item.getTitle());
                holder.setText(R.id.item_blog_tv_description, item.getBody().trim());
                holder.setText(R.id.item_blog_tv_author, "推荐阅读");
                holder.setText(R.id.item_blog_tv_date, item.getPubDate());
                holder.getView(R.id.item_blog_tip_recommend).setVisibility(View.GONE);

                ImageView imageView = holder.getView(R.id.item_blog_img);
                imageView.setVisibility(View.GONE);
            }
        };
    }

    @Override
    public void doRequest() {
        doRequest(0);
    }

    private void doRequest(int index) {
        HttpParams params = new HttpParams();
        params.put("authoruid", 1428332);
        params.put("pageIndex", index);
        params.put("pageSize", 20);
        new Core.Builder().url(Api.OSC_BLOG)
                .contentType(Request.HttpMethod.GET)
                .cacheTime(60)
                .params(params)
                .callback(callBack)
                .doTask();
    }

    @Override
    public void onBottom() {
        doRequest(datas.size() / 20);
        adapter.setState(BasePullUpRecyclerAdapter.STATE_LOADING);
    }

    @Override
    public void onItemClick(View view, Object data, int position) {
        OSCBlog blog = ((OSCBlog) data);
        OSCBlogDetailActivity.goinActivity(getActivity(), blog.getId(), blog.getUrl(),
                blog.getTitle());
    }
}

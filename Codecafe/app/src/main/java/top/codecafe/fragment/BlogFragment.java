package top.codecafe.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;

import com.kymjs.api.Api;
import com.kymjs.frame.adapter.BasePullUpRecyclerAdapter;
import com.kymjs.frame.adapter.RecyclerHolder;
import com.kymjs.gallery.KJGalleryActivity;
import com.kymjs.kjcore.Core;
import com.kymjs.kjcore.http.KJHttp;
import com.kymjs.kjcore.http.Request;
import com.kymjs.kjcore.utils.StringUtils;
import com.kymjs.model.Blog;
import com.kymjs.model.BlogList;

import java.util.ArrayList;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import top.codecafe.R;
import top.codecafe.activity.BlogDetailActivity;
import top.codecafe.utils.XmlUtils;

/**
 * 博客列表界面
 *
 * @author kymjs (http://www.kymjs.com/) on 11/27/15.
 */
public class BlogFragment extends MainListFragment<Blog> {

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Observable.just(KJHttp.getCache(Api.BLOG_LIST))
                .filter(new Func1<byte[], Boolean>() {
                    @Override
                    public Boolean call(byte[] cache) {
                        return cache != null && cache.length != 0;
                    }
                })
                .map(new Func1<byte[], ArrayList<Blog>>() {
                    @Override
                    public ArrayList<Blog> call(byte[] bytes) {
                        return parserInAsync(bytes);
                    }
                })
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<ArrayList<Blog>>() {
                    @Override
                    public void call(ArrayList<Blog> blogs) {
                        datas = blogs;
                        adapter.refresh(datas);
                        viewDelegate.mEmptyLayout.dismiss();
                    }
                });
    }

    @Override
    protected ArrayList<Blog> parserInAsync(byte[] t) {
        return XmlUtils.toBean(BlogList.class, t).getChannel().getItemArray();
    }

    @Override
    protected BasePullUpRecyclerAdapter<Blog> getAdapter() {
        return new BasePullUpRecyclerAdapter<Blog>(recyclerView, datas, R.layout.item_blog) {
            @Override
            public void convert(RecyclerHolder holder, final Blog item, int position, boolean
                    isScrolling) {
                holder.setText(R.id.item_blog_tv_title, item.getTitle());
                holder.setText(R.id.item_blog_tv_description, item.getDescription());
                holder.setText(R.id.item_blog_tv_author, item.getAuthor());
                holder.setText(R.id.item_blog_tv_date, item.getPubDate());
                if (StringUtils.isEmpty(item.getRecommend())) {
                    holder.getView(R.id.item_blog_tip_recommend).setVisibility(View.GONE);
                } else {
                    holder.getView(R.id.item_blog_tip_recommend).setVisibility(View.VISIBLE);
                }

                ImageView imageView = holder.getView(R.id.item_blog_img);
                if (StringUtils.isEmpty(item.getImage())) {
                    imageView.setVisibility(View.GONE);
                } else {
                    imageView.setVisibility(View.VISIBLE);
                    new Core.Builder().url(item.getImage().trim())
                            .errorBitmapRes(R.mipmap.logo)
                            .view(imageView).doTask();
                    imageView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            KJGalleryActivity.toGallery(viewDelegate.getRootView().getContext(),
                                    item.getImage());
                        }
                    });
                }
            }
        };
    }

    @Override
    public void doRequest() {
        new Core.Builder().url(Api.BLOG_LIST)
                .contentType(Request.HttpMethod.GET)
                .cacheTime(60)
                .callback(callBack)
                .doTask();
    }

    @Override
    public void onItemClick(View view, Object data, int position) {
        Blog blog = ((Blog) data);
        BlogDetailActivity.goinActivity(getActivity(), blog.getLink(), blog.getTitle());
    }
}

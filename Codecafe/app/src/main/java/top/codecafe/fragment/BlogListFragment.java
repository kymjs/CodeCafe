package top.codecafe.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.kymjs.api.Api;
import com.kymjs.core.bitmap.client.BitmapCore;
import com.kymjs.frame.adapter.BasePullUpRecyclerAdapter;
import com.kymjs.frame.adapter.RecyclerHolder;
import com.kymjs.model.Blog;
import com.kymjs.model.BlogList;
import com.kymjs.rxvolley.RxVolley;

import java.util.ArrayList;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import top.codecafe.R;
import top.codecafe.activity.BlogDetailActivity;
import top.codecafe.utils.XmlUtil;

/**
 * 博客列表界面
 *
 * @author kymjs (http://www.kymjs.com/) on 11/27/15.
 */
public class BlogListFragment extends MainListFragment<Blog> {

    private Subscription cacheSubscript;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        cacheSubscript = Observable.just(RxVolley.getCache(Api.BLOG_LIST))
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
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<ArrayList<Blog>>() {
                    @Override
                    public void call(ArrayList<Blog> blogs) {
                        datas = blogs;
                        adapter.refresh(datas);
                        viewDelegate.mEmptyLayout.dismiss();
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                    }
                });
    }

    @Override
    protected ArrayList<Blog> parserInAsync(byte[] t) {
        return XmlUtil.toBean(BlogList.class, t).getChannel().getItemArray();
    }

    @Override
    protected BasePullUpRecyclerAdapter<Blog> getAdapter() {
        return new BasePullUpRecyclerAdapter<Blog>(recyclerView, datas, R.layout.item_blog) {
//            final View.OnClickListener imageClickListener = new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    KJGalleryActivity.toGallery(getActivity(), (String) v.getTag());
//                }
//            };

            @Override
            public void convert(RecyclerHolder holder, final Blog item, int position) {
                holder.setText(R.id.item_blog_tv_title, item.getTitle());
                holder.setText(R.id.item_blog_tv_description, item.getDescription());
                holder.setText(R.id.item_blog_tv_author, item.getAuthor());
                holder.setText(R.id.item_blog_tv_date, item.getPubDate());
                if (TextUtils.isEmpty(item.getRecommend())) {
                    holder.getView(R.id.item_blog_tip_recommend).setVisibility(View.GONE);
                } else {
                    holder.getView(R.id.item_blog_tip_recommend).setVisibility(View.VISIBLE);
                }

                ImageView imageView = holder.getView(R.id.item_blog_img);
                String imageUrl = item.getImage().trim();
                if (TextUtils.isEmpty(imageUrl)) {
                    imageView.setVisibility(View.GONE);
                } else {
                    imageView.setVisibility(View.VISIBLE);
                    new BitmapCore.Builder().url(imageUrl).view(imageView).doTask();
                    //在列表点击图片就直接进详情了,没必要进图片预览(布局文件中已取消焦点)
//                    imageView.setOnClickListener(imageClickListener);
                }
            }
        };
    }

    @Override
    public void doRequest() {
        new RxVolley.Builder().url(Api.BLOG_LIST)
                .contentType(RxVolley.Method.GET)
                .cacheTime(600)
                .callback(callBack)
                .doTask();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (cacheSubscript != null && cacheSubscript.isUnsubscribed())
            cacheSubscript.unsubscribe();
    }

    @Override
    public void onItemClick(View view, Object data, int position) {
        Blog blog = ((Blog) data);
        BlogDetailActivity.goinActivity(getActivity(), blog.getLink(), blog.getTitle());
    }
}

package top.codecafe.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.kymjs.api.Api;
import com.kymjs.core.bitmap.client.BitmapCore;
import com.kymjs.frame.adapter.BasePullUpRecyclerAdapter;
import com.kymjs.frame.adapter.RecyclerHolder;
import com.kymjs.model.Blog;
import com.kymjs.model.BlogList;
import com.kymjs.rxvolley.RxVolley;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.functions.Function;
import io.reactivex.rxjava3.functions.Predicate;
import io.reactivex.rxjava3.schedulers.Schedulers;
import top.codecafe.R;
import top.codecafe.utils.ConstKt;
import top.codecafe.utils.LinkDispatcher;

/**
 * 博客列表界面
 *
 * @author kymjs (https://kymjs.com/) on 11/27/15.
 */
public class BlogListFragment extends MainListFragment<Blog> {
    private Disposable cacheSubscript;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        cacheSubscript = Observable.just(RxVolley.getCache(Api.BLOG_LIST))
                .filter(new Predicate<byte[]>() {
                    @Override
                    public boolean test(byte[] cache) throws Throwable {
                        return cache != null && cache.length != 0;
                    }
                })
                .map(new Function<byte[], List<Blog>>() {
                    @Override
                    public List<Blog> apply(byte[] bytes) {
                        return parserInAsync(bytes);
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<Blog>>() {
                    @Override
                    public void accept(List<Blog> blogs) {
                        datas = blogs;
                        adapter.refresh(datas);
                        viewDelegate.mEmptyLayout.dismiss();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) {
                    }
                });
    }

    @Override
    protected List<Blog> parserInAsync(byte[] t) {
        return ConstKt.getGson().fromJson(new String(t), BlogList.class).getItems();
    }

    @Override
    protected BasePullUpRecyclerAdapter<Blog> getAdapter() {
        return new BasePullUpRecyclerAdapter<Blog>(recyclerView, datas, R.layout.item_blog) {

            @Override
            public void convert(RecyclerHolder holder, final Blog item, int position) {
                holder.setText(R.id.item_blog_tv_title, item.getTitle());
                holder.setText(R.id.item_blog_tv_description, item.getDescription());
                holder.setText(R.id.item_blog_tv_author, item.getAuthor());
                holder.setText(R.id.item_blog_tv_date, item.getPubDate());
                TextView typeTextView = holder.getView(R.id.item_blog_type);
                typeTextView.setText(item.getType());
                typeTextView.setTextColor(Color.parseColor(item.getTypeColor()));
                if (!item.getType().equalsIgnoreCase("付费")) {
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
        if (cacheSubscript != null && !cacheSubscript.isDisposed())
            cacheSubscript.dispose();
    }

    @Override
    public void onItemClick(View view, Object data, int position) {
        Blog blog = ((Blog) data);
        LinkDispatcher.dispatch(blog.getLink(), null, blog.getTitle());
    }
}

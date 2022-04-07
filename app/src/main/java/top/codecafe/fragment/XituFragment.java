package top.codecafe.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.kymjs.api.Api;
import com.kymjs.frame.adapter.BasePullUpRecyclerAdapter;
import com.kymjs.frame.adapter.RecyclerHolder;
import com.kymjs.model.XituBlog;
import com.kymjs.model.XituBlogList;
import com.kymjs.rxvolley.RxVolley;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.functions.Function;
import io.reactivex.rxjava3.functions.Predicate;
import io.reactivex.rxjava3.schedulers.Schedulers;
import top.codecafe.R;
import top.codecafe.utils.LinkDispatcher;
import top.codecafe.utils.XmlUtil;

/**
 * 稀土的数据rss
 *
 * @author kymjs (https://kymjs.com/) on 12/3/15.
 */
public class XituFragment extends MainListFragment<XituBlog> {

    private Disposable cacheSubscript;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        cacheSubscript = Observable.just(RxVolley.getCache(Api.XITU_BLOG_LIST))
                .filter(new Predicate<byte[]>() {
                    @Override
                    public boolean test(byte[] cache) throws Throwable {
                        return cache != null && cache.length != 0;
                    }
                })
                .map(new Function<byte[], List<XituBlog>>() {
                    @Override
                    public List<XituBlog> apply(byte[] bytes) {
                        return parserInAsync(bytes);
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<XituBlog>>() {
                    @Override
                    public void accept(List<XituBlog> blogs) {
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
    protected ArrayList<XituBlog> parserInAsync(byte[] t) {
        ArrayList<XituBlog> datas = XmlUtil.toBean(XituBlogList.class, t).getChannel().getItemArray();
        for (XituBlog data : datas) {
            data.format();
        }
        return datas;
    }

    @Override
    protected BasePullUpRecyclerAdapter<XituBlog> getAdapter() {
        return new BasePullUpRecyclerAdapter<XituBlog>(recyclerView, datas, R.layout.item_blog) {
            @Override
            public void convert(RecyclerHolder holder, final XituBlog item, int position) {
                holder.setText(R.id.item_blog_tv_title, item.getTitle());
                holder.setText(R.id.item_blog_tv_description, item.getDescription());
                holder.setText(R.id.item_blog_tv_date, item.getPubDate());
                holder.setText(R.id.item_blog_tv_author, item.getAuthor());
                TextView typeTextView = holder.getView(R.id.item_blog_type);
                if (item.getCategory().equalsIgnoreCase("android")) {
                    typeTextView.setText("Android");
                    typeTextView.setTextColor(Color.parseColor("#1E90FF"));
                } else if (item.getCategory().equalsIgnoreCase("ios")) {
                    typeTextView.setText("iOS");
                    typeTextView.setTextColor(Color.parseColor("#AE57A4"));
                } else if (item.getCategory().equalsIgnoreCase("backend")) {
                    typeTextView.setText("后端");
                    typeTextView.setTextColor(Color.parseColor("#008080"));
                } else if (item.getCategory().equalsIgnoreCase("frontend")) {
                    typeTextView.setText("前端");
                    typeTextView.setTextColor(Color.parseColor("#FF8000"));
                } else {
                    typeTextView.setText(item.getCategory());
                    typeTextView.setTextColor(Color.parseColor("#FF8000"));
                }
            }
        };
    }

    @Override
    public void doRequest() {
        new RxVolley.Builder().url(Api.XITU_BLOG_LIST)
                .contentType(RxVolley.Method.GET)
                .cacheTime(10)
                .callback(callBack)
                .doTask();
    }

    @Override
    public void onItemClick(View view, Object data, int position) {
        LinkDispatcher.dispatch(((XituBlog) data).getLink(), null, ((XituBlog) data).getTitle());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (cacheSubscript != null && !cacheSubscript.isDisposed())
            cacheSubscript.dispose();
    }
}

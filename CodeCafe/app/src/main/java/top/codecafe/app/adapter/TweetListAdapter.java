package top.codecafe.app.adapter;

import android.support.v7.widget.RecyclerView;

import java.util.Collection;

import top.codecafe.app.R;
import top.codecafe.app.bean.Tweet;
import top.codecafe.app.ui.base.BaseRecyclerAdapter;
import top.codecafe.app.ui.base.RecyclerHolder;

/**
 * 动态列表适配器
 *
 * @author kymjs (http://www.kymjs.com/) on 8/27/15.
 */
public class TweetListAdapter extends BaseRecyclerAdapter<Tweet> {

    public TweetListAdapter(RecyclerView v, Collection<Tweet> datas) {
        super(v, datas, R.layout.item_recycler_widget);
    }

    @Override
    public void convert(RecyclerHolder holder, Tweet item, boolean isScrolling) {
        holder.setText(R.id.name, item.getAuthor());
        holder.setText(R.id.type, item.getBody());
    }
}

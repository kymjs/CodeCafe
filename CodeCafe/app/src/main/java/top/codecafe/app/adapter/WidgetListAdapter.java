package top.codecafe.app.adapter;

import android.support.v7.widget.RecyclerView;

import java.util.Collection;

import top.codecafe.app.R;
import top.codecafe.app.bean.Widget;
import top.codecafe.app.ui.base.BaseRecyclerAdapter;
import top.codecafe.app.ui.base.RecyclerHolder;

/**
 * UI控件列表适配器
 *
 * @author kymjs (http://www.kymjs.com/) on 8/27/15.
 */
public class WidgetListAdapter extends BaseRecyclerAdapter<Widget> {

    public WidgetListAdapter(RecyclerView v, Collection<Widget> datas) {
        super(v, datas, R.layout.item_recycler_widget);
    }

    @Override
    public void convert(RecyclerHolder holder, Widget item, boolean isScrolling) {
        holder.setText(R.id.name, item.getName());
        holder.setText(R.id.type, item.getDesc());
    }
}

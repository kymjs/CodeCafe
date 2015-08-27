package top.codecafe.app.ui;

import top.codecafe.app.adapter.WidgetListAdapter
import top.codecafe.app.bean.Widget
import top.codecafe.app.ui.base.BasePullActivity
import top.codecafe.app.ui.base.BaseRecyclerAdapter
import top.codecafe.kjframe.R

/**
 * 控件列表界面
 * @author kymjs (http://www.kymjs.com/) on 8/27/15.
 */
public class WidgetListActivity : BasePullActivity() {

    override fun getRecyclerAdapter(): BaseRecyclerAdapter<Widget> {
        return WidgetListAdapter(recyclerView, null)
    }

    override fun refreshing() {
    }

    override fun completeRefresh() {
    }
}

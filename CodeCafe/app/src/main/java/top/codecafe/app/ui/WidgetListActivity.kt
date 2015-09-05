package top.codecafe.app.ui;

import android.support.v7.widget.LinearLayoutManager
import top.codecafe.app.adapter.WidgetListAdapter
import top.codecafe.app.api.API
import top.codecafe.app.api.Parser
import top.codecafe.app.bean.Widget
import top.codecafe.app.ui.base.BasePullActivity
import top.codecafe.app.ui.base.BaseRecyclerAdapter
import top.codecafe.kjframe.KJHttp
import top.codecafe.kjframe.http.HttpCallBack
import java.util.ArrayList

/**
 * 控件列表界面
 * @author kymjs (http://www.kymjs.com/) on 8/27/15.
 */
public class WidgetListActivity : BasePullActivity() {

    var datas: ArrayList<Widget> = ArrayList()

    val kjh: KJHttp = KJHttp()

    override fun getRecyclerAdapter(): BaseRecyclerAdapter<Widget> {
        return WidgetListAdapter(recyclerView, datas)
    }

    override fun initData() {
        for (i in 0..50) {
            var data: Widget = Widget()
            data.name = "名字$i"
            datas.add(data)
        }
    }

    override fun initWidget() {
        super.initWidget()
        recyclerView?.setLayoutManager(LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false))
    }
    override fun onRefresh() {
        requestData()
    }

    override fun requestData() {
        setSwipeRefreshLoadingState()
        API.getWidgetList("", object : HttpCallBack() {
            override fun onSuccess(s: String?) {
                datas = Parser.parserWidgetList(s);
                recyclerView?.setAdapter(getRecyclerAdapter())
                setSwipeRefreshLoadedState()
            }
        })
    }
}

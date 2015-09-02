package top.codecafe.app.ui.fragment

import top.codecafe.app.api.API
import top.codecafe.app.api.Parser
import top.codecafe.app.ui.base.BasePullFragment
import top.codecafe.app.ui.base.BaseRecyclerAdapter
import top.codecafe.kjframe.http.HttpCallBack

/**
 * 今日话题，(话题列表)
 * @author kymjs (http://www.kymjs.com/) on 8/13/15.
 */
public class TopicList : BasePullFragment() {

    override fun getRecyclerAdapter(): BaseRecyclerAdapter<*> {
        throw UnsupportedOperationException()
    }

    override fun completeRefresh() {
        throw UnsupportedOperationException()
    }

    override fun refreshing() {
    }

    override fun requestData() {
        API.getTopicList("CodeCafe", object : HttpCallBack() {
            override fun onSuccess(s: String?) {
                recyclerView?.setAdapter(getRecyclerAdapter())
                refreshLayout?.finishRefreshing()
            }
        })
    }
}

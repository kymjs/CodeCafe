package top.codecafe.app.ui.fragment

import android.support.v7.widget.LinearLayoutManager
import android.view.View
import top.codecafe.app.adapter.TweetListAdapter
import top.codecafe.app.api.API
import top.codecafe.app.api.Parser
import top.codecafe.app.bean.Tweet
import top.codecafe.app.bean.TweetsList
import top.codecafe.app.ui.base.BasePullFragment
import top.codecafe.app.ui.base.BaseRecyclerAdapter
import top.codecafe.app.utils.kjlog
import top.codecafe.app.utils.toast
import top.codecafe.kjframe.http.HttpCallBack
import java.util.TreeSet

/**
 * 今日话题，(话题列表)
 * @author kymjs (http://www.kymjs.com/) on 8/13/15.
 */
public class TopicList : BasePullFragment() {
    override fun onRefresh() {
        requestData()
    }

    private val tweets: TreeSet<Tweet> = TreeSet<Tweet>()
    private var adapter: TweetListAdapter? = null

    override fun getRecyclerAdapter(): BaseRecyclerAdapter<Tweet> {
        adapter = TweetListAdapter(recyclerView, tweets)
        return adapter!!
    }

    override fun initWidget(parentView: View?) {
        super.initWidget(parentView)
        recyclerView?.setLayoutManager(LinearLayoutManager(outsideAty, LinearLayoutManager.VERTICAL, false))
    }

    override fun requestData() {
        setSwipeRefreshLoadingState()
        API.getTopicList("重要的事情说三遍", object : HttpCallBack() {
            override fun onSuccess(s: String?) {
                kjlog("网络请求：$s")
                val datas: List<Tweet> = Parser.xmlToBean(javaClass<TweetsList>(), s)
                        .getList();
                tweets.addAll(datas)
                recyclerView?.setAdapter(getRecyclerAdapter())
                setSwipeRefreshLoadedState()
            }
        })
    }
}

package top.codecafe.app.ui.fragment

import android.support.v7.widget.LinearLayoutManager
import android.view.View
import rx.functions.Action1
import top.codecafe.app.adapter.TopicListAdapter
import top.codecafe.app.api.API
import top.codecafe.app.api.Parser
import top.codecafe.app.bean.Tweet
import top.codecafe.app.bean.TweetsList
import top.codecafe.app.ui.base.BasePullFragment
import top.codecafe.app.ui.base.BaseRecyclerAdapter
import top.codecafe.app.utils.kjlog
import top.codecafe.kjframe.http.HttpCallBack
import java.util.TreeSet

/**
 * 今日话题，(话题列表)
 * @author kymjs (http://www.kymjs.com/) on 8/13/15.
 */
public class TopicList : BasePullFragment() {

    private val tweets: TreeSet<Tweet> = TreeSet<Tweet>()
    private var adapter: TopicListAdapter? = null

    /**
     * TopicListAdapter中item点击事件的接收者
     */
    public val itemClickSubscribers: Action1<Int> = Action1() { id ->
        kjlog("点击事件===$id")
        when (id) {
            
        }
    }
    
    override fun getRecyclerAdapter(): BaseRecyclerAdapter<Tweet> {
        adapter = TopicListAdapter(recyclerView!!, tweets)
        return adapter!!
    }

    override fun initWidget(parentView: View?) {
        super.initWidget(parentView)
        recyclerView?.setLayoutManager(LinearLayoutManager(outsideAty, LinearLayoutManager.VERTICAL, false))
    }

    override fun onRefresh() {
        requestData()
    }

    override fun requestData() {
        setSwipeRefreshLoadingState()
        API.getTopicList("完善简历 送键盘", object : HttpCallBack() {
            override fun onSuccess(s: String?) {
                kjlog("网络请求：$s")
                val datas: List<Tweet> = Parser.xmlToBean(javaClass<TweetsList>(), s)
                        .getList();
                tweets.addAll(datas)
                recyclerView?.setAdapter(getRecyclerAdapter())
                adapter?.addSubscription(itemClickSubscribers)
                setSwipeRefreshLoadedState()
            }
        })
    }
}

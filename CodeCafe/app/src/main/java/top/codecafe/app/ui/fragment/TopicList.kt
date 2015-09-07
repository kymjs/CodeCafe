package top.codecafe.app.ui.fragment

import android.content.Intent
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import rx.functions.Action1
import top.codecafe.app.R
import top.codecafe.app.adapter.TopicListAdapter
import top.codecafe.app.api.API
import top.codecafe.app.api.Parser
import top.codecafe.app.bean.ItemViewData
import top.codecafe.app.bean.Tweet
import top.codecafe.app.bean.TweetsList
import top.codecafe.app.ui.ImageActivity
import top.codecafe.app.ui.base.BasePullFragment
import top.codecafe.app.ui.base.BaseRecyclerAdapter
import top.codecafe.app.utils.kjlog
import top.codecafe.app.utils.showActivity
import top.codecafe.app.utils.toast
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
    public val itemClickSubscribers: Action1<ItemViewData> = Action1() { evenData ->
        when (evenData.id) {
            R.id.item_topic_forwarding -> toast("点击了按钮")
            R.id.item_topic_image -> {
                val intent: Intent = Intent(outsideAty, javaClass<ImageActivity>())
                var item: Object? = evenData.data
                if (item is Tweet)
                    intent.putExtra(ImageActivity.URL_KEY, item.getImgBig())
                showActivity(intent)
            }
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

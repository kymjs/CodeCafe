package top.codecafe.app.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import rx.functions.Action1
import top.codecafe.app.R
import top.codecafe.app.adapter.TopicListAdapter
import top.codecafe.app.api.API
import top.codecafe.app.api.Parser
import top.codecafe.app.bean.ItemViewData
import top.codecafe.app.bean.Tweet
import top.codecafe.app.bean.TweetsList
import top.codecafe.app.ui.ImageActivity
import top.codecafe.app.ui.base.BaseMainFragment
import top.codecafe.app.ui.base.BasePullFragment
import top.codecafe.app.ui.base.BaseRecyclerAdapter
import top.codecafe.app.ui.base.OnFloatButtonClickListener
import top.codecafe.app.utils.kjlog
import top.codecafe.app.utils.showActivity
import top.codecafe.app.utils.toast
import top.codecafe.kjframe.http.HttpCallBack
import java.util.*

/**
 * 心情广场

 * @author kymjs (http://www.kymjs.com/) on 8/13/15.
 */
public class MoodPlaza : BasePullFragment(), OnFloatButtonClickListener {

    private val tweets: TreeSet<Tweet> = TreeSet<Tweet>()
    private var adapter: BaseRecyclerAdapter<Tweet>? = null

    /**
     * TopicListAdapter中item点击事件的接收者
     */
    public val itemClickSubscribers: Action1<ItemViewData> = Action1() { evenData ->
        when (evenData.id) {
            R.id.item_topic_forwarding -> toast("点击了按钮")
            R.id.item_topic_image -> {
                val intent: Intent = Intent(outsideAty, ImageActivity::class.java)
                var item: Any? = evenData.data
                if (item is Tweet)
                    intent.putExtra(ImageActivity.URL_KEY, item.getImgBig())
                showActivity(intent)
            }
        }
    }

    override fun getRecyclerAdapter(): BaseRecyclerAdapter<Tweet> {
        adapter = adapter?.refresh(tweets) ?: TopicListAdapter(recyclerView!!, tweets)
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
        API.getMoodList(object : HttpCallBack() {
            override fun onSuccessInAsync(t: Any) {
                if (t is ByteArray) {
                    val datas: List<Tweet> = Parser.xmlToBean(TweetsList::class.java, String(t))
                            .getList();
                    tweets.addAll(datas)
                }
            }

            override fun onSuccess(s: String?) {
                kjlog("网络请求：$s")
                recyclerView?.setAdapter(getRecyclerAdapter())
                (adapter as TopicListAdapter?)?.addSubscription(itemClickSubscribers)
                setSwipeRefreshLoadedState()
            }
        }, tweets.size() / 20)
    }

    override fun onFloatButtonClick(v: View) {
        toast("发送心情说说", v.context)
    }
}

package top.codecafe.app.adapter

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView

import rx.Observable
import rx.functions.Action1
import top.codecafe.app.R
import top.codecafe.app.bean.Tweet
import top.codecafe.app.bean.User
import top.codecafe.app.ui.base.BaseRecyclerAdapter
import top.codecafe.app.ui.base.RecyclerHolder
import top.codecafe.app.utils.kjlog
import top.codecafe.kjframe.KJBitmap
import top.codecafe.kjframe.utils.StringUtils
import java.util.*

/**
 * 动态列表适配器

 * @author kymjs (http://www.kymjs.com/) on 8/27/15.
 */
public class TopicListAdapter(v: RecyclerView, datas: Collection<Tweet>) :
        BaseRecyclerAdapter<Tweet>(v, datas, R.layout.item_recycler_topic) {

    private val kjb = KJBitmap()
    private var observable: Observable<Int> = Observable.just(1);
    private val subscriberArray: MutableList<Action1<Int>> = ArrayList()

    override fun convert(holder: RecyclerHolder, item: Tweet, isScrolling: Boolean) {
        holder.setText(R.id.item_topic_tv_name, item.getAuthor())
        holder.setText(R.id.item_topic_tv_content, item.getBody())
        kjb.displayLoadAndErrorBitmap(holder.getView<View>(R.id.item_topic_img_head), item.getPortrait(), R.drawable.default_head, R.drawable.default_head)
        val image = holder.getView<ImageView>(R.id.item_topic_image)
        if (StringUtils.isEmpty(item.getImgBig())) {
            image.setVisibility(View.GONE)
        } else {
            image.setVisibility(View.VISIBLE)
            kjb.display(image, item.getImgBig())
            setClickObservable(holder, image)
        }

        setClickObservable(holder, id = R.id.item_topic_forwarding)
    }

    /**
     * 给一个View或一个id所对应的View设置点击事件监听器，这个监听器将会向外层发送这一事件{@link sendClickEven(v:View)}
     */
    private fun setClickObservable(holder: RecyclerHolder,view: View? = null, id: Int = 0) {
        if (view == null) {
            holder.getView<View>(id).setOnClickListener { v -> sendClickEven(v) }
        } else {
            view.setOnClickListener { v -> sendClickEven(v) }
        }
    }

    /**
     * 向这个适配器的外部传递适配器中item的点击事件
     */
    private fun sendClickEven(v: View) {
        observable = observable.map { id -> v.getId() }

        for (action in subscriberArray) {
            observable.subscribe(action)
        }
    }

    /**
     * 添加一个点击事件接收器（已知调用类：TopicList）
     */
    public fun addSubscription(onNext: Action1<Int>) {
        subscriberArray.add(onNext)
    }
}

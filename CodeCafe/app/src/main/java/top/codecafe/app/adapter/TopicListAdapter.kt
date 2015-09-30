package top.codecafe.app.adapter

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import rx.Observable
import rx.functions.Action1
import rx.functions.Func1
import top.codecafe.app.R
import top.codecafe.app.bean.ItemViewData
import top.codecafe.app.bean.Tweet
import top.codecafe.app.ui.base.BaseRecyclerAdapter
import top.codecafe.app.ui.base.RecyclerHolder
import top.codecafe.kjframe.KJBitmap
import top.codecafe.kjframe.utils.StringUtils
import java.util.ArrayList

/**
 * 动态列表适配器

 * @author kymjs (http://www.kymjs.com/) on 8/27/15.
 */
public class TopicListAdapter(v: RecyclerView, datas: Collection<Tweet>) :
        BaseRecyclerAdapter<Tweet>(v, datas, R.layout.item_recycler_topic) {

    private val kjb = KJBitmap()
    private var observable: Observable<ItemViewData> = Observable.just(ItemViewData());
    private val subscriberArray: MutableList<Action1<ItemViewData>> = ArrayList(5)

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
    private fun setClickObservable(holder: RecyclerHolder, view: View? = null, id: Int = 0) {
        if (view == null) {
            holder.getView<View>(id).setOnClickListener { v -> sendClickEven(v, holder.getLayoutPosition()) }
        } else {
            view.setOnClickListener { v -> sendClickEven(v, holder.getLayoutPosition()) }
        }
    }

    /**
     * 向这个适配器的外部传递适配器中item的点击事件
     */
    private fun sendClickEven(v: View, position: Int) {
        observable.map { data ->
            data.id = v.getId()
            data.data = realDatas.get(position)
            data.position = position
            data.view = v
            data
        }
                .flatMap { Observable.from(subscriberArray) }
                .subscribe { action -> observable.subscribe (action) }
    }

    /**
     * 添加一个点击事件接收器（已知调用类：TopicList）
     */
    fun addSubscription(onNext: Action1<ItemViewData>) {
        subscriberArray.add(onNext)
    }
}

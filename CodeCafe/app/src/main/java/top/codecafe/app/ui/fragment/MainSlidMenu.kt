package top.codecafe.app.ui.fragment

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import rx.Observable
import rx.Subscriber
import top.codecafe.app.R
import top.codecafe.app.ui.MainActivity
import top.codecafe.app.ui.widget.RoundImageView
import top.codecafe.kjframe.KJBitmap
import top.codecafe.kjframe.ui.SupportFragment

/**
 * 侧滑菜单Fragment

 * @author kymjs (http://www.kymjs.com/) on 8/11/15.
 */
public class MainSlidMenu : SupportFragment() {

    private var headImage: RoundImageView? = null
    private var imgGender: ImageView? = null
    private var tvName: TextView? = null
    private var tvDesc: TextView? = null

    private val kjb: KJBitmap = KJBitmap()

    override fun inflaterView(inflater: LayoutInflater?, container: ViewGroup?, bundle: Bundle?): View? {
        return View.inflate(getActivity(), R.layout.frag_main_menu, null);
    }

    override fun initWidget(parentView: View) {
        bindViewClick(R.id.menu_item_tag1, R.id.menu_item_tag2, R.id.menu_item_tag3, R.id.menu_item_tag4, R.id.menu_item_tag5)
        headImage = bindView(R.id.menu_img_head)
        imgGender = bindView(R.id.menu_img_gender)
        tvName = bindView(R.id.menu_tv_name)
        tvDesc = bindView(R.id.menu_tv_desc)

        //        kjb.displayWithErrorBitmap(headImage, user?.getPortrait() ?: "http://kymjs.com/image/logo_s.png", R.drawable.default_head)
        //        tvName?.setText(user?.getName() ?: "未知用户")
        //        if (user?.getGender() == 1) {
        //            imgGender?.setImageResource(R.drawable.userinfo_icon_male)
        //        } else if (user?.getGender() == 2) {
        //            imgGender?.setImageResource(R.drawable.userinfo_icon_female)
        //        }
        tvDesc?.setText("helloworld，一句话代表一个世界")
    }

    override fun widgetClick(v: View?) {
        super.widgetClick(v)
        when (v!!.getId()) {
            R.id.menu_item_tag1 -> sendChangeFragmentEven(1)
            R.id.menu_item_tag2 -> sendChangeFragmentEven(2)
            R.id.menu_item_tag3 -> sendChangeFragmentEven(3)
            R.id.menu_item_tag4 -> sendChangeFragmentEven(4)
            R.id.menu_item_tag5 -> sendChangeFragmentEven(5)
        }
    }

    /**
     * 侧滑菜单中的item被点击，发送消息，让MainActivity响应这个事件
     */
    fun sendChangeFragmentEven(tag: Int) {
        var changeFragmentEven = Observable.create(object : Observable.OnSubscribe<Int> {
            override fun call(sub: Subscriber<in Int>) {
                sub.onNext(tag)
                sub.onCompleted()
            }
        })

        val aty: Activity = getActivity()

        if (aty is MainActivity)
            changeFragmentEven.subscribe(aty.changeContentSubscribers);
    }
}

package top.codecafe.app.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import top.codecafe.app.R
import top.codecafe.app.ui.fragment.BaseMainFragment

/**
 * 心情广场

 * @author kymjs (http://www.kymjs.com/) on 8/13/15.
 */
public class MoodPlaza : BaseMainFragment() {
    val api:String = "http://www.oschina.net/action/api/tweet_list?pageSize=20&pageIndex="

    override fun inflaterView(layoutInflater: LayoutInflater, viewGroup: ViewGroup, bundle: Bundle?): View? {
        val rootView: View = layoutInflater.inflate(R.layout.frag_main_mood, null)
        return rootView
    }

    override fun initData(){
        
    }
    
    override fun initWidget(parentView: View?) {
        super.initWidget(parentView)
    }
}

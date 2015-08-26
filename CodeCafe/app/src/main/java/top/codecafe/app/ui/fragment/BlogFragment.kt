package top.codecafe.app.ui.fragment;

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import top.codecafe.app.R
import top.codecafe.app.ui.base.BaseMainFragment

/**
 * 博客界面
 *
 * @author kymjs (http://www.kymjs.com/) on 8/26/15.
 */
public class BlogFragment : BaseMainFragment() {
    override fun inflaterView(layoutInflater: LayoutInflater, viewGroup: ViewGroup, bundle: Bundle?): View? {
        val rootView: View = layoutInflater.inflate(R.layout.frag_main_mood, null)
        return rootView
    }
}

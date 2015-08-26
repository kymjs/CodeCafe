package top.codecafe.app.ui.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import top.codecafe.app.ui.MainActivity
import top.codecafe.kjframe.ui.SupportFragment

/**
 * 主界面使用Fragment的基类

 * @author kymjs (http://www.kymjs.com/) on 8/13/15.
 */
public abstract class BaseMainFragment : SupportFragment() {
    protected var outsideAty: MainActivity? = null

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        outsideAty = getActivity() as MainActivity
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    /**
     * 设置标题

     * @param text
     */
    protected fun setTitle(text: CharSequence) {
        outsideAty?.tvTitle?.setText(text)
    }
}

package top.codecafe.app.ui.fragment

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

    //    private val actionBarRes = ActionBarRes()
    //
    //    /**
    //     * 封装一下方便一起返回(JAVA没有结构体这么一种东西实在是个遗憾)
    //
    //     * @author kymjs (https://www.kymjs.com/)
    //     */
    //    public class ActionBarRes {
    //        public var title: CharSequence = ""
    //        public var backImageId: Int = 0
    //        public var backImageDrawable: Drawable? = null
    //        public var menuImageId: Int = 0
    //        public var menuImageDrawable: Drawable? = null
    //    }
    //
    //
    //    /**
    //     * 方便Fragment中设置ActionBar资源
    //
    //     * @param actionBarRes
    //     * *
    //     * @return
    //     */
    //    protected fun setActionBarRes(actionBarRes: ActionBarRes) {
    //    }
}

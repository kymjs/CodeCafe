package top.codecafe.app.bean;

import android.view.View

/**
 * 一个item中包含数据的封装
 *
 * @author kymjs (http://www.kymjs.com/) on 9/7/15.
 */
public final class ItemViewData {
    var view: View? = null
    var position: Int = 0
    var data: Any? = null
    var id: Int = 0
}

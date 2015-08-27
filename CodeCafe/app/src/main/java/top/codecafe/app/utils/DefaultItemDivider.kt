package top.codecafe.app.utils

import android.graphics.Rect
import android.support.v7.widget.RecyclerView
import android.view.View

/**
 * RecyclerView 的默认 divider 高度 20px
 * @author kymjs (http://www.kymjs.com/) on 8/27/15.
 */
open class DefaultItemDivider : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        super.getItemOffsets(outRect, view, parent, state);
        outRect.set(0, 0, 0, 20);//每个item的底部偏移
    }
}
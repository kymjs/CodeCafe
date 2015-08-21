package top.codecafe.app.utils

import android.app.Activity
import android.content.Context
import android.util.TypedValue
import android.view.View
import android.widget.Toast
import top.codecafe.kjframe.ui.SupportFragment

/**
 * 工具方法
 * 
 * @author kymjs (http://www.kymjs.com/) on 15/8/21.
 */
fun Context.toast(message: CharSequence, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, message, duration).show()
}

fun Context.screenHeight(): Int = getResources().getDisplayMetrics().heightPixels

fun Context.screenWidth(): Int = getResources().getDisplayMetrics().widthPixels

fun Context.dip2px(dpValue: Float): Int = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
        dpValue, getResources().getDisplayMetrics()).toInt();

fun Context.sp2px(value: Float): Int = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
        value, getResources().getDisplayMetrics()).toInt();

fun Context.px2sp(value: Float): Int = (value / getResources().getDisplayMetrics().scaledDensity + 0.5f).toInt()

fun Context.px2dp(value: Float): Int = (value / getResources().getDisplayMetrics().density + 0.5f).toInt()

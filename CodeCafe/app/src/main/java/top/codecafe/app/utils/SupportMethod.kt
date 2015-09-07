package top.codecafe.app.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.support.v4.app.Fragment
import android.util.Log
import android.util.TypedValue
import android.widget.Toast

/**
 * 工具方法
 *
 * @author kymjs (http://www.kymjs.com/) on 15/8/21.
 */


fun Context.toast(message: CharSequence, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, message, duration).show()
}

fun Fragment.toast(message: CharSequence, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(getActivity(), message, duration).show()
}

fun Context.screenHeight(): Int = getResources().getDisplayMetrics().heightPixels

fun Context.screenWidth(): Int = getResources().getDisplayMetrics().widthPixels

fun Context.dip2px(dpValue: Float): Int = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
        dpValue, getResources().getDisplayMetrics()).toInt();

fun Context.sp2px(value: Float): Int = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
        value, getResources().getDisplayMetrics()).toInt();

fun Context.px2sp(value: Float): Int = (value / getResources().getDisplayMetrics().scaledDensity + 0.5f).toInt()

fun Context.px2dp(value: Float): Int = (value / getResources().getDisplayMetrics().density + 0.5f).toInt()

fun Context.showActivity(clazz: Class<*>) {
    startActivity(Intent(this, clazz))
}

fun Context.showActivity(intent: Intent) {
    startActivity(intent)
}

fun Activity.skipActivity(clazz: Class<*>) {
    startActivity(Intent(this, clazz))
    finish();
}

fun Fragment.showActivity(clazz: Class<*>) {
    startActivity(Intent(getActivity(), clazz))
}

fun Fragment.showActivity(intent: Intent) {
    startActivity(intent)
}

fun Fragment.skipActivity(clazz: Class<*>) {
    startActivity(Intent(getActivity(), clazz))
    getActivity().finish();
}

fun Any.kjlog(msg: String? = "") {
    val element: StackTraceElement = Thread.currentThread().getStackTrace()[3]
    Log.i("kymjs", StringBuilder().append("[")
            .append(element.getFileName())
            .append(":")
            .append(element.getLineNumber())
            .append("]-->")
            .append(msg ?: "").toString())
}
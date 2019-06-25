package com.bear.screenshot.utils

import android.content.Context

/**
 * description:
 * author: bear .
 * Created date:  2019-06-25.
 * mail:2280885690@qq.com
 */
fun dip2px(context: Context,dpValue: Float): Int {
    return (dpValue * context.resources.displayMetrics.density + 0.5f).toInt()
}

fun px2dip(context: Context,pxValue: Float): Int {
    return (pxValue / context.resources.displayMetrics.density + 0.5f).toInt()
}
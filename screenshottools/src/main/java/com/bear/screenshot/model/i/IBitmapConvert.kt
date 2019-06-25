package com.bear.screenshot.model.i

import android.view.View

/**
 * description:
 * author: bear .
 * Created date:  2019-06-25.
 * mail:2280885690@qq.com
 */
interface IBitmapConvert {
    fun convert(view: View, callBack: IBitmapConvertCallBack?)
}
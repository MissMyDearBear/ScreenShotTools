package com.bear.screenshot

import android.content.Context
import android.graphics.Bitmap
import android.view.View
import com.bear.screenshot.model.engine.BitmapConvertImpl
import com.bear.screenshot.model.engine.ScreenShotImpl
import com.bear.screenshot.model.i.IScreenShotCallBack

/**
 * description: 长截图工具类
 * author: bear .
 * Created date:  2019-06-24.
 * mail:2280885690@qq.com
 */
class ScreenShotTools private constructor() {

    private val screenShot by lazy {
        ScreenShotImpl(BitmapConvertImpl())
    }

    fun takeCapture(context: Context, view: View, callBack: IScreenShotCallBack?) {
        screenShot.takeCapture(context, view, callBack)
    }

    fun takeCapture(context: Context, view: View, topBitmap: Bitmap?, callBack: IScreenShotCallBack?) {
        screenShot.takeCapture(context, view, topBitmap, callBack)
    }

    fun takeCapture(
        context: Context,
        view: View,
        topBitmap: Bitmap?,
        bottomBitmap: Bitmap?,
        callBack: IScreenShotCallBack?
    ) {
        screenShot.takeCapture(context, view, topBitmap, bottomBitmap, callBack)
    }

    fun takeCapture(
        context: Context,
        view: View,
        topBitmap: Bitmap?,
        bottomBitmap: Bitmap?,
        width: Int,
        callBack: IScreenShotCallBack?
    ) {
        screenShot.takeCapture(context, view, topBitmap, bottomBitmap, width, callBack)
    }


    companion object {
        val instance: ScreenShotTools by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
            ScreenShotTools()
        }
    }
}
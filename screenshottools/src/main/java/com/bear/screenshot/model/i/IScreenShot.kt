package com.bear.screenshot.model.i

import android.content.Context
import android.graphics.Bitmap
import android.view.View

/**
 * description:
 * author: bear .
 * Created date:  2019-06-24.
 * mail:2280885690@qq.com
 */
interface IScreenShot {

    fun takeCapture(context: Context,view: View,callBack: IScreenShotCallBack?)

    fun takeCapture(context: Context,view: View,topBitmap:Bitmap?,callBack: IScreenShotCallBack?)

    fun takeCapture(context: Context,view: View,topBitmap:Bitmap?,bottomBitmap:Bitmap?,callBack: IScreenShotCallBack?)

    fun takeCapture(context: Context,view: View,topBitmap: Bitmap?,bottomBitmap: Bitmap?,width:Int,callBack: IScreenShotCallBack?)
}
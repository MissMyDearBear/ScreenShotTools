package com.bear.screenshot.model.i

import com.bear.screenshot.model.ScreenBitmap

/**
 * description: 截图callBack（异步处理）
 * author: bear .
 * Created date:  2019-06-24.
 * mail:2280885690@qq.com
 */
interface IScreenShotCallBack{
   fun onResult(screenBitmap: ScreenBitmap?)
}
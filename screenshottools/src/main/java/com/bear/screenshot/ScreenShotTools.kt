package com.bear.screenshot

/**
 * description: 长截图工具类
 * author: bear .
 * Created date:  2019-06-24.
 * mail:2280885690@qq.com
 */
class ScreenShotTools private constructor(){






    companion object{
      val instance : ScreenShotTools by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
        ScreenShotTools()
      }
    }
}
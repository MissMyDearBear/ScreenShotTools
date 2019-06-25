package com.bear.screenshot.model.engine

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.view.View
import com.bear.screenshot.model.config.ScreenShotConfig
import com.bear.screenshot.model.i.IBitmapConvert
import com.bear.screenshot.model.i.IBitmapConvertCallBack
import com.bear.screenshot.model.i.IScreenShot
import com.bear.screenshot.model.i.IScreenShotCallBack
import com.bear.screenshot.utils.combineBitmapsIntoOnlyOne
import com.bear.screenshot.utils.savingBitmapIntoFile

/**
 * description:
 * author: bear .
 * Created date:  2019-06-25.
 * mail:2280885690@qq.com
 */
class ScreenShotImpl(bitmapConvert: IBitmapConvert) : IScreenShot {

    private var mBitmapConvert = bitmapConvert

    override fun takeCapture(context: Context, view: View, callBack: IScreenShotCallBack?) {
        if (context is Activity && context.isFinishing) return
        mBitmapConvert.convert(view, object : IBitmapConvertCallBack {
            override fun onResult(bitmap: Bitmap?) {
                bitmap?.let {
                    savingBitmapIntoFile(context, it, callBack)
                }
            }
        })

    }

    override fun takeCapture(context: Context, view: View, topBitmap: Bitmap?, callBack: IScreenShotCallBack?) {
        if (topBitmap == null) {
            takeCapture(context, view, callBack)
            return
        }
        mBitmapConvert.convert(view, object : IBitmapConvertCallBack {
            override fun onResult(bitmap: Bitmap?) {
                bitmap?.let {
                    val bitmapList = ArrayList<Bitmap>()
                    bitmapList.add(topBitmap)
                    bitmapList.add(it)
                    val combineBitmap = combineBitmapsIntoOnlyOne(
                        context,
                        bitmapList,
                        context.resources.displayMetrics.widthPixels,
                        ScreenShotConfig.MAX_SCREEN_SHOT_HEIGHT
                    )
                    savingBitmapIntoFile(context, combineBitmap, callBack)
                }
            }
        })
    }

    override fun takeCapture(
        context: Context,
        view: View,
        topBitmap: Bitmap?,
        bottomBitmap: Bitmap?,
        callBack: IScreenShotCallBack?
    ) {
        if (topBitmap == null && bottomBitmap == null) {
            takeCapture(context, view, callBack)
            return
        }
        mBitmapConvert.convert(view, object : IBitmapConvertCallBack {
            override fun onResult(bitmap: Bitmap?) {
                bitmap?.let {
                    val bitmapList = ArrayList<Bitmap>()
                    if (topBitmap != null) {
                        bitmapList.add(topBitmap)
                    }
                    bitmapList.add(it)
                    if (bottomBitmap != null) {
                        bitmapList.add(bottomBitmap)
                    }
                    val combineBitmap = combineBitmapsIntoOnlyOne(
                        context,
                        bitmapList,
                        context.resources.displayMetrics.widthPixels,
                        ScreenShotConfig.MAX_SCREEN_SHOT_HEIGHT
                    )
                    savingBitmapIntoFile(context, combineBitmap, callBack)
                }
            }
        })
    }

    override fun takeCapture(
        context: Context,
        view: View,
        topBitmap: Bitmap?,
        bottomBitmap: Bitmap?,
        width: Int,
        callBack: IScreenShotCallBack?
    ) {
        if (topBitmap == null && bottomBitmap == null) {
            takeCapture(context, view, callBack)
            return
        }
        mBitmapConvert.convert(view, object : IBitmapConvertCallBack {
            override fun onResult(bitmap: Bitmap?) {
                bitmap?.let {
                    val bitmapList = ArrayList<Bitmap>()
                    if (topBitmap != null) {
                        bitmapList.add(topBitmap)
                    }
                    bitmapList.add(it)
                    if (bottomBitmap != null) {
                        bitmapList.add(bottomBitmap)
                    }
                    val combineBitmap = combineBitmapsIntoOnlyOne(
                        context,
                        bitmapList,
                        width,
                        ScreenShotConfig.MAX_SCREEN_SHOT_HEIGHT
                    )
                    savingBitmapIntoFile(context, combineBitmap, callBack)
                }
            }
        })
    }
}
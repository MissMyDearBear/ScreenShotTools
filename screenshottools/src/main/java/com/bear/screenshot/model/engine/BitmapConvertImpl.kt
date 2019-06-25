package com.bear.screenshot.model.engine

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import android.view.View.MeasureSpec
import android.webkit.WebView
import android.widget.ScrollView
import com.bear.screenshot.model.config.ERROR_ACHIEVE_MAX_HEIGHT
import com.bear.screenshot.model.i.IBitmapConvert
import com.bear.screenshot.model.i.IBitmapConvertCallBack
import com.bear.screenshot.utils.dip2px


/**
 * description:
 * author: bear .
 * Created date:  2019-06-25.
 * mail:2280885690@qq.com
 */
class BitmapConvertImpl : IBitmapConvert {
    companion object {
        const val TAG = "BitmapConvertImpl"
    }

    override fun convert(view: View, callBack: IBitmapConvertCallBack?) {
        when (view) {
            is RecyclerView -> recycleViewConvert(view, callBack)
            is ScrollView -> scrollViewConvert(view, callBack)
            is WebView -> webViewConvert(view, callBack)
            else -> viewConvert(view, callBack)
        }
    }

    private fun recycleViewConvert(view: RecyclerView, callBack: IBitmapConvertCallBack?) {
        if (view.adapter == null) {
            callBack?.onResult(null)
        }
        val adapter = view.adapter
        val paint = Paint()
        val oneScreenHeight = view.measuredHeight
        var shotHeight = 0
        if (adapter != null && adapter.itemCount > 0) {
            for (i in 0 until adapter.itemCount) {
                val holder = adapter.createViewHolder(
                    view,
                    adapter.getItemViewType(i)
                )
                adapter.onBindViewHolder(holder, i)
                holder.itemView.measure(
                    MeasureSpec.makeMeasureSpec(view.width, MeasureSpec.EXACTLY),
                    MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED)
                )
                shotHeight += holder.itemView.measuredHeight
                if (shotHeight > 15000) {
                    //设置截图最大值
                    Log.e(TAG, ERROR_ACHIEVE_MAX_HEIGHT)
                    callBack?.onResult(null)
                }
            }
            //添加底部高度(加载更多或loading布局高度,此处为固定值:)
            val footerHeight = dip2px(view.context, 42f)
            shotHeight += footerHeight

            //返回到顶部
            while (view.canScrollVertically(-1)) {
                view.scrollBy(0, -oneScreenHeight)
            }
            view.scrollBy(0, -oneScreenHeight)
            //绘制截图的背景
            val bigBitmap = Bitmap.createBitmap(view.measuredWidth, shotHeight, Bitmap.Config.ARGB_8888)
            val bigCanvas = Canvas(bigBitmap)
            val lBackground = view.background
            if (lBackground is ColorDrawable) {
                val lColor = lBackground.color
                bigCanvas.drawColor(lColor)
            }


            val drawOffset = intArrayOf(0)
            val canvas = Canvas()
            if (shotHeight <= oneScreenHeight) {
                //仅有一页
                val bitmap =
                    Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
                canvas.setBitmap(bitmap)
                view.draw(canvas)
                callBack?.onResult(bitmap)
            } else {
                //超过一页
                val finalShotHeight = shotHeight
                view.postDelayed(object : Runnable {
                    override fun run() {
                        if (drawOffset[0] + oneScreenHeight < finalShotHeight) {
                            //超过一屏
                            val bitmap = Bitmap.createBitmap(
                                view.width,
                                view.height,
                                Bitmap.Config.ARGB_8888
                            )
                            canvas.setBitmap(bitmap)
                            view.draw(canvas)
                            bigCanvas.drawBitmap(bitmap, 0f, drawOffset[0].toFloat(), paint)
                            drawOffset[0] += oneScreenHeight
                            view.scrollBy(0, oneScreenHeight)
                            try {
                                bitmap.recycle()
                            } catch (ex: Exception) {
                                ex.printStackTrace()
                            }

                            view.postDelayed(this, 30)
                        } else {
                            //不足一屏时的处理
                            val leftHeight = finalShotHeight - drawOffset[0] - footerHeight
                            view.scrollBy(0, leftHeight)
                            val top = oneScreenHeight - (finalShotHeight - drawOffset[0])
                            if (top > 0 && leftHeight > 0) {
                                var bitmap = Bitmap.createBitmap(
                                    view.width,
                                    view.height,
                                    Bitmap.Config.ARGB_8888
                                )
                                canvas.setBitmap(bitmap)
                                view.draw(canvas)
                                //截图,只要补足的那块图
                                bitmap = Bitmap.createBitmap(bitmap, 0, top, bitmap.width, leftHeight, null, false)
                                bigCanvas.drawBitmap(bitmap, 0f, drawOffset[0].toFloat(), paint)
                                try {
                                    bitmap.recycle()
                                } catch (ex: Exception) {
                                    ex.printStackTrace()
                                }

                            }
                            callBack?.onResult(bigBitmap)
                        }
                    }
                }, 1000)
            }
        }
    }

    private fun scrollViewConvert(scrollView: ScrollView, callBack: IBitmapConvertCallBack?) {
        var contBmp: Bitmap?
        var h = 0
        for (i in 0 until scrollView.childCount) {
            h += scrollView.getChildAt(i).height
        }
        try {
            contBmp = Bitmap.createBitmap(scrollView.width, h, Bitmap.Config.ARGB_4444)
            val contentC = Canvas(contBmp)
            scrollView.draw(contentC)
        } catch (e: Exception) {

            e.printStackTrace()
            contBmp = null
        } catch (error: OutOfMemoryError) {
            contBmp = null
        }
        callBack?.onResult(contBmp)

    }

    private fun webViewConvert(webView: WebView, callBack: IBitmapConvertCallBack?) {
        var contBmp: Bitmap?
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //获取webview缩放率
            try {
                val scale = webView.scale
                //得到缩放后webview内容的高度
                val webViewHeight = (webView.contentHeight * scale).toInt()
                contBmp = Bitmap.createBitmap(webView.width, webViewHeight, Bitmap.Config.ARGB_4444)
                val canvas = Canvas(contBmp)
                //绘制
                webView.draw(canvas)
            } catch (e: Exception) {
                e.printStackTrace()
                contBmp = null
            }

        } else {
            //获取Picture对象
            val picture = webView.capturePicture()
            //得到图片的宽和高（没有reflect图片内容）
            val width = picture.width
            val height = picture.height
            if (width > 0 && height > 0) {
                //创建位图
                try {
                    contBmp = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
                    val canvas = Canvas(contBmp)
                    //绘制(会调用native方法，完成图形绘制)
                    picture.draw(canvas)
                } catch (e: Exception) {
                    e.printStackTrace()
                    contBmp = null
                }

            } else {
                contBmp = null
            }
        }
        callBack?.onResult(contBmp)
    }

    @SuppressLint("ObsoleteSdkInt")
    private fun viewConvert(view: View, callBack: IBitmapConvertCallBack?) {
        view.isDrawingCacheEnabled = true
        view.buildDrawingCache()
        if (Build.VERSION.SDK_INT >= 11) {
            view.measure(
                MeasureSpec.makeMeasureSpec(
                    view.width,
                    MeasureSpec.EXACTLY
                ), MeasureSpec.makeMeasureSpec(
                    view.height, MeasureSpec.EXACTLY
                )
            )
            view.layout(
                view.x.toInt(), view.y.toInt(),
                view.x.toInt() + view.measuredWidth,
                view.y.toInt() + view.measuredHeight
            )
        } else {
            view.measure(
                MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED),
                MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED)
            )
            view.layout(0, 0, view.measuredWidth, view.measuredHeight)
        }
        val b = Bitmap.createBitmap(view.drawingCache, 0, 0, view.measuredWidth, view.measuredHeight)

        view.isDrawingCacheEnabled = false
        view.destroyDrawingCache()
        callBack?.onResult(b)
    }
}
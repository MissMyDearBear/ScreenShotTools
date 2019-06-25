package com.bear.screenshot.utils

import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Matrix
import android.net.Uri
import android.os.Environment
import com.bear.screenshot.model.ScreenBitmap
import com.bear.screenshot.model.config.PermissionConst
import com.bear.screenshot.model.config.ScreenShotConfig
import com.bear.screenshot.model.i.IScreenShotCallBack
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

/**
 * description: bitMap
 * author: bear .
 * Created date:  2019-06-25.
 * mail:2280885690@qq.com
 */
fun combineBitmapsIntoOnlyOne(
    context: Context,
    bitmaps: List<Bitmap>?,
    totalWidth: Int,
    maxHeight: Int
): Bitmap? {
    var bitmap: Bitmap? = null
    var height = 0
    if (bitmaps != null && bitmaps.isNotEmpty()) {
        for (map in bitmaps) {
            height += map.height
        }
        if (height > maxHeight) {
            return null
        }
        bitmap = Bitmap.createBitmap(totalWidth, height, Bitmap.Config.ARGB_4444)
        val canvas = Canvas(bitmap)
        canvas.drawColor(ScreenShotConfig.COLOR_CONTENT_BG)
        var drawHeight = 0
        for (map in bitmaps) {
            canvas.drawBitmap(map, 0f, drawHeight.toFloat(), null)
            drawHeight += map.height
        }
    }
    return bitmap
}

/**
 * 将传入的Bitmap合理压缩后输出到系统截屏目录下
 * 命名格式为：Screenshot+时间戳+启信宝报名.jpg
 * 同时通知系统重新扫描系统文件
 *
 * @param context 用于通知重新扫描文件系统，为提升性能可去掉
 */
fun savingBitmapIntoFile(context: Context?, bitmap: Bitmap?, callBack: IScreenShotCallBack?) {
    if (context == null || (context is Activity && context.isFinishing)) {
        return
    }
    if (bitmap == null) {
        callBack?.onResult(null)
        return
    }
    //存储权限需要询问用户
    if (!((context as Activity).let {
            PermissionConst.canUseThisPermission(
                it,
                "存储",
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                PermissionConst.REQUEST_PERMISSION_STORY_WRITE
            )
        })
    ) {
        return
    }
    val dialog = ProgressDialog(context)
    dialog.setMessage("正在生成长图···")
    dialog.show()
    val thread = Thread(Runnable {
        var fileReturnPath = ""
        // 获取当前时间
        val sdf = SimpleDateFormat("yyyy-MM-dd-HH-mm-ss-ms", Locale.getDefault())
        val data = sdf.format(Date())

        // 获取内存路径
        // 设置图片路径+命名规范
        // 声明输出文件
        val storagePath = Environment.getExternalStorageDirectory().absolutePath
        val fileTitle = "Screenshot_" + data + "_biz.jpg"
        val filePath = "$storagePath/DCIM/"
        val fileAbsolutePath = filePath + fileTitle
        val file = File(fileAbsolutePath)
        /**
         * 质压与比压结合
         * 分级压缩
         * 输出文件
         */
        if (bitmap != null) {
            try {
                if (!file.exists()) {
                    file.createNewFile()
                }
                // 首先，对原图进行一步质量压缩,形成初步文件
                val fos = FileOutputStream(file, false)
                bitmap.compress(Bitmap.CompressFormat.JPEG, 50, fos)

                // 另建一个文件other_file预备输出
                val otherFileTitle = "Screenshot_$data.jpg"
                val otherFileAbsolutePath = filePath + otherFileTitle
                val otherFile = File(otherFileAbsolutePath)
                val otherFos = FileOutputStream(otherFile, false)

                // 其次，要判断质压之后的文件大小，按文件大小分级进行处理
                val fileSize = file.length() / 1024 // size of file(KB)
                if (fileSize < 0 || !file.exists()) {
                    // 零级： 文件判空
                    try {
                        fos.flush()
                        otherFos.flush()
                        otherFos.close()
                        fos.close()
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }

                    return@Runnable
                } else if (fileSize in 1..256) {
                    try {
                        fos.flush()
                        otherFos.flush()
                        otherFos.close()
                        fos.close()
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }

                    // 一级： 直接输出
                    deleteFile(otherFile)
                    // 通知刷新文件系统，显示最新截取的图文件
                    fileReturnPath = fileAbsolutePath
                    context.sendBroadcast(
                        Intent(
                            Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
                            Uri.parse("file://$fileAbsolutePath")
                        )
                    )
                } else if (fileSize in 257..768) {
                    // 二级： 简单压缩:压缩为原比例的3/4，质压为50%
                    anyRatioCompressing(
                        bitmap,
                        3.toFloat() / 4,
                        3.toFloat() / 4
                    ).compress(Bitmap.CompressFormat.JPEG, 40, otherFos)
                    try {
                        fos.flush()
                        otherFos.flush()
                        otherFos.close()
                        fos.close()
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }

                    deleteFile(file)
                    // 通知刷新文件系统，显示最新截取的图文件
                    fileReturnPath = otherFileAbsolutePath
                    context.sendBroadcast(
                        Intent(
                            Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
                            Uri.parse("file://$otherFileAbsolutePath")
                        )
                    )
                } else if (fileSize in 769..1280) {
                    // 三级： 中度压缩：压缩为原比例的1/2，质压为40%
                    anyRatioCompressing(
                        bitmap,
                        1.toFloat() / 2,
                        1.toFloat() / 2
                    ).compress(Bitmap.CompressFormat.JPEG, 40, otherFos)
                    try {
                        fos.flush()
                        otherFos.flush()
                        otherFos.close()
                        fos.close()
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }

                    deleteFile(file)
                    // 通知刷新文件系统，显示最新截取的图文件
                    fileReturnPath = otherFileAbsolutePath
                    context.sendBroadcast(
                        Intent(
                            Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
                            Uri.parse("file://$otherFileAbsolutePath")
                        )
                    )
                } else if (fileSize in 1281..2048) {
                    // 四级： 大幅压缩：压缩为原比例的1/3，质压为40%
                    anyRatioCompressing(
                        bitmap,
                        1.toFloat() / 3,
                        1.toFloat() / 3
                    ).compress(Bitmap.CompressFormat.JPEG, 40, otherFos)
                    try {
                        fos.flush()
                        otherFos.flush()
                        otherFos.close()
                        fos.close()
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }

                    deleteFile(file)
                    // 通知刷新文件系统，显示最新截取的图文件
                    fileReturnPath = otherFileAbsolutePath
                    context.sendBroadcast(
                        Intent(
                            Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
                            Uri.parse("file://$otherFileAbsolutePath")
                        )
                    )
                } else if (fileSize > 2048) {
                    // 五级： 中度压缩：压缩为原比例的1/2，质压为40%
                    anyRatioCompressing(
                        bitmap,
                        1.toFloat() / 2,
                        1.toFloat() / 2
                    ).compress(Bitmap.CompressFormat.JPEG, 40, otherFos)
                    try {
                        fos.flush()
                        otherFos.flush()
                        otherFos.close()
                        fos.close()
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }

                    deleteFile(file)
                    // 通知刷新文件系统，显示最新截取的图文件
                    fileReturnPath = otherFileAbsolutePath
                    context.sendBroadcast(
                        Intent(
                            Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
                            Uri.parse("file://$otherFileAbsolutePath")
                        )
                    )
                }
                dialog.dismiss()
                val screenBitmap = ScreenBitmap()
                screenBitmap.apply {
                    this.bitmap = bitmap
                    this.filePath = fileReturnPath
                }
                callBack?.onResult(screenBitmap)

            } catch (e: Exception) {
                e.printStackTrace()
                dialog.dismiss()
                val screenBitmap = ScreenBitmap()
                screenBitmap.apply {
                    this.bitmap = bitmap
                    this.filePath = fileReturnPath
                }
                callBack?.onResult(screenBitmap)
            }

        } else {
            dialog.dismiss()
            callBack?.onResult(null)
        }
    })
    thread.start()
}

/**
 * 可实现任意宽高比例压缩（宽高压比可不同）的压缩方法（主要用于微压）
 *
 * @param bitmap       源图
 * @param width_ratio  宽压比（float）（0<&&<1)
 * @param height_ratio 高压比（float）（0<&&<1)
 * @return 目标图片
 *
 *
 */
fun anyRatioCompressing(bitmap: Bitmap, width_ratio: Float, height_ratio: Float): Bitmap {
    val matrix = Matrix()
    matrix.postScale(width_ratio, height_ratio)
    return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, false)
}


/**
 * 删除指定文件
 *
 * @param file 要删除的文件
 *
 *
 */
fun deleteFile(file: File): Boolean {
    if (!file.exists()) {
        return false
    }
    val security = System.getSecurityManager()
    security?.checkDelete(file.absolutePath)
    return file.delete()
}

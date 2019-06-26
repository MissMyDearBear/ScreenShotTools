package com.bear.screenshotdemo

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import com.bear.screenshot.ScreenShotTools
import com.bear.screenshot.model.ScreenBitmap
import com.bear.screenshot.model.i.IScreenShotCallBack
import kotlinx.android.synthetic.main.activity_web_view.*

/**
 * description:
 * author: bear .
 * Created date:  2019-06-26.
 * mail:2280885690@qq.com
 */
class WebViewActivity : AppCompatActivity() {

    init {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            android.webkit.WebView.enableSlowWholeDocumentDraw()
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web_view)
        tv.setOnClickListener {
            ScreenShotTools.instance.takeCapture(this, web_view, object : IScreenShotCallBack {
                override fun onResult(screenBitmap: ScreenBitmap?) {
                    //todo do your things
                }
            })
        }
        initWebViewSetting()
        web_view.loadUrl("https://www.baidu.com/")
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun initWebViewSetting() {
        val webSettings = web_view.settings
        webSettings.javaScriptCanOpenWindowsAutomatically = true
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webSettings.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
        }
        webSettings.useWideViewPort = true// 关键点
        webSettings.layoutAlgorithm = WebSettings.LayoutAlgorithm.SINGLE_COLUMN
        // webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        webSettings.displayZoomControls = false
        webSettings.javaScriptEnabled = true // 设置支持javascript脚本
        webSettings.allowFileAccess = true // 允许访问文件
        webSettings.builtInZoomControls = true // 设置显示缩放按钮
        webSettings.setSupportZoom(true) // 支持缩放
        webSettings.blockNetworkImage = false
        webSettings.loadWithOverviewMode = true
        webSettings.domStorageEnabled = true

        web_view.requestFocus()
        web_view.scrollBarStyle = View.SCROLLBARS_INSIDE_OVERLAY
        web_view.webChromeClient = WebChromeClient()

        webSettings.useWideViewPort = true
        webSettings.loadWithOverviewMode = true

        /**
         * 用WebView显示图片，可使用这个参数 设置网页布局类型： 1、LayoutAlgorithm.NARROW_COLUMNS ：
         * 适应内容大小 2、LayoutAlgorithm.SINGLE_COLUMN:适应屏幕，内容将自动缩放
         */
        webSettings.layoutAlgorithm = WebSettings.LayoutAlgorithm.SINGLE_COLUMN

    }

    companion object {
        @JvmStatic
        fun action(context: Context) {
            val intent = Intent(context, WebViewActivity::class.java)
            context.startActivity(intent)
        }
    }
}
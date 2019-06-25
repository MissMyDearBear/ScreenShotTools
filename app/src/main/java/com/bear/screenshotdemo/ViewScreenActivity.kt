package com.bear.screenshotdemo

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.bear.screenshot.ScreenShotTools
import com.bear.screenshot.model.ScreenBitmap
import com.bear.screenshot.model.i.IScreenShotCallBack
import kotlinx.android.synthetic.main.activity_view.*

/**
 * description:
 * author: bear .
 * Created date:  2019-06-25.
 * mail:2280885690@qq.com
 */
class ViewScreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view)
        ll.setOnClickListener {
            ScreenShotTools.instance.takeCapture(this, ll, object : IScreenShotCallBack {
                override fun onResult(screenBitmap: ScreenBitmap?) {

                }
            })
        }
    }

    companion object {
        @JvmStatic
        fun action(context: Context) {
            val intent = Intent(context, ViewScreenActivity::class.java)
            context.startActivity(intent)
        }
    }
}
package com.bear.screenshotdemo

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_show_screen_image.*

/**
 * description:
 * author: bear .
 * Created date:  2019-06-28.
 * mail:2280885690@qq.com
 */
class ShowScreenImageActivity : AppCompatActivity() {

    private val path: String by lazy {
        intent.getStringExtra(EXTRA_KEY_PATH)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_screen_image)
        iv.setImageBitmap(BitmapUtils.decodeBitmap(path, this))
    }


    companion object {
        private const val EXTRA_KEY_PATH = "path"
        @JvmStatic
        fun action(context: Context, path: String) {
            val intent = Intent(context, ShowScreenImageActivity::class.java)
            intent.putExtra(EXTRA_KEY_PATH, path)
            context.startActivity(intent)
        }
    }
}
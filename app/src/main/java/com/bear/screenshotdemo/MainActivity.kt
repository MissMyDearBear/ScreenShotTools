package com.bear.screenshotdemo

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity(), View.OnClickListener {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        button.setOnClickListener(this)
        button2.setOnClickListener(this)
        button3.setOnClickListener(this)
        button4.setOnClickListener(this)
    }

    override fun onClick(view: View?) {
        when (view) {
            button -> {
                ViewScreenActivity.action(this)
            }
            button2 -> {
                ScrollViewActivity.action(this)
            }
            button3 -> {
                RecyclerViewActivity.action(this)
            }
            button4 -> {
                WebViewActivity.action(this)
            }
        }
    }
}

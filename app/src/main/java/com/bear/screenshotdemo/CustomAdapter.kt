package com.bear.screenshotdemo

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

/**
 * description:
 * author: bear .
 * Created date:  2019-06-26.
 * mail:2280885690@qq.com
 */
class CustomAdapter(list: ArrayList<String>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val mList = list

    override fun onCreateViewHolder(h: ViewGroup, p: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(h.context).inflate(R.layout.item_list, h, false);
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    override fun onBindViewHolder(h: RecyclerView.ViewHolder, p: Int) {
        if (h is MyViewHolder) {
            h.tvText.text = mList[p]
        }
    }


    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvText: TextView = itemView.findViewById<TextView>(R.id.tv)
    }


}
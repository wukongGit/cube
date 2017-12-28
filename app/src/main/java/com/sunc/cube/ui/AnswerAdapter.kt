package com.sunc.cube.ui

import android.app.Activity
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

import com.sunc.cube.R
import com.sunc.cube.view.recyclerview.RecyclerArrayAdapter

/**
 * Description:
 * Date: 2017-09-18 16:45
 * Author: suncheng
 */

class AnswerAdapter(activity: Activity, data: List<String>) : RecyclerArrayAdapter<String, AnswerAdapter.DataHolder>(activity, data) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AnswerAdapter.DataHolder {
        return DataHolder(mInflater.inflate(R.layout.item_answer, parent, false))
    }

    override fun onBindViewHolder(holder: AnswerAdapter.DataHolder, position: Int) {
        val info = getItem(position)
        holder.mTextView.text = info
    }

    class DataHolder(view: View) : RecyclerView.ViewHolder(view) {
        var mTextView: TextView = view.findViewById(R.id.iv_step) as TextView

    }
}

package com.sunc.cube.ui

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.bumptech.glide.Glide

import com.sunc.cube.R
import com.sunc.cube.bmob.Picture
import com.sunc.cube.view.recyclerview.RecyclerArrayAdapter

/**
 * Description:
 * Date: 2017-09-18 16:45
 * Author: suncheng
 */

class PictureAdapter(activity: ListActivity, data: List<Picture>, internal var mOnItemClickListener: PictureAdapter.OnItemClickListener?) : RecyclerArrayAdapter<Picture, RecyclerView.ViewHolder>(activity, data) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PictureAdapter.DataHolder {
        return DataHolder(mInflater.inflate(R.layout.item_picture, parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val info = getItem(position)
        Glide.with(mActivity).load(info.url).into((holder as DataHolder).mPicture)
        holder.itemView.setOnClickListener { mOnItemClickListener?.onItemClick(info) }
    }

    class DataHolder(view: View) : RecyclerView.ViewHolder(view) {
        var mPicture: ImageView = view.findViewById(R.id.iv_picture) as ImageView

    }

    interface OnItemClickListener {
        fun onItemClick(picture: Picture)
    }
}

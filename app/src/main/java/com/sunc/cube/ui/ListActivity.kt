package com.sunc.cube.ui

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.StaggeredGridLayoutManager
import android.view.View
import cn.bmob.v3.BmobQuery
import cn.bmob.v3.exception.BmobException
import cn.bmob.v3.listener.FindListener
import com.squareup.otto.Subscribe

import com.sunc.cube.R
import com.sunc.cube.bmob.Picture
import com.sunc.cube.eventbus.NotifyType
import com.sunc.cube.toast
import com.sunc.cube.ui.PictureAdapter.OnItemClickListener
import com.sunc.cube.utils.AndroidUtils
import com.sunc.cube.utils.DBKeys
import com.sunc.cube.utils.DBUtils
import com.sunc.cube.utils.TranslucentStatusCompat
import kotlinx.android.synthetic.main.activity_picture.*
import java.util.*

class ListActivity : BaseActivity() {
    private var mPictures: ArrayList<Picture> = ArrayList()
    var mCurrentPage = 0
    val mPageSize = 10
    var mHasMore:Boolean = true
    var mIsLoading:Boolean = false
    lateinit var mAdapter : PictureAdapter
    var mLevel: Int = DrawerActivity.LEVEL_1
    var mLocalKey: String = DBKeys.PICTURE_1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        TranslucentStatusCompat.requestTranslucentStatus(this)
        setContentView(R.layout.activity_picture)
        mLevel = intent.getIntExtra(DrawerActivity.LEVEL, DrawerActivity.LEVEL_1)
        when(mLevel) {
            DrawerActivity.LEVEL_1 -> mLocalKey = DBKeys.PICTURE_1
            DrawerActivity.LEVEL_2 -> mLocalKey = DBKeys.PICTURE_2
            DrawerActivity.LEVEL_3 -> mLocalKey = DBKeys.PICTURE_3
            DrawerActivity.LEVEL_4 -> mLocalKey = DBKeys.PICTURE_4
            DrawerActivity.LEVEL_5 -> mLocalKey = DBKeys.PICTURE_5
        }
        init()
    }

    fun init() {
        tv_level.text = "${getString(R.string.level)}：$mLevel"
        iv_boring.setOnClickListener { view ->
            val intent = Intent(this, UploadActivity::class.java)
            intent.putExtra(DrawerActivity.LEVEL, mLevel)
            startActivity(intent)
        }
        rv_picture.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        addEndlessScrollListener()
        mAdapter = PictureAdapter(this, mPictures, object : OnItemClickListener {
            override fun onItemClick(picture: Picture) {
                startActivity(picture)
            }
        })
        rv_picture.adapter = mAdapter
        requestPictures(0)
    }

    private fun addEndlessScrollListener() {
        rv_picture.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView?, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                val layoutManager = recyclerView!!.layoutManager
                val lastVisibleItemPosition = (layoutManager as StaggeredGridLayoutManager).findLastVisibleItemPositions(null)
                val visibleItemCount = layoutManager.getChildCount()
                val totalItemCount = layoutManager.getItemCount()

                if (hasMore() && visibleItemCount > 0 &&
                        newState == RecyclerView.SCROLL_STATE_IDLE &&
                        (lastVisibleItemPosition[0] >= totalItemCount - 1 || lastVisibleItemPosition[1] >= totalItemCount - 1)) {
                    requestPictures(mCurrentPage)
                    rv_picture.scrollToPosition(rv_picture.adapter.itemCount - 1)
                }
            }

        })
    }

    fun hasMore() = mHasMore
    fun isLoading() = mIsLoading

    fun requestPictures(page: Int) {
        if (!AndroidUtils.isNetworkConnected(this)) {
            if (mCurrentPage == 0) {
                var list : ArrayList<Picture>? = DBUtils.read(mLocalKey)
                if (list != null) {
                    mPictures = list!!
                    mAdapter.set(mPictures)
                    mAdapter.notifyDataSetChanged()
                } else {
                    toast(getString(R.string.network_error))
                }
            }
            return
        }
        if (isLoading()) {
            return
        }
        progress_bar.visibility = View.VISIBLE
        mIsLoading = true
        var query = BmobQuery<Picture>()
        query.addWhereEqualTo("level", mLevel)
        query.setLimit(mPageSize)
        query.setSkip(page * mPageSize)
        query.order("-createdAt")
        query.findObjects(object : FindListener<Picture>() {
            override fun done(p0: List<Picture>?, p1: BmobException?) {
                progress_bar.visibility = View.GONE
                if(p1 == null) {
                    if (p0 == null || p0.isEmpty()) {
                        if (mCurrentPage == 0) {
                            var list : ArrayList<Picture>? = DBUtils.read(mLocalKey)
                            if (list != null) {
                                mPictures = list!!
                                mAdapter.set(mPictures)
                                mAdapter.notifyDataSetChanged()
                            }
                        }
                    } else {
                        if (mCurrentPage == 0) {
                            DBUtils.write(mLocalKey, p0)
                            mPictures.clear()
                        }
                        mPictures.addAll(p0)
                        mHasMore = mPageSize == p0.size
                        if (mHasMore) {
                            mCurrentPage++
                        }
                        mAdapter.notifyDataSetChanged()
                    }
                } else {
                    if (mCurrentPage == 0) {
                        var list : ArrayList<Picture>? = DBUtils.read(mLocalKey)
                        if (list != null) {
                            mPictures = list!!
                            mAdapter.set(mPictures)
                            mAdapter.notifyDataSetChanged()
                        }
                    }
                }
                mIsLoading = false
            }
        })
    }

    fun startActivity(picture: Picture) {
        if (mLevel == DrawerActivity.LEVEL_5) {
            val intent = Intent(this, Drawer4Activity::class.java)
            intent.putExtra(DrawerActivity.LEVEL, mLevel)
            intent.putExtra(DrawerActivity.PICTURE, picture)
            startActivity(intent)
        } else {
            val intent = Intent(this, DrawerActivity::class.java)
            intent.putExtra(DrawerActivity.LEVEL, mLevel)
            intent.putExtra(DrawerActivity.PICTURE, picture)
            startActivity(intent)
        }

    }

    @Subscribe
    override fun onNotify(type: NotifyType) {
        when (type.type) {
            NotifyType.UPLOAD_PICTURE_SUCCESS -> {
                mCurrentPage = 0
                requestPictures(mCurrentPage)
            }
        }
    }
}

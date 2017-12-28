package com.sunc.cube.ui

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.support.v7.widget.Toolbar
import android.view.Gravity
import android.view.View
import android.widget.TextView
import cn.bmob.v3.BmobBatch
import cn.bmob.v3.BmobObject
import cn.bmob.v3.datatype.BatchResult

import com.sunc.cube.R
import com.sunc.cube.utils.TranslucentStatusCompat
import kotlinx.android.synthetic.main.activity_upload.*
import cn.bmob.v3.datatype.BmobFile
import cn.bmob.v3.exception.BmobException
import cn.bmob.v3.listener.QueryListListener
import cn.bmob.v3.listener.UploadFileListener
import com.bumptech.glide.Glide
import com.sunc.cube.bmob.Picture
import com.sunc.cube.eventbus.EventBus
import com.sunc.cube.eventbus.NotifyType
import com.sunc.cube.toast
import com.sunc.cube.utils.AndroidUtils
import com.tbruyelle.rxpermissions.RxPermissions
import java.io.File
import java.util.*

class UploadActivity : BaseActivity() {
    override fun onNotify(type: NotifyType?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    val REQUEST_CODE_PICTURE_PICK = 100
    var mPath: String = ""
    var mLevels: ArrayList<Int> = ArrayList()
    var mDefaultLevel: Int = DrawerActivity.LEVEL_1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        TranslucentStatusCompat.setWindowStatusBarColor(this, R.color.main_color_normal)
        setContentView(R.layout.activity_upload)
        mDefaultLevel = intent.getIntExtra(DrawerActivity.LEVEL, DrawerActivity.LEVEL_1)
        initHeader(tool_bar)
        initView()
    }

    private fun initHeader(toolbar: Toolbar) {
        val headerView = layoutInflater.inflate(R.layout.layout_main_header, toolbar, false)
        val lp  = headerView.layoutParams as  Toolbar.LayoutParams
        lp.gravity = Gravity.CENTER
        toolbar.addView(headerView, lp)
        var titleView  = headerView.findViewById(R.id.title) as TextView
        titleView.text = getString(R.string.submit_styleable_picture)

        val back = headerView.findViewById(R.id.back)
        back.visibility = View.VISIBLE
        back.setOnClickListener { onBackPressed() }
    }

    private fun initView() {
        when(mDefaultLevel) {
            DrawerActivity.LEVEL_1 -> cb_1.isChecked = true
            DrawerActivity.LEVEL_2 -> cb_2.isChecked = true
            DrawerActivity.LEVEL_3 -> cb_3.isChecked = true
            DrawerActivity.LEVEL_4 -> cb_4.isChecked = true
            DrawerActivity.LEVEL_5 -> cb_5.isChecked = true
        }
        iv_add.setOnClickListener { view ->
            safeOpen()
        }
        btn_submit.setOnClickListener { view ->
            if(checkNull()) {
                return@setOnClickListener
            }
            uploadPicture(mPath)
        }
    }

    fun checkNull(): Boolean {
        if (mPath.isEmpty()) {
            toast(getString(R.string.please_upload_picture))
            return true
        }
        if (!cb_1.isChecked && !cb_2.isChecked && !cb_3.isChecked && !cb_4.isChecked && !cb_5.isChecked) {
            toast(getString(R.string.please_set_level))
            return true
        }
        if (cb_1.isChecked) {
            mLevels.add(10)
        }
        if (cb_2.isChecked) {
            mLevels.add(20)
        }
        if (cb_3.isChecked) {
            mLevels.add(30)
        }
        if (cb_4.isChecked) {
            mLevels.add(50)
        }
        if (cb_5.isChecked) {
            mLevels.add(100)
        }
        return false
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when(requestCode) {
                REQUEST_CODE_PICTURE_PICK -> {
                    var uri = data?.data
                    Glide.with(this).load(uri).into(iv_add)
                    mPath = AndroidUtils.getRealFilePath(this, uri)
                }
            }
        }
    }

    fun uploadPicture(path: String) {
        progress_bar.visibility = View.VISIBLE
        var query = BmobFile(File(path))
        query.uploadblock(object : UploadFileListener() {
            override fun done(p0: BmobException?) {
                if (p0 == null) {
                    uploadInfo(query.fileUrl)
                } else {
                    progress_bar.visibility = View.GONE
                    this@UploadActivity.toast(p0.message!!)
                }
            }
        })
    }

    fun uploadInfo(url :String) {
        var info = mutableListOf<Picture>()
        for (level in mLevels) {
            var pic = Picture()
            pic.name = et_description.text.toString()
            pic.anthor = et_author.text.toString()
            pic.url = url
            pic.level = level
            pic.cai = et_cai.text.toString()
            info.add(pic)
        }
        BmobBatch().insertBatch(info as List<BmobObject>?).doBatch(object : QueryListListener<BatchResult>() {
            override fun done(p0: MutableList<BatchResult>?, p1: BmobException?) {
                if (p1 == null) {
                    startActivity(info[0], mLevels[0])
                    EventBus.bus().post(NotifyType(NotifyType.UPLOAD_PICTURE_SUCCESS))
                    finish()
                } else {
                    this@UploadActivity.toast(p1.message!!)
                }
                progress_bar.visibility = View.GONE
            }
        })
    }

    private fun safeOpen() {
        RxPermissions.getInstance(this)
                .request(Manifest.permission.READ_EXTERNAL_STORAGE)
                .subscribe({ b ->
                    if (b) {
                        var intent = Intent(Intent.ACTION_PICK)
                        intent.addCategory(Intent.CATEGORY_OPENABLE)
                        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*")
                        if (Build.VERSION.SDK_INT < 19) {
                            intent.action = Intent.ACTION_GET_CONTENT
                        } else {
                            intent.action = Intent.ACTION_OPEN_DOCUMENT
                        }
                        startActivityForResult(intent, REQUEST_CODE_PICTURE_PICK)
                    }
                })
    }

    fun startActivity(picture: Picture, level: Int) {
        if (level == DrawerActivity.LEVEL_5) {
            val intent = Intent(this, Drawer4Activity::class.java)
            intent.putExtra(DrawerActivity.LEVEL, level)
            intent.putExtra(DrawerActivity.PICTURE, picture)
            startActivity(intent)
        } else {
            val intent = Intent(this, DrawerActivity::class.java)
            intent.putExtra(DrawerActivity.LEVEL, level)
            intent.putExtra(DrawerActivity.PICTURE, picture)
            startActivity(intent)
        }
    }
}

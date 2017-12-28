package com.sunc.cube.ui

import android.content.Intent
import android.os.Bundle

import com.sunc.cube.R
import com.sunc.cube.eventbus.NotifyType
import com.sunc.cube.utils.TranslucentStatusCompat
import kotlinx.android.synthetic.main.activity_start.*

class StartActivity : BaseActivity() {
    override fun onNotify(type: NotifyType?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        TranslucentStatusCompat.requestTranslucentStatus(this)
        setContentView(R.layout.activity_start)
        init()
    }

    fun init() {
        iv_1.setOnClickListener { view ->
            startActivity(DrawerActivity.LEVEL_1)
        }
        iv_2.setOnClickListener { view ->
            startActivity(DrawerActivity.LEVEL_2)
        }
        iv_3.setOnClickListener { view ->
            startActivity(DrawerActivity.LEVEL_3)
        }
        iv_4.setOnClickListener { view ->
            startActivity(DrawerActivity.LEVEL_4)
        }
        iv_5.setOnClickListener { view ->
            startActivity(DrawerActivity.LEVEL_5)
        }
    }

    /**
     * 打开拼图界面
     * @param level 难度系数
     * */
    fun startActivity(level: Int) {
        val intent = Intent(this, ListActivity::class.java)
        intent.putExtra(DrawerActivity.LEVEL, level)
        startActivity(intent)
    }
}

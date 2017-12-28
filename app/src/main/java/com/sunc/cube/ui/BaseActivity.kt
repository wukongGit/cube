package com.sunc.cube.ui

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.sunc.cube.eventbus.EventBus
import com.sunc.cube.eventbus.NotifyType
import com.umeng.analytics.MobclickAgent

abstract class BaseActivity : AppCompatActivity(), NotifyType.INotify {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        EventBus.bus().register(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.bus().unregister(this)
    }

    override fun onResume() {
        super.onResume()
        MobclickAgent.onResume(this)
    }

    override fun onPause() {
        super.onPause()
        MobclickAgent.onPause(this)
    }

}

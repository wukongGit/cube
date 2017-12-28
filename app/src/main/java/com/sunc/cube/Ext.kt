package com.sunc.cube

import android.content.Context
import android.widget.Toast

/**
 * Created by Administrator on 2017/10/24.
 */
fun Context.toast(message:String, len:Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, message, len).show()
}

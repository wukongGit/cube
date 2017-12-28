package com.sunc.cube.utils

import android.content.Context
import com.sunc.cube.R

/**
 * Created by Administrator on 2017/10/27.
 */
object TimeUtil {
    fun getTimeDiscription(context: Context, str: String): String {
        val array = str.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        when(array.size) {
            1 -> return str + context.getString(R.string.second)
            2 -> return array[0] + context.getString(R.string.minute) + array[1] + context.getString(R.string.second)
            3 -> return array[0] + context.getString(R.string.hour) + array[1] + context.getString(R.string.minute) + array[2] + context.getString(R.string.second)
        }
        return str
    }

    fun getTimeSecond(context: Context, str: String): Int {
        val array = str.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        when(array.size) {
            1 -> return str.toInt()
            2 -> return array[0].toInt() * 60  + array[1].toInt()
            3 -> return array[0].toInt() * 3600 + array[1].toInt() * 60 + array[2].toInt()
        }
        return 0
    }
}
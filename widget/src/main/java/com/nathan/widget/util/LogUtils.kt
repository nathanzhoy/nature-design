package com.nathan.widget.util

import android.util.Log

object LogUtils {

    fun i(tag: String, msg: String, err: Throwable? = null) {
        Log.i(tag, msg, err)
    }

    fun e(tag: String, msg: String, err: Throwable? = null) {
        Log.e(tag, msg, err)
    }
}
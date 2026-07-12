package com.nathan.widget.app.util

import android.app.Activity
import android.content.ComponentName
import android.content.Context
import android.content.Intent

fun Context.startActivity(clazz: Class<*>) {
    startActivity(Intent(this, clazz).also {
        if (this !is Activity) {
            it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
    })
}

fun Context.startActivity(component: ComponentName) {
    startActivity(Intent().also {
        it.component = component
        if (this !is Activity) {
            it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
    })
}
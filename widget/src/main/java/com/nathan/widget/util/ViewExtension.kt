package com.nathan.widget.util

import android.view.View

fun View.gone() {
    visibility = View.GONE
}

fun View.visible() {
    visibility = View.VISIBLE
}

fun View.visibility(show: Boolean) {
    visibility = if (show) View.VISIBLE else View.GONE
}
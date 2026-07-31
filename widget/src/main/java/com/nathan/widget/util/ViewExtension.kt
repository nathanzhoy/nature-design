package com.nathan.widget.util

import android.view.View
import com.nathan.widget.R

fun View.gone() {
    visibility = View.GONE
}

fun View.visible() {
    visibility = View.VISIBLE
}

fun View.visibility(show: Boolean) {
    visibility = if (show) View.VISIBLE else View.GONE
}

private const val ALPHA_DISABLE = 0.35F

/**
 * 禁用控件时的透明度，防止写太多的Drawable，请勿过度依赖
 * 请以selector enable=true/false为主要方案
 *
 * @param enable 是否可用
 */
fun View.enable(enable: Boolean) {
    isEnabled = enable
    if (enable) {
        // 获取原始透明度
        val originAlpha = getTag(R.id.na_key_enable_alpha) as? Float ?: return
        // 修改透明度
        alpha = originAlpha
    } else {
        // 获取原始透明度
        val originAlpha = getTag(R.id.na_key_enable_alpha) as? Float ?: alpha
        // 储存透明度
        setTag(R.id.na_key_enable_alpha, originAlpha)
        // 修改透明度
        alpha = ALPHA_DISABLE * originAlpha
    }
}
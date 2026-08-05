package com.nathan.widget.ui.view

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatSeekBar
import com.nathan.widget.util.PressAnimUtils

class NatureSeekBar @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defAttr: Int = android.R.attr.seekBarStyle
) : AppCompatSeekBar(context, attrs, defAttr) {

    init {
        PressAnimUtils(this)
    }
}
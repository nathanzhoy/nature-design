package com.nathan.widget.ui.view

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.SwitchCompat
import com.nathan.widget.util.PressAnimUtils

class NatureSwitch @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defAttr: Int = android.R.attr.switchStyle
) : SwitchCompat(context, attrs, defAttr) {

    init {
        PressAnimUtils(this)
    }
}
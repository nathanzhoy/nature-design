package com.nathan.widget.ui.view

import android.content.Context
import android.util.AttributeSet
import com.google.android.material.button.MaterialButton
import com.nathan.widget.R
import com.nathan.widget.util.PressAnimUtils
import com.nathan.widget.util.enable

class NatureButton @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defAttr: Int = R.attr.natureButtonStyle
) : MaterialButton(context, attrs, defAttr) {

    private val defStyle = R.style.Widget_Nature_Button

    init {
        PressAnimUtils(this)
    }
}
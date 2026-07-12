package com.nathan.widget.ui

import android.content.Context
import android.util.AttributeSet
import com.google.android.material.button.MaterialButton
import com.nathan.widget.R
import com.nathan.widget.util.PressAnimUtils

class Button @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defAttr: Int = R.attr.button
) : MaterialButton(context, attributeSet, defAttr) {

    private val defStyle = R.style.Widget_Nature_Button

    init {
        PressAnimUtils(this)
    }
}
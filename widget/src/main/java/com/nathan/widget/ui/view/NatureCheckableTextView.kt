package com.nathan.widget.ui.view

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatCheckedTextView
import com.nathan.widget.util.PressAnimUtils

class NatureCheckableTextView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defAttrs: Int = android.R.attr.checkedTextViewStyle
) : AppCompatCheckedTextView(context, attrs, defAttrs) {

    init {
        PressAnimUtils(this)
    }
}
package com.nathan.widget.ui.view

import android.content.Context
import android.os.Parcelable
import android.util.AttributeSet
import com.google.android.material.button.MaterialButton
import com.nathan.widget.R
import com.nathan.widget.util.PressAnimUtils

class NatureButton @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defAttr: Int = R.attr.natureButtonStyle
) : MaterialButton(context, attrs, defAttr) {

    companion object {
        private const val TAG = "NatureButton"
    }

    private val defStyle = R.style.Widget_Nature_Button

    init {
        PressAnimUtils(this)
    }

    /**
     * 启用MaterialButton的isCheck恢复，他会导致checkedButton再切换item后
     * 重建页面，默认选中的item被强制恢复为false（重建前会保存item的状态，因为切换item，默认项为false）
     */
    override fun onRestoreInstanceState(state: Parcelable?) {
        super.onRestoreInstanceState(null)
    }
}
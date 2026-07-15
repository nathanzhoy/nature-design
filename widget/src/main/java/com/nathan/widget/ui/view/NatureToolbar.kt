package com.nathan.widget.ui.view

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.appcompat.widget.AppCompatImageButton
import androidx.appcompat.widget.Toolbar
import com.nathan.widget.R
import com.nathan.widget.util.PressAnimUtils
import com.nathan.widget.util.ScreenUtils

class NatureToolbar @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defAttr: Int = R.attr.natureToolbarStyle
) : Toolbar(context, attrs, defAttr) {

    private var navigationButton: View? = null

    override fun onViewAdded(child: View) {
        super.onViewAdded(child)
        PressAnimUtils(child)
        if (child is AppCompatImageButton) {
            child.apply {
                layoutParams = (layoutParams as MarginLayoutParams).apply {
                    val size = ScreenUtils.dp2Px(context, 42F).toInt()
                    val horizontalMargin = ScreenUtils.dp2Px(context, 10F).toInt()
                    marginStart = horizontalMargin
                    marginEnd = horizontalMargin
                    width = size
                    height = size
                }
            }
            navigationButton = child
        }
    }
}

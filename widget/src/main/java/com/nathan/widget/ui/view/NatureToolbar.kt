package com.nathan.widget.ui.view

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageButton
import androidx.appcompat.widget.AppCompatTextView
import androidx.appcompat.widget.Toolbar
import com.nathan.widget.R
import com.nathan.widget.util.NatureToolbarTitleAnimUtils
import com.nathan.widget.util.PressAnimUtils

class NatureToolbar @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defAttr: Int = R.attr.natureToolbarStyle
) : Toolbar(context, attrs, defAttr) {

    private var navigationButton: View? = null
    private var titleView: TextView? = null

    override fun onViewAdded(child: View) {
        super.onViewAdded(child)
        PressAnimUtils(child)
        if (child is AppCompatTextView) {
            titleView = child
            NatureToolbarTitleAnimUtils.initAnim(child)
        }
        if (child is AppCompatImageButton) {
            child.apply {
                layoutParams = (layoutParams as MarginLayoutParams).apply {
                    val size = resources.getDimensionPixelSize(R.dimen.na_common_dp_42)
                    val horizontalMargin = resources.getDimensionPixelSize(R.dimen.na_common_dp_10)
                    marginStart = horizontalMargin
                    marginEnd = horizontalMargin
                    width = size
                    height = size
                }
            }
            navigationButton = child
        }
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        titleView?.also {
            NatureToolbarTitleAnimUtils.onAttached(it)
        }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        titleView?.also {
            NatureToolbarTitleAnimUtils.onDetached(it)
        }
    }
}

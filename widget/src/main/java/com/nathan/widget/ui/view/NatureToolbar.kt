package com.nathan.widget.ui.view

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageButton
import androidx.appcompat.widget.AppCompatTextView
import androidx.appcompat.widget.Toolbar
import com.nathan.widget.R
import com.nathan.widget.util.ToolbarTitleAnimUtils
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
            ToolbarTitleAnimUtils.initAnim(listOf(child))
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
            ToolbarTitleAnimUtils.initAnim(listOf(child))
        }
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        val views = mutableListOf<View>()
        titleView?.also {
            views.add(it)
        }
        navigationButton?.also {
            views.add(it)
        }
        ToolbarTitleAnimUtils.onAttached(views)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        val views = mutableListOf<View>()
        titleView?.also {
            views.add(it)
        }
        navigationButton?.also {
            views.add(it)
        }
        ToolbarTitleAnimUtils.onDetached(views)
    }
}

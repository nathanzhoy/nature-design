package com.nathan.widget.ui.recyclerview

import android.content.Context
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.nathan.widget.R

class NatureItemDecoration(context: Context) : RecyclerView.ItemDecoration() {

    private val space = context.resources.getDimensionPixelOffset(R.dimen.na_common_dp_5)

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        outRect.top = space
        outRect.bottom = space
    }
}
package com.nathan.widget.ui.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.widget.FrameLayout
import androidx.core.content.withStyledAttributes
import androidx.core.graphics.withClip
import com.nathan.widget.R
import com.nathan.widget.util.BezierPathUtils

class NatureSmoothCornerLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defAttr: Int = R.attr.natureSmoothCornerLayoutStyle
) : FrameLayout(context, attrs, defAttr) {

    private val path = Path()
    private val paint = Paint().apply {
        style = Paint.Style.FILL
        isAntiAlias = true
        isDither = true
    }

    private var radius = 0F
    private var color = Color.WHITE

    init {
        clipChildren = true
        context.withStyledAttributes(
            attrs,
            R.styleable.NatureSmoothCornerLayout,
            defAttr,
            R.style.Widget_Nature_SmoothCornerLayout
        ) {
            radius = getDimension(R.styleable.NatureSmoothCornerLayout_android_radius, 0F)
            color = getColor(R.styleable.NatureSmoothCornerLayout_android_color, 0)
            paint.apply {
                color = this@NatureSmoothCornerLayout.color
            }
        }
    }

    /**
     * 重写dispatchDraw，让子View在裁剪区域内绘制
     */
    override fun dispatchDraw(canvas: Canvas) {
        val width = width.toFloat()
        val height = height.toFloat()
        if (width > 0 && height > 0) {
            val effectiveRadius = radius.coerceAtMost(width.coerceAtMost(height) / 2f)
            BezierPathUtils.bezierPath(width, height, effectiveRadius, path)
            canvas.withClip(path) {
                if (color != Color.TRANSPARENT) {
                    drawPath(path, paint)
                }
                super.dispatchDraw(canvas)
            }
            return
        }
        super.dispatchDraw(canvas)
    }
}

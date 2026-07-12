package com.nathan.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.widget.FrameLayout
import androidx.core.content.withStyledAttributes
import androidx.core.graphics.withClip

class SmoothCornerLayout @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

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
            R.styleable.SmoothCornerLayout,
            R.attr.smoothCornerLayout,
            R.style.Widget_Nathan_SmoothCornerLayout
        ) {
            radius = getDimension(R.styleable.SmoothCornerLayout_android_radius, 20F)
            color = getColor(R.styleable.SmoothCornerLayout_android_color, 0)
            paint.apply {
                color = this@SmoothCornerLayout.color
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
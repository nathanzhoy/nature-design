package com.nathan.widget

import android.annotation.SuppressLint
import android.content.res.Resources
import android.content.res.TypedArray
import android.graphics.Canvas
import android.graphics.ColorFilter
import android.graphics.Paint
import android.graphics.Path
import android.graphics.PixelFormat
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import org.xmlpull.v1.XmlPullParser

@SuppressLint("PrivateApi")
class SmoothCornerDrawable : Drawable() {

    private val path = Path()
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
    }

    private var radius: Float = 0F
    private var color = 0
    private var widthCache: Float = 0F
    private var heightCache: Float = 0F
    private var attrs = intArrayOf()

    override fun draw(canvas: Canvas) {
        val bounds = bounds
        val width = bounds.width().toFloat()
        val height = bounds.height().toFloat()

        if (width <= 0 || height <= 0) return

        val effectiveRadius = radius.coerceAtMost(width.coerceAtMost(height) / 2f)

        if (widthCache != width || heightCache != height) {
            BezierPathUtils.bezierPath(width, height, effectiveRadius, path)
            widthCache = width
            heightCache = height
        }

        canvas.drawPath(path, paint)
    }

    override fun setAlpha(alpha: Int) {
        paint.alpha = alpha
        invalidateSelf()
    }

    override fun setColorFilter(colorFilter: ColorFilter?) {
        paint.colorFilter = colorFilter
        invalidateSelf()
    }

    @Deprecated("Deprecated", ReplaceWith("PixelFormat.TRANSLUCENT"))
    override fun getOpacity(): Int = PixelFormat.TRANSLUCENT

    fun setColor(color: Int) {
        paint.color = color
        invalidateSelf()
    }

    override fun canApplyTheme(): Boolean {
        return true
    }

    override fun applyTheme(t: Resources.Theme) {
        super.applyTheme(t)
        reflectResolveAttributes(t)
    }

    private fun reflectResolveAttributes(t: Resources.Theme) {
        val javaClass = t.javaClass
        val declaredMethod = javaClass.getDeclaredMethod(
            "resolveAttributes",
            IntArray::class.java,
            IntArray::class.java
        )
        declaredMethod.isAccessible = true
        val typedArray =
            declaredMethod.invoke(t, attrs, R.styleable.SmoothCornerDrawable) as TypedArray
        updateStateFromTypedArray(typedArray)
    }

    override fun inflate(
        r: Resources,
        parser: XmlPullParser,
        attrs: AttributeSet,
        theme: Resources.Theme?
    ) {
        super.inflate(r, parser, attrs, theme)
        val typedArray = theme?.obtainStyledAttributes(
            attrs,
            R.styleable.SmoothCornerDrawable,
            0,
            0
        ) ?: r.obtainAttributes(attrs, R.styleable.SmoothCornerDrawable)

        updateStateFromTypedArray(typedArray)
        typedArray.recycle()
    }

    private fun updateStateFromTypedArray(typedArray: TypedArray) {
        reflectExtractThemeAttrs(typedArray)
        radius = typedArray.getDimension(R.styleable.SmoothCornerDrawable_android_radius, radius)
        color = typedArray.getColor(R.styleable.SmoothCornerDrawable_android_color, color)
        if (color != 0) {
            setColor(color)
        }
    }

    private fun reflectExtractThemeAttrs(typedArray: TypedArray) {
        val javaClass = typedArray.javaClass
        val declaredMethod = javaClass.getDeclaredMethod("extractThemeAttrs")
        declaredMethod.isAccessible = true
        attrs = declaredMethod.invoke(typedArray) as? IntArray ?: intArrayOf()
    }
}
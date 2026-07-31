package com.nathan.widget.drawable

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
import com.nathan.widget.R
import com.nathan.widget.util.BezierPathUtils
import org.xmlpull.v1.XmlPullParser

@SuppressLint("PrivateApi")
class SmoothCornerDrawable(private var smoothState: SmoothState = SmoothState()) : Drawable() {

    companion object {
        private const val TAG = "SmoothCornerDrawable"
    }

    private var paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
    }
    private var path = Path()

    override fun draw(canvas: Canvas) {
        val state = smoothState
        val bounds = bounds
        val width = bounds.width().toFloat()
        val height = bounds.height().toFloat()

        if (width <= 0 || height <= 0) return

        val atMost = width.coerceAtMost(height) / 2F
        val effectiveRadius = state.radius.coerceAtMost(atMost)

        if (state.widthCache != width || state.heightCache != height) {
            BezierPathUtils.bezierPath(
                width, height, effectiveRadius, path
            )
            state.widthCache = width
            state.heightCache = height
        }
        // Update color
        if (state.color != 0) {
            setColor(state.color)
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

    override fun applyTheme(t: Resources.Theme) {
        super.applyTheme(t)
        reflectResolveAttributes(t)
    }

    override fun getConstantState(): ConstantState {
        return smoothState
    }

    override fun mutate(): Drawable {
        smoothState = SmoothState(smoothState)
        return this
    }

    override fun setChangingConfigurations(configs: Int) {
        super.setChangingConfigurations(configs)
    }

    override fun canApplyTheme(): Boolean {
        return true
    }

    private fun reflectResolveAttributes(t: Resources.Theme) {
        val javaClass = t.javaClass
        val declaredMethod = javaClass.getDeclaredMethod(
            "resolveAttributes",
            IntArray::class.java,
            IntArray::class.java
        )
        declaredMethod.isAccessible = true
        val state = smoothState
        val a = declaredMethod.invoke(t, state.attrs, R.styleable.SmoothCornerDrawable) as TypedArray
        updateStateFromTypedArray(a)
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

    private fun updateStateFromTypedArray(a: TypedArray) {
        reflectExtractThemeAttrs(a)
        val state = smoothState
        state.configurations = state.configurations or a.getChangingConfigurations()
        state.radius = a.getDimension(R.styleable.SmoothCornerDrawable_android_radius, state.radius)
        state.color = a.getColor(R.styleable.SmoothCornerDrawable_android_color, state.color)
    }

    private fun reflectExtractThemeAttrs(typedArray: TypedArray) {
        val javaClass = typedArray.javaClass
        val declaredMethod = javaClass.getDeclaredMethod("extractThemeAttrs")
        declaredMethod.isAccessible = true
        smoothState.attrs = declaredMethod.invoke(typedArray) as? IntArray ?: intArrayOf()
    }

    class SmoothState(origin: SmoothState? = null) : ConstantState() {

        var configurations = 0
        var attrs = intArrayOf()
        var radius: Float = 0F
        var color: Int = 0
        var widthCache: Float = 0F
        var heightCache: Float = 0F

        init {
            if (origin != null) {
                attrs = origin.attrs
                radius = origin.radius
                color = origin.color
                widthCache = origin.widthCache
                heightCache = origin.heightCache
            }
        }

        override fun getChangingConfigurations(): Int {
            return configurations
        }

        override fun newDrawable(): Drawable {
            return SmoothCornerDrawable(SmoothState(this))
        }
    }
}
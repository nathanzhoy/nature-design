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
class SmoothCornerDrawable(private var cs: SmoothCornerState = SmoothCornerState()) : Drawable() {

    companion object {
        private const val TAG = "SmoothCornerDrawable"
    }

    private val path = Path()
    private var isMutated = false

    override fun draw(canvas: Canvas) {
        val state = cs
        val bounds = bounds
        val width = bounds.width().toFloat()
        val height = bounds.height().toFloat()

        if (width <= 0 || height <= 0) return

        val atMost = width.coerceAtMost(height) / 2F
        val effectiveRadius = state.radius.coerceAtMost(atMost)

        if (state.width != width || state.height != height || path.isEmpty) {
            BezierPathUtils.bezierPath(width, height, effectiveRadius, path)
            state.width = width
            state.height = height
        }
        val color = state.color
        if (color != state.paint.color) {
            setColor(color)
        }
        canvas.drawPath(path, cs.paint)
    }

    override fun setAlpha(alpha: Int) {
        cs.paint.alpha = alpha
        invalidateSelf()
    }

    override fun setColorFilter(colorFilter: ColorFilter?) {
        cs.paint.colorFilter = colorFilter
        invalidateSelf()
    }

    @Deprecated("Deprecated", ReplaceWith("PixelFormat.TRANSLUCENT"))
    override fun getOpacity(): Int = PixelFormat.TRANSLUCENT

    fun setColor(color: Int) {
        cs.paint.color = color
        invalidateSelf()
    }

    override fun applyTheme(t: Resources.Theme) {
        super.applyTheme(t)
        reflectResolveAttributes(t)
    }

    override fun getConstantState(): ConstantState {
        return cs
    }

    override fun mutate(): Drawable {
        if (isMutated.not() && super.mutate() === this) {
            cs = SmoothCornerState(cs)
            isMutated = true
        }
        return this
    }

    override fun canApplyTheme(): Boolean {
        return true
    }

    private fun reflectResolveAttributes(t: Resources.Theme) {
        val state = cs
        if (state.attrs.isNotEmpty()) {
            val javaClass = t.javaClass
            val declaredMethod = javaClass.getDeclaredMethod(
                "resolveAttributes",
                IntArray::class.java,
                IntArray::class.java
            )
            declaredMethod.isAccessible = true
            val a = declaredMethod.invoke(t, state.attrs, R.styleable.SmoothCornerDrawable) as TypedArray
            updateStateFromTypedArray(a)
        }
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
        val state = cs
        state.radius = a.getDimension(R.styleable.SmoothCornerDrawable_android_radius, state.radius)
        state.color = a.getColor(R.styleable.SmoothCornerDrawable_android_color, state.color)
    }

    private fun reflectExtractThemeAttrs(typedArray: TypedArray) {
        val javaClass = typedArray.javaClass
        val declaredMethod = javaClass.getDeclaredMethod("extractThemeAttrs")
        declaredMethod.isAccessible = true
        cs.attrs = declaredMethod.invoke(typedArray) as? IntArray ?: intArrayOf()
    }

    class SmoothCornerState(origin: SmoothCornerState? = null) : ConstantState() {

        var paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            style = Paint.Style.FILL
        }
        var attrs = intArrayOf()
        var radius: Float = 0F
        var color: Int = 0
        var width: Float = 0F
        var height: Float = 0F

        init {
            if (origin != null) {
                paint = origin.paint
                attrs = origin.attrs
                radius = origin.radius
                color = origin.color
                width = origin.width
                height = origin.height
            }
        }

        override fun getChangingConfigurations(): Int {
            return 0
        }

        override fun newDrawable(): Drawable {
            return SmoothCornerDrawable(SmoothCornerState(this))
        }
    }
}
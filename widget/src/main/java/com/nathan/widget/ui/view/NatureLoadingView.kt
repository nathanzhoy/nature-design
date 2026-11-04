package com.nathan.widget.ui.view

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.SweepGradient
import android.util.AttributeSet
import android.view.View
import android.view.animation.LinearInterpolator
import androidx.core.content.withStyledAttributes
import com.nathan.widget.R

class NatureLoadingView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defAttr: Int = R.attr.natureLoadingViewStyle
) : View(context, attrs, defAttr) {

    private var strokeWidth = 0F
    private var startColor = Color.TRANSPARENT
    private var endColor = Color.TRANSPARENT
    private var sweepAngle = 305F
    private var startAngle = 45F
    private var cx = 0F
    private var cy = 0F

    /** 旋转一圈的时长（ms），<=0 视为 1000 */
    var rotateDuration: Long = 1000L
        set(value) {
            field = if (value <= 0L) 1000L else value
            rotateAnimator.duration = field
        }

    /** 当前旋转角度（0~360） */
    private var currentRotation = 0F

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        strokeCap = Paint.Cap.ROUND
        this.strokeWidth = this@NatureLoadingView.strokeWidth
    }

    private val rectF = RectF()
    private var shader: SweepGradient? = null

    // 无限循环旋转动画
    private val rotateAnimator = ValueAnimator.ofFloat(0F, 360F).apply {
        repeatCount = ValueAnimator.INFINITE
        interpolator = LinearInterpolator()
        duration = rotateDuration
        addUpdateListener { anim ->
            currentRotation = anim.animatedValue as Float
            invalidate()
        }
    }

    init {
        context.withStyledAttributes(
            attrs,
            R.styleable.NatureLoadingView,
            defAttr,
            R.style.Widget_Nature_LoadingView
        ) {
            strokeWidth = getDimension(R.styleable.NatureLoadingView_strokeWidth, 0F)
            setStrokeWidth(strokeWidth)
            startColor = getColor(R.styleable.NatureLoadingView_android_startColor, 0)
            setLineStartColor(startColor)
            endColor = getColor(R.styleable.NatureLoadingView_android_endColor, 0)
            setLineEndColor(endColor)
        }
    }

    private fun setLineEndColor(lineEndColor: Int) {
        this.endColor = lineEndColor
        setupShader()
    }

    private fun setLineStartColor(lineStartColor: Int) {
        this.startColor = lineStartColor
        setupShader()
    }

    private fun setStrokeWidth(strokeWidth: Float) {
        this.strokeWidth = strokeWidth
        paint.strokeWidth = strokeWidth
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        cx = w / 2F
        cy = h / 2F
        val radius = (minOf(w, h) - strokeWidth) / 2F
        rectF.set(cx - radius, cy - radius, cx + radius, cy + radius)
        setupShader()
    }

    private fun setupShader() {
        val colors = intArrayOf(endColor, startColor)
        val positions = floatArrayOf(0F, 1F)
        shader = SweepGradient(cx, cy, colors, positions)
        paint.shader = shader
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.save()
        // 关键：startAngle + currentRotation，旋转时渐变会一起跟着转
        canvas.rotate(startAngle + currentRotation, width / 2F, height / 2F)
        canvas.drawArc(rectF, strokeWidth, sweepAngle, false, paint)
        canvas.restore()
    }

    /** 开始（无限）旋转 */
    fun start() {
        if (rotateAnimator.isRunning) return
        rotateAnimator.start()
    }

    /** 停止旋转并复位到初始角度 */
    fun stop() {
        if (rotateAnimator.isRunning) {
            rotateAnimator.cancel()
        }
        currentRotation = 0F
        invalidate()
    }

    /** 当前是否在旋转 */
    val isRunning: Boolean
        get() = rotateAnimator.isRunning

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        if (!rotateAnimator.isRunning) {
            start()
        }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        // 窗口分离时停止，避免后台无谓刷新/泄漏
        if (rotateAnimator.isRunning) {
            rotateAnimator.cancel()
        }
    }
}
package com.nathan.widget

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.animation.LinearInterpolator

class PressAnimUtils(
    private val view: View,
    private val scaleDown: Float = 0.92f,       // 按下时的缩放比例，默认 0.92
    private val duration: Long = 150L,           // 动画持续时间（毫秒）
    private val interpolator: (Float) -> Float = { it } // 默认线性插值
) {

    companion object {
        private const val TAG = "PressAnimUtils"
    }

    // 当前是否处于按下缩放状态
    private var isPressed = false
    // 是否正在执行动画
    private var isAnimating = false
    // 当前的缩放动画
    private var currentAnimator: ValueAnimator? = null

    // 保存原始缩放值，用于恢复
    private var originalScaleX = 1.0f
    private var originalScaleY = 1.0f

    init {
        // 保存当前缩放值作为原始值（支持已经做过其他变换的 View）
        originalScaleX = view.scaleX
        originalScaleY = view.scaleY
        setupTouchListener()
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setupTouchListener() {
        view.setOnTouchListener { _, event ->
            Log.i(TAG, "setupTouchListener action: ${event.action}")
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    // 按下：缩小
                    animateScale(scaleDown)
                }
                MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                    // 抬起或取消：恢复
                    animateScale(1.0f)
                }
            }
            false
        }
    }

    /**
     * 执行缩放动画
     * @param targetScale 目标缩放比例
     */
    private fun animateScale(targetScale: Float) {
        // 如果已经在动画中，取消当前动画
        currentAnimator?.cancel()

        // 如果已经处于目标状态，不做任何操作
        val currentScale = view.scaleX
        if (currentScale == targetScale && !isAnimating) {
            return
        }

        // 如果是按下，标记状态；如果是恢复，重置状态
        if (targetScale == scaleDown) {
            isPressed = true
        } else {
            isPressed = false
        }

        isAnimating = true

        // 使用 ValueAnimator 从当前值动画到目标值
        val animator = ValueAnimator.ofFloat(currentScale, targetScale).apply {
            this.duration = duration
            this.interpolator = LinearInterpolator()

            addUpdateListener { animation ->
                val fraction = animation.animatedValue as Float
                view.scaleX = fraction
                view.scaleY = fraction
                // 如果需要保持 pivot 为中心，确保 pivot 在 View 中心
                view.pivotX = view.width / 2f
                view.pivotY = view.height / 2f
            }

            addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    isAnimating = false
                    currentAnimator = null
                    // 确保最终值准确
                    view.scaleX = targetScale
                    view.scaleY = targetScale
                }

                override fun onAnimationCancel(animation: Animator) {
                    isAnimating = false
                    currentAnimator = null
                }
            })

            start()
        }

        currentAnimator = animator
    }

    /**
     * 手动触发缩放（用于非触摸场景）
     */
    fun scaleTo(scale: Float, onEnd: (() -> Unit)? = null) {
        animateScale(scale)
    }

    /**
     * 重置为原始大小（带动画）
     */
    fun reset() {
        animateScale(1.0f)
    }

    /**
     * 立即重置，无动画
     */
    fun resetImmediate() {
        currentAnimator?.cancel()
        isAnimating = false
        isPressed = false
        view.scaleX = 1.0f
        view.scaleY = 1.0f
    }

    /**
     * 释放资源，移除监听
     */
    fun destroy() {
        currentAnimator?.cancel()
        currentAnimator = null
        view.setOnTouchListener(null)
        // 重置缩放
        view.scaleX = originalScaleX
        view.scaleY = originalScaleY
    }

    /**
     * 是否处于按下状态
     */
    fun isPressed(): Boolean = isPressed

    /**
     * 是否正在执行动画
     */
    fun isAnimating(): Boolean = isAnimating
}
package com.nathan.widget.util

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.view.MotionEvent
import android.view.View
import android.view.animation.DecelerateInterpolator

class PressAnimUtils(
    private val view: View,
    private val scaleRatio: Float = 0.9f,
    private val duration: Long = 200L
) {
    private var currentAnimator: AnimatorSet? = null

    init {
        view.setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    scaleView(scaleRatio)
                }
                MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                    scaleView(1.0f)
                }
            }
            false
        }
    }

    private fun scaleView(targetScale: Float) {
        currentAnimator?.cancel()

        val animatorX = ObjectAnimator.ofFloat(view, "scaleX", view.scaleX, targetScale)
        val animatorY = ObjectAnimator.ofFloat(view, "scaleY", view.scaleY, targetScale)

        currentAnimator = AnimatorSet().apply {
            playTogether(animatorX, animatorY)
            duration = this@PressAnimUtils.duration
            interpolator = DecelerateInterpolator()
            start()
        }
    }

    fun release() {
        currentAnimator?.cancel()
        currentAnimator = null
        view.setOnTouchListener(null)
        view.scaleX = 1.0f
        view.scaleY = 1.0f
    }
}
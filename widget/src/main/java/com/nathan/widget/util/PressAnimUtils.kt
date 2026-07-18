package com.nathan.widget.util

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.animation.DecelerateInterpolator

@SuppressLint("ClickableViewAccessibility")
class PressAnimUtils(
    private val view: View,
    private val scaleRatio: Float = 0.95F,
    private val duration: Long = 200L
) {

    companion object {
        private const val TAG = "PressAnimUtils"
    }

    private var currentAnimator: AnimatorSet? = null

    init {
        view.setOnTouchListener { v, event ->
            if (v.isEnabled.not() || v.isClickable.not()) {
                Log.i(TAG, "onTouch view is disabled, ignore press")
                return@setOnTouchListener false
            }
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    scaleView(scaleRatio)
                }

                MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                    scaleView(1.0F)
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
        view.scaleX = 1.0F
        view.scaleY = 1.0F
    }
}
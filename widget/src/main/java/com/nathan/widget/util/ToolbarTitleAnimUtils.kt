package com.nathan.widget.util

import android.animation.ObjectAnimator
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator

object ToolbarTitleAnimUtils {

    private const val TRANS_X = 150F
    private const val TRANS_X_DURATION = 300L

    private val INTERPOLATOR = AccelerateDecelerateInterpolator()

    private var transX: ObjectAnimator? = null

    fun initAnim(views: List<View>) {
        views.forEach {
            it.translationX = TRANS_X
        }
    }

    fun onAttached(views: List<View>) {
        views.forEach {
            it.alpha = 0F
        }
        transX = ObjectAnimator.ofFloat(views, "translationX", TRANS_X, 0F)
        val anim = transX ?: return
        anim.duration = TRANS_X_DURATION
        anim.interpolator = INTERPOLATOR
        anim.addUpdateListener { animator ->
            val value = animator.animatedValue as Float
            views.forEach {
                it.translationX = value
                it.alpha = (1 - value / TRANS_X)
            }
        }
        anim.start()
    }

    fun onDetached(views: List<View>) {
        transX = ObjectAnimator.ofFloat(views, "translationX", 0F, TRANS_X)
        val anim = transX ?: return
        anim.duration = TRANS_X_DURATION
        anim.interpolator = INTERPOLATOR
        anim.addUpdateListener { animator ->
            val value = animator.animatedValue as Float
            views.forEach {
                it.translationX = value
                it.alpha = (1 - value / TRANS_X)
            }
        }
        anim.start()
    }
}
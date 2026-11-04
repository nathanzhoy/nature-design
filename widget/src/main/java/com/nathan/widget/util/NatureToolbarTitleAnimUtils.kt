package com.nathan.widget.util

import android.animation.ObjectAnimator
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator

object NatureToolbarTitleAnimUtils {

    private const val TRANS_X = 150F
    private const val TRANS_X_DURATION = 300L

    private val INTERPOLATOR = AccelerateDecelerateInterpolator()

    private var transX: ObjectAnimator? = null

    fun initAnim(view: View) {
        view.translationX = TRANS_X
    }

    fun onAttached(view: View) {
        view.alpha = 0F
        transX = ObjectAnimator.ofFloat(view, "translationX", TRANS_X, 0F)
        val anim = transX ?: return
        anim.duration = TRANS_X_DURATION
        anim.interpolator = INTERPOLATOR
        anim.addUpdateListener {
            val value = it.animatedValue as Float
            view.translationX = value
            view.alpha = (1 - value / TRANS_X)
        }
        anim.start()
    }

    fun onDetached(view: View) {
        transX = ObjectAnimator.ofFloat(view, "translationX", 0F, TRANS_X)
        val anim = transX ?: return
        anim.duration = TRANS_X_DURATION
        anim.interpolator = INTERPOLATOR
        anim.addUpdateListener {
            val value = it.animatedValue as Float
            view.translationX = value
            view.alpha = (1 - value / TRANS_X)
        }
        anim.start()
    }
}
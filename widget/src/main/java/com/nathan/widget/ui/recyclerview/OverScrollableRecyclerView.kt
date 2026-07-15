package com.nathan.widget.ui.recyclerview

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.widget.EdgeEffect
import androidx.dynamicanimation.animation.DynamicAnimation
import androidx.dynamicanimation.animation.SpringAnimation
import androidx.dynamicanimation.animation.SpringForce
import androidx.recyclerview.widget.RecyclerView

class OverScrollableRecyclerView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defAttr: Int = 0
) : RecyclerView(context, attrs, defAttr) {

    private var onScrollListener: OnScrollListener? = null

    fun setOverScrollStateChangedListener(listener: OnScrollListener) {
        if (overScrollMode == OVER_SCROLL_NEVER) {
            Log.i(TAG, "setOverScrollStateChangedListener mode is never")
            return
        }
        onScrollListener = listener

        // 设置越界滑动监听
        val effectFactory = edgeEffectFactory
        if (effectFactory is BounceEdgeEffectFactory) {
            effectFactory.setOnOverScrollChangedListener(listener)
        }
    }

    companion object {
        private const val TAG = "OverScrollableRecyclerView"
    }

    init {
        edgeEffectFactory = BounceEdgeEffectFactory()
        addOnScrollListener(object : OnScrollListener() {

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                // 状态变化跟随RecyclerView状态
                onScrollListener?.onScrollStateChanged(recyclerView, newState)
            }
        })
    }

    class BounceEdgeEffectFactory : EdgeEffectFactory() {

        companion object {
            private const val PULL_DAMPING = 1F
            private const val ABSORB_DAMPING = 0.15F
        }

        private var onScrollListener: OnScrollListener? = null
        private var isPulling = false
        private var translationAnim: DynamicAnimation<*>? = null

        fun setOnOverScrollChangedListener(listener: OnScrollListener) {
            onScrollListener = listener
        }

        override fun createEdgeEffect(view: RecyclerView, direction: Int): EdgeEffect {
            return object : EdgeEffect(view.context) {

                private val pullingRollbackAnim =
                    SpringAnimation(view, DynamicAnimation.TRANSLATION_Y).apply {
                        setSpring(
                            SpringForce(0F).apply {
                                stiffness = SpringForce.STIFFNESS_LOW
                                dampingRatio = PULL_DAMPING
                            }
                        )
                        addUpdateListener { _, _, _ ->
                            onScrollListener?.onScrolled(
                                view,
                                view.translationX.toInt(),
                                view.translationY.toInt()
                            )
                        }
                        addEndListener { _, _, _, _ ->
                            isPulling = false
                        }
                    }

                override fun onPull(deltaDistance: Float, displacement: Float) {
                    isPulling = true
                    val sign = if (direction == DIRECTION_BOTTOM || direction == DIRECTION_RIGHT) {
                        -1
                    } else {
                        1
                    }

                    val delta = sign * view.height * deltaDistance * 0.8F
                    if (isUpDownDirection(direction)) {
                        view.translationY += delta
                    } else {
                        view.translationX += delta
                    }

                    onScrollListener?.onScrolled(
                        view,
                        view.translationX.toInt(),
                        view.translationY.toInt()
                    )
                }

                private fun isUpDownDirection(direction: Int): Boolean {
                    return direction == DIRECTION_TOP || direction == DIRECTION_BOTTOM
                }

                override fun onRelease() {
                    Log.e(TAG, "onRelease")
                    if (isPulling.not()) {
                        Log.i(TAG, "onRelease do not pull")
                        return
                    }

                    // Over pulling action
                    if (view.translationY != 0F) {
                        pullingRollbackAnim.start()
                    }
                }

                override fun onAbsorb(velocity: Int) {
                    Log.i(TAG, "onAbsorb v: $velocity")
                    // User pulling fast, consume the velocity
                    if (isPulling) {
                        Log.e(TAG, "onAbsorb triggered by pull, do not consume")
                        return
                    }

                    // 根据方向确定偏移符号
                    val sign = if (direction == DIRECTION_BOTTOM) -1 else 1
                    // 计算“继续滑动”的目标偏移量
                    val targetTranslate = sign * velocity * ABSORB_DAMPING
                    startAbsorbContinuationAnim(view, targetTranslate)
                }

                private fun startAbsorbRollbackAnim(view: RecyclerView) {
                    translationAnim = SpringAnimation(
                        view,
                        DynamicAnimation.TRANSLATION_Y
                    ).apply {
                        spring = SpringForce(0F).apply {
                            stiffness = SpringForce.STIFFNESS_LOW
                            dampingRatio = PULL_DAMPING
                        }
                        start()
                    }
                }

                private fun startAbsorbContinuationAnim(view: RecyclerView, to: Float) {
                    Log.i(TAG, "startAbsorbContinuationAnim to: $to")
                    val final = if (to > 0) {
                        to.coerceAtMost(200F)
                    } else {
                        to.coerceAtLeast(-200F)
                    }

                    translationAnim = SpringAnimation(
                        view,
                        DynamicAnimation.TRANSLATION_Y
                    ).apply {
                        spring = SpringForce(final).apply {
                            stiffness = SpringForce.STIFFNESS_HIGH
                            dampingRatio = 1F
                        }
                        addUpdateListener { _, value, _ ->
                            view.translationY = value
                        }
                        addEndListener { _, _, _, _ ->
                            startAbsorbRollbackAnim(view)
                        }
                        start()
                    }
                }
            }
        }
    }
}

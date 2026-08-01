package com.nathan.widget.util

import android.graphics.Path

object BezierPathUtils {

    private const val TAG = "BezierPathUtils"
    private const val MAX_OFFSET = 0.45F
    private const val NORMAL_OFFSET = 0.3F

    fun bezierPath(width: Float, height: Float, radius: Float, path: Path) {
        val offset = if(radius == width.coerceAtMost(height) / 2) {
            MAX_OFFSET
        } else {
            NORMAL_OFFSET
        }
        LogUtils.i(TAG, "radius: $radius, width: $width, height: $height")
        path.reset()
        path.moveTo(radius, 0f)
        path.lineTo(width - radius, 0f)
        path.cubicTo(width - radius * offset, 0f, width, radius * offset, width, radius)
        path.lineTo(width, height - radius)
        path.cubicTo(
            width,
            height - radius * offset,
            width - radius * offset,
            height,
            width - radius,
            height
        )
        path.lineTo(radius, height)
        path.cubicTo(radius * offset, height, 0f, height - radius * offset, 0f, height - radius)
        path.lineTo(0f, radius)
        path.cubicTo(0f, radius * offset, radius * offset, 0f, radius, 0f)
        path.close()
    }
}
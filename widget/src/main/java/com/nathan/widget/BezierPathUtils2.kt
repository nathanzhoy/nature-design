package com.nathan.widget

import android.graphics.Path
import android.util.Log

object BezierPathUtils2 {

    private const val TAG = "BezierPathUtils"

    private var CTRL_OFFSET_RATE = 0.3F

    fun bezierPath(width: Float, height: Float, radius: Float, path: Path) {
        path.reset()
        path.moveTo(radius, 0f)
        path.lineTo(width - radius, 0f)
        path.cubicTo(width - radius * CTRL_OFFSET_RATE, 0f, width, radius * CTRL_OFFSET_RATE, width, radius)
        path.lineTo(width, height - radius)
        path.cubicTo(
            width, height - radius * CTRL_OFFSET_RATE, width - radius * CTRL_OFFSET_RATE, height,
            width - radius, height
        )
        path.lineTo(radius, height)
        path.cubicTo(radius * CTRL_OFFSET_RATE, height, 0f, height - radius * CTRL_OFFSET_RATE, 0f, height - radius)
        path.lineTo(0f, radius)
        path.cubicTo(0f, radius * CTRL_OFFSET_RATE, radius * CTRL_OFFSET_RATE, 0f, radius, 0f)
        path.close()
    }
}
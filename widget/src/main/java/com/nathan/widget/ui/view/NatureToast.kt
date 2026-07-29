package com.nathan.widget.ui.view

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.IntDef
import androidx.core.content.ContextCompat
import com.nathan.widget.R
import com.nathan.widget.util.ScreenUtils
import com.nathan.widget.util.visibility
import kotlin.math.max

class NatureToast(
    context: Context,
    text: String = "",
    @Duration duration: Int,
    drawable: Drawable? = null
) : Toast(context) {

    @IntDef(value = [Toast.LENGTH_SHORT, Toast.LENGTH_LONG])
    @Retention(AnnotationRetention.SOURCE)
    annotation class Duration

    init {
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.na_view_toast, null)
        val title = view.findViewById<TextView>(android.R.id.title)
        val icon = view.findViewById<ImageView>(android.R.id.icon)
        title.text = text
        icon.visibility(drawable != null)
        icon.setImageDrawable(drawable)
        if (drawable != null) {
            setGravity(Gravity.CENTER, 0, 0)
            // Make it be a rectangle
            /*val layoutParams = view.layoutParams
            val height = layoutParams.height
            val width = layoutParams.width
            val length = max(width, height)
            layoutParams.width = length
            layoutParams.height = length*/
        } else {
            setGravity(
                Gravity.CENTER_HORIZONTAL or Gravity.BOTTOM,
                0,
                ScreenUtils.dp2Px(context, 50F).toInt()
            )
        }
        this.duration = duration
        this.view = view
    }

    companion object {

        fun makeText(
            context: Context,
            text: String,
            @Duration duration: Int,
            drawable: Drawable? = null
        ): NatureToast {
            return NatureToast(context, text, duration, drawable)
        }

        fun makeText(
            context: Context,
            textRes: Int,
            @Duration duration: Int,
            iconRes: Int = 0
        ): NatureToast {
            return makeText(
                context,
                context.getString(textRes),
                duration,
                ContextCompat.getDrawable(context, iconRes)
            )
        }
    }
}
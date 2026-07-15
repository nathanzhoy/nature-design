package com.nathan.widget.util

import android.content.Context
import android.content.res.Resources
import android.graphics.Rect
import android.text.TextUtils
import android.util.Log
import android.util.Size
import android.util.TypedValue
import java.util.Locale

object ScreenUtils {
    private const val TAG = "ScreenUtils"
    private const val SCREEN_SIZE_OFFSET = 0.5f

    /**
     * Screen size pixel convert to dp
     * 
     * @param context the context
     * @param pxValue size in pixel
     * @return the size in pixels
     */
    fun px2dp(context: Context, pxValue: Float): Int {
        val scale = context.resources.displayMetrics.density
        return (pxValue / scale + SCREEN_SIZE_OFFSET).toInt()
    }

    /**
     * Screen size dp convert to pixel
     * 
     * @param context the context
     * @param dpValue size in dp
     * @return the size in pixels
     */
    fun dp2Px(context: Context, dpValue: Float): Float {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dpValue,
            context.getResources().getDisplayMetrics()
        )
    }

    /**
     * Text size sp convert to pixel
     * @param sp text size
     * @return pixel
     */
    fun sp2px(sp: Float): Float {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_SP,
            sp,
            Resources.getSystem().getDisplayMetrics()
        )
    }

    /**
     * Screen size dp convert to pixel
     * 
     * @param context the context
     * @param pxValue size in sp
     * @return the size in pixels
     */
    fun px2sp(context: Context, pxValue: Float): Int {
        val fontScale = context.getResources().getDisplayMetrics().scaledDensity
        return (pxValue / fontScale + SCREEN_SIZE_OFFSET).toInt()
    }

    /**
     * Get screen size in pixels
     * 
     * @param context the context
     * @return Pair first:width, second:height
     */
    fun getScreenPx(context: Context): Size {
        val metrics = context.getApplicationContext().getResources().getDisplayMetrics()
        return Size(metrics.widthPixels, metrics.heightPixels)
    }

    /**
     * Get screen density
     * 
     * @param context the context
     * @return the density
     */
    fun getDensity(context: Context): Float {
        return context.getApplicationContext().getResources().getDisplayMetrics().density
    }

    val isRtl: Boolean
        /**
         * Determine whether RTL it is.
         * 
         * @return true is RTL otherwise not.
         */
        get() = TextUtils.getLayoutDirectionFromLocale(Locale.getDefault()) == 1

    fun getGeelyDisplayRect(displayId: Int): Rect? {
        try {
            val managerClazz = Class.forName("android.view.GeelyMultiManagerExt")
            val getInstanceMethod = managerClazz.getMethod("getInstance")
            val manager = getInstanceMethod.invoke(null)

            val getDisplayRectMethod =
                managerClazz.getMethod("getDisplayRect", Int::class.javaPrimitiveType)
            val rect = getDisplayRectMethod.invoke(manager, displayId)

            Log.i(
                TAG,
                "GeelyMultiManagerExt get display rect, display id:" + displayId + " result:" + rect
            )
            if (rect is Rect) {
                return rect
            }
        } catch (e: Exception) {
            Log.w(TAG, "Get geely display rect failed.")
            e.printStackTrace()
        }

        return null
    }
}

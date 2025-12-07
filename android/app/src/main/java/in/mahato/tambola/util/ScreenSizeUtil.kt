package `in`.mahato.tambola.util

import android.app.Activity
import android.content.Context
import android.util.DisplayMetrics
import android.view.WindowManager
import androidx.window.layout.WindowMetricsCalculator

object ScreenSizeUtil {


    /**
     * Returns screen width in pixels using WindowMetrics (modern way)
     */
    fun getScreenWidthPx(activity: Activity): Int {
        val metrics = WindowMetricsCalculator.getOrCreate()
            .computeCurrentWindowMetrics(activity)

        return metrics.bounds.width()
    }

    /**
     * Returns screen height in pixels using WindowMetrics (modern way)
     */
    fun getScreenHeightPx(activity: Activity): Int {
        val metrics = WindowMetricsCalculator.getOrCreate()
            .computeCurrentWindowMetrics(activity)

        return metrics.bounds.height()
    }

    /** Returns screen width in dp */
    fun getScreenWidthDp(context: Context): Float {
        val metrics = context.resources.displayMetrics
        return metrics.widthPixels / metrics.density
    }

    /** Returns screen height in dp */
    fun getScreenHeightDp(context: Context): Float {
        val metrics = context.resources.displayMetrics
        return metrics.heightPixels / metrics.density
    }

    /**
     * Legacy fallback (if needed) – works on all old devices
     */
    fun getScreenSizeLegacy(context: Context): Pair<Int, Int> {
        val displayMetrics = DisplayMetrics()
        val wm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        wm.defaultDisplay.getMetrics(displayMetrics)

        val width = displayMetrics.widthPixels
        val height = displayMetrics.heightPixels

        return Pair(width, height)
    }
}
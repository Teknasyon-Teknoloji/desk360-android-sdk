package com.teknasyon.desk360.helper

import android.content.Context
import android.os.Build
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager

/**
 * System bar type enum for specifying which system bar padding to apply
 */
enum class SystemBarType {
    STATUS_BAR,
    NAVIGATION_BAR
}

/**
 * Adds padding to a view for the specified system bar (status bar or navigation bar)
 *
 * @param systemBarType The type of system bar to add padding for
 */
fun View.addPaddingForSystemBar(systemBarType: SystemBarType) {
    val systemBarHeight = getSystemBarHeight(context, systemBarType)

    when (systemBarType) {
        SystemBarType.STATUS_BAR -> {
            setPadding(
                paddingLeft,
                paddingTop + systemBarHeight,
                paddingRight,
                paddingBottom
            )
        }

        SystemBarType.NAVIGATION_BAR -> {
            setPadding(
                paddingLeft,
                paddingTop,
                paddingRight,
                paddingBottom + systemBarHeight
            )
        }
    }
}

/**
 * Calculates the height of the system bar (status bar or navigation bar)
 *
 * @param context Context for accessing resources
 * @param systemBarType The type of system bar to calculate height for
 * @return The height of the system bar in pixels
 */
fun getSystemBarHeight(context: Context, systemBarType: SystemBarType): Int {
    // First try to get actual height from the system
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val windowInsets = windowManager.currentWindowMetrics.windowInsets

        return when (systemBarType) {
            SystemBarType.STATUS_BAR ->
                windowInsets.getInsetsIgnoringVisibility(WindowInsets.Type.statusBars()).top

            SystemBarType.NAVIGATION_BAR ->
                windowInsets.getInsetsIgnoringVisibility(WindowInsets.Type.navigationBars()).bottom
        }
    }

    // Fallback method for older Android versions
    val resourceId = context.resources.getIdentifier(
        when (systemBarType) {
            SystemBarType.STATUS_BAR -> "status_bar_height"
            SystemBarType.NAVIGATION_BAR -> "navigation_bar_height"
        },
        "dimen",
        "android"
    )

    return if (resourceId > 0) {
        context.resources.getDimensionPixelSize(resourceId)
    } else {
        // If all else fails, use the values from dimens.xml
        context.resources.getDimensionPixelSize(
            when (systemBarType) {
                SystemBarType.STATUS_BAR ->
                    context.resources.getIdentifier(
                        "status_bar_height",
                        "dimen",
                        context.packageName
                    )

                SystemBarType.NAVIGATION_BAR ->
                    context.resources.getIdentifier(
                        "navigation_bar_height",
                        "dimen",
                        context.packageName
                    )
            }
        )
    }
}

/**
 * Extension function to add padding for status bar
 */
fun View.addPaddingForStatusBar() {
    addPaddingForSystemBar(SystemBarType.STATUS_BAR)
}

/**
 * Extension function to add padding for navigation bar
 */
fun View.addPaddingForNavigationBar() {
    addPaddingForSystemBar(SystemBarType.NAVIGATION_BAR)
}
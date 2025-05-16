package com.teknasyon.desk360.helper

import android.view.View

/**
 * Extension function to set system bar padding for a view in XML layouts
 * This allows using the extension with View Binding by calling it directly on the view
 *
 * @param systemBarType String value "status_bar" or "navigation_bar" to determine which padding to apply
 */
fun View.setSystemBarPadding(systemBarType: String) {
    when (systemBarType.lowercase()) {
        "status_bar" -> this.addPaddingForStatusBar()
        "navigation_bar" -> this.addPaddingForNavigationBar()
    }
}
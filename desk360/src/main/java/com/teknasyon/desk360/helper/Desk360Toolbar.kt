package com.teknasyon.desk360.helper

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import androidx.appcompat.view.ContextThemeWrapper
import androidx.appcompat.widget.Toolbar
import com.teknasyon.desk360.R
import com.teknasyon.desk360.helper.Desk360Constants.currentTheme

class Desk360Toolbar : Toolbar {
    init {
        this.setBackgroundColor(Color.parseColor(Desk360Constants.currentType?.data?.general_settings?.header_background_color))
        this.setTitleTextColor(Color.parseColor(Desk360Constants.currentType?.data?.general_settings?.header_text_color))
    }

    constructor(context: Context) : super(
        if (currentTheme == 1) {
            ContextThemeWrapper(context, R.style.LightThemeToolbar)
        } else {
            ContextThemeWrapper(context, R.style.DarkThemeToolbar)
        }
    )

    constructor(context: Context, attrs: AttributeSet) : super(
        if (currentTheme == 1) {
            ContextThemeWrapper(context, R.style.LightThemeToolbar)
        } else {
            ContextThemeWrapper(context, R.style.DarkThemeToolbar)
        }, attrs
    )

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(
        if (currentTheme == 1) {
            ContextThemeWrapper(context, R.style.LightThemeToolbar)
        } else {
            ContextThemeWrapper(context, R.style.DarkThemeToolbar)
        }, attrs, defStyle
    )
}
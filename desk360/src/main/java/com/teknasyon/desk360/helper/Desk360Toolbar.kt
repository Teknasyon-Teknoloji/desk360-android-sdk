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

        when (currentTheme) {
            1, 2, 3, 5 -> {
                this.setBackgroundColor(Color.parseColor("#f7f7f7"))
                this.setTitleTextColor(Color.parseColor("#5c5c5c"))
            }
            4 -> {
                this.setBackgroundColor(Color.parseColor("#3f4b60"))
                this.setTitleTextColor(Color.parseColor("#ffffff"))
            }
            else -> {
                this.setBackgroundColor(Color.parseColor("#f7f7f7"))
                this.setTitleTextColor(Color.parseColor("#5c5c5c"))
            }
        }
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
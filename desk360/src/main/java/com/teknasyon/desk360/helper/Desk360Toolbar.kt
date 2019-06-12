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
        if (currentTheme == "light") {
            this.setBackgroundColor(Color.WHITE)
            this.setTitleTextColor(Color.parseColor("#212121"))
        } else {
            this.setBackgroundColor(Color.BLACK)
            this.setTitleTextColor(Color.WHITE)
        }
    }

    constructor(context: Context) : super(if (currentTheme == "light") {
        ContextThemeWrapper(context, R.style.LightThemeToolbar)
    } else {
        ContextThemeWrapper(context, R.style.DarkThemeToolbar)
    })

    constructor(context: Context, attrs: AttributeSet) : super(if (currentTheme == "light") {
        ContextThemeWrapper(context, R.style.LightThemeToolbar)
    } else {
        ContextThemeWrapper(context, R.style.DarkThemeToolbar)
    }, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(if (currentTheme == "light") {
        ContextThemeWrapper(context, R.style.LightThemeToolbar)
    } else {
        ContextThemeWrapper(context, R.style.DarkThemeToolbar)
    }, attrs, defStyle)
}
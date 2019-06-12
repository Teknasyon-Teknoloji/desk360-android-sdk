package com.teknasyon.desk360.helper

import android.content.Context
import android.util.AttributeSet
import android.widget.ProgressBar
import androidx.appcompat.view.ContextThemeWrapper
import com.teknasyon.desk360.R
import com.teknasyon.desk360.helper.Desk360Constants.currentTheme

class Desk360Loading(context: Context, attrs: AttributeSet) : ProgressBar(
        if (currentTheme == "light") {
            ContextThemeWrapper(context, R.style.LightAccent)
        } else {
            ContextThemeWrapper(context, R.style.DarkAccent)
        }, attrs) {
    init {
        if (currentTheme == "light") {
            this.setBackgroundResource(R.drawable.light_theme_button_bg)
        } else {
            this.setBackgroundResource(R.drawable.dark_theme_button_bg)
        }
    }
}
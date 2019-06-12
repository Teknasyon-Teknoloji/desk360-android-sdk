package com.teknasyon.desk360.helper

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import androidx.appcompat.view.ContextThemeWrapper
import com.google.android.material.textfield.TextInputLayout
import com.teknasyon.desk360.R
import com.teknasyon.desk360.helper.Desk360Constants.currentTheme

/**
 * Created by seyfullah on 27,May,2019
 *
 */

class Desk360TextInputLayout : TextInputLayout {
    init {
        if (currentTheme == "light") {
            this.boxBackgroundColor = Color.WHITE
            this.boxStrokeColor = Color.parseColor("#58b0fa")
        } else {
            this.boxStrokeColor = Color.WHITE
            this.boxBackgroundColor = Color.parseColor("#2b2b2b")
        }
    }

    constructor(context: Context) : super(
        if (currentTheme == "light") {
            ContextThemeWrapper(context, R.style.LightTextLabel)
        } else {
            ContextThemeWrapper(context, R.style.DarkTextLabel)
        }
    )

    constructor(context: Context, attrs: AttributeSet) : super(
        if (currentTheme == "light") {
            ContextThemeWrapper(context, R.style.LightTextLabel)
        } else {
            ContextThemeWrapper(context, R.style.DarkTextLabel)
        }, attrs
    )

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(
        if (currentTheme == "light") {
            ContextThemeWrapper(context, R.style.LightTextLabel)
        } else {
            ContextThemeWrapper(context, R.style.DarkTextLabel)
        }, attrs, defStyle
    )
}
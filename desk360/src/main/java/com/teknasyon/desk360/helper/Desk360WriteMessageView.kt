package com.teknasyon.desk360.helper

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.widget.EditText
import androidx.appcompat.view.ContextThemeWrapper
import com.teknasyon.desk360.R
import com.teknasyon.desk360.helper.Desk360Constants.currentTheme

class Desk360WriteMessageView : EditText {
    init {
        if (currentTheme == "light") {
            this.setTextColor(Color.parseColor("#000000"))
            this.setHintTextColor(Color.parseColor("#a6a6a8"))
        } else {
            this.setTextColor(Color.parseColor("#ffffff"))
            this.setHintTextColor(Color.parseColor("#a6a6a8"))
        }
    }

    constructor(context: Context) : super(
            if (currentTheme == "light") {
                ContextThemeWrapper(context, R.style.LightTextLabel)
            } else {
                ContextThemeWrapper(context, R.style.DarkTextLabel)
            })

    constructor(context: Context, attrs: AttributeSet) : super(
            if (currentTheme == "light") {
                ContextThemeWrapper(context, R.style.LightTextLabel)
            } else {
                ContextThemeWrapper(context, R.style.DarkTextLabel)
            }, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(
            if (currentTheme == "light") {
                ContextThemeWrapper(context, R.style.LightTextLabel)
            } else {
                ContextThemeWrapper(context, R.style.DarkTextLabel)
            }, attrs, defStyle)
}
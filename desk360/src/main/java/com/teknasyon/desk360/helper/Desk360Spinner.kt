package com.teknasyon.desk360.helper

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.widget.Spinner
import com.teknasyon.desk360.R
import com.teknasyon.desk360.helper.Desk360Constants.currentTheme

class Desk360Spinner : Spinner {
    init {
         if (currentTheme == 1) {
            this.setBackgroundColor(Color.WHITE)
            this.setBackgroundResource(R.drawable.light_theme_spinner_bg)
        } else {
            this.setBackgroundColor(Color.parseColor("#2b2b2b"))
            this.setBackgroundResource(R.drawable.dark_theme_spinner_bg)
        }
    }

    constructor(context: Context) : super(
        context
    )

    constructor(context: Context, attrs: AttributeSet) : super(
        context, attrs
    )

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(
        context, attrs, defStyle
    )
}
package com.teknasyon.desk360.themev2

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.widget.Spinner
import com.teknasyon.desk360.R
import com.teknasyon.desk360.helper.Desk360Constants

class Desk360Spinner : Spinner {

    init {

        when (Desk360Constants.currentTheme) {
            1 -> {

                this.setBackgroundColor(Color.TRANSPARENT)
                this.setBackgroundResource(R.drawable.spinner_bg_desk360_transparent)
                context.setTheme(R.style.spinnerTheme)
            }
            2 -> {
                this.setBackgroundColor(Color.WHITE)
                this.setBackgroundResource(R.drawable.spinner_bg_desk360)
                context.setTheme(R.style.spinnerTheme)
            }
            3 -> {
                this.setBackgroundColor(Color.WHITE)
                this.setBackgroundResource(R.drawable.spinner_bg_desk360)
                context.setTheme(R.style.spinnerTheme)
            }
            4 -> {
                this.setBackgroundColor(Color.WHITE)
                this.setBackgroundResource(R.drawable.spinner_bg_desk360_transparent_white_icon)
                context.setTheme(R.style.spinnerTheme)
            }
            5 -> {
                this.setBackgroundColor(Color.WHITE)
                this.setBackgroundResource(R.drawable.spinner_bg_desk360)
                context.setTheme(R.style.spinnerTheme)
            }
            else -> {
                this.setBackgroundColor(Color.WHITE)
                this.setBackgroundResource(R.drawable.spinner_bg_desk360)
                context.setTheme(R.style.spinnerTheme)

            }
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
package com.teknasyon.desk360.helper

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.widget.TextView
import com.teknasyon.desk360.helper.Desk360Constants.currentTheme

class Desk360TextTitle : TextView {
    init {

        when (currentTheme) {
            1, 2, 3, 5 -> {
                this.setTextColor(Color.parseColor("#de000000"))
            }
            4 -> {
                this.setTextColor(Color.parseColor("#a6a6a8"))
            }
            else -> {
                this.setTextColor(Color.parseColor("#de000000"))

            }
        }

    }

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(
        context,
        attrs,
        defStyle
    )
}
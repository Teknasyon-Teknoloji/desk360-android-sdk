package com.teknasyon.desk360.themev2

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.widget.TextView
import com.teknasyon.desk360.helper.Desk360Constants


class Desk360FileNameText  : TextView{
    init {

        when (Desk360Constants.currentTheme) {
            1 -> {
                this.setTextColor(Color.parseColor("#5a5a5a"))
            }
            4 -> {
                this.setTextColor(Color.parseColor("#ffffff"))
            }

            else -> {
                this.setTextColor(Color.parseColor("#5a5a5a"))
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
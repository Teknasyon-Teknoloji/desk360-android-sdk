package com.teknasyon.desk360.themev2

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import androidx.constraintlayout.widget.ConstraintLayout
import com.teknasyon.desk360.helper.Desk360Constants


class Desk360CommonBottomBar : ConstraintLayout {

    init {
        when (Desk360Constants.currentTheme) {
            1, 2, 3, 5 -> {
                this.setBackgroundColor(Color.parseColor("#71717b"))
            }
            4 -> {
                this.setBackgroundColor(Color.parseColor("#2d3543"))
            }
            else -> {
                this.setBackgroundColor(Color.parseColor("#71717b"))
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
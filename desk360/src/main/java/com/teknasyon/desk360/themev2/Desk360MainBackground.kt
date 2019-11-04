package com.teknasyon.desk360.themev2

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import androidx.constraintlayout.widget.ConstraintLayout
import com.teknasyon.desk360.helper.Desk360Constants


class Desk360MainBackground : ConstraintLayout {

    init {

        when (Desk360Constants.currentTheme) {
            1, 2, 3, 5 -> {
                this.setBackgroundColor(Color.parseColor("#f7f7f7"))
            }
            4 -> {
                this.setBackgroundColor(Color.parseColor("#3f4b60"))
            }
            else -> {
                this.setBackgroundColor(Color.parseColor("#f7f7f7"))
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
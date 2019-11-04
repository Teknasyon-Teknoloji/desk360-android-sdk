package com.teknasyon.desk360.themev2

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.widget.EditText
import com.teknasyon.desk360.helper.Desk360Constants


class Desk360EditTextMessage : EditText {
    init {

        when (Desk360Constants.currentTheme) {
            1 -> {
                this.setTextColor(Color.BLACK)
            }
            4 -> {
                this.setTextColor(Color.WHITE)
            }

            else -> {
                this.setTextColor(Color.BLACK)
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
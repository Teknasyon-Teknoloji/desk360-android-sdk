package com.teknasyon.desk360.themev2

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.widget.TextView
import com.teknasyon.desk360.helper.Desk360Constants.currentType
import com.teknasyon.desk360.helper.Desk360CustomStyle

class Desk360CommonButtonText : TextView {

    init {
        this.setTextColor(Color.parseColor(currentType?.data?.first_screen?.button_text_color))
        this.textSize = currentType?.data?.first_screen?.button_text_font_size!!.toFloat()
        this.text = Desk360CustomStyle.setButtonText(
            currentType?.data?.first_screen?.button_text!!.length,
            currentType?.data?.first_screen?.button_text
        )
    }


    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(
        context,
        attrs,
        defStyle
    )
}
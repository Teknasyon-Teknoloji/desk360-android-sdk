package com.teknasyon.desk360.themev2

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.widget.TextView
import com.teknasyon.desk360.helper.Desk360Constants
import com.teknasyon.desk360.helper.Desk360CustomStyle

class Desk360TicketDetailScreenButtonText : TextView {

    init {

        this.setTextColor(Color.parseColor(Desk360Constants.currentType?.data?.first_screen?.button_text_color))
        this.textSize = Desk360Constants.currentType?.data?.first_screen?.button_text_font_size!!.toFloat()
        this.text = Desk360CustomStyle.setButtonText(
            Desk360Constants.currentType?.data?.first_screen?.button_text!!.length,
            Desk360Constants.currentType?.data?.first_screen?.button_text
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
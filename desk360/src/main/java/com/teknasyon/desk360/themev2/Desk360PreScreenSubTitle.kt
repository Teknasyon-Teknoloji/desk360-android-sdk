package com.teknasyon.desk360.themev2

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.widget.TextView
import com.teknasyon.desk360.helper.Desk360Constants

class Desk360PreScreenSubTitle : TextView {
    init {
        this.setTextColor(Color.parseColor(Desk360Constants.currentType?.data?.create_pre_screen?.sub_title_color))
        this.text = Desk360Constants.currentType?.data?.create_pre_screen?.sub_title
        this.textSize = Desk360Constants.currentType?.data?.create_pre_screen?.sub_title_font_size!!.toFloat()
    }


    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(
        context,
        attrs,
        defStyle
    )
}
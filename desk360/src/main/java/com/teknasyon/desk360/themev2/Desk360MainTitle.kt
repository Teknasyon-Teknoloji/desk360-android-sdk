package com.teknasyon.desk360.themev2

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import com.teknasyon.desk360.helper.Desk360Constants


class Desk360MainTitle : AppCompatTextView {

    init {

        this.setTextColor(Color.parseColor(Desk360Constants.currentType?.data?.general_settings?.header_text_color))
        this.textSize = Desk360Constants.currentType?.data?.general_settings?.header_text_font_size!!.toFloat()
    }

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)


    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(
        context,
        attrs,
        defStyle
    )

}
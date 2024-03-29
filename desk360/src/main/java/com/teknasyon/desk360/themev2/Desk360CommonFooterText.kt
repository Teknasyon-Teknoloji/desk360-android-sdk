package com.teknasyon.desk360.themev2

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.View
import android.widget.TextView
import com.teknasyon.desk360.helper.Desk360SDK


class Desk360CommonFooterText : TextView {

    init {

        this.setTextColor(Color.parseColor(Desk360SDK.config?.data?.general_settings?.bottom_note_color))
        this.textSize=Desk360SDK.config?.data?.general_settings?.bottom_note_font_size!!.toFloat()

        this.text = Desk360SDK.config?.data?.first_screen?.bottom_note_text
        if (!Desk360SDK.config?.data?.first_screen?.bottom_note_is_hidden!!) {
            this.visibility = View.INVISIBLE
        } else {
            this.visibility = View.VISIBLE
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
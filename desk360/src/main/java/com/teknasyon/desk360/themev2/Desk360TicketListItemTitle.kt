package com.teknasyon.desk360.themev2

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.widget.TextView
import com.teknasyon.desk360.helper.Desk360Constants


class Desk360TicketListItemTitle : TextView {

    init {

        this.setTextColor(Color.parseColor(Desk360Constants.currentType?.data?.ticket_list_screen?.ticket_subject_color))
        this.textSize =
            Desk360Constants.currentType?.data?.ticket_list_screen?.ticket_subject_font_size!!.toFloat()
    }


    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(
        context,
        attrs,
        defStyle
    )
}
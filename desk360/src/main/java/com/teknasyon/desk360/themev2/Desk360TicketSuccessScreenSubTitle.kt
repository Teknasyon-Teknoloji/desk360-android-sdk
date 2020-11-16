package com.teknasyon.desk360.themev2

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import com.teknasyon.desk360.helper.Desk360Constants

class Desk360TicketSuccessScreenSubTitle : AppCompatTextView {

    init {
        val ticketSuccessScreen = Desk360Constants.currentType?.data?.ticket_success_screen

        this.setTextColor(Color.parseColor(ticketSuccessScreen?.sub_title_color))
        this.text = ticketSuccessScreen?.sub_title
        this.textSize =
            ticketSuccessScreen?.sub_title_font_size!!.toFloat()
    }


    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(
        context,
        attrs,
        defStyle
    )
}
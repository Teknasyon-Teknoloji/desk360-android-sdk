package com.teknasyon.desk360.themev2

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import com.teknasyon.desk360.helper.Desk360Constants

class Desk360TicketSuccessScreenDesc : AppCompatTextView {

    init {
        val ticketSuccessScreen = Desk360Constants.currentType?.data?.ticket_success_screen

        this.setTextColor(Color.parseColor(ticketSuccessScreen?.description_color))
        this.text = ticketSuccessScreen?.description
        this.textSize = ticketSuccessScreen?.description_font_size!!.toFloat()
    }


    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(
        context,
        attrs,
        defStyle
    )
}
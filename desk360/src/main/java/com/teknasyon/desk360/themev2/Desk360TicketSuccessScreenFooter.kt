package com.teknasyon.desk360.themev2

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.View
import androidx.appcompat.widget.AppCompatTextView
import com.teknasyon.desk360.helper.Desk360Constants

class Desk360TicketSuccessScreenFooter : AppCompatTextView {

    init {
        val generalSettings = Desk360Constants.currentType?.data?.general_settings

        this.setTextColor(Color.parseColor(generalSettings?.bottom_note_color))
        this.textSize = generalSettings?.bottom_note_font_size!!.toFloat()

        val ticketSuccessScreen = Desk360Constants.currentType?.data?.ticket_success_screen

        this.text = ticketSuccessScreen?.bottom_note_text

        if (ticketSuccessScreen?.bottom_note_is_hidden != true) {
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
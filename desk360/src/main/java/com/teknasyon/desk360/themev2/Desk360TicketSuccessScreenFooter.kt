package com.teknasyon.desk360.themev2

import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.util.AttributeSet
import android.view.View
import android.widget.TextView
import com.teknasyon.desk360.helper.Desk360Constants

class Desk360TicketSuccessScreenFooter : TextView {

    init {

        this.setTextColor(Color.parseColor(Desk360Constants.currentType?.data?.general_settings?.bottom_note_color))
        this.textSize=Desk360Constants.currentType?.data?.general_settings?.bottom_note_font_size!!.toFloat()

        this.text = Desk360Constants.currentType?.data?.ticket_success_screen?.bottom_note_text
        if (Desk360Constants.currentType?.data?.ticket_success_screen?.bottom_note_is_hidden!!) {
            this.visibility = View.VISIBLE
        } else {
            this.visibility = View.INVISIBLE
        }
        when (Desk360Constants.currentType?.data?.general_settings?.bottom_note_font_weight) {
            "regular" -> {
                this.setTypeface(null, Typeface.NORMAL)
            }
            "bold" -> {
                this.setTypeface(null, Typeface.BOLD)
            }
            "normal" -> {
                this.setTypeface(null, Typeface.NORMAL)
            }
            else -> {
                this.setTypeface(null, Typeface.NORMAL)
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
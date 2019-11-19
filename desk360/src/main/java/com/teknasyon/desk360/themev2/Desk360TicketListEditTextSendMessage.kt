package com.teknasyon.desk360.themev2

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.Typeface
import android.util.AttributeSet
import android.widget.EditText
import com.teknasyon.desk360.helper.Desk360Constants


class Desk360TicketListEditTextSendMessage : EditText {

    init {

        this.setTextColor(Color.parseColor(Desk360Constants.currentType?.data?.ticket_detail_screen?.write_message_text_color))
        this.textSize =
            Desk360Constants.currentType?.data?.ticket_detail_screen?.write_message_font_size!!.toFloat()
        this.hint =
            Desk360Constants.currentType?.data?.ticket_detail_screen?.write_message_place_holder_text
        this.setHintTextColor(Color.parseColor(Desk360Constants.currentType?.data?.ticket_detail_screen?.write_message_place_holder_color))
        when (Desk360Constants.currentType?.data?.ticket_detail_screen?.write_message_font_weight) {
            100 -> {
                this.setTypeface(null, Typeface.NORMAL)
            }
            200 -> {
                this.setTypeface(null, Typeface.BOLD)
            }
            300 -> {
                this.setTypeface(null, Typeface.NORMAL)
            }
            400 -> {
                this.setTypeface(null, Typeface.NORMAL)
            }
            500 -> {
                this.setTypeface(null, Typeface.BOLD)
            }
            600 -> {
                this.setTypeface(null, Typeface.NORMAL)
            }
            700 -> {
                this.setTypeface(null, Typeface.NORMAL)
            }
            800 -> {
                this.setTypeface(null, Typeface.BOLD)
            }
            900 -> {
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
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
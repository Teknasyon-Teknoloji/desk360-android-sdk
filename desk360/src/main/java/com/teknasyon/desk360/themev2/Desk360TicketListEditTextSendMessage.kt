package com.teknasyon.desk360.themev2

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import androidx.appcompat.R
import androidx.appcompat.widget.AppCompatEditText
import com.teknasyon.desk360.helper.Desk360Constants
import com.teknasyon.desk360.helper.toTypeFace


class Desk360TicketListEditTextSendMessage @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = R.attr.editTextStyle
) : AppCompatEditText(context, attrs, defStyle) {

    init {
        val style = Desk360Constants.currentType?.data?.ticket_detail_screen

        setTextColor(Color.parseColor(style?.write_message_text_color))
        setHintTextColor(Color.parseColor(style?.write_message_place_holder_color))

        style?.write_message_font_size?.toFloat()?.let { textSize = it }

        hint = style?.write_message_place_holder_text

        typeface = style?.write_message_font_weight.toTypeFace(context)
    }
}
package com.teknasyon.desk360.themev2

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import com.teknasyon.desk360.helper.Desk360Constants
import com.teknasyon.desk360.helper.toTypeFace

class Desk360SentAttachmentText @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = android.R.attr.textViewStyle
) : AppCompatTextView(context, attrs, defStyle) {

    init {
        val ticketDetailScreen = Desk360Constants.currentType?.data?.ticket_detail_screen

        this.setTextColor(Color.parseColor(ticketDetailScreen?.chat_receiver_text_color))
        this.setLinkTextColor(Color.parseColor(ticketDetailScreen?.chat_receiver_text_color))

        ticketDetailScreen?.chat_sender_font_size?.toFloat()?.let {
            this.textSize = it
        }

        typeface = ticketDetailScreen?.chat_receiver_font_weight.toTypeFace(context)
    }
}
package com.teknasyon.desk360.themev2

import android.content.Context
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import com.teknasyon.desk360.R
import com.teknasyon.desk360.helper.Desk360Constants
import com.teknasyon.desk360.helper.toTypeFace

class Desk360IncomingText @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = android.R.attr.textViewStyle
) : AppCompatTextView(context, attrs, defStyle) {

    init {
        val ticketDetailScreen = Desk360Constants.currentType?.data?.ticket_detail_screen

        setBackgroundResource(
            when (ticketDetailScreen?.chat_box_style) {
                1 -> R.drawable.incoming_message_layout_type1
                2 -> R.drawable.incoming_message_layout_type2
                3 -> R.drawable.incoming_message_layout_type3
                4 -> R.drawable.incoming_message_layout_type4
                else -> R.drawable.incoming_message_layout_type1
            }
        )

        if (ticketDetailScreen?.chat_sender_shadow_is_hidden == true) {
            elevation = 20f
        }

        background.colorFilter = PorterDuffColorFilter(
            Color.parseColor(ticketDetailScreen?.chat_sender_background_color),
            PorterDuff.Mode.SRC_ATOP
        )

        setTextColor(Color.parseColor(ticketDetailScreen?.chat_sender_text_color))
        setLinkTextColor(Color.parseColor(ticketDetailScreen?.chat_sender_text_color))

        ticketDetailScreen?.chat_sender_font_size?.toFloat()?.let { size -> textSize = size }

        typeface = ticketDetailScreen?.chat_sender_font_weight.toTypeFace(context)
    }
}
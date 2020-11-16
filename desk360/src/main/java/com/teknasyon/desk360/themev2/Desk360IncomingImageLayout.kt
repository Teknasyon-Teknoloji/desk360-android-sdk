package com.teknasyon.desk360.themev2

import android.content.Context
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.util.AttributeSet
import android.widget.LinearLayout
import com.teknasyon.desk360.R
import com.teknasyon.desk360.helper.Desk360Constants

class Desk360IncomingImageLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : LinearLayout(context, attrs, defStyle) {

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

        background.colorFilter = PorterDuffColorFilter(
            Color.parseColor(ticketDetailScreen?.chat_sender_background_color),
            PorterDuff.Mode.SRC_ATOP
        )
    }
}
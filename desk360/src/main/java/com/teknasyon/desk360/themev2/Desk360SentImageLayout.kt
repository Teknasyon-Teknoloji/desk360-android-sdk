package com.teknasyon.desk360.themev2

import android.content.Context
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.util.AttributeSet
import androidx.constraintlayout.widget.ConstraintLayout
import com.teknasyon.desk360.R
import com.teknasyon.desk360.helper.Desk360Constants

class Desk360SentImageLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : ConstraintLayout(context, attrs, defStyle) {

    init {
        val ticketDetailScreen = Desk360Constants.currentType?.data?.ticket_detail_screen

        setBackgroundResource(
            when (ticketDetailScreen?.chat_box_style) {
                1 -> R.drawable.sent_message_layout_type1
                2 -> R.drawable.sent_message_layout_type2
                3 -> R.drawable.sent_message_layout_type3
                4 -> R.drawable.sent_message_layout_type4
                else -> R.drawable.sent_message_layout_type1
            }
        )

        background.colorFilter = PorterDuffColorFilter(
            Color.parseColor(ticketDetailScreen?.chat_receiver_background_color),
            PorterDuff.Mode.SRC_ATOP
        )
    }
}
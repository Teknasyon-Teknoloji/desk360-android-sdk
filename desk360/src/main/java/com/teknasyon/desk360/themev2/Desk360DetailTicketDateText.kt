package com.teknasyon.desk360.themev2

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import com.teknasyon.desk360.helper.Desk360Constants

class Desk360DetailTicketDateText @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = android.R.attr.textViewStyle
) : AppCompatTextView(context, attrs, defStyle) {

    init {
        val ticketDetailScreen = Desk360Constants.currentType?.data?.ticket_detail_screen
        this.setTextColor(Color.parseColor(ticketDetailScreen?.chat_receiver_date_color))
    }
}
package com.teknasyon.desk360.themev2

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import com.teknasyon.desk360.helper.Desk360Constants

class Desk360CurrentEmptyTicketSubTitle @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = android.R.attr.textViewStyle
) : AppCompatTextView(context, attrs, defStyle) {

    init {
        val ticketListScreen = Desk360Constants.currentType?.data?.ticket_list_screen
        this.setTextColor(Color.parseColor(ticketListScreen?.tab_text_color))
        this.text = ticketListScreen?.ticket_list_empty_current_text
    }
}
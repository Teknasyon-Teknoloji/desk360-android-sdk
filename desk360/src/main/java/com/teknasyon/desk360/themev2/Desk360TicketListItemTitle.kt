package com.teknasyon.desk360.themev2

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import com.teknasyon.desk360.helper.Desk360SDK

class Desk360TicketListItemTitle : AppCompatTextView {

    init {
        Desk360SDK.config?.data?.let { data ->
            this.setTextColor(Color.parseColor(data.ticket_list_screen?.ticket_subject_color))
            data.ticket_list_screen?.ticket_subject_font_size?.let { size ->
                this.textSize = size.toFloat()
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
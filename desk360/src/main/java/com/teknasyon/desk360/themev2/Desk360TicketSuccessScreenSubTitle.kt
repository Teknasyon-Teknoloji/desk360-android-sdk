package com.teknasyon.desk360.themev2

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import com.teknasyon.desk360.helper.Desk360SDK

class Desk360TicketSuccessScreenSubTitle : AppCompatTextView {

    init {
        Desk360SDK.config?.data.let { data ->
            this.setTextColor(Color.parseColor(data?.ticket_success_screen?.sub_title_color))
            this.text = data?.ticket_success_screen?.sub_title
            data?.ticket_success_screen?.sub_title_font_size?.let { size ->
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
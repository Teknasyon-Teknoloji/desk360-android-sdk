package com.teknasyon.desk360.themev2

import android.content.Context
import android.util.AttributeSet
import android.widget.ImageView
import com.teknasyon.desk360.R
import com.teknasyon.desk360.helper.Desk360Constants

class Desk360TicketListSendMessageIcon  : ImageView{

    init {

        when (Desk360Constants.currentTheme) {
            1,2,3,5-> {
                this.setImageResource(R.drawable.message_send_icon_blue)
            }
            4 -> {
                this.setImageResource(R.drawable.message_send_icon_white)
            }

            else -> {
                this.setImageResource(R.drawable.message_send_icon_blue)
            }
        }
    }

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle)
}
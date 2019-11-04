package com.teknasyon.desk360.themev2

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.widget.LinearLayout
import com.teknasyon.desk360.helper.Desk360Constants


class Desk360TicketListMessageLayout : LinearLayout{

    init {

        when (Desk360Constants.currentTheme) {
            1,2,3,5-> {
                this.setBackgroundColor(Color.WHITE)
            }
            4 -> {
                this.setBackgroundColor(Color.parseColor("#505c70"))
            }

            else -> {
                this.setBackgroundColor(Color.WHITE)
            }
        }
    }

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle)
}
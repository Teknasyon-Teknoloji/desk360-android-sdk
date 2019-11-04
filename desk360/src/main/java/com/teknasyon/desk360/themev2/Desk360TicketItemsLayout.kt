package com.teknasyon.desk360.themev2

import android.content.Context
import android.util.AttributeSet
import androidx.constraintlayout.widget.ConstraintLayout
import com.teknasyon.desk360.R

import com.teknasyon.desk360.helper.Desk360Constants

class Desk360TicketItemsLayout  : ConstraintLayout{

    init {

        when (Desk360Constants.currentTheme) {
            1, 2, 3, 5 -> {

                this.setBackgroundResource(R.drawable.desk360_bg_ticket_items_white)
            }
            4 -> {
                this.setBackgroundResource(R.drawable.desk360_ticket_blue_bg)
            }
            else -> {
                this.setBackgroundResource(R.drawable.desk360_bg_ticket_items_white)

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
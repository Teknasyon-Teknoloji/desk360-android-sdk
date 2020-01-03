package com.teknasyon.desk360.themev2

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.util.AttributeSet
import android.widget.LinearLayout
import com.teknasyon.desk360.helper.Desk360Constants


class Desk360TicketListMessageLayout : LinearLayout {

    private val gradientDrawable = GradientDrawable()

    init {

        gradientDrawable.setColor(Color.parseColor(Desk360Constants.currentType?.data?.ticket_detail_screen?.write_message_background_color))
        gradientDrawable.setStroke(
            2,
            Color.parseColor(Desk360Constants.currentType?.data?.ticket_detail_screen?.write_message_border_color)
        )
        gradientDrawable.cornerRadius = 1f
        
        this.background = gradientDrawable
    }

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(
        context,
        attrs,
        defStyle
    )
}
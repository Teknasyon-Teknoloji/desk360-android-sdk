package com.teknasyon.desk360.themev2

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import androidx.recyclerview.widget.RecyclerView
import com.teknasyon.desk360.helper.Desk360Constants

class Desk360TicketRecyclerView : RecyclerView {

    init {

        this.setBackgroundColor(Color.parseColor(Desk360Constants.currentType?.data?.ticket_detail_screen?.chat_background_color))

    }

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(
        context,
        attrs,
        defStyle
    )
}
package com.teknasyon.desk360.themev2

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import androidx.recyclerview.widget.RecyclerView
import com.teknasyon.desk360.helper.Desk360Constants

class Desk360TicketRecyclerView : RecyclerView {

    init {

        when (Desk360Constants.currentTheme) {
            1, 2, 3, 5 -> {
                this.setBackgroundColor(Color.parseColor("#eeeff0"))
            }
            4 -> {
                this.setBackgroundColor(Color.parseColor("#4a576d"))
            }
            else -> {
                this.setBackgroundColor(Color.parseColor("#eeeff0"))
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
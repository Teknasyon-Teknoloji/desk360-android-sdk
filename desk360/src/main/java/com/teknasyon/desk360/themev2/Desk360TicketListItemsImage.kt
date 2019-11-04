package com.teknasyon.desk360.themev2

import android.content.Context
import android.util.AttributeSet
import android.widget.ImageView

import com.teknasyon.desk360.R
import com.teknasyon.desk360.helper.Desk360Constants

class Desk360TicketListItemsImage : ImageView {

    init {

        when (Desk360Constants.currentTheme) {
            1, 2, 3, 5 -> {
                this.setBackgroundResource(R.drawable.mavi_zarf_icon)
            }
            4 -> {
                this.setBackgroundResource(R.drawable.zarf)
            }
            else -> {
                this.setBackgroundResource(R.drawable.mavi_zarf_icon)

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
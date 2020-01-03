package com.teknasyon.desk360.themev2

import android.content.Context
import android.graphics.Color
import android.graphics.PorterDuff
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import com.teknasyon.desk360.R
import com.teknasyon.desk360.helper.Desk360Constants

class Desk360MessageDetailImage : ImageView {


    init {

        when (Desk360Constants.currentType?.data?.ticket_detail_screen?.chat_box_style) {
            4 -> {
                this.setImageResource(R.drawable.sent_small_icon)
                this.visibility = View.VISIBLE

            }
            else -> {
                this.setBackgroundResource(0)
                this.visibility = View.GONE
            }

        }

        this.setColorFilter(
            Color.parseColor(Desk360Constants.currentType?.data?.ticket_detail_screen?.chat_receiver_background_color),
            PorterDuff.Mode.SRC_ATOP
        )

    }


    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(
        context,
        attrs,
        defStyle
    )

}
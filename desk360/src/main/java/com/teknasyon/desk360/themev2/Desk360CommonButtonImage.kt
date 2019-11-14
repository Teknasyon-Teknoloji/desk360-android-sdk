package com.teknasyon.desk360.themev2

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView

import com.teknasyon.desk360.R
import com.teknasyon.desk360.helper.Desk360Constants

class Desk360CommonButtonImage : ImageView {

    init {

        when (Desk360Constants.currentType?.data?.first_screen?.button_style_id) {
            1, 3, 4 -> {
                this.setBackgroundResource(R.drawable.arrow_right)
                this.visibility = View.VISIBLE
            }
            2 -> {
                this.setBackgroundColor(Color.parseColor(Desk360Constants.currentType?.data?.first_screen?.button_background_color))
                this.visibility = View.GONE
            }

            5 -> {
                this.setBackgroundResource(R.drawable.arrow_right_blue)
                this.visibility = View.VISIBLE
            }
            else -> {
                this.setBackgroundResource(R.drawable.arrow_right)
                this.visibility = View.VISIBLE
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
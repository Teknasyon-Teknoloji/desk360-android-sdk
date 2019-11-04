package com.teknasyon.desk360.themev2

import android.content.Context
import android.util.AttributeSet
import android.widget.ImageView
import com.teknasyon.desk360.R
import com.teknasyon.desk360.helper.Desk360Constants

class Desk360CloseImage : ImageView {

    init {

        when (Desk360Constants.currentTheme) {
            1, 2, 3, 5 -> {
                this.setBackgroundResource(R.drawable.close_button_desk)
            }
            4 -> {
                this.setBackgroundResource(R.drawable.close_button_desk_white)

            }
            else -> {
                this.setBackgroundResource(R.drawable.close_button_desk)

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
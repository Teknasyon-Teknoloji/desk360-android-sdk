package com.teknasyon.desk360.themev2

import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.util.AttributeSet
import android.widget.TextView
import com.teknasyon.desk360.R
import com.teknasyon.desk360.helper.Desk360Constants.currentType

class Desk360CommonButtonText : TextView {

    init {
        this.setTextColor(Color.parseColor(currentType?.data?.first_screen?.button_text_color))
        this.text = currentType?.data?.first_screen?.button_text
        this.textSize = currentType?.data?.first_screen?.button_text_font_size!!.toFloat()
        when (currentType?.data?.first_screen?.button_text_font_weight) {
            100 -> {
                this.setTypeface(null, Typeface.NORMAL)
            }
            200 -> {
                this.setTypeface(null, Typeface.BOLD)
            }
            300 -> {
                this.setTypeface(null, Typeface.NORMAL)
            }
            400 -> {
                this.setTypeface(null, Typeface.NORMAL)
            }
            500 -> {
                this.setTypeface(null, Typeface.BOLD)
            }
            600 -> {
                this.setTypeface(null, Typeface.NORMAL)
            }
            700 -> {
                this.setTypeface(null, Typeface.NORMAL)
            }
            800 -> {
                this.setTypeface(null, Typeface.BOLD)
            }
            900 -> {
                this.setTypeface(null, Typeface.NORMAL)
            }
            else -> {
                this.setTypeface(null, Typeface.NORMAL)
            }
        }

        if (currentType?.data?.first_screen?.button_icon_is_hidden != true) {
            if (currentType?.data?.first_screen?.button_style_id == 5) {
                this.setCompoundDrawablesWithIntrinsicBounds(R.drawable.mavi_zarf_icon, 0, 0, 0)

            } else {
                this.setCompoundDrawablesWithIntrinsicBounds(R.drawable.zarf, 0, 0, 0)
            }
        } else {
            this.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)
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
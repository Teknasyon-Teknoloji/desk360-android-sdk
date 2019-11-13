package com.teknasyon.desk360.themev2

import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.util.AttributeSet
import android.widget.TextView
import com.teknasyon.desk360.R
import com.teknasyon.desk360.helper.Desk360Constants

class Desk360ButtonReturnMessages : TextView {
    init {

        this.setTextColor(Color.parseColor(Desk360Constants.currentType?.data?.ticket_success_screen?.button_text_color))
        this.text = Desk360Constants.currentType?.data?.ticket_success_screen?.button_text
        this.textSize =
            Desk360Constants.currentType?.data?.ticket_success_screen?.button_text_font_size!!.toFloat()
        when (Desk360Constants.currentType?.data?.ticket_success_screen?.button_text_font_weight) {
            //TODO sabir
            "regular" -> {
                this.setTypeface(null, Typeface.NORMAL)
            }
            "bold" -> {
                this.setTypeface(null, Typeface.BOLD)
            }
            "normal" -> {
                this.setTypeface(null, Typeface.NORMAL)
            }
            else -> {
                this.setTypeface(null, Typeface.NORMAL)
            }
        }

        if (Desk360Constants.currentType?.data?.ticket_success_screen?.button_icon_is_hidden == true) {
            if (Desk360Constants.currentType?.data?.ticket_success_screen?.button_style_id == 5) {
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
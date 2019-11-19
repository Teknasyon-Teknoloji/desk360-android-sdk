package com.teknasyon.desk360.themev2

import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.util.AttributeSet
import android.view.View
import android.widget.TextView
import com.teknasyon.desk360.helper.Desk360Constants


class Desk360CommonFooterText : TextView {

    init {

        this.setTextColor(Color.parseColor(Desk360Constants.currentType?.data?.general_settings?.bottom_note_color))
        this.textSize=Desk360Constants.currentType?.data?.general_settings?.bottom_note_font_size!!.toFloat()

        this.text = Desk360Constants.currentType?.data?.first_screen?.bottom_note_text
        if (Desk360Constants.currentType?.data?.first_screen?.bottom_note_is_hidden!!) {
            this.visibility = View.VISIBLE
        } else {
            this.visibility = View.INVISIBLE
        }
        when (Desk360Constants.currentType?.data?.general_settings?.bottom_note_font_weight) {
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
    }


    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(
        context,
        attrs,
        defStyle
    )
}
package com.teknasyon.desk360.themev2

import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.util.AttributeSet
import android.widget.TextView
import com.teknasyon.desk360.helper.Desk360Constants


class Desk360MainTitle : TextView {

    init {

        this.setTextColor(Color.parseColor(Desk360Constants.currentType?.data?.general_settings?.header_text_color))
        this.textSize =
            Desk360Constants.currentType?.data?.general_settings?.header_text_font_size!!.toFloat()
        when (Desk360Constants.currentType?.data?.general_settings?.header_text_font_weight) {
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

    }

    constructor(context: Context) : super(context)
//    {
//        val face = Typeface.createFromAsset(context.assets, "Gotham-Book.ttf")
//        this.typeface = face
//    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)
//    {
//        val face = Typeface.createFromAsset(context.assets, "Gotham-Book.ttf")
//        this.typeface = face
//    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(
        context,
        attrs,
        defStyle
    )
//    {
//        val face = Typeface.createFromAsset(context.assets, "Gotham-Book.ttf")
//        this.typeface = face
//    }
}
package com.teknasyon.desk360.themev2

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.widget.TextView
import com.teknasyon.desk360.helper.Desk360Constants


class Desk360MainTitle : TextView {

    init {
        when (Desk360Constants.currentTheme) {
            1, 2, 3, 5 -> {
                this.setTextColor(Color.parseColor("#5c5c5c"))
            }
            4 -> {
                this.setTextColor(Color.parseColor("#ffffff"))
            }
            else -> {
                this.setTextColor(Color.parseColor("#5c5c5c"))
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
package com.teknasyon.desk360.themev2

import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.GradientDrawable
import android.util.AttributeSet
import android.util.DisplayMetrics
import android.widget.Button
import com.teknasyon.desk360.helper.Desk360Constants

class Desk360SendFormButton : Button {

    private val gradientDrawable = GradientDrawable()

    init {

        this.setTextColor(Color.parseColor(Desk360Constants.currentType?.data?.ticket_detail_screen?.button_text_color))
        this.text = Desk360Constants.currentType?.data?.ticket_detail_screen?.button_text
        this.textSize =
            Desk360Constants.currentType?.data?.ticket_detail_screen?.button_text_font_size!!.toFloat()
        gradientDrawable.setColor(Color.parseColor(Desk360Constants.currentType?.data?.ticket_detail_screen?.button_background_color))
        gradientDrawable.setStroke(
            2,
            Color.parseColor(Desk360Constants.currentType?.data?.ticket_detail_screen?.button_border_color)
        )
        when (Desk360Constants.currentType?.data?.ticket_detail_screen?.button_text_font_weight) {
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

        when (Desk360Constants.currentType?.data?.ticket_detail_screen?.button_style_id) {
            1 -> {
                gradientDrawable.cornerRadius = convertDpToPixel(28f, context)
            }
            2 -> {
                gradientDrawable.cornerRadius = convertDpToPixel(10f, context)
            }
            3 -> {
                gradientDrawable.cornerRadius = convertDpToPixel(2f, context)
            }
            4 -> {
                gradientDrawable.cornerRadius = convertDpToPixel(0f, context)
            }
            else -> {
                gradientDrawable.cornerRadius = convertDpToPixel(28f, context)
            }
        }

        this.background = gradientDrawable
    }

    private fun convertDpToPixel(dp: Float, context: Context): Float {
        return dp * (context.resources.displayMetrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)
    }


    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(
        context,
        attrs,
        defStyle
    )
}
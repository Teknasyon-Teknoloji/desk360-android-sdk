package com.teknasyon.desk360.themev2

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.util.AttributeSet
import android.util.DisplayMetrics
import androidx.constraintlayout.widget.ConstraintLayout
import com.teknasyon.desk360.helper.Desk360Constants

class Desk360TicketItemsLayout : ConstraintLayout {

    private val gradientDrawable = GradientDrawable()

    init {

        gradientDrawable.setColor(Color.parseColor(Desk360Constants.currentType?.data?.ticket_list_screen?.ticket_item_backgroud_color))
        gradientDrawable.cornerRadius = convertDpToPixel(10f, context)


        when (Desk360Constants.currentType?.data?.ticket_list_screen?.ticket_list_type) {
            1 -> {
            }
            2 -> {
            }
            3 -> {
            }
            4 -> {
            }
            5 -> {
            }
            else -> {
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
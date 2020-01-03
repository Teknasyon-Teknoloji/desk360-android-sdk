package com.teknasyon.desk360.themev2

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Build
import android.util.AttributeSet
import android.util.DisplayMetrics
import androidx.constraintlayout.widget.ConstraintLayout
import com.teknasyon.desk360.helper.Desk360Constants

class Desk360TicketItemsLayout : ConstraintLayout {

    private val gradientDrawable = GradientDrawable()

    init {

        gradientDrawable.setColor(Color.parseColor(Desk360Constants.currentType?.data?.ticket_list_screen?.ticket_item_backgroud_color))

        when (Desk360Constants.currentType?.data?.ticket_list_screen?.ticket_list_type) {
            1 -> {
                gradientDrawable.cornerRadius = convertDpToPixel(10f, context)
            }
            2 -> {
                gradientDrawable.cornerRadius = convertDpToPixel(4f, context)
            }
            3 -> {
                gradientDrawable.cornerRadius = convertDpToPixel(2f, context)
            }
            4 -> {
                gradientDrawable.cornerRadius = convertDpToPixel(0f, context)
            }
            else -> {
                gradientDrawable.cornerRadius = convertDpToPixel(10f, context)
            }
        }

        if(Desk360Constants.currentType?.data?.ticket_list_screen?.ticket_item_shadow_is_hidden==true){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                this.elevation=20f
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
package com.teknasyon.desk360.themev2

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.util.AttributeSet
import android.util.DisplayMetrics
import androidx.constraintlayout.widget.ConstraintLayout
import com.teknasyon.desk360.helper.Desk360Constants

class Desk360CreateScreenButton : ConstraintLayout {
    private val gradientDrawable = GradientDrawable()

    init {
        gradientDrawable.setColor(Color.parseColor(Desk360Constants.currentType?.data?.create_screen?.button_background_color))
        gradientDrawable.setStroke(
            1,
            Color.parseColor(Desk360Constants.currentType?.data?.create_screen?.button_border_color)
        )

        when (Desk360Constants.currentType?.data?.create_screen?.button_style_id) {
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
package com.teknasyon.desk360.themev2

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.util.AttributeSet
import androidx.constraintlayout.widget.ConstraintLayout
import com.teknasyon.desk360.helper.Desk360Constants.currentType
import com.teknasyon.desk360.helper.convertDpToPixel


class Desk360CommonButton @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr, defStyleRes) {

    private val gradientDrawable = GradientDrawable()

    init {
        val firstScreen = currentType?.data?.first_screen

        gradientDrawable.setColor(Color.parseColor(firstScreen?.button_background_color))
        gradientDrawable.setStroke(2, Color.parseColor(firstScreen?.button_border_color))

        if (firstScreen?.button_shadow_is_hidden == true) {
            this.elevation = 20f
        }

        gradientDrawable.cornerRadius = when (firstScreen?.button_style_id) {
            1 -> context.convertDpToPixel(28f)
            2 -> context.convertDpToPixel(10f)
            3 -> context.convertDpToPixel(2f)
            4 -> context.convertDpToPixel(0f)
            else -> context.convertDpToPixel(28f)
        }

        this.background = gradientDrawable
    }

}
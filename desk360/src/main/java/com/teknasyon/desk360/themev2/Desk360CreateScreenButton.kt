package com.teknasyon.desk360.themev2

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.util.AttributeSet
import androidx.constraintlayout.widget.ConstraintLayout
import com.teknasyon.desk360.helper.Desk360Constants
import com.teknasyon.desk360.helper.convertDpToPixel

class Desk360CreateScreenButton @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr, defStyleRes) {

    private val gradientDrawable = GradientDrawable()

    init {
        val createScreen = Desk360Constants.currentType?.data?.create_screen

        gradientDrawable.setColor(Color.parseColor(createScreen?.button_background_color))
        gradientDrawable.setStroke(2, Color.parseColor(createScreen?.button_border_color))

        if (createScreen?.button_shadow_is_hidden == true) {
            this.elevation = 20f
        }

        gradientDrawable.cornerRadius = when (createScreen?.button_style_id) {
            1 -> context.convertDpToPixel(28f)
            2 -> context.convertDpToPixel(10f)
            3 -> context.convertDpToPixel(2f)
            4 -> context.convertDpToPixel(0f)
            else -> context.convertDpToPixel(28f)
        }

        this.background = gradientDrawable
    }
}
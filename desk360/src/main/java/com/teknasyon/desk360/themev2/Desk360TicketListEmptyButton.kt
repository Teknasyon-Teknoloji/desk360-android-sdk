package com.teknasyon.desk360.themev2

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.util.AttributeSet
import androidx.constraintlayout.widget.ConstraintLayout
import com.teknasyon.desk360.helper.Desk360Constants.currentType
import com.teknasyon.desk360.helper.convertDpToPixel

class Desk360TicketListEmptyButton : ConstraintLayout {
    private val gradientDrawable = GradientDrawable()

    init {
        gradientDrawable.setColor(Color.parseColor(currentType?.data?.first_screen?.button_text_color))
        gradientDrawable.setStroke(
            2,
            Color.parseColor(currentType?.data?.first_screen?.button_border_color)
        )

        when (currentType?.data?.first_screen?.button_style_id) {
            1 -> {
                gradientDrawable.cornerRadius = context.convertDpToPixel(28f)
            }
            2 -> {
                gradientDrawable.cornerRadius = context.convertDpToPixel(10f)
            }
            3 -> {
                gradientDrawable.cornerRadius = context.convertDpToPixel(2f)
            }
            4 -> {
                gradientDrawable.cornerRadius = context.convertDpToPixel(0f)
            }
            else -> {
                gradientDrawable.cornerRadius = context.convertDpToPixel(28f)
            }
        }

        this.background = gradientDrawable
    }


    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(
        context,
        attrs,
        defStyle
    )
}
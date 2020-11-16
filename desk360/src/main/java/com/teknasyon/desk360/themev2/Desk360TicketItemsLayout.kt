package com.teknasyon.desk360.themev2

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Build
import android.util.AttributeSet
import androidx.constraintlayout.widget.ConstraintLayout
import com.teknasyon.desk360.helper.Desk360Constants
import com.teknasyon.desk360.helper.convertDpToPixel

class Desk360TicketItemsLayout : ConstraintLayout {

    private val gradientDrawable = GradientDrawable()

    init {

        gradientDrawable.setColor(Color.parseColor(Desk360Constants.currentType?.data?.ticket_list_screen?.ticket_item_backgroud_color))

        when (Desk360Constants.currentType?.data?.ticket_list_screen?.ticket_list_type) {
            1 -> {
                gradientDrawable.cornerRadius = context.convertDpToPixel(10f)
            }
            2 -> {
                gradientDrawable.cornerRadius = context.convertDpToPixel(4f)
            }
            3 -> {
                gradientDrawable.cornerRadius = context.convertDpToPixel(2f)
            }
            4 -> {
                gradientDrawable.cornerRadius = context.convertDpToPixel(0f)
            }
            else -> {
                gradientDrawable.cornerRadius = context.convertDpToPixel(10f)
            }
        }

        if(Desk360Constants.currentType?.data?.ticket_list_screen?.ticket_item_shadow_is_hidden==true){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                this.elevation=20f
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
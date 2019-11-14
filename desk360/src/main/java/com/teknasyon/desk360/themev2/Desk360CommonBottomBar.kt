package com.teknasyon.desk360.themev2

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import androidx.constraintlayout.widget.ConstraintLayout
import com.teknasyon.desk360.helper.Desk360Constants


class Desk360CommonBottomBar : ConstraintLayout {

    init {

//      this.setBackgroundColor(Color.parseColor(Desk360Constants.currentType?.data?.general_settings?.copyright_background_color))
        this.setBackgroundColor(Color.parseColor("#71717b"))
    }


    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(
        context,
        attrs,
        defStyle
    )
}
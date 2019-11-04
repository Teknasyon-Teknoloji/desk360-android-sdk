package com.teknasyon.desk360.themev2

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import androidx.constraintlayout.widget.ConstraintLayout
import com.teknasyon.desk360.R
import com.teknasyon.desk360.helper.Desk360Constants


class Desk360CommonButton : ConstraintLayout {

    init {

        when (Desk360Constants.currentTheme) {
            1 -> {
                this.setBackgroundResource(R.drawable.button_oval_blue)
            }
            2 -> {
                this.setBackgroundColor(Color.parseColor("#58b0fa"))
            }
            3 -> {
                this.setBackgroundResource(R.drawable.button_oval_blue2)
            }
            4 -> {
                this.setBackgroundColor(Color.parseColor("#58b0fa"))
            }
            5 -> {
                this.setBackgroundResource(R.drawable.button_oval_white)
            }
            else ->{
                this.setBackgroundResource(R.drawable.button_oval_blue)
            }
        }
    }



    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle)
}
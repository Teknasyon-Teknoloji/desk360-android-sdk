package com.teknasyon.desk360.themev2

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.widget.Button

import com.teknasyon.desk360.R
import com.teknasyon.desk360.helper.Desk360Constants

class Desk360SendFormButton  : Button {
    init {

        when (Desk360Constants.currentTheme) {
            1 -> {
                this.setTextColor(Color.parseColor("#ffffff"))
                this.setBackgroundResource(R.drawable.button_oval_blue)
            }
            2 -> {
                this.setTextColor(Color.parseColor("#ffffff"))
                this.setBackgroundColor(Color.parseColor("#58b0fa"))
            }
            3 -> {
                this.setTextColor(Color.parseColor("#ffffff"))
                this.setBackgroundResource(R.drawable.button_oval_blue2)
            }
            4 -> {
                this.setTextColor(Color.parseColor("#ffffff"))
                this.setBackgroundColor(Color.parseColor("#58b0fa"))

            }
            5 -> {
                this.setTextColor(Color.parseColor("#58b0fa"))
                this.setBackgroundResource(R.drawable.button_oval_white)
            }
            else ->{
                this.setBackgroundResource(R.drawable.button_oval_blue)
                this.setBackgroundColor(Color.parseColor("#ffffff"))
            }
        }
    }



    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle)
}
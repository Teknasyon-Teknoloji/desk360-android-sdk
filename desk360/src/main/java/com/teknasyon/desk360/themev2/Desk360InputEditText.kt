package com.teknasyon.desk360.themev2

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet

import com.google.android.material.textfield.TextInputEditText
import com.teknasyon.desk360.helper.Desk360Constants

class Desk360InputEditText  : TextInputEditText {

    init {

        when (Desk360Constants.currentTheme) {
            1 -> {

                this.setTextColor(Color.parseColor("#000000"))
            }
            2 -> {
                this.setTextColor(Color.parseColor("#000000"))
            }
            3 -> {
                this.setTextColor(Color.parseColor("#000000"))
                this.background=null
            }
            4 -> {
                this.setTextColor(Color.parseColor("#ffffff"))
            }
            5 -> {
                this.setTextColor(Color.parseColor("#000000"))
            }
            else ->{
                this.setTextColor(Color.parseColor("#000000"))
                this.background=null

            }
        }
    }



    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle)
}
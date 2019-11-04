package com.teknasyon.desk360.themev2

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.widget.TextView

import com.teknasyon.desk360.R
import com.teknasyon.desk360.helper.Desk360Constants

class Desk360CommonButtonText : TextView {

    init {

        when (Desk360Constants.currentTheme) {
            1 -> {
               this.setTextColor(Color.parseColor("#ffffff"))
                this.setCompoundDrawablesWithIntrinsicBounds( R.drawable.zarf, 0, 0, 0)
            }
            2 -> {
                this.setTextColor(Color.parseColor("#ffffff"))
                this.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)
            }
            3 -> {
                this.setTextColor(Color.parseColor("#ffffff"))
                this.setCompoundDrawablesWithIntrinsicBounds( R.drawable.zarf, 0, 0, 0)
            }
            4 -> {
                this.setTextColor(Color.parseColor("#ffffff"))
                this.setCompoundDrawablesWithIntrinsicBounds( R.drawable.zarf, 0, 0, 0)
            }
            5 -> {
                this.setTextColor(Color.parseColor("#58b0fa"))
                this.setCompoundDrawablesWithIntrinsicBounds( R.drawable.mavi_zarf_icon, 0, 0, 0)
            }
            else ->{
                this.setTextColor(Color.parseColor("#ffffff"))
                this.setCompoundDrawablesWithIntrinsicBounds( R.drawable.zarf, 0, 0, 0)
            }
        }
    }



    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle)
}
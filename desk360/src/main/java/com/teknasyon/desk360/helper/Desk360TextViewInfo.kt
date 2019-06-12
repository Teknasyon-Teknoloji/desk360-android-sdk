package com.teknasyon.desk360.helper

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.widget.TextView
import com.teknasyon.desk360.helper.Desk360Constants.currentTheme

class Desk360TextViewInfo : TextView {
    init {
        if (currentTheme == "light") {
            this.setBackgroundColor(Color.WHITE)
            this.setTextColor(Color.parseColor("#707070"))
        } else {
            this.setBackgroundColor(Color.parseColor("#2b2b2b"))
            this.setTextColor(Color.parseColor("#959595"))
        }
    }

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle)
}
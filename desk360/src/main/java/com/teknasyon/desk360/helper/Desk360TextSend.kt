package com.teknasyon.desk360.helper

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.widget.TextView
import com.teknasyon.desk360.R
import com.teknasyon.desk360.helper.Desk360Constants.currentTheme

class Desk360TextSend : TextView {
    init {
        if (currentTheme == "light") {
            this.setBackgroundResource(R.drawable.light_theme_send_message_bg)
            this.setTextColor(Color.parseColor("#ffffff"))
        } else {
            this.setBackgroundResource(R.drawable.dark_theme_send_message_bg)
            this.setTextColor(Color.parseColor("#de000000"))
        }
    }

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle)
}
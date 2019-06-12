package com.teknasyon.desk360.helper

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.widget.Button
import com.teknasyon.desk360.R
import com.teknasyon.desk360.helper.Desk360Constants.currentTheme

class Desk360SendButton : Button {
    init {
        if (currentTheme == "light") {
            this.setBackgroundResource(R.drawable.light_theme_button_bg)
            this.setTextColor(Color.parseColor("#ffffff"))
        } else {
            this.setBackgroundResource(R.drawable.dark_theme_button_bg)
            this.setTextColor(Color.parseColor("#ffffff"))
        }
    }

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle)
}
package com.teknasyon.desk360.helper

import android.content.Context
import android.util.AttributeSet
import android.widget.ImageView
import com.teknasyon.desk360.R
import com.teknasyon.desk360.helper.Desk360Constants.currentTheme

class Desk360SendMessageView : ImageView{
    init {
        if (currentTheme == "light") {
            this.setImageResource(R.drawable.light_theme_send_icon)
        } else {
            this.setImageResource(R.drawable.dark_theme_send_icon)
        }
    }

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle)
}
package com.teknasyon.desk360.helper

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.widget.LinearLayout
import com.teknasyon.desk360.helper.Desk360Constants.currentTheme

class Desk360SendMessageLayoutView : LinearLayout {
    init {
        if (currentTheme == "light") {
            this.setBackgroundColor(Color.WHITE)
        } else {
            this.setBackgroundColor(Color.parseColor("#3b3b3b"))
        }
    }

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle)
}
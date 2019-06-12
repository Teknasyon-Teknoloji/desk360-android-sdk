package com.teknasyon.desk360.helper

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import com.google.android.material.textfield.TextInputEditText
import com.teknasyon.desk360.helper.Desk360Constants.currentTheme


/**
 * Created by seyfullah on 27,May,2019
 *
 */

class Desk360TextInputEditText : TextInputEditText {
    init {
        if (currentTheme == "light") {
            this.setTextColor(Color.BLACK)
            setBackgroundColor(Color.WHITE)
        } else {
            setBackgroundColor(Color.parseColor("#2b2b2b"))
            this.setTextColor(Color.WHITE)
        }
    }

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle)
}
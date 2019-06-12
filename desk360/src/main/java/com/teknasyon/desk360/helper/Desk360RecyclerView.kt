package com.teknasyon.desk360.helper

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import androidx.recyclerview.widget.RecyclerView
import com.teknasyon.desk360.helper.Desk360Constants.currentTheme

class Desk360RecyclerView : RecyclerView {
    init {
        if (currentTheme == "light")
            this.setBackgroundColor(Color.parseColor("#ffffff"))
        else
            this.setBackgroundColor(Color.parseColor("#2b2b2b"))
    }
    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle)
}
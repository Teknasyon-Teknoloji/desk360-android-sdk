package com.teknasyon.desk360.themev2

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet

import com.google.android.material.tabs.TabLayout
import com.teknasyon.desk360.helper.Desk360Constants

class Desk360TicketListTabLayout  : TabLayout{

    init {

        when (Desk360Constants.currentTheme) {
            1, 2, 3, 5 -> {
                this.setBackgroundColor(Color.parseColor("#f7f7f7"))
                this.setSelectedTabIndicatorColor(Color.parseColor("#58b0fa"))
                this.setTabTextColors(Color.parseColor("#9298b1"),Color.parseColor("#58b0fa"))
            }
            4 -> {
                this.setBackgroundColor(Color.parseColor("#3f4b60"))
                this.setSelectedTabIndicatorColor(Color.parseColor("#58b0fa"))
                this.setTabTextColors(Color.parseColor("#8794ac"),Color.parseColor("#58b0fa"))
            }
            else -> {
                this.setBackgroundColor(Color.parseColor("#f7f7f7"))
                this.setSelectedTabIndicatorColor(Color.parseColor("#58b0fa"))
                this.setTabTextColors(Color.parseColor("#9298b1"),Color.parseColor("#58b0fa"))
            }
        }
    }

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(
        context,
        attrs,
        defStyle
    )
}
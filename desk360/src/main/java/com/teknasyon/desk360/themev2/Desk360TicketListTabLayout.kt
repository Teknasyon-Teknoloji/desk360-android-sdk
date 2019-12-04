package com.teknasyon.desk360.themev2

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import com.google.android.material.tabs.TabLayout
import com.teknasyon.desk360.helper.Desk360Constants

class Desk360TicketListTabLayout : TabLayout {

    init {

        this.setBackgroundColor(Color.parseColor(Desk360Constants.currentType?.data?.general_settings?.main_background_color))
        this.setSelectedTabIndicatorColor(Color.parseColor(Desk360Constants.currentType?.data?.ticket_list_screen?.tab_active_border_color))
    }

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(
        context,
        attrs,
        defStyle
    )
}
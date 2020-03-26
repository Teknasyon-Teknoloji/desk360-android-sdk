package com.teknasyon.desk360.helper

import android.content.Context
import android.graphics.Color
import android.os.Build
import android.util.AttributeSet
import androidx.appcompat.widget.Toolbar

class Desk360Toolbar : Toolbar {

    init {
        this.setBackgroundColor(Color.parseColor(Desk360Constants.currentType?.data?.general_settings?.header_background_color))
        this.setTitleTextColor(Color.parseColor(Desk360Constants.currentType?.data?.general_settings?.header_text_color))
        if(Desk360Constants.currentType?.data?.general_settings?.header_shadow_is_hidden!!){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                this.elevation=20f
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
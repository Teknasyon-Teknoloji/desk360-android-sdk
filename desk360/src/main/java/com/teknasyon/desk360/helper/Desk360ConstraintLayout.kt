package com.teknasyon.desk360.helper

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import androidx.constraintlayout.widget.ConstraintLayout
import com.teknasyon.desk360.helper.AresConstants.currentTheme

/**
 * Created by seyfullah on 27,May,2019
 *
 */

class Desk360ConstraintLayout : ConstraintLayout {
    init {
        if (currentTheme == "light")
            this.setBackgroundColor(Color.WHITE)
        else
            this.setBackgroundColor(Color.parseColor("#5b5b5b"))
    }

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle)
}
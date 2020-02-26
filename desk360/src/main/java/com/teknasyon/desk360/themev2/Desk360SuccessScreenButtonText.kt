package com.teknasyon.desk360.themev2

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import com.teknasyon.desk360.helper.AutoResizeTextView
import com.teknasyon.desk360.helper.Desk360Constants

class Desk360SuccessScreenButtonText : AutoResizeTextView {

    init {

        this.setTextColor(Color.parseColor(Desk360Constants.currentType?.data?.ticket_success_screen?.button_text_color))
        this.text = "Mesajlar Sayasfına git"
    }

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(
        context,
        attrs,
        defStyle
    )
}
package com.teknasyon.desk360.themev2

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.appcompat.widget.AppCompatImageView
import com.teknasyon.desk360.helper.Desk360SDK

class Desk360SuccessScreenButtonIcon : AppCompatImageView {


    init {
        if (Desk360SDK.config?.data?.ticket_success_screen?.button_icon_is_hidden == true) {
            this.visibility = View.VISIBLE
        } else {
            this.visibility = View.GONE
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
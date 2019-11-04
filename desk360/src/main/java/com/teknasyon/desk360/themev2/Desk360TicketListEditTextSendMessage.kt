package com.teknasyon.desk360.themev2

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.widget.EditText
import com.teknasyon.desk360.helper.Desk360Constants


class Desk360TicketListEditTextSendMessage : EditText {

    init {

        when (Desk360Constants.currentTheme) {
            1 ,2,3,5-> {

                this.setTextColor(Color.parseColor("#000000"))
                this.setHintTextColor(Color.parseColor("#9298b1"))
            }
            4 -> {
                this.setTextColor(Color.parseColor("#ffffff"))
                this.setHintTextColor(Color.parseColor("#eeeff0"))
            }

            else ->{
                this.setTextColor(Color.parseColor("#000000"))
                this.setHintTextColor(Color.parseColor("#9298b1"))

            }
        }
    }



    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle)
}
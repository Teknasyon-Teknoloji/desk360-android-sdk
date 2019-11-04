package com.teknasyon.desk360.themev2

import android.content.Context
import android.util.AttributeSet
import android.widget.TextView
import com.teknasyon.desk360.R
import com.teknasyon.desk360.helper.Desk360Constants

class Desk360SentMessageLayout : TextView {


    init {

        when (Desk360Constants.currentTheme) {
            1 ,2,4-> {

                this.setBackgroundResource(R.drawable.sent_message_background)
            }
            3,5 -> {
                this.setBackgroundResource(R.drawable.send_messages_background_type2)
            }

            else ->{
                this.setBackgroundResource(R.drawable.sent_message_background)

            }
        }
    }



    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle)
}
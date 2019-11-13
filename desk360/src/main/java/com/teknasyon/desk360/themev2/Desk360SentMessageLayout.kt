package com.teknasyon.desk360.themev2

import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.util.AttributeSet
import android.widget.TextView
import com.teknasyon.desk360.R
import com.teknasyon.desk360.helper.Desk360Constants

class Desk360SentMessageLayout : TextView {


    init {

        when (Desk360Constants.currentType?.data?.ticket_detail_screen?.button_style_id) {
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

            this.setTextColor(Color.parseColor(Desk360Constants.currentType?.data?.ticket_detail_screen?.chat_sender_text_color))
            this.textSize = Desk360Constants.currentType?.data?.ticket_detail_screen?.chat_sender_font_size!!.toFloat()

            when(Desk360Constants.currentType?.data?.ticket_detail_screen?.chat_sender_font_weight){
                "regular" ->{
                    this.setTypeface(null, Typeface.NORMAL)
                }
                "bold" ->{
                    this.setTypeface(null, Typeface.BOLD)
                }
                "normal" ->{
                    this.setTypeface(null, Typeface.NORMAL)
                }
                else ->{
                    this.setTypeface(null, Typeface.NORMAL)
                }
            }
    }



    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle)
}
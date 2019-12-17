package com.teknasyon.desk360.helper

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.drawable.GradientDrawable
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.LinearLayout
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.teknasyon.desk360.modelv2.Desk360ScreenCreate

class TextInputViewGroup(val style: Desk360ScreenCreate, viewGroup: Fragment) {
    var holder: MyHolder
    var view: View? = null

    inner class MyHolder {
        var textInputLayout: TextInputLayout? = null
        var textInputEditText: TextInputEditText? = null
        var cardView: CardView? = null
    }

    init {
        view = viewGroup.layoutInflater.inflate(
            com.teknasyon.desk360.R.layout.custom_input_layout,
            null
        )
        holder = MyHolder()
        holder.textInputEditText =
            view?.findViewById(com.teknasyon.desk360.R.id.input_edit_text)
        holder.textInputLayout =
            view?.findViewById(com.teknasyon.desk360.R.id.input_layout)
        holder.cardView = view?.findViewById(com.teknasyon.desk360.R.id.input_card_view)
        holder.cardView?.setCardBackgroundColor(Color.parseColor(style.form_input_background_color))
    }

    fun createEditText(hintText: String): View? {
        if (style.form_style_id == 2 || style.form_style_id == 1)
            holder.cardView?.visibility = View.INVISIBLE
        else
            holder.cardView?.visibility = View.VISIBLE
        holder.textInputEditText?.setDesk360InputStyle(style)
        holder.textInputLayout?.setDesk360TextAreaStyle(style)
        when (style.form_style_id) {
            1 -> {
                //line
                holder.textInputLayout?.setPadding(0, 0, 0, 0)
                holder.textInputLayout?.boxBackgroundMode = TextInputLayout.BOX_BACKGROUND_NONE
            }
            2 -> {
                //box
                holder.textInputLayout?.setPadding(0, 0, 0, 0)
                holder.textInputLayout?.boxBackgroundMode = TextInputLayout.BOX_BACKGROUND_OUTLINE
            }
            else -> {
                //shadow
                holder.textInputLayout?.setStroke(style)
                holder.textInputLayout?.boxBackgroundMode = TextInputLayout.BOX_BACKGROUND_NONE
            }
        }
        holder.textInputLayout?.hint = hintText
        holder.textInputEditText?.setOnFocusChangeListener { view, hasFocus ->
            if (hasFocus || holder.textInputEditText!!.text?.length ?: 0 > 0) {
                holder.cardView?.setCardBackgroundColor(Color.parseColor(style.form_input_focus_background_color))
            } else {
                holder.cardView?.setCardBackgroundColor(Color.parseColor(style.form_input_background_color))
            }
        }
        return view
    }
}

@SuppressLint("ClickableViewAccessibility")
fun TextInputEditText.setDesk360InputStyle(style: Desk360ScreenCreate) {
    this.setTextColor(Color.parseColor(style.form_input_focus_color))
    this.imeOptions = EditorInfo.IME_ACTION_NEXT
    when (style.form_style_id) {
        1 -> {
            //line
            this.setPadding(0, 24, 0, 24)
            val colorStateList = ColorStateList(
                arrayOf(
                    intArrayOf(-android.R.attr.state_focused),
                    intArrayOf(android.R.attr.state_focused)
                ),
                intArrayOf(
                    Color.parseColor(style.form_input_border_color),
                    Color.parseColor(style.form_input_focus_border_color)
                )
            )
            supportBackgroundTintList = colorStateList
            supportBackgroundTintMode = PorterDuff.Mode.SRC_ATOP
        }
        2 -> {
            //box
            this.setPadding(16, 32, 16, 32)
            this.background = null
        }
        else -> {
            //shadow
            this.background = null
        }
    }
}

fun LinearLayout.setStroke(style: Desk360ScreenCreate) {
    //shadow
    val gd = GradientDrawable()
    gd.setColor(Color.TRANSPARENT)
    gd.cornerRadius = 13f
    gd.setStroke(3, Color.parseColor(style.form_input_border_color))
    this.background = gd
}

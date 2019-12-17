package com.teknasyon.desk360.helper

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Build
import android.view.View
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.teknasyon.desk360.modelv2.Desk360ScreenCreate

class TextAreaViewGroup(val style: Desk360ScreenCreate, viewGroup: Fragment) {
    var holder: MyHolder
    var view: View? = null

    inner class MyHolder {
        var textAreaLayout: TextInputLayout? = null
        var textAreaEditText: TextInputEditText? = null
        var textAreaCardView: CardView? = null
    }

    init {
        view = viewGroup.layoutInflater.inflate(
            com.teknasyon.desk360.R.layout.custom_textarea_layout,
            null
        )
        holder = MyHolder()
        holder.textAreaEditText =
            view?.findViewById(com.teknasyon.desk360.R.id.textarea_edit_text)
        holder.textAreaLayout =
            view?.findViewById(com.teknasyon.desk360.R.id.textarea_layout)
        holder.textAreaCardView = view?.findViewById(com.teknasyon.desk360.R.id.textarea_card_view)
        holder.textAreaCardView?.setCardBackgroundColor(Color.parseColor(style.form_input_background_color))
    }

    fun createEditText(hintText: String): View? {
        holder.textAreaLayout?.hint = hintText
        holder.textAreaEditText?.setDesk360TextAreaStyle(style)
        holder.textAreaLayout?.setDesk360TextAreaStyle(style)
        holder.textAreaEditText?.setOnFocusChangeListener { view, hasFocus ->
            if (hasFocus || holder.textAreaEditText!!.text?.length ?: 0 > 0) {
                holder.textAreaCardView?.setCardBackgroundColor(Color.parseColor(style.form_input_focus_background_color))
            } else {
                holder.textAreaCardView?.setCardBackgroundColor(Color.parseColor(style.form_input_background_color))
            }
        }
        if (style.form_style_id == 2 || style.form_style_id == 1)
            holder.textAreaCardView?.visibility = View.INVISIBLE
        else
            holder.textAreaCardView?.visibility = View.VISIBLE
        holder.textAreaEditText?.background = null
        when (style.form_style_id) {
            3 -> {
                //shadow
                holder.textAreaLayout?.setStroke(style)
                holder.textAreaLayout?.boxBackgroundMode = TextInputLayout.BOX_BACKGROUND_NONE
            }
            else -> {
                //box
                holder.textAreaLayout?.setPadding(0, 0, 0, 0)
                holder.textAreaLayout?.boxBackgroundMode = TextInputLayout.BOX_BACKGROUND_OUTLINE
            }
        }
        return view
    }
}

fun TextInputEditText.setDesk360TextAreaStyle(style: Desk360ScreenCreate) {
    this.setPadding(16, 24, 16, 24)
    this.setTextColor(Color.parseColor(style.form_input_focus_color))
}

fun TextInputLayout.setDesk360TextAreaStyle(style: Desk360ScreenCreate) {
    if (style.form_style_id == 3) {
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

        val colorHintStateListNormal = ColorStateList(
            arrayOf(
                intArrayOf(-android.R.attr.state_empty),
                intArrayOf(android.R.attr.state_empty)
            ),
            intArrayOf(
                Color.parseColor(style.form_input_border_color),
                Color.parseColor(style.label_text_color)
            )
        )

        val colorHintStateListDefault = ColorStateList(
            arrayOf(
                intArrayOf(-android.R.attr.state_empty),
                intArrayOf(android.R.attr.state_empty)
            ),
            intArrayOf(
                Color.parseColor(style.form_input_color),
                Color.parseColor(style.form_input_focus_color)
            )
        )

        this.defaultHintTextColor = colorHintStateListDefault
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            this.backgroundTintList = colorStateList
        }
        this.hintTextColor = colorHintStateListNormal
    } else {
        val states = arrayOf(
            intArrayOf(-android.R.attr.state_focused),
            intArrayOf(android.R.attr.state_focused)
        )
        val colors = intArrayOf(
            Color.parseColor(style.form_input_border_color),
            Color.parseColor(style.form_input_focus_border_color)
        )
        val myColorList = ColorStateList(states, colors)
        this.setBoxStrokeColorStateList(myColorList)

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

        val colorHintStateListNormal = ColorStateList(
            arrayOf(
                intArrayOf(-android.R.attr.state_focused),
                intArrayOf(android.R.attr.state_focused)
            ),
            intArrayOf(
                Color.parseColor(style.form_input_border_color),
                Color.parseColor(style.label_text_color)
            )
        )

        val colorHintStateListDefault = ColorStateList(
            arrayOf(
                intArrayOf(-android.R.attr.state_focused),
                intArrayOf(android.R.attr.state_focused)
            ),
            intArrayOf(
                Color.parseColor(style.form_input_color),
                Color.parseColor(style.form_input_focus_color)
            )
        )

        this.defaultHintTextColor = colorHintStateListDefault
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            this.backgroundTintList = colorStateList
        }
        this.hintTextColor = colorHintStateListNormal
    }
}
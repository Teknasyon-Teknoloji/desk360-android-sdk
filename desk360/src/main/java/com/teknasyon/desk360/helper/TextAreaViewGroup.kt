package com.teknasyon.desk360.helper

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Build
import android.view.View
import android.widget.LinearLayout
import android.widget.Spinner
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
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

        view = viewGroup.layoutInflater.inflate(com.teknasyon.desk360.R.layout.custom_textarea_layout, null)
        holder = MyHolder()

        holder.textAreaEditText = view?.findViewById(com.teknasyon.desk360.R.id.textarea_edit_text)

        holder.textAreaLayout = view?.findViewById(com.teknasyon.desk360.R.id.textarea_layout)

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
            2 -> {
                //box
                holder.textAreaLayout?.setPadding(0, 0, 0, 0)
                holder.textAreaLayout?.boxBackgroundMode = TextInputLayout.BOX_BACKGROUND_OUTLINE
            }

            1 -> {
                //line

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

        val states2 = Array(2, init = { IntArray(1) })
        states2[0] = IntArray(1) {android.R.attr.state_focused}
        states2[1] = IntArray(1) {android.R.attr.state_focused}

        val colors2 = IntArray(2)
        colors2[0] = Color.parseColor(style.form_input_border_color)
        colors2[1] = Color.parseColor(style.form_input_focus_border_color)

        val colorStateList = ColorStateList(states2,colors2)

        val states3 = Array(2, init = { IntArray(1) })
        states3[0] = IntArray(1) {-android.R.attr.state_focused}
        states3[1] = IntArray(1) {android.R.attr.state_focused}

        val colors3 = IntArray(2)
        colors3[0] = Color.parseColor(style.form_input_border_color)
        colors3[1] = Color.parseColor(style.label_text_color)

        val colorHintStateListNormal = ColorStateList(states3,colors3)

        val states4 = Array(2, init = { IntArray(1) })
        states4[0] = IntArray(1) {-android.R.attr.state_empty}
        states4[1] = IntArray(1) {android.R.attr.state_empty}

        val colors4 = IntArray(2)
        colors4[0] = Color.parseColor(style.form_input_color)
        colors4[1] = Color.parseColor(style.form_input_focus_color)

        val colorHintStateListDefault = ColorStateList(states4,colors4)

        this.defaultHintTextColor = colorHintStateListDefault

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            this.backgroundTintList = colorStateList
        }

        this.hintTextColor = colorHintStateListNormal

    } else {

        val states = Array(2, init = { IntArray(1) })
        val focused = IntArray(2)
        focused[0] = android.R.attr.state_focused
        states[0] = focused
        states[1] = focused

        val colors = IntArray(2, init = { 0 })
        colors[0] = Color.parseColor(style.form_input_border_color)
        colors[1] = Color.parseColor(style.form_input_focus_border_color)

        val myColorList = ColorStateList(states, colors)
        this.setBoxStrokeColorStateList(myColorList)

        val states2 = Array(2, init = { IntArray(1) })
        states2[0] = IntArray(1) {android.R.attr.state_focused}
        states2[1] = IntArray(1) {-android.R.attr.state_focused}

        val colors2 = IntArray(2)
        colors2[0] = Color.parseColor(style.form_input_border_color)
        colors2[1] = Color.parseColor(style.form_input_focus_border_color)

        val colorStateList = ColorStateList(states2, colors2)

        val states3 = Array(2, init = { IntArray(1) })
        states3[0] = IntArray(1) {-android.R.attr.state_focused}
        states3[1] = IntArray(1) {android.R.attr.state_focused}

        val colors3 = IntArray(2)
        colors3[0] = Color.parseColor(style.form_input_border_color)
        colors3[1] = Color.parseColor(style.label_text_color)

        val colorHintStateListNormal = ColorStateList(states3, colors3)

        val states4 = Array(2, init = { IntArray(1) })
        states4[0] = IntArray(1) {-android.R.attr.state_focused}
        states4[1] = IntArray(1) {android.R.attr.state_focused}

        val colors4 = IntArray(2)
        colors4[0] = Color.parseColor(style.form_input_color)
        colors4[1] = Color.parseColor(style.form_input_focus_color)

        val colorHintStateListDefault = ColorStateList(states4, colors4)

        this.defaultHintTextColor = colorHintStateListDefault

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            this.backgroundTintList = colorStateList
        }

        this.hintTextColor = colorHintStateListNormal
    }
}
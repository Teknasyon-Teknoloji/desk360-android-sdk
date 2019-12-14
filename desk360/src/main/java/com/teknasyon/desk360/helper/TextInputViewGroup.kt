package com.teknasyon.desk360.helper
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Build
import android.view.View
import android.view.inputmethod.EditorInfo
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
        holder.textInputLayout?.setDesk360InputStyle(style)
        when (style.form_style_id) {
            1 -> {
                //line
                holder.textInputLayout?.boxBackgroundMode = TextInputLayout.BOX_BACKGROUND_NONE
            }
            2 -> {
                //box
                holder.textInputLayout?.boxBackgroundMode = TextInputLayout.BOX_BACKGROUND_OUTLINE
            }
            else -> {
                //shadow
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
fun TextInputLayout.setDesk360InputStyle(style: Desk360ScreenCreate) {
    val states = arrayOf(
        intArrayOf(android.R.attr.state_focused),
        intArrayOf(android.R.attr.state_hovered),
        intArrayOf(android.R.attr.state_enabled),
        intArrayOf()
    )
    val colors = intArrayOf(
        Color.parseColor(style.form_input_focus_border_color),
        Color.parseColor(style.form_input_border_color),
        Color.parseColor(style.form_input_focus_border_color),
        Color.parseColor(style.form_input_border_color)
    )
    val myColorList = ColorStateList(states, colors)
    this.setBoxStrokeColorStateList(myColorList)
    val colorStateList = ColorStateList(
        arrayOf(
            intArrayOf(-android.R.attr.state_focused),
            intArrayOf(android.R.attr.state_focused),
            intArrayOf(android.R.attr.state_drag_hovered),
            intArrayOf(-android.R.attr.state_hovered),
            intArrayOf(android.R.attr.state_active)
        ),
        intArrayOf(
            Color.parseColor(style.form_input_border_color),
            Color.parseColor(style.form_input_focus_color),
            Color.parseColor(style.form_input_focus_color),
            Color.parseColor(style.form_input_focus_color),
            Color.parseColor(style.form_input_focus_color)
        )
    )
    val colorHintStateList = ColorStateList(
        arrayOf(
            intArrayOf(-android.R.attr.state_focused),
            intArrayOf(android.R.attr.state_focused)
        ),
        intArrayOf(
            Color.parseColor(style.form_input_color),
            Color.parseColor(style.form_input_focus_color)
        )
    )

    val colorHintStateListNormal = ColorStateList(
        arrayOf(
            intArrayOf(-android.R.attr.state_focused),
            intArrayOf(android.R.attr.state_focused),
            intArrayOf(android.R.attr.state_drag_hovered),
            intArrayOf(-android.R.attr.state_hovered),
            intArrayOf(android.R.attr.state_active)
        ),
        intArrayOf(
            Color.parseColor(style.form_input_border_color),
            Color.parseColor(style.label_text_color),
            Color.parseColor(style.label_text_color),
            Color.parseColor(style.label_text_color),
            Color.parseColor(style.label_text_color)
        )
    )

    val colorHintStateListDefault = ColorStateList(
        arrayOf(
            intArrayOf(-android.R.attr.state_focused),
            intArrayOf(android.R.attr.state_focused),
            intArrayOf(android.R.attr.state_drag_hovered),
            intArrayOf(-android.R.attr.state_hovered),
            intArrayOf(android.R.attr.state_active)
        ),
        intArrayOf(
            Color.parseColor(style.form_input_color),
            Color.parseColor(style.form_input_focus_color),
            Color.parseColor(style.form_input_focus_color),
            Color.parseColor(style.form_input_focus_color),
            Color.parseColor(style.form_input_focus_color)
        )
    )

    this.defaultHintTextColor = colorHintStateListDefault
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        this.backgroundTintList = colorStateList
    }
    this.hintTextColor = colorHintStateListNormal
}
fun TextInputEditText.setDesk360InputStyle(style: Desk360ScreenCreate) {
    this.setTextColor(Color.parseColor(style.form_input_focus_color))
    this.imeOptions = EditorInfo.IME_ACTION_NEXT
    when (style.form_style_id) {
        1 -> {
            //line
            val colorStateList = ColorStateList(
                arrayOf(
                    intArrayOf(-android.R.attr.state_focused),
                    intArrayOf(android.R.attr.state_focused),
                    intArrayOf(android.R.attr.state_drag_hovered),
                    intArrayOf(android.R.attr.state_hovered),
                    intArrayOf(android.R.attr.state_active)
                ),
                intArrayOf(
                    Color.parseColor(style.form_input_border_color),
                    Color.parseColor(style.form_input_focus_border_color),
                    Color.parseColor(style.form_input_focus_border_color),
                    Color.parseColor(style.form_input_focus_border_color),
                    Color.parseColor(style.form_input_focus_border_color)
                )
            )
            supportBackgroundTintList = colorStateList
            supportBackgroundTintMode = PorterDuff.Mode.SRC_ATOP
        }
        2 -> {
            this.background = null
            //box
        }
        else -> {
            //shadow
            this.background = null
        }
    }
}
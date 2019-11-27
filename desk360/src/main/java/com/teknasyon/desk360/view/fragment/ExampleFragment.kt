package com.teknasyon.desk360.view.fragment


import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.RectShape
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import com.google.android.material.textfield.TextInputLayout
import com.teknasyon.desk360.R
import com.teknasyon.desk360.databinding.FragmentExampleBinding
import com.teknasyon.desk360.helper.Desk360ButtonStyle
import com.teknasyon.desk360.helper.Desk360Constants
import kotlinx.android.synthetic.main.fragment_example.view.*

class ExampleFragment : Fragment() {
    private val type = 3
    private lateinit var binding: FragmentExampleBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        FragmentExampleBinding.inflate(inflater, container, false).also {
            binding = it
            binding.lifecycleOwner = viewLifecycleOwner
            return it.root
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.textFooterCreateTicketScreen.movementMethod = ScrollingMovementMethod()
        Desk360ButtonStyle.setStyle(
            Desk360Constants.currentType?.data?.create_pre_screen?.button_style_id,
            binding.createTicketButton,
            context!!
        )
        binding.createScreenButtonIcon.setImageResource(R.drawable.zarf)
        binding.createScreenButtonIcon.setColorFilter(
            Color.parseColor(Desk360Constants.currentType?.data?.create_pre_screen?.button_text_color),
            PorterDuff.Mode.SRC_ATOP
        )
        val rootParamsLayout = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        rootParamsLayout.setMargins(24, 24, 24, 24)

        //field  email

        for (i in 0..0) {
            val textInputLayoutEmail = TextInputLayout(context)
            textInputLayoutEmail.layoutParams = rootParamsLayout
            textInputLayoutEmail.gravity = Gravity.CENTER_VERTICAL

            textInputLayoutEmail.setDesk360InputStyle(InputLayoutStyle(), type)

            val editTexEmail = EditText(context)
            editTexEmail.setDesk360EditTextStyle(EditTextStyle(), type)
            textInputLayoutEmail.addView(editTexEmail)

            if (type == 3) {
                val cardView = CardView(context!!)
                cardView.layoutParams = rootParamsLayout
                cardView.setDesk360CardViewStyle()
                cardView.addView(textInputLayoutEmail)
                binding.root.root_layout_ticket_create.addView(cardView)
            } else {
                binding.root.root_layout_ticket_create.addView(textInputLayoutEmail)
            }
        }

        for (i in 0..0) {
            val textInputLayoutSpinner = TextInputLayout(context)
            textInputLayoutSpinner.layoutParams = rootParamsLayout
            textInputLayoutSpinner.gravity = Gravity.CENTER_VERTICAL

            textInputLayoutSpinner.setDesk360InputStyle(InputLayoutStyle(), type)

            val spinner = Spinner(context)
            spinner.setDesk360SpinnerStyle(SpinnerStyle(), type)
            textInputLayoutSpinner.addView(spinner)

            if (type == 3) {
                val cardView = CardView(context!!)
                cardView.layoutParams = rootParamsLayout
                cardView.setDesk360CardViewStyle()
                cardView.addView(textInputLayoutSpinner)
                binding.root.root_layout_ticket_create.addView(cardView)
            } else {
                binding.root.root_layout_ticket_create.addView(textInputLayoutSpinner)
            }
        }

//        for (i in 0..0) {
//            val spinner = Spinner(context)
//            spinner.layoutParams = rootParamsLayout
//            spinner.gravity = Gravity.CENTER_VERTICAL
//
//            val spinnerArray = ArrayList<String>()
//            spinnerArray.add("Seciniz..")
//            spinnerArray.add("two")
//            spinnerArray.add("three")
//            spinnerArray.add("four")
//            spinnerArray.add("five")
//
//
//
//            val spinnerArrayAdapter =
//                ArrayAdapter(context, R.layout.dropdown_new, spinnerArray)
//
//            spinner.adapter = spinnerArrayAdapter
//
//
//            spinner.setDesk360SpinnerStyle(SpinnerStyle())
//
//            if (type == 2) {
//                val cardViewSpinner = CardView(context!!)
//                cardViewSpinner.layoutParams = rootParamsLayout
//                cardViewSpinner.setDesk360SpinnerCardViewStyle()
//                spinner.setPadding(24,24,24,24)
//                cardViewSpinner.addView(spinner)
//                binding.root.root_layout_ticket_create.addView(cardViewSpinner)
//            } else {
//                spinner.setPadding(24,8,24,0)
//                val underLine = View(context)
//                underLine.minimumHeight=5
//                underLine.setBackgroundColor(Color.parseColor("#000000"))
//                binding.root.root_layout_ticket_create.addView(spinner)
//                binding.root.root_layout_ticket_create.addView(underLine)
//            }
//        }



        for (i in 0..1) {
            val messageStyleId = 3

            when (messageStyleId) {
                in listOf(1, 4) -> setMessageStyle(
                    context,
                    rootParamsLayout,
                    binding.root.root_layout_ticket_create, 2
                )
                in listOf(3, 5) -> setMessageStyle(
                    context,
                    rootParamsLayout,
                    binding.root.root_layout_ticket_create, 3
                )
                else -> setMessageStyle(
                    context,
                    rootParamsLayout,
                    binding.root.root_layout_ticket_create, 1

                )
            }
        }

    }

}


//---------------------------------------------------------
//INPUT STYLE
//---------------------------------------------------------


class InputLayoutStyle {
    var input_layout_email_hint_text: String = "Email"
    var input_layout_email_hint_unactive_color: String = "#cecece"
    var input_layout_email_radius: Int = 10
    var input_layout_email_style_id: Int = 1
    var input_layout_email_border_color: String = "#58b0fa"

}


fun TextInputLayout.setDesk360InputStyle(style: InputLayoutStyle, type: Int) {
    if (type == 3) {
        this.setBoxBackgroundMode(TextInputLayout.BOX_BACKGROUND_NONE)
    } else {
        this.setBoxBackgroundMode(style.input_layout_email_style_id.textInputLayoutBoxMode())
        this.boxStrokeColor = Color.parseColor(style.input_layout_email_border_color)
    }

    this.hint = style.input_layout_email_hint_text
    val textInputLayoutEmailStates = arrayOf(intArrayOf())
    val textInputLayoutEmailColors =
        intArrayOf(Color.parseColor(style.input_layout_email_hint_unactive_color))
    this.defaultHintTextColor =
        ColorStateList(textInputLayoutEmailStates, textInputLayoutEmailColors)
    this.setBoxCornerRadii(
        style.input_layout_email_radius.toFloat(),
        style.input_layout_email_radius.toFloat(),
        style.input_layout_email_radius.toFloat(),
        style.input_layout_email_radius.toFloat()
    )

}

class EditTextStyle {
    var edit_text_email_text_color: String = "#000000"
    var edit_text_email_text_size: Int = 18
    var edit_text_email_text_weight: Int = 400
}


fun EditText.setDesk360EditTextStyle(style: EditTextStyle, type: Int) {
    if (type == 3) {
        this.background = null
        this.setPadding(16, 16, 16, 16)
    } else {
        this.setPadding(48, 48, 48, 48)
    }

    this.setTextColor(Color.parseColor(style.edit_text_email_text_color))
    this.textSize = style.edit_text_email_text_size.toFloat()


}


fun CardView.setDesk360CardViewStyle() {
    this.cardElevation = 20f
    this.radius = 4f
}


fun Int.textInputLayoutBoxMode(): Int {

    return if (this == 2) {
        TextInputLayout.BOX_BACKGROUND_OUTLINE

    } else {
        TextInputLayout.BOX_BACKGROUND_NONE

    }
}
//---------------------------------------------------------
//INPUT STYLE
//---------------------------------------------------------

class SpinnerStyle {
    var button_text_font_weight: Int = 600
    var added_file_is_hidden: Boolean = false
    var padding: Int = 16
}

fun Spinner.setDesk360SpinnerStyle(
    style: SpinnerStyle,
    type: Int
) {

    if (type == 3) {
        this.background = null
        this.setPadding(16, 16, 16, 16)
    } else {
        this.setPadding(48, 48, 48, 48)
    }

    this.isActivated = style.added_file_is_hidden
    this.setPadding(style.padding, style.padding, style.padding, style.padding)
}

fun CardView.setDesk360SpinnerCardViewStyle() {
    this.cardElevation = 20f
    this.radius = 4f
}



//---------------------------------------------------------
//MESSAGE TYPES
//---------------------------------------------------------

fun setMessageStyle(
    context: Context?,
    rootParamsLayout: LinearLayout.LayoutParams,
    rootLayout: LinearLayout,
    style: Int

) {

    when (style) {
        1 -> {
            val inputMessage = TextInputLayout(context)
            inputMessage.layoutParams = rootParamsLayout
            inputMessage.gravity = Gravity.CENTER_VERTICAL
            inputMessage.hint = "Mesajınız"

            val statesMessage = arrayOf(intArrayOf())
            val colorsMessage = intArrayOf(Color.parseColor("#cecece"))
            inputMessage.defaultHintTextColor = ColorStateList(statesMessage, colorsMessage)

            inputMessage.setBoxCornerRadii(10f, 10f, 10f, 10f)

            inputMessage.setBoxBackgroundMode(TextInputLayout.BOX_BACKGROUND_OUTLINE)
            inputMessage.boxStrokeColor = Color.parseColor("#58b0fa")

            rootLayout.addView(inputMessage)


            val editMessage = EditText(context)
            editMessage.minLines = 6
            editMessage.maxLines = 7
            editMessage.gravity = Gravity.START
            editMessage.setPadding(50, 50, 50, 50)
            editMessage.setTextColor(Color.BLACK)
            inputMessage.addView(editMessage)
        }
        2 -> {
            val messageLayout = LinearLayout(context)
            messageLayout.layoutParams = rootParamsLayout
            messageLayout.orientation = LinearLayout.VERTICAL
            messageLayout.gravity = Gravity.CENTER_VERTICAL
            rootLayout.addView(messageLayout)

            val hintTextMessage = TextView(context)
            hintTextMessage.text = "Mesajınız"
            hintTextMessage.setPadding(0, 0, 0, 24)
            hintTextMessage.setTextColor(Color.GRAY)


            messageLayout.addView(hintTextMessage)

            val shape = ShapeDrawable(RectShape())
            shape.paint.color = Color.GRAY
            shape.paint.style = Paint.Style.STROKE
            shape.paint.strokeWidth = 3f

            val editMessage2 = EditText(context)
            editMessage2.minLines = 6
            editMessage2.maxLines = 7
            editMessage2.background = shape
            editMessage2.gravity = Gravity.START
            editMessage2.setPadding(32, 32, 32, 32)
            editMessage2.setTextColor(Color.BLACK)
            messageLayout.addView(editMessage2)

        }
        else -> {
            val cardViewMessage = CardView(context!!)
            cardViewMessage.layoutParams = rootParamsLayout
            cardViewMessage.cardElevation = 20f
            cardViewMessage.radius = 4f
            rootLayout.addView(cardViewMessage)

            val inputMessageType2 = TextInputLayout(context)
            inputMessageType2.hint = "Mesajınız"
            inputMessageType2.setBoxBackgroundMode(TextInputLayout.BOX_BACKGROUND_NONE)
            inputMessageType2.setPadding(24, 16, 24, 8)
            cardViewMessage.addView(inputMessageType2)

            val editMessageType2 = EditText(context)
            editMessageType2.setTextColor(Color.BLACK)
            editMessageType2.background = null
            editMessageType2.minLines = 6
            editMessageType2.gravity = Gravity.START
            editMessageType2.maxLines = 7
            inputMessageType2.addView(editMessageType2)
        }
    }

}



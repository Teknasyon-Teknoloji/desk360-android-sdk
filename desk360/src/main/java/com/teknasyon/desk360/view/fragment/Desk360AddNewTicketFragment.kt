package com.teknasyon.desk360.view.fragment

import android.content.Context.INPUT_METHOD_SERVICE
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.drawable.GradientDrawable
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.util.Log
import android.view.*
import android.view.KeyEvent.ACTION_UP
import android.view.KeyEvent.KEYCODE_DPAD_CENTER
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.LinearLayout
import android.widget.Spinner
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.NavOptions
import androidx.navigation.Navigation
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.teknasyon.desk360.R
import com.teknasyon.desk360.databinding.Desk360AddNewTicketLayoutBinding
import com.teknasyon.desk360.helper.Desk360Config
import com.teknasyon.desk360.helper.Desk360Constants
import com.teknasyon.desk360.helper.Desk360CustomStyle
import com.teknasyon.desk360.model.Desk360Type
import com.teknasyon.desk360.modelv2.Desk360CustomFields
import com.teknasyon.desk360.modelv2.Desk360ScreenCreate
import com.teknasyon.desk360.view.adapter.Desk360CustomSupportTypeAdapter
import com.teknasyon.desk360.view.adapter.Desk360SupportTypeAdapter
import com.teknasyon.desk360.viewmodel.AddNewTicketViewModel
import java.util.regex.Pattern


/**
 * Created by seyfullah on 30,May,2019
 *
 */

open class Desk360AddNewTicketFragment : Fragment() {

    private var viewModel: AddNewTicketViewModel? = null
    private var nameField: TextInputEditText? = null
    private var eMailField: TextInputEditText? = null
    private var messageField: TextInputEditText? = null
    private var subjectTypeSpinner: Spinner? = null
    private val selectedTypeId = 1

    private lateinit var binding: Desk360AddNewTicketLayoutBinding

    private var typeList: ArrayList<Desk360Type>? = null
    private val editTextStyleModel =
        Desk360Config.instance.getDesk360Preferences()?.types?.data?.create_screen
    private var customInputField: List<Desk360CustomFields> = arrayListOf()
    private var customSelectBoxField: List<Desk360CustomFields> = arrayListOf()
    private var customTextAreaField: List<Desk360CustomFields> = arrayListOf()

    private var customInputViewList: ArrayList<TextInputEditText> = arrayListOf()
    private var customSelectBoxViewList: ArrayList<Spinner> = arrayListOf()
    private var customTextAreaViewList: ArrayList<TextInputEditText> = arrayListOf()

    //Validate variables
    private var nameData: String? = null
    private var emailData: String? = null
    private var messageData: String? = null
    private var messageLength: Int = 0
    private var nameFieldFill: Boolean = false
    private var emailFieldFill: Boolean = false
    private var messageFieldFill: Boolean = false

    private val rootParamsLayout = LinearLayout.LayoutParams(
        LinearLayout.LayoutParams.MATCH_PARENT,
        LinearLayout.LayoutParams.WRAP_CONTENT
    )

    private var observer = Observer<ArrayList<Desk360Type>> {
        binding.loadingProgress?.visibility = View.GONE
        if (it != null) {
            typeList = it

            val listOfType: ArrayList<String> = arrayListOf()
            for (i in 0 until it.size) {
                listOfType.add(it[i].title.toString())
            }
            val myAdapter =
                context?.let { it1 ->
                    Desk360SupportTypeAdapter(
                        it1,
                        R.layout.desk360_type_dropdown,
                        listOfType
                    )
                }
            subjectTypeSpinner?.adapter = myAdapter
        }
    }

    private fun observerName() {
        nameField?.isEnabled = true
        nameField?.requestFocus()
        nameField?.onKeyUp(
            KEYCODE_DPAD_CENTER,
            KeyEvent(ACTION_UP, KEYCODE_DPAD_CENTER)
        )
    }

    private fun observerEMail() {
        eMailField?.isEnabled = true
        eMailField?.requestFocus()
        eMailField?.onKeyUp(
            KEYCODE_DPAD_CENTER,
            KeyEvent(ACTION_UP, KEYCODE_DPAD_CENTER)
        )
    }

    private fun observerMessage() {
        messageField?.isEnabled = true
        messageField?.requestFocus()
        messageField?.onKeyUp(
            KEYCODE_DPAD_CENTER,
            KeyEvent(ACTION_UP, KEYCODE_DPAD_CENTER)
        )
    }

    private var observerAddedTicket = Observer<String> {
        if (it != null) {
            view?.let { it1 ->
                Navigation.findNavController(it1)
                    .navigate(
                        R.id.action_addNewTicketFragment_to_thanksFragment,
                        null,
                        NavOptions.Builder().setPopUpTo(R.id.addNewTicketFragment, true).build()
                    )
            }

            val imm = activity?.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view!!.windowToken, 0)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Desk360AddNewTicketLayoutBinding.inflate(inflater, container, false).also {
            binding = it
            binding.lifecycleOwner = viewLifecycleOwner
            return it.root
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = AddNewTicketViewModel()
        viewModel?.typeList?.observe(this, observer)
        viewModel?.addedTicket?.observe(this, observerAddedTicket)
//      binding.subjectType?.prompt = "Gender"
//      binding.subjectType?.onItemSelectedListener =
//          (object : AdapterView.OnItemSelectedListener {
//              override fun onNothingSelected(parent: AdapterView<*>?) {
//
//              }
//
//              override fun onItemSelected(
//                  parent: AdapterView<*>,
//                  view: View?,
//                  position: Int,
//                  id: Long
//              ) {
//                  typeList?.let { it[position].let { it1 -> selectedTypeId = it1.id!! } }
//              }
//          })
//      binding.txtBottomFooterMessageForm?.movementMethod = ScrollingMovementMethod()
        binding.createTicketButton?.setOnClickListener {
            validateAllField()
        }

        rootParamsLayout.setMargins(24, 24, 24, 24)

        /**
         * find custom fields
         */
        Desk360Config.instance.getDesk360Preferences()?.types?.data?.custom_fields?.apply {
            customInputField = filter { it.type == "input" }
            customSelectBoxField = filter { it.type == "selectbox" }
            customTextAreaField = filter { it.type == "textarea" }
        }

        /**
         * name filed
         */
        nameField = createEditText("Name")
        nameField?.setLines(1)
        nameField?.setSingleLine(true)
        nameField?.imeOptions = EditorInfo.IME_ACTION_NEXT
        nameField?.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {}
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                nameQuality(s)
            }
        })

        /**
         * email filed
         */
        eMailField = createEditText("Email")
        eMailField?.setLines(1)
        eMailField?.setSingleLine(true)
        eMailField?.imeOptions = EditorInfo.IME_ACTION_NEXT
        eMailField?.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {}
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                emailQuality(s)
            }
        })

        for (i in 0 until customInputField.size) {
            customInputViewList.add(createEditText("custom").also {
                it?.tag = customInputField[i].id
                it?.setLines(1)
                it?.setSingleLine(true)
                it?.imeOptions = EditorInfo.IME_ACTION_NEXT
                it?.addTextChangedListener(object : TextWatcher {
                    override fun afterTextChanged(s: Editable) {}
                    override fun beforeTextChanged(
                        s: CharSequence,
                        start: Int,
                        count: Int,
                        after: Int
                    ) {
                    }

                    override fun onTextChanged(
                        s: CharSequence,
                        start: Int,
                        before: Int,
                        count: Int
                    ) {
                        Log.d("customInputField", customInputField[i].id.toString())
                        customFieldQuality(s)
                    }
                })
            }!!)
        }

        /**
         * subject filed
         */
        subjectTypeSpinner = createSpinner()
        for (i in customSelectBoxField.indices) {
            customSelectBoxViewList.add(
                createSpinner().also {
                    val myAdapter =
                        context?.let { it1 ->
                            customSelectBoxField[i].options?.let { customSelectBoxField ->
                                Desk360CustomSupportTypeAdapter(
                                    it1,
                                    R.layout.desk360_type_dropdown,
                                    customSelectBoxField
                                )
                            }
                        }
                    it?.adapter = myAdapter
                }!!
            )
        }

        /**
         * message filed
         */
        messageField = createCustomTextArea("Message")
        messageField?.maxLines = 6
        messageField?.minLines = 6

        if ((customTextAreaField.isNotEmpty())) {
            messageField?.imeOptions = EditorInfo.IME_ACTION_NEXT
        } else {
            messageField?.imeOptions = EditorInfo.IME_ACTION_DONE
        }

        messageField?.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {}
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                messageQuality(s)
            }
        })
        Desk360CustomStyle.setFontWeight(binding.createScreenButtonText,context,Desk360Constants.currentType?.data?.create_screen?.button_text_font_weight)
        Desk360CustomStyle.setStyle(Desk360Constants.currentType?.data?.create_screen?.button_style_id,binding.createTicketButton,context!!)
        binding.createScreenButtonIcon.setImageResource(R.drawable.zarf)
        binding.createScreenButtonIcon.setColorFilter(
            Color.parseColor(Desk360Constants.currentType?.data?.create_screen?.button_text_color),
            PorterDuff.Mode.SRC_ATOP
        )
        Desk360CustomStyle.setFontWeight(binding.textFooterCreateTicketScreen,context,Desk360Constants.currentType?.data?.general_settings?.bottom_note_font_weight)
        messageField?.gravity = Gravity.TOP

        for (i in customTextAreaField.indices) {
            customTextAreaViewList.add(createCustomTextArea("custom").also {
                it?.maxLines = 6
                it?.minLines = 6
                if (i == customTextAreaField.size) {
                    messageField?.imeOptions = EditorInfo.IME_ACTION_DONE
                } else {
                    messageField?.imeOptions = EditorInfo.IME_ACTION_NEXT
                }
                it?.addTextChangedListener(object : TextWatcher {
                    override fun afterTextChanged(s: Editable) {}
                    override fun beforeTextChanged(
                        s: CharSequence,
                        start: Int,
                        count: Int,
                        after: Int
                    ) {
                    }

                    override fun onTextChanged(
                        s: CharSequence,
                        start: Int,
                        before: Int,
                        count: Int
                    ) {
                        messageQuality(s)
                    }
                })
                it?.gravity = Gravity.TOP

            }!!)
        }

        binding.viewModel = viewModel
    }

    private fun createEditText(hintText: String, isMessage: Boolean = false): TextInputEditText? {
        if (context == null)
            return null
        val textInputLayout = TextInputLayout(context!!)
        textInputLayout.hint = hintText
        textInputLayout.layoutParams = rootParamsLayout
        textInputLayout.gravity = Gravity.CENTER_VERTICAL

        editTextStyleModel?.let { textInputLayout.setDesk360InputStyle(it) }
        if (editTextStyleModel!!.form_style_id == 1 && isMessage) {
            textInputLayout.setPadding(24, 8, 24, 8)
            textInputLayout.boxBackgroundMode = TextInputLayout.BOX_BACKGROUND_OUTLINE
        }

        val textInputEditText = TextInputEditText(context)
        textInputEditText.setDesk360InputStyle(editTextStyleModel)

        textInputLayout.addView(textInputEditText)

        if (editTextStyleModel.form_style_id == 3) {
            val cardView = CardView(context!!)
            cardView.layoutParams = rootParamsLayout
            cardView.setDesk360CardViewStyle()
            cardView.addView(textInputLayout)
            binding.createScreenRootView.addView(cardView)
        } else {
            binding.createScreenRootView.addView(textInputLayout)
        }

        return textInputEditText
    }

    private fun createCustomTextArea(
        hintText: String
    ): TextInputEditText? {
        if (context == null)
            return null
        val textInputLayout = TextInputLayout(context!!)
        textInputLayout.hint = hintText
        textInputLayout.layoutParams = rootParamsLayout
        textInputLayout.gravity = Gravity.TOP and Gravity.START

        editTextStyleModel?.let { textInputLayout.setDesk360TextAreaStyle(it) }

        val textInputEditText = TextInputEditText(context)
        editTextStyleModel?.let { textInputEditText.setDesk360TextAreaStyle(it) }

        textInputLayout.addView(textInputEditText)

        if (editTextStyleModel?.form_style_id == 3) {
            val cardView = CardView(context!!)
            cardView.layoutParams = rootParamsLayout
            cardView.setDesk360CardViewStyle()
            cardView.addView(textInputLayout)
            binding.createScreenRootView.addView(cardView)
        } else {
            binding.createScreenRootView.addView(textInputLayout)
        }

        return textInputEditText
    }

    private fun createSpinner(): Spinner? {
        if (context == null)
            return null
        val textInputLayoutSpinner = TextInputLayout(context!!)
        textInputLayoutSpinner.layoutParams = rootParamsLayout
        textInputLayoutSpinner.gravity = Gravity.CENTER_VERTICAL

        val spinner = Spinner(context)
        editTextStyleModel?.let { spinner.setDesk360SpinnerStyle(it) }
        textInputLayoutSpinner.addView(spinner)

        when (editTextStyleModel?.form_style_id) {
            3 -> {
                val cardView = CardView(context!!)
                cardView.layoutParams = rootParamsLayout
                cardView.setDesk360CardViewStyle()
                cardView.addView(textInputLayoutSpinner)
                binding.createScreenRootView.addView(cardView)
            }
            2 -> {
                binding.createScreenRootView.addView(textInputLayoutSpinner)
            }
            else -> {
                binding.createScreenRootView.addView(textInputLayoutSpinner)

                val underline = View(context)
                underline.setBackgroundColor(Color.parseColor(editTextStyleModel?.form_input_border_color))
                val underlineParamsLayout = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, 2
                )
                underlineParamsLayout.leftMargin = 24
                underlineParamsLayout.rightMargin = 24
                underline.layoutParams = underlineParamsLayout
                binding.createScreenRootView.addView(underline)
            }
        }

        return spinner
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel?.let {
            with(viewModel!!) {
                typeList?.removeObserver(observer)
//              subjectFieldFill.removeObserver(observerSubject)
                addedTicket.removeObserver(observerAddedTicket)
            }
        }
    }

    fun nameQuality(s: CharSequence) {
        nameData = s.toString().trim()
        nameFieldFill = when {
            s.isEmpty() -> {
                false
            }
            else -> {
                true
            }
        }
    }

    fun customFieldQuality(s: CharSequence) {
        nameData = s.toString().trim()
        nameFieldFill = when {
            s.isEmpty() -> {
                false
            }
            else -> {
                true
            }
        }
    }

    fun emailQuality(s: CharSequence) {
        emailData = s.toString().trim()
        emailFieldFill = when {
            s.isEmpty() -> false
            !checkEmail(email = s.toString()) -> false
            else -> {
                true
            }
        }
    }

    fun messageQuality(s: CharSequence) {
        messageData = s.toString().trim()
        messageLength = messageData!!.length
        messageFieldFill = when {
            s.isEmpty() -> false
            s.length < 3 -> false
            else -> {
                true
            }
        }
    }

    private fun checkEmail(email: String): Boolean {
        return EMAIL_ADDRESS_PATTERN.matcher(email).matches()
    }


    val EMAIL_ADDRESS_PATTERN = Pattern.compile(
        "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
                "\\@" +
                "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                "(" +
                "\\." +
                "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
                ")+"
    )

    private fun validateAllField() {
        val ticketItem = HashMap<String, String>()
        if (nameFieldFill && emailFieldFill && messageLength > 0) {
            var isExistEmptyCustomField = false
            for (i in 0 until customInputViewList.size) {
                ticketItem[customInputField[i].id.toString()] = customInputViewList[i].text.toString()

                if (customInputViewList[i].text?.isNotEmpty() != true) {
                    customInputViewList[i].isEnabled = true
                    customInputViewList[i].requestFocus()
                    customInputViewList[i].onKeyUp(
                        KEYCODE_DPAD_CENTER,
                        KeyEvent(ACTION_UP, KEYCODE_DPAD_CENTER)
                    )
                    isExistEmptyCustomField = true
                    break
                }
            }
            if (isExistEmptyCustomField) {
                return
            }
            for (i in customTextAreaViewList.indices) {
                ticketItem[customTextAreaViewList[i].id.toString()] =
                    customTextAreaViewList[i].text.toString()

                if (customTextAreaViewList[i].text?.isNotEmpty() != true) {
                    customTextAreaViewList[i].isEnabled = true
                    customTextAreaViewList[i].requestFocus()
                    customTextAreaViewList[i].onKeyUp(
                        KEYCODE_DPAD_CENTER,
                        KeyEvent(ACTION_UP, KEYCODE_DPAD_CENTER)
                    )
                    isExistEmptyCustomField = true
                    break
                }
            }
            if (isExistEmptyCustomField) {
                return
            }

            ticketItem["email"] = emailData!!
            ticketItem["name"] = nameData!!
            ticketItem["message"] = messageData!!
            ticketItem["type_id"] = selectedTypeId.toString()
            ticketItem["source"] = "App"
            ticketItem["platform"] = "Android"
            ticketItem["country_code"] = Desk360Constants.countryCode()





            viewModel?.addSupportTicket(ticketItem)
        } else when {
            !nameFieldFill -> {
                nameFieldFill = false
                observerName()
            }
            !emailFieldFill -> {
                emailFieldFill = false
                observerEMail()
            }
            messageLength <= 0 -> {
                messageFieldFill = false
                observerMessage()
            }
        }
    }
}

fun CardView.setDesk360CardViewStyle() {
    this.cardElevation = 20f
    this.radius = 8f
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
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        this.backgroundTintList = colorStateList
    }
    this.defaultHintTextColor = colorStateList
    this.hintTextColor = colorStateList
    this.boxBackgroundMode = TextInputLayout.BOX_BACKGROUND_NONE

    when (style.form_style_id) {
        1 -> {
            //line
            this.boxBackgroundMode = TextInputLayout.BOX_BACKGROUND_NONE
        }

        2 -> {
            //box
            this.boxBackgroundMode = TextInputLayout.BOX_BACKGROUND_OUTLINE
        }

        else -> {
            //shadow
            this.boxBackgroundMode = TextInputLayout.BOX_BACKGROUND_NONE
        }
    }
}

fun TextInputLayout.setDesk360TextAreaStyle(style: Desk360ScreenCreate) {
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
    this.defaultHintTextColor = colorHintStateList
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        this.backgroundTintList = colorStateList
    }
    this.hintTextColor = colorHintStateList

    when (style.form_style_id) {
        3 -> {
            //shadow
            this.boxBackgroundMode = TextInputLayout.BOX_BACKGROUND_NONE
        }

        else -> {
            //box
            this.boxBackgroundMode = TextInputLayout.BOX_BACKGROUND_OUTLINE
        }
    }
}

fun TextInputEditText.setDesk360InputStyle(style: Desk360ScreenCreate) {
    this.setTextColor(Color.parseColor(style.form_input_focus_color))

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
                    Color.parseColor(style.form_input_focus_color),
                    Color.parseColor(style.form_input_focus_color),
                    Color.parseColor(style.form_input_focus_color),
                    Color.parseColor(style.form_input_focus_color)
                )
            )
            supportBackgroundTintList = colorStateList
            supportBackgroundTintMode = PorterDuff.Mode.SRC_ATOP
        }
        2 -> {
            //box
            this.background = null
            this
                .setPadding(24, 24, 24, 24)
        }
        else -> {
            //shadow
            this.background = null
        }
    }
}

fun TextInputEditText.setDesk360TextAreaStyle(style: Desk360ScreenCreate) {
    this.inputType = InputType.TYPE_TEXT_FLAG_IME_MULTI_LINE
    this.setTextColor(Color.parseColor(style.form_input_focus_color))
    this.background = null
    this.setPadding(24, 24, 24, 24)
}

fun Spinner.setDesk360SpinnerStyle(style: Desk360ScreenCreate) {
    this.setPadding(24, 20, 24, 20)
    when (style.form_style_id) {
        1 -> {
            //line
            this.setPadding(0, 10, 24, 0)
        }

        2 -> {
            val gd = GradientDrawable()
            gd.setColor(Color.TRANSPARENT)
            gd.cornerRadius = 8f
            gd.setStroke(3, Color.parseColor(style.form_input_focus_border_color))
            this.background = gd
            //box
        }

        else -> {
            //shadow
        }
    }

    this.isActivated = style.added_file_is_hidden
}
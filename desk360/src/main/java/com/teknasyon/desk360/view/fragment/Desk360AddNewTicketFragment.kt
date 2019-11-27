package com.teknasyon.desk360.view.fragment

import android.content.Context.INPUT_METHOD_SERVICE
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.*
import android.view.KeyEvent.ACTION_UP
import android.view.KeyEvent.KEYCODE_DPAD_CENTER
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView
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
import com.teknasyon.desk360.model.Desk360TicketReq
import com.teknasyon.desk360.model.Desk360Type
import com.teknasyon.desk360.modelv2.Desk360ScreenCreate
import com.teknasyon.desk360.view.adapter.Desk360SupportTypeAdapter
import com.teknasyon.desk360.viewmodel.AddNewTicketViewModel
import kotlinx.android.synthetic.main.desk360_add_new_ticket_layout.view.*
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
    private lateinit var binding: Desk360AddNewTicketLayoutBinding
    private var typeList: ArrayList<Desk360Type>? = null
    private val editTextStyleModel =
        Desk360Config.instance.getDesk360Preferences()?.types?.data?.create_screen

    private var selectedTypeId: Int = 1

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
//        binding.subjectType?.prompt = "Gender"
        subjectTypeSpinner?.onItemSelectedListener =
            (object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {

                }

                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    typeList?.let { it[position].let { it1 -> selectedTypeId = it1.id!! } }
                }
            })
//        binding.txtBottomFooterMessageForm?.movementMethod = ScrollingMovementMethod()
        binding.createTicketButton?.setOnClickListener {
            validateAllField(selectedTypeId)
        }
        rootParamsLayout.setMargins(24, 24, 24, 24)
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
        /**
         * subject filed
         */
        subjectTypeSpinner = createSpinner()
        /**
         * message filed
         */
        messageField = createEditText("Message", true)
        messageField?.maxLines = 7
        messageField?.minLines = 6
        messageField?.imeOptions = EditorInfo.IME_ACTION_DONE
        messageField?.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {}
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                messageQuality(s)
            }
        })
        messageField?.gravity = Gravity.TOP
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
            binding.root.create_screen_root_view.addView(cardView)
        } else {
            binding.root.create_screen_root_view.addView(textInputLayout)
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
                binding.root.create_screen_root_view.addView(cardView)
            }
            2 -> {
                binding.root.create_screen_root_view.addView(textInputLayoutSpinner)
            }
            else -> binding.root.create_screen_root_view.addView(textInputLayoutSpinner)
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
        nameFieldFill =
            when {
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
        emailFieldFill =
            when {
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
        messageFieldFill =
            when {
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

    private fun validateAllField(selectedTypeId: Int) {
        val ticketItem = Desk360TicketReq()
        if (nameFieldFill && emailFieldFill && messageLength > 0) {
            ticketItem.email = emailData
            ticketItem.name = nameData
//            ticketItem.subject = subjectData
            ticketItem.message = messageData
            ticketItem.type_id = selectedTypeId.toString()
            ticketItem.source = "App"
            ticketItem.platform = "Android"
            ticketItem.country_code = Desk360Constants.countryCode()
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
    val colorsHint = intArrayOf(
        Color.parseColor(style.label_text_color),
        Color.parseColor(style.form_input_border_color),
        Color.parseColor(style.form_input_border_color),
        Color.parseColor(style.label_text_color)
    )
    val defaultHintColors = intArrayOf(
        Color.parseColor(style.form_input_color),
        Color.parseColor(style.form_input_color),
        Color.parseColor(style.form_input_color),
        Color.parseColor(style.form_input_color)
    )
    val myColorList = ColorStateList(states, colors)
    val myColorListDefault = ColorStateList(states, defaultHintColors)
    val myColorListHint = ColorStateList(states, colorsHint)
    this.setBoxStrokeColorStateList(myColorList)
    this.defaultHintTextColor = myColorListDefault
    this.hintTextColor = myColorListHint
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

fun TextInputEditText.setDesk360InputStyle(style: Desk360ScreenCreate) {
    val drawable: Drawable = this.background
    this.setTextColor(Color.parseColor(style.form_input_focus_color))
    when (style.form_style_id) {
        1 -> {
            //line
            drawable.setColorFilter(Color.RED, PorterDuff.Mode.SRC_ATOP)
        }
        2 -> {
            //box
            this.background = null
            this.setPadding(24, 24, 24, 24)
        }
        else -> {
            //shadow
            this.background = null
        }
    }
}

fun Spinner.setDesk360SpinnerStyle(style: Desk360ScreenCreate) {
    this.setPadding(24, 20, 24, 20)
    when (style.form_style_id) {
        1 -> {
            //line
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
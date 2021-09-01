package com.teknasyon.desk360.view.fragment

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Context.INPUT_METHOD_SERVICE
import android.content.DialogInterface
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.Rect
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.provider.MediaStore
import android.text.Editable
import android.text.InputFilter
import android.text.InputType
import android.text.TextWatcher
import android.text.method.LinkMovementMethod
import android.util.Log
import android.util.Patterns
import android.view.*
import android.view.KeyEvent.ACTION_UP
import android.view.KeyEvent.KEYCODE_DPAD_CENTER
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.text.HtmlCompat
import androidx.documentfile.provider.DocumentFile
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import com.teknasyon.desk360.R
import com.teknasyon.desk360.databinding.Desk360AddNewTicketLayoutBinding
import com.teknasyon.desk360.helper.*
import com.teknasyon.desk360.model.CacheTicket
import com.teknasyon.desk360.model.Desk360TicketResponse
import com.teknasyon.desk360.model.Desk360Type
import com.teknasyon.desk360.modelv2.Desk360CustomFields
import com.teknasyon.desk360.modelv2.Desk360Options
import com.teknasyon.desk360.view.activity.Desk360BaseActivity
import com.teknasyon.desk360.view.adapter.Desk360CustomSupportTypeAdapter
import com.teknasyon.desk360.view.adapter.Desk360SupportTypeAdapter
import com.teknasyon.desk360.viewmodel.AddNewTicketViewModel
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.io.FileOutputStream

/**
 * Created by seyfullah on 30,May,2019
 *
 */

open class Desk360AddNewTicketFragment : Fragment(),
    Desk360BottomSheetDialogFragment.BottomSheetListener {

    companion object {
        private const val MESSAGE_MIN_LENGTH = 3
        private const val MESSAGE_MAX_LENGTH = 5000
        private const val NAME_AND_EMAIL_MAX_LENGTH = 100
    }

    private var viewModel: AddNewTicketViewModel? = null
    private var nameField: TextInputViewGroup? = null
    private var eMailField: TextInputViewGroup? = null
    private var messageField: TextAreaViewGroup? = null
    private var subjectTypeSpinner: SelectBoxViewGroup? = null
    private var selectedTypeId = 1

    private var binding: Desk360AddNewTicketLayoutBinding? = null

    private var typeList: ArrayList<Desk360Type>? = null

    private val editTextStyleModel =
        Desk360Config.instance.getDesk360Preferences()?.types?.data?.create_screen
    private var customInputField: List<Desk360CustomFields> = arrayListOf()
    private var customSelectBoxField: List<Desk360CustomFields> = arrayListOf()
    private var customTextAreaField: List<Desk360CustomFields> = arrayListOf()

    private var customInputViewList: ArrayList<TextInputViewGroup> = arrayListOf()
    private var customSelectBoxViewList: ArrayList<SelectBoxViewGroup> = arrayListOf()
    private var customTextAreaViewList: ArrayList<TextAreaViewGroup> = arrayListOf()
    private var myAdapter: Desk360SupportTypeAdapter? = null

    //Validate variables
    private var nameData: String? = null
    private var emailData: String? = null
    private var messageData: String? = null
    private var messageLength = 0
    private var nameFieldFill = false
    private var emailFieldFill = false
    private var messageFieldFill = false
    private var selectedItem = false
    private val listOfType: ArrayList<String> = arrayListOf()
    private var invalidEmail = false

    private lateinit var activity: Desk360BaseActivity

    private var RESULT_LOAD_FILES = 1221
    var params: HashMap<String, RequestBody> = HashMap()
    var file: File? = null
    var fileName: String? = null

    private var errorLabelTextColor: ColorStateList? = null

    private val rootParamsLayout = LinearLayout.LayoutParams(
        LinearLayout.LayoutParams.MATCH_PARENT,
        LinearLayout.LayoutParams.WRAP_CONTENT
    )

    private val preferencesManager = PreferencesManager()

    private fun observerName() {
        nameField?.holder?.textInputEditText?.isEnabled = true
        nameField?.holder?.textInputEditText?.requestFocus()
        nameField?.holder?.textInputEditText?.onKeyUp(
            KEYCODE_DPAD_CENTER,
            KeyEvent(ACTION_UP, KEYCODE_DPAD_CENTER)
        )
    }

    private fun observerEMail() {
        eMailField?.holder?.textInputEditText?.isEnabled = true
        eMailField?.holder?.textInputEditText?.requestFocus()
        eMailField?.holder?.textInputEditText?.onKeyUp(
            KEYCODE_DPAD_CENTER,
            KeyEvent(ACTION_UP, KEYCODE_DPAD_CENTER)
        )
    }

    private fun observerMessage() {
        messageField?.holder?.textAreaEditText?.isEnabled = true
        messageField?.holder?.textAreaEditText?.requestFocus()
        messageField?.holder?.textAreaEditText?.onKeyUp(
            KEYCODE_DPAD_CENTER,
            KeyEvent(ACTION_UP, KEYCODE_DPAD_CENTER)
        )
    }

    private var observerAddedTicket = Observer<Desk360TicketResponse> {
        if (it != null) {
            view?.let { _ ->
                remove()
                findNavController().navigate(
                    when (Desk360Constants.manager?.enableHelpMode) {
                        true -> Desk360AddNewTicketFragmentDirections.actionAddNewTicketFragmentToThanksFragment()
                        else -> Desk360AddNewTicketFragmentDirections.actionAddNewTicketFragmentToTicketListFragment()
                    },
                    NavOptions.Builder().setPopUpTo(R.id.addNewTicketFragment, true).build()
                )
            }

            val imm = activity.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view!!.windowToken, 0)
        }
        binding?.createTicketButton?.isClickable = true
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Desk360AddNewTicketLayoutBinding.inflate(inflater, container, false).also {
            binding = it
            binding?.lifecycleOwner = viewLifecycleOwner
            return it.root
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        activity.changeMainUI()

        activity.window?.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
        activity.binding.contactUsMainBottomBar.visibility = View.VISIBLE

        keyboardListener()

        viewModel = AddNewTicketViewModel()

        typeList = Desk360Config.instance.getDesk360Preferences()?.types?.data?.create_screen?.types
        viewModel?.addedTicket?.observe(this, observerAddedTicket)

        viewModel?.error?.observe(this, Observer<String> { t ->
            if (t != null) {
                Toast.makeText(view.context, t, Toast.LENGTH_LONG).show()
                viewModel?.error?.value = null
                binding?.loadingProgress?.visibility = View.GONE
                activity.window?.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
            }
        })

        listOfType.clear()

        listOfType.add("")
        typeList?.let {
            for (i in 0 until it.size) {
                listOfType.add(it[i].title.toString())
            }
        }
        myAdapter = context?.let { it1 ->
            Desk360SupportTypeAdapter(
                it1,
                R.layout.desk360_type_dropdown,
                listOfType
            )
        }

        binding?.createTicketButton?.setOnClickListener {
            binding?.createTicketButton?.isClickable = false
            validateAllField()
        }

        val createScreen = Desk360Constants.currentType?.data?.create_screen

        binding?.formConfirm?.visibility = if (createScreen?.form_confirm_is_hidden == true)
            View.VISIBLE
        else
            View.GONE

        binding?.formConfirmText?.text = if (createScreen?.form_confirm_link.isNullOrBlank())
            createScreen?.form_confirm_text
        else if (createScreen?.form_confirm_link?.startsWith("http://") == true || createScreen?.form_confirm_link?.startsWith(
                "https://"
            ) == true
        )
            HtmlCompat.fromHtml(
                """<a href="${createScreen.form_confirm_link}">${createScreen.form_confirm_text}</a>""",
                HtmlCompat.FROM_HTML_MODE_COMPACT
            )
        else
            HtmlCompat.fromHtml(
                """<a href="http://${createScreen?.form_confirm_link}">${createScreen?.form_confirm_text}</a>""",
                HtmlCompat.FROM_HTML_MODE_COMPACT
            )

        Desk360Constants.currentType?.data?.create_screen?.form_input_color?.let { color ->
            binding?.formConfirmText?.setTextColor(Color.parseColor(color))
            binding?.formConfirmText?.setLinkTextColor(Color.parseColor(color))
        }

        binding?.formConfirmText?.movementMethod = LinkMovementMethod.getInstance()
        createScreen?.button_background_color?.let {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                binding?.formConfirmCheckbox?.buttonTintList =
                    ColorStateList.valueOf(Color.parseColor(it))
            }
        }

        binding?.formConfirmCheckbox?.setOnCheckedChangeListener { _, isChecked ->
            binding?.createTicketButton?.isEnabled = isChecked

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                if (!isChecked) {
                    binding?.createTicketButton?.backgroundTintMode = PorterDuff.Mode.OVERLAY
                    binding?.createTicketButton?.backgroundTintList =
                        ColorStateList.valueOf(Color.LTGRAY)

                    binding?.createScreenButtonIcon?.backgroundTintMode = PorterDuff.Mode.OVERLAY
                    binding?.createScreenButtonIcon?.backgroundTintList =
                        ColorStateList.valueOf(Color.LTGRAY)

                    binding?.createScreenButtonText?.backgroundTintMode = PorterDuff.Mode.OVERLAY
                    binding?.createScreenButtonText?.backgroundTintList =
                        ColorStateList.valueOf(Color.LTGRAY)
                } else {
                    binding?.createTicketButton?.backgroundTintList = null
                    binding?.createScreenButtonIcon?.backgroundTintList = null
                    binding?.createScreenButtonText?.backgroundTintList = null
                }

            }
        }

        if (binding?.formConfirm?.visibility == View.VISIBLE) {
            binding?.createTicketButton?.isEnabled = false

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                binding?.createTicketButton?.backgroundTintMode = PorterDuff.Mode.OVERLAY
                binding?.createTicketButton?.backgroundTintList =
                    ColorStateList.valueOf(Color.LTGRAY)

                binding?.createScreenButtonIcon?.backgroundTintMode = PorterDuff.Mode.OVERLAY
                binding?.createScreenButtonIcon?.backgroundTintList =
                    ColorStateList.valueOf(Color.LTGRAY)

                binding?.createScreenButtonText?.backgroundTintMode = PorterDuff.Mode.OVERLAY
                binding?.createScreenButtonText?.backgroundTintList =
                    ColorStateList.valueOf(Color.LTGRAY)
            }
        }

        binding?.fileNameIcon?.setOnClickListener {
            file = null
            binding?.fileNameIcon?.visibility = View.INVISIBLE
            binding?.fileNameTextCreateTicketScreen?.visibility = View.INVISIBLE
        }

        binding?.fileNameTextCreateTicketScreen?.visibility = View.INVISIBLE
        binding?.fileNameIcon?.visibility = View.INVISIBLE

        binding?.textPathCreateTicketScreen?.setOnClickListener {
            val bottomDialog = Desk360BottomSheetDialogFragment(this)
            fragmentManager?.let { it1 -> bottomDialog.show(it1, "bottomSheet") }
        }

        rootParamsLayout.setMargins(24, 24, 24, 24)

        val colors = ArrayCreator.createSingleArray(2)
        colors[0] = Color.parseColor(editTextStyleModel?.error_label_text_color)

        val colorStateList = ColorStateList(ArrayCreator.createDoubleArray(1, 1), colors)

        errorLabelTextColor = colorStateList

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
        nameField = TextInputViewGroup(editTextStyleModel!!, this@Desk360AddNewTicketFragment)

        binding?.createScreenRootView?.addView(
            nameField?.createEditText(
                Desk360Constants.currentType?.data?.general_settings?.name_field_text ?: "Name"
            )
        )

        nameField?.holder?.textInputEditText?.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {}
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                nameQuality(s)
            }
        })

        if (Desk360Constants.manager?.name?.isNotEmpty() == true) {
            nameField?.holder?.textInputEditText?.setText(Desk360Constants.manager?.name)
        }

        /**
         * email filed
         */
        eMailField = TextInputViewGroup(editTextStyleModel, this@Desk360AddNewTicketFragment)
        binding?.createScreenRootView?.addView(
            eMailField?.createEditText(
                Desk360Constants.currentType?.data?.general_settings?.email_field_text ?: "Email"
            )
        )
        eMailField?.holder?.textInputEditText?.inputType =
            InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS

        eMailField?.holder?.textInputEditText?.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {}
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                emailQuality(s)
            }
        })

        if (Desk360Constants.manager?.emailAddress?.isNotEmpty() == true) {
            eMailField?.holder?.textInputEditText?.setText(Desk360Constants.manager?.emailAddress)
        }

        for (i in customInputField.indices) {
            val customInputViewGroup =
                TextInputViewGroup(editTextStyleModel, this@Desk360AddNewTicketFragment)
            binding?.createScreenRootView?.addView(
                customInputViewGroup.createEditText(customInputField[i].place_holder ?: "")
            )

            customInputViewList.add(customInputViewGroup)
        }

        /**
         * subject filed
         */
        subjectTypeSpinner =
            SelectBoxViewGroup(editTextStyleModel, this@Desk360AddNewTicketFragment)

        binding?.createScreenRootView?.addView(subjectTypeSpinner?.createSpinner())

        subjectTypeSpinner?.holder?.selectBox?.onItemSelectedListener =
            (object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {

                }

                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View?,
                    position: Int,
                    id: Long
                ) {

                    if (position == 0) {
                        selectedItem = false

                        if (editTextStyleModel.form_style_id == 3) {

                            view?.setBackgroundColor(Color.parseColor(editTextStyleModel.form_input_background_color))
                            editTextStyleModel.form_input_border_color?.let {
                                subjectTypeSpinner?.holder?.shadowBorder?.setStroke(
                                    it
                                )
                            }
                            subjectTypeSpinner?.holder?.selectBoxCardView?.setCardBackgroundColor(
                                Color.parseColor(editTextStyleModel.form_input_background_color)
                            )
                        }
                        return
                    }

                    if (editTextStyleModel.form_style_id == 3) {

                        editTextStyleModel.form_input_focus_border_color?.let {
                            subjectTypeSpinner?.holder?.shadowBorder?.setStroke(
                                it
                            )
                        }
                        view?.setBackgroundColor(Color.parseColor(editTextStyleModel.form_input_focus_background_color))
                        subjectTypeSpinner?.holder?.selectBoxCardView?.setCardBackgroundColor(
                            Color.parseColor(
                                editTextStyleModel.form_input_focus_background_color
                            )
                        )
                    }

                    selectedItem = true
                    typeList?.let { it[position - 1].let { it1 -> selectedTypeId = it1.id!! } }
                    (subjectTypeSpinner?.holder?.selectBox?.selectedView as TextView).setTextColor(
                        Color.parseColor(editTextStyleModel.form_input_focus_color)
                    )
                }
            })

        subjectTypeSpinner?.holder?.selectBox?.adapter = myAdapter

        for (i in customSelectBoxField.indices) {

            val spinnerItem =
                SelectBoxViewGroup(editTextStyleModel, this@Desk360AddNewTicketFragment)

            binding?.createScreenRootView?.addView(spinnerItem.createSpinner())

            customSelectBoxViewList.add(spinnerItem)

            val optionsList = arrayListOf<Desk360Options>()
            optionsList.add(
                Desk360Options(
                    value = customSelectBoxField[i].place_holder,
                    order = -1
                )
            )
            customSelectBoxField[i].options?.let { it1 -> optionsList.addAll(it1) }

            val myAdapter = context?.let { it1 ->
                Desk360CustomSupportTypeAdapter(
                    it1,
                    R.layout.desk360_type_dropdown,
                    optionsList
                )
            }

            spinnerItem.holder.selectBox?.adapter = myAdapter

            spinnerItem.holder.selectBox?.onItemSelectedListener =
                (object : AdapterView.OnItemSelectedListener {
                    override fun onNothingSelected(parent: AdapterView<*>?) {
                    }

                    override fun onItemSelected(
                        parent: AdapterView<*>,
                        view: View?,
                        position: Int,
                        id: Long
                    ) {
                        if (position == 0) {

                            (spinnerItem.holder.selectBox?.selectedView as TextView).setTextColor(
                                Color.parseColor(editTextStyleModel.form_input_color)
                            )

                            if (editTextStyleModel.form_style_id == 3) {

                                view?.setBackgroundColor(Color.parseColor(editTextStyleModel.form_input_background_color))
                                editTextStyleModel.form_input_border_color?.let {
                                    spinnerItem.holder.shadowBorder?.setStroke(
                                        it
                                    )
                                }
                                spinnerItem.holder.selectBoxCardView?.setCardBackgroundColor(
                                    Color.parseColor(
                                        editTextStyleModel.form_input_background_color
                                    )
                                )
                            }

                            return
                        }

                        (spinnerItem.holder.selectBox?.selectedView as TextView).setTextColor(
                            Color.parseColor(
                                editTextStyleModel.form_input_focus_color
                            )
                        )

                        optionsList.run {
                            this[position].let { it1 ->
                                val customSelectboxValue = it1.value.toString()
                                    .toRequestBody("text/plain".toMediaTypeOrNull())

                                params[customSelectBoxField[i].name.toString()] =
                                    customSelectboxValue
                            }
                        }

                        if (editTextStyleModel.form_style_id == 3) {

                            editTextStyleModel.form_input_focus_border_color?.let {
                                spinnerItem.holder.shadowBorder?.setStroke(
                                    it
                                )
                            }
                            view?.setBackgroundColor(Color.parseColor(editTextStyleModel.form_input_focus_background_color))
                            spinnerItem.holder.selectBoxCardView?.setCardBackgroundColor(
                                Color.parseColor(editTextStyleModel.form_input_focus_background_color)
                            )
                        }
                    }
                })
        }

        /**
         * message filed
         */

        messageField = TextAreaViewGroup(editTextStyleModel, this@Desk360AddNewTicketFragment)

        binding?.createScreenRootView?.addView(
            messageField?.createEditText(
                Desk360Constants.currentType?.data?.general_settings?.message_field_text
                    ?: "Message"
            )
        )

        nameField?.holder?.apply {
            textInputLayout?.apply {
                setErrorTextColor(errorLabelTextColor)
                boxStrokeErrorColor = errorLabelTextColor
            }

            textInputEditText?.filters =
                arrayOf(InputFilter.LengthFilter(NAME_AND_EMAIL_MAX_LENGTH))
        }

        eMailField?.holder?.apply {
            textInputLayout?.apply {
                setErrorTextColor(errorLabelTextColor)
                boxStrokeErrorColor = errorLabelTextColor
            }

            textInputEditText?.filters =
                arrayOf(InputFilter.LengthFilter(NAME_AND_EMAIL_MAX_LENGTH))
        }

        messageField?.holder?.textAreaLayout?.apply {
            setErrorTextColor(errorLabelTextColor)
            boxStrokeErrorColor = errorLabelTextColor
        }

        messageField?.holder?.textAreaEditText?.apply {
            addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable) {}
                override fun beforeTextChanged(
                    s: CharSequence,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                    messageQuality(s)
                }
            })

            filters = arrayOf(InputFilter.LengthFilter(MESSAGE_MAX_LENGTH))
        }

        Util.setEditTextScrollable(messageField?.holder?.textAreaEditText!!)

        binding?.apply {
            Desk360CustomStyle.setFontWeight(
                createScreenButtonText,
                context,
                createScreen?.button_text_font_weight
            )
            Desk360CustomStyle.setStyle(
                createScreen?.button_style_id,
                createTicketButton,
                context!!
            )

           textPathCreateTicketScreen.text =
                Desk360Constants.currentType?.data?.general_settings?.add_file_text

            pathIconn.setImageResource(R.drawable.path_icon_desk360)
            pathIconn.setColorFilter(
                Color.parseColor(createScreen?.label_text_color),
                PorterDuff.Mode.SRC_ATOP
            )

            fileNameIcon.setBackgroundResource(R.drawable.document_cancel_icon)
            fileNameIcon.background.setColorFilter(
                Color.parseColor(createScreen?.form_input_color),
                PorterDuff.Mode.SRC_ATOP
            )

            if (createScreen?.added_file_is_hidden!!) {
                pathIconn.visibility = View.VISIBLE
            } else {
                pathIconn.visibility = View.INVISIBLE
            }

            createScreenButtonIcon.setImageResource(R.drawable.zarf)
            createScreenButtonIcon.setColorFilter(
                Color.parseColor(createScreen.button_text_color),
                PorterDuff.Mode.SRC_ATOP
            )
            Desk360CustomStyle.setFontWeight(
                textFooterCreateTicketScreen,
                context,
                Desk360Constants.currentType?.data?.general_settings?.bottom_note_font_weight
            )
        }

        for (i in customTextAreaField.indices) {

            val customTextAreaViewGroup =
                TextAreaViewGroup(editTextStyleModel, this@Desk360AddNewTicketFragment)
            binding?.createScreenRootView?.addView(
                customTextAreaViewGroup.createEditText(
                    customTextAreaField[i].place_holder ?: ""
                )
            )

            customTextAreaViewList.add(customTextAreaViewGroup)
        }

        binding?.viewModel = viewModel

        Handler().postDelayed({
            activity.setMainTitle(
                createScreen?.title
            )
        }, 35)
    }


    @SuppressLint("SetTextI18n")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {

            RESULT_LOAD_FILES -> {

                val pathUri = data?.data ?: return

                when (RESULT_LOAD_FILES) {
                    1221 -> {
                        file = File(pathUri.let { ImageFilePath().getUriRealPath(context!!, it) })
                        fileName = file?.name
                    }
                    1222 -> {
                        //file = File(pathUri?.let { getRealPathFromURI(it) })
                        val cachFile = File(
                            context!!.cacheDir, DocumentFile.fromSingleUri(
                                activity,
                                pathUri
                            )?.name?.replace(" ", "")
                        )
                        try {
                            val inputStream = DocumentFile.fromSingleUri(
                                activity,
                                pathUri
                            )?.uri?.let {
                                context!!.contentResolver.openInputStream(
                                    it
                                )
                            }
                            val outputStream = FileOutputStream(cachFile)
                            var read = 0
                            val maxBufferSize = 1 * 1024 * 1024
                            val bytesAvailable = inputStream?.available()
                            //int bufferSize = 1024;
                            val bufferSize = bytesAvailable?.let { Math.min(it, maxBufferSize) }
                            val buffers = bufferSize?.let { ByteArray(it) }
                            while (inputStream?.read(buffers).also {
                                    if (it != null) {
                                        read = it
                                    }
                                } != -1) {
                                outputStream.write(buffers, 0, read)
                            }
                            inputStream?.close()
                            outputStream.close()
                        } catch (e: Exception) {
                            Log.e("Exception", e.message.toString())
                        }

                        file = cachFile
                        fileName = file?.name
                    }
                    1223 -> {
                        file = File(pathUri.let { ImageFilePath().getUriRealPath(context!!, it) })
                        fileName = file?.name
                    }
                }

                file?.let {
                    if (it.exists()) {
                        if (!fileSizeIsHigh(it)) {
                            fileName?.length?.let { length ->
                                if (length > 10) {
                                    binding?.fileNameTextCreateTicketScreen?.text =
                                        fileName?.substring(0, 8) + "..."
                                } else {
                                    binding?.fileNameTextCreateTicketScreen?.text = fileName
                                }
                                binding?.fileNameTextCreateTicketScreen?.visibility = View.VISIBLE
                                binding?.fileNameIcon?.visibility = View.VISIBLE
                            }
                        } else {
                            showAlert()
                        }


                    }
                }
            }

        }
    }

    private fun showAlert() {
        activity.let {
            val alert = AlertDialog.Builder(it)
            alert.setMessage(Desk360Constants.currentType?.data?.general_settings?.file_size_error_text)
                ?: ""
            alert.setCancelable(false)
            alert.setNegativeButton(getString(R.string.ok_button)) { _: DialogInterface, _: Int ->
                file = null
            }
            alert.show()
        }

    }

    private fun remove(): Boolean {
        return try {
            if (file?.exists() == true) file?.delete()

            true
        } catch (e: Throwable) {
            e.printStackTrace()
            false
        }
    }

    private fun fileSizeIsHigh(file: File): Boolean {

        return ((file.length() / 1024) / 1024).toInt() >= 20

    }

    override fun onDestroy() {
        super.onDestroy()
        activity.window?.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
        viewModel?.let {
            with(viewModel!!) {
                //typeList?.removeObserver(observer)
//              subjectFieldFill.removeObserver(observerSubject)
                addedTicket.removeObserver(observerAddedTicket)
            }
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        activity = context as Desk360BaseActivity
    }

    fun nameQuality(s: CharSequence) {

        nameData = s.toString().trim()

        nameFieldFill = when {

            s.isEmpty() -> {
                nameField?.holder?.textInputLayout?.error =
                    Desk360Constants.currentType?.data?.general_settings?.required_field_message
                        ?: "Lütfen İsim Alanını Doldurunuz"
                nameField?.holder?.textInputLayout?.isErrorEnabled = true
                false
            }
            s.length < MESSAGE_MIN_LENGTH -> {
                nameField?.holder?.textInputLayout?.error =
                    "İsim bilgisi 3 karakterden küçük olamaz!"
                nameField?.holder?.textInputLayout?.isErrorEnabled = true
                false
            }
            else -> {
                nameField?.holder?.textInputLayout?.isErrorEnabled = false
                nameField?.holder?.textInputLayout?.error = null
                true
            }
        }
    }

    fun emailQuality(s: CharSequence) {

        emailData = s.toString().trim()

        emailFieldFill = when {
            s.isEmpty() -> {
                false
            }
            !checkEmail(email = s.toString()) -> {
                invalidEmail = true
                eMailField?.holder?.textInputLayout?.isErrorEnabled = true
                eMailField?.holder?.textInputLayout?.error =
                    Desk360Constants.currentType?.data?.general_settings?.required_email_field_message
                        ?: "Email Alanını Formatına Göre Giriniz."
                false
            }

            else -> {
                eMailField?.holder?.textInputLayout?.isErrorEnabled = false
                eMailField?.holder?.textInputLayout?.error = null
                invalidEmail = false
                true
            }
        }
    }

    fun messageQuality(s: CharSequence) {

        messageData = s.toString().trim()
        messageLength = messageData!!.length
        messageFieldFill = when {
            s.isEmpty() -> {
                messageField?.holder?.textAreaLayout?.error =
                    Desk360Constants.currentType?.data?.general_settings?.required_textarea_message
                        ?: "Mesaj Alanını Doldurunuz."
                messageField?.holder?.textAreaLayout?.isErrorEnabled = true
                false
            }
            s.length < MESSAGE_MIN_LENGTH -> {
                messageField?.holder?.textAreaLayout?.error =
                    "Mesaj bilgisi 3 karakterden küçük olamaz!"
                messageField?.holder?.textAreaLayout?.isErrorEnabled = true
                false
            }
            else -> {
                messageField?.holder?.textAreaLayout?.isErrorEnabled = false
                messageField?.holder?.textAreaLayout?.error = null
                true
            }
        }
    }

    private fun checkEmail(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun validateAllField() {
        if (nameFieldFill && emailFieldFill && messageLength >= MESSAGE_MIN_LENGTH && selectedItem) {
            for (i in 0 until customInputViewList.size) {
                val customInputData =
                    customInputViewList[i].holder.textInputEditText?.text.toString()
                        .toRequestBody("text/plain".toMediaTypeOrNull())
                params[customInputField[i].name.toString()] = customInputData
            }
            for (i in customTextAreaViewList.indices) {
                val customInputData =
                    customTextAreaViewList[i].holder.textAreaEditText?.text.toString()
                        .toRequestBody("text/plain".toMediaTypeOrNull())
                params[customTextAreaField[i].name.toString()] = customInputData
            }

            val json: MediaType? = "application/json; charset=utf-8".toMediaTypeOrNull()

            val email = emailData!!.toRequestBody("text/plain".toMediaTypeOrNull())
            val name = nameData.toString().toRequestBody("text/plain".toMediaTypeOrNull())
            val message = messageData.toString()
                .toRequestBody("text/plain".toMediaTypeOrNull())
            val typeId =
                selectedTypeId.toString().toRequestBody("text/plain".toMediaTypeOrNull())
            val source = "App".toRequestBody("text/plain".toMediaTypeOrNull())
            val platform =
                (if (Desk360Constants.manager?.platform == Platform.HUAWEI) "Huawei" else "Android").toRequestBody(
                    "text/plain".toMediaTypeOrNull()
                )
            val settings = Desk360Constants.manager?.jsonObject.toString().toRequestBody(json)
            val countryCode =
                Desk360Constants.countryCode().toUpperCase()
                    .toRequestBody("text/plain".toMediaTypeOrNull())
            val notificationToken =
                activity.notificationToken.toString()
                    .toRequestBody("text/plain".toMediaTypeOrNull())

            params["name"] = name
            params["email"] = email
            params["message"] = message
            params["type_id"] = typeId
            params["source"] = source
            params["platform"] = platform
            params["settings"] = settings
            params["country_code"] = countryCode

            if (!activity.notificationToken.isNullOrBlank()) {
                params["push_token"] = notificationToken
            }

            if (Desk360Constants.currentType?.data?.create_screen?.form_confirm_is_hidden == true) {
                params["confirm"] = (if (binding?.formConfirmCheckbox?.isChecked == true) "1" else "0")
                    .toRequestBody("text/plain".toMediaTypeOrNull())
            }

            binding?.loadingProgress?.visibility = View.VISIBLE
            activity.window?.setFlags(
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
            )
            viewModel?.addSupportTicket(params, file, RESULT_LOAD_FILES)

        } else {
            when {
                !nameFieldFill -> {
                    nameField?.holder?.textInputLayout?.error =
                        Desk360Constants.currentType?.data?.general_settings?.required_field_message
                            ?: "Lütfen İsim Alanını Doldurunuz"
                    nameFieldFill = false
                    observerName()
                }
                !emailFieldFill -> {
                    if (invalidEmail)
                        eMailField?.holder?.textInputLayout?.error =
                            Desk360Constants.currentType?.data?.general_settings?.required_email_field_message
                                ?: "Email Alanını Formatına Göre Giriniz."
                    else
                        eMailField?.holder?.textInputLayout?.error =
                            Desk360Constants.currentType?.data?.general_settings?.required_email_field_message
                                ?: "Email Alanını Lütfen Boş Bırakmayın."
                    emailFieldFill = false
                    observerEMail()
                }
                !selectedItem -> {
                    selectedItem = false
                    subjectTypeSpinner?.holder?.selectBox?.performClick()
                }
                messageLength < MESSAGE_MIN_LENGTH -> {
                    messageField?.holder?.textAreaLayout?.error =
                        "Mesaj bilgisi 3 karakterden küçük olamaz!"
                    messageFieldFill = false
                    observerMessage()
                }
            }
            binding?.createTicketButton?.isClickable = true
        }
    }

    private fun keyboardListener() {

        val constraintLayout = binding?.baseLayout
        constraintLayout?.viewTreeObserver?.addOnGlobalLayoutListener {

            val rec = Rect()
            constraintLayout.getWindowVisibleDisplayFrame(rec)

            //finding screen height
            val screenHeight = constraintLayout.rootView?.height

            //finding keyboard height
            val keypadHeight = screenHeight?.minus(rec.bottom)

            if (screenHeight != null) {
                if (keypadHeight != null) {
                    if (keypadHeight > screenHeight * 0.15) {
                        activity.binding.contactUsMainBottomBar.visibility = View.GONE
                    } else {

                        if (!activity.isTicketDetailFragment) {
                            activity.binding.contactUsMainBottomBar.visibility = View.VISIBLE
                        }
                    }
                }
            }
        }
    }

    override fun onButtonClicked(typeOfAttachment: Int) {
        Dexter.withActivity(activity)
            .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
            .withListener(object : PermissionListener {
                override fun onPermissionGranted(response: PermissionGrantedResponse) {
                    if (typeOfAttachment == 0) {
                        RESULT_LOAD_FILES = 1221
                        startActivityForResult(
                            Intent(
                                Intent.ACTION_PICK,
                                MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                            ), RESULT_LOAD_FILES
                        )
                    } else if (typeOfAttachment == 2) {
                        RESULT_LOAD_FILES = 1222
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                            val photoPickerIntent = Intent(Intent.ACTION_OPEN_DOCUMENT)
                            photoPickerIntent.type = "application/pdf"
                            startActivityForResult(photoPickerIntent, RESULT_LOAD_FILES)
                        }
                    } else {
                        RESULT_LOAD_FILES = 1223
                        val intent = Intent()
                        intent.type = "video/.mp4"
                        intent.action = Intent.ACTION_GET_CONTENT
                        startActivityForResult(
                            Intent.createChooser(intent, "Select Video"),
                            RESULT_LOAD_FILES
                        )
                    }
                }

                override fun onPermissionDenied(response: PermissionDeniedResponse) {

                }

                override fun onPermissionRationaleShouldBeShown(
                    permission: PermissionRequest,
                    token: PermissionToken
                ) {
                    token.continuePermissionRequest()
                }
            })
            .check()
    }
}
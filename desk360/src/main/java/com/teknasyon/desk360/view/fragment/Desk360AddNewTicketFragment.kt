package com.teknasyon.desk360.view.fragment

import android.Manifest
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
import android.provider.MediaStore
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
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
import androidx.core.content.ContextCompat
import androidx.documentfile.provider.DocumentFile
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.NavOptions
import androidx.navigation.Navigation
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
import kotlinx.android.synthetic.main.desk360_fragment_main.*
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

    private var viewModel: AddNewTicketViewModel? = null
    private var nameField: TextInputViewGroup? = null
    private var eMailField: TextInputViewGroup? = null
    private var messageField: TextAreaViewGroup? = null
    private var subjectTypeSpinner: SelectBoxViewGroup? = null
    private var selectedTypeId = 1

    private lateinit var binding: Desk360AddNewTicketLayoutBinding

    private var typeList: ArrayList<Desk360Type>? = null

    private val editTextStyleModel = Desk360Config.instance.getDesk360Preferences()?.types?.data?.create_screen
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
    private var messageLength: Int = 0
    private var nameFieldFill: Boolean = false
    private var emailFieldFill: Boolean = false
    private var messageFieldFill: Boolean = false
    private var selectedItem: Boolean = false
    private val listOfType: ArrayList<String> = arrayListOf()
    private var invalidEmail: Boolean = false

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

            //activity.isNewTicketAdded = true
            //addTicketToCache(it)

            view?.let { it1 ->
                remove()
                Navigation.findNavController(it1)
                    .navigate(
                        R.id.action_addNewTicketFragment_to_thanksFragment,
                        null,
                        NavOptions.Builder().setPopUpTo(R.id.addNewTicketFragment, true).build()
                    )
            }

            val imm = activity.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view!!.windowToken, 0)
        }
        binding.createTicketButton.isClickable = true
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

        activity.changeMainUI()

        activity.window?.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
        activity.contactUsMainBottomBar.visibility = View.VISIBLE

        keyboardListener()

        viewModel = AddNewTicketViewModel()

        typeList = Desk360Config.instance.getDesk360Preferences()?.types!!.data.create_screen.types
        viewModel?.addedTicket?.observe(this, observerAddedTicket)

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

        binding.createTicketButton?.setOnClickListener {
            binding.createTicketButton.isClickable = false
            validateAllField()
        }

        binding.fileNameIcon.setOnClickListener {
            file = null
            binding.fileNameIcon.visibility = View.INVISIBLE
            binding.fileNameTextCreateTicketScreen.visibility = View.INVISIBLE
        }

        binding.fileNameTextCreateTicketScreen.visibility = View.INVISIBLE
        binding.fileNameIcon.visibility = View.INVISIBLE

        binding.textPathCreateTicketScreen.setOnClickListener {
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

        binding.createScreenRootView.addView(
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

        /**
         * email filed
         */
        eMailField = TextInputViewGroup(editTextStyleModel, this@Desk360AddNewTicketFragment)
        binding.createScreenRootView.addView(
            eMailField?.createEditText(
                Desk360Constants.currentType?.data?.general_settings?.email_field_text ?: "Email"
            )
        )
        eMailField?.holder?.textInputEditText?.inputType = InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS

        eMailField?.holder?.textInputEditText?.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {}
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                emailQuality(s)
            }
        })

        for (i in customInputField.indices) {
            val customInputViewGroup =
                TextInputViewGroup(editTextStyleModel, this@Desk360AddNewTicketFragment)
            binding.createScreenRootView.addView(
                customInputViewGroup.createEditText(customInputField[i].place_holder ?: "")
            )

            customInputViewList.add(customInputViewGroup)
        }

        /**
         * subject filed
         */
        subjectTypeSpinner = SelectBoxViewGroup(editTextStyleModel, this@Desk360AddNewTicketFragment)

        binding.createScreenRootView.addView(subjectTypeSpinner?.createSpinner())

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
                            subjectTypeSpinner?.holder?.shadowBorder?.setStroke(editTextStyleModel.form_input_border_color)
                            subjectTypeSpinner?.holder?.selectBoxCardView?.setCardBackgroundColor(
                                Color.parseColor(editTextStyleModel.form_input_background_color)
                            )
                        }
                        return
                    }

                    if (editTextStyleModel.form_style_id == 3) {

                        subjectTypeSpinner?.holder?.shadowBorder?.setStroke(editTextStyleModel.form_input_focus_border_color)
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

            val spinnerItem = SelectBoxViewGroup(editTextStyleModel, this@Desk360AddNewTicketFragment)

            binding.createScreenRootView.addView(spinnerItem.createSpinner())

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
                                spinnerItem.holder.shadowBorder?.setStroke(editTextStyleModel.form_input_border_color)
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
                                val customSelectboxId = it1.order.toString()
                                    .toRequestBody("text/plain".toMediaTypeOrNull())

                                params[customSelectBoxField[i].name.toString()] =
                                    customSelectboxId
                            }
                        }

                        if (editTextStyleModel.form_style_id == 3) {

                            spinnerItem.holder.shadowBorder?.setStroke(editTextStyleModel.form_input_focus_border_color)
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
        binding.createScreenRootView.addView(messageField?.createEditText(Desk360Constants.currentType?.data?.general_settings?.message_field_text ?: "Message"))

        nameField?.holder?.textInputLayout?.setErrorTextColor(errorLabelTextColor)
        eMailField?.holder?.textInputLayout?.setErrorTextColor(errorLabelTextColor)
        messageField?.holder?.textAreaLayout?.setErrorTextColor(errorLabelTextColor)

        nameField?.holder?.textInputLayout?.boxStrokeErrorColor = errorLabelTextColor
        eMailField?.holder?.textInputLayout?.boxStrokeErrorColor = errorLabelTextColor
        messageField?.holder?.textAreaLayout?.boxStrokeErrorColor = errorLabelTextColor

        messageField?.holder?.textAreaEditText?.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {}
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                messageQuality(s)
            }
        })

        Util.setEditTextScrollable(messageField?.holder?.textAreaEditText!!)

        Desk360CustomStyle.setFontWeight(
            binding.createScreenButtonText,
            context,
            Desk360Constants.currentType?.data?.create_screen?.button_text_font_weight
        )
        Desk360CustomStyle.setStyle(
            Desk360Constants.currentType?.data?.create_screen?.button_style_id,
            binding.createTicketButton,
            context!!
        )

        binding.textPathCreateTicketScreen.text = Desk360Constants.currentType?.data?.general_settings?.add_file_text

        binding.pathIconn.setImageResource(R.drawable.path_icon_desk360)
        binding.pathIconn.setColorFilter(Color.parseColor(Desk360Constants.currentType?.data?.create_screen?.label_text_color), PorterDuff.Mode.SRC_ATOP)


        binding.fileNameIcon.setBackgroundResource(R.drawable.document_cancel_icon)
        binding.fileNameIcon.background.setColorFilter(Color.parseColor(Desk360Constants.currentType?.data?.create_screen?.form_input_color), PorterDuff.Mode.SRC_ATOP)

        if (Desk360Constants.currentType?.data?.create_screen?.added_file_is_hidden!!) {
            binding.pathIconn.visibility = View.VISIBLE
        } else {
            binding.pathIconn.visibility = View.INVISIBLE
        }

        binding.createScreenButtonIcon.setImageResource(R.drawable.zarf)
        binding.createScreenButtonIcon.setColorFilter(
            Color.parseColor(Desk360Constants.currentType?.data?.create_screen?.button_text_color),
            PorterDuff.Mode.SRC_ATOP
        )
        Desk360CustomStyle.setFontWeight(
            binding.textFooterCreateTicketScreen,
            context,
            Desk360Constants.currentType?.data?.general_settings?.bottom_note_font_weight
        )

        for (i in customTextAreaField.indices) {

            val customTextAreaViewGroup = TextAreaViewGroup(editTextStyleModel, this@Desk360AddNewTicketFragment)
            binding.createScreenRootView.addView(customTextAreaViewGroup.createEditText(customTextAreaField[i].place_holder ?: ""))

            customTextAreaViewList.add(customTextAreaViewGroup)
        }

        binding.viewModel = viewModel
    }


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
                            val inputStream = context!!.contentResolver.openInputStream(
                                DocumentFile.fromSingleUri(
                                    activity,
                                    pathUri
                                )?.uri
                            )
                            val outputStream = FileOutputStream(cachFile)
                            var read = 0
                            val maxBufferSize = 1 * 1024 * 1024
                            val bytesAvailable = inputStream.available()
                            //int bufferSize = 1024;
                            val bufferSize = Math.min(bytesAvailable, maxBufferSize)
                            val buffers = ByteArray(bufferSize)
                            while (inputStream.read(buffers).also { read = it } != -1) {
                                outputStream.write(buffers, 0, read)
                            }
//                            Log.e("File Size", "Size " + cachFile.length())
                            inputStream.close()
                            outputStream.close()
//                            Log.e("File Path", "Path " + cachFile.path)
                        } catch (e: Exception) {
                            Log.e("Exception", e.message)
                        }
//                        Log.d("pdf_path", cachFile.path + "")
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
                            fileName?.length?.let {
                                if (it > 10) {
                                    binding.fileNameTextCreateTicketScreen.text =
                                        fileName?.substring(0, 8) + "..."
                                } else {
                                    binding.fileNameTextCreateTicketScreen.text = fileName
                                }
                                binding.fileNameTextCreateTicketScreen.visibility = View.VISIBLE
                                binding.fileNameIcon.visibility = View.VISIBLE
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

    private fun addTicketToCache(desk360TicketResponse: Desk360TicketResponse?){

        desk360TicketResponse?.let {

            val cacheTickets = preferencesManager
                .readObject("tickets", CacheTicket::class.java)
                    as ArrayList<Desk360TicketResponse>

            cacheTickets.add(0,desk360TicketResponse)

            preferencesManager.writeObject("tickets", cacheTickets)
        }
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
            s.length < 3 -> {
                messageField?.holder?.textAreaLayout?.error =
                    Desk360Constants.currentType?.data?.general_settings?.required_textarea_message
                        ?: "Mesaj Alanını Doldurunuz."
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
        if (nameFieldFill && emailFieldFill && messageLength > 0 && selectedItem) {
            for (i in 0 until customInputViewList.size) {
                val customInputData = customInputViewList[i].holder.textInputEditText?.text.toString()
                    .toRequestBody("text/plain".toMediaTypeOrNull())
                params[customInputField[i].name.toString()] = customInputData
            }
            for (i in customTextAreaViewList.indices) {
                val customInputData = customTextAreaViewList[i].holder.textAreaEditText?.text.toString()
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
            val platform = "Android".toRequestBody("text/plain".toMediaTypeOrNull())
            val settings = Desk360Constants.jsonObject.toString().toRequestBody(json)
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

            binding.loadingProgress.visibility = View.VISIBLE
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
                            Desk360Constants.currentType?.data?.general_settings?.required_email_field_message_empty
                                ?: "Email Alanını Lütfen Boş Bırakmayın."
                    emailFieldFill = false
                    observerEMail()
                }
                !selectedItem -> {
                    selectedItem = false
                    subjectTypeSpinner?.holder?.selectBox?.performClick()
                }
                messageLength <= 0 -> {
                    messageField?.holder?.textAreaLayout?.error =
                        Desk360Constants.currentType?.data?.general_settings?.required_textarea_message
                            ?: "Mesaj Alanını Doldurunuz."
                    messageFieldFill = false
                    observerMessage()
                }
            }
            binding.createTicketButton.isClickable = true
        }
    }

    private fun keyboardListener(){

        val constraintLayout = binding.baseLayout
        constraintLayout.viewTreeObserver.addOnGlobalLayoutListener {

            val rec = Rect()
            constraintLayout.getWindowVisibleDisplayFrame(rec)

            //finding screen height
            val screenHeight = constraintLayout.rootView.height

            //finding keyboard height
            val keypadHeight = screenHeight - rec.bottom

            if (keypadHeight > screenHeight * 0.15) {
                activity.contactUsMainBottomBar.visibility = View.GONE
            } else {

                if(!activity.isTicketDetailFragment){
                    activity.contactUsMainBottomBar.visibility = View.VISIBLE
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
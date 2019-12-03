package com.teknasyon.desk360.view.fragment

import android.Manifest
import android.content.Context.INPUT_METHOD_SERVICE
import android.content.Intent
import android.content.res.ColorStateList
import android.database.Cursor
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.drawable.GradientDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.util.Log
import android.view.*
import android.view.KeyEvent.ACTION_UP
import android.view.KeyEvent.KEYCODE_DPAD_CENTER
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView
import android.widget.LinearLayout
import android.widget.Spinner
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.documentfile.provider.DocumentFile
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.NavOptions
import androidx.navigation.Navigation
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import com.teknasyon.desk360.R
import com.teknasyon.desk360.databinding.Desk360AddNewTicketLayoutBinding
import com.teknasyon.desk360.helper.Desk360Config
import com.teknasyon.desk360.helper.Desk360Constants
import com.teknasyon.desk360.helper.Desk360CustomStyle
import com.teknasyon.desk360.helper.ImageFilePath
import com.teknasyon.desk360.model.Desk360Type
import com.teknasyon.desk360.modelv2.Desk360CustomFields
import com.teknasyon.desk360.modelv2.Desk360ScreenCreate
import com.teknasyon.desk360.view.adapter.Desk360CustomSupportTypeAdapter
import com.teknasyon.desk360.view.adapter.Desk360SupportTypeAdapter
import com.teknasyon.desk360.viewmodel.AddNewTicketViewModel
import okhttp3.MediaType
import okhttp3.RequestBody
import java.io.File
import java.io.FileOutputStream
import java.util.regex.Pattern

/**
 * Created by seyfullah on 30,May,2019
 *
 */

open class Desk360AddNewTicketFragment : Fragment(),
    Desk360BottomSheetDialogFragment.BottomSheetListener {

    private var viewModel: AddNewTicketViewModel? = null
    private var nameField: TextInputEditText? = null
    private var eMailField: TextInputEditText? = null
    private var messageField: TextInputEditText? = null
    private var subjectTypeSpinner: Spinner? = null
    private var selectedTypeId = 1

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
    private var RESULT_LOAD_FILES = 1221
    var params: HashMap<String, RequestBody> = HashMap()
    var file: File? = null
    var fileName: String? = null

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
                remove()
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

        binding.createTicketButton?.setOnClickListener {
            validateAllField()
        }

        binding.fileNameIcon.setOnClickListener{
            file=null
            binding.fileNameIcon.visibility=View.INVISIBLE
            binding.fileNameTextCreateTicketScreen.visibility=View.INVISIBLE
        }
        binding.fileNameTextCreateTicketScreen.visibility=View.INVISIBLE
        binding.fileNameIcon.visibility=View.INVISIBLE

        binding.textPathCreateTicketScreen.setOnClickListener {
            val bottomDialog = Desk360BottomSheetDialogFragment(this)
            fragmentManager?.let { it1 -> bottomDialog.show(it1, "bottomSheet") }
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
        eMailField?.inputType = InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
        eMailField?.imeOptions = EditorInfo.IME_ACTION_NEXT
        eMailField?.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {}
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                emailQuality(s)
            }
        })

        for (i in customInputField.indices) {
            customInputViewList.add(customInputField[i].place_holder?.let {
                createEditText(it).also {
                    it?.tag = customInputField[i].id
                    it?.setLines(1)
                    it?.setSingleLine(true)
                    it?.imeOptions = EditorInfo.IME_ACTION_NEXT
                }
            }!!)
        }

        /**
         * subject filed
         */
        subjectTypeSpinner = createSpinner()
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
                    (subjectTypeSpinner?.selectedView as TextView).setTextColor(
                        Color.parseColor(editTextStyleModel?.form_input_focus_color ?: "#000000")
                    )
                }
            })
        for (i in customSelectBoxField.indices) {
            customSelectBoxViewList.add(
                createSpinner().also {
                    val customSelectBoxId = RequestBody.create(
                        MediaType.parse("text/plain"),
                        customSelectBoxField[i].options?.get(0)?.order.toString()
                    )
                    params[customSelectBoxField[i].id.toString()] = customSelectBoxId

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

                    it?.onItemSelectedListener = (object : AdapterView.OnItemSelectedListener {
                        override fun onNothingSelected(parent: AdapterView<*>?) {
                        }

                        override fun onItemSelected(
                            parent: AdapterView<*>,
                            view: View?,
                            position: Int,
                            id: Long
                        ) {
                            (it?.selectedView as TextView).setTextColor(
                                Color.parseColor(
                                    editTextStyleModel?.form_input_focus_color ?: "#000000"
                                )
                            )
                            customSelectBoxField[i].options?.let { it ->
                                it[position].let { it1 ->
                                    val customSelectboxId = RequestBody.create(
                                        MediaType.parse("text/plain"), it1.order.toString()
                                    )
                                    params[customSelectBoxField[i].name.toString()] =
                                        customSelectboxId
                                }
                            }
                        }
                    })
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

        binding.pathIconn.setImageResource(R.drawable.path_icon_desk360)
        binding.pathIconn.setColorFilter(
            Color.parseColor(Desk360Constants.currentType?.data?.create_screen?.label_text_color),
            PorterDuff.Mode.SRC_ATOP
        )


        binding.fileNameIcon.setBackgroundResource(R.drawable.document_cancel_icon)
        binding.fileNameIcon.background.setColorFilter(
            Color.parseColor(Desk360Constants.currentType?.data?.create_screen?.form_input_color),
            PorterDuff.Mode.SRC_ATOP
        )

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
        messageField?.gravity = Gravity.TOP

        for (i in customTextAreaField.indices) {
            customTextAreaViewList.add(customTextAreaField[i].place_holder?.let {
                createCustomTextArea(it).also {
                    it?.maxLines = 6
                    it?.minLines = 6
                    if (i == customTextAreaField.size) {
                        messageField?.imeOptions = EditorInfo.IME_ACTION_DONE
                    } else {
                        messageField?.imeOptions = EditorInfo.IME_ACTION_NEXT
                    }

                    it?.gravity = Gravity.TOP

                }
            }!!)
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
                        fileName = file!!.name
                    }
                    1222 -> {
                        //file = File(pathUri?.let { getRealPathFromURI(it) })
                        val cachFile = File(
                            context!!.cacheDir, DocumentFile.fromSingleUri(
                                activity!!,
                                pathUri
                            )?.name?.replace(" ", "")
                        )
                        try {
                            val inputStream = context!!.contentResolver.openInputStream(
                                DocumentFile.fromSingleUri(
                                    activity!!,
                                    pathUri!!
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
                            Log.e("File Size", "Size " + cachFile.length())
                            inputStream.close()
                            outputStream.close()
                            Log.e("File Path", "Path " + cachFile.path)
                        } catch (e: Exception) {
                            Log.e("Exception", e.message)
                        }
                        Log.d("pdf_path", cachFile.path + "")
                        file = cachFile
                        fileName = file!!.name
                    }
                }

               if(file?.exists() == true){
                   fileName?.length?.let {
                       if(it > 10){
                           binding.fileNameTextCreateTicketScreen.text= fileName?.substring(0,8) + "..."
                       }else{
                           binding.fileNameTextCreateTicketScreen.text=fileName
                       }
                       binding.fileNameTextCreateTicketScreen.visibility=View.VISIBLE
                       binding.fileNameIcon.visibility=View.VISIBLE
                   }

                }

                Log.d("asasa", "saas")
            }

        }
    }

    open fun getRealPathFromURI(contentURI: Uri): String? {
        var cursor: Cursor? = null
        var filepath = ""
        try {
            val proj =
                arrayOf(MediaStore.Images.Media.DATA)
            cursor = context?.contentResolver?.query(contentURI, proj, null, null, null)
            val column_index: Int? = cursor?.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            cursor?.moveToFirst()
            filepath = column_index?.let { cursor?.getString(2) }!!
        } finally {
            cursor?.close()
        }
        return filepath
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

    private fun remove(): Boolean {
        return try {
            if (file?.exists() == true) file?.delete()

            true
        } catch (e: Throwable) {
            e.printStackTrace()
            false
        }
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
        binding.loadingProgress.visibility=View.VISIBLE
        if (nameFieldFill && emailFieldFill && messageLength > 0) {
            var isExistEmptyCustomField = false
            for (i in 0 until customInputViewList.size) {
                if (customInputViewList[i].text?.isNotEmpty() != true) {
                    customInputViewList[i].isEnabled = true
                    customInputViewList[i].requestFocus()
                    customInputViewList[i].onKeyUp(
                        KEYCODE_DPAD_CENTER,
                        KeyEvent(ACTION_UP, KEYCODE_DPAD_CENTER)
                    )
                    isExistEmptyCustomField = true
                    break
                } else {
                    val customInputData = RequestBody.create(
                        MediaType.parse("text/plain"),
                        customInputViewList[i].text.toString()
                    )
                    params[customInputField[i].name.toString()] = customInputData
                }
            }
            if (isExistEmptyCustomField) {
                return
            }
            for (i in customTextAreaViewList.indices) {
                if (customTextAreaViewList[i].text?.isNotEmpty() != true) {
                    customTextAreaViewList[i].isEnabled = true
                    customTextAreaViewList[i].requestFocus()
                    customTextAreaViewList[i].onKeyUp(
                        KEYCODE_DPAD_CENTER,
                        KeyEvent(ACTION_UP, KEYCODE_DPAD_CENTER)
                    )
                    isExistEmptyCustomField = true
                    break
                } else {
                    val customInputData = RequestBody.create(
                        MediaType.parse("text/plain"),
                        customTextAreaViewList[i].text.toString()
                    )
                    params[customTextAreaField[i].name.toString()] = customInputData
                }
            }
            if (isExistEmptyCustomField) {
                return
            }

            val email = RequestBody.create(MediaType.parse("text/plain"), emailData!!)
            val name = RequestBody.create(MediaType.parse("text/plain"), nameData)
            val message = RequestBody.create(MediaType.parse("text/plain"), messageData)
            val typeId =
                RequestBody.create(MediaType.parse("text/plain"), selectedTypeId.toString())
            val source = RequestBody.create(MediaType.parse("text/plain"), "App")
            val platform = RequestBody.create(MediaType.parse("text/plain"), "Android")
            val countryCode =
                RequestBody.create(MediaType.parse("text/plain"), Desk360Constants.countryCode())

            params["name"] = name
            params["email"] = email
            params["message"] = message
            params["type_id"] = typeId
            params["source"] = source
            params["platform"] = platform
            params["country_code"] = countryCode

            viewModel?.addSupportTicket(params, file)

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

    override fun onButtonClicked(isClickedImage: Boolean) {

        Dexter.withActivity(activity)
            .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
            .withListener(object : PermissionListener {
                override fun onPermissionGranted(response: PermissionGrantedResponse) {
                    if (isClickedImage) {

                        RESULT_LOAD_FILES = 1221
                        startActivityForResult(
                            Intent(
                                Intent.ACTION_PICK,
                                MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                            ), RESULT_LOAD_FILES
                        )
                    } else {
                        RESULT_LOAD_FILES = 1222
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                            val photoPickerIntent = Intent(Intent.ACTION_OPEN_DOCUMENT)
                            photoPickerIntent.type = "application/pdf"
                            startActivityForResult(photoPickerIntent, RESULT_LOAD_FILES)
                        }
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
package com.teknasyon.desk360.view.fragment

import android.Manifest
import android.content.Context.INPUT_METHOD_SERVICE
import android.content.Intent
import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.provider.MediaStore
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.util.Log
import android.view.KeyEvent
import android.view.KeyEvent.ACTION_UP
import android.view.KeyEvent.KEYCODE_DPAD_CENTER
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView
import android.widget.LinearLayout
import android.widget.Spinner
import android.widget.TextView
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
import com.teknasyon.desk360.model.Desk360Type
import com.teknasyon.desk360.modelv2.Desk360CustomFields
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
    private var nameField: TextInputViewGroup? = null
    private var eMailField: TextInputViewGroup? = null
    private var messageField: TextAreaViewGroup? = null
    private var subjectTypeSpinner: SelectBoxViewGroup? = null
    private var selectedTypeId = 1

    private lateinit var binding: Desk360AddNewTicketLayoutBinding

    private var typeList: ArrayList<Desk360Type>? = null
    private val editTextStyleModel =
        Desk360Config.instance.getDesk360Preferences()?.types?.data?.create_screen
    private var customInputField: List<Desk360CustomFields> = arrayListOf()
    private var customSelectBoxField: List<Desk360CustomFields> = arrayListOf()
    private var customTextAreaField: List<Desk360CustomFields> = arrayListOf()

    private var customInputViewList: ArrayList<TextInputViewGroup> = arrayListOf()
    private var customSelectBoxViewList: ArrayList<Spinner> = arrayListOf()
    private var customTextAreaViewList: ArrayList<TextAreaViewGroup> = arrayListOf()

    //Validate variables
    private var nameData: String? = null
    private var emailData: String? = null
    private var messageData: String? = null
    private var messageLength: Int = 0
    private var nameFieldFill: Boolean = false
    private var emailFieldFill: Boolean = false
    private var messageFieldFill: Boolean = false
    private var selectedItem: Boolean = false
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
            subjectTypeSpinner?.holder?.selectBox?.adapter = myAdapter
        }
    }

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
            binding.createTicketButton.isClickable = false
            Handler().postDelayed({
                binding.createTicketButton.isClickable = true
            validateAllField()
            }, 800)
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
        subjectTypeSpinner =
            SelectBoxViewGroup(editTextStyleModel, this@Desk360AddNewTicketFragment)
        binding.createScreenRootView.addView(
            subjectTypeSpinner?.createSpinner()
        )
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

                            subjectTypeSpinner?.holder?.selectBoxCardView?.setCardBackgroundColor(
                                Color.parseColor(
                                    editTextStyleModel.form_input_background_color
                                )
                            )
                        }
                        return
                    }
                    if (editTextStyleModel.form_style_id == 3) {
                        view?.setBackgroundColor(Color.parseColor(editTextStyleModel.form_input_focus_background_color))
                        subjectTypeSpinner?.holder?.selectBoxCardView?.setCardBackgroundColor(
                            Color.parseColor(
                                editTextStyleModel.form_input_focus_background_color
                            )
                        )
                    }

                    selectedItem = true
                    typeList?.let { it[position].let { it1 -> selectedTypeId = it1.id!! } }
                    (subjectTypeSpinner?.holder?.selectBox?.selectedView as TextView).setTextColor(
                        Color.parseColor(editTextStyleModel.form_input_focus_color)
                    )
                }
            })
//        for (i in customSelectBoxField.indices) {
//            customSelectBoxViewList.add(
//                createSpinner().also {
//                    val optionsList = arrayListOf<Desk360Options>()
//                    optionsList.add(
//                        Desk360Options(
//                            value = customSelectBoxField[i].place_holder,
//                            order = -1
//                        )
//                    )
//                    customSelectBoxField[i].options?.let { it1 -> optionsList.addAll(it1) }
//
//                    val myAdapter =
//                        context?.let { it1 ->
//                            Desk360CustomSupportTypeAdapter(
//                                it1,
//                                R.layout.desk360_type_dropdown,
//                                optionsList
//                            )
//                        }
//                    it?.adapter = myAdapter
//
//                    it?.onItemSelectedListener = (object : AdapterView.OnItemSelectedListener {
//                        override fun onNothingSelected(parent: AdapterView<*>?) {
//                        }
//
//                        override fun onItemSelected(
//                            parent: AdapterView<*>,
//                            view: View?,
//                            position: Int,
//                            id: Long
//                        ) {
//                            (it?.selectedView as TextView).setTextColor(
//                                Color.parseColor(
//                                    editTextStyleModel?.form_input_focus_color ?: "#000000"
//                                )
//                            )
//                            customSelectBoxField[i].options?.let { it ->
//                                it[position].let { it1 ->
//                                    val customSelectboxId = RequestBody.create(
//                                        MediaType.parse("text/plain"), it1.order.toString()
//                                    )
//                                    params[customSelectBoxField[i].name.toString()] =
//                                        customSelectboxId
//                                }
//                            }
//                        }
//                    })
//                }!!
//            )
//        }

        /**
         * message filed
         */
        messageField = TextAreaViewGroup(editTextStyleModel, this@Desk360AddNewTicketFragment)
        binding.createScreenRootView.addView(
            messageField?.createEditText(
                Desk360Constants.currentType?.data?.general_settings?.message_field_text
                    ?: "Message"
            )
        )

        messageField?.holder?.textAreaEditText?.addTextChangedListener(object : TextWatcher {
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

        binding.textPathCreateTicketScreen.text =
            Desk360Constants.currentType?.data?.general_settings?.add_file_text

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

        for (i in customTextAreaField.indices) {

            val customTextAreaViewGroup =
                TextAreaViewGroup(editTextStyleModel, this@Desk360AddNewTicketFragment)
            binding.createScreenRootView.addView(
                customTextAreaViewGroup.createEditText(customTextAreaField[i].place_holder ?: "")
            )

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

                if (file?.exists() == true) {
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

                }

                Log.d("asasa", "saas")
            }

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
        if (nameFieldFill && emailFieldFill && messageLength > 0 && selectedItem) {
            for (i in 0 until customInputViewList.size) {

                val customInputData = RequestBody.create(
                    MediaType.parse("text/plain"),
                    customInputViewList[i].holder.textInputEditText?.text.toString()
                )
                params[customInputField[i].name.toString()] = customInputData
            }
            for (i in customTextAreaViewList.indices) {

                val customInputData = RequestBody.create(
                    MediaType.parse("text/plain"),
                    customTextAreaViewList[i].holder.textAreaEditText?.text.toString()
                )
                params[customTextAreaField[i].name.toString()] = customInputData
            }
            val email = RequestBody.create(MediaType.parse("text/plain"), emailData!!)
            val name = RequestBody.create(MediaType.parse("text/plain"), nameData)
            val message = RequestBody.create(MediaType.parse("text/plain"), messageData)
            val typeId =
                RequestBody.create(MediaType.parse("text/plain"), selectedTypeId.toString())
            val source = RequestBody.create(MediaType.parse("text/plain"), "App")
            val platform = RequestBody.create(MediaType.parse("text/plain"), "Android")
            val countryCode =
                RequestBody.create(MediaType.parse("text/plain"), Desk360Constants.countryCode().toUpperCase())

            params["name"] = name
            params["email"] = email
            params["message"] = message
            params["type_id"] = typeId
            params["source"] = source
            params["platform"] = platform
            params["country_code"] = countryCode

            binding.loadingProgress.visibility = View.VISIBLE

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
            !selectedItem -> {
                selectedItem = false
                //
                subjectTypeSpinner?.holder?.selectBox?.performClick()
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

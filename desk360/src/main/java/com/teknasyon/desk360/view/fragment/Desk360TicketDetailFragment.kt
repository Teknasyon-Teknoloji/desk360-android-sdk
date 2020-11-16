package com.teknasyon.desk360.view.fragment

import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.drawable.GradientDrawable
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat
import androidx.core.content.getSystemService
import androidx.core.graphics.drawable.DrawableCompat
import androidx.core.net.toUri
import androidx.core.view.children
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.chip.Chip
import com.teknasyon.desk360.R
import com.teknasyon.desk360.databinding.Desk360FragmentTicketDetailBinding
import com.teknasyon.desk360.helper.*
import com.teknasyon.desk360.model.Desk360File
import com.teknasyon.desk360.model.Desk360Message
import com.teknasyon.desk360.model.Desk360TicketResponse
import com.teknasyon.desk360.view.activity.Desk360Activity
import com.teknasyon.desk360.view.adapter.Desk360TicketDetailListAdapter
import com.teknasyon.desk360.viewmodel.TicketDetailViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.desk360_fragment_main.*
import java.io.File
import java.util.*

class Desk360TicketDetailFragment : Fragment(),
    Desk360BottomSheetDialogFragment.BottomSheetListener {

    private var binding: Desk360FragmentTicketDetailBinding? = null
    private val gradientDrawable = GradientDrawable()
    private var ticketId: Int? = null
    private var ticketStatus: String? = null
    private var backButtonAction: Disposable? = null

    private val preferencesManager = PreferencesManager()
    private var cacheDesk360TicketResponse: Desk360TicketResponse? = null

    private lateinit var desk360Activity: Desk360Activity

    private val attachments: MutableList<File> = mutableListOf()

    private val layoutManager by lazy {
        LinearLayoutManager(context, RecyclerView.VERTICAL, false)
    }

    private val adapter by lazy {
        Desk360TicketDetailListAdapter { file ->
            downloadFile(activity, file)
            binding?.loadingProgressTicketDetail?.visibility = View.VISIBLE
        }
    }

    private var observer = Observer<Desk360TicketResponse> {
        binding?.loadingProgressTicketDetail?.visibility = View.INVISIBLE

        if (it != null) {
            preferencesManager.writeObject(ticketId.toString(), it)
            cacheDesk360TicketResponse = preferencesManager.readObject(
                ticketId.toString(),
                Desk360TicketResponse::class.java
            )

            setData(
                cacheDesk360TicketResponse?.messages ?: listOf(),
                cacheDesk360TicketResponse?.attachment_url
            )
        }
    }

    private var addMessageObserver = Observer<Desk360Message> {
        binding?.loadingProgressTicketDetail?.visibility = View.INVISIBLE

        if (it != null) {
            cacheDesk360TicketResponse = preferencesManager.readObject(
                ticketId.toString(),
                Desk360TicketResponse::class.java
            )
            val tickets = cacheDesk360TicketResponse?.messages
            tickets?.set(tickets.size - 1, it)

            preferencesManager.writeObject(ticketId.toString(), cacheDesk360TicketResponse!!)

            addTicketToCache(null)

            binding?.messageEditText?.setText("")
            binding?.fileList?.children?.forEach { binding?.fileList?.removeView(it) }
            attachments.clear()
        }
    }

    private var videoPathObserver = Observer<String> {

        if (it.isNotEmpty()) {

            binding?.loadingProgressTicketDetail?.visibility = View.GONE
            ImageFilePath.refreshGallery(it, desk360Activity)
        }
    }

    private var viewModel: TicketDetailViewModel? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        desk360Activity = context as Desk360Activity
        context.registerReceiver(
            downloadReceiver,
            IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE)
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.desk360_fragment_ticket_detail,
            container,
            false
        )

        desk360Activity.contactUsMainBottomBar.visibility = View.GONE

        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)

        desk360Activity.contactUsMainBottomBar.visibility = View.GONE
        desk360Activity.changeMainUI()

        cacheDesk360TicketResponse =
            preferencesManager.readObject(ticketId.toString(), Desk360TicketResponse::class.java)

        cacheDesk360TicketResponse?.let { response ->
            if (!response.messages.isNullOrEmpty()) {
                binding?.loadingProgressTicketDetail?.visibility = View.INVISIBLE
            } else {
                binding?.loadingProgressTicketDetail?.visibility = View.VISIBLE
            }

            setData(response.messages ?: listOf(), response.attachment_url)
        } ?: run {
            binding?.loadingProgressTicketDetail?.visibility = View.VISIBLE
        }

        binding?.messageDetailRecyclerView?.layoutManager = layoutManager
        binding?.messageDetailRecyclerView?.adapter = adapter

        viewModel = ticketId?.let { TicketDetailViewModel(it) }

        viewModel?.ticketDetailList?.observe(viewLifecycleOwner, observer)

        viewModel?.addMessageItem?.observe(viewLifecycleOwner, addMessageObserver)

        viewModel?.videoPath?.observe(viewLifecycleOwner, videoPathObserver)

        context?.let { context ->
            binding?.addNewTicketButton?.let { button ->
                Desk360CustomStyle.setStyle(
                    Desk360Constants.currentType?.data?.first_screen?.button_style_id,
                    button,
                    context
                )
            }
        }

        binding?.addNewMessageButton?.setOnClickListener {

            binding?.messageEditText?.text?.trim()?.apply {
                if (isNotEmpty() && toString().isNotEmpty()) {

                    val message = Desk360Message()
                    message.id = -1
                    message.is_answer = false
                    message.message = this.toString()
                    message.created = Util.convertDateToString(Date(), "yyyy-MM-dd HH:mm:ss")
                    message.tick = false

                    addTicketToCache(message)

                    ticketId?.let { it1 ->
                        viewModel?.addMessage(
                            it1,
                            binding?.messageEditText?.text.toString(),
                            attachments
                        )
                    }
                }
            }
        }

        binding?.ticketDetailButtonIcon?.setImageResource(R.drawable.zarf)
        binding?.ticketDetailButtonIcon?.setColorFilter(
            Color.parseColor(Desk360Constants.currentType?.data?.first_screen?.button_text_color),
            PorterDuff.Mode.SRC_ATOP
        )

        context?.let { context ->
            binding?.messageEditText?.background?.let { background ->
                DrawableCompat.setTint(
                    background,
                    ContextCompat.getColor(context, R.color.colorHintDesk360)
                )
            }
        }

        val states = ArrayCreator.createDoubleArray(1, 2)
        states[0][0] = android.R.attr.state_focused
        states[0][1] = android.R.attr.state_enabled

        val colors = ArrayCreator.createSingleArray(2)
        colors[0] =
            Color.parseColor(Desk360Constants.currentType?.data?.ticket_detail_screen?.write_message_border_active_color)
        colors[1] =
            Color.parseColor(Desk360Constants.currentType?.data?.ticket_detail_screen?.write_message_border_color)

        val myList = ColorStateList(states, colors)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            binding!!.messageEditText.backgroundTintList = myList
        }

        Desk360CustomStyle.setFontWeight(
            binding!!.ticketDetailButtonText,
            context,
            Desk360Constants.currentType?.data?.first_screen?.button_text_font_weight
        )

        gradientDrawable.setColor(Color.parseColor(Desk360Constants.currentType?.data?.ticket_detail_screen?.write_message_background_color))

        binding?.addNewMessageButton?.setImageResource(R.drawable.message_send_icon_blue)

        binding?.addNewMessageButton?.setColorFilter(
            Color.parseColor(Desk360Constants.currentType?.data?.ticket_detail_screen?.write_message_button_icon_disable_color),
            PorterDuff.Mode.SRC_ATOP
        )

        binding?.addAttachmentButton?.setImageResource(R.drawable.ic_attach)

        binding?.addAttachmentButton?.setColorFilter(
            Color.parseColor(Desk360Constants.currentType?.data?.ticket_detail_screen?.write_message_button_icon_disable_color),
            PorterDuff.Mode.SRC_ATOP
        )

        binding?.addAttachmentButton?.setOnClickListener {
            val bottomDialog = Desk360BottomSheetDialogFragment(this)
            bottomDialog.show(parentFragmentManager, "bottomSheet")
        }

        binding?.messageEditText?.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                if (s?.length == 0) {
                    binding?.addNewMessageButton?.setColorFilter(
                        Color.parseColor(Desk360Constants.currentType?.data?.ticket_detail_screen?.write_message_button_icon_disable_color),
                        PorterDuff.Mode.SRC_ATOP
                    )

                } else {
                    binding?.addNewMessageButton?.setColorFilter(
                        Color.parseColor(Desk360Constants.currentType?.data?.ticket_detail_screen?.write_message_button_icon_color),
                        PorterDuff.Mode.SRC_ATOP
                    )
                }
            }

        })

        binding?.layoutSendNewMessageNormal?.background = gradientDrawable
        binding?.addNewTicketButton?.setOnClickListener {
            Navigation
                .findNavController(it)
                .navigate(R.id.action_ticketDetailFragment_to_addNewTicketFragment, null)
        }

        expireControl()
    }

    private fun expireControl() {

        if (ticketStatus == "expired") {

            binding?.layoutSendNewMessageNormal?.visibility = View.GONE
            binding?.addNewTicketButton?.visibility = View.VISIBLE
            backButtonAction =
                RxBus.listen(String::class.java).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({ iti ->
                        when (iti) {
                            "backButtonActionKey" -> {
                                view?.let { it1 ->
                                    Navigation.findNavController(it1).popBackStack(
                                        R.id.action_global_ticketDetailFragment,
                                        true
                                    )

                                }
                            }
                        }
                    }, { t ->
                        Log.d("Test", "$t.")
                    })
        } else {
            binding?.layoutSendNewMessageNormal?.visibility = View.VISIBLE
            binding?.addNewTicketButton?.visibility = View.GONE
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            ticketId = it.getInt("ticket_id")
            ticketStatus = it.getString("ticket_status")
        }
    }

    override fun onDetach() {
        activity?.unregisterReceiver(downloadReceiver)
        super.onDetach()
    }

    override fun onDestroy() {
        super.onDestroy()

        viewModel?.ticketDetailList?.removeObserver(observer)
        viewModel?.addMessageItem?.removeObserver(addMessageObserver)
        viewModel?.videoPath?.removeObserver(videoPathObserver)

        if (backButtonAction?.isDisposed == false)
            backButtonAction?.dispose()

        hideSoftKeyboard()
    }

    private fun hideSoftKeyboard() {
        activity?.currentFocus?.let { view ->
            val imm = view.context.getSystemService<InputMethodManager>()
            imm?.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    private fun addTicketToCache(desk360Message: Desk360Message?) {

        cacheDesk360TicketResponse =
            preferencesManager.readObject(ticketId.toString(), Desk360TicketResponse::class.java)

        viewModel?.ticketDetailList?.value?.messages?.clear()
        viewModel?.ticketDetailList?.value?.messages?.addAll(
            cacheDesk360TicketResponse?.messages ?: listOf()
        )

        desk360Message?.let {
            viewModel?.ticketDetailList?.value?.messages?.add(desk360Message)
            cacheDesk360TicketResponse?.messages?.add(desk360Message)
            preferencesManager.writeObject(ticketId.toString(), cacheDesk360TicketResponse!!)
        }

        setData(
            viewModel?.ticketDetailList?.value?.messages ?: listOf(),
            cacheDesk360TicketResponse?.attachment_url
        )
    }

    private fun downloadFile(context: Context?, desk360File: Desk360File) {
        val request = DownloadManager.Request(desk360File.url.toUri())
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, desk360File.name)
        val manager = context?.getSystemService<DownloadManager>()
        manager?.enqueue(request)
    }

    private fun setData(messages: List<Desk360Message>, url: String?) {
        adapter.setData(messages, url)
        binding?.messageDetailRecyclerView?.scrollToPosition(messages.size - 1)
    }

    private val downloadReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val referenceId = intent?.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)
            val manager = context?.getSystemService<DownloadManager>()
            val uri = referenceId?.let { manager?.getUriForDownloadedFile(it) }
            viewModel?.videoPath?.postValue(uri.toString())
        }
    }

    override fun onButtonClicked(typeOfAttachment: FileType) {
        when (typeOfAttachment) {
            FileType.IMAGE -> {
                val intent = Intent(Intent.ACTION_GET_CONTENT)

                intent.type = "image/png,image/jpg,image/jpeg,image/bmp,image/gif"
                intent.putExtra(
                    Intent.EXTRA_MIME_TYPES,
                    arrayOf("image/png", "image/jpg", "image/jpeg", "image/bmp", "image/gif")
                )
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)

                startActivityForResult(intent, SELECT_IMAGE)
            }
            FileType.DOC -> {
                val photoPickerIntent = Intent(Intent.ACTION_OPEN_DOCUMENT)
                photoPickerIntent.type = "application/pdf"
                startActivityForResult(photoPickerIntent, SELECT_DOC)
            }
            FileType.VIDEO -> {
                val intent = Intent(Intent.ACTION_GET_CONTENT)

                intent.type = "video/mp4,video/mov,video/flv,video/avi"
                intent.putExtra(
                    Intent.EXTRA_MIME_TYPES,
                    arrayOf("video/mp4", "video/mov", "video/flv", "video/avi")
                )

                startActivityForResult(intent, SELECT_VIDEO)
            }
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode in arrayOf(SELECT_DOC, SELECT_IMAGE, SELECT_VIDEO)) {

            val pathUri = data?.data ?: return
            val context = this.context ?: return
            val file = context.getFile(pathUri) ?: return
            val fileName = context.getFileName(pathUri) ?: ""

            val chip = Chip(context)
            chip.text = fileName
            chip.setChipIconResource(fileName.getFileIconResource())
            chip.isCloseIconVisible = true

            binding?.fileList?.addView(chip)
            attachments.add(file)

            chip.setOnCloseIconClickListener {
                binding?.fileList?.removeView(chip)
                attachments.remove(file)
            }
        }
    }

    companion object {
        const val SELECT_IMAGE = 1221
        const val SELECT_DOC = 1222
        const val SELECT_VIDEO = 1223
    }
}
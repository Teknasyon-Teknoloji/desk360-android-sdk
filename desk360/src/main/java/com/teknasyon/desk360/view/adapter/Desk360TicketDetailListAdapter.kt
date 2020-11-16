package com.teknasyon.desk360.view.adapter

import android.annotation.SuppressLint
import android.app.Activity
import android.graphics.Bitmap
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.net.toUri
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.teknasyon.desk360.R
import com.teknasyon.desk360.databinding.Desk360SendAttachmentItemLayoutBinding
import com.teknasyon.desk360.helper.getFileIconResource
import com.teknasyon.desk360.helper.verifyStoragePermissions
import com.teknasyon.desk360.model.Desk360File
import com.teknasyon.desk360.model.Desk360Message
import com.teknasyon.desk360.themev2.Desk360IncomingAttachmentText
import com.teknasyon.desk360.themev2.Desk360SentAttachmentText
import com.teknasyon.desk360.databinding.Desk360IncomingAttachmentItemLayoutBinding as IncomingAttachmentBinding
import com.teknasyon.desk360.databinding.Desk360IncomingMessageItemLayoutBinding as IncomingMessageBinding
import com.teknasyon.desk360.databinding.Desk360SendMessageItemLayoutBinding as SentMessageBinding

class Desk360TicketDetailListAdapter(
    private val downloadFile: (Desk360File) -> Unit
) : RecyclerView.Adapter<Desk360TicketDetailListAdapter.ViewHolder>() {
    private var ticketList: List<Desk360Message> = listOf()
    private var url: String? = null

    fun setData(ticketList: List<Desk360Message>, url: String?) {
        this.ticketList = ticketList
        this.url = url
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            INCOMING_MESSAGE -> {
                val binding = IncomingMessageBinding.inflate(inflater, parent, false)
                IncomingViewHolder(binding)
            }
            SENT_MESSAGE -> {
                val binding = SentMessageBinding.inflate(inflater, parent, false)
                SendMessageViewHolder(binding, url)
            }
            INCOMING_ATTACHMENT -> {
                val binding = IncomingAttachmentBinding.inflate(inflater, parent, false)
                IncomingAttachmentViewHolder(binding, url, downloadFile)
            }
            else -> {
                val binding =
                    Desk360SendAttachmentItemLayoutBinding.inflate(inflater, parent, false)
                SentAttachmentViewHolder(binding, url, downloadFile)
            }
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val message = ticketList[position]
        holder.bind(message)
    }

    private fun hasAttachments(message: Desk360Message): Boolean {

        val attachments = message.attachments
        val hasImages = !attachments?.images.isNullOrEmpty()
        val hasFiles = !attachments?.files.isNullOrEmpty()
        val hasVideos = !attachments?.videos.isNullOrEmpty()
        val hasOthers = !attachments?.others.isNullOrEmpty()

        return hasImages || hasFiles || hasVideos || hasOthers
    }

    override fun getItemViewType(position: Int): Int {

        val message = ticketList[position]
        val isAnswer = message.is_answer

        return if (isAnswer) {
            if (hasAttachments(message)) {
                INCOMING_ATTACHMENT
            } else {
                INCOMING_MESSAGE
            }
        } else {
            if (hasAttachments(message)) {
                SENT_ATTACHMENT
            } else {
                SENT_MESSAGE
            }
        }
    }

    override fun getItemCount(): Int {
        return ticketList.size
    }


    abstract class ViewHolder(
        binding: ViewDataBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        abstract fun bind(message: Desk360Message)
    }

    class IncomingViewHolder(
        val binding: IncomingMessageBinding
    ) : ViewHolder(binding) {
        override fun bind(message: Desk360Message) {
            binding.messageIncoming.text = message.message
            binding.dateIncoming.text = message.created
        }
    }

    class SendMessageViewHolder(
        val binding: SentMessageBinding,
        private val url: String?
    ) : ViewHolder(binding) {
        @SuppressLint("SetJavaScriptEnabled")
        override fun bind(message: Desk360Message) {

            binding.messageSend.text = message.message
            binding.dateSend.text = message.created
            binding.imageUrl.visibility = View.GONE
            binding.rlVideoView.visibility = View.GONE
            binding.rlWebView.visibility = View.GONE

            if (message.tick) {
                binding.messageTick.setImageResource(R.drawable.cift)
            } else {
                binding.messageTick.setImageResource(R.drawable.tek)
            }


            if (!url.isNullOrEmpty() && adapterPosition == 0) {
                when {
                    url.endsWith("pdf", true) -> {
                        binding.rlWebView.visibility = View.VISIBLE
                        binding.rlVideoView.visibility = View.GONE
                        val webSettings: WebSettings = binding.webView.settings
                        webSettings.javaScriptEnabled = true
                        webSettings.builtInZoomControls = true

                        val url = "https://docs.google.com/viewer?embedded=true&url=$url"
                        binding.webView.loadUrl(url)

                        binding.webView.webViewClient = object : WebViewClient() {
                            override fun onPageStarted(
                                view: WebView?,
                                url: String?,
                                favicon: Bitmap?
                            ) {
                                binding.progressBar.visibility = View.GONE
                            }
                        }
                    }
                    url.endsWith("mp4", true) -> {
                        binding.rlVideoView.visibility = View.VISIBLE
                        binding.rlWebView.visibility = View.GONE

                        val mediaController = MediaController(binding.root.context)
                        mediaController.setAnchorView(binding.videoView)
                        binding.videoView.setMediaController(mediaController)
                        binding.videoView.setVideoURI(url.toUri())
                        binding.videoView.setOnPreparedListener { mp ->
                            binding.videoView.visibility = View.VISIBLE
                            binding.vwProgressBar.visibility = View.GONE
                            binding.videoViewPlaceholder.visibility = View.GONE
                            binding.videoView.start()
                            mp.isLooping = true
                        }
                    }
                    else -> {
                        binding.rlWebView.visibility = View.GONE
                        binding.rlVideoView.visibility = View.GONE
                        binding.imageUrl.visibility = View.VISIBLE
                        Picasso.get()
                            .load(url)
                            .placeholder(R.drawable.sent_message_layout_type4)
                            .into(binding.imageUrl)
                    }
                }

            } else {
                binding.videoView.visibility = View.GONE
                binding.imageUrl.visibility = View.GONE
                binding.webView.visibility = View.GONE
            }
        }
    }

    abstract class AttachmentViewHolder(binding: ViewDataBinding) : ViewHolder(binding)

    class IncomingAttachmentViewHolder(
        val binding: IncomingAttachmentBinding,
        private val url: String?,
        private val downloadFile: (Desk360File) -> Unit
    ) : AttachmentViewHolder(binding) {
        override fun bind(message: Desk360Message) {

            binding.llImages.removeAllViews()

            binding.imageMessageIncoming.text = message.message

            var height = 0

            if (message.views == null) {
                val views = mutableListOf<View>()
                message.attachments?.let { attachments ->
                    attachments.images.forEach {
                        views.add(createAttachment(it, "Picture"))
                        height += 237
                    }
                    attachments.videos.forEach {
                        views.add(createAttachment(it, "Video"))
                        height += 237
                    }
                    attachments.files.forEach {
                        views.add(createAttachment(it, "File"))
                        height += 37
                    }
                    attachments.others.forEach {
                        views.add(createAttachment(it, "Other"))
                        height += 37
                    }
                }

                message.views = views
            }

            message.views?.forEach {
                binding.llImages.addView(it)
            }

            binding.dateIncoming.text = message.created
        }

        private fun createAttachment(file: Desk360File, type: String): View {
            binding.llImages.visibility = View.VISIBLE

            val activity = binding.llImages.context as Activity

            val inflater = LayoutInflater.from(binding.root.context)
            val view = inflater.inflate(
                R.layout.desk360_incoming_attachment_message_item,
                binding.root as? ViewGroup,
                false
            )

            val image = view.findViewById<ImageView>(R.id.iv_image)
            val rlVideo = view.findViewById<ConstraintLayout>(R.id.rl_video)

            val video = rlVideo.findViewById<VideoView>(R.id.videoView)
            val progressBar = rlVideo.findViewById<ProgressBar>(R.id.pb_vw_progressBar)
            val playButton = rlVideo.findViewById<ImageView>(R.id.iv_video_play)

            val rlFile = view.findViewById<RelativeLayout>(R.id.rl_file)

            val fileName = rlFile.findViewById<Desk360IncomingAttachmentText>(R.id.tv_type)
            val save = rlFile.findViewById<ImageView>(R.id.iv_download)

            fileName.text = file.name

            when (type) {

                "Picture" -> { // if attachment has picture

                    fileName.setCompoundDrawablesWithIntrinsicBounds(
                        file.name.getFileIconResource(),
                        0,
                        0,
                        0
                    )

                    rlVideo.visibility = View.GONE

                    Picasso.get().load(file.url).placeholder(R.drawable.sent_message_layout_type4)
                        .into(image)

                    save.setOnClickListener {
                        activity.verifyStoragePermissions()
                        url?.let { downloadFile(file) }
                    }
                }

                "Video" -> { // if attachment has video

                    fileName.setCompoundDrawablesWithIntrinsicBounds(
                        file.name.getFileIconResource(),
                        0,
                        0,
                        0
                    )

                    image.visibility = View.GONE
                    save.visibility = View.INVISIBLE

                    video.setVideoURI(Uri.parse(file.url))

                    video.setOnPreparedListener {
                        video.seekTo(1)

                        progressBar.visibility = View.GONE
                        playButton.visibility = View.VISIBLE
                        save.visibility = View.VISIBLE
                    }

                    video.setOnCompletionListener {
                        playButton.visibility = View.VISIBLE
                    }

                    playButton.setOnClickListener {
                        video.start()
                        playButton.visibility = View.GONE
                    }

                    save.setOnClickListener {
                        activity.verifyStoragePermissions()
                        url?.let { downloadFile(file) }
                    }
                }

                "File", "Other" -> {

                    image.visibility = View.GONE
                    rlVideo.visibility = View.GONE

                    fileName.setCompoundDrawablesWithIntrinsicBounds(
                        file.name.getFileIconResource(),
                        0,
                        0,
                        0
                    )

                    save.setOnClickListener {
                        activity.verifyStoragePermissions()
                        url?.let { downloadFile(file) }
                    }
                }
            }

            return view
        }

    }

    class SentAttachmentViewHolder(
        val binding: Desk360SendAttachmentItemLayoutBinding,
        private val url: String?,
        private val downloadFile: (Desk360File) -> Unit
    ) : AttachmentViewHolder(binding) {
        override fun bind(message: Desk360Message) {

            binding.llImages.removeAllViews()

            binding.imageMessageSent.text = message.message

            var height = 0

            if (message.views == null) {
                val views = mutableListOf<View>()
                message.attachments?.let { attachments ->
                    attachments.images.forEach {
                        views.add(createAttachment(it, "Picture"))
                        height += 237
                    }
                    attachments.videos.forEach {
                        views.add(createAttachment(it, "Video"))
                        height += 237
                    }
                    attachments.files.forEach {
                        views.add(createAttachment(it, "File"))
                        height += 37
                    }
                    attachments.others.forEach {
                        views.add(createAttachment(it, "Other"))
                        height += 37
                    }
                }

                message.views = views
            }

            message.views?.forEach {
                binding.llImages.addView(it)
            }

            binding.dateSend.text = message.created
        }

        private fun createAttachment(file: Desk360File, type: String): View {
            binding.llImages.visibility = View.VISIBLE

            val activity = binding.llImages.context as Activity

            val inflater = LayoutInflater.from(binding.root.context)
            val view = inflater.inflate(
                R.layout.desk360_sent_attachment_message_item,
                binding.root as? ViewGroup,
                false
            )

            val image = view.findViewById<ImageView>(R.id.iv_image)
            val rlVideo = view.findViewById<ConstraintLayout>(R.id.rl_video)

            val video = rlVideo.findViewById<VideoView>(R.id.videoView)
            val progressBar = rlVideo.findViewById<ProgressBar>(R.id.pb_vw_progressBar)
            val playButton = rlVideo.findViewById<ImageView>(R.id.iv_video_play)

            val rlFile = view.findViewById<RelativeLayout>(R.id.rl_file)

            val fileName = rlFile.findViewById<Desk360SentAttachmentText>(R.id.tv_type)
            val save = rlFile.findViewById<ImageView>(R.id.iv_download)

            fileName.text = file.name

            when (type) {

                "Picture" -> { // if attachment has picture

                    fileName.setCompoundDrawablesWithIntrinsicBounds(
                        file.name.getFileIconResource(),
                        0,
                        0,
                        0
                    )

                    rlVideo.visibility = View.GONE

                    Picasso.get().load(file.url).placeholder(R.drawable.sent_message_layout_type4)
                        .into(image)

                    save.setOnClickListener {
                        activity.verifyStoragePermissions()
                        url?.let { downloadFile(file) }
                    }
                }

                "Video" -> { // if attachment has video

                    fileName.setCompoundDrawablesWithIntrinsicBounds(
                        file.name.getFileIconResource(),
                        0,
                        0,
                        0
                    )

                    image.visibility = View.GONE
                    save.visibility = View.INVISIBLE

                    video.setVideoURI(Uri.parse(file.url))

                    video.setOnPreparedListener {
                        video.seekTo(1)

                        progressBar.visibility = View.GONE
                        playButton.visibility = View.VISIBLE
                        save.visibility = View.VISIBLE
                    }

                    video.setOnCompletionListener {
                        playButton.visibility = View.VISIBLE
                    }

                    playButton.setOnClickListener {
                        video.start()
                        playButton.visibility = View.GONE
                    }

                    save.setOnClickListener {
                        activity.verifyStoragePermissions()
                        url?.let { downloadFile(file) }
                    }
                }

                "File", "Other" -> {

                    image.visibility = View.GONE
                    rlVideo.visibility = View.GONE

                    fileName.setCompoundDrawablesWithIntrinsicBounds(
                        file.name.getFileIconResource(),
                        0,
                        0,
                        0
                    )

                    save.setOnClickListener {
                        activity.verifyStoragePermissions()
                        url?.let { downloadFile(file) }
                    }
                }
            }

            return view
        }

    }

    companion object {
        const val INCOMING_MESSAGE = 0
        const val INCOMING_ATTACHMENT = 2
        const val SENT_MESSAGE = 1
        const val SENT_ATTACHMENT = 3
    }
}

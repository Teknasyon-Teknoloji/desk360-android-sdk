package com.teknasyon.desk360.view.adapter

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.core.view.children
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.squareup.picasso.Picasso
import com.teknasyon.desk360.R
import com.teknasyon.desk360.databinding.Desk360IncomingAttachmentItemLayoutBinding
import com.teknasyon.desk360.databinding.Desk360IncomingMessageItemLayoutBinding
import com.teknasyon.desk360.databinding.Desk360SendMessageItemLayoutBinding
import com.teknasyon.desk360.helper.Permission
import com.teknasyon.desk360.model.Desk360File
import com.teknasyon.desk360.model.Desk360Message
import com.teknasyon.desk360.themev2.Desk360ImageLayout
import com.teknasyon.desk360.view.activity.Desk360BaseActivity
import kotlinx.android.synthetic.main.desk360_attachment_message_item.view.*
import kotlinx.android.synthetic.main.desk360_incoming_attachment_item_layout.view.*
import kotlinx.android.synthetic.main.desk360_incoming_message_item_layout.view.*
import kotlinx.android.synthetic.main.desk360_incoming_message_item_layout.view.date_incoming
import kotlinx.android.synthetic.main.desk360_send_message_item_layout.view.*
import kotlinx.android.synthetic.main.desk360_send_message_item_layout.view.videoView

class Desk360TicketDetailListAdapter(
    private val downloadFile: (Desk360File) -> Unit,
    private val ticketList: ArrayList<Desk360Message>,
    private val url: String?,
    private val context: Context?

) : RecyclerView.Adapter<Desk360TicketDetailListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        when (viewType) {

            0 -> {

                val binding = DataBindingUtil.inflate<Desk360IncomingMessageItemLayoutBinding>(
                    LayoutInflater.from(parent.context),
                    R.layout.desk360_incoming_message_item_layout,
                    parent,
                    false
                )

                return ViewHolder(binding, 0)

            }
            1 -> {

                val binding = DataBindingUtil.inflate<Desk360SendMessageItemLayoutBinding>(
                    LayoutInflater.from(parent.context),
                    R.layout.desk360_send_message_item_layout,
                    parent,
                    false
                )

                return ViewHolder(binding, 1)

            }
            else -> {

                val binding = DataBindingUtil.inflate<Desk360IncomingAttachmentItemLayoutBinding>(
                    LayoutInflater.from(parent.context),
                    R.layout.desk360_incoming_attachment_item_layout,
                    parent,
                    false
                )

                return ViewHolder(binding, 2)
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val message = ticketList[position]

        when (holder.itemViewType) {

            0 -> {
                holder.itemView.message_incoming.text = message.message
                holder.itemView.date_incoming.text = message.created
            }

            1 -> {

                holder.itemView.message_send.text = message.message
                holder.itemView.date_send.text = message.created
                holder.itemView.imageUrl.visibility = View.GONE
                holder.itemView.rl_videoView.visibility = View.GONE
                holder.itemView.rl_webView.visibility = View.GONE

                if (message.tick) {
                    holder.itemView.message_tick.setImageResource(R.drawable.cift)
                } else {
                    holder.itemView.message_tick.setImageResource(R.drawable.tek)
                }

                if (url != null && url != "" && position == 0) {

                    holder.itemView.rl_webView.visibility = View.VISIBLE

                    when {

                        url.substring(url.length - 3) == "pdf" -> {

                            val webSettings: WebSettings = holder.itemView.webView.settings
                            webSettings.javaScriptEnabled = true
                            webSettings.builtInZoomControls = true

                            val url = "https://docs.google.com/viewer?embedded=true&url=$url"
                            holder.itemView.webView.loadUrl(url)

                            holder.itemView.webView.webViewClient = object : WebViewClient() {
                                override fun onPageStarted(
                                    view: WebView?,
                                    url: String?,
                                    favicon: Bitmap?
                                ) {
                                    holder.itemView.progressBar.visibility = View.GONE
                                }
                            }
                        }

                        url.substring(url.length - 3) == "mp4" -> {

                            holder.itemView.rl_videoView.visibility = View.VISIBLE
                            holder.itemView.rl_webView.visibility = View.GONE

                            val video: Uri = Uri.parse(url)
                            val mediaController = MediaController(context)
                            mediaController.setAnchorView(holder.itemView.videoView)
                            holder.itemView.videoView.setMediaController(mediaController)
                            holder.itemView.videoView.setVideoURI(video)
                            holder.itemView.videoView.setOnPreparedListener { mp ->

                                holder.itemView.videoView.visibility = View.VISIBLE
                                holder.itemView.vw_progressBar.visibility = View.GONE
                                holder.itemView.videoView_placeholder.visibility = View.GONE
                                holder.itemView.invalidate()
                                holder.itemView.videoView.start()
                                mp.isLooping = true

                            }
                        }

                        else -> {

                            holder.itemView.imageUrl.visibility = View.VISIBLE
                            Picasso.get().load(url).into(holder.itemView.imageUrl)
                        }
                    }

                } else {

                    holder.itemView.videoView.visibility = View.GONE
                    holder.itemView.imageUrl.visibility = View.GONE
                    holder.itemView.webView.visibility = View.GONE
                }
            }

            2 -> {

                holder.itemView.ll_images.removeAllViews()

                holder.itemView.image_message_incoming.text = message.message

                val images = ticketList[position].attachments?.images
                val videos = ticketList[position].attachments?.videos
                val files = ticketList[position].attachments?.files
                val others = ticketList[position].attachments?.others

                if (images?.isNotEmpty()!!) {
                    images.forEach { it ->
                        addAttachment(holder, it, "Picture")
                    }
                }

                if (videos?.isNotEmpty()!!) {
                    videos.forEach { it ->
                        addAttachment(holder, it, "Video")
                    }
                }

                if (files?.isNotEmpty()!!) {
                    files.forEach { it ->
                        addAttachment(holder, it, "File")
                    }
                }

                if (others?.isNotEmpty()!!) {
                    others.forEach { it ->
                        addAttachment(holder, it, "Other")
                    }
                }

                holder.itemView.date_incoming.text = message.created
            }
        }
    }

    private fun addAttachment(holder: ViewHolder, file: Desk360File, type: String) {

        holder.itemView.ll_images.visibility = View.VISIBLE

        val activity = holder.itemView.ll_images.context as Desk360BaseActivity

        val inflater = activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflater.inflate(R.layout.desk360_attachment_message_item, null)

        val rl = (view as LinearLayout).getChildAt(0) as RelativeLayout

        val image = rl.getChildAt(0) as ImageView
        val rlVideo = rl.getChildAt(1) as RelativeLayout

        val video = rlVideo.getChildAt(0) as VideoView
        val placeholder = rlVideo.getChildAt(1) as ImageView
        val progressBar = rlVideo.getChildAt(2) as ProgressBar
        val playButton = rlVideo.getChildAt(3) as ImageView

        val rlFile = view.getChildAt(1) as RelativeLayout

        val fileName = rlFile.getChildAt(0) as TextView
        val save = rlFile.getChildAt(1) as ImageView

        fileName.text = file.name

        when (type) {

            "Picture" -> { // if attachment has picture

                fileName.setCompoundDrawablesWithIntrinsicBounds(setImageType(file.name), 0, 0, 0)

                rlVideo.visibility = View.GONE

                Glide.with(activity).load(file.url).into(image)

                save.setOnClickListener {

                    Permission.verifyStoragePermissions(activity)
                    url?.let { downloadFile(file) }
                }
            }

            "Video" -> { // if attachment has video

                fileName.setCompoundDrawablesWithIntrinsicBounds(setVideoType(file.name), 0, 0, 0)

                image.visibility = View.GONE
                save.visibility = View.INVISIBLE

                video.setVideoURI(Uri.parse(file.url))

                video.setOnPreparedListener { mp ->

                    video.seekTo(1)

                    placeholder.visibility = View.GONE
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
                    Permission.verifyStoragePermissions(activity)
                    url?.let { downloadFile(file) }
                }
            }

            "File", "Other" -> {

                rl.visibility = View.GONE

                fileName.setCompoundDrawablesWithIntrinsicBounds(setFileType(file.name), 0, 0, 0)

                save.setOnClickListener {
                    Permission.verifyStoragePermissions(activity)
                    url?.let { downloadFile(file) }
                }
            }
        }

        holder.itemView.ll_images.addView(view)
    }

    private fun setImageType(name: String): Int {

        return when {
            name.contains("jpg") || name.contains("jpeg") -> {
                R.drawable.ic_jpg
            }
            name.contains("gif") -> {
                R.drawable.gif
            }
            name.contains("bmp") -> {
                R.drawable.bmp
            }
            name.contains("png") -> {
                R.drawable.ic_png
            }
            else -> R.drawable.others
        }
    }

    private fun setVideoType(name: String): Int {

        return when {
            name.contains("mp4") -> {
                R.drawable.mp4
            }
            name.contains("avi") -> {
                R.drawable.ic_avi
            }
            name.contains("flv") -> {
                R.drawable.flv
            }
            else -> R.drawable.others
        }
    }

    private fun setFileType(name: String): Int {

        return when {
            name.contains("pdf") -> {
                R.drawable.pdf
            }
            name.contains("doc") -> {
                R.drawable.doc
            }
            name.contains("docx") -> {
                R.drawable.docx
            }
            name.contains("xls") -> {
                R.drawable.xls
            }
            name.contains("xlsx") -> {
                R.drawable.xlsx
            }
            name.contains("zip") -> {
                R.drawable.zip
            }
            name.contains("gzip") -> {
                R.drawable.gzip
            }
            name.contains("rar") -> {
                R.drawable.rar
            }
            else -> R.drawable.others
        }
    }

    private fun hasAttachments(position: Int): Boolean {

        val images = ticketList[position].attachments?.images
        val files = ticketList[position].attachments?.files
        val videos = ticketList[position].attachments?.videos
        val others = ticketList[position].attachments?.others

        return images?.isNotEmpty()!! || files?.isNotEmpty()!! || videos?.isNotEmpty()!! || others?.isNotEmpty()!!
    }

    private fun isFileVideo(name: String): Boolean {

        return name.contains("mp4") ||
                name.contains("avi") ||
                name.contains("flv")
    }

    override fun getItemViewType(position: Int): Int {

        val hasAnswer = ticketList[position].is_answer

        return if (hasAnswer) {

            if (hasAttachments(position)) {
                2
            } else {
                0
            }

        } else {
            1
        }
    }

    override fun getItemCount(): Int {
        return ticketList.size
    }

    override fun onViewDetachedFromWindow(holder: ViewHolder) {

        try {

            val attachmentsLayout =
                (holder.itemView as LinearLayout).getChildAt(0) as Desk360ImageLayout

            val attachmentsRoot = attachmentsLayout.getChildAt(1) as LinearLayout

            attachmentsRoot.children.forEach { root ->

                root.rootView.pb_vw_progressBar.visibility = View.VISIBLE
                root.rootView.iv_video_play.visibility = View.VISIBLE
                root.rootView.iv_videoView_placeholder.visibility = View.VISIBLE

                if (isFileVideo(root.rootView.tv_type.text.toString())) {
                    root.rootView.iv_download.visibility = View.GONE
                }
            }

        } catch (e: Exception) {
        }
    }

    class ViewHolder(binding: Any?, viewType: Int) : RecyclerView.ViewHolder(

        when (viewType) {

            0 -> {
                (binding as Desk360IncomingMessageItemLayoutBinding).root
            }
            1 -> {
                (binding as Desk360SendMessageItemLayoutBinding).root
            }
            else -> {
                (binding as Desk360IncomingAttachmentItemLayoutBinding).root
            }
        }
    )
}

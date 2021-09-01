package com.teknasyon.desk360.view.adapter

import android.content.Context
import android.net.Uri
import android.text.util.Linkify
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.URLUtil
import android.webkit.WebSettings
import android.widget.MediaController
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.teknasyon.desk360.R
import com.teknasyon.desk360.databinding.Desk360IncomingMessageItemLayoutBinding
import com.teknasyon.desk360.databinding.Desk360SendMessageItemLayoutBinding
import com.teknasyon.desk360.model.Desk360Message

class Desk360TicketDetailListAdapter(
    private val ticketList: ArrayList<Desk360Message>,
    private val url: String?, private val context: Context?

) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        const val INCOMING_MESSAGE_TYPE = 0
        const val OUTGOING_MESSAGE_TYPE = 1
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = when (viewType) {
        INCOMING_MESSAGE_TYPE -> IncomingMessageViewHolder(
            Desk360IncomingMessageItemLayoutBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
        else -> OutgoingMessageViewHolder(
            Desk360SendMessageItemLayoutBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val ticket = ticketList[position]

        when (holder) {
            is IncomingMessageViewHolder -> holder.bind(ticket)
            is OutgoingMessageViewHolder -> holder.bind(ticket, position)
        }
    }

    override fun getItemViewType(position: Int) =
        if (ticketList[position].is_answer == true) INCOMING_MESSAGE_TYPE else OUTGOING_MESSAGE_TYPE

    override fun getItemCount() = ticketList.size

    inner class OutgoingMessageViewHolder(private val binding: Desk360SendMessageItemLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(ticket: Desk360Message, position: Int) {
            binding.messageSend.text = ticket.message
            if (URLUtil.isValidUrl(URLUtil.guessUrl(ticket.message))) {
                Linkify.addLinks(binding.messageSend, Linkify.WEB_URLS)
                context?.let {
                    ContextCompat.getColor(
                        it, R.color.color_text_link
                    )
                }?.let { binding.messageSend.setLinkTextColor(it) }
            }

            binding.dateSend.text = ticket.created
            binding.webView.visibility = View.GONE
            binding.imageUrl.visibility = View.GONE
            binding.videoView.visibility = View.GONE
            binding.messageTick.setImageResource(
                if (ticket.tick == true)
                    R.drawable.cift
                else
                    R.drawable.tek
            )

            if (url != null && url != "" && position == 0) {
                when {
                    url.substring(url.length - 3) == "pdf" -> {
                        binding.webView.visibility = View.VISIBLE
                        binding.webView.loadUrl("https://desk-360.s3.amazonaws.com/attachment/52/desk360test/QOFN3o0jaPtdPnVmNJJpPSZtErvTHR1mKMvkr7i3.pdf")
                        val webSettings: WebSettings = binding.webView.settings
                        webSettings.javaScriptEnabled = true
                        webSettings.builtInZoomControls = true
                        val myPdfUrl =
                            "https://desk-360.s3.amazonaws.com/attachment/52/desk360test/QOFN3o0jaPtdPnVmNJJpPSZtErvTHR1mKMvkr7i3.pdf"
                        val url = "https://docs.google.com/viewer?embedded=true&url=$myPdfUrl"
                        binding.webView.loadUrl(url)
                    }
                    url.substring(url.length - 3) == "mp4" -> {
                        binding.videoView.visibility = View.VISIBLE
                        val video: Uri = Uri.parse(url)
                        val mediaController = MediaController(context)
                        mediaController.setAnchorView(binding.videoView)
                        binding.videoView.setMediaController(mediaController)
                        binding.videoView.setVideoURI(video)
                        binding.videoView.setOnPreparedListener { mp ->
                            mp.isLooping = true
                            binding.videoView.start()
                        }
                    }
                    else -> {
                        binding.imageUrl.visibility = View.VISIBLE
                        Glide.with(binding.imageUrl.context).load(url)
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

    inner class IncomingMessageViewHolder(private val binding: Desk360IncomingMessageItemLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(ticket: Desk360Message) {
            binding.dateIncoming.text = ticket.created
            binding.messageIncoming.text = ticket.message

            if (URLUtil.isValidUrl(URLUtil.guessUrl(ticket.message))) {
                Linkify.addLinks(binding.messageIncoming, Linkify.WEB_URLS)
                context?.let {
                    ContextCompat.getColor(
                        it, R.color.color_text_link
                    )
                }?.let { binding.messageIncoming.setLinkTextColor(it) }
            }
        }
    }
}

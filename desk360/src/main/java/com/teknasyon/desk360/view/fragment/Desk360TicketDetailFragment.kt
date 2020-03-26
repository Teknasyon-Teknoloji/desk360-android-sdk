package com.teknasyon.desk360.view.fragment

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.drawable.GradientDrawable
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.teknasyon.desk360.R
import com.teknasyon.desk360.databinding.Desk360FragmentTicketDetailBinding
import com.teknasyon.desk360.helper.*
import com.teknasyon.desk360.model.CacheTicket
import com.teknasyon.desk360.model.Desk360Message
import com.teknasyon.desk360.model.Desk360TicketResponse
import com.teknasyon.desk360.view.activity.Desk360BaseActivity
import com.teknasyon.desk360.view.adapter.Desk360TicketDetailListAdapter
import com.teknasyon.desk360.viewmodel.TicketDetailViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.desk360_fragment_main.*
import java.util.*

open class Desk360TicketDetailFragment : Fragment() {

    private var binding: Desk360FragmentTicketDetailBinding? = null
    private var ticketDetailAdapter: Desk360TicketDetailListAdapter? = null
    private val gradientDrawable = GradientDrawable()
    private var ticketId: Int? = null
    private var ticketStatus: String? = null
    private var backButtonAction: Disposable? = null

    private val preferencesManager = PreferencesManager()
    private var cacheDesk360TicketResponse: Desk360TicketResponse? = null

    private lateinit var desk360BaseActivity: Desk360BaseActivity

    private var observer = Observer<Desk360TicketResponse> {

        binding?.loadingProgressTicketDetail?.visibility = View.INVISIBLE

        if (it != null) {

            preferencesManager.writeObject(ticketId.toString(), it)
            cacheDesk360TicketResponse = preferencesManager.readObject(
                ticketId.toString(),
                Desk360TicketResponse::class.java
            )

            ticketDetailAdapter = Desk360TicketDetailListAdapter(
                cacheDesk360TicketResponse!!.messages!!,
                cacheDesk360TicketResponse!!.attachment_url,
                context
            )

            val layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)

            binding?.messageDetailRecyclerView?.apply {
                this.layoutManager = layoutManager
                adapter = ticketDetailAdapter
                scrollToPosition(cacheDesk360TicketResponse!!.messages?.size!! - 1)
            }
        }
    }

    private var addMessageObserver = Observer<Desk360Message> {

        binding?.loadingProgressTicketDetail?.visibility = View.INVISIBLE

        if (it != null) {

            cacheDesk360TicketResponse = preferencesManager.readObject(
                ticketId.toString(),
                Desk360TicketResponse::class.java
            )
            val tickets = cacheDesk360TicketResponse!!.messages
            tickets!![tickets.size - 1] = it

            preferencesManager.writeObject(ticketId.toString(), cacheDesk360TicketResponse!!)

            Handler().postDelayed({ addTicketToCache(null) }, 300)

            binding?.messageEditText?.setText("")
        }
    }

    private var viewModel: TicketDetailViewModel? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        desk360BaseActivity = context as Desk360BaseActivity
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

        desk360BaseActivity.contactUsMainBottomBar.visibility = View.GONE

        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)

        desk360BaseActivity.contactUsMainBottomBar.visibility = View.GONE
        desk360BaseActivity.changeMainUI()

        cacheDesk360TicketResponse =
            preferencesManager.readObject(ticketId.toString(), Desk360TicketResponse::class.java)

        cacheDesk360TicketResponse?.let {

            cacheDesk360TicketResponse!!.messages?.let {

                if (cacheDesk360TicketResponse!!.messages!!.isNotEmpty()) {
                    binding?.loadingProgressTicketDetail?.visibility = View.INVISIBLE
                } else {
                    binding?.loadingProgressTicketDetail?.visibility = View.VISIBLE
                }

                ticketDetailAdapter = Desk360TicketDetailListAdapter(
                    cacheDesk360TicketResponse!!.messages!!,
                    cacheDesk360TicketResponse!!.attachment_url,
                    context
                )

                val layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)

                binding?.messageDetailRecyclerView?.apply {
                    this.layoutManager = layoutManager
                    adapter = ticketDetailAdapter
                    scrollToPosition(cacheDesk360TicketResponse!!.messages?.size!! - 1)
                }
            }
        } ?: run {
            binding?.loadingProgressTicketDetail?.visibility = View.VISIBLE
        }

        viewModel = ticketId?.let { TicketDetailViewModel(it) }

        viewModel?.ticketDetailList?.observe(this, observer)

        viewModel?.addMessageItem?.observe(this, addMessageObserver)

        Desk360CustomStyle.setStyle(
            Desk360Constants.currentType?.data?.first_screen?.button_style_id,
            binding!!.addNewTicketButton,
            context!!
        )

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
                            binding?.messageEditText?.text.toString()
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

        DrawableCompat.setTint(
            binding!!.messageEditText.background,
            ContextCompat.getColor(context!!, R.color.colorHintDesk360)
        )

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

        if (arguments != null) {
            ticketId = arguments!!.getInt("ticket_id")
            ticketStatus = arguments!!.getString("ticket_status")
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        viewModel?.ticketDetailList?.removeObserver(observer)
        viewModel?.addMessageItem?.removeObserver(addMessageObserver)
        if (backButtonAction?.isDisposed == false)
            backButtonAction?.dispose()
        hideSoftKeyboard()
    }

    private fun hideSoftKeyboard() {

        activity?.let {
            val view = activity!!.currentFocus
            if (view != null) {
                val imm =
                    activity!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(view.windowToken, 0)
            }
        }
    }

    private fun addTicketToCache(desk360Message: Desk360Message?) {

        cacheDesk360TicketResponse =
            preferencesManager.readObject(ticketId.toString(), Desk360TicketResponse::class.java)

        viewModel?.ticketDetailList?.value?.messages?.clear()
        viewModel?.ticketDetailList?.value?.messages?.addAll(cacheDesk360TicketResponse!!.messages!!)

        desk360Message?.let {

            viewModel?.ticketDetailList?.value?.messages?.add(desk360Message)
            cacheDesk360TicketResponse?.messages?.add(desk360Message)
            preferencesManager.writeObject(ticketId.toString(), cacheDesk360TicketResponse!!)
        }

        ticketDetailAdapter = Desk360TicketDetailListAdapter(
            viewModel?.ticketDetailList?.value?.messages!!,
            cacheDesk360TicketResponse!!.attachment_url,
            context
        )

        val layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        binding!!.messageDetailRecyclerView.layoutManager = layoutManager
        binding!!.messageDetailRecyclerView.adapter = ticketDetailAdapter

        binding?.messageDetailRecyclerView?.scrollToPosition(cacheDesk360TicketResponse!!.messages!!.size - 1)
    }
}
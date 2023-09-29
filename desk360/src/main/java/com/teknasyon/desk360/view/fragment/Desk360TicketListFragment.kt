package com.teknasyon.desk360.view.fragment

import android.content.Context
import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.method.ScrollingMovementMethod
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.teknasyon.desk360.R
import com.teknasyon.desk360.databinding.Desk360FragmentTicketListBinding
import com.teknasyon.desk360.helper.Desk360CustomStyle
import com.teknasyon.desk360.helper.Desk360SDK
import com.teknasyon.desk360.helper.PreferencesManager
import com.teknasyon.desk360.helper.RxBus
import com.teknasyon.desk360.model.CacheTicket
import com.teknasyon.desk360.model.Desk360TicketResponse
import com.teknasyon.desk360.view.activity.Desk360BaseActivity
import com.teknasyon.desk360.view.adapter.Desk360TicketListAdapter
import com.teknasyon.desk360.viewmodel.GetTypesViewModel
import com.teknasyon.desk360.viewmodel.TicketListViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import com.teknasyon.desk360.view.fragment.Desk360TicketListFragmentDirections.Companion as Directions

open class Desk360TicketListFragment : Fragment(), Desk360TicketListAdapter.TicketOnClickListener {
    private var desk360BaseActivity: Desk360BaseActivity? = null
    private var binding: Desk360FragmentTicketListBinding? = null
    private var disposable: Disposable? = null
    private var isTypesFetched = false

    private var ticketAdapter: Desk360TicketListAdapter? = null
    private var cacheTickets: ArrayList<Desk360TicketResponse> = arrayListOf()
    private var tickets: ArrayList<Desk360TicketResponse> = arrayListOf()
    private var viewModel: TicketListViewModel? = null
    private var isPushed: Boolean = false

    override fun onAttach(context: Context) {
        super.onAttach(context)
        desk360BaseActivity = context as Desk360BaseActivity
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = Desk360FragmentTicketListBinding.inflate(
            inflater, container, false
        )

        binding?.txtBottomFooterMainTicketList?.movementMethod = ScrollingMovementMethod()

        return binding?.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        try {
            val topic = desk360BaseActivity?.selectedTopic
            desk360BaseActivity?.selectedTopic = null
            if (!topic.isNullOrBlank()) {
                findNavController()
                    .navigate(Directions.actionTicketListFragmentToAddNewTicketFragment(topic))
            }
            initUI()

            val call = GetTypesViewModel()
            call.getTypes { isFetched ->

                if (isFetched) {
                    isTypesFetched = true
                }
            }

            if (getCacheTickets().isNotEmpty()) {
                setViewFillLayout()
            } else {
                if (desk360BaseActivity?.isMainLoadingShown == false) {
                    desk360BaseActivity?.isMainLoadingShown = true
                    binding?.loadingCurrentTicket?.visibility = View.VISIBLE
                }

                listenCurrentTicketList()
            }

            getTickets()
        } catch (e: Exception) {
            Log.e("exception", e.toString())
        }
    }

    private fun initUI() {
        desk360BaseActivity?.changeMainUI()
        desk360BaseActivity?.binding?.contactUsMainBottomBar?.visibility = View.VISIBLE

        binding?.apply {
            emptysAddNewTicketButtonTicketList.setOnClickListener {

                desk360BaseActivity?.addBtnClicked = true
                Handler(Looper.getMainLooper()).removeCallbacksAndMessages(null)
                Handler(Looper.getMainLooper()).postDelayed({
                    desk360BaseActivity?.addBtnClicked = false
                }, 800)

                findNavController().navigate(
                    Directions.actionTicketListFragmentToAddNewTicketFragment(
                        null
                    )
                )
            }

            Desk360CustomStyle.setStyle(
                Desk360SDK.config?.data?.first_screen?.button_style_id,
                emptysAddNewTicketButtonTicketList,
                requireContext()
            )

            Desk360CustomStyle.setFontWeight(
                emptyListLayoutTicketListSubTitle,
                context,
                Desk360SDK.config?.data?.first_screen?.sub_title_font_weight
            )

            Desk360CustomStyle.setFontWeight(
                emptyListLayoutTicketListDesc,
                context,
                Desk360SDK.config?.data?.first_screen?.description_font_weight
            )

            Desk360CustomStyle.setFontWeight(
                txtOpenMessageFormTicketList,
                context,
                Desk360SDK.config?.data?.first_screen?.button_text_font_weight
            )

            Desk360CustomStyle.setFontWeight(
                txtBottomFooterMainTicketList,
                context,
                Desk360SDK.config?.data?.general_settings?.bottom_note_font_weight
            )

            firstScreenButtonIcon.apply {
                setImageResource(R.drawable.zarf)
                setColorFilter(
                    Color.parseColor(Desk360SDK.config?.data?.first_screen?.button_text_color),
                    PorterDuff.Mode.SRC_ATOP
                )
            }
        }
    }

    private fun getTickets() {
        binding?.currentTicketList?.visibility = View.INVISIBLE
        binding?.emptyLayoutCurrent?.visibility = View.INVISIBLE

        val preferencesManager = PreferencesManager()

        cacheTickets = try {
            preferencesManager.readObject(
                "tickets",
                CacheTicket::class.java
            ) as ArrayList<Desk360TicketResponse>
        } catch (e: Exception) {
            tickets
        }

        ticketAdapter = Desk360TicketListAdapter(context, cacheTickets)

        setViews()

        binding?.currentTicketList?.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding?.currentTicketList?.adapter = ticketAdapter
        ticketAdapter?.clickItem = this

        viewModel = ViewModelProvider(requireActivity())[TicketListViewModel::class.java]

        binding?.apply {
            Desk360CustomStyle.setStyle(
                Desk360SDK.config?.data?.first_screen?.button_style_id,
                openMessageformEmptyCurrentList,
                requireContext()
            )

            Desk360CustomStyle.setFontWeight(
                ticketListEmptyButtonText,
                context,
                Desk360SDK.config?.data?.first_screen?.button_text_font_weight
            )

            imageEmptyCurrent.apply {
                requestLayout()
                setImageResource(R.drawable.no_expired_ticket_list_icon)
                setColorFilter(
                    Color.parseColor(Desk360SDK.config?.data?.ticket_list_screen?.tab_text_active_color),
                    PorterDuff.Mode.SRC_ATOP
                )
            }

            ticketListEmptyButtonIcon.apply {
                setImageResource(R.drawable.zarf)
                setColorFilter(
                    Color.parseColor(Desk360SDK.config?.data?.ticket_list_screen?.ticket_list_empty_text_color),
                    PorterDuff.Mode.SRC_ATOP
                )
            }

            openMessageformEmptyCurrentList.setOnClickListener {
                findNavController().navigate(Directions.actionTicketListFragmentToPreNewTicketFragment())
            }
        }

        viewModel?.ticketList?.observe(viewLifecycleOwner, Observer {
            it?.let {
                tickets.clear()
                tickets.addAll(it)

                preferencesManager.writeObject("tickets", tickets)
                swapTicketAdapter(preferencesManager)

                setViews()

                desk360BaseActivity?.ticketId?.let {
                    if (!isPushed) {
                        forcePushToTicketDetail()
                        isPushed = true
                    }
                }
            }
        })

        val showLoading = cacheTickets.isEmpty()
        viewModel?.register(showLoading)
    }

    private fun setViews() {

        viewModel?.let {
            viewModel?.progress?.set(View.GONE)
        }

        desk360BaseActivity?.notifyToolBar(cacheTickets)

        if (cacheTickets.isEmpty()) {

            binding?.currentTicketList?.visibility = View.INVISIBLE
            binding?.emptyLayoutCurrent?.visibility = View.VISIBLE

        } else {

            binding?.currentTicketList?.visibility = View.VISIBLE
            binding?.emptyLayoutCurrent?.visibility = View.INVISIBLE
        }
    }


    private fun swapTicketAdapter(preferencesManager: PreferencesManager) {
        cacheTickets = preferencesManager.readObject(
            "tickets",
            CacheTicket::class.java
        ) as ArrayList<Desk360TicketResponse>

        ticketAdapter = Desk360TicketListAdapter(context, cacheTickets)

        binding?.currentTicketList?.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding?.currentTicketList?.adapter = ticketAdapter
        ticketAdapter?.clickItem = this
    }

    private fun forcePushToTicketDetail() {
        tickets.filter { it.id != null && it.id.toString() == desk360BaseActivity?.ticketId }
            .forEach { ticket ->
                findNavController().navigate(
                    Desk360SuccessScreenDirections.actionGlobalTicketDetailFragment(
                        ticket.id!!, ticket.status.toString(),
                    )
                )
            }
    }

    private fun listenCurrentTicketList() {
        disposable =
            RxBus.listen(HashMap<String, Int>()::class.java).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({

                    if (it.keys.contains("sizeTicketList")) {
                        it["sizeTicketList"]?.let { sizeList ->
                            if (sizeList > 0) {
                                setViewFillLayout()
                            } else {
                                setViewEmptyLayout()
                            }
                        }
                    }
                    if (it.keys.contains("unReadSizeTicketList")) {
                        it["unReadSizeTicketList"]?.let { sizeUnread ->
                            // unread ticket size
                        }
                    }
                }, { t ->
                    Log.d("dataSize", "$t")
                })
    }

    private fun getCacheTickets(): ArrayList<Desk360TicketResponse> {
        val preferencesManager = PreferencesManager()

        return try {
            preferencesManager.readObject(
                "tickets",
                CacheTicket::class.java
            ) as ArrayList<Desk360TicketResponse>

        } catch (e: Exception) {
            ArrayList()
        }
    }

    private fun setViewFillLayout() {
        desk360BaseActivity?.binding?.toolbarTitle?.text =
            Desk360SDK.config?.data?.ticket_list_screen?.title

        binding?.apply {
            fillListLayout.visibility = View.VISIBLE
            fragmentTicketListRoot.visibility = View.VISIBLE
            loadingCurrentTicket.visibility = View.INVISIBLE
            emptyListLayoutTicketList.visibility = View.INVISIBLE
        }
    }

    private fun setViewEmptyLayout() {
        desk360BaseActivity?.binding?.toolbarTitle?.text =
            Desk360SDK.config?.data?.first_screen?.title

        binding?.apply {
            fillListLayout.visibility = View.INVISIBLE
            fragmentTicketListRoot.visibility = View.VISIBLE
            loadingCurrentTicket.visibility = View.INVISIBLE
            emptyListLayoutTicketList.visibility = View.VISIBLE

            //Main BackGround Color
            txtBottomFooterMainTicketList.setBackgroundColor(Color.parseColor(Desk360SDK.config?.data?.general_settings?.main_background_color))

            //Sub Title Text
            emptyListLayoutTicketListSubTitle.setTextColor(Color.parseColor(Desk360SDK.config?.data?.first_screen?.sub_title_color))
            emptyListLayoutTicketListSubTitle.text =
                Desk360SDK.config?.data?.first_screen?.sub_title
            emptyListLayoutTicketListSubTitle.textSize =
                Desk360SDK.config?.data?.first_screen?.sub_title_font_size!!.toFloat()

            //Description Title Text
            emptyListLayoutTicketListDesc.setTextColor(Color.parseColor(Desk360SDK.config?.data?.first_screen?.description_color))
            emptyListLayoutTicketListDesc.text =
                Desk360SDK.config?.data?.first_screen?.description
            emptyListLayoutTicketListDesc.textSize =
                Desk360SDK.config?.data?.first_screen?.description_font_size!!.toFloat()

            txtOpenMessageFormTicketList.setTextColor(Color.parseColor(Desk360SDK.config?.data?.first_screen?.button_text_color))
            txtOpenMessageFormTicketList.textSize =
                Desk360SDK.config?.data?.first_screen?.button_text_font_size!!.toFloat()
            txtOpenMessageFormTicketList.text = Desk360CustomStyle.setButtonText(
                Desk360SDK.config?.data?.first_screen?.button_text!!.length,
                Desk360SDK.config?.data?.first_screen?.button_text
            )

            firstScreenButtonIcon.visibility =
                if (Desk360SDK.config?.data?.first_screen?.button_icon_is_hidden == true)
                    View.VISIBLE
                else
                    View.INVISIBLE

            txtBottomFooterMainTicketList.setTextColor(Color.parseColor(Desk360SDK.config?.data?.general_settings?.bottom_note_color))
            txtBottomFooterMainTicketList.textSize =
                Desk360SDK.config?.data?.general_settings?.bottom_note_font_size!!.toFloat()

            txtBottomFooterMainTicketList.text =
                Desk360SDK.config?.data?.first_screen?.bottom_note_text

            txtBottomFooterMainTicketList.visibility =
                if (!Desk360SDK.config?.data?.first_screen?.bottom_note_is_hidden!!)
                    View.INVISIBLE
                else
                    View.VISIBLE


        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (disposable?.isDisposed == false)
            disposable?.dispose()
    }

    override fun selectTicket(item: Desk360TicketResponse, position: Int) {
        view?.let {

            val bundle = Bundle()

            item.id?.let { it1 -> bundle.putInt("ticket_id", it1) }
            bundle.putString("ticket_status", item.status.toString())

            binding?.root?.let { it1 ->
                Navigation
                    .findNavController(it1)
                    .navigate(R.id.action_global_ticketDetailFragment, bundle)
            }
        }
    }

}

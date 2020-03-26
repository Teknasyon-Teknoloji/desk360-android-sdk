package com.teknasyon.desk360.view.fragment

import android.content.Context
import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.teknasyon.desk360.R
import com.teknasyon.desk360.databinding.FragmentCurrentTicketListBinding
import com.teknasyon.desk360.helper.Desk360CustomStyle
import com.teknasyon.desk360.helper.Desk360Constants
import com.teknasyon.desk360.helper.PreferencesManager
import com.teknasyon.desk360.model.CacheTicket
import com.teknasyon.desk360.model.Desk360TicketResponse
import com.teknasyon.desk360.view.activity.Desk360BaseActivity
import com.teknasyon.desk360.view.adapter.Desk360TicketListAdapter
import com.teknasyon.desk360.viewmodel.TicketListViewModel

class Desk360CurrentTicketFragment : Fragment(), Desk360TicketListAdapter.TicketOnClickListener {

    private var ticketAdapter: Desk360TicketListAdapter? = null

    private var cacheTickets: ArrayList<Desk360TicketResponse> = arrayListOf()
    private var tickets: ArrayList<Desk360TicketResponse> = arrayListOf()

    private var viewModel: TicketListViewModel? = null

    private lateinit var binding: FragmentCurrentTicketListBinding
    private lateinit var desk360BaseActivity: Desk360BaseActivity

    private var isPushed: Boolean = false

    override fun onAttach(context: Context) {
        super.onAttach(context)
        desk360BaseActivity = context as Desk360BaseActivity
    }

    override fun selectTicket(item: Desk360TicketResponse, position: Int) {

        view?.let {

            val bundle = Bundle()

            item.id?.let { it1 -> bundle.putInt("ticket_id", it1) }
            bundle.putString("ticket_status", item.status.toString())

            binding.root.let { it1 ->
                Navigation
                    .findNavController(it1)
                    .navigate(R.id.action_global_ticketDetailFragment, bundle)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)

        FragmentCurrentTicketListBinding.inflate(inflater, container, true).also {

            binding = it
            binding.lifecycleOwner = viewLifecycleOwner
            return it.root
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        desk360BaseActivity.changeMainUI()

        binding.currentTicketList.visibility = View.INVISIBLE
        binding.emptyLayoutCurrent.visibility = View.INVISIBLE

        val preferencesManager = PreferencesManager()

        cacheTickets = try {
            preferencesManager.readObject(
                "tickets",
                CacheTicket::class.java
            ) as ArrayList<Desk360TicketResponse>
        } catch (e: Exception) {
            this.tickets
        }

        ticketAdapter = Desk360TicketListAdapter(context, cacheTickets)

        setViews()

        binding.currentTicketList?.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.currentTicketList?.adapter = ticketAdapter
        ticketAdapter?.clickItem = this

        viewModel = ViewModelProviders.of(activity!!).get(TicketListViewModel::class.java)

        Desk360CustomStyle.setStyle(
            Desk360Constants.currentType?.data?.first_screen?.button_style_id,
            binding.openMessageformEmptyCurrentList,
            context!!
        )

        Desk360CustomStyle.setFontWeight(
            binding.ticketListEmptyButtonText,
            context,
            Desk360Constants.currentType?.data?.first_screen?.button_text_font_weight
        )

        binding.imageEmptyCurrent.requestLayout()
        binding.viewModelList = viewModel
        binding.imageEmptyCurrent.setImageResource(R.drawable.no_expired_ticket_list_icon)
        binding.imageEmptyCurrent.setColorFilter(
            Color.parseColor(Desk360Constants.currentType?.data?.ticket_list_screen?.tab_text_active_color),
            PorterDuff.Mode.SRC_ATOP
        )

        viewModel?.ticketList?.observe(viewLifecycleOwner, Observer {

            it?.let {

                this.tickets.clear()
                this.tickets.addAll(it)

                preferencesManager.writeObject("tickets", this.tickets)
                swapTicketAdapter(preferencesManager)

                setViews()

                desk360BaseActivity.targetId?.let {

                    if (!isPushed) {
                        forcePushToTicketDetail()
                        isPushed = true
                    }
                }
            }
        })

        binding.ticketListEmptyButtonIcon.setImageResource(R.drawable.zarf)
        binding.ticketListEmptyButtonIcon.setColorFilter(
            Color.parseColor(Desk360Constants.currentType?.data?.ticket_list_screen?.ticket_list_empty_text_color),
            PorterDuff.Mode.SRC_ATOP
        )

        binding.openMessageformEmptyCurrentList.setOnClickListener {
            Navigation
                .findNavController(it)
                .navigate(R.id.action_ticketListFragment_to_preNewTicketFragment, null)
        }

        val showLoading = cacheTickets.isEmpty()


        if (!isRegistered) {
            viewModel?.register(showLoading)

        } else {
            //swapTicketAdapter(preferencesManager)
            viewModel?.getTicketList(showLoading)
        }
    }

    private fun swapTicketAdapter(preferencesManager: PreferencesManager) {

        cacheTickets = preferencesManager.readObject(
            "tickets",
            CacheTicket::class.java
        ) as ArrayList<Desk360TicketResponse>

        ticketAdapter = Desk360TicketListAdapter(context, cacheTickets)

        binding.currentTicketList?.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.currentTicketList?.adapter = ticketAdapter
        ticketAdapter?.clickItem = this
    }

    private fun forcePushToTicketDetail() {

        for (item: Desk360TicketResponse in tickets) {

            if (item.id.toString() == desk360BaseActivity.targetId) {

                val bundle = Bundle()

                item.id?.let { itemId -> bundle.putInt("ticket_id", itemId) }

                bundle.putString("ticket_status", item.status.toString())

                binding.root.let { it1 ->
                    Navigation
                        .findNavController(it1)
                        .navigate(R.id.action_global_ticketDetailFragment, bundle)
                }
            }
        }
    }

    private fun setViews() {

        viewModel?.let {
            viewModel!!.progress!!.set(View.GONE)
        }

        desk360BaseActivity.notifyToolBar(cacheTickets)

        if (cacheTickets.isEmpty()) {

            binding.currentTicketList.visibility = View.INVISIBLE
            binding.emptyLayoutCurrent.visibility = View.VISIBLE

        } else {

            binding.currentTicketList.visibility = View.VISIBLE
            binding.emptyLayoutCurrent.visibility = View.INVISIBLE
        }
    }

    companion object {

        var ticketSize = 0
        var isRegistered: Boolean = false

        fun newInstance(): Desk360CurrentTicketFragment {
            val args = Bundle()
            val fragment = Desk360CurrentTicketFragment()
            fragment.arguments = args
            return fragment
        }
    }
}
package com.teknasyon.desk360.view.fragment

import android.content.Context
import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.teknasyon.desk360.R
import com.teknasyon.desk360.databinding.FragmentPastTicketListBinding
import com.teknasyon.desk360.helper.Desk360Constants
import com.teknasyon.desk360.helper.PreferencesManager
import com.teknasyon.desk360.model.CacheTicket
import com.teknasyon.desk360.model.Desk360TicketResponse
import com.teknasyon.desk360.view.activity.Desk360BaseActivity
import com.teknasyon.desk360.view.adapter.Desk360TicketListAdapter
import com.teknasyon.desk360.viewmodel.TicketListViewModel

class Desk360PastTicketListFragment : Fragment(), Desk360TicketListAdapter.TicketOnClickListener {

    private var ticketAdapter: Desk360TicketListAdapter? = null

    private var cacheTickets: ArrayList<Desk360TicketResponse> = arrayListOf()
    private var tickets: ArrayList<Desk360TicketResponse> = arrayListOf()

    private var viewModel: TicketListViewModel? = null

    private lateinit var desk360BaseActivity: Desk360BaseActivity
    private lateinit var binding: FragmentPastTicketListBinding

    override fun selectTicket(item: Desk360TicketResponse, position: Int) {

        view?.let {

            val bundle = Bundle()
            item.id?.let { it1 -> bundle.putInt("ticket_id", it1) }

            bundle.putString("ticket_status", item.status.toString())

            binding.root.let { it1 -> Navigation.findNavController(it1).navigate(R.id.action_global_ticketDetailFragment, bundle) }
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        desk360BaseActivity = context as Desk360BaseActivity
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        super.onCreateView(inflater, container, savedInstanceState)

        FragmentPastTicketListBinding.inflate(inflater, container, true).also {
            binding = it
            binding.lifecycleOwner = viewLifecycleOwner
            return it.root
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        desk360BaseActivity.changeMainUI()

        val preferencesManager = PreferencesManager()

        try {
            cacheTickets = preferencesManager.readObject("pastTickets", CacheTicket::class.java) as ArrayList<Desk360TicketResponse>
            ticketAdapter = Desk360TicketListAdapter(context, cacheTickets)
        } catch (e:Exception){
            ticketAdapter = Desk360TicketListAdapter(context, this.tickets)
        }

        binding.pastTicketList?.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.pastTicketList?.adapter = ticketAdapter
        ticketAdapter?.clickItem = this

        binding.noExpiredImageEmpty.requestLayout()
        binding.noExpiredImageEmpty.setImageResource(R.drawable.no_expired_ticket_list_icon)

        binding.noExpiredImageEmpty.setColorFilter(Color.parseColor(Desk360Constants.currentType?.data?.ticket_list_screen?.tab_text_active_color), PorterDuff.Mode.SRC_ATOP)

        viewModel = ViewModelProviders.of(activity!!).get(TicketListViewModel::class.java)

        viewModel?.expiredList?.observe(viewLifecycleOwner, Observer {
            it?.let {

                this.tickets.clear()
                this.tickets.addAll(it)

                preferencesManager.writeObject("pastTickets",this.tickets)
                cacheTickets = preferencesManager.readObject("pastTickets", CacheTicket::class.java) as ArrayList<Desk360TicketResponse>

                ticketAdapter = Desk360TicketListAdapter(context, cacheTickets)
                binding.pastTicketList?.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                binding.pastTicketList?.adapter = ticketAdapter
                ticketAdapter?.clickItem = this

                setViews()
            }
        })

        binding.root.setBackgroundColor(Color.parseColor(Desk360Constants.currentType?.data?.ticket_list_screen?.ticket_list_backgroud_color))
    }

    private fun setViews() {

        if (tickets.isEmpty()) {
            binding.pastTicketList.visibility = View.INVISIBLE
            binding.noExpireEmptyLayout.visibility = View.VISIBLE
        } else {
            binding.pastTicketList.visibility = View.VISIBLE
            binding.noExpireEmptyLayout.visibility = View.INVISIBLE
        }
    }

    override fun onResume() {
        super.onResume()
        setViews()
    }

    companion object {

        fun newInstance(): Desk360PastTicketListFragment {
            val args = Bundle()
            val fragment = Desk360PastTicketListFragment()
            fragment.arguments = args
            return fragment
        }
    }

}
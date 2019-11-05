package com.teknasyon.desk360.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.teknasyon.desk360.R
import com.teknasyon.desk360.databinding.FragmentCurrentTicketListBinding
import com.teknasyon.desk360.model.Desk360TicketResponse
import com.teknasyon.desk360.view.activity.Desk360BaseActivity
import com.teknasyon.desk360.view.adapter.Desk360TicketListAdapter
import com.teknasyon.desk360.viewmodel.TicketListViewModel

class Desk360CurrentTicketFragment : Fragment(), Desk360TicketListAdapter.TicketOnClickListener {

    private var ticketAdapter: Desk360TicketListAdapter? = null
    private var binding: FragmentCurrentTicketListBinding? = null
    private var viewModel: TicketListViewModel? = null
    private var currentTicketList: ArrayList<Desk360TicketResponse> = arrayListOf()


    override fun selectTicket(item: Desk360TicketResponse, position: Int) {
        view?.let {
            val bundle = Bundle()
            item.id?.let { it1 -> bundle.putInt("ticket_id", it1) }
            bundle.putString("ticket_status", item.status.toString())

            (activity as Desk360BaseActivity).userRegistered = false

            Navigation
                .findNavController(it)
                .navigate(R.id.action_ticketListFragment_to_ticketDetailFragment, bundle)
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (binding == null) {
            binding = DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_current_ticket_list,
                container,
                false
            )
        }
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProviders.of(activity!!).get(TicketListViewModel::class.java)
        viewModel?.ticketList?.observe(activity!!,Observer{
            getCurrentTickets(it)
            ticketAdapter = Desk360TicketListAdapter(context, currentTicketList)
            binding?.currentTicketList?.layoutManager = LinearLayoutManager(context)
            binding?.currentTicketList?.adapter = ticketAdapter
            ticketAdapter?.clickItem = this
        })

    }

    private fun getCurrentTickets(list: ArrayList<Desk360TicketResponse>) {
        for(i in 0 until list.size){
            if(list[i].status != "expired"){
                currentTicketList.add(list[i])
            }
        }
    }

    companion object {
        fun newInstance(): Desk360CurrentTicketFragment {
            val args = Bundle()
            val fragment = Desk360CurrentTicketFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onResume() {
        super.onResume()
        (activity as Desk360BaseActivity).userRegistered = true
    }
}
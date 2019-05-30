package com.teknasyon.desk360.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.teknasyon.desk360.R
import com.teknasyon.desk360.databinding.FragmentTicketListBinding
import com.teknasyon.desk360.model.TicketResponse
import com.teknasyon.desk360.view.adapter.TicketListAdapter
import com.teknasyon.desk360.viewmodel.TicketListViewModel


/**
 * Created by seyfullah on 24,May,2019
 *
 */

class TicketListFragment : Fragment(), TicketListAdapter.TicketOnClickListener {


    private var ticketAdapter: TicketListAdapter? = null
    private var observer = Observer<ArrayList<TicketResponse>>
    {
        if (it != null) {
            ticketAdapter = TicketListAdapter(context, it)
            binding?.ticketList?.layoutManager = LinearLayoutManager(context)
            binding?.ticketList?.adapter = ticketAdapter
            ticketAdapter?.clickItem = this
        } else {
            binding?.emptyListLayout?.visibility = View.VISIBLE
        }
    }

    var binding: FragmentTicketListBinding? = null

    override fun selectTicket(item: TicketResponse, position: Int) {
        view?.let {
            val bundle = Bundle()
            item.id?.let { it1 -> bundle.putInt("ticket_id", it1) }
            bundle.putString("ticket_status", item.status.toString())

            Navigation
                .findNavController(it)
                .navigate(R.id.action_ticketListFragment_to_ticketDetailFragment, bundle)
        }
    }

    private var viewModel: TicketListViewModel? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_ticket_list, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = TicketListViewModel()
        viewModel?.ticketList?.observe(this, observer)
        binding?.emptyListLayout?.setOnClickListener {
            Navigation
                .findNavController(it)
                .navigate(R.id.action_ticketListFragment_to_addNewTicketFragment, null)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel?.ticketList?.removeObserver(observer)
    }
}
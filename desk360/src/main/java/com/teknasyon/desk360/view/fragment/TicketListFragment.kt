package com.teknasyon.desk360.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.teknasyon.desk360.R
import com.teknasyon.desk360.model.TicketResponce
import com.teknasyon.desk360.view.adapter.TicketListAdapter
import com.teknasyon.desk360.viewmodel.TicketListViewModel
import kotlinx.android.synthetic.main.fragment_ticket_list.*
import java.util.*

/**
 * Created by seyfullah on 24,May,2019
 *
 */

class TicketListFragment : Fragment(), TicketListAdapter.TicketOnClickListener {
    override fun selectTicket(item: TicketResponce, position: Int) {
        view?.let {
            Navigation.findNavController(it).navigate(R.id.action_ticketListFragment_to_ticketDetailFragment, null)
        }
    }

    var ticketAdapter: TicketListAdapter? = null
    var observer = Observer<ArrayList<TicketResponce>>
    {
        if (it != null) {
            ticketAdapter = TicketListAdapter(context, it)
            ticket_list.layoutManager = LinearLayoutManager(context)
            ticket_list.adapter = ticketAdapter
            ticketAdapter?.clickItem = this
        }
    }

    private var viewModel: TicketListViewModel? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_ticket_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = TicketListViewModel()
        viewModel?.ticketList?.observe(this, observer)
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel?.ticketList?.removeObserver(observer)
    }

    companion object {
        fun newInstance(): TicketListFragment {
            val args = Bundle()
            val fragment = TicketListFragment()
            fragment.arguments = args
            return fragment
        }
    }
}
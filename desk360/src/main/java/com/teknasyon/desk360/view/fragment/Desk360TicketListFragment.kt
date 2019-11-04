package com.teknasyon.desk360.view.fragment

import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.teknasyon.desk360.R
import com.teknasyon.desk360.databinding.Desk360FragmentTicketListBinding
import com.teknasyon.desk360.helper.RxBus
import com.teknasyon.desk360.model.Desk360TicketResponse
import com.teknasyon.desk360.view.activity.Desk360BaseActivity
import com.teknasyon.desk360.view.adapter.Desk360TicketListAdapter
import com.teknasyon.desk360.viewmodel.TicketListViewModel


/**
 * Created by seyfullah on 24,May,2019
 *
 */

open class Desk360TicketListFragment : Fragment(), Desk360TicketListAdapter.TicketOnClickListener {

    private var ticketAdapter: Desk360TicketListAdapter? = null
    private var observer = Observer<ArrayList<Desk360TicketResponse>> {
        binding?.loadingProgressMain?.visibility = View.VISIBLE
        it?.let {
            if (it.size != 0) {
                ticketAdapter = Desk360TicketListAdapter(context, it)
                binding?.ticketList?.layoutManager = LinearLayoutManager(context)
                binding?.ticketList?.adapter = ticketAdapter
                ticketAdapter?.clickItem = this
                binding?.emptyListLayout?.visibility = View.INVISIBLE
                binding?.fillListLayout?.visibility = View.VISIBLE
                RxBus.publish("ticketListIsNotEmpty")
            } else {
                binding?.emptyListLayout?.visibility = View.VISIBLE
                binding?.fillListLayout?.visibility = View.INVISIBLE
                (activity as Desk360BaseActivity).title = "Bize Ulaşın"
                RxBus.publish("ticketListIsEmpty")
            }
        }
        binding?.loadingProgressMain?.visibility = View.INVISIBLE
    }

    var binding: Desk360FragmentTicketListBinding? = null

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

    private var viewModel: TicketListViewModel? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.desk360_fragment_ticket_list,
            container,
            false
        )
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = TicketListViewModel()
        binding?.loadingProgressMain?.visibility = View.VISIBLE
        viewModel?.ticketList?.observe(this, observer)
        binding?.emptysAddNewTicketButton?.setOnClickListener {
            Navigation
                .findNavController(it)
                .navigate(R.id.action_ticketListFragment_to_addNewTicketFragment, null)
        }
        binding?.txtBottomFooterMain?.movementMethod = ScrollingMovementMethod()

    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel?.ticketList?.removeObserver(observer)
    }

    override fun onResume() {
        super.onResume()
        (activity as Desk360BaseActivity).userRegistered = true
    }
}
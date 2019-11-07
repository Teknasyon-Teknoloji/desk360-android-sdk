package com.teknasyon.desk360.view.fragment

import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import com.teknasyon.desk360.R
import com.teknasyon.desk360.databinding.Desk360FragmentTicketListBinding
import com.teknasyon.desk360.helper.RxBus
import com.teknasyon.desk360.model.Desk360TicketResponse
import com.teknasyon.desk360.view.activity.Desk360BaseActivity
import com.teknasyon.desk360.view.adapter.Desk360TicketPagerAdapter
import com.teknasyon.desk360.viewmodel.TicketListViewModel




/**
 * Created by seyfullah on 24,May,2019
 *
 */

open class Desk360TicketListFragment : Fragment() {

    private lateinit var ticketListPagerAdapter: Desk360TicketPagerAdapter
    private var currentTicketSize: Int = 0
    private var binding: Desk360FragmentTicketListBinding? = null
    private var viewModel: TicketListViewModel? = null

    private var observer = Observer<ArrayList<Desk360TicketResponse>> {
        binding?.loadingProgressMain?.visibility = View.VISIBLE
        it?.let {
            if (it.size != 0) {
                getCurrentAndPastTicketsize(it)
                binding?.emptyListLayout?.visibility = View.INVISIBLE
                binding?.fillListLayout?.visibility = View.VISIBLE
                binding?.ticketsTabs?.setupWithViewPager(binding?.viewPager)
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
        viewModel = ViewModelProviders.of(activity!!).get(TicketListViewModel::class.java)
        binding?.loadingProgressMain?.visibility = View.VISIBLE
        viewModel?.ticketList?.observe(activity!!, observer)
        viewModel?.register()
        binding?.emptysAddNewTicketButton?.setOnClickListener {
            Navigation
                .findNavController(it)
                .navigate(R.id.action_ticketListFragment_to_addNewTicketFragment, null)
        }
        ticketListPagerAdapter = Desk360TicketPagerAdapter(childFragmentManager)
        binding!!.viewPager.adapter = ticketListPagerAdapter
        binding?.txtBottomFooterMain?.movementMethod = ScrollingMovementMethod()


    }


    private fun getCurrentAndPastTicketsize(it: ArrayList<Desk360TicketResponse>) {
        for (i in 0 until it.size) {
            if (it[i].status == "unread") {
                currentTicketSize++
            }
        }
        setTicketSize()
        if(currentTicketSize==0) binding!!.textTicketsCurrentCount.visibility=View.INVISIBLE
        else binding!!.textTicketsCurrentCount.visibility=View.VISIBLE

    }

    private fun setTicketSize(){
        if(currentTicketSize>99){
            binding!!.textTicketsCurrentCount.text= "$currentTicketSize+"
        }else{
            binding!!.textTicketsCurrentCount.text= "$currentTicketSize"
        }

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
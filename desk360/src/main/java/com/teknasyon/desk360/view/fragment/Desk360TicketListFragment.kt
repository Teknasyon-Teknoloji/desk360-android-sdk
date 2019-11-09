package com.teknasyon.desk360.view.fragment

import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.navigation.Navigation
import com.teknasyon.desk360.R
import com.teknasyon.desk360.databinding.Desk360FragmentTicketListBinding
import com.teknasyon.desk360.helper.RxBus
import com.teknasyon.desk360.model.Desk360TicketResponse
import com.teknasyon.desk360.view.activity.Desk360BaseActivity
import com.teknasyon.desk360.viewmodel.TicketListViewModel


open class Desk360TicketListFragment : Fragment() {

    private lateinit var ticketListPagerAdapter: Desk360TicketPagerAdapter
    private var currentTicketSize: Int = 0
    private var binding: Desk360FragmentTicketListBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (binding == null) {
            binding = Desk360FragmentTicketListBinding.inflate(
                inflater, container, false
            )
        } else {
            container?.removeAllViews()
        }

        binding!!.ticketsTabs?.setupWithViewPager(binding?.viewPagerContainer)
        ticketListPagerAdapter = Desk360TicketPagerAdapter(childFragmentManager)
        binding!!.viewPagerContainer.adapter = ticketListPagerAdapter
        binding!!.txtBottomFooterMain?.movementMethod = ScrollingMovementMethod()


        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.emptysAddNewTicketButton?.setOnClickListener {
            Navigation
                .findNavController(binding!!.root)
                .navigate(R.id.action_ticketListFragment_to_addNewTicketFragment)
        }
    }

    override fun onResume() {
        super.onResume()
        (activity as Desk360BaseActivity).userRegistered = true

    }

    private fun setViewFillLayout(it: ArrayList<Desk360TicketResponse>) {
        getCurrentAndPastTicketsize(it)
        binding!!.emptyListLayout?.visibility = View.INVISIBLE
        binding!!.fillListLayout?.visibility = View.VISIBLE
        RxBus.publish("ticketListIsNotEmpty")
    }

    private fun setViewEmptyLayout() {
        binding!!.emptyListLayout?.visibility = View.VISIBLE
        binding!!.fillListLayout?.visibility = View.INVISIBLE
        (activity as Desk360BaseActivity).title = "Bize Ulaşın"
        RxBus.publish("ticketListIsEmpty")
    }

    private fun getCurrentAndPastTicketsize(it: ArrayList<Desk360TicketResponse>) {
        for (i in 0 until it.size) {
            if (it[i].status == "unread") {
                currentTicketSize++
            }
        }
        setTicketSize()
        if (currentTicketSize == 0) binding!!.textTicketsCurrentCount.visibility = View.INVISIBLE
        else binding!!.textTicketsCurrentCount.visibility = View.VISIBLE
    }

    private fun setTicketSize() {
        if (currentTicketSize > 99) {
            binding!!.textTicketsCurrentCount.text = "$99+"
        } else {
            binding!!.textTicketsCurrentCount.text = "$currentTicketSize"
        }
    }


    class Desk360TicketPagerAdapter(fm: FragmentManager) :
        FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
        override fun getCount(): Int {
            return 2
        }

        override fun getItem(i: Int): Fragment {
            return when (i) {
                0 -> Desk360CurrentTicketFragment.newInstance()
                1 -> Desk360PastTicketListFragment.newInstance()
                else -> Desk360CurrentTicketFragment.newInstance()
            }
        }

        override fun getPageTitle(position: Int): CharSequence? {
            return when (position) {
                0 -> "Current"
                else -> "Past"
            }
        }
    }

}

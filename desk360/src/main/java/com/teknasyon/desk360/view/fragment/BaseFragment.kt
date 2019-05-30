package com.teknasyon.desk360.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.teknasyon.desk360.R

/**
 * Created by seyfullah on 25,May,2019
 *
 */
open class BaseFragment : Fragment() {
    //    var ticketDetailAdapter: TicketListAdapter? = null
//    private var observer = Observer<ArrayList<TicketReq>> {
//        if (it != null) {
//            ticketDetailAdapter = TicketListAdapter(context, it)
//            ticket_list.layoutManager = LinearLayoutManager(context)
//            ticket_list.adapter = ticketDetailAdapter
//            ticketDetailAdapter?.clickItem = this
//        }
//    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    companion object {
        fun newInstance(): BaseFragment {
            val args = Bundle()
            val fragment = BaseFragment()
            fragment.arguments = args
            return fragment
        }
    }
}
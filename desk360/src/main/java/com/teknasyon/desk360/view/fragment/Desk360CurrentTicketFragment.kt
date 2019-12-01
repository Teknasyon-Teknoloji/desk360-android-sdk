package com.teknasyon.desk360.view.fragment

import android.content.Context
import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Bundle
import android.util.DisplayMetrics
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
import com.teknasyon.desk360.model.Desk360TicketResponse
import com.teknasyon.desk360.view.activity.Desk360BaseActivity
import com.teknasyon.desk360.view.adapter.Desk360TicketListAdapter
import com.teknasyon.desk360.viewmodel.TicketListViewModel


class Desk360CurrentTicketFragment : Fragment(), Desk360TicketListAdapter.TicketOnClickListener {

    private var ticketAdapter: Desk360TicketListAdapter? = null
    private var tickets: ArrayList<Desk360TicketResponse> = arrayListOf()
    private lateinit var binding: FragmentCurrentTicketListBinding
    private var viewModel: TicketListViewModel? = null


    override fun selectTicket(item: Desk360TicketResponse, position: Int) {
        view?.let {
            val bundle = Bundle()
            item.id?.let { it1 -> bundle.putInt("ticket_id", it1) }
            bundle.putString("ticket_status", item.status.toString())

            (activity as Desk360BaseActivity).userRegistered = false

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
        FragmentCurrentTicketListBinding.inflate(
            inflater,
            container,
            true
        ).also {
            binding = it
            binding.lifecycleOwner = viewLifecycleOwner
            return it.root
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ticketAdapter = Desk360TicketListAdapter(context, tickets)
        binding.currentTicketList?.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.currentTicketList?.adapter = ticketAdapter
        ticketAdapter?.clickItem = this
        viewModel = ViewModelProviders.of(activity!!).get(TicketListViewModel::class.java)
        Desk360CustomStyle.setStyle(Desk360Constants.currentType?.data?.first_screen?.button_style_id,binding.openMessageformEmptyCurrentList,context!!)
        Desk360CustomStyle.setFontWeight(binding.ticketListEmptyButtonText,context,Desk360Constants.currentType?.data?.first_screen?.button_text_font_weight)


        binding.imageEmptyCurrent.requestLayout()
        binding.viewModelList=viewModel
        binding.imageEmptyCurrent.setImageResource(R.drawable.no_expired_ticket_list_icon)
        binding.imageEmptyCurrent.setColorFilter(
            Color.parseColor(Desk360Constants.currentType?.data?.ticket_list_screen?.ticket_list_empty_icon_color),
            PorterDuff.Mode.SRC_ATOP
        )

        viewModel?.ticketList?.observe(viewLifecycleOwner, Observer {
            it?.let {
                tickets.clear()
                tickets.addAll(it)
                ticketAdapter!!.notifyDataSetChanged()
                setViews()
            }
        })

        binding.ticketListEmptyButtonIcon.setImageResource(R.drawable.zarf)
        binding.ticketListEmptyButtonIcon.setColorFilter(
            Color.parseColor(Desk360Constants.currentType?.data?.ticket_list_screen?.ticket_list_empty_text_color),
            PorterDuff.Mode.SRC_ATOP
        )

        binding.openMessageformEmptyCurrentList.setOnClickListener{
            Navigation
                .findNavController(it)
                .navigate(R.id.action_ticketListFragment_to_preNewTicketFragment, null)
        }
    }

    private fun convertDpToPixel(dp: Float, context: Context): Float {
        return dp * (context.resources.displayMetrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)
    }
    private fun setViews() {
        if(tickets.isEmpty()){
            binding.currentTicketList.visibility=View.INVISIBLE
            binding.emptyLayoutCurrent.visibility=View.VISIBLE
        }else{
            binding.currentTicketList.visibility=View.VISIBLE
            binding.emptyLayoutCurrent.visibility=View.INVISIBLE
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
        viewModel?.register()
        ticketAdapter!!.notifyDataSetChanged()
    }
}
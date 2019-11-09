package com.teknasyon.desk360.view.fragment

import android.content.Context
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.teknasyon.desk360.R
import com.teknasyon.desk360.databinding.Desk360FragmentTicketDetailBinding
import com.teknasyon.desk360.helper.RxBus
import com.teknasyon.desk360.model.Desk360Message
import com.teknasyon.desk360.view.adapter.Desk360TicketDetailListAdapter
import com.teknasyon.desk360.viewmodel.TicketDetailViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers


/**
 * Created by seyfullah on 25,May,2019
 *
 */
open class Desk360TicketDetailFragment : Fragment() {
    private var binding: Desk360FragmentTicketDetailBinding? = null
    private var ticketDetailAdapter: Desk360TicketDetailListAdapter? = null

    private var ticketId: Int? = null
    private var ticketStatus: String? = null

    private var backButtonAction: Disposable? = null

    private var observer = Observer<ArrayList<Desk360Message>> {
        binding?.loadingProgress?.visibility = View.INVISIBLE

        if (it != null) {
            ticketDetailAdapter = Desk360TicketDetailListAdapter(it)
            val layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            binding?.messageDetailRecyclerView?.apply {
                this.layoutManager = layoutManager
                adapter = ticketDetailAdapter
                scrollToPosition(it.size - 1)
            }
        }
    }

    private var addMessageObserver = Observer<Desk360Message> {
        binding?.loadingProgress?.visibility = View.INVISIBLE

        if (it != null) {
            viewModel?.ticketDetailList?.value?.add(it)
            ticketDetailAdapter?.notifyDataSetChanged()
            viewModel?.ticketDetailList?.value?.size?.minus(1)
                ?.let { it1 -> binding?.messageDetailRecyclerView?.scrollToPosition(it1) }
            binding?.messageEditText?.setText("")
        }
    }

    private var viewModel: TicketDetailViewModel? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.desk360_fragment_ticket_detail, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.loadingProgress?.visibility = View.VISIBLE
        viewModel = ticketId?.let { TicketDetailViewModel(it) }
        viewModel?.ticketDetailList?.observe(this, observer)
        viewModel?.addMessageItem?.observe(this, addMessageObserver)
        binding?.addNewMessageButton?.setOnClickListener {
            binding?.messageEditText?.text?.trim()?.apply {
                if (isNotEmpty() && toString().isNotEmpty()) {

                    ticketId?.let { it1 ->
                        viewModel?.addMessage(
                            it1,
                            binding?.messageEditText?.text.toString()
                        )
                    }
                    binding?.loadingProgress?.visibility = View.VISIBLE
                }
            }
        }

        binding?.addNewTicketButton?.setOnClickListener {
            Navigation
                .findNavController(it)
                .navigate(R.id.action_ticketDetailFragment_to_addNewTicketFragment, null)
        }
        expireControl()
    }

    private fun expireControl() {
        if (ticketStatus == "expired") {
            binding?.layoutSendNewMessageNormal?.visibility = View.GONE
            binding?.addNewTicketButton?.visibility = View.VISIBLE
            backButtonAction =
                RxBus.listen(String::class.java).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({ iti ->
                        when (iti) {
                            "backButtonActionKey" -> {
                                view?.let { it1 ->
                                    Navigation.findNavController(it1).popBackStack(
                                        R.id.action_global_ticketDetailFragment,
                                        true
                                    )

                                }
                            }
                        }
                    }, { t ->
                        Log.d("Test", "$t.")
                    })
        } else {
            binding?.layoutSendNewMessageNormal?.visibility = View.VISIBLE
            binding?.addNewTicketButton?.visibility = View.GONE
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            ticketId = arguments!!.getInt("ticket_id")
            ticketStatus = arguments!!.getString("ticket_status")
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel?.ticketDetailList?.removeObserver(observer)
        viewModel?.addMessageItem?.removeObserver(addMessageObserver)
        if (backButtonAction?.isDisposed == false)
            backButtonAction?.dispose()
        hideSoftKeyboard()
    }

    private fun hideSoftKeyboard() {
        activity?.let {
            val view = activity!!.currentFocus
            if (view != null) {
                val imm =
                    activity!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(view.windowToken, 0)
            }
        }
    }

}
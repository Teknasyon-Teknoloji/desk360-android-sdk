package com.teknasyon.desk360.view.fragment

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.navigation.Navigation
import com.teknasyon.desk360.R
import com.teknasyon.desk360.databinding.Desk360FragmentTicketListBinding
import com.teknasyon.desk360.helper.Desk360Constants
import com.teknasyon.desk360.helper.RxBus
import com.teknasyon.desk360.view.activity.Desk360BaseActivity
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.desk360_fragment_ticket_list.*
import kotlinx.android.synthetic.main.desk360_ticket_list_item.view.*


open class Desk360TicketListFragment : Fragment() {

    private lateinit var ticketListPagerAdapter: Desk360TicketPagerAdapter
    private var binding: Desk360FragmentTicketListBinding? = null
    private var disposable: Disposable? = null

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

    @SuppressLint("CheckResult")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.emptysAddNewTicketButton?.setOnClickListener {
            Navigation
                .findNavController(binding!!.root)
                .navigate(R.id.action_ticketListFragment_to_addNewTicketFragment)
        }

        binding!!.firstScreenButtonIcon.setImageResource(R.drawable.zarf)
        binding!!.firstScreenButtonIcon.setColorFilter(
            Color.parseColor(Desk360Constants.currentType?.data?.first_screen?.button_text_color),
            PorterDuff.Mode.SRC_ATOP
        )

        disposable = RxBus.listen(HashMap<String, Int>()::class.java).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({

                if (it.keys.contains("sizeTicketList")) {
                    it["sizeTicketList"]?.let { sizeList ->
                        if (sizeList > 0) {
                            setViewFillLayout()
                        } else {
                            setViewEmptyLayout()
                        }
                    }
                }
                if (it.keys.contains("unReadSizeTicketList")) {
                    it["unReadSizeTicketList"]?.let { sizeUnread ->
                        setUnreadTicketSize(sizeUnread)
                    }
                }
            }, { t ->
                Log.d("dataSize", "$t")
            })
    }

    override fun onResume() {
        super.onResume()
        viewPagerContainer.currentItem = 0
        (activity as Desk360BaseActivity).userRegistered = true
    }

    override fun onDestroy() {
        super.onDestroy()
        if (disposable?.isDisposed == false)
            disposable?.dispose()
    }

    private fun setViewFillLayout() {
        binding!!.emptyListLayout?.visibility = View.INVISIBLE
        binding!!.fillListLayout?.visibility = View.VISIBLE
        RxBus.publish("ticketListIsNotEmpty")
    }

    private fun setViewEmptyLayout() {
        binding!!.emptyListLayout?.visibility = View.VISIBLE
        binding!!.fillListLayout?.visibility = View.INVISIBLE
        (activity as Desk360BaseActivity).title = getString(R.string.CONTACT_US_TITLE)
        RxBus.publish("ticketListIsEmpty")
    }

    private fun setUnreadTicketSize(sizeUnread: Int) {
        if (sizeUnread == 0) binding!!.textTicketsCurrentCount.visibility = View.INVISIBLE
        else binding!!.textTicketsCurrentCount.visibility = View.VISIBLE

        if (sizeUnread > 99) {
            binding!!.textTicketsCurrentCount.text = "99+"
        } else {
            binding!!.textTicketsCurrentCount.text = "$sizeUnread"
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
                0 -> Desk360Constants.currentType?.data?.ticket_list_screen?.tab_current_text
                else -> Desk360Constants.currentType?.data?.ticket_list_screen?.tab_past_text
            }
        }
    }

}

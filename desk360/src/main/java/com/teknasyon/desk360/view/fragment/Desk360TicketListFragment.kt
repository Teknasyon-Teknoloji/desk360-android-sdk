package com.teknasyon.desk360.view.fragment

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.Typeface
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TableLayout
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.get
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.navigation.Navigation
import com.google.android.material.tabs.TabLayout
import com.teknasyon.desk360.R
import com.teknasyon.desk360.databinding.Desk360FragmentTicketListBinding
import com.teknasyon.desk360.helper.Desk360CustomStyle
import com.teknasyon.desk360.helper.Desk360Constants
import com.teknasyon.desk360.helper.RxBus
import com.teknasyon.desk360.view.activity.Desk360BaseActivity
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.desk360_fragment_ticket_list.*


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
        binding!!.txtBottomFooterMainTicketList?.movementMethod = ScrollingMovementMethod()

        return binding?.root
    }

    @SuppressLint("CheckResult")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.emptysAddNewTicketButtonTicketList?.setOnClickListener {
            Navigation
                .findNavController(binding!!.root)
                .navigate(R.id.action_ticketListFragment_to_addNewTicketFragment)
        }


        for (i in 0 until binding?.ticketsTabs?.tabCount!!) {

            val tabItem = LayoutInflater.from(context).inflate(
                R.layout.desk360_toolbar_title_text,
                null
            ) as TextView

            tabItem.textSize =
                Desk360Constants.currentType?.data?.ticket_list_screen?.tab_text_font_size?.toFloat()!!
            Desk360CustomStyle.setFontWeight(
                tabItem,
                context,
                Desk360Constants.currentType?.data?.ticket_list_screen?.tab_text_font_weight
            )

            if (i == 0) {
                tabItem.setTextColor(Color.parseColor(Desk360Constants.currentType?.data?.ticket_list_screen?.tab_text_active_color))
            }

            binding?.ticketsTabs?.getTabAt(i)?.customView = tabItem
        }

        binding?.ticketsTabs?.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabReselected(tab: TabLayout.Tab?) {
                val customView = tab?.customView
                (customView?.findViewById(android.R.id.text1) as? TextView)?.setTextColor(
                    Color.parseColor(
                        Desk360Constants.currentType?.data?.ticket_list_screen?.tab_text_active_color
                    )
                )
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                val customView = tab?.customView
                (customView?.findViewById(android.R.id.text1) as? TextView)?.setTextColor(
                    Color.parseColor(
                        Desk360Constants.currentType?.data?.ticket_list_screen?.tab_text_color
                    )
                )
            }

            override fun onTabSelected(tab: TabLayout.Tab?) {
                if (tab?.customView != null) {
                    val customView = tab.customView
                    (customView?.findViewById(android.R.id.text1) as? TextView)?.setTextColor(
                        Color.parseColor(
                            Desk360Constants.currentType?.data?.ticket_list_screen?.tab_text_active_color
                        )
                    )
                }

            }

        })


        Desk360CustomStyle.setStyle(
            Desk360Constants.currentType?.data?.first_screen?.button_style_id,
            binding!!.emptysAddNewTicketButtonTicketList,
            context!!
        )
        Desk360CustomStyle.setFontWeight(
            binding!!.emptyListLayoutTicketListSubTitle,
            context,
            Desk360Constants.currentType?.data?.first_screen?.sub_title_font_weight
        )
        Desk360CustomStyle.setFontWeight(
            binding!!.emptyListLayoutTicketListDesc,
            context,
            Desk360Constants.currentType?.data?.first_screen?.description_font_weight
        )
        Desk360CustomStyle.setFontWeight(
            binding!!.txtOpenMessageFormTicketList,
            context,
            Desk360Constants.currentType?.data?.first_screen?.button_text_font_weight
        )
        Desk360CustomStyle.setFontWeight(
            binding!!.txtBottomFooterMainTicketList,
            context,
            Desk360Constants.currentType?.data?.general_settings?.bottom_note_font_weight
        )

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
        binding!!.emptyListLayoutTicketList?.visibility = View.INVISIBLE
        binding!!.fillListLayout?.visibility = View.VISIBLE
        RxBus.publish("ticketListIsNotEmpty")
    }

    private fun setViewEmptyLayout() {
        binding!!.emptyListLayoutTicketList?.visibility = View.VISIBLE
        binding!!.fillListLayout?.visibility = View.INVISIBLE
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

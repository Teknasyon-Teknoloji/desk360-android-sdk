package com.teknasyon.desk360.view.fragment

import android.content.Context
import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Bundle
import android.os.Handler
import android.text.method.ScrollingMovementMethod
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.google.android.material.tabs.TabLayout
import com.teknasyon.desk360.R
import com.teknasyon.desk360.databinding.Desk360FragmentTicketListBinding
import com.teknasyon.desk360.helper.Desk360Constants
import com.teknasyon.desk360.helper.Desk360CustomStyle
import com.teknasyon.desk360.helper.PreferencesManager
import com.teknasyon.desk360.helper.RxBus
import com.teknasyon.desk360.model.CacheTicket
import com.teknasyon.desk360.model.Desk360TicketResponse
import com.teknasyon.desk360.view.activity.Desk360BaseActivity
import com.teknasyon.desk360.view.adapter.Desk360TicketPagerAdapter
import com.teknasyon.desk360.viewmodel.GetTypesViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.desk360_fragment_main.*
import kotlinx.android.synthetic.main.desk360_fragment_ticket_list.*

open class Desk360TicketListFragment : Fragment() {

    private lateinit var ticketListPagerAdapter: Desk360TicketPagerAdapter
    private lateinit var desk360BaseActivity: Desk360BaseActivity
    private var binding: Desk360FragmentTicketListBinding? = null
    private var disposable: Disposable? = null

    private var isTypesFetched = false

    override fun onAttach(context: Context) {
        super.onAttach(context)
        desk360BaseActivity = context as Desk360BaseActivity
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        if (binding == null) {
            binding = Desk360FragmentTicketListBinding.inflate(
                inflater, container, false
            )
        } else {

        }

        binding!!.ticketsTabs?.setupWithViewPager(binding?.viewPagerContainer)
        ticketListPagerAdapter = Desk360TicketPagerAdapter(childFragmentManager)
        binding!!.viewPagerContainer.adapter = ticketListPagerAdapter
        binding!!.txtBottomFooterMainTicketList?.movementMethod = ScrollingMovementMethod()

        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        try {
            desk360BaseActivity.changeMainUI()

            val call = GetTypesViewModel()
            call.getTypes { isFetched ->

                if (isFetched) {
                    isTypesFetched = true
                }
            }

            binding?.emptysAddNewTicketButtonTicketList?.setOnClickListener {

                desk360BaseActivity.addBtnClicked = true
                Handler().removeCallbacksAndMessages(null)
                Handler().postDelayed({ desk360BaseActivity.addBtnClicked = false }, 800)

                Navigation
                    .findNavController(binding!!.root)
                    .navigate(R.id.action_ticketListFragment_to_addNewTicketFragment)
            }

            desk360BaseActivity.contactUsMainBottomBar.visibility = View.VISIBLE

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
                } else {
                    tabItem.setTextColor(Color.parseColor(Desk360Constants.currentType?.data?.ticket_list_screen?.tab_text_color))
                }

                binding?.ticketsTabs?.getTabAt(i)?.customView = tabItem
            }

            binding?.ticketsTabs?.addOnTabSelectedListener(object :
                TabLayout.OnTabSelectedListener {
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


            if (getCacheTickets().isNotEmpty()) {

                setViewFillLayout()

            } else {

                if (!desk360BaseActivity.isMainLoadingShown) {
                    desk360BaseActivity.isMainLoadingShown = true
                    binding!!.loadingCurrentTicket?.visibility = View.VISIBLE
                }

                listenCurrentTicketList()
            }
        }catch (e:Exception){
            Log.e("exception",e.toString())
        }
    }

    private fun listenCurrentTicketList() {

        disposable =
            RxBus.listen(HashMap<String, Int>()::class.java).subscribeOn(Schedulers.io())
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

    private fun getCacheTickets(): ArrayList<Desk360TicketResponse> {

        val preferencesManager = PreferencesManager()

        return try {
            preferencesManager.readObject(
                "tickets",
                CacheTicket::class.java
            ) as ArrayList<Desk360TicketResponse>

        } catch (e: Exception) {
            ArrayList()
        }
    }

    private fun setViewFillLayout() {

        desk360BaseActivity.toolbar_title.text =
            Desk360Constants.currentType?.data?.ticket_list_screen?.title

        binding!!.fillListLayout?.visibility = View.VISIBLE
        binding!!.fragmentTicketListRoot?.visibility = View.VISIBLE
        binding!!.loadingCurrentTicket?.visibility = View.INVISIBLE
        binding!!.emptyListLayoutTicketList?.visibility = View.INVISIBLE
    }

    private fun setViewEmptyLayout() {

        desk360BaseActivity.toolbar_title.text =
            Desk360Constants.currentType?.data?.first_screen?.title

        binding!!.fillListLayout?.visibility = View.INVISIBLE
        binding!!.fragmentTicketListRoot?.visibility = View.VISIBLE
        binding!!.loadingCurrentTicket?.visibility = View.INVISIBLE
        binding!!.emptyListLayoutTicketList?.visibility = View.VISIBLE

        //Main BackGround Color
        binding!!.txtBottomFooterMainTicketList.setBackgroundColor(Color.parseColor(Desk360Constants.currentType?.data?.general_settings?.main_background_color))

        //Sub Title Text
        binding!!.emptyListLayoutTicketListSubTitle.setTextColor(Color.parseColor(Desk360Constants.currentType?.data?.first_screen?.sub_title_color))
        binding!!.emptyListLayoutTicketListSubTitle.text =
            Desk360Constants.currentType?.data?.first_screen?.sub_title
        binding!!.emptyListLayoutTicketListSubTitle.textSize =
            Desk360Constants.currentType?.data?.first_screen?.sub_title_font_size!!.toFloat()

        //Description Title Text
        binding!!.emptyListLayoutTicketListDesc.setTextColor(Color.parseColor(Desk360Constants.currentType?.data?.first_screen?.description_color))
        binding!!.emptyListLayoutTicketListDesc.text =
            Desk360Constants.currentType?.data?.first_screen?.description
        binding!!.emptyListLayoutTicketListDesc.textSize =
            Desk360Constants.currentType?.data?.first_screen?.description_font_size!!.toFloat()

        binding!!.txtOpenMessageFormTicketList.setTextColor(Color.parseColor(Desk360Constants.currentType?.data?.first_screen?.button_text_color))
        binding!!.txtOpenMessageFormTicketList.textSize =
            Desk360Constants.currentType?.data?.first_screen?.button_text_font_size!!.toFloat()
        binding!!.txtOpenMessageFormTicketList.text = Desk360CustomStyle.setButtonText(
            Desk360Constants.currentType?.data?.first_screen?.button_text!!.length,
            Desk360Constants.currentType?.data?.first_screen?.button_text
        )

        if (Desk360Constants.currentType?.data?.first_screen?.button_icon_is_hidden == true) {
            binding!!.firstScreenButtonIcon.visibility = View.VISIBLE
        } else {
            binding!!.firstScreenButtonIcon.visibility = View.INVISIBLE
        }

        binding!!.txtBottomFooterMainTicketList.setTextColor(Color.parseColor(Desk360Constants.currentType?.data?.general_settings?.bottom_note_color))
        binding!!.txtBottomFooterMainTicketList.textSize =
            Desk360Constants.currentType?.data?.general_settings?.bottom_note_font_size!!.toFloat()

        binding!!.txtBottomFooterMainTicketList.text =
            Desk360Constants.currentType?.data?.first_screen?.bottom_note_text
        if (!Desk360Constants.currentType?.data?.first_screen?.bottom_note_is_hidden!!) {
            binding!!.txtBottomFooterMainTicketList.visibility = View.INVISIBLE
        } else {
            binding!!.txtBottomFooterMainTicketList.visibility = View.VISIBLE
        }
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

    override fun onResume() {
        super.onResume()
        viewPagerContainer.currentItem = 0
    }

    override fun onDestroy() {
        super.onDestroy()
        if (disposable?.isDisposed == false)
            disposable?.dispose()
    }

}

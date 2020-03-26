package com.teknasyon.desk360.view.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.teknasyon.desk360.helper.Desk360Constants
import com.teknasyon.desk360.view.fragment.Desk360CurrentTicketFragment
import com.teknasyon.desk360.view.fragment.Desk360PastTicketListFragment

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
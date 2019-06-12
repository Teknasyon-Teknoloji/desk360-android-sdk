package com.teknasyon.desk360.view.activity

import android.content.Context
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.NavController
import androidx.navigation.Navigation.findNavController
import androidx.navigation.fragment.FragmentNavigator
import androidx.navigation.ui.NavigationUI.setupActionBarWithNavController
import com.teknasyon.desk360.R
import com.teknasyon.desk360.databinding.FragmentMainBinding
import com.teknasyon.desk360.helper.Desk360Constants
import kotlinx.android.synthetic.main.fragment_main.*


class Desk360BaseActivity : AppCompatActivity(), LifecycleOwner {

    private var localMenu: Menu? = null
    var userRegistered = true
    private var navController: NavController? = null

    private var binding: FragmentMainBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.fragment_main)
        setSupportActionBar(findViewById(R.id.toolbar))

        navController =
                findNavController(this, R.id.my_nav_host_fragment)
        navController?.addOnDestinationChangedListener { _, _, _ ->

            userRegistered = false
            val currentNav = (navController?.currentDestination as FragmentNavigator.Destination).className

            if (currentNav == "com.teknasyon.desk360.view.fragment.TicketListFragment") {
                userRegistered = true
            }

            toolbar.title = navController?.currentDestination?.label
            localMenu?.let { onPrepareOptionsMenu(it) }
        }
        setupActionBarWithNavController(this, navController!!)
    }

    override fun onSupportNavigateUp(): Boolean {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(window.decorView.rootView.windowToken, 0)

        return findNavController(this, R.id.my_nav_host_fragment).navigateUp()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        localMenu = menu
        menuInflater.inflate(R.menu.menu_main, menu)
        val register: MenuItem = menu.findItem(R.id.action_add_new_ticket)

        if (Desk360Constants.currentTheme == "light") {
            register.icon = resources.getDrawable(R.drawable.ic_add_light_theme_icon)
        } else {
            register.icon = resources.getDrawable(R.drawable.ic_add_dark_theme_icon)
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId

        if (id == R.id.action_add_new_ticket) {

            userRegistered = false

            findNavController(findViewById(R.id.my_nav_host_fragment)).navigate(R.id.action_ticketListFragment_to_addNewTicketFragment)
            return true
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onPrepareOptionsMenu(menu: Menu): Boolean {
        val register: MenuItem = menu.findItem(R.id.action_add_new_ticket)
        register.isVisible = userRegistered
        return true
    }
}

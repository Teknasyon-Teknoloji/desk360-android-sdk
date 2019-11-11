package com.teknasyon.desk360.view.activity

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.Navigation.findNavController
import androidx.navigation.fragment.FragmentNavigator
import com.teknasyon.desk360.R
import com.teknasyon.desk360.databinding.Desk360FragmentMainBinding
import com.teknasyon.desk360.helper.Desk360Constants
import com.teknasyon.desk360.helper.RxBus
import com.teknasyon.desk360.viewmodel.GetTypesViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.desk360_fragment_main.*

open class Desk360BaseActivity : AppCompatActivity(), LifecycleOwner {

    private var localMenu: Menu? = null
    private var vieeModelType:GetTypesViewModel?=null
    var userRegistered = true
    private var navController: NavController? = null
    private var disposable: Disposable? = null

    private var binding: Desk360FragmentMainBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = Desk360FragmentMainBinding.inflate(layoutInflater)
        setContentView(binding!!.root)
        setSupportActionBar(findViewById(R.id.toolbar))

        vieeModelType=ViewModelProviders.of(this).get(GetTypesViewModel::class.java)

        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
        }

        navController =
            findNavController(this, R.id.my_nav_host_fragment)

        navController?.addOnDestinationChangedListener { _, destination, _ ->

            userRegistered = false
            val currentNav =
                (navController?.currentDestination as FragmentNavigator.Destination).className

            if (currentNav == "com.teknasyon.desk360.view.fragment.Desk360TicketListFragment") {
                userRegistered = true
            }

            binding?.toolbarTitle?.text = navController?.currentDestination?.label
            localMenu?.let { onPrepareOptionsMenu(it) }

            if (destination.id == R.id.ticketListFragment || destination.id == R.id.preNewTicketFragment || destination.id==R.id.thanksFragment) {
                if (Desk360Constants.currentTheme in listOf(1, 2, 3, 5))
                    toolbar.navigationIcon = resources.getDrawable(R.drawable.close_button_desk)
                else
                    toolbar.navigationIcon =
                        resources.getDrawable(R.drawable.close_button_desk_white)

            } else {
                if (Desk360Constants.currentTheme in listOf(1, 2, 3, 5))
                    toolbar.navigationIcon = resources.getDrawable(R.drawable.back_btn_dark_theme)
                else
                    toolbar.navigationIcon = resources.getDrawable(R.drawable.back_btn_light_theme)
            }
        }
//        setupActionBarWithNavController(this, navController!!, appBarConfiguration)
    }

    override fun onSupportNavigateUp(): Boolean {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(window.decorView.rootView.windowToken, 0)
        return if (userRegistered) {
            super.onBackPressed()
            true
        } else
            findNavController(this, R.id.my_nav_host_fragment).navigateUp()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        localMenu = menu
        menuInflater.inflate(R.menu.menu_main, menu)
        val register: MenuItem = menu.findItem(R.id.action_add_new_ticket)
        disposable = RxBus.listen(String::class.java).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                when (it) {
                    "ticketListIsNotEmpty" -> {

                        register.isVisible = true

                        if (Desk360Constants.currentTheme in listOf(1, 2, 3, 5))
                            register.icon =
                                resources.getDrawable(R.drawable.add_new_message_icon_black)
                        else
                            register.icon =
                                resources.getDrawable(R.drawable.add_new_message_icon_white)
                    }

                    "ticketListIsEmpty" -> {
                        register.isVisible = false
                    }
                }
            }, { t ->
                Log.d("Test", "$t.")
            })

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId

        if (id == R.id.action_add_new_ticket) {
            userRegistered = false
            findNavController(findViewById(R.id.my_nav_host_fragment)).navigate(R.id.action_ticketListFragment_to_preNewTicketFragment)
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        if (userRegistered)
            super.onBackPressed()
        else
            onSupportNavigateUp()
    }

    override fun onPrepareOptionsMenu(menu: Menu): Boolean {
        val register: MenuItem = menu.findItem(R.id.action_add_new_ticket)
        register.isVisible = false
        return true
    }

    override fun onDestroy() {
        super.onDestroy()
        if (disposable?.isDisposed == false)
            disposable?.dispose()
    }
}

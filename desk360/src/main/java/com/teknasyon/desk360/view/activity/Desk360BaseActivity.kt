package com.teknasyon.desk360.view.activity

import android.content.Context
import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Bundle
import android.os.Handler
import android.view.Menu
import android.view.MenuItem
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.Navigation.findNavController
import androidx.navigation.fragment.FragmentNavigator
import com.teknasyon.desk360.R
import com.teknasyon.desk360.databinding.Desk360FragmentMainBinding
import com.teknasyon.desk360.helper.Desk360Constants
import com.teknasyon.desk360.helper.Desk360CustomStyle
import com.teknasyon.desk360.viewmodel.TicketListViewModel
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.desk360_fragment_main.*


open class Desk360BaseActivity : AppCompatActivity(), LifecycleOwner {

    private var localMenu: Menu? = null
    var ticketListSize = 0
    var currentScreenTicketList = true
    private var navController: NavController? = null
    private var disposable: Disposable? = null
    private var viewModel: TicketListViewModel? = null

    private var addBtnClicked = false

    private var binding: Desk360FragmentMainBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = Desk360FragmentMainBinding.inflate(layoutInflater)
        setContentView(binding!!.root)
        setSupportActionBar(findViewById(R.id.toolbar))
        supportActionBar?.setDisplayShowTitleEnabled(false)
        viewModel = ViewModelProviders.of(this).get(TicketListViewModel::class.java)

        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
        }

        viewModel?.ticketSize?.observe(this, Observer {
            ticketListSize = it
            notifyToolBar()
        })

        navController =
            findNavController(this, R.id.my_nav_host_fragment)

        navController?.addOnDestinationChangedListener { _, destination, _ ->

            val currentNav =
                (navController?.currentDestination as FragmentNavigator.Destination).className

            currentScreenTicketList =
                currentNav == "com.teknasyon.desk360.view.fragment.Desk360TicketListFragment"
            notifyToolBar()
            Desk360CustomStyle.setFontWeight(
                binding!!.toolbarTitle,
                this,
                Desk360Constants.currentType?.data?.general_settings?.header_text_font_weight
            )

            when (destination.id) {
                R.id.preNewTicketFragment -> {
                    setMainTitle(
                        Desk360Constants.currentType?.data?.create_pre_screen?.title,
                        binding?.toolbarTitle
                    )
                }
                R.id.thanksFragment -> {
                    setMainTitle(
                        Desk360Constants.currentType?.data?.ticket_success_screen?.title,
                        binding?.toolbarTitle
                    )
                }
                R.id.ticketDetailFragment -> {
                    setMainTitle(
                        Desk360Constants.currentType?.data?.ticket_detail_screen?.title,
                        binding?.toolbarTitle
                    )
                }
                R.id.addNewTicketFragment -> {
                    setMainTitle(
                        Desk360Constants.currentType?.data?.create_screen?.title,
                        binding?.toolbarTitle
                    )
                }

                else -> ""

            }

            localMenu?.let { onPrepareOptionsMenu(it) }

            if (destination.id == R.id.ticketListFragment || destination.id == R.id.preNewTicketFragment || destination.id == R.id.thanksFragment) {
                toolbar.navigationIcon = resources.getDrawable(R.drawable.close_button_desk)

            } else {
                toolbar.navigationIcon = resources.getDrawable(R.drawable.back_btn_dark_theme)
            }

            toolbar.navigationIcon?.setColorFilter(
                Color.parseColor(Desk360Constants.currentType?.data?.general_settings?.header_icon_color),
                PorterDuff.Mode.SRC_ATOP
            )

            Handler().postDelayed(
                {
                    addBtnClicked = false
                }, 600
            )


        }
//        setupActionBarWithNavController(this, navController!!, appBarConfiguration)
    }

    private fun notifyToolBar() {
        if (ticketListSize > 0) {
            setMainTitle(
                Desk360Constants.currentType?.data?.ticket_list_screen?.title,
                binding?.toolbarTitle
            )
        } else {
            setMainTitle(
                Desk360Constants.currentType?.data?.first_screen?.title,
                binding?.toolbarTitle
            )
        }

        localMenu?.let { onPrepareOptionsMenu(it) }
    }

    private fun setMainTitle(titleHead: String?, titleTextView: TextView?) {

        when {
            titleHead?.length!! < 29 -> titleTextView?.text = titleHead
            else -> titleTextView?.text = titleHead.substring(0, 18) + "..."
        }

    }

    override fun onSupportNavigateUp(): Boolean {
        if (addBtnClicked) {
            return false
        }
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(window.decorView.rootView.windowToken, 0)
        return if (currentScreenTicketList) {
            super.onBackPressed()
            true
        } else
            findNavController(this, R.id.my_nav_host_fragment).navigateUp()

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        localMenu = menu
        menuInflater.inflate(R.menu.menu_main, menu)
        val register: MenuItem = menu.findItem(R.id.action_add_new_ticket)

        register.isVisible = true
        register.isEnabled = true
        register.icon =
            resources.getDrawable(R.drawable.add_new_message_icon_black)

        register.icon?.setColorFilter(
            Color.parseColor(Desk360Constants.currentType?.data?.general_settings?.header_icon_color),
            PorterDuff.Mode.SRC_ATOP
        )

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId

        if (id == R.id.action_add_new_ticket) {
            addBtnClicked = true

            findNavController(findViewById(R.id.my_nav_host_fragment)).navigate(R.id.action_ticketListFragment_to_preNewTicketFragment)
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        if (currentScreenTicketList)
            super.onBackPressed()
        else
            onSupportNavigateUp()
    }

    override fun onPrepareOptionsMenu(menu: Menu): Boolean {
        val register: MenuItem = menu.findItem(R.id.action_add_new_ticket)
        register.isVisible = currentScreenTicketList && ticketListSize > 0
        return true
    }

    override fun onDestroy() {
        super.onDestroy()
        if (disposable?.isDisposed == false)
            disposable?.dispose()
    }

}

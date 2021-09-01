package com.teknasyon.desk360.view.activity

import android.app.ActivityManager
import android.content.Context
import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.NavController
import androidx.navigation.Navigation.findNavController
import androidx.navigation.fragment.FragmentNavigator
import com.teknasyon.desk360.R
import com.teknasyon.desk360.databinding.Desk360FragmentMainBinding
import com.teknasyon.desk360.helper.Desk360Constants
import com.teknasyon.desk360.helper.Desk360CustomStyle
import com.teknasyon.desk360.helper.binding
import com.teknasyon.desk360.model.Desk360TicketResponse
import com.teknasyon.desk360.view.fragment.Desk360TicketListFragmentDirections
import io.reactivex.disposables.Disposable

open class Desk360BaseActivity : AppCompatActivity(), LifecycleOwner {
    private var cacheTickets: ArrayList<Desk360TicketResponse>? = null
    private var navController: NavController? = null
    private var disposable: Disposable? = null
    private var localMenu: Menu? = null

    var notificationToken: String? = null
    var ticketId: String? = null
    private var appId: String? = null

    private var currentScreenTicketList = true
    var addBtnClicked = false
    var isMainLoadingShown = false
    var isTicketDetailFragment = false

    val binding: Desk360FragmentMainBinding by binding(R.layout.desk360_fragment_main)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        intent.extras?.let { bundle ->
            appId = bundle.getString(Desk360SplashActivity.EXTRA_APP_ID)
            ticketId = bundle.getString(Desk360SplashActivity.EXTRA_TARGET_ID)
            notificationToken = bundle.getString(Desk360SplashActivity.EXTRA_TOKEN)
        }

        setContentView(binding.root)
        setSupportActionBar(findViewById(R.id.toolbar))
        supportActionBar?.setDisplayShowTitleEnabled(false)

        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
        }

        navController = findNavController(this, R.id.my_nav_host_fragment)

        navController?.addOnDestinationChangedListener { _, destination, _ ->

            val currentNav =
                (navController?.currentDestination as FragmentNavigator.Destination).className

            currentScreenTicketList =
                currentNav == "com.teknasyon.desk360.view.fragment.Desk360TicketListFragment"


            cacheTickets?.let {
                notifyToolBar(cacheTickets!!)
            }

            Desk360CustomStyle.setFontWeight(
                binding.toolbarTitle,
                this,
                Desk360Constants.currentType?.data?.general_settings?.header_text_font_weight
            )

            when (destination.id) {

                R.id.preNewTicketFragment -> {
                    setMainTitle(
                        Desk360Constants.currentType?.data?.create_pre_screen?.title
                    )
                    isTicketDetailFragment = false
                }
                R.id.thanksFragment -> {
                    setMainTitle(
                        Desk360Constants.currentType?.data?.ticket_success_screen?.title
                    )
                    isTicketDetailFragment = false
                }
                R.id.ticketDetailFragment -> {
                    setMainTitle(
                        Desk360Constants.currentType?.data?.ticket_detail_screen?.title
                    )
                    isTicketDetailFragment = true
                }
                R.id.addNewTicketFragment -> {
                    setMainTitle(
                        Desk360Constants.currentType?.data?.create_screen?.title
                    )
                    isTicketDetailFragment = false
                }

                else -> ""
            }

            binding?.toolbar?.navigationIcon = ContextCompat.getDrawable(
                this,
                if (destination.id == R.id.ticketListFragment || destination.id == R.id.preNewTicketFragment || destination.id == R.id.thanksFragment)
                    R.drawable.close_button_desk
                else
                    R.drawable.back_btn_dark_theme
            )

            binding?.toolbar?.navigationIcon?.setColorFilter(
                Color.parseColor(Desk360Constants.currentType?.data?.general_settings?.header_icon_color),
                PorterDuff.Mode.SRC_ATOP
            )
        }

        binding?.logo?.visibility =
            if (Desk360Constants.currentType?.data?.general_settings?.copyright_logo_is_show == true) View.VISIBLE else View.GONE
    }

    fun notifyToolBar(cacheTickets: ArrayList<Desk360TicketResponse>) {

        this.cacheTickets = cacheTickets
        localMenu?.let { onPrepareOptionsMenu(it) }
    }

    fun changeMainUI() {
        binding?.apply {
            mainBackground.setBackgroundColor(Color.parseColor(Desk360Constants.currentType?.data?.general_settings?.main_background_color))
            toolbar.setBackgroundColor(Color.parseColor(Desk360Constants.currentType?.data?.general_settings?.header_background_color))
            toolbar.setTitleTextColor(Color.parseColor(Desk360Constants.currentType?.data?.general_settings?.header_text_color))

            if (Desk360Constants.currentType?.data?.general_settings?.header_shadow_is_hidden == true && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                toolbar.elevation = 20f
            }

            toolbarTitle.setTextColor(Color.parseColor(Desk360Constants.currentType?.data?.general_settings?.header_text_color))
            toolbarTitle.textSize =
                Desk360Constants.currentType?.data?.general_settings?.header_text_font_size!!.toFloat()
        }
    }

    fun setMainTitle(titleHead: String?) {
        binding?.toolbarTitle?.text = when {
            titleHead?.length!! < 29 -> titleHead
            else -> titleHead.substring(0, 18) + "..."
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        if (addBtnClicked) {
            return false
        }

        addBtnClicked = true
        Handler().removeCallbacksAndMessages(null)
        Handler().postDelayed({ addBtnClicked = false }, 800)

        if (cacheTickets?.isNotEmpty() == true) {
            setMainTitle(
                Desk360Constants.currentType?.data?.ticket_list_screen?.title
            )
        } else {
            setMainTitle(
                Desk360Constants.currentType?.data?.first_screen?.title
            )
        }

        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(window.decorView.rootView.windowToken, 0)

        return if (currentScreenTicketList) {
            checkRunningActivities()
            true
        } else
            findNavController(this, R.id.my_nav_host_fragment).navigateUp()

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        localMenu = menu
        menuInflater.inflate(R.menu.menu_main, menu)

        menu.findItem(R.id.action_add_new_ticket)?.apply {
            isVisible = true
            isEnabled = true
            icon = resources.getDrawable(R.drawable.add_new_message_icon_black)
            icon.setColorFilter(
                Color.parseColor(Desk360Constants.currentType?.data?.general_settings?.header_icon_color),
                PorterDuff.Mode.SRC_ATOP
            )
        }

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId

        if (addBtnClicked)
            return true

        if (id == R.id.action_add_new_ticket) {
            addBtnClicked = true
            Handler().removeCallbacksAndMessages(null)
            Handler().postDelayed({ addBtnClicked = false }, 800)

            findNavController(findViewById(R.id.my_nav_host_fragment)).navigate(
                when (Desk360Constants.manager?.enableHelpMode) {
                    true -> Desk360TicketListFragmentDirections.actionTicketListFragmentToPreNewTicketFragment()
                    else -> Desk360TicketListFragmentDirections.actionTicketListFragmentToAddNewTicketFragment()
                }
            )


            return true
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        if (currentScreenTicketList)
            checkRunningActivities()
        else
            onSupportNavigateUp()
    }

    private fun checkRunningActivities() {
        val activityManager = this.getSystemService(ACTIVITY_SERVICE) as ActivityManager

        val runningActivities =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                activityManager.appTasks[0].taskInfo.numActivities
            } else {
                activityManager.getRunningTasks(1)[0].numRunning
            }

        if (runningActivities == 1) {
            val intent = packageManager.getLaunchIntentForPackage(appId!!)
            startActivity(intent)
            finish()
        } else {
            super.onBackPressed()
        }
    }

    override fun onPrepareOptionsMenu(menu: Menu): Boolean {
        val register = menu.findItem(R.id.action_add_new_ticket)

        try {
            register.isVisible = currentScreenTicketList && cacheTickets!!.size > 0
        } catch (e: Exception) {
            register.isVisible = true
        }

        return true
    }

    override fun onDestroy() {
        super.onDestroy()
        if (disposable?.isDisposed == false)
            disposable?.dispose()
    }
}

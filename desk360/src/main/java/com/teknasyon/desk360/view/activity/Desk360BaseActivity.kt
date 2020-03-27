package com.teknasyon.desk360.view.activity

import android.app.Activity
import android.app.ActivityManager
import android.content.Context
import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.NavController
import androidx.navigation.Navigation.findNavController
import androidx.navigation.fragment.FragmentNavigator
import com.teknasyon.desk360.R
import com.teknasyon.desk360.databinding.Desk360FragmentMainBinding
import com.teknasyon.desk360.helper.Desk360Constants
import com.teknasyon.desk360.helper.Desk360CustomStyle
import com.teknasyon.desk360.model.Desk360TicketResponse
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.desk360_fragment_main.*

open class Desk360BaseActivity : AppCompatActivity(), LifecycleOwner {

    private lateinit var register: MenuItem

    private var cacheTickets: ArrayList<Desk360TicketResponse>? = null
    private var navController: NavController? = null
    private var disposable: Disposable? = null
    private var localMenu: Menu? = null

    var notificationToken: String? = null
    var targetId: String? = null
    var appId: String? = null

    private var currentScreenTicketList = true
    var addBtnClicked = false
    var isMainLoadingShown = false
    var isTicketDetailFragment = false

    var binding: Desk360FragmentMainBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val bundle = intent.extras

        bundle?.let {

            appId = bundle.getString("appId")
            targetId = bundle.getString("targetId")
            notificationToken = bundle.getString("token")
        }

        binding = Desk360FragmentMainBinding.inflate(layoutInflater)
        setContentView(binding!!.root)
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
                    isTicketDetailFragment = false
                }
                R.id.thanksFragment -> {
                    setMainTitle(
                        Desk360Constants.currentType?.data?.ticket_success_screen?.title,
                        binding?.toolbarTitle
                    )
                    isTicketDetailFragment = false
                }
                R.id.ticketDetailFragment -> {
                    setMainTitle(
                        Desk360Constants.currentType?.data?.ticket_detail_screen?.title,
                        binding?.toolbarTitle
                    )
                    isTicketDetailFragment = true
                }
                R.id.addNewTicketFragment -> {
                    setMainTitle(
                        Desk360Constants.currentType?.data?.create_screen?.title,
                        binding?.toolbarTitle
                    )
                    isTicketDetailFragment = false
                }

                else -> ""
            }

            if (destination.id == R.id.ticketListFragment || destination.id == R.id.preNewTicketFragment || destination.id == R.id.thanksFragment) {
                toolbar.navigationIcon = resources.getDrawable(R.drawable.close_button_desk)
            } else {
                toolbar.navigationIcon = resources.getDrawable(R.drawable.back_btn_dark_theme)
            }

            toolbar.navigationIcon?.setColorFilter(
                Color.parseColor(Desk360Constants.currentType?.data?.general_settings?.header_icon_color),
                PorterDuff.Mode.SRC_ATOP
            )


        }
    }

    fun notifyToolBar(cacheTickets: ArrayList<Desk360TicketResponse>) {

        this.cacheTickets = cacheTickets
        localMenu?.let { onPrepareOptionsMenu(it) }
    }

    fun changeMainUI() {

        binding!!.mainBackground.setBackgroundColor(Color.parseColor(Desk360Constants.currentType?.data?.general_settings?.main_background_color))

        binding!!.toolbar.setBackgroundColor(Color.parseColor(Desk360Constants.currentType?.data?.general_settings?.header_background_color))
        binding!!.toolbar.setTitleTextColor(Color.parseColor(Desk360Constants.currentType?.data?.general_settings?.header_text_color))

        if (Desk360Constants.currentType?.data?.general_settings?.header_shadow_is_hidden!!) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                binding!!.toolbar.elevation = 20f
            }
        }

        binding!!.toolbarTitle.setTextColor(Color.parseColor(Desk360Constants.currentType?.data?.general_settings?.header_text_color))
        binding!!.toolbarTitle.textSize =
            Desk360Constants.currentType?.data?.general_settings?.header_text_font_size!!.toFloat()
    }

    fun setMainTitle(titleHead: String?, titleTextView: TextView?) {

        when {
            titleHead?.length!! < 29 -> titleTextView?.text = titleHead
            else -> titleTextView?.text = titleHead.substring(0, 18) + "..."
        }
    }

    override fun onSupportNavigateUp(): Boolean {

        if (addBtnClicked) {
            return false
        }

        addBtnClicked = true
        Handler().removeCallbacksAndMessages(null)
        Handler().postDelayed({ addBtnClicked = false }, 800)

        Log.e("exception","girdi")

        if (cacheTickets!!.size > 0) {
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

        register = menu.findItem(R.id.action_add_new_ticket)

        register.isVisible = true
        register.isEnabled = true
        register.icon = resources.getDrawable(R.drawable.add_new_message_icon_black)

        register.icon?.setColorFilter(
            Color.parseColor(Desk360Constants.currentType?.data?.general_settings?.header_icon_color),
            PorterDuff.Mode.SRC_ATOP
        )

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        val id = item.itemId

        if (addBtnClicked) {
            return true

        }
        if (id == R.id.action_add_new_ticket) {
            addBtnClicked = true
            Handler().removeCallbacksAndMessages(null);
            Handler().postDelayed({ addBtnClicked = false }, 800)
            findNavController(findViewById(R.id.my_nav_host_fragment)).navigate(R.id.action_ticketListFragment_to_preNewTicketFragment)
            return true
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {

        if (currentScreenTicketList) {
            checkRunningActivities()
        } else
            onSupportNavigateUp()
    }

    private fun checkRunningActivities() {

        val am = this.getSystemService(ACTIVITY_SERVICE) as ActivityManager

        val runningActivities =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                am.appTasks[0].taskInfo.numActivities
            } else {
                am.getRunningTasks(1)[0].numRunning
            }

        if (runningActivities == 1) {
            val intent = packageManager.getLaunchIntentForPackage(appId!!)
            startActivity(intent)
            finish()
        } else {
            super.onBackPressed()
        }
    }

    fun blockUI(activity: Activity, status: Boolean) {

        if (status) {
            activity.window.setFlags(
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
            )
        } else {
            activity.window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
        }
    }

    override fun onPrepareOptionsMenu(menu: Menu): Boolean {
        val register: MenuItem = menu.findItem(R.id.action_add_new_ticket)

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

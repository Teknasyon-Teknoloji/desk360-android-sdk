package com.teknasyon.desk360.helper

import android.content.Context
import android.content.Intent
import android.os.Build
import android.telephony.TelephonyManager
import com.teknasyon.desk360.connection.BaseCallback
import com.teknasyon.desk360.connection.Desk360RetrofitFactory
import com.teknasyon.desk360.model.Desk360TicketListResponse
import com.teknasyon.desk360.model.Desk360TicketResponse
import com.teknasyon.desk360.modelv2.Desk360ConfigResponse
import com.teknasyon.desk360.view.activity.Desk360SplashActivity
import com.teknasyon.desk360.viewmodel.GetTypesViewModel
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Response
import java.util.Date
import java.util.Locale

/**
 * Created by Yasin ÇETİN on 15/09/2021.
 */

object Desk360SDK {
    var manager: Desk360SDKManager? = null
    var client: Desk360Client? = null
    var config: Desk360ConfigResponse? = null
        get() {
            field = Desk360Config.instance.getDesk360Preferences()?.types
            return field
        }

    /**
     * It checks the config information created specifically for each application.
     * If config information is not available, config information is retrieved again.
     * @param  desk360ConfigResponse unit parameter
     * @return Boolean
     */
    fun desk360Config(desk360ConfigResponse: (status: Boolean) -> Unit = {}): Boolean {
        val call = GetTypesViewModel()

        Desk360Config.instance.getDesk360Preferences()?.data?.let {

            if (Util.isTokenExpired(it.expired_at)) {

                call.register { status ->

                    if (status) {
                        checkType(desk360ConfigResponse, call)
                    }
                }
            } else {
                checkType(desk360ConfigResponse, call)
            }

        } ?: run {

            call.register { status ->

                if (status) {
                    checkType(desk360ConfigResponse, call)
                }
            }
        }

        return true
    }

    /**
     * Checks whether custom-created config information is received for each application.
     * @param desk360ConfigResponse the key of application
     * @param call the key of application
     */
    fun checkType(
        desk360ConfigResponse: (status: Boolean) -> Unit,
        call: GetTypesViewModel
    ) {

        val isTypeFetched = Desk360Config.instance.getDesk360Preferences()!!.isTypeFetched

        if (isTypeFetched) {
            desk360ConfigResponse(true)
        } else {
            call.getTypes { configurationsResponse ->
                desk360ConfigResponse(configurationsResponse)
            }
        }
    }

    /**
     * Gets the ticket id from the notification data
     * @param hermes the key of Json Object
     * @return ticket id
     */
    fun getTicketId(hermes: String?): String? {

        try {
            hermes?.let {

                val targetDetail = JSONObject(hermes).getJSONObject("target_detail")
                targetDetail?.let {

                    val targetCategory = targetDetail.getString("target_category")
                    targetCategory?.let {

                        if (targetCategory == "Desk360Deeplink") {
                            return targetDetail.getString("target_id")
                        }
                    }
                }
            }
        } catch (e: Exception) {
            return null
        }
        return null
    }

    /**
     * Gets the Country Code
     * @return the network country code if the network country is available, or the device country code if it is not.
     */
    fun countryCode(): String {

        val tm =
            Desk360Config.instance.context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        return if (tm.networkCountryIso != null && tm.networkCountryIso != "") tm.networkCountryIso else Locale.getDefault().country
    }

    /**
     * Generated a unique device id
     */
    fun getDeviceId() {
        val devicesId = Desk360Config.instance.getDesk360Preferences()?.adId
        if (devicesId != null && devicesId != "") {
            return
        }

        val date = Date()
        date.time

        val deviceId =
            date.time.toString() + Build.VERSION.SDK_INT + "-" + Build.VERSION.INCREMENTAL + Build.MODEL

        deviceId.let {
            Desk360Config.instance.getDesk360Preferences()!!.adId = it
        }
    }

    fun startWithTopic(id: String) {
        client?.selectedTopic = id
        start()
    }

    /**
     * Firebase or HM Push Kit set token
     * @param token
     */
    fun setPushToken(token: String) {
        client?.notificationToken = token
    }

    /**
     * Start the Desk360 SDK.
     * The Desk360SDKManager must have been built before.
     * @see Desk360SDKManager
     */
    fun start(ticketId: String? = null) {
        Desk360Config.instance.context.startActivity(
            getIntent(
                Desk360Config.instance.context,
                ticketId
            )
        )
        client?.selectedTopic = null
    }

    /**
     * Generated Intent for start the SDK
     * @param context
     * @param ticketId
     * @return Intent
     * @see Intent
     */
    fun getIntent(context: Context, ticketId: String? = null) =
        Intent(context, Desk360SplashActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

            putExtra(Desk360SplashActivity.EXTRA_APP_ID, context.applicationInfo.processName)
            putExtra(Desk360SplashActivity.EXTRA_TARGET_ID, ticketId)
            putExtra(Desk360SplashActivity.EXTRA_TOKEN, client?.notificationToken)
            putExtra(Desk360SplashActivity.SELECTED_TOPIC, client?.selectedTopic)
            putExtra(Desk360SplashActivity.EXTRA_DEVICE_TOKEN, client?.deviceId)
        }

    /**
     * Return unread ticket list
     * @param list
     * @see Desk360TicketResponse
     */
    fun getUnreadTickets(list: (ArrayList<Desk360TicketResponse>) -> Unit) {
        Desk360RetrofitFactory.instance.desk360Service.getTicket()
            .enqueue(object : BaseCallback<Desk360TicketListResponse>() {
                override fun onResponseSuccess(
                    call: Call<Desk360TicketListResponse>,
                    response: Response<Desk360TicketListResponse>
                ) {
                    if (response.isSuccessful && response.body() != null) {
                        list(response.body()!!.data?.filter { unread -> unread.status == "unread" } as ArrayList<Desk360TicketResponse>)
                    }
                }

                override fun onFailure(call: Call<Desk360TicketListResponse>, t: Throwable) {
                    super.onFailure(call, t)
                    list(arrayListOf())
                }
            })
    }
}
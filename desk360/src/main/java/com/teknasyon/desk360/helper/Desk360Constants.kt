package com.teknasyon.desk360.helper

import android.content.Context
import android.os.Build
import android.telephony.TelephonyManager
import com.teknasyon.desk360.modelv2.Desk360ConfigResponse
import com.teknasyon.desk360.viewmodel.GetTypesViewModel
import org.json.JSONObject
import java.util.*

/**
 * Created by seyfullah on 09/04/2019.
 */

object Desk360Constants {
    var baseURL = "https://teknasyon.desk360.com/"
    var manager: Desk360SDKManager? = null
    var currentType: Desk360ConfigResponse? = null
        get() {
            field = Desk360Config.instance.getDesk360Preferences()?.types
            return field
        }

    fun desk360Config(desk360ConfigResponse: (status: Boolean) -> Unit = {}): Boolean {
        val call = GetTypesViewModel()

        Desk360Config.instance.getDesk360Preferences()?.data?.let {

            if (Util.isTokenExpired(it.expired_at)) {

                call.register { status ->

                    if (status) {
                        checkType(desk360ConfigResponse, call)
                    } // TODO REGISTER OLAMAZ ISE BAKILACAK
                }
            } else {
                checkType(desk360ConfigResponse, call)
            }

        } ?: run {

            call.register { status ->

                if (status) {
                    checkType(desk360ConfigResponse, call)
                } // TODO REGISTER OLAMAZ ISE BAKILACAK
            }
        }

        return true
    }

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

    fun countryCode(): String {

        val tm =
            Desk360Config.instance.context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        return if (tm.networkCountryIso != null && tm.networkCountryIso != "") tm.networkCountryIso else Locale.getDefault().country
    }

    /**
     * Generated a unique [deviceId]
     *
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
}
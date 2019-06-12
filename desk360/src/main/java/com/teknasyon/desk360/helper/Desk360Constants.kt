package com.teknasyon.desk360.helper

import android.content.Context
import android.os.AsyncTask
import android.telephony.TelephonyManager
import com.google.android.gms.ads.identifier.AdvertisingIdClient
import com.google.android.gms.common.GooglePlayServicesNotAvailableException
import com.google.android.gms.common.GooglePlayServicesRepairableException
import com.teknasyon.desk360.Desk360Application
import java.io.IOException
import java.util.*


/**
 * Created by seyfullah on 09/04/2019.
 */

object Desk360Constants {
    var currentTheme: String = "light"
    var app_key: String? = ""

    fun countryCode(): String {
        val tm = Desk360Application.instance.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        return if (tm.networkCountryIso != null && tm.networkCountryIso != "") tm.networkCountryIso else Locale.getDefault().country
    }

    fun getDeviceId() {
        val devicesId = Desk360Application.instance.getDesk360Preferences()?.adId
        if (devicesId != null && devicesId != "") {
            return
        }

        AsyncTask.execute {
            try {

                val adInfo = AdvertisingIdClient.getAdvertisingIdInfo(Desk360Application.instance)
                val deviceId = adInfo?.id
                deviceId.let {
                    Desk360Application.instance.getDesk360Preferences()!!.adId = it!!
                }

            } catch (exception: IOException) {
            } catch (exception: GooglePlayServicesRepairableException) {
            } catch (exception: GooglePlayServicesNotAvailableException) {
            }
        }
    }
}

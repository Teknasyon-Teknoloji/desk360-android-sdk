package com.teknasyon.desk360.helper

import com.teknasyon.desk360.model.Desk360Data
import com.teknasyon.desk360.model.Desk360Meta

open class Desk360Preferences : PreferencesManager() {
    var meta: Desk360Meta?
        get() = getObject<Desk360Meta>(META, Desk360Meta::class.java)
        set(meta) = putObject(META, meta!!)

    var data: Desk360Data?
        get() = getObject<Desk360Data>(DATA, Desk360Data::class.java)
        set(meta) = putObject(DATA, meta!!)

    var adId: String?
        get() = getString(AD_ID)
        set(adId) = setString(AD_ID, adId ?: "")


    companion object {
        private const val META = "meta"
        private const val DATA = "data"
        private const val AD_ID = "device_id"
    }
}

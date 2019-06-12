package com.teknasyon.desk360.helper

import com.teknasyon.desk360.model.Data
import com.teknasyon.desk360.model.Meta

open class Desk360Preferences : PreferencesManager() {
    var meta: Meta?
        get() = getObject<Meta>(META, Meta::class.java)
        set(meta) = putObject(META, meta!!)

    var data: Data?
        get() = getObject<Data>(DATA, Data::class.java)
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

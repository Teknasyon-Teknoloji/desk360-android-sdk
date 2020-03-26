package com.teknasyon.desk360.helper

import com.teknasyon.desk360.model.Desk360Data
import com.teknasyon.desk360.model.Desk360Meta
import com.teknasyon.desk360.model.Desk360TypeResponse
import com.teknasyon.desk360.modelv2.Desk360ConfigResponse

open class Desk360Preferences : PreferencesManager() {
    var meta: Desk360Meta?
        get() = getObject<Desk360Meta>(META, Desk360Meta::class.java)
        set(meta) = putObject(META, meta!!)

    var data: Desk360Data?
        get() = getObject<Desk360Data>(DATA, Desk360Data::class.java)
        set(meta) = putObject(DATA, meta!!)

    var types: Desk360ConfigResponse?
        get() {
            return getObject<Desk360ConfigResponse>(TYPES, Desk360ConfigResponse::class.java) ?: Desk360ConfigResponse()
        }
        set(types) {
            putObject(TYPES, types!!)
        }

    var isTypeFetched: Boolean
        get() {
            return getObject<Boolean>(TYPEFETCHED, Boolean::class.java) ?: false
        }
        set(isTypeFetched) {
            putObject(TYPEFETCHED, isTypeFetched)
        }

    var subjects: Desk360TypeResponse?
        get() {
            return getObject<Desk360TypeResponse>(SUBJECTS, Desk360TypeResponse::class.java)
                ?: Desk360TypeResponse()
        }
        set(subjects) {
            putObject(SUBJECTS, subjects!!)
        }


    var adId: String?
        get() = getString(AD_ID)
        set(adId) = setString(AD_ID, adId ?: "")

    companion object {

        private const val META = "meta"
        private const val DATA = "data"
        private const val TYPES = "types"
        private const val TYPEFETCHED = "isTypeFetched"
        private const val SUBJECTS = "subjects"
        private const val AD_ID = "device_id"
    }
}

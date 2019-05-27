package com.teknasyon.desk360.helper

import com.teknasyon.desk360.model.Data
import com.teknasyon.desk360.model.Meta

open class AresPreferences : PreferencesManager() {
    var meta: Meta?
        get() = getObject<Meta>(META, Meta::class.java)
        set(meta) = putObject(META, meta!!)

    var data: Data?
        get() = getObject<Data>(DATA, Data::class.java)
        set(meta) = putObject(DATA, meta!!)

    companion object {
        private const val META = "meta"
        private const val DATA = "data"
    }
}

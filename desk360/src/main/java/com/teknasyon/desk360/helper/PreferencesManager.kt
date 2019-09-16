package com.teknasyon.desk360.helper

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import java.lang.reflect.Type

open class PreferencesManager(context: Context) {
    private val preferences: SharedPreferences
    private val gson: Gson

    init {
        preferences = context.getSharedPreferences(
            PREFERENCE_NAME,
            Context.MODE_PRIVATE
        )!!
        gson = Gson()
    }

    @SuppressLint("ApplySharedPref")
    internal fun setString(tag: String, value: String) {
        val editor = preferences.edit()
        editor.putString(tag, value)
        editor.commit()
    }

    internal fun getString(tag: String): String? {
        return preferences.getString(tag, null)
    }


    internal fun putObject(tag: String, targetObject: Any) {
        setString(tag, gson.toJson(targetObject))
    }


    fun <T> getObject(key: String, type: Type? = null, clazz: Class<T>? = null): T? {
        if (preferences.contains(key)) {
            val preferenceTarget = preferences.getString(key, "")
            if (preferenceTarget != "") {
                return gson.fromJson<T>(preferenceTarget, type ?: clazz)
            }
        }
        return null
    }

    companion object {
        private const val PREFERENCE_NAME = "desk360_android_sdk"
    }
}

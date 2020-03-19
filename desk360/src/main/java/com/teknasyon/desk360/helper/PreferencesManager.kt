package com.teknasyon.desk360.helper

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import com.google.gson.Gson
import java.lang.reflect.Type

open class PreferencesManager {
    private val preferences: SharedPreferences
    private val gson: Gson

    init {
        preferences = Desk360Config.instance.context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)!!
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

    fun writeObject(key: String? = null, data: Any) {
        key?.let { safeKey ->
            write(safeKey, gson.toJson(data))
        } ?: write(data::class.java.simpleName, gson.toJson(data))
    }

    fun <T> readObject(key: String? = null, target: Class<T>): T? {
        key?.let { safeKey ->
            return gson.fromJson(read(safeKey, ""), target)
        } ?: return gson.fromJson(read(target.simpleName, ""), target) as T
    }

    private fun <T> write(key: String, value: T) {
        when (value) {
            is String -> preferences.edit { putString(key, value).commit() }
            is Int -> preferences.edit { putInt(key, value).commit() }
            is Boolean -> preferences.edit { putBoolean(key, value).commit() }
            is Long -> preferences.edit { putLong(key, value).commit() }
            else -> Unit
        }
    }

    private fun <T> read(key: String, defaultValue: T): T {
        return when (defaultValue) {
            is String -> preferences.getString(key, defaultValue as String) as? T ?: defaultValue
            is Int -> preferences.getInt(key, defaultValue as Int) as T ?: defaultValue
            is Boolean -> preferences.getBoolean(key, defaultValue as Boolean) as T ?: defaultValue
            is Long -> preferences.getLong(key, defaultValue as Long) as T ?: defaultValue
            else -> defaultValue
        }
    }

    companion object {
        private const val PREFERENCE_NAME = "desk360_android_sdk"
    }
}

package com.trackster.tracksterapp.utils

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import android.text.TextUtils
import com.facebook.AccessToken
import com.facebook.login.LoginManager
import com.google.gson.Gson
import com.trackster.tracksterapp.network.responce.InitialAccessToken
import java.util.*

object PreferenceUtils {


    private const val KEY_AUTH_TOKEN = "auth_token"
    private const val KEY_USER_ID = "user_id"
    private const val KEY_CHAT_ID = "chat_id"
    private const val KEY_BROKER_NAME = "broker_name"
    private const val KEY_DRIVER_NAME = "driver_name"
    private const val BROKER_ID = "broker_id"
    private const val CARRIER_ID = "carrier_id"
    private const val CARRIER_NAME = "carrier_name"
    private const val MESS_SIZE = "mess_size"
    private const val PDF_SIZE = "pdf_size"
    private const val PNG_SIZE = "png_size"
    private const val AAC_SIZE = "aac_size"

    private fun getPreferences(context: Context): SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)

    private inline fun SharedPreferences.edit(operation: (SharedPreferences.Editor) -> Unit) {
        val editor = this.edit()
        operation(editor)
        editor.apply()
    }

    /**
     * Saves the provided key-value pair in shared preferences.
     *
     * @param key       The name of the preference
     * @param value     The new value of the preference
     */
    operator fun SharedPreferences.set(key: String, value: Any?) {
        when (value) {
            is String? -> edit { it.putString(key, value) }
            is Int -> edit { it.putInt(key, value) }
            is Boolean -> edit { it.putBoolean(key, value) }
            is Float -> edit { it.putFloat(key, value) }
            is Long -> edit { it.putLong(key, value) }
            else -> throw UnsupportedOperationException("Not implemented yet!")
        }
    }

    /**
     * Returns the value on a given key.
     *
     * @param key           The preference name
     * @param defaultValue  Optional default value - will take null for strings, false for bool and -1 for numeric values if [defaultValue] is not specified
     */
    private inline fun <reified T : Any> SharedPreferences.get(key: String, defaultValue: T? = null): T? =
        when (T::class) {
            String::class -> getString(key, defaultValue as? String) as T?
            Int::class -> getInt(key, defaultValue as? Int ?: -1) as T?
            Boolean::class -> getBoolean(key, defaultValue as? Boolean ?: false) as T?
            Float::class -> getFloat(key, defaultValue as? Float ?: -1f) as T?
            Long::class -> getLong(key, defaultValue as? Long ?: -1) as T?
            else -> throw UnsupportedOperationException("Not implemented yet!")
        }

    fun saveAuthorizationToken(context: Context, token: String) {
        getPreferences(context)[KEY_AUTH_TOKEN] = token
    }

    fun getAuthorizationToken(context: Context): String = getPreferences(context).getString(KEY_AUTH_TOKEN, "") as String
    fun saveUserId(context: Context, id: String) {
        getPreferences(context)[KEY_USER_ID] = id
    }
    fun getUserId(context: Context): String = getPreferences(context).getString(KEY_USER_ID, "") as String

    fun saveChatId(context: Context, id: String) {
        getPreferences(context)[KEY_CHAT_ID] = id
    }
    fun getChatId(context: Context): String = getPreferences(context).getString(KEY_CHAT_ID, "") as String

    fun saveDriverName(context: Context, id: String) {
        getPreferences(context)[KEY_DRIVER_NAME] = id
    }
    fun getDriverName(context: Context): String = getPreferences(context).getString(KEY_DRIVER_NAME, "") as String

    fun saveBrokerName(context: Context, id: String) {
        getPreferences(context)[KEY_BROKER_NAME] = id
    }
    fun getBrokerName(context: Context): String = getPreferences(context).getString(KEY_BROKER_NAME, "") as String

    fun saveCarrierName(context: Context, id: String) {
        getPreferences(context)[CARRIER_NAME] = id
    }
    fun getCarrierName(context: Context): String = getPreferences(context).getString(CARRIER_NAME, "") as String

    fun saveCarrierId(context: Context, id: String) {
        getPreferences(context)[CARRIER_ID] = id
    }
    fun getCarrierId(context: Context): String = getPreferences(context).getString(CARRIER_ID, "") as String

    fun saveBrokerId(context: Context, id: String) {
        getPreferences(context)[BROKER_ID] = id
    }
    fun getBrokerId(context: Context): String = getPreferences(context).getString(BROKER_ID, "") as String

    fun saveMessSize(context: Context, id: Int) {
        getPreferences(context)[MESS_SIZE] = id
    }
    fun getSize(context: Context): Int? = getPreferences(context).getInt(MESS_SIZE, 0)

    fun savePDFSize(context: Context, id: Int) {
        getPreferences(context)[PDF_SIZE] = id
    }
    fun getPDFSize(context: Context): Int? = getPreferences(context).getInt(PDF_SIZE, 0)

    fun savePNGSize(context: Context, id: Int) {
        getPreferences(context)[PNG_SIZE] = id
    }
    fun getPNGSize(context: Context): Int? = getPreferences(context).getInt(PNG_SIZE, 0)

    fun saveAudioSize(context: Context, id: Int) {
        getPreferences(context)[AAC_SIZE] = id
    }
    fun getAudioSize(context: Context): Int? = getPreferences(context).getInt(AAC_SIZE, 0)

    fun saveString(context: Context, id: String) {
        getPreferences(context)["string"] = id
    }
    fun getString(context: Context): String = getPreferences(context).getString("string", "") as String

     fun removePreference(context:Context, key:String) {
        val preferences = getPreferences(context)
        val editor = preferences.edit()
        editor.remove(key)
        editor.apply()
    }
}
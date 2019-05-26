package com.trackster.tracksterapp.utils

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
    private const val PDF_NAME = "pdf_name"

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

    fun savePdfPath(context: Context, id: String) {
        getPreferences(context)[PDF_NAME] = id
    }
    fun getPdf(context: Context): String = getPreferences(context).getString(PDF_NAME, "") as String

//    fun clearAuthorizationToken(context: Context) {
//        getPreferences(context)[KEY_AUTH_TOKEN] = ""
//        getPreferences(context)[KEY_TOKEN_EXPIRE] = ""
//    }
//
//    fun saveLanguage(context: Context, language: String) {
//        getPreferences(context)[KEY_LANGUAGE] = language
//    }
//
//    fun saveIsProfileComplete(context: Context, isComplete: Boolean) {
//        getPreferences(context)[KEY_IS_PROFILE_COMPLETE] = isComplete
//    }
//
//    private fun isProfileComplete(context: Context): Boolean = getPreferences(context).getBoolean(KEY_IS_PROFILE_COMPLETE, false)
//
//    fun getAuthorizationToken(context: Context): String = "bearer " + getPreferences(context).getString(KEY_AUTH_TOKEN, "")
//
//    fun getLanguage(context: Context): String = getPreferences(context).getString(KEY_LANGUAGE, Constants.DEFAULT_LANGUAGE) as String
//
//    fun getTokenExipreDate(context: Context): String = getPreferences(context).getString(KEY_TOKEN_EXPIRE, "") as String
//
//    fun isUserLoggedIn(context: Context): Boolean {
//        val authToken = getPreferences(context).getString(KEY_AUTH_TOKEN, "") as String
//
//        val expiryDate = getTokenExipreDate(context)
//
//        if (expiryDate.isBlank()) {
//            return false
//        }
//
//        return authToken.isNotEmpty() && DateFormat.tokenNotExpired(expiryDate) && isProfileComplete(context)
//    }
//
//    fun logoutUser(context: Context) {
//        clearAuthorizationToken(context)
//        clearProfile(context)
//        saveIsProfileComplete(context, false)
//        if (InitialAccessToken.getCurrentAccessToken() != null) {
//            LoginManager.getInstance().logOut();
//        }
//    }
//
//    private fun clearProfile(context: Context) {
//        //profile
//        getPreferences(context)[KEY_USER_PROFILE_PICTURE_STRING] = ""
//        getPreferences(context)[KEY_PROFILE_USERNAME] = ""
//        getPreferences(context)[KEY_USER_NICKNAME] = ""
//        getPreferences(context)[KEY_USER_CITY_ID] = ""
//        getPreferences(context)[KEY_USER_ID] = ""
//        getPreferences(context)[KEY_PROFILE_NAME] = ""
//        getPreferences(context)[KEY_PROFILE_LASTNAME] = ""
//        getPreferences(context)[KEY_PROFILE_BIRTHDAY] = ""
//        getPreferences(context)[KEY_PROFILE_PHONE] = ""
//        getPreferences(context)[KEY_PROFILE_EMAIL] = ""
//        getPreferences(context)[KEY_PROFILE_GENDER] = ""
//        getPreferences(context)[KEY_PROFILE_HIDE_USERNAME] = ""
//
//        //Vip Membership
//        getPreferences(context)[KEY_IS_USER_VIP_MEMBER] = false
//        getPreferences(context)[KEY_USER_VIP_MEMBERSHIP_DETAILS] = ""
//    }
//
//    fun saveMenuItem(context: Context, menuItem: MenuItem?) {
//        getPreferences(context)[KEY_MENU_ITEM_COUNTRY] = menuItem?.country
//        getPreferences(context)[KEY_MENU_ITEM_COUNTRY_ISO] = menuItem?.countryIso
//        getPreferences(context)[KEY_MENU_ITEM_CITY] = menuItem?.city
//        getPreferences(context)[KEY_MENU_ITEM_CITY_ID] = menuItem?.cityId
//        getPreferences(context)[KEY_MENU_ITEM_PLACE_TYPE] = menuItem?.placeType
//        getPreferences(context)[KEY_MENU_ITEM_EVENT_OCCURRENCE] = menuItem?.eventOccurrence
//        getPreferences(context)[KEY_MENU_ITEM_DATE] = menuItem?.date
//        getPreferences(context)[KEY_MENU_ITEM_CLUB_CITY_MEMBER_DISCOUNT] = menuItem?.clubCityRebate
//        getPreferences(context)[KEY_MENU_ITEM_VISITED_BY_MY_FRIENDS] = menuItem?.visitedByMyFriends
//        getPreferences(context)[KEY_MENU_ITEM_INCLUDE_NEARBY_CITES] = menuItem?.includeNearbyCities
//    }
//
//    fun getCheckItemSelectCountry(context: Context) = getPreferences(context).getInt(KEY_SELECT_COUNTRY_DIALOG_CHECKED_ITEM, SELECT_COUNTRY_DIALOG_CHECKED_ITEM)
//    fun saveCheckedItemSelectCountry(context: Context, item: Int) {
//        getPreferences(context)[KEY_SELECT_COUNTRY_DIALOG_CHECKED_ITEM] = item
//    }
//
//    fun getMenuDateformatedForApi(context: Context): String? {
//        val date = getPreferences(context).getString(KEY_MENU_ITEM_DATE, "") as String
//        if (TextUtils.isEmpty(date)) {
//            return DateFormat.formatDate(Calendar.getInstance().time, DateFormat.DATE_FORMAT_MENU_DATE)
//        }
//        return DateFormat.formatDateEventsFilter(date)
//    }
//
//    fun getMenuCountry(context: Context): String =
//        getPreferences(context).getString(KEY_MENU_ITEM_COUNTRY, DEFAULT_COUNTRY) as String
//
//    fun getMenuCountryIso(context: Context): String =
//        getPreferences(context).getString(KEY_MENU_ITEM_COUNTRY_ISO, DEFAULT_COUNTRY_ISO) as String
//
//    fun getMenuCity(context: Context): String =
//        getPreferences(context).getString(KEY_MENU_ITEM_CITY, DEFAULT_CITY) as String
//
//    private fun getMenuCityId(context: Context): Int =
//        getPreferences(context).getInt(KEY_MENU_ITEM_CITY_ID, DEFAULT_CITY_ID)
//
//    fun getMenuPlaceType(context: Context): Int =
//        getPreferences(context).getInt(KEY_MENU_ITEM_PLACE_TYPE, DEFAULT_PLACE_TYPE)
//
//    fun getMenuEventOccurrence(context: Context): Int =
//        getPreferences(context).getInt(KEY_MENU_ITEM_EVENT_OCCURRENCE, DEFAULT_EVENT_OCCURRENCE)
//
//    fun getMenuDate(context: Context): String {
//        val date = getPreferences(context).getString(KEY_MENU_ITEM_DATE, "") as String
//        if (TextUtils.isEmpty(date)) {
//            return DateFormat.formatDate(Calendar.getInstance().time, DateFormat.DATE_FORMAT_MENU_DATE)
//        }
//        return date
//    }
//
//    fun getMenuClubCityMemberDiscount(context: Context): Boolean =
//        getPreferences(context).getBoolean(KEY_MENU_ITEM_CLUB_CITY_MEMBER_DISCOUNT, false)
//
//    fun getMenuVisitedByMyFriends(context: Context): Boolean =
//        getPreferences(context).getBoolean(KEY_MENU_ITEM_VISITED_BY_MY_FRIENDS, false)
//
//    fun getMenuIncludeNearbyCities(context: Context): Boolean =
//        getPreferences(context).getBoolean(KEY_MENU_ITEM_INCLUDE_NEARBY_CITES, true)
//
//    fun saveFilterSortBy(context: Context, sortBy: Int) {
//        getPreferences(context)[KEY_FILTER_SORT_BY] = sortBy
//    }
//
//    fun getFilterSortBy(context: Context): Int {
//        return when (getPreferences(context).getInt(KEY_MENU_ITEM_PLACE_TYPE, DEFAULT_PLACE_TYPE)) {
//            2 -> getPreferences(context).getInt(KEY_FILTER_SORT_BY, DEFAULT_SORT_BY)
//            else -> {
//                if (getPreferences(context).getInt(KEY_FILTER_SORT_BY, DEFAULT_SORT_BY) > 2) {
//                    return DEFAULT_SORT_BY_CLUBS
//                }
//                return getPreferences(context).getInt(KEY_FILTER_SORT_BY, DEFAULT_SORT_BY_CLUBS)
//            }
//        }
//    }
//
//    fun getPlaceSearchRequest(context: Context, pageNum: Int): HomeEventFilterRequest {
//
//        var eventDate: String? = null
//        val eventOccurrance = getMenuEventOccurrence(context)
//
//        if (eventOccurrance == 1) {
//            eventDate = PreferenceUtils.getMenuDateformatedForApi(context)
//        }
//
//        val requestEvents = HomeEventFilterRequest(cityId = getMenuCityId(context),
//            placeType = getMenuPlaceType(context),
//            pageNumber = pageNum,
//            pageSize = EventsFragment.PAGE_SIZE,
//            sortBy = getFilterSortBy(context),
//            eventOccurrence = getMenuEventOccurrence(context),
//            clubCityRebate = getMenuClubCityMemberDiscount(context),
//            visitedByMyFriends = getMenuVisitedByMyFriends(context),
//            descending = getMenuPlaceType(context) == DEFAULT_PLACE_TYPE,
//            showInNearbyCites = getMenuIncludeNearbyCities(context),
//            eventDate = eventDate)
//
//        return requestEvents
//    }
//
//    fun saveUser(context: Context, user: User) {
//        getPreferences(context)[KEY_USER_PROFILE_PICTURE_STRING] = user.profilePictureString
//        getPreferences(context)[KEY_PROFILE_USERNAME] = user.userName
//        getPreferences(context)[KEY_USER_NICKNAME] = user.nickName
//        getPreferences(context)[KEY_USER_CITY_ID] = user.cityId
//        getPreferences(context)[KEY_USER_ID] = user.userId
//        getPreferences(context)[KEY_PROFILE_NAME] = user.firstName
//        getPreferences(context)[KEY_PROFILE_LASTNAME] = user.lastName
//        getPreferences(context)[KEY_PROFILE_BIRTHDAY] = user.birthday
//        getPreferences(context)[KEY_PROFILE_PHONE] = user.phoneNumber
//        getPreferences(context)[KEY_PROFILE_EMAIL] = user.email
//        getPreferences(context)[KEY_PROFILE_GENDER] = user.gender
//        getPreferences(context)[KEY_PROFILE_HIDE_USERNAME] = user.hideMyRealName
//        getPreferences(context)[KEY_IS_USER_VIP_MEMBER] = user.isVipMembershipConnected
//    }
//
//    fun saveUserProfilePicture(context: Context, profilePictureRequest: ProfilePictureRequest) {
//        getPreferences(context)[KEY_USER_PROFILE_PICTURE_STRING] = profilePictureRequest.pictureBase64
//    }
//
//
//    fun saveUserNotificationPrefernce(context: Context, prefNotifications: String) {
//        getPreferences(context)[KEY_PERMISSIONS_NOTIFICATIONS_USER] = prefNotifications
//    }
//
//    fun getUserNotificationPrefernce(context: Context): String = getPreferences(context).getString(KEY_PERMISSIONS_NOTIFICATIONS_USER, "") as String
//
//    fun getUserProfilePicture(context: Context): String = getPreferences(context).getString(KEY_USER_PROFILE_PICTURE_STRING, "") as String
//
//    fun getUsername(context: Context): String = getPreferences(context).getString(KEY_PROFILE_USERNAME, "") as String
//
//    fun getNickname(context: Context): String = getPreferences(context).getString(KEY_USER_NICKNAME, "") as String
//
//    fun updateNickname(context: Context, nickname: String) {
//        getPreferences(context)[KEY_USER_NICKNAME] = nickname
//    }
//
//    fun getUserCityId(context: Context): Int = getPreferences(context).getInt(KEY_USER_CITY_ID, DEFAULT_CITY_ID)
//
//    fun getUserId(context: Context): String = getPreferences(context).getString(KEY_USER_ID, "") as String
//
//    fun saveUserId(context: Context): String = getPreferences(context).getString(KEY_USER_ID, "") as String
//
//    fun getUserName(context: Context): String = getPreferences(context).getString(KEY_PROFILE_NAME, "") as String
//
//    fun getUserLastName(context: Context): String = getPreferences(context).getString(KEY_PROFILE_LASTNAME, "") as String
//
//    fun getUserBirthday(context: Context): String = getPreferences(context).getString(KEY_PROFILE_BIRTHDAY, "") as String
//
//    fun getUserPhone(context: Context): String = getPreferences(context).getString(KEY_PROFILE_PHONE, "") as String
//
//    fun getUserEmail(context: Context): String = getPreferences(context).getString(KEY_PROFILE_EMAIL, "") as String
//
//    fun getUserGender(context: Context): String = getPreferences(context).getString(KEY_PROFILE_GENDER, "") as String
//
//    fun getUserHideUsername(context: Context): Boolean = getPreferences(context).getBoolean(KEY_PROFILE_HIDE_USERNAME, false)
//
//    fun getVipMemberStatus(context: Context) = getPreferences(context).getBoolean(KEY_IS_USER_VIP_MEMBER, false)
//
//    fun saveVipmembershipDetails(context: Context, vipUser: VipMemberUserInfoResponse) {
//        val userVip = Gson().toJson(vipUser)
//        getPreferences(context)[KEY_USER_VIP_MEMBERSHIP_DETAILS] = userVip
//        getPreferences(context)[KEY_IS_USER_VIP_MEMBER] = true
//    }
//
//    fun getVipMembershipDetails(context: Context): VipMemberUserInfoResponse? {
//
//        if(getVipMemberStatus(context)){
//            val jsonUserVip = getPreferences(context).get(KEY_USER_VIP_MEMBERSHIP_DETAILS, "") as String?
//            return if (jsonUserVip.isNullOrEmpty()) {
//                null
//            } else {
//                Gson().fromJson(jsonUserVip, VipMemberUserInfoResponse::class.java)
//            }
//        }
//        else{
//            return null
//        }
//    }
//
//    fun saveUserCity(context: Context, city: City) {
//        getPreferences(context)[KEY_MENU_ITEM_CITY] = city.name
//        getPreferences(context)[KEY_MENU_ITEM_CITY_ID] = city.id
//    }
//
//    fun saveUserCountry(context: Context, supportedCountry: SupportedCountry) {
//        getPreferences(context)[KEY_MENU_ITEM_COUNTRY] = supportedCountry.name
//        getPreferences(context)[KEY_MENU_ITEM_COUNTRY_ISO] = supportedCountry.iso
//    }
}
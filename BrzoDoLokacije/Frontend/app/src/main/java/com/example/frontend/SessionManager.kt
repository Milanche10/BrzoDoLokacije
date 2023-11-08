package com.example.frontend

import android.content.Context
import android.content.SharedPreferences

class SessionManager(context: Context?) {
    //SharedPref mode
    val PRIVATE_MODE = 0

    //SharedPref FileName
    private val PREF_NAME = "SharedPreferences"
    private val IS_LOGIN = "is_login"

    val pref: SharedPreferences? = context?.getSharedPreferences(PREF_NAME, PRIVATE_MODE)
    val editor: SharedPreferences.Editor? = pref?.edit()

    fun setLogin(isLogin: Boolean) {
        editor?.putBoolean(IS_LOGIN, isLogin)
        editor?.commit()
    }

    fun setToken(token: String?) {
        editor?.putString("token", token)
        editor?.commit()
    }

    fun setEmail(email: String?) {
        editor?.putString("email", email)
        editor?.commit()
    }

    fun setFirstname(firstname: String?) {
        editor?.putString("firstname", firstname)
        editor?.commit()
    }

    fun setLastname(lastname: String?) {
        editor?.putString("lastname", lastname)
        editor?.commit()
    }

    fun setId(id: Long?) {
        if (id != null) {
            editor?.putLong("id", id)
        }
        editor?.commit()
    }

    fun setPostId(id: Long?) {
        if (id != null) {
            editor?.putLong("postId", id)
        }
        editor?.commit()
    }

    fun setLocationId(id: Long?) {
        if (id != null) {
            editor?.putLong("locationId", id)
        }
        editor?.commit()
    }

    fun setLocationIdFromMap(id: Int?) {
        if (id != null) {
            editor?.putInt("locationIdFromMap", id)
        }
        editor?.commit()
    }

    fun setLocationNameFromMap(name: String?) {
        editor?.putString("locationNameFromMap", name)
        editor?.commit()
    }

    fun setPostIdInt(id: Int?) {
        if (id != null) {
            editor?.putInt("postId", id)
        }
        editor?.commit()
    }

    fun setUserIdFromPost(id: Long?) {
        if (id != null) {
            editor?.putLong("useridfrompost", id)
        }
        editor?.commit()
    }

    fun setUserEmailFromPost(email: String?) {
        if (email != "") {
            editor?.putString("useremailfrompost", email)
        }
        editor?.commit()
    }

    fun isLogin(): Boolean? {
        return pref?.getBoolean(IS_LOGIN, false)
    }

    fun getToken(): String? {
        return pref?.getString("token", "")
    }

    fun getEmail(): String? {
        return pref?.getString("email", "")
    }

    fun getFirstname(): String? {
        return pref?.getString("firstname", "")
    }

    fun getLastname(): String? {
        return pref?.getString("lastname", "")
    }

    fun getId(): Long? {
        var t = getToken();
        return pref?.getLong("id", 0)
    }

    fun getPostId(): Long? {
        var t = getToken();
        return pref?.getLong("postId", 0)
    }

    fun getLocationId(): Long?{

        var t = getToken()
        return pref?.getLong("locationId",0)
    }

    fun getLocationIdFromMap(): Int?{

        var t = getToken()
        return pref?.getInt("locationIdFromMap",-1)
    }

    fun getLocationNameFromMap(): String?{

        var t = getToken()
        return pref?.getString("locationNameFromMap", "")
    }

    fun getPostIdInt() : Int? {
        var t = getToken();
        return pref?.getInt("postId",0)
    }

    fun getUserIdFromPost() : Long? {
        var t = getToken();
        return pref?.getLong("useridfrompost",0)
    }

    fun getUserEmailFromPost() : String? {
        var t = getToken();
        return pref?.getString("useremailfrompost", "")
    }

    fun removeData() {
        editor?.clear()
        editor?.commit()
    }

}
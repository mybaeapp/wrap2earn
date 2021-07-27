package com.wraptwoearn.util

import android.content.Context

class PrefrenceHelper(internal var _context: Context) {

    internal var pref: android.content.SharedPreferences
    internal var editor: android.content.SharedPreferences.Editor

    var token: String
        get() = pref!!.getString(TOKEN, "")!!
        set(token) {
            editor.putString(TOKEN, token)
            editor.commit()

        }

    var loginDate: String
        get() = pref.getString(LOGIN_DATE, "")!!
        set(loginDate) {
            editor.putString(LOGIN_DATE, loginDate)
            editor.commit()
        }

    var app_runFirstShowcase_assist: String?
        get() = pref.getString(ShowCaseView, "FIRST")
        set(App_runFirst) {
            editor.remove(ShowCaseView)
            editor.putString(ShowCaseView, App_runFirst)
            editor.commit()
        }


    var app_runFirstShowcase_fandb: String?
        get() = pref.getString(ShowCaseViewfandb, "FIRST")
        set(App_runFirst) {
            editor.remove(ShowCaseViewfandb)
            editor.putString(ShowCaseViewfandb, App_runFirst)
            editor.commit()
        }


    var app_runProduct: String?
        get() = pref.getString(FIRST_TIME_PRODUCTS, "FIRST")
        set(App_runProduct) {
            editor.remove(FIRST_TIME_PRODUCTS)
            editor.putString(FIRST_TIME_PRODUCTS, App_runProduct)
            editor.commit()
        }

    var app_refresh: String?
        get() = pref.getString(REFRESH_PRODUCTS, "FIRST")
        set(App_refreshdata) {
            editor.remove(REFRESH_PRODUCTS)
            editor.putString(REFRESH_PRODUCTS, App_refreshdata)
            editor.commit()
        }

    var loginStatus: String?
        get() = pref.getString(LOGIN_STATUS, "FALSE")
        set(LoginStatus) {
            editor.remove(LOGIN_STATUS)
            editor.putString(LOGIN_STATUS, LoginStatus)
            editor.commit()
        }

    var email: String
        get() = pref!!.getString(EMAIL, "")!!
        set(Email) {
            editor.putString(EMAIL, Email)
            editor.commit()

        }

    var password: String
        get() = pref!!.getString(PASSWORD, "")!!
        set(Password) {
            editor.putString(PASSWORD, Password)
            editor.commit()

        }


    var cartcount: Int
        get() = pref.getInt(CARTCOUNT, 0)
        set(cartcount) {
            editor.putInt(CARTCOUNT, cartcount)
            editor.commit()

        }


    var draftcount: Int
        get() = pref.getInt(DRAFTCOUNT, 0)
        set(draftCount) {
            editor.putInt(DRAFTCOUNT, draftCount)
            editor.commit()

        }

    var language: String?
        get() = pref.getString(LOCALE_LANGUAGE, "en")
        set(language) {
            editor.remove(LOCALE_LANGUAGE)
            editor.putString(LOCALE_LANGUAGE, language)
            editor.commit()
        }

    var latlong_info: Boolean?
        get() = pref.getBoolean(LATLONG, false)
        set(latlong_info) {
            editor.remove(LATLONG)
            editor.putBoolean(LATLONG, latlong_info!!)
            editor.commit()
        }

    var latPos: String?
        get() = pref.getString(LATPOS, "0.0")
        set(lat_info) {
            editor.remove(LATPOS)
            editor.putString(LATPOS, lat_info)
            editor.commit()
        }

    var langPos: String?
        get() = pref.getString(LONGPOS, "0.0")
        set(long_info) {
            editor.remove(LONGPOS)
            editor.putString(LONGPOS, long_info)
            editor.commit()
        }

    var clientID: Int
        get() = pref.getInt(CLIENTID, 0)
        set(clientID) {
            editor.putInt(CLIENTID, clientID)
            editor.commit()

        }

    var scratch_card_id: Int
        get() = pref.getInt(SCRATCHCARDID, 0)
        set(scratchID) {
            editor.putInt(SCRATCHCARDID, scratchID)
            editor.commit()

        }

    var businessId: Int
        get() = pref.getInt(BUSINESS_ID, 0)
        set(businessID) {
            editor.putInt(BUSINESS_ID, businessID)
            editor.commit()

        }

    var businessName: String
        get() = pref.getString(BUSINESS_NAME, "")!!
        set(businessName) {
            editor.putString(BUSINESS_NAME, businessName)
            editor.commit()
        }

    var unassigned: String
        get() = pref.getString(UNASSIGNED, "")!!
        set(unassigned) {
            editor.putString(UNASSIGNED, unassigned)
            editor.commit()
        }

    var accepted: String
        get() = pref.getString(ACCEPTED, "")!!
        set(accepted) {
            editor.putString(ACCEPTED, accepted)
            editor.commit()
        }

    var delivered: String
        get() = pref.getString(DELIVERED, "")!!
        set(delivered) {
            editor.putString(DELIVERED, delivered)
            editor.commit()
        }


    var dispathed: String
        get() = pref.getString(DISPATCHED, "")!!
        set(dispathed) {
            editor.putString(DISPATCHED, dispathed)
            editor.commit()
        }

    var address: String?
        get() = pref.getString(ADDRESS, "")
        set(caddress) {
            editor.putString(ADDRESS, caddress)
            editor.commit()

        }

    val moblie: String?
        get() = pref.getString(MOBLIE, "")

    var firebaseToken: String?
        get() = pref.getString(FIREBASE_TOKEN, "")
        set(firebasetoken) {
            editor.putString(FIREBASE_TOKEN, firebasetoken)
            editor.commit()

        }


    init {
        pref = _context.getSharedPreferences(PREF_NAME, 0)
        editor = pref.edit()

    }

    fun setMobile(cmobile: String) {
        editor.putString(MOBLIE, cmobile)
        editor.commit()

    }

    companion object {
        private val PREF_NAME = "testing"
        val KEY_SET_APP_RUN_FIRST_TIME = "KEY_SET_APP_RUN_FIRST_TIME"
        val LOGIN_STATUS = "LOGIN_STATUS"
        val EMAIL = "email"
        val PASSWORD = "password"
        val CARTCOUNT = "cartCount"
        val DRAFTCOUNT = "draftCount"
        val FIRST_TIME_PRODUCTS = "KEY_SET_PRODUCT_FIRST_TIME"
        val REFRESH_PRODUCTS = "REFRESH_PRODUCTS"
        val CART_COUNTER = "SET_CART_COUNT"
        val LOCALE_LANGUAGE = "KEY_LOCALE_LANGUAGE"
        val LATLONG = "latlong"
        val LATPOS = "key_lat"
        val LONGPOS = "key_long"
        val CLIENTID = "ClientID"
        val ADDRESS = "caddress"
        val MOBLIE = "cmobile"
        val FIREBASE_TOKEN = "firebasetoken"

        val UNASSIGNED = "unassigned"
        val ACCEPTED = "accepted"
        val DISPATCHED = "dispatched"
        val DELIVERED = "delivered"
        val SCRATCHCARDID = "scratch_card_id"

        val TOKEN = "BearerToken"
        val BUSINESS_ID = "business_id"
        val BUSINESS_NAME = "business_name"
        val LOGIN_DATE = "logindate"
        val ShowCaseView = "assist"
        val ShowCaseViewexplore = "explore"
        val ShowCaseViewfandb = "fandb"
        val ShowCaseViewbusiness: Boolean? = false
    }

}
package com.wraptwoearn.util

import java.util.*

class Constatns {

    companion object {


        var CAT_ID: String = "1"
        var CAT_NAME: String = ""

        val BASE_URL: String = "https://wrap2earn.com/in/api/"

        val BASE_URL_V2: String = "https://admin.baewithyou.com/api/v2/"

        val BASE_URL_STAGING_V2: String = "http://baetest.xyz/api/v2/"

        val BASE_URL_STAGING_: String = "http://baetest.xyz/api/"

        val BASE_URL_LOCATION = "https://maps.googleapis.com/maps/api/"
        var CURRENT_ADDRESS = ""
        var BUISINESS_ADDRESS = ""

        var ISAPPLYALLCASHBACK = false

        var BUSINESS_LAT: Double = 0.0
        var BUSIESS_LONG: Double = 0.0
        var CartItemCount: Int = 0
        var isClickShare: Boolean = false
        var isClickShareOrder: Boolean = false


        val uniqueId = UUID.randomUUID().toString()


        var lattitude: Double = 0.0//18.5362
        var longitude: Double = 0.0//73.8940
        var latitude_dynamic: Double = 0.0//73.8940
        var longitude_dynamic: Double = 0.0//73.8940

        val default_logo = "https://admin.baewithyou.com/storage/default-logo.png"
        val default_logo_cover_nearby =
            "https://admin.baewithyou.com/storage/default-cover-logo.jpg"

        val LOCATION_REQUEST = 100
        val READ_CONTACT_REQUEST = 102
        val GPS_REQUEST = 101
        val USER_MESSAGE = "Bae"
        var ADDRESS = ""
        var CITY_NAME = ""

        val ALARM_SET = "alarm.set"
        val ALARM_SET_INTENT = "#alarm.set.intent_action"
        val ALARM_CHANGE = "alarm.change"
        val ALARM_CHANGE_INTENT = "#alarm.change.intent_action"
        val ALARM_CHECK = "alarm.check"
        val ALARM_CHECK_INTENT = "#alarm.check.intent_action"
        val ADD_REMINDER = "reminders.add"
        val ADD_REMINDER_LOCATION = "reminders.location_based.add"
        val REMINDER_GET = "reminders.get"

        //val REMINDER_RENAME = "reminders.rename"
        val REMINDER_ANOTHER_REMINDER = "#reminders.intent_action_another_one"

        // val REMINDER_RECURRENCE = "#reminders.intent_action_recurrence"
        // val REMINDER_RESCHEDULE = "reminders.reschedule"
        val CUSTOM_SEARCH = "custom.search"
        val CUSTOM_SEARCH_INTENT = "#custom.search.intent_action"
        val NEWS_SEARCH = "news.search"
        val NEWS_SEARCH_INTENT_ACTION = "#news.search.intent_action"
        val MAKE_CALL = "make.call"
        val MESSAGE = "message"
        val BUSINESS_SEARCH = "business.search"
        val BUSINESS_SEARCH_INTENT = "#business.search.intent_action"
        val BUSINESS_SEARCH_OPEN = "business.search.open"
        val MOVIE_SEARCH = "movie.search"
        val MOVIE_SEARCH_INTENT = "#movie.search.intent_action"
        val OFFER_SEARCH = "offer.search"
        val OFFER_SEARCH_INTENT = "#offer.search.intent_action"
        val START_NAVIGAION = "start.navigation"
        val TIME_NAVIGATION = "time.navigation"
        val DISTANCE_NAVIGATION = "distance.navigation"
        val MAPS_CURRENT_LOCATION = "maps.current.location"
        val YOUTUBE_VIDEO = "youtube.videos"
        val PRODUCT_SEARCH = "product.search"
        val PRODUCT_SEARCH_INTENT = "#product.search.intent_action"
        val WEATHER_CONDITION = "weather.condition"
        val WEATHER_CONDITION_CONDITION = "wweathercondition.weathercondition-condition"
        val WEATHER_TEMPRATURE = "weather.temperature"
        val WEATHER_TEMPRATURE_TEMP = "weathertemperature.weathertemperature-temperature"
        val WHAT_CAN_I_DO = "Whatcanyodo"
        val BRANDS = "brands"
        val MORE_BRANDS = "brands.brands-more"
        val COLLECTION = "collections"
        val COLLECTION_MORE = "collections.collections-more"
        val CUSTOM_SEARCH_PHOTO = "custom.search.photo"
        val SAERCH_SUBJECT = "search.subject"


        var status = ""


        //query

        val TURN_OFF_ALARM = "turn off the alarm for "

        const val VIEW_TYPE_ITEM = 0
        const val VIEW_TYPE_LOADING = 1

    }
}
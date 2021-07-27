package com.wraptwoearn.appcontroller

import android.app.Application
import android.content.Context

/**
 * Created by Hakim on 10/6/2017.
 */
class AppController : Application() {

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
    }


    override fun onCreate() {
        instance = this
        super.onCreate()
        //  VisionManager.init(this, getResources().getString(R.string.mapbox_access_token));


    }


    companion object {

        lateinit var instance: AppController
            private set

        val context: Context
            get() = instance

    }
}
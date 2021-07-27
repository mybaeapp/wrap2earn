package com.wraptwoearn.remote

import android.content.Context
import com.google.gson.GsonBuilder
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory.*
import com.wraptwoearn.util.Constatns
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.concurrent.TimeUnit

class ApiServices {
    companion object Factory {
        var gson = GsonBuilder().setLenient().create()
        fun create(cotext: Context): ApiHelper {
            val interceptor = HttpLoggingInterceptor()
            interceptor.level = HttpLoggingInterceptor.Level.BODY
            val client = OkHttpClient.Builder()
                .addInterceptor(Interceptor { chain ->
                    val original = chain.request()
                    val request = original.newBuilder()
                        .method(original.method(), original.body())
                        .build()
                    return@Interceptor chain.proceed(request)

                })

                .addInterceptor(interceptor)
                .connectTimeout(100, TimeUnit.SECONDS)
                .readTimeout(100, TimeUnit.SECONDS)
                .build()


            val retrofit = Retrofit.Builder()
                .addCallAdapterFactory(create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(client)
                .baseUrl(Constatns.BASE_URL)
                .build()

            return retrofit.create(ApiHelper::class.java)
        }

        @Volatile
        private var instance: ApiServices? = null

        fun getApiClient(): ApiServices = instance ?: ApiServices()
    }

    private val mRetrofit: Retrofit = Retrofit.Builder()

        .addConverterFactory(ScalarsConverterFactory.create())
        .addConverterFactory(MoshiConverterFactory.create())
        .baseUrl(Constatns.BASE_URL_LOCATION)
        .build()

    private val api: ApiHelper

    init {
        api = mRetrofit.create(ApiHelper::class.java)
    }

    fun getApi(): ApiHelper = api


}

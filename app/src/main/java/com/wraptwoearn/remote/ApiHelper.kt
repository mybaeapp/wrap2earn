package com.wraptwoearn.remote


import com.wraptwoearn.model.City
import com.wraptwoearn.model.Loginresponse
import com.wraptwoearn.model.SaveLocation
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiHelper {

    @POST("driverRegisterApp")
    fun register(
        @Query("carnumber") carnumber: String?,
        @Query("mobilenumber") mobilenumber: String?,
        @Query("fname") fname: String?,
        @Query("lname") lname: String?,
        @Query("imei") imei_: String?,
        @Query("password") password: String,
        @Query("city") city: String
    ): Call<Loginresponse>

    @POST("updateTripCurrentLocationNewImei")
    fun saveLocation(
        @Query("userId") userId: String?,
        @Query("latitude") latitude: String?,
        @Query("longitude") longitude: String?,
        @Query("address") address: String?,
        @Query("country") country: String?,
        @Query("area") area: String,
        @Query("accuracy") accuracy: String,
        @Query("speed") speed: String,
        @Query("imei") imei: String
    ): Call<SaveLocation>


    @POST("driverLoginApp")
    fun login(
        @Query("carnumber") clientId: String?,
        @Query("password") userName: String,
        @Query("imei") password: String
    ): Call<Loginresponse>


    //location Search


    @GET(value = "getCityList")
    fun getPlaceDetails(): Call<City>

}
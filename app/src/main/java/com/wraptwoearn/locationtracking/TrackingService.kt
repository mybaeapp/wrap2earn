package com.wraptwoearn.locationtracking

import android.Manifest
import android.annotation.SuppressLint
import android.app.*
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.graphics.Color
import android.location.Address
import android.location.Location
import android.location.LocationManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import android.os.PowerManager
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.LocationListener
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.wraptwoearn.appcontroller.AppController
import com.wraptwoearn.model.SaveLocation
import com.wraptwoearn.remote.ApiServices
import com.wraptwoearn.util.PrefrenceHelper
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.*
import java.net.URLEncoder

class TrackingService : Service(), GoogleApiClient.ConnectionCallbacks,
    GoogleApiClient.OnConnectionFailedListener {

    private var country: String = "India";
    private var city: String = ""
    private var address: String = ""

    @Volatile
    private var isRunning = false

    private var currentLocation: Location? = null
    lateinit var addresses: List<Address>
    private var speed = ""

    private var googleApiClient: GoogleApiClient? = null
    private var locationRequest: LocationRequest? = null
    private var locationManager: LocationManager? = null

    private val SOUND_LEVEL_NOTIFICATION_ID = 73
    private val NO_CONNECTION_NOTIFICATION_ID = 74
    private val PERMISSION_NOT_GRANTED_NOTIFICATION_ID = 75
    private val BATTERY_SAVER_ON_NOTIFICATION_ID = 77
    private var CHANNEL_ID = "com.medium.foreground.channel"
    private var wakelock: PowerManager.WakeLock? = null
    internal var requestID = 10001
    private val fusedLocationProviderApi = LocationServices.FusedLocationApi
    private var thread: ContinousThread? = null

    private val SERVICE_INTERVAL: Long = 20000
    private val SERVICE_FASTEST: Long = 20000

    override fun onCreate() {
        super.onCreate()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForeground()
        }

        val pm = getSystemService(Context.POWER_SERVICE) as PowerManager
        wakelock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "medium:wakelock")

        thread = ContinousThread()

        init()
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        return START_NOT_STICKY
    }

    private fun init() {
        wakelock!!.acquire()

        if (!isRunning) {
            isRunning = true
            thread!!.start()
        }

        locationRequest = LocationRequest.create()
        locationRequest!!.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
        locationRequest!!.setInterval(SERVICE_INTERVAL)
        locationRequest!!.setFastestInterval(SERVICE_FASTEST)
        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager

        googleApiClient = GoogleApiClient.Builder(this)
            .addApi(LocationServices.API).addConnectionCallbacks(this)
            .addOnConnectionFailedListener(this).build()
        try {
            googleApiClient!!.connect()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private fun startForeground() {
        val notificationIntent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0)

        val channelName = "Medium"
        val chan = NotificationChannel(CHANNEL_ID, channelName, NotificationManager.IMPORTANCE_NONE)
        val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.createNotificationChannel(chan)

        val notificationBuilder = NotificationCompat.Builder(this, CHANNEL_ID)
        val notification = notificationBuilder.setOngoing(true)
            .setSmallIcon(R.drawable.logo)
            .setContentTitle("Wrap2earn")
            .setContentTitle("Wrap2earn campaign active")
            .setContentIntent(pendingIntent)
            .setPriority(NotificationManager.IMPORTANCE_NONE)
            .setCategory(Notification.CATEGORY_SERVICE)
            .build()
        startForeground(2, notification)
    }

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    //define the listener
    private val locationListener: LocationListener = object : LocationListener {
        override fun onLocationChanged(location: Location) {
            currentLocation = location
            Log.v("MediumApp", location.accuracy.toString())
        }
    }

    override fun onConnected(p0: Bundle?) {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) === PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) === PackageManager.PERMISSION_GRANTED
        ) {

            fusedLocationProviderApi!!.requestLocationUpdates(
                googleApiClient!!,
                locationRequest!!,
                locationListener
            )
        } else {
            showNotification(
                applicationContext, PERMISSION_NOT_GRANTED_NOTIFICATION_ID,
                "Location is off",
                "Could you please enable location permission for this app?"
            )
        }
    }

    override fun onConnectionSuspended(p0: Int) {

    }

    override fun onConnectionFailed(p0: ConnectionResult) {

    }

    private fun showNotification(
        context: Context,
        notificationId: Int,
        title: String,
        content: String
    ) {
        var mBuilder: NotificationCompat.Builder? = null
        val managerCompat =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        mBuilder = NotificationCompat.Builder(context)
        mBuilder.setContentTitle(title)
        mBuilder.setStyle(NotificationCompat.BigTextStyle().bigText(content))
        mBuilder.setLargeIcon(BitmapFactory.decodeResource(resources, R.mipmap.ic_launcher))
        mBuilder.setContentText(content)
        mBuilder.setChannelId(CHANNEL_ID)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel =
                NotificationChannel(CHANNEL_ID, "Medium", NotificationManager.IMPORTANCE_HIGH)
            try {
                managerCompat!!.createNotificationChannel(channel)
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }

        mBuilder.setAutoCancel(true)
            .setSmallIcon(R.mipmap.ic_launcher)
        mBuilder.setLights(Color.WHITE, 2000, 3000)
        mBuilder.setDefaults(
            Notification.DEFAULT_LIGHTS
                    or Notification.DEFAULT_VIBRATE or Notification.DEFAULT_SOUND
        )

        if (notificationId == PERMISSION_NOT_GRANTED_NOTIFICATION_ID) {
            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
            val uri = Uri.fromParts("package", packageName, null)
            intent.data = uri
            val pendingIntent = PendingIntent.getActivity(
                context, requestID,
                intent, 0
            )

            mBuilder.setContentIntent(pendingIntent)
        }

        managerCompat.notify(9, mBuilder.build())
    }

    internal inner class ContinousThread : Thread() {
        var i: Long = 0
        override fun run() {
            while (isRunning) {
                try {
                    sleep(20000)
                    sendLocationToServer()
                    i++


                    if (i % 4 == 0L) {
                        //locationCheck()
                    }

                    if (i % 20 == 0L) {
                        //connectivityCheck()
                    }

                } catch (e: InterruptedException) {
                    isRunning = false
                }

            }
        }

        @SuppressLint("MissingPermission")
        private fun getLastKnownLocation(): Location? {
            val providers = locationManager!!.getProviders(true)
            var bestLocation: Location? = null
            for (provider in providers) {
                val location = locationManager!!.getLastKnownLocation(provider) ?: continue

                if (bestLocation == null || location.accuracy < bestLocation.accuracy) {
                    bestLocation = location
                }
            }
            return bestLocation
        }

        private fun sendLocationToServer() {
            try {
//                val geocoder: Geocoder
//
//                geocoder = Geocoder(applicationContext, Locale.getDefault())
//
//                try {
//                    addresses = geocoder.getFromLocation(
//                        currentLocation?.latitude!!,
//                        currentLocation?.longitude!!,
//                        1
//                    )
//                    address =
//                        addresses[0].getAddressLine(0) // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
//
//                    city = addresses[0].getLocality()
//                    country = addresses[0].getCountryName()
//                    // Only if available else return NULL
//
//
//                } catch (e: java.lang.Exception) {
//
//                }

            } catch (e: Exception) {
                e.printStackTrace()
            }


            var user_id = PrefrenceHelper(AppController.context).scratch_card_id
            var imei = PrefrenceHelper(AppController.context).app_runFirstShowcase_fandb
            loginTrueCaller(
                user_id.toString(),
                currentLocation?.latitude.toString(),
                currentLocation?.longitude.toString(),
                address,
                country,
                city,
                currentLocation?.accuracy.toString(),
                currentLocation?.speed.toString(), imei.toString()
            )


//            val urlString = "https://wrap2earn.com/in/api/" + "updateTripCurrentLocationNewImei"
//            var response = ""
//            var urlConnection: HttpURLConnection? = null
//            try {
//                val url = URL(urlString)
//                urlConnection = url.openConnection() as HttpURLConnection
//                urlConnection.connectTimeout = 12000
//                urlConnection.requestMethod = "PUT"
//                urlConnection.doInput = true
//                urlConnection.doOutput = true
//
//                val entry1 = AbstractMap.SimpleEntry<String, String>(
//                    "lat",
//                    currentLocation?.latitude.toString()
//                )
//                val entry2 = AbstractMap.SimpleEntry<String, String>(
//                    "long",
//                    currentLocation?.longitude.toString()
//                )
//                val entry4 = AbstractMap.SimpleEntry<String, String>(
//                    "accuracy",
//                    currentLocation?.accuracy.toString()
//                )
//                val entry3 = AbstractMap.SimpleEntry<String, String>(
//                    "speed",
//                    currentLocation?.speed.toString()
//                )
//
//                val entry5 = AbstractMap.SimpleEntry<String, String>(
//                    "userId",
//                    user_id.toString()
//                )
//
//                val entry6 = AbstractMap.SimpleEntry<String, String>(
//                    "address",
//                    address
//                )
//
//                val entry7 = AbstractMap.SimpleEntry<String, String>(
//                    "country",
//                    country
//                )
//
//                val entry8 = AbstractMap.SimpleEntry<String, String>(
//                    "area",
//                    city
//                )
//
//                val entry9 = AbstractMap.SimpleEntry<String, String>(
//                    "imei",
//                    imei
//                )
//
//
//                val params = ArrayList<Map.Entry<String, String>>()
//                params.add(entry1)
//                params.add(entry2)
//                params.add(entry3)
//                params.add(entry4)
//                params.add(entry5)
//                params.add(entry6)
//                params.add(entry7)
//                params.add(entry8)
//                params.add(entry9)
//
//                val os = urlConnection.outputStream
//                val writer = BufferedWriter(
//                    OutputStreamWriter(os, "UTF-8")
//                )
//                writer.write(getQuery(params))
//                writer.flush()
//                writer.close()
//                os.close()
//
//                val responseCode = urlConnection.responseCode
//                if (responseCode == HttpsURLConnection.HTTP_OK) {
//
//                    val bufferedReader =
//                        BufferedReader(InputStreamReader(urlConnection.inputStream))
//                    var line = bufferedReader.readLine()
//                    while (line != null) {
//                        response += line
//                        line = bufferedReader.readLine()
//                    }
//
//                    Log.v("Medium", response)
//
//                } else if (responseCode == 401) {
//                    Log.v("Medium", "Login expired")
//                }
//            } catch (e: Exception) {
//                Log.v("Medium", e.localizedMessage)
//            } finally {
//                urlConnection?.disconnect()
//            }
        }

        fun loginTrueCaller(
            user_id: String,
            lat: String,
            long: String,
            address: String,
            country: String,
            area: String,
            accuracy: String,
            speed: String,
            imei: String

        ) {

            ApiServices.create(AppController.instance.applicationContext)
                .saveLocation(user_id, lat, long, address, country, area, accuracy, speed, imei)
                .enqueue(object : Callback<SaveLocation> {

                    override fun onResponse(
                        call: Call<SaveLocation>, response: Response<SaveLocation>
                    ) {
                        if (response.code() == 200) {

                            if (response.body()!!.status.equals("success")) {
                                MainActivity.compain.setText(response.body()!!.message)
                                MainActivity.image_view.setImageDrawable(
                                    getResources().getDrawable(
                                        R.drawable.success
                                    )
                                )
                            } else {
                                MainActivity.compain.setText(response.body()!!.message)
                                MainActivity.image_view.setImageDrawable(
                                    getResources().getDrawable(
                                        R.drawable.error
                                    )
                                )

                            }
                        } else if (response.code() == 401) {

                            Toast.makeText(
                                AppController.instance.applicationContext,
                                "Server Error",
                                Toast.LENGTH_LONG
                            ).show()
                        } else if (response.code() == 500) {

                        }

                    }

                    override fun onFailure(call: Call<SaveLocation>, t: Throwable) {
                        Toast.makeText(
                            AppController.instance.applicationContext,
                            t.toString(),
                            Toast.LENGTH_LONG
                        ).show()
                    }
                })

        }

        @Throws(UnsupportedEncodingException::class)
        private fun getQuery(params: List<Map.Entry<String, String>>): String {
            val result = StringBuilder()
            var first = true

            for (pair in params) {
                if (first)
                    first = false
                else
                    result.append("&")

                result.append(URLEncoder.encode(pair.key, "UTF-8"))
                result.append("=")
                result.append(URLEncoder.encode(pair.value, "UTF-8"))
            }

            return result.toString()
        }
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val serviceChannel = NotificationChannel(
                CHANNEL_ID, "Medium",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            val manager = getSystemService(NotificationManager::class.java)
            manager!!.createNotificationChannel(serviceChannel)
        }
    }

    fun stopService(context: Context) {
        val stopIntent = Intent(context, TrackingService::class.java)
        context.stopService(stopIntent)
    }
}
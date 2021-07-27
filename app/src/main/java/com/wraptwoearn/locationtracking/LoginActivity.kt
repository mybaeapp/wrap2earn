package com.wraptwoearn.locationtracking

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.telephony.TelephonyManager
import android.view.View
import android.view.View.GONE
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.wraptwoearn.appcontroller.AppController
import com.wraptwoearn.model.Loginresponse
import com.wraptwoearn.remote.ApiServices
import com.wraptwoearn.util.PrefrenceHelper
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_login.etpassword
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {

    private val TAG = "PermissionDemo"
    private val LOCATION_REQUEST_CODE = 101

    @SuppressLint("HardwareIds")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        setupPermissions()
        etImei.setText(Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID));
        PrefrenceHelper(AppController.context).app_runFirstShowcase_fandb =
            Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID)
    }

    private fun setupPermissions() {
        val permission = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
        )

        if (permission != PackageManager.PERMISSION_GRANTED) {
            requestLocationPermission()
        }
    }

    private fun requestLocationPermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
            LOCATION_REQUEST_CODE
        )
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            LOCATION_REQUEST_CODE -> {

                if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {


                } else {
                }
            }
        }
    }


    fun loginTrueCaller(
        carnumber: String,
        password: String,
        imei: String

    ) {

        ApiServices.create(AppController.instance.applicationContext)
            .login(carnumber, password, imei)
            .enqueue(object : Callback<Loginresponse> {

                override fun onResponse(
                    call: Call<Loginresponse>, response: Response<Loginresponse>
                ) {
                    if (response.body()!!.status.equals("success")) {
                        PrefrenceHelper(AppController.context).businessName =
                            response.body()!!.first_name
                        PrefrenceHelper(AppController.context).language =
                            response.body()!!.last_name
                        PrefrenceHelper(AppController.context).app_refresh =
                            response.body()!!.user_car_number
                        PrefrenceHelper(AppController.context).scratch_card_id =
                            response.body()!!.user_id

                        startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                        finish()

                    } else {
                        Toast.makeText(
                            AppController.instance.applicationContext,
                            "Please Enter Valid Credential",
                            Toast.LENGTH_LONG
                        ).show()
                        progress_bar.visibility = GONE
                    }

                }

                override fun onFailure(call: Call<Loginresponse>, t: Throwable) {
                    Toast.makeText(
                        AppController.instance.applicationContext, t.toString(), Toast.LENGTH_LONG
                    ).show()
                }
            })

    }

    fun onLogin(view: View) {
        if (et_carnuber_login.text.length == 0) {
            Toast.makeText(
                AppController.instance.applicationContext,
                "Please Enter Car number",
                Toast.LENGTH_LONG
            ).show()
        } else if (etpassword.text.length == 0) {
            Toast.makeText(
                AppController.instance.applicationContext,
                "Please Enter Password",
                Toast.LENGTH_LONG
            ).show()
        } else {
            loginTrueCaller(
                et_carnuber_login.text.toString(),
                etpassword.text.toString(),
                etImei.text.toString()
            )
        }


    }

    fun onRegisterClick(view: View) {
        startActivity(Intent(this@LoginActivity, RegistrationActivity::class.java))
        finish()
    }


    @SuppressLint("HardwareIds")
    private fun IMEI() {
        var myIMEI: String? = null
        try {
            val tm = getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
            val IMEI = tm.deviceId
            if (IMEI != null) {
                myIMEI = IMEI
                etImei.setText(myIMEI)
                PrefrenceHelper(AppController.context).app_runFirstShowcase_fandb =
                    myIMEI
            }
        } catch (ex: Exception) {
            Toast.makeText(this, ex.toString(), Toast.LENGTH_SHORT).show()

        }

        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_PHONE_STATE) !=
            PackageManager.PERMISSION_GRANTED
        ) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this,
                    android.Manifest.permission.READ_PHONE_STATE
                )
            ) {

            } else {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(android.Manifest.permission.READ_PHONE_STATE),
                    2
                )

            }
        }
    }

    fun OpenRegistrationSecond(view: View) {
        startActivity(Intent(this@LoginActivity, RegistrationActivity::class.java))
        finish()
    }


}
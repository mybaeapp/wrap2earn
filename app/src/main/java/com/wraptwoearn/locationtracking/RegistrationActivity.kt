package com.wraptwoearn.locationtracking

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.telephony.TelephonyManager
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.wraptwoearn.appcontroller.AppController
import com.wraptwoearn.model.CityItem
import com.wraptwoearn.model.Loginresponse
import com.wraptwoearn.remote.ApiServices
import com.wraptwoearn.util.PrefrenceHelper
import kotlinx.android.synthetic.main.activity_login.etpassword
import kotlinx.android.synthetic.main.activity_registration.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegistrationActivity : AppCompatActivity() {


    companion object {

        lateinit var edit_city: EditText

    }

    @SuppressLint("HardwareIds")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)
        edit_city = findViewById(R.id.etcity)
        etImei_r.setText(
            Settings.Secure.getString(
                getContentResolver(),
                Settings.Secure.ANDROID_ID
            )
        )
        PrefrenceHelper(AppController.context).app_runFirstShowcase_fandb =
            Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID)
    }

    override fun onResume() {
        super.onResume()

    }

    override fun onStart() {
        super.onStart()
    }

    fun loginTrueCaller(
        carnumber: String,
        mobilenumber: String,
        fname: String,
        lname: String,
        imei: String,
        password: String,
        city: String

    ) {

        ApiServices.create(AppController.instance.applicationContext)
            .register(carnumber, mobilenumber, fname, lname, imei, password, city)
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
                        startActivity(Intent(this@RegistrationActivity, MainActivity::class.java))
                        finish()

                    } else {

                        Toast.makeText(
                            AppController.instance.applicationContext,
                            "Please Enter Valid Credential",
                            Toast.LENGTH_LONG
                        ).show()
                    }

                }

                override fun onFailure(call: Call<Loginresponse>, t: Throwable) {
                    Toast.makeText(
                        AppController.instance.applicationContext, t.toString(), Toast.LENGTH_LONG
                    ).show()
                }
            })

    }

    fun showMenu() {
        val original = listOf(
            CityItem(
                "Mumbai / Thane"
            ),
            CityItem("Delhi / NCR"),
            CityItem("Bangalore"),
            CityItem("Pune"),
            CityItem("Chennai"),
            CityItem("Kolkata"),
            CityItem("Hyderabad"),
            CityItem("Ahmedabad"),
            CityItem("Surat"),
            CityItem("Jaipur"),
            CityItem("Lucknow"),
            CityItem("Indore"),
            CityItem("Kanpur"),
            CityItem("Nagpur"),
            CityItem("Guwahati"),
            CityItem("Chandigarh"),
            CityItem("Bhopal"),
            CityItem("Patna"),
            CityItem("Bhubaneswar"),
            CityItem("Dehradun"),
            CityItem("Kochi"),
            CityItem("Jodhpur"),
            CityItem("Raipur"),
            CityItem("Bilaspur"),
            CityItem("Vizag"),
            CityItem("Vijaywada"),
            CityItem("Nellore"),
            CityItem("Dhaka"),
            CityItem("Agra"),
            CityItem("Varanasi")
        )
        CustomDialogFragment().apply {
            show(supportFragmentManager, CustomDialogFragment.TAG)
        }
    }

    fun onRegistration(view: View) {

        if (et_carnumber_registration.text.length == 0) {
            Toast.makeText(
                AppController.instance.applicationContext,
                "Please Enter Car number",
                Toast.LENGTH_LONG
            ).show()
        } else if (et_mobile_number.text.length == 0) {
            Toast.makeText(
                AppController.instance.applicationContext,
                "Please Enter Mobile Number",
                Toast.LENGTH_LONG
            ).show()
        } else if (et_first_name.text.length == 0) {
            Toast.makeText(
                AppController.instance.applicationContext,
                "Please Enter First name",
                Toast.LENGTH_LONG
            ).show()
        } else if (et_lastname.text.length == 0) {
            Toast.makeText(
                AppController.instance.applicationContext,
                "Please Enter Password",
                Toast.LENGTH_LONG
            ).show()
        } else if (etpassword.text.length == 0) {
            Toast.makeText(
                AppController.instance.applicationContext,
                "Please Enter Password",
                Toast.LENGTH_LONG
            ).show()
        } else if (etcity.text.length == 0) {
            Toast.makeText(
                AppController.instance.applicationContext,
                "Please Enter City",
                Toast.LENGTH_LONG
            ).show()
        } else {
            loginTrueCaller(
                et_carnumber_registration.text.toString(),
                et_mobile_number.text.toString(),
                et_first_name.text.toString(),
                et_lastname.text.toString(),
                etImei_r.text.toString(),
                etpassword.text.toString(),
                etcity.text.toString()
            );
        }


    }

    fun openCityDialog(view: View) {
        showMenu()
    }


    @SuppressLint("HardwareIds")
    private fun IMEI() {
        var myIMEI: String? = null
        try {
            val telephonyManager = getSystemService(Context.TELEPHONY_SERVICE) as
                    TelephonyManager
            val IMEI: String = telephonyManager.deviceId
            if (IMEI != null) {
                myIMEI = IMEI

                print(telephonyManager.deviceId)
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

    fun OpenLoginPage(view: View) {
        startActivity(Intent(this@RegistrationActivity, LoginActivity::class.java))
        finish()

    }

    fun OpenLoginPagetSecond(view: View) {
        startActivity(Intent(this@RegistrationActivity, LoginActivity::class.java))
        finish()
    }
}
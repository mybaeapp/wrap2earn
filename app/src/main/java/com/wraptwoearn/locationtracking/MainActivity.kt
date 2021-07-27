package com.wraptwoearn.locationtracking

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.wraptwoearn.appcontroller.AppController
import com.wraptwoearn.appcontroller.AppController.Companion.context
import com.wraptwoearn.util.PrefrenceHelper
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    private val TAG = "PermissionDemo"
    private val LOCATION_REQUEST_CODE = 101
    private var CHANNEL_ID = "com.medium.foreground.channel"

    companion object {

        lateinit var compain: TextView
        lateinit var image_view: CircleImageView

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        compain = findViewById(R.id.startButton)
        image_view = findViewById(R.id.imge_error_success)
        getVersionNme();
        setUpName()

        setupPermissions()

        ContextCompat.startForegroundService(
            this,
            Intent(this@MainActivity, TrackingService::class.java)
        )

    }

    private fun setUpName() {
        first_name.setText(PrefrenceHelper(AppController.context).businessName)
        last_name.setText(PrefrenceHelper(AppController.context).language)
        car_number.setText("(" + PrefrenceHelper(AppController.context).app_refresh + ")")
        user_id.setText(PrefrenceHelper(AppController.context).scratch_card_id.toString())
    }

    fun getVersionNme() {
        try {
            val pInfo =
                context.packageManager.getPackageInfo(context.packageName, 0)
            val version = pInfo.versionName
            version_name.setText("Version Name " + version.toString())
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
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
                    ContextCompat.startForegroundService(
                        this,
                        Intent(this@MainActivity, TrackingService::class.java)
                    )
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here.
        val id = item.getItemId()

        if (id == R.id.action_one) {
            stopService(context)
            PrefrenceHelper(AppController.instance.applicationContext).scratch_card_id = 0
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
            return true
        }

        return super.onOptionsItemSelected(item)

    }

    fun onOpenPage(view: View) {
        startActivity(Intent(this@MainActivity, WebView::class.java))

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

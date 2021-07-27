package com.wraptwoearn.locationtracking

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.WindowManager
import com.wraptwoearn.appcontroller.AppController
import com.wraptwoearn.util.PrefrenceHelper

class SplashScreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        // we used the postDelayed(Runnable, time) method
        // to send a message with a delayed time.
        Handler().postDelayed({
            if (PrefrenceHelper(AppController.context).scratch_card_id != 0) {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }

        }, 3000) // 3000 is the delayed time in milliseconds.

    }
}
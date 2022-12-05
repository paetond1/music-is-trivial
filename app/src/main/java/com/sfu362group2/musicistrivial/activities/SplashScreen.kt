package com.sfu362group2.musicistrivial.activities

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.sfu362group2.musicistrivial.R

// TODO: Migrate to proper SplashScreen https://developer.android.com/reference/kotlin/androidx/core/splashscreen/SplashScreen

class SplashScreen : AppCompatActivity() {

    private lateinit var handler: Handler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        handler = Handler(Looper.getMainLooper())
        handler.postDelayed({
            val i = Intent(this, MainActivity::class.java)
            startActivity(i)
            finish()
        },1000)

    }
}
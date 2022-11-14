package com.sfu362group2.musicistrivial.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.sfu362group2.musicistrivial.R
import com.sfu362group2.musicistrivial.activities.SignInActivity.Companion.EXTRA_NAME

class GoogleSignInActivity : AppCompatActivity() {
    private lateinit var tv:TextView
    private lateinit var signOutbtn:Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_google_sign_in)

        tv = findViewById(R.id.tv_name)
        signOutbtn =findViewById(R.id.sign_out_button)

        tv.text = intent.getStringExtra(EXTRA_NAME)

        signOutbtn.setOnClickListener {
            Firebase.auth.signOut()

            val intent = Intent(applicationContext, MainActivity::class.java)
            startActivity(intent)
        }

    }

}
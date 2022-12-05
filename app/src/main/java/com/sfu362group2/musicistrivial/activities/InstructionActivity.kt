package com.sfu362group2.musicistrivial.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.sfu362group2.musicistrivial.R

class InstructionActivity : AppCompatActivity() {

    private lateinit var homeButton: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_instruction)

        homeButton = findViewById(R.id.home_button)
        homeButton.setOnClickListener {
            this.finish()
        }
    }
}
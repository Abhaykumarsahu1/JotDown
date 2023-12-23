package com.example.jotdown

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler

class Splashactivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splashactivity)
        Handler().postDelayed({
            startActivity(Intent(this,MainActivity::class.java))
            finish()
        },1300)

    }
}
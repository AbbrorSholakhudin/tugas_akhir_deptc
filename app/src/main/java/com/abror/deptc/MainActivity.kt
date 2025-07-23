package com.abror.deptc

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main) // Layout splash screen

        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(Intent(this, TransitDeteksiActivity::class.java))
            finish()
        }, 2000) // 2000 ms = 2 detik
    }
}

package com.abror.deptc

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
class homeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val btnDeteksi = findViewById<Button>(R.id.btnDeteksi)
        val btnContoh = findViewById<Button>(R.id.btnContoh)

        btnDeteksi.setOnClickListener {
            val intent = Intent(this, deteksiActivity::class.java)
            startActivity(intent)
        }

        btnContoh.setOnClickListener {
            val intent = Intent(this, contohgambarActivity::class.java)
            startActivity(intent)
        }
    }
}

package com.abror.deptc

import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class contohgambarActivity: AppCompatActivity()  {
    // Info penyakit dan pencegahannya
    val penyakitInfo = mapOf(
        "healthy" to Pair("Daun Sehat (Normal)", "Tidak perlu tindakan khusus."),
        "leaf_curl" to Pair("Daun Melengkung", "Gunakan insektisida & pilih benih tahan penyakit."),
        "leaf_spot" to Pair("Bercak Daun", "Pangkas daun terinfeksi dan semprot fungisida."),
        "whitefly" to Pair("Whitefly (Kutu Putih)", "Gunakan perangkap kuning dan insektisida."),
        "yellowish" to Pair("Kuning Daun", "Perbaiki irigasi dan semprot anti virus.")
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contohgambar) // Layout splash screen
        val imageHealthy = findViewById<ImageView>(R.id.imageHealthy)
        val checkboxHealthy = findViewById<CheckBox>(R.id.checkboxHealthy)

        val resultContainer = findViewById<LinearLayout>(R.id.resultContainer)
        val diseaseText = findViewById<TextView>(R.id.textDisease)
        val preventionText = findViewById<TextView>(R.id.textPrevention)
        val btnCloseResult = findViewById<Button>(R.id.btnCloseResult)

        fun showDiseaseInfo(key: String) {
            val (label, prevention) = penyakitInfo[key] ?: return
            checkboxHealthy.isChecked = true // centang otomatis

            diseaseText.text = "Penyakit : $label"
            preventionText.text = "Pencegahan : $prevention"

            val slideUp = AnimationUtils.loadAnimation(this, R.anim.slide_up)
            resultContainer.visibility = View.VISIBLE
            resultContainer.startAnimation(slideUp)
        }

        imageHealthy.setOnClickListener {
            showDiseaseInfo("healthy")
        }
        btnCloseResult.setOnClickListener {
            resultContainer.visibility = View.GONE
        }

    }
}
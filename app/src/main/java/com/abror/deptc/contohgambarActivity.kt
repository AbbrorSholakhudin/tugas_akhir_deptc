package com.abror.deptc

import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class contohgambarActivity : AppCompatActivity() {

    val penyakitInfo = mapOf(
        "healthy" to Pair("Daun Sehat (Normal)", "Tidak perlu tindakan khusus."),
        "leaf_curl" to Pair("Daun Melengkung", "Gunakan insektisida & pilih benih tahan penyakit."),
        "leaf_spot" to Pair("Bercak Daun", "Pangkas daun terinfeksi dan semprot fungisida."),
        "whitefly" to Pair("Whitefly (Kutu Putih)", "Gunakan perangkap kuning dan insektisida."),
        "yellowish" to Pair("Kuning Daun", "Perbaiki irigasi dan semprot anti virus.")
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contohgambar)

        val resultContainer = findViewById<LinearLayout>(R.id.resultContainer)
        val diseaseText = findViewById<TextView>(R.id.textDisease)
        val preventionText = findViewById<TextView>(R.id.textPrevention)
        val btnCloseResult = findViewById<Button>(R.id.btnCloseResult)

        // Gambar + CheckBox
        val imageHealthy = findViewById<ImageView>(R.id.imageHealthy)
        val cbHealthy = findViewById<CheckBox>(R.id.checkboxHealthy)

        val imageLeafCurl = findViewById<ImageView>(R.id.imageLeafCurl)
        val cbLeafCurl = findViewById<CheckBox>(R.id.checkboxLeafCurl)

        val imageLeafSpot = findViewById<ImageView>(R.id.imageLeafSpot)
        val cbLeafSpot = findViewById<CheckBox>(R.id.checkboxLeafSpot)

        val imageWhitefly = findViewById<ImageView>(R.id.imageWhitefly)
        val cbWhitefly = findViewById<CheckBox>(R.id.checkboxWhitefly)

        val imageYellowish = findViewById<ImageView>(R.id.imageYellowish)
        val cbYellowish = findViewById<CheckBox>(R.id.checkboxYellowish)

        val checkboxes = listOf(cbHealthy, cbLeafCurl, cbLeafSpot, cbWhitefly, cbYellowish)

        fun showDiseaseInfo(key: String, selectedCheckBox: CheckBox) {
            val (label, prevention) = penyakitInfo[key] ?: return

            // Uncheck semua dulu
            checkboxes.forEach { it.isChecked = false }

            // Check yang dipilih
            selectedCheckBox.isChecked = true

            // Tampilkan info
            diseaseText.text = "Penyakit : $label"
            preventionText.text = "Pencegahan : $prevention"

            val slideUp = AnimationUtils.loadAnimation(this, R.anim.slide_up)
            resultContainer.visibility = View.VISIBLE
            resultContainer.startAnimation(slideUp)
        }

        imageHealthy.setOnClickListener { showDiseaseInfo("healthy", cbHealthy) }
        imageLeafCurl.setOnClickListener { showDiseaseInfo("leaf_curl", cbLeafCurl) }
        imageLeafSpot.setOnClickListener { showDiseaseInfo("leaf_spot", cbLeafSpot) }
        imageWhitefly.setOnClickListener { showDiseaseInfo("whitefly", cbWhitefly) }
        imageYellowish.setOnClickListener { showDiseaseInfo("yellowish", cbYellowish) }

        btnCloseResult.setOnClickListener {
            resultContainer.visibility = View.GONE
        }

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Contoh Penyakit"
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}

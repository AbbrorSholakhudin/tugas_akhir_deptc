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

        // Fungsi utama saat gambar diclick
        fun showDiseaseInfo(key: String, checkbox: CheckBox) {
            val (label, prevention) = penyakitInfo[key] ?: return
            checkbox.isChecked = true

            diseaseText.text = "Penyakit : $label"
            preventionText.text = "Pencegahan : $prevention"

            val slideUp = AnimationUtils.loadAnimation(this, R.anim.slide_up)
            resultContainer.visibility = View.VISIBLE
            resultContainer.startAnimation(slideUp)
        }

        // Binding semua gambar dan checkbox
        val imageHealthy = findViewById<ImageView>(R.id.imageHealthy)
        val checkboxHealthy = findViewById<CheckBox>(R.id.checkboxHealthy)

        val imageLeafCurl = findViewById<ImageView>(R.id.imageLeafCurl)
        val checkboxLeafCurl = findViewById<CheckBox>(R.id.checkboxLeafCurl)

        val imageLeafSpot = findViewById<ImageView>(R.id.imageLeafSpot)
        val checkboxLeafSpot = findViewById<CheckBox>(R.id.checkboxLeafSpot)

        val imageWhitefly = findViewById<ImageView>(R.id.imageWhitefly)
        val checkboxWhitefly = findViewById<CheckBox>(R.id.checkboxWhitefly)

        val imageYellowish = findViewById<ImageView>(R.id.imageYellowish)
        val checkboxYellowish = findViewById<CheckBox>(R.id.checkboxYellowish)

        // Set onClickListener ke tiap gambar
        imageHealthy.setOnClickListener { showDiseaseInfo("healthy", checkboxHealthy) }
        imageLeafCurl.setOnClickListener { showDiseaseInfo("leaf_curl", checkboxLeafCurl) }
        imageLeafSpot.setOnClickListener { showDiseaseInfo("leaf_spot", checkboxLeafSpot) }
        imageWhitefly.setOnClickListener { showDiseaseInfo("whitefly", checkboxWhitefly) }
        imageYellowish.setOnClickListener { showDiseaseInfo("yellowish", checkboxYellowish) }

        btnCloseResult.setOnClickListener {
            resultContainer.visibility = View.GONE
        }
        // Aktifkan tombol kembali di ActionBar
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Deteksi Daun"
    }

    // Tangani aksi tombol kembali di atas
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed() // atau finish()
        return true
    }
}

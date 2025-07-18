package com.abror.deptc

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class deteksiActivity : AppCompatActivity() {

    private lateinit var classifier: ImageClassifier
    private lateinit var imageView: ImageView
    private lateinit var resultContainer: LinearLayout
    private lateinit var btnCloseResult: Button
    private var selectedBitmap: Bitmap? = null

    companion object {
        private const val IMAGE_PICK_CODE = 1001
        private const val CAMERA_REQUEST_CODE = 1002
        private const val CAMERA_PERMISSION_REQUEST_CODE = 1003
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_deteksi)

        classifier = ImageClassifier(this)

        val btnSelect = findViewById<Button>(R.id.btnSelectImage)
        val btnCamera = findViewById<Button>(R.id.btnCamera)
        val btnPredict = findViewById<Button>(R.id.btnPredict)
        imageView = findViewById(R.id.containerimageView)
        resultContainer = findViewById(R.id.resultContainer)
        btnCloseResult = findViewById(R.id.btnCloseResult)

        val diseaseText = findViewById<TextView>(R.id.textDisease)
        val preventionText = findViewById<TextView>(R.id.textPrevention)

        // Sembunyikan container hasil saat awal
        resultContainer.visibility = View.GONE

        btnSelect.setOnClickListener {
            pickImageFromGallery()
        }

        btnCamera.setOnClickListener {
            checkCameraPermissionAndOpenCamera()
        }

        btnPredict.setOnClickListener {
            selectedBitmap?.let {
                val resultIndex = classifier.classify(it)
                val (label, prevention) = getLabelAndPrevention(resultIndex)

                diseaseText.text = "Penyakit : $label"
                preventionText.text = "Pencegahan : $prevention"

                val slideUp = AnimationUtils.loadAnimation(this, R.anim.slide_up)
                resultContainer.visibility = View.VISIBLE
                resultContainer.startAnimation(slideUp)


            } ?: run {
                Toast.makeText(this, "Pilih atau ambil gambar terlebih dahulu.", Toast.LENGTH_SHORT).show()
            }
        }

        btnCloseResult.setOnClickListener {
            resultContainer.visibility = View.GONE
        }
    }

    private fun pickImageFromGallery() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        startActivityForResult(intent, IMAGE_PICK_CODE)
    }

    private fun checkCameraPermissionAndOpenCamera() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.CAMERA),
                CAMERA_PERMISSION_REQUEST_CODE
            )
        } else {
            openCamera()
        }
    }

    private fun openCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (intent.resolveActivity(packageManager) != null) {
            startActivityForResult(intent, CAMERA_REQUEST_CODE)
        } else {
            Toast.makeText(this, "Kamera tidak tersedia", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == CAMERA_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openCamera()
            } else {
                Toast.makeText(this, "Izin kamera ditolak", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                IMAGE_PICK_CODE -> {
                    val imageUri: Uri? = data?.data
                    imageUri?.let { uri ->
                        try {
                            val originalBitmap = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                                val source = ImageDecoder.createSource(contentResolver, uri)
                                ImageDecoder.decodeBitmap(source)
                            } else {
                                MediaStore.Images.Media.getBitmap(contentResolver, uri)
                            }

                            selectedBitmap = originalBitmap.copy(Bitmap.Config.ARGB_8888, true)
                            imageView.setImageBitmap(selectedBitmap)

                        } catch (e: Exception) {
                            e.printStackTrace()
                            Toast.makeText(this, "Gagal memuat gambar", Toast.LENGTH_SHORT).show()
                        }
                    }
                }

                CAMERA_REQUEST_CODE -> {
                    val photo = data?.extras?.get("data") as? Bitmap
                    if (photo != null) {
                        selectedBitmap = photo.copy(Bitmap.Config.ARGB_8888, true)
                        imageView.setImageBitmap(selectedBitmap)
                    }
                }
            }
        }
    }

    private fun getLabelAndPrevention(index: Int): Pair<String, String> {
        return when (index) {
            0 -> "Daun Sehat (Normal)" to "Tidak ada tindakan yang diperlukan. Tetap pantau dan rawat tanaman secara rutin."
            1 -> "Daun Melengkung (Ciri infeksi virus Gemini)" to "Penyebab umum adalah virus Gemini atau kekurangan air. Cabut dan musnahkan daun yang terinfeksi, lakukan penyiraman yang cukup."
            2 -> "Bercak Hitam (Black Spot - *Alternaria alternata*)" to "Semprot dengan fungisida berbahan aktif mancozeb atau tembaga setiap 7–10 hari sekali hingga sembuh. Buang daun yang sangat terinfeksi."
            3 -> "Serangan Hama (Thrips / Aphid / Kutu Daun)" to "Gunakan insektisida sistemik, semprot secara merata terutama di bawah daun dan pucuk muda. Ulangi penyemprotan sesuai petunjuk insektisida."
            4 -> "Klorosis (Gejala Awal Infeksi Gemini / Kekurangan Nitrogen)" to "Perbaiki drainase, hindari penyiraman berlebih, dan berikan pupuk nitrogen sesuai dosis. Jika dicurigai virus, cabut tanaman terinfeksi."
            else -> "Kelas Tidak Diketahui" to "Gambar tidak dikenali. Silakan coba lagi dengan gambar yang lebih jelas atau ambil dari sudut yang berbeda."
        }
    }

}

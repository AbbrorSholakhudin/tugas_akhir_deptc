package com.abror.deptc

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class homeActivity : AppCompatActivity() {

    private lateinit var classifier: ImageClassifier
    private lateinit var imageView: ImageView
    private lateinit var resultView: TextView
    private var selectedBitmap: Bitmap? = null

    companion object {
        private const val IMAGE_PICK_CODE = 1001
        private const val CAMERA_REQUEST_CODE = 1002
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        classifier = ImageClassifier(this)

        val btnSelect = findViewById<Button>(R.id.btnSelectImage)
        val btnCamera = findViewById<Button>(R.id.btnCamera)
        val btnPredict = findViewById<Button>(R.id.btnPredict)
        imageView = findViewById(R.id.containerimageView)
        resultView = findViewById(R.id.textResult)

        btnSelect.setOnClickListener {
            pickImageFromGallery()
        }

        btnCamera.setOnClickListener {
            openCamera()
        }

        btnPredict.setOnClickListener {
            selectedBitmap?.let {
                val resultIndex = classifier.classify(it)
                val label = getLabelForClass(resultIndex)
                resultView.text = "Hasil Prediksi: $label"
            } ?: run {
                resultView.text = "Pilih atau ambil gambar terlebih dahulu."
            }
        }
    }

    private fun pickImageFromGallery() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        startActivityForResult(intent, IMAGE_PICK_CODE)
    }

    private fun openCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (intent.resolveActivity(packageManager) != null) {
            startActivityForResult(intent, CAMERA_REQUEST_CODE)
        } else {
            Toast.makeText(this, "Kamera tidak tersedia", Toast.LENGTH_SHORT).show()
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

    private fun getLabelForClass(index: Int): String {
        return when (index) {
            0 -> "Daun Sehat"
            1 -> "Daun Melengkung"
            2 -> "Daun Berbintik Hitam"
            3 -> "Daun Terkena Hama"
            4 -> "Daun Menguning Lesu"
            else -> "Kelas Tidak Diketahui"
        }
    }
}

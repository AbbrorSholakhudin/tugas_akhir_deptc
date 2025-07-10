package com.abror.deptc

import android.content.Context
import android.graphics.Bitmap
import org.tensorflow.lite.Interpreter
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer
import java.io.FileInputStream
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.channels.FileChannel

class ImageClassifier(context: Context) {

    // Sesuaikan ukuran input dan output berdasarkan model
    private val inputImageSize = 100  // misal: 100x100px
    private val inputChannels = 3     // RGB
    private val outputClasses = 5     // misal: 5 kelas

    private var interpreter: Interpreter

    init {
        // Muat model dari assets sebagai MappedByteBuffer (lebih efisien)
        val assetFileDescriptor = context.assets.openFd("model.tflite")
        val inputStream = FileInputStream(assetFileDescriptor.fileDescriptor)
        val fileChannel = inputStream.channel
        val startOffset = assetFileDescriptor.startOffset
        val declaredLength = assetFileDescriptor.declaredLength

        val modelBuffer = fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength)
        interpreter = Interpreter(modelBuffer)
    }

    /**
     * Fungsi utama untuk melakukan klasifikasi bitmap
     */
    fun classify(bitmap: Bitmap): Int {
        val resizedBitmap = Bitmap.createScaledBitmap(bitmap, inputImageSize, inputImageSize, true)
        val inputBuffer = convertBitmapToByteBuffer(resizedBitmap)

        // Output buffer sesuai jumlah kelas
        val outputBuffer = TensorBuffer.createFixedSize(
            intArrayOf(1, outputClasses),
            org.tensorflow.lite.DataType.FLOAT32
        )

        interpreter.run(inputBuffer, outputBuffer.buffer.rewind())

        val output = outputBuffer.floatArray
        return output.indices.maxByOrNull { output[it] } ?: -1
    }

    /**
     * Konversi Bitmap ke ByteBuffer sesuai format model (RGB dan float)
     */
    private fun convertBitmapToByteBuffer(bitmap: Bitmap): ByteBuffer {
        val byteBuffer = ByteBuffer.allocateDirect(4 * inputImageSize * inputImageSize * inputChannels)
        byteBuffer.order(ByteOrder.nativeOrder())

        val intValues = IntArray(inputImageSize * inputImageSize)
        bitmap.getPixels(intValues, 0, bitmap.width, 0, 0, bitmap.width, bitmap.height)

        for (pixel in intValues) {
            val r = ((pixel shr 16) and 0xFF) / 255.0f
            val g = ((pixel shr 8) and 0xFF) / 255.0f
            val b = (pixel and 0xFF) / 255.0f

            byteBuffer.putFloat(r)
            byteBuffer.putFloat(g)
            byteBuffer.putFloat(b)
        }

        return byteBuffer
    }
}

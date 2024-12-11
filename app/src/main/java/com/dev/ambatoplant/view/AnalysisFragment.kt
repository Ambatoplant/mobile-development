package com.dev.ambatoplant.view

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.dev.ambatoplant.ml.PlantModel
import com.google.android.material.progressindicator.LinearProgressIndicator
import org.tensorflow.lite.DataType
import org.tensorflow.lite.support.image.TensorImage
import com.dev.ambatoplant.R
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer

class AnalysisFragment : Fragment() {

    // Properti untuk komponen UI
    private lateinit var previewImageView: ImageView
    private lateinit var galleryButton: Button
    private lateinit var analyzeButton: Button
    private lateinit var progressIndicator: LinearProgressIndicator

    // Pada onCreateView kita akan menghubungkan layout fragment dengan activity
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Menginflate layout fragment_analysis
        val view = inflater.inflate(R.layout.analyze, container, false)

        // Menemukan view dari findViewById
        previewImageView = view.findViewById(R.id.previewImageView)
        galleryButton = view.findViewById(R.id.galleryButton)
        analyzeButton = view.findViewById(R.id.analyzeButton)
        progressIndicator = view.findViewById(R.id.progressIndicator)

        // Setup listener untuk tombol
        galleryButton.setOnClickListener {
            // Logika untuk membuka galeri dan memilih gambar
            openGallery()
        }

        analyzeButton.setOnClickListener {
            // Logika untuk analisis gambar (sesuai dengan fungsi analyzeImage)
            val bitmap = (previewImageView.drawable as? BitmapDrawable)?.bitmap
            bitmap?.let { analyzeImage(it) }
        }

        return view
    }

    // Fungsi untuk membuka galeri dan memilih gambar
    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, REQUEST_CODE_PICK_IMAGE)
    }

    // Mengambil hasil dari galeri
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_PICK_IMAGE && resultCode == Activity.RESULT_OK && data != null) {
            val selectedImageUri: Uri? = data.data
            selectedImageUri?.let {
                previewImageView.setImageURI(it) // Menampilkan gambar yang dipilih
            }
        }
    }

    // Fungsi untuk menganalisis gambar menggunakan model machine learning
    private fun analyzeImage(image: Bitmap) {
        progressIndicator.visibility = LinearProgressIndicator.VISIBLE

        // Panggil model machine learning di sini (misalnya PlantModel)
        val model = PlantModel.newInstance(requireContext())

        // Proses gambar dan inputkan ke model (preprocessing sesuai model)
        val inputImage = preprocessImage(image)

        // Lakukan inference pada model
        val output = model.process(inputImage)

        // Tangani output dan tampilkan hasil analisis
        val result = output.outputFeature0AsTensorBuffer.floatArray.joinToString(", ")

        // Sembunyikan progress indicator dan tampilkan hasil analisis
        progressIndicator.visibility = LinearProgressIndicator.INVISIBLE
        showAnalysisResult(result)
    }

    private fun preprocessImage(image: Bitmap): TensorBuffer {
        // Ubah gambar bitmap menjadi TensorImage
        val tensorImage = TensorImage.fromBitmap(image)

        // Jika model Anda membutuhkan tensor dengan dimensi tertentu, Anda dapat mengubahnya di sini
        val byteBuffer = tensorImage.buffer

        // Mengonversi byteBuffer menjadi TensorBuffer yang sesuai
        return TensorBuffer.createFixedSize(intArrayOf(1, 224, 224, 3), DataType.FLOAT32).apply {
            loadBuffer(byteBuffer)
        }
    }

    private fun showAnalysisResult(result: String) {
        // Menampilkan hasil analisis pada Toast atau TextView
        Toast.makeText(requireContext(), "Hasil Analisis: $result", Toast.LENGTH_LONG).show()
    }

    // Kode untuk menangani request gallery result
    companion object {
        private const val REQUEST_CODE_PICK_IMAGE = 100
    }
}

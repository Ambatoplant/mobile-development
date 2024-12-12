package com.dev.ambatoplant.view

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.dev.ambatoplant.R
import com.dev.ambatoplant.ml.PlantModel
import com.google.android.material.progressindicator.LinearProgressIndicator
import org.tensorflow.lite.support.image.TensorImage
import java.io.IOException

class AnalyzeActivity : AppCompatActivity() {

    private lateinit var previewImageView: ImageView
    private lateinit var galleryButton: Button
    private lateinit var analyzeButton: Button
    private lateinit var progressIndicator: LinearProgressIndicator

    private val PICK_IMAGE = 1001
    private var selectedImage: Bitmap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_analyze)

        previewImageView = findViewById(R.id.previewImageView)
        galleryButton = findViewById(R.id.galleryButton)
        analyzeButton = findViewById(R.id.analyzeButton)
        progressIndicator = findViewById(R.id.progressIndicator)

        // Open gallery to pick an image
        galleryButton.setOnClickListener {
            openGallery()
        }

        // Start analysis when the analyze button is clicked
        analyzeButton.setOnClickListener {
            selectedImage?.let {
                analyzeImage(it)
            }
        }
    }

    // Open gallery to choose an image
    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, PICK_IMAGE)
    }

    // Handle gallery image result
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE && resultCode == Activity.RESULT_OK) {
            val imageUri = data?.data
            try {
                selectedImage = MediaStore.Images.Media.getBitmap(contentResolver, imageUri)
                previewImageView.setImageBitmap(selectedImage)
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    // Analyze the image using the machine learning model
    private fun analyzeImage(image: Bitmap) {
        progressIndicator.visibility = LinearProgressIndicator.VISIBLE

        // Create a new instance of the model
        val model = PlantModel.newInstance(this)

        // Preprocess the image (convert to TensorImage)
        val tensorImage = preprocessImage(image)

        // Convert TensorImage to TensorBuffer
        val tensorBuffer = tensorImage.tensorBuffer

        // Run inference using the model
        val output = model.process(tensorBuffer)

        // Get the result (output is a TensorBuffer, so we need to convert it to a readable format)
        val result = output.outputFeature0AsTensorBuffer.floatArray.joinToString(", ")

        // Hide the progress indicator and show the analysis result
        progressIndicator.visibility = LinearProgressIndicator.INVISIBLE
        showAnalysisResult(result)
    }

    // Preprocess the image to match the input format required by the model
    private fun preprocessImage(image: Bitmap): TensorImage {
        // Convert Bitmap to TensorImage
        return TensorImage.fromBitmap(image)
    }

    // Show the result of the analysis in a Toast
    private fun showAnalysisResult(result: String) {
        Toast.makeText(this, "Analysis Result: $result", Toast.LENGTH_LONG).show()
    }
}

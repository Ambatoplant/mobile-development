package com.dev.ambatoplant.view

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import com.dev.ambatoplant.R
import com.dev.ambatoplant.data.source.local.entity.HistoryEntity
import com.dev.ambatoplant.databinding.FragmentAnalyzeBinding
import com.dev.ambatoplant.helper.ImageClassifierHelper
import com.yalantis.ucrop.UCrop
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.tensorflow.lite.task.vision.classifier.Classifications
import java.io.File
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale


class AnalysisFragment : Fragment(R.layout.fragment_analyze) {

    private lateinit var binding: FragmentAnalyzeBinding

    private val viewModel: MainViewModel by viewModel()
    private var currentImageUri: Uri? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAnalyzeBinding.inflate(inflater, container, false)

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        binding.apply {
            galleryButton.setOnClickListener { startGallery() }
            analyzeButton.setOnClickListener { analyzeImage() }
            appBar.ibHistory.setOnClickListener {
                val intent = Intent(requireContext(), HistoryActivity::class.java)
                startActivity(intent)
            }
        }

        return binding.root
    }

    private fun startGallery() {
        launcherGallery.launch(
            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
        )
    }

    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            currentImageUri = uri
            cropImage(uri)
        } else {
            Log.d("PhotoPicker", "No media selected")
        }
    }

    private fun cropImage(sourceUri: Uri) {
        val fileName = "Asclepius_CroppedImage_${System.currentTimeMillis()}"
        val destinationUri = Uri.fromFile(File(requireContext().cacheDir, fileName))
        UCrop.of(sourceUri, destinationUri)
            .withAspectRatio(1f, 1f)
            .start(requireContext(), launcherCrop)
    }

    private val launcherCrop = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val resultUri: Uri = UCrop.getOutput(result.data!!)!!
            currentImageUri = resultUri
            showImage()
        }
    }

    private fun showImage() {
        currentImageUri?.let { uri ->
            Log.d("ImageURI", "showImage: $uri")
            binding.previewImageView.setImageURI(uri)
        }
    }

    private fun analyzeImage() {
        if (currentImageUri != null) {
            binding.progressIndicator.visibility = View.VISIBLE

            val imageClassifierHelper = ImageClassifierHelper(
                context = requireContext(),
                classifierListener = object : ImageClassifierHelper.ClassifierListener {
                    override fun onError(error: String) {
                        binding.progressIndicator.visibility = View.GONE
                        showToast(error)
                    }

                    override fun onResults(results: List<Classifications>?) {
                        requireActivity().runOnUiThread {
                            results?.let {
                                if (it.isNotEmpty() && it[0].categories.isNotEmpty()) {
                                    val classificationResult = it.first().categories.first()

                                    binding.progressIndicator.visibility = View.GONE
                                    val time = Calendar.getInstance().time
                                    val formatter = SimpleDateFormat("dd-MMMM-yyyy HH:mm:ss", Locale.US)
                                    val currentTime = formatter.format(time)

                                    moveToResult(
                                        HistoryEntity(
                                            imageUri = currentImageUri.toString(),
                                            predictionResult = classificationResult.label,
                                            confidenceScore = classificationResult.score,
                                            dateTaken = currentTime.toString()
                                        )
                                    )
                                }
                            }
                        }
                    }
                }
            )
            imageClassifierHelper.classifyStaticImage(currentImageUri!!)
        } else {
            showToast(getString(R.string.please_insert_the_image_file_first))
        }
    }

    private fun moveToResult(historyEntity: HistoryEntity) {
        viewModel.insertPredictionResult(historyEntity)
        showToast(getString(R.string.the_classification_result_has_been_saved))
        binding.previewImageView.setImageResource(R.drawable.ic_place_holder)
        currentImageUri = null

        val intent = Intent(requireContext(), ResultActivity::class.java)
        intent.putExtra(ResultActivity.EXTRA_CLASSIFICATION_RESULT, historyEntity)
        startActivity(intent)
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }
}

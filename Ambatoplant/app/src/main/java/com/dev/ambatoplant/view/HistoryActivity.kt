package com.dev.ambatoplant.view

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dev.ambatoplant.R
import com.dev.ambatoplant.data.source.local.entity.HistoryEntity
import com.dev.ambatoplant.view.adapter.HistoryAdapter

class HistoryActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var historyAdapter: HistoryAdapter
    private val analysisHistory: MutableList<HistoryEntity> = mutableListOf()

    // Convert to HistoryEntity, adding a unique classificationId
    private fun convertToHistoryEntity(analysisResult: AnalysisResult, id: Int): HistoryEntity {
        return HistoryEntity(
            classificationId = id, // Here we're passing an Int value
            plantName = analysisResult.plantName,
            result = analysisResult.result
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)

        // Initialize RecyclerView
        recyclerView = findViewById(R.id.rv_history)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Initialize Adapter
        historyAdapter = HistoryAdapter(analysisHistory)
        recyclerView.adapter = historyAdapter

        // Sample data - in real case, load from a database or API
        loadSampleData()
    }

    private fun loadSampleData() {
        // Simulate loading history data
        for (i in 1..10) {
            val analysisResult = AnalysisResult("Plant $i", "Result $i")
            analysisHistory.add(convertToHistoryEntity(analysisResult, i)) // Pass i as classificationId
        }

        // Notify adapter to update the list
        historyAdapter.notifyDataSetChanged()

        // If no data, show empty view
        if (analysisHistory.isEmpty()) {
            findViewById<View>(R.id.view_empty).visibility = View.VISIBLE
        } else {
            findViewById<View>(R.id.view_empty).visibility = View.GONE
        }
    }
}

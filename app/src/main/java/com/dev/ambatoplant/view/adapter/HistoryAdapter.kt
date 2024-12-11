package com.dev.ambatoplant.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.dev.ambatoplant.R
import com.dev.ambatoplant.data.source.local.entity.HistoryEntity

class HistoryAdapter(private val analysisHistory: List<HistoryEntity>) : RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_history, parent, false)
        return HistoryViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        val currentAnalysis = analysisHistory[position]
        holder.plantNameTextView.text = currentAnalysis.plantName
        holder.resultTextView.text = currentAnalysis.result
    }

    override fun getItemCount(): Int {
        return analysisHistory.size
    }

    class HistoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val plantNameTextView: TextView = itemView.findViewById(R.id.tv_plant_name)
        val resultTextView: TextView = itemView.findViewById(R.id.tv_result)
    }
}

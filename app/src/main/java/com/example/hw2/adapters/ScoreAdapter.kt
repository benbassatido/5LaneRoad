package com.example.hw2.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.hw2.R
import com.example.hw2.models.Score

class ScoreAdapter(
    private val scores: List<Score>,
    private val onItemClick: (Score) -> Unit
) : RecyclerView.Adapter<ScoreAdapter.ScoreViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScoreViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_score, parent, false)
        return ScoreViewHolder(view)
    }

    override fun onBindViewHolder(holder: ScoreViewHolder, position: Int) {
        val score = scores[position]
        holder.bind(score)
        holder.itemView.setOnClickListener {
            onItemClick(score)
        }
    }

    override fun getItemCount(): Int = scores.size

    inner class ScoreViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val scoreTextView: TextView = itemView.findViewById(R.id.item_LBL_score)
        private val nameTextView: TextView = itemView.findViewById(R.id.item_LBL_name)

        fun bind(score: Score) {
            scoreTextView.text = score.score.toString()
            nameTextView.text = score.name
        }
    }
}

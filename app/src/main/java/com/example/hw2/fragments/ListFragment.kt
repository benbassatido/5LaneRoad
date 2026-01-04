package com.example.hw2.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.hw2.R
import com.example.hw2.adapters.ScoreAdapter
import com.example.hw2.models.Score
import com.example.hw2.utilities.ScoreManager

class ListFragment : Fragment() {

    interface OnScoreSelectedListener {
        fun onScoreSelected(score: Score)
    }

    private var listener: OnScoreSelectedListener? = null

    private lateinit var recyclerView: RecyclerView
    private lateinit var scoreAdapter: ScoreAdapter
    private lateinit var scoreManager: ScoreManager

    override fun onAttach(context: Context) {
        super.onAttach(context)
        listener = context as? OnScoreSelectedListener
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_list, container, false)
        scoreManager = ScoreManager(requireContext())
        recyclerView = view.findViewById(R.id.list_RCV_scores)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val scores = scoreManager.getScores()
        scoreAdapter = ScoreAdapter(scores) { clickedScore ->
            listener?.onScoreSelected(clickedScore)
        }
        recyclerView.adapter = scoreAdapter
    }
}

package com.example.hw2

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.hw2.fragments.ListFragment
import com.example.hw2.fragments.MapFragment
import com.example.hw2.models.Score

class ScoresActivity : AppCompatActivity(), ListFragment.OnScoreSelectedListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scores)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.scores_FMT_list, ListFragment())
                .replace(R.id.scores_FMT_map, MapFragment(), "MAP_FRAGMENT")
                .commit()
        }
    }

    override fun onScoreSelected(score: Score) {

        val mapFragment = supportFragmentManager.findFragmentByTag("MAP_FRAGMENT") as? MapFragment

        mapFragment?.focusOnScore(score)
    }

}

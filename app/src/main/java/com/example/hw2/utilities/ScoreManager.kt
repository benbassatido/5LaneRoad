package com.example.hw2.utilities

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.os.Looper
import com.example.hw2.models.Score
import com.google.android.gms.location.*
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class ScoreManager(context: Context) {

    companion object {
        private const val SCORES_KEY = "scores"
        private const val TOP_LIMIT = 10
    }

    private val appContext = context.applicationContext
    private val sharedPrefs = SharedPreferencesManager.getInstance()
    private val gson = Gson()

    private val fused: FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(appContext)

    fun saveScore(score: Score) {
        val scores = getScores().toMutableList()
        scores.add(score)

        val topScores = scores.sortedByDescending { it.score }.take(TOP_LIMIT)

        val rankedScores = topScores.mapIndexed { index, s ->
            s.copy(rank = index + 1)
        }

        val json = gson.toJson(rankedScores)
        sharedPrefs.putString(SCORES_KEY, json)
    }

    fun getScores(): List<Score> {
        val json = sharedPrefs.getString(SCORES_KEY, null) ?: return emptyList()
        val type = object : TypeToken<List<Score>>() {}.type
        return gson.fromJson(json, type)
    }

    fun clearScores() {
        sharedPrefs.remove(SCORES_KEY)
    }

    @SuppressLint("MissingPermission")
    fun saveScoreWithCurrentLocation(
        name: String,
        points: Int,
        onSaved: (() -> Unit)? = null
    ) {
        fused.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, null)
            .addOnSuccessListener { loc ->
                if (loc != null) {
                    saveWithLocation(name, points, loc, onSaved)
                } else {
                    requestSingleUpdate(name, points, onSaved)
                }
            }
            .addOnFailureListener {
                requestSingleUpdate(name, points, onSaved)
            }
    }

    @SuppressLint("MissingPermission")
    private fun requestSingleUpdate(name: String, points: Int, onSaved: (() -> Unit)?) {
        val request = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 1000L)
            .setMaxUpdates(1)
            .build()

        val callback = object : LocationCallback() {
            override fun onLocationResult(result: LocationResult) {
                val loc = result.lastLocation
                if (loc != null) {
                    saveWithLocation(name, points, loc, onSaved)
                } else {
                    val score = Score(score = points, name = name, lat = 0.0, lon = 0.0)
                    saveScore(score)
                    onSaved?.invoke()
                }
                fused.removeLocationUpdates(this)
            }
        }

        fused.requestLocationUpdates(request, callback, Looper.getMainLooper())
    }

    private fun saveWithLocation(name: String, points: Int, loc: Location, onSaved: (() -> Unit)?) {
        val score = Score(
            score = points,
            name = name,
            lat = loc.latitude,
            lon = loc.longitude
        )
        saveScore(score)
        onSaved?.invoke()
    }
}

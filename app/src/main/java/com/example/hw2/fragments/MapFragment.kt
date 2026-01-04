package com.example.hw2.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.hw2.R
import com.example.hw2.models.Score
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions

class MapFragment : Fragment(), OnMapReadyCallback {

    private var googleMap: GoogleMap? = null
    private var selectedScoreMarker: Marker? = null

    private var pendingSelectedScore: Score? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.fragment_map, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val existing = childFragmentManager.findFragmentByTag("MAP") as? SupportMapFragment
        val mapFragment = existing ?: SupportMapFragment.newInstance().also {
            childFragmentManager.beginTransaction()
                .replace(R.id.map_container, it, "MAP")
                .commit()
        }

        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(map: GoogleMap) {
        googleMap = map

        val world = LatLng(20.0, 0.0)
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(world, 2.2f))

        pendingSelectedScore?.let {
            focusOnScore(it)
            pendingSelectedScore = null
        }
    }


    fun focusOnScore(score: Score) {
        val map = googleMap
        if (map == null) {
            pendingSelectedScore = score
            return
        }


        if (score.lat == 0.0 && score.lon == 0.0) {
            return
        }

        val latLng = LatLng(score.lat, score.lon)

        selectedScoreMarker?.remove()
        selectedScoreMarker = map.addMarker(
            MarkerOptions()
                .position(latLng)
                .title(score.name)
        )

        map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 12f))
    }
}

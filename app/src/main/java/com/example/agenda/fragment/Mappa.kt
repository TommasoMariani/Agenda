package com.example.agenda.fragment

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.agenda.R
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class Mappa : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mappa)

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Aggiungi un marker di esempio
        val sydney = LatLng(-34.0, 151.0)
        mMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))

        // Imposta un listener per il click sulla mappa per selezionare una posizione
        mMap.setOnMapClickListener { latLng ->
            // Rimuove i marker esistenti
            mMap.clear()
            // Aggiungi un marker alla posizione selezionata
            mMap.addMarker(MarkerOptions().position(latLng).title("Selected Location"))

            // Restituisci la posizione selezionata al Fragment
            val intent = Intent()
            intent.putExtra("location", "${latLng.latitude},${latLng.longitude}")
            setResult(RESULT_OK, intent)
            finish() // Chiudi l'Activity dopo aver selezionato il luogo
        }
    }
}

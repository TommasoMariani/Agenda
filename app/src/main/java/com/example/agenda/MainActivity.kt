package com.example.agenda

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.example.agenda.fragment.FragmentHome
import com.example.agenda.fragment.FragmentImpostazioni
import com.example.agenda.fragment.FragmentPreferiti
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main) // Deve essere chiamato prima di accedere alle view

        enableEdgeToEdge()

        val fragmentHome = FragmentHome()
        val fragmentPreferiti = FragmentPreferiti()
        val fragmentImpostazioni = FragmentImpostazioni()

        // Carica il primo Fragment (Home) all'avvio
        makeCurrentFragment(fragmentHome)

        val bottomNavigation = findViewById<BottomNavigationView>(R.id.navMenu)
        bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.iconaHome -> {
                    makeCurrentFragment(fragmentHome)
                    true
                }
                R.id.iconaPreferiti -> {
                    makeCurrentFragment(fragmentPreferiti)
                    true
                }
                R.id.iconaAccount -> {
                    makeCurrentFragment(fragmentImpostazioni)
                    true
                }
                else -> false
            }
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun makeCurrentFragment(fragment: Fragment) =
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.wrapper, fragment) // Usa l'ID "wrapper" dal layout XML
            commit()
        }
}

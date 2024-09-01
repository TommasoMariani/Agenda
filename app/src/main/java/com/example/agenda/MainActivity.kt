package com.example.agenda

import android.content.Context
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
        setContentView(R.layout.activity_main)

        enableEdgeToEdge()

        val fragmentHome = FragmentHome()
        val fragmentPreferiti = FragmentPreferiti()
        val fragmentImpostazioni = FragmentImpostazioni()

        val bottomNavigation = findViewById<BottomNavigationView>(R.id.navMenu)

        // Ripristina il frammento selezionato in base alle SharedPreferences
        val lastSelectedFragment = getSharedPreferences("user_settings", Context.MODE_PRIVATE)
            .getInt("last_selected_fragment", R.id.iconaHome)

        when (lastSelectedFragment) {
            R.id.iconaHome -> makeCurrentFragment(fragmentHome)
            R.id.iconaPreferiti -> makeCurrentFragment(fragmentPreferiti)
            R.id.iconaAccount -> makeCurrentFragment(fragmentImpostazioni)
        }

        bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.iconaHome -> {
                    makeCurrentFragment(fragmentHome)
                    // Salva lo stato della selezione
                    getSharedPreferences("user_settings", Context.MODE_PRIVATE)
                        .edit().putInt("last_selected_fragment", R.id.iconaHome).apply()
                    true
                }
                R.id.iconaPreferiti -> {
                    makeCurrentFragment(fragmentPreferiti)
                    // Salva lo stato della selezione
                    getSharedPreferences("user_settings", Context.MODE_PRIVATE)
                        .edit().putInt("last_selected_fragment", R.id.iconaPreferiti).apply()
                    true
                }
                R.id.iconaAccount -> {
                    makeCurrentFragment(fragmentImpostazioni)
                    // Salva lo stato della selezione
                    getSharedPreferences("user_settings", Context.MODE_PRIVATE)
                        .edit().putInt("last_selected_fragment", R.id.iconaAccount).apply()
                    true
                }
                else -> false
            }
        }

        
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0)  // Imposta 0 per evitare padding inferiore
            insets
        }
    }

    private fun makeCurrentFragment(fragment: Fragment) =
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.wrapper, fragment)
            commit()
        }
}

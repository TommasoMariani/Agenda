package com.example.agenda

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.Switch
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import java.util.Locale

class Utente : AppCompatActivity() {

    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_utente)


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        sharedPreferences = getSharedPreferences("user_settings", Context.MODE_PRIVATE)

        // Spinner per il cambio lingua
        val spinner = findViewById<Spinner>(R.id.menuLingue)
        val languages = resources.getStringArray(R.array.lingue)
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, languages)
        spinner.adapter = adapter

        // Listener per il cambio lingua
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                when (position) {
                    0 -> setLocale("en")  // Inglese
                    1 -> setLocale("it")  // Italiano
                    2 -> setLocale("es")  // Spagnolo
                    3 -> setLocale("de")  // Tedesco


                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
            }
        }

        // Switch per abilitare le notifiche
        val notificationSwitch = findViewById<Switch>(R.id.switchNotifiche)
        notificationSwitch.isChecked = sharedPreferences.getBoolean("notifications_enabled", false)

        notificationSwitch.setOnCheckedChangeListener { _, isChecked ->
            sharedPreferences.edit().putBoolean("notifications_enabled", isChecked).apply()
            if (isChecked) {
                enableNotifications()
            } else {
                disableNotifications()
            }
        }

        // Bottone di logout
        val logoutButton = findViewById<Button>(R.id.bottoneLogout)
        logoutButton.setOnClickListener {
            logout()
        }
    }

    // Funzione per impostare la lingua selezionata
    private fun setLocale(languageCode: String) {
        val locale = Locale(languageCode)
        Locale.setDefault(locale)
        val config = Configuration()
        config.locale = locale
        resources.updateConfiguration(config, resources.displayMetrics)
        recreate()  // Ricrea l'activity per applicare la nuova lingua
    }

    // Funzioni per abilitare e disabilitare le notifiche
    private fun enableNotifications() {
        // Codice per abilitare le notifiche
    }

    private fun disableNotifications() {
        // Codice per disabilitare le notifiche
    }

    // Funzione di logout
    private fun logout() {

        sharedPreferences.edit().clear().apply()

        // Torna alla pagina di login
        val intent = Intent(this, Login::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }
}

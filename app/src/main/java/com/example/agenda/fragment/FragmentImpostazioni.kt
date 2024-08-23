package com.example.agenda.fragment

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.Switch
import androidx.fragment.app.Fragment
import com.example.agenda.Login
import com.example.agenda.R
import java.util.Locale

class FragmentImpostazioni : Fragment() {

    private lateinit var sharedPreferences: SharedPreferences

    // Questo metodo viene chiamato quando il Fragment viene creato.
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // "inflate" il layout del Fragment (fragment_impostazioni.xml) e lo restituisce come View
        val view = inflater.inflate(R.layout.fragment_impostazioni, container, false)

        // Configura le SharedPreferences (per memorizzare le impostazioni dell'utente)
        // Usando "requireActivity()" otteniamo il contesto dell'Activity che ospita il Fragment
        sharedPreferences = requireActivity().getSharedPreferences("user_settings", Context.MODE_PRIVATE)

        // Configurazione dello Spinner (menu a tendina) per il cambio lingua
        val spinner = view.findViewById<Spinner>(R.id.menuLingue)
        val languages = resources.getStringArray(R.array.lingue)
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, languages)
        spinner.adapter = adapter

        // Listener per gestire il cambio di lingua quando l'utente seleziona un'opzione dallo Spinner
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                when (position) {
                    0 -> setLocale("en")  // Inglese
                    1 -> setLocale("it")  // Italiano
                    2 -> setLocale("es")  // Spagnolo
                    3 -> setLocale("de")  // Tedesco
                    // Puoi aggiungere altre lingue qui se necessario
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Nessuna azione necessaria quando nulla è selezionato
            }
        }

        // Configurazione dello Switch per abilitare/disabilitare le notifiche
        val notificationSwitch = view.findViewById<Switch>(R.id.switchNotifiche)
        notificationSwitch.isChecked = sharedPreferences.getBoolean("notifications_enabled", false)

        // Listener per salvare lo stato dello Switch nelle SharedPreferences
        notificationSwitch.setOnCheckedChangeListener { _, isChecked ->
            sharedPreferences.edit().putBoolean("notifications_enabled", isChecked).apply()
            if (isChecked) {
                enableNotifications()
            } else {
                disableNotifications()
            }
        }

        // Configurazione del bottone di logout
        val logoutButton = view.findViewById<Button>(R.id.bottoneLogout)
        logoutButton.setOnClickListener {
            logout()
        }

        // Restituisce la View per essere visualizzata dal Fragment
        return view
    }

    // Funzione per cambiare la lingua dell'applicazione
    private fun setLocale(languageCode: String) {
        val locale = Locale(languageCode)
        Locale.setDefault(locale)
        val config = Configuration()
        config.locale = locale
        resources.updateConfiguration(config, resources.displayMetrics)
        requireActivity().recreate()  // Ricrea l'Activity per applicare la nuova lingua
    }

    // Funzione per abilitare le notifiche
    private fun enableNotifications() {
        // Implementa qui il codice per abilitare le notifiche (se necessario)
    }

    // Funzione per disabilitare le notifiche
    private fun disableNotifications() {
        // Implementa qui il codice per disabilitare le notifiche (se necessario)
    }

    // Funzione di logout
    private fun logout() {
        // Cancella le informazioni dell'utente (ad esempio, SharedPreferences)
        sharedPreferences.edit().clear().apply()

        // Torna alla pagina di login
        val intent = Intent(requireActivity(), Login::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        requireActivity().finish()  // Chiude l'Activity corrente per prevenire il ritorno alla pagina delle impostazioni
    }
}

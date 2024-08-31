package com.example.agenda.fragment

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.Switch
import androidx.fragment.app.Fragment
import com.example.agenda.LoginActivity
import com.example.agenda.R
import java.util.Locale

class FragmentImpostazioni : Fragment() {

    private lateinit var sharedPreferences: SharedPreferences
    private var currentLanguageCode: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_impostazioni, container, false)

        sharedPreferences = requireActivity().getSharedPreferences("user_settings", Context.MODE_PRIVATE)
        currentLanguageCode = sharedPreferences.getString("selected_language", Locale.getDefault().language)

        val spinner = view.findViewById<Spinner>(R.id.menuLingue)
        val languages = resources.getStringArray(R.array.lingue)
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, languages)
        spinner.adapter = adapter

        // Imposta la lingua selezionata corretta
        spinner.setSelection(getLanguagePosition(currentLanguageCode))

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                val selectedLanguageCode = when (position) {
                    0 -> "en"  // Inglese
                    1 -> "it"  // Italiano
                    2 -> "es"  // Spagnolo
                    3 -> "de"  // Tedesco
                    else -> "en"
                }

                // Cambia lingua solo se diversa da quella corrente
                if (selectedLanguageCode != currentLanguageCode) {
                    setLocale(selectedLanguageCode)
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Nessuna azione necessaria quando nulla è selezionato
            }
        }

        val notificationSwitch = view.findViewById<Switch>(R.id.switchNotifiche)
        notificationSwitch.isChecked = sharedPreferences.getBoolean("notifications_enabled", false)

        notificationSwitch.setOnCheckedChangeListener { _, isChecked ->
            sharedPreferences.edit().putBoolean("notifications_enabled", isChecked).apply()
            if (isChecked) {
                enableNotifications()
            } else {
                disableNotifications()
            }
        }

        val logoutButton = view.findViewById<Button>(R.id.bottoneLogout)
        logoutButton.setOnClickListener {
            logout()
        }

        return view
    }

    private fun getLanguagePosition(languageCode: String?): Int {
        return when (languageCode) {
            "en" -> 0
            "it" -> 1
            "es" -> 2
            "de" -> 3
            else -> 0
        }
    }

    private fun setLocale(languageCode: String) {
        Log.d("FragmentImpostazioni", "Changing locale to $languageCode")
        val locale = Locale(languageCode)
        Locale.setDefault(locale)
        val config = Configuration()
        config.setLocale(locale)
        resources.updateConfiguration(config, resources.displayMetrics)

        // Usa SharedPreferences per salvare la lingua selezionata
        sharedPreferences.edit().putString("selected_language", languageCode).apply()

        // Aggiorna la lingua corrente
        currentLanguageCode = languageCode

        // Usa SharedPreferences per salvare l'ID del frammento corrente
        sharedPreferences.edit().putInt("last_selected_fragment", R.id.iconaAccount).apply()

        requireActivity().recreate()
    }

    private fun enableNotifications() {
        // Implementa qui il codice per abilitare le notifiche
    }

    private fun disableNotifications() {
        // Implementa qui il codice per disabilitare le notifiche
    }

    private fun logout() {
        Log.d("FragmentImpostazioni", "Logout button clicked")

        sharedPreferences.edit().clear().apply()

        val intent = Intent(requireActivity(), LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        requireActivity().finish()
    }

    override fun onResume() {
        super.onResume()
        Log.d("FragmentImpostazioni", "onResume called")
    }

    override fun onPause() {
        super.onPause()
        Log.d("FragmentImpostazioni", "onPause called")
    }

    override fun onStop() {
        super.onStop()
        Log.d("FragmentImpostazioni", "onStop called")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.d("FragmentImpostazioni", "onDestroyView called")
    }
}

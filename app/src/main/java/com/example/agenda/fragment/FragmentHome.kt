package com.example.agenda.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CalendarView
import android.widget.EditText
import android.widget.TimePicker
import android.widget.Toast
import androidx.appcompat.widget.SwitchCompat
import androidx.fragment.app.Fragment
import com.example.agenda.R
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class FragmentHome : Fragment() {

    private lateinit var calendarView: CalendarView
    private lateinit var eventName: EditText
    private lateinit var city: EditText
    private lateinit var province: EditText
    private lateinit var street: EditText
    private lateinit var eventTimePicker: TimePicker
    private lateinit var notificationSwitch: SwitchCompat
    private lateinit var addEventButton: Button
    private lateinit var database: DatabaseReference

    private var selectedDate: String = ""
    private var selectedTime: String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        // Inizializzazione del riferimento al database
        database = FirebaseDatabase.getInstance().getReference("events")

        // Collegamento degli elementi della UI con il codice
        calendarView = view.findViewById(R.id.calendarView)
        eventName = view.findViewById(R.id.nomeEvento)
        city = view.findViewById(R.id.citta)
        province = view.findViewById(R.id.provincia)
        street = view.findViewById(R.id.via)
        eventTimePicker = view.findViewById(R.id.eventTimePicker)
        notificationSwitch = view.findViewById(R.id.notifiSwitch)
        addEventButton = view.findViewById(R.id.aggiungiEvento)

        // Listener per la selezione della data dal calendario
        calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
            selectedDate = "$dayOfMonth/${month + 1}/$year"
            Log.d("FragmentHome", "Selected Date: $selectedDate")
        }

        // Configurazione e listener per la selezione dell'ora
        eventTimePicker.setIs24HourView(true)
        eventTimePicker.setOnTimeChangedListener { _, hourOfDay, minute ->
            selectedTime = String.format("%02d:%02d", hourOfDay, minute)
            Log.d("FragmentHome", "Selected Time: $selectedTime")
        }

        // Listener per il pulsante di aggiunta evento
        addEventButton.setOnClickListener {
            addEventToFirebase()
        }

        return view
    }

    private fun addEventToFirebase() {
        val name = eventName.text.toString()
        val cityName = city.text.toString()
        val provinceName = province.text.toString()
        val streetName = street.text.toString()
        val notificationsEnabled = notificationSwitch.isChecked

        // Log per verificare i valori immessi
        Log.d("FragmentHome", "Event Name: $name")
        Log.d("FragmentHome", "Selected Date: $selectedDate")
        Log.d("FragmentHome", "Selected Time: $selectedTime")
        Log.d("FragmentHome", "City: $cityName")
        Log.d("FragmentHome", "Province: $provinceName")
        Log.d("FragmentHome", "Street: $streetName")

        // Controllo se tutti i campi obbligatori sono stati compilati
        if (name.isEmpty()) {
            Toast.makeText(requireContext(), "Il nome dell'evento è obbligatorio", Toast.LENGTH_SHORT).show()
            return
        }
        if (selectedDate.isEmpty()) {
            Toast.makeText(requireContext(), "La data dell'evento è obbligatoria", Toast.LENGTH_SHORT).show()
            return
        }
        if (selectedTime.isEmpty()) {
            Toast.makeText(requireContext(), "L'orario dell'evento è obbligatorio", Toast.LENGTH_SHORT).show()
            return
        }
        if (cityName.isEmpty()) {
            Toast.makeText(requireContext(), "La città è obbligatoria", Toast.LENGTH_SHORT).show()
            return
        }
        if (provinceName.isEmpty()) {
            Toast.makeText(requireContext(), "La provincia è obbligatoria", Toast.LENGTH_SHORT).show()
            return
        }
        if (streetName.isEmpty()) {
            Toast.makeText(requireContext(), "La via è obbligatoria", Toast.LENGTH_SHORT).show()
            return
        }

        // Generazione dell'ID dell'evento e creazione del nuovo oggetto evento
        val eventId = database.push().key
        if (eventId == null) {
            Toast.makeText(requireContext(), "Errore generando l'ID dell'evento", Toast.LENGTH_SHORT).show()
            return
        }
        val event = Event(eventId, name, selectedDate, selectedTime, cityName, provinceName, streetName, notificationsEnabled)

        // Aggiunta dell'evento al database Firebase
        database.child(eventId).setValue(event).addOnCompleteListener {
            if (it.isSuccessful) {
                Toast.makeText(requireContext(), "Evento aggiunto con successo", Toast.LENGTH_SHORT).show()
                resetFields()
            } else {
                Toast.makeText(requireContext(), "Errore durante l'aggiunta dell'evento", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun resetFields() {
        // Reset dei campi dopo aver aggiunto l'evento
        eventName.text.clear()
        city.text.clear()
        province.text.clear()
        street.text.clear()
        notificationSwitch.isChecked = false

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            eventTimePicker.hour = 0
            eventTimePicker.minute = 0
        } else {
            eventTimePicker.currentHour = 0
            eventTimePicker.currentMinute = 0
        }

        selectedDate = ""
        selectedTime = ""
    }

    // Data class per rappresentare un evento
    data class Event(
        val id: String,
        val name: String,
        val date: String,
        val time: String,
        val city: String,
        val province: String,
        val street: String,
        val notificationsEnabled: Boolean
    )
}

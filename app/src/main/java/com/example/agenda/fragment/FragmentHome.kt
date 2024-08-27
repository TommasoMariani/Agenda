package com.example.agenda.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.appcompat.widget.SwitchCompat
import com.example.agenda.R
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class FragmentHome : Fragment() {


    private lateinit var calendarView: CalendarView
    private lateinit var eventName: EditText
    private lateinit var selectLocationButton: Button
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


        database = FirebaseDatabase.getInstance().getReference("events")

        // Collegamento delle viste
        calendarView = view.findViewById(R.id.calendarView)
        eventName = view.findViewById(R.id.eventName)
        selectLocationButton = view.findViewById(R.id.selezionaLuogo) // Corretto qui
        eventTimePicker = view.findViewById(R.id.eventTimePicker)
        notificationSwitch = view.findViewById(R.id.notificationSwitch)
        addEventButton = view.findViewById(R.id.addEventButton)

        // Configurazione del CalendarView
        calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
            selectedDate = "$dayOfMonth/${month + 1}/$year"
        }

        // Configurazione del TimePicker
        eventTimePicker.setIs24HourView(true)
        eventTimePicker.setOnTimeChangedListener { _, hourOfDay, minute ->
            selectedTime = String.format("%02d:%02d", hourOfDay, minute)
        }

        // Gestione del pulsante per selezionare la posizione
        selectLocationButton.setOnClickListener {
            // Implementazione per selezionare il luogo (da aggiungere)
            val intent = Intent(context, Mappa::class.java) // Sostituisci con la tua attività per la mappa
            startActivityForResult(intent, 1)
        }

        // Gestione del pulsante per aggiungere un evento
        addEventButton.setOnClickListener {
            addEventToFirebase()
        }

        return view
    }

    // Funzione per aggiungere un evento a Firebase
    private fun addEventToFirebase() {
        val name = eventName.text.toString()
        val location = selectLocationButton.text.toString()
        val notificationsEnabled = notificationSwitch.isChecked

        if (name.isEmpty() || selectedDate.isEmpty() || selectedTime.isEmpty()) {
            Toast.makeText(context, "Completa tutti i campi obbligatori", Toast.LENGTH_SHORT).show()
            return
        }

        val eventId = database.push().key!!
        val event = Event(eventId, name, selectedDate, selectedTime, location, notificationsEnabled)

        database.child(eventId).setValue(event).addOnCompleteListener {
            if (it.isSuccessful) {
                Toast.makeText(context, "Evento aggiunto con successo", Toast.LENGTH_SHORT).show()
                resetFields()
            } else {
                Toast.makeText(context, "Errore durante l'aggiunta dell'evento", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Funzione per resettare i campi dopo l'aggiunta dell'evento
    private fun resetFields() {
        eventName.text.clear()
        selectLocationButton.text = getString(R.string.selezionaLuogoooo) // Corretto qui
        notificationSwitch.isChecked = false
        eventTimePicker.currentHour = 0
        eventTimePicker.currentMinute = 0
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1 && resultCode == Activity.RESULT_OK) { // Corretto qui
            val location = data?.getStringExtra("location")
            selectLocationButton.text = location
        }
    }

    // Classe per rappresentare l'evento
    data class Event(
        val id: String,
        val name: String,
        val date: String,
        val time: String,
        val location: String,
        val notificationsEnabled: Boolean
    )
}


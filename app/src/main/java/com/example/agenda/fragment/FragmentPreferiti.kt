package com.example.agenda.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.util.Log
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.agenda.R
import com.example.agenda.Event
import com.example.agenda.EventoAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class FragmentPreferiti : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var nessunEventoTextView: TextView
    private lateinit var eventoAdapter: EventoAdapter
    private lateinit var listaEventi: MutableList<Event>
    private lateinit var database: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_preferiti, container, false)

        recyclerView = view.findViewById(R.id.recyclerViewPreferiti)
        nessunEventoTextView = view.findViewById(R.id.nessunEventoTextView)
        recyclerView.layoutManager = LinearLayoutManager(context)

        listaEventi = mutableListOf()
        eventoAdapter = EventoAdapter(listaEventi) { evento -> eliminaEvento(evento) }
        recyclerView.adapter = eventoAdapter

        database = FirebaseDatabase.getInstance().getReference("eventi")
        caricaEventi()

        return view
    }

    private fun caricaEventi() {
        val currentUser = FirebaseAuth.getInstance().currentUser
        if (currentUser != null) {
            database.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    listaEventi.clear()
                    if (snapshot.exists()) {
                        for (eventoSnapshot in snapshot.children) {
                            val evento = eventoSnapshot.getValue(Event::class.java)
                            evento?.let { listaEventi.add(it) }
                        }
                        Log.d("FragmentPreferiti", "Numero di eventi caricati: ${listaEventi.size}")
                        eventoAdapter.notifyDataSetChanged()
                    } else {
                        Log.d("FragmentPreferiti", "Snapshot non esiste")
                    }
                    aggiornaUI()
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e("FragmentPreferiti", "Errore nel caricamento degli eventi", error.toException())
                }
            })
        } else {
            Log.d("FragmentPreferiti", "Utente non autenticato")
            Toast.makeText(context, "Devi essere autenticato per vedere i tuoi eventi preferiti.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun aggiornaUI() {
        if (listaEventi.isEmpty()) {
            nessunEventoTextView.visibility = View.VISIBLE
            recyclerView.visibility = View.GONE
        } else {
            nessunEventoTextView.visibility = View.GONE
            recyclerView.visibility = View.VISIBLE
        }
    }

    private fun eliminaEvento(evento: Event) {
        database.child(evento.id).removeValue().addOnCompleteListener {
            if (it.isSuccessful) {
                listaEventi.remove(evento)
                eventoAdapter.notifyDataSetChanged()
                aggiornaUI()
            } else {
                // Gestisci l'errore di eliminazione
            }
        }
    }
}

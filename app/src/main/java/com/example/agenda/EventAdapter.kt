package com.example.agenda

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class EventoAdapter(
    private val eventi: List<Event>,
    private val onDeleteClicked: (Event) -> Unit
) : RecyclerView.Adapter<EventoAdapter.EventoViewHolder>() {

    class EventoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nomeEvento: TextView = itemView.findViewById(R.id.nomeEvento)
        val dataEvento: TextView = itemView.findViewById(R.id.dataEvento)
        val orarioEvento: TextView = itemView.findViewById(R.id.orarioEvento)
        val bottoneElimina: Button = itemView.findViewById(R.id.bottoneElimina)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventoViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_evento_adapter, parent, false)
        return EventoViewHolder(view)
    }

    override fun onBindViewHolder(holder: EventoViewHolder, position: Int) {
        val evento = eventi[position]
        holder.nomeEvento.text = evento.nome
        holder.dataEvento.text = evento.data
        holder.orarioEvento.text = evento.orario

        holder.bottoneElimina.setOnClickListener {
            val currentUser = FirebaseAuth.getInstance().currentUser
            if (currentUser != null) {
                onDeleteClicked(evento)
            } else {
                // Mostra un messaggio di errore o richiedi il login
            }
        }
    }

    override fun getItemCount(): Int = eventi.size
}

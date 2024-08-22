package com.example.agenda

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.agenda.databinding.ActivityRegistrazioneBinding
import com.google.firebase.auth.FirebaseAuth

class Registrazione : AppCompatActivity() {

    private lateinit var binding: ActivityRegistrazioneBinding
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inizializzazione del binding e FirebaseAuth
        binding = ActivityRegistrazioneBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()

        binding.textView.setOnClickListener {
            val intent = Intent(this, Registrazione::class.java)
            startActivity(intent)
        }

        binding. bottoneRegistrati.setOnClickListener {
            val email = binding.email.text.toString()
            val pass = binding.password.text.toString()
            val confPass = binding.confermaPassword.text.toString()

            // Controllo che tutti i campi siano riempiti
            if (email.isNotEmpty() && pass.isNotEmpty() && confPass.isNotEmpty()) {
                // Controllo che le password corrispondano
                if (pass == confPass) {
                    // Creazione dell'utente con Firebase Authentication
                    firebaseAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener {
                        if (it.isSuccessful) {
                            val intent = Intent(this, Registrazione::class.java)
                            startActivity(intent)
                        } else {
                            Toast.makeText(this, it.exception.toString(), Toast.LENGTH_SHORT).show()
                        }
                    }
                } else {
                    Toast.makeText(this, "Le Password non corrispondono", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Tutti i campi sono obbligatori", Toast.LENGTH_SHORT).show()
            }
        }

        // Configurazione dell'interfaccia a schermo intero
        enableEdgeToEdge()
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}

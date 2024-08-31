package com.example.agenda

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
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

        binding = ActivityRegistrazioneBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()

        binding.textView.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java) // Cambiato per andare alla pagina di login
            startActivity(intent)
        }

        binding.bottoneRegistrati.setOnClickListener {
            val email = binding.email.text.toString()
            val pass = binding.password.text.toString()
            val confPass = binding.confermaPassword.text.toString()

           //controllo campi
            if (email.isNotEmpty() && pass.isNotEmpty() && confPass.isNotEmpty()) {
                // Controllo che le password corrispondano
                if (pass == confPass) {
                    // Creazione dell'utente con Firebase Authentication
                    firebaseAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener {
                        if (it.isSuccessful) {
                            val intent = Intent(this, MainActivity::class.java) // Porta alla MainActivity
                            startActivity(intent)
                            finish() // Chiude l'activity corrente in modo che non si possa tornare indietro con il pulsante "indietro"
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


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}


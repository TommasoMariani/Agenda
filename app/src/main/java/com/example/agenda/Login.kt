package com.example.agenda

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {

    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Inizializza FirebaseAuth
        firebaseAuth = FirebaseAuth.getInstance()

        val loginButton = findViewById<Button>(R.id.bottoneAccedi)
        val emailInput = findViewById<EditText>(R.id.email)
        val passwordInput = findViewById<EditText>(R.id.password)
        val registerText = findViewById<TextView>(R.id.textView) // Cambiato a TextView

        // Listener per passare alla schermata di registrazione
        registerText.setOnClickListener {
            val intent = Intent(this, Registrazione::class.java)
            startActivity(intent)
        }

        // Listener per il pulsante di login
        loginButton.setOnClickListener {
            val email = emailInput.text.toString().trim()
            val password = passwordInput.text.toString().trim()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                // Login con Firebase Auth
                firebaseAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            // Naviga alla MainActivity
                            val intent = Intent(this, MainActivity::class.java)
                            startActivity(intent)
                            finish() // Chiudi LoginActivity
                        } else {
                            // Mostra messaggio di errore
                            Toast.makeText(this, "Errore di autenticazione: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                        }
                    }
            } else {
                Toast.makeText(this, "Tutti i campi sono obbligatori!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onStart() {
        super.onStart()
        // Controlla se l'utente è già autenticato
        if (firebaseAuth.currentUser != null) {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}

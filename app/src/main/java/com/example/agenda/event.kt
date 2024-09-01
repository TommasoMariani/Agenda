package com.example.agenda

data class Event(
    val id: String = "",
    val nome: String = "",
    val data: String = "",
    val orario: String = "",
    val citta: String = "",
    val provincia: String = "",
    val via: String = "",
    val notificheAbilitate: Boolean = false
)

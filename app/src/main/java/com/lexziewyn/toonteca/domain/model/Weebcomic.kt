package com.lexziewyn.toonteca.domain.model

data class Weebcomic(
    val id: Int = 0,
    val titulo: String,
    val genero: String,
    val capitulosTotales: Int,
    val capitulosVistos: Int,
    val terminado: Boolean
)
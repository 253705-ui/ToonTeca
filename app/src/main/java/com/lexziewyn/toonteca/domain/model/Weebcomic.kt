package com.lexziewyn.toonteca.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Weebcomic(
    val id: String = "",
    val titulo: String,
    val genero: String,
    val capitulosTotales: Int,
    val capitulosVistos: Int,
    val terminado: Boolean
)
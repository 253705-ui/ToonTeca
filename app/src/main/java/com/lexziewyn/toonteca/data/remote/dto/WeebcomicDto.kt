package com.lexziewyn.toonteca.data.remote.dto

import com.google.gson.annotations.SerializedName

data class WeebcomicDto(
    @SerializedName("id") val id: Int? = null,
    @SerializedName("titulo") val titulo: String,
    @SerializedName("genero") val genero: String,
    @SerializedName("capitulosTotales") val capitulosTotales: Int,
    @SerializedName("capitulosVistos") val capitulosVistos: Int,
    @SerializedName("terminado") val terminado: Boolean
)
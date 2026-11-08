package com.lexziewyn.toonteca.data.mapper

import com.lexziewyn.toonteca.data.remote.dto.WeebcomicDto
import com.lexziewyn.toonteca.domain.model.Weebcomic

fun WeebcomicDto.toDomain(): Weebcomic {
    return Weebcomic(
        id = id ?: 0,
        titulo = titulo,
        genero = genero,
        capitulosTotales = capitulosTotales,
        capitulosVistos = capitulosVistos,
        terminado = terminado
    )
}

fun Weebcomic.toDto(): WeebcomicDto {
    return WeebcomicDto(
        id = if (id == 0) null else id,
        titulo = titulo,
        genero = genero,
        capitulosTotales = capitulosTotales,
        capitulosVistos = capitulosVistos,
        terminado = terminado
    )
}
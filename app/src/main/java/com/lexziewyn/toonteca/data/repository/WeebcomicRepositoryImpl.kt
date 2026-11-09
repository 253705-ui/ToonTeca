package com.lexziewyn.toonteca.data.repository

import com.lexziewyn.toonteca.data.remote.ToonTecaApi
import com.lexziewyn.toonteca.data.mapper.toDomain
import com.lexziewyn.toonteca.data.mapper.toDto
import com.lexziewyn.toonteca.domain.model.Weebcomic
import com.lexziewyn.toonteca.domain.repository.WeebcomicRepository

class WeebcomicRepositoryImpl(
    private val api: ToonTecaApi
) : WeebcomicRepository {

    override suspend fun getComics(): List<Weebcomic> {
        return api.getComics().map { it.toDomain() }
    }

    override suspend fun getComicById(id: Int): Weebcomic {
        return api.getComicById(id).toDomain()
    }

    override suspend fun insertComic(comic: Weebcomic): Weebcomic {
        return api.insertComic(comic.toDto()).toDomain()
    }

    override suspend fun updateComic(id: Int, comic: Weebcomic): Weebcomic {
        return api.updateComic(id, comic.toDto()).toDomain()
    }

    override suspend fun deleteComic(id: Int) {
        api.deleteComic(id)
    }
}